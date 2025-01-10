import React from "react";
import { Card } from "antd";
import { useNavigate } from "react-router-dom";

const LazyProductCard = ({ product }) => {
  const navigate = useNavigate();

  return (
    <Card
      hoverable
      cover={
        <img
          alt={product.name}
          src={product.product_images[0]?.image_link}
          style={{ height: 200, objectFit: "cover" }}
        />
      }
      onClick={() => navigate(`/customer/product/${product.id}`)}
    >
      <div style={{ textAlign: "start" }}>
        <h3
          style={{
            margin: "10px 0",
            fontSize: "1.1rem",
            fontWeight: "bold",
          }}
        >
          {product.name}
        </h3>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <p
            style={{
              margin: 0,
              fontWeight: "bold",
              fontSize: "1.1rem",
              color: "#ff4d4f",
            }}
          >
            {new Intl.NumberFormat("vi-VN", {
              style: "currency",
              currency: "VND",
            }).format(product.price)}
          </p>
          <p
            style={{
              margin: 0,
              fontSize: "0.85rem",
              color: "#555",
              whiteSpace: "nowrap",
            }}
          >
            {`Origin: ${product.origin}`}
          </p>
        </div>
      </div>
    </Card>
  );
};

export default LazyProductCard;
