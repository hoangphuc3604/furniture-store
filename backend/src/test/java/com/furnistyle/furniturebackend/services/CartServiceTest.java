package com.furnistyle.furniturebackend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.furnistyle.furniturebackend.utils.Constants;

import java.util.List;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CartServiceTest {
    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private CartDetailMapper cartDetailMapper;

    @Autowired
    private CartService cartService;

    @Test
    void cartOfCurrentUser_WhenTokenInvalid_ThenThrowNotFoundException() {
        String token = "invalidToken";

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> cartService.cartOfCurrentUser(token)
        );

        assertEquals(Constants.Message.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    void cartOfCurrentUser_WhenTokenValid_ThenReturnCartDetails() {
        String token = "validToken";
        Token tokenEntity = Instancio.create(Token.class);
        User user = tokenEntity.getUser();
        List<CartDetail> cartDetails = Instancio.ofList(CartDetail.class).size(3).create();
        List<CartDetailDTO> cartDetailDTOs = Instancio.ofList(CartDetailDTO.class).size(3).create();

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        Mockito.when(cartRepository.findAllByOwner_Id(user.getId())).thenReturn(cartDetails);
        Mockito.when(cartDetailMapper.toDTOs(cartDetails)).thenReturn(cartDetailDTOs);

        List<CartDetailDTO> result = cartService.cartOfCurrentUser(token);

        assertEquals(cartDetailDTOs, result);
    }

    @Test
    void addToCart_WhenTokenInvalid_ThenThrowNotFoundException() {
        String token = "invalidToken";
        Long productId = 1L;
        Integer amount = 2;

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> cartService.addToCart(token, productId, amount)
        );

        assertEquals(Constants.Message.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    void addToCart_WhenProductNotFound_ThenThrowNotFoundException() {
        String token = "validToken";
        Long productId = 1L;
        Integer amount = 2;
        Token tokenEntity = Instancio.create(Token.class);

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> cartService.addToCart(token, productId, amount)
        );

        assertEquals(Constants.Message.NOT_FOUND_PRODUCT, exception.getMessage());
    }

    @Test
    void addToCart_WhenValidRequest_ThenSaveCartDetail() {
        String token = "validToken";
        Long productId = 1L;
        Integer amount = 2;
        Token tokenEntity = Instancio.create(Token.class);
        User user = tokenEntity.getUser();
        CartDetail cartDetail = Instancio.create(CartDetail.class);

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(cartDetail.getProduct()));

        cartService.addToCart(token, productId, amount);

        Mockito.verify(cartRepository).save(Mockito.any(CartDetail.class));
    }

    @Test
    void updateCartDetail_WhenCartDetailNotFound_ThenThrowNotFoundException() {
        String token = "validToken";
        Long productId = 1L;
        Integer amount = 2;
        Token tokenEntity = Instancio.create(Token.class);
        Long userId = tokenEntity.getUser().getId();

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        Mockito.when(cartRepository.findById(new CartDetailId(userId, productId))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> cartService.updateCartDetail(token, productId, amount)
        );

        assertEquals(Constants.Message.NOT_FOUND_CART_DETAIL, exception.getMessage());
    }

    @Test
    void updateCartDetail_WhenValidRequest_ThenUpdateCartDetail() {
        String token = "validToken";
        Long productId = 1L;
        Integer amount = 5;
        Token tokenEntity = Instancio.create(Token.class);
        Long userId = tokenEntity.getUser().getId();
        CartDetail cartDetail = Instancio.create(CartDetail.class);

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        Mockito.when(cartRepository.findById(new CartDetailId(userId, productId))).thenReturn(Optional.of(cartDetail));

        cartService.updateCartDetail(token, productId, amount);

        assertEquals(amount, cartDetail.getAmount());
        Mockito.verify(cartRepository).save(cartDetail);
    }

    @Test
    void deleteCartDetail_WhenCartDetailNotFound_ThenThrowNotFoundException() {
        String token = "validToken";
        Long productId = 1L;
        Token tokenEntity = Instancio.create(Token.class);
        Long userId = tokenEntity.getUser().getId();

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        Mockito.when(cartRepository.findById(new CartDetailId(userId, productId))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> cartService.deleteCartDetail(token, productId)
        );

        assertEquals(Constants.Message.NOT_FOUND_CART_DETAIL, exception.getMessage());
    }

    @Test
    void deleteCartDetail_WhenValidRequest_ThenDeleteCartDetail() {
        String token = "validToken";
        Long productId = 1L;
        Token tokenEntity = Instancio.create(Token.class);
        Long userId = tokenEntity.getUser().getId();
        CartDetail cartDetail = Instancio.create(CartDetail.class);

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        Mockito.when(cartRepository.findById(new CartDetailId(userId, productId))).thenReturn(Optional.of(cartDetail));

        cartService.deleteCartDetail(token, productId);

        Mockito.verify(cartRepository).delete(cartDetail);
    }
}
