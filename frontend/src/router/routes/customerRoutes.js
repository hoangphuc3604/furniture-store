import { lazy } from "react";
import OrderHistory from "../../views/customer/OrderHistory";
const HomePage = lazy(() => import("../../views/customer/HomePage"));
const ProductList = lazy(() => import("../../views/customer/Shop"));
const Cart = lazy(() => import("./../../views/customer/Cart"));
const ProductDetails = lazy(() =>
  import("./../../views/customer/ProductDetails")
);
const UserProfile = lazy(() => import("./../../views/customer/UserProfile"));

export const customerRoute = [
  {
    path: "/customer/homepage",
    element: <HomePage />,
    role: ["USER"],
  },
  {
    path: "/customer/product-list",
    element: <ProductList />,
    role: ["USER"],
  },
  {
    path: "/customer/cart",
    element: <Cart />,
    role: ["USER"],
  },
  {
    path: "/customer/order-history",
    element: <OrderHistory />,
    role: ["USER"],
  },
  {
    path: "/customer/product/:id",
    element: <ProductDetails />,
    role: ["USER"],
  },
  {
    path: "/customer/user-profile",
    element: <UserProfile />,
    role: ["USER"],
  },
];
