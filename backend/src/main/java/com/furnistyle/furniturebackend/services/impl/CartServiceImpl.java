package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.CartDetailDTO;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.CartDetailMapper;
import com.furnistyle.furniturebackend.models.CartDetail;
import com.furnistyle.furniturebackend.models.Token;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.models.embeddedid.CartDetailId;
import com.furnistyle.furniturebackend.repositories.CartRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.repositories.TokenRepository;
import com.furnistyle.furniturebackend.services.CartService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final TokenRepository tokenRepository;
    private final CartDetailMapper cartDetailMapper;

    @Override
    public List<CartDetailDTO> cartOfCurrentUser(String token) {
        Token tokens = tokenRepository.findByToken(token)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER));
        Long id = tokens.getUser().getId();
        return cartDetailMapper.toDTOs(cartRepository.findAllByOwner_Id(id));
    }

    @Override
    public void addToCart(String token, Long productId, Integer amount) {
        Token tokens = tokenRepository.findByToken(token)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER));
        User owner = tokens.getUser();
        CartDetail cartDetail = new CartDetail();
        cartDetail.setOwner(owner);
        cartDetail.setProduct(productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT)));
        cartDetail.setAmount(amount);
        cartDetail.setId(new CartDetailId(owner.getId(), productId));

        cartRepository.save(cartDetail);
    }

    @Override
    public void updateCartDetail(String token, Long productId, Integer amount) {
        Token tokens = tokenRepository.findByToken(token)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER));
        Long id = tokens.getUser().getId();
        CartDetail cartDetail = cartRepository.findById(new CartDetailId(id, productId))
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_CART_DETAIL));

        cartDetail.setAmount(amount);
        cartRepository.save(cartDetail);
    }

    @Override
    public void deleteCartDetail(String token, Long productId) {
        Token tokens = tokenRepository.findByToken(token)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER));
        Long id = tokens.getUser().getId();
        CartDetail cartDetail = cartRepository.findById(new CartDetailId(id, productId))
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_CART_DETAIL));
        cartRepository.delete(cartDetail);
    }
}
