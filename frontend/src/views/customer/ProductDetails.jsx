import React, { useEffect, useState } from "react";
import { Row, Col, Card, Button, Carousel, Divider, InputNumber } from "antd";
import { ShoppingCartOutlined, ShoppingOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-hot-toast";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import {
  get_product,
  get_related_products,
} from "../../store/Reducers/productReducer";
import { add_to_cart } from "../../store/Reducers/cartReducer";
import ProductCard from "../util/ProductCard";

const ProductDetails = () => {
  const [quantity, setQuantity] = useState(1);
  const { id } = useParams();

  const { product, relatedProducts } = useSelector((state) => state.products);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { userInfo } = useSelector((state) => state.auth);

  useEffect(() => {
    dispatch(get_product(id));
    dispatch(get_related_products(id));
  }, [dispatch, id]);

  console.log(product);

  const handleAddToCart = () => {
    const cartItem = {
      id: product.id,
      quantity,
      name: product.name,
      price: product.price,
      image: product.product_images[0]?.image_link,
    };

    if (!userInfo.role) {
      const tempCart = JSON.parse(localStorage.getItem("cart")) || [];
      const existingItem = tempCart.find((item) => item.id === product.id);
      if (existingItem) {
        existingItem.quantity += quantity;
      } else {
        tempCart.push(cartItem);
      }
      localStorage.setItem("cart", JSON.stringify(tempCart));
      toast.success("Added to cart successfully. Please login to checkout.");
    } else {
      dispatch(add_to_cart({ ...cartItem }));
      toast.success("Added to cart successfully.");
    }
  };

  return (
    <div className="px-4 lg:px-10 py-8">
      <Row gutter={[16, 16]} className="bg-white shadow-md rounded-lg p-5">
        <Col xs={24} md={12}>
          <Carousel
            autoplay
            prevArrow={
              <LeftOutlined style={{ fontSize: "24px", color: "#000" }} />
            }
            nextArrow={
              <RightOutlined style={{ fontSize: "24px", color: "#000" }} />
            }
          >
            {product.product_images?.map((image, index) => (
              <div key={index}>
                <img
                  src={image?.image_link}
                  alt={`product-${index}`}
                  className="w-full h-96 object-cover rounded-md"
                />
              </div>
            ))}
          </Carousel>
        </Col>
        <Col xs={24} md={12}>
          <div className="text-start">
            <h1 className="text-2xl font-bold mb-4">{product.name}</h1>
            <p className="text-red-500 text-xl font-semibold mb-2">
              {new Intl.NumberFormat("vi-VN", {
                style: "currency",
                currency: "VND",
              }).format(product.price)}
            </p>
            <Divider />
            <p>
              <strong>Category:</strong> {product.category_id?.category_name}
            </p>
            <p>
              <strong>Material:</strong> {product.material_id?.material_name}
            </p>
            <p>
              <strong>Origin:</strong> {product.origin}
            </p>
            <p>
              <strong>Size:</strong> {product.size}
            </p>
            <p>
              <strong>Description:</strong> {product.description}
            </p>
            <div className="mt-5 flex flex-row items-baseline">
              <p className="mr-5">
                <strong>Quantity:</strong>
              </p>
              <InputNumber
                min={1}
                max={100}
                value={quantity}
                onChange={(value) => setQuantity(value || 1)}
                className="mb-4 w-full md:w-1/3"
              />
            </div>
            <div className="mt-1 flex gap-4">
              <Button
                type="primary"
                size="large"
                icon={<ShoppingCartOutlined />}
                className="flex-1"
                onClick={handleAddToCart}
              >
                Add to cart
              </Button>
            </div>
          </div>
        </Col>
      </Row>

      {/* Sản phẩm cùng danh mục */}
      <div className="mt-10">
        <h2 className="text-2xl font-bold mb-6">Related Products</h2>
        <Row gutter={[16, 16]}>
          {relatedProducts.map((product) => (
            <Col xs={24} sm={12} md={8} lg={6} key={product.id}>
              <ProductCard
                product={product}
                key={product.id}
                onClick={() => navigate(`/product/${product.id}`)}
              />
            </Col>
          ))}
        </Row>
      </div>
    </div>
  );
};

export default ProductDetails;
