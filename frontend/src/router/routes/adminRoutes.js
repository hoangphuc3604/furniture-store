import { lazy } from "react";
const AdminDashboard = lazy(() => import("./../../views/admin/AdminDashboard"));
const AddProduct = lazy(() => import("./../../views/admin/AddProduct"));
const UserList = lazy(() => import("./../../views/admin/UserList"));
const ProductList = lazy(() => import("./../../views/admin/Product"));
const UserDetail = lazy(() => import("./../../views/admin/UserDetail"));
const EditProduct = lazy(() => import("./../../views/admin/EditProduct"));
const OrderList = lazy(() => import("./../../views/admin/OrderList"));

export const adminRoute = [
  {
    path: "/admin/dashboard",
    element: <AdminDashboard />,
    role: ["ADMIN", "SUPER_ADMIN"],
  },
  {
    path: "/admin/add-product",
    element: <AddProduct />,
    role: ["ADMIN", "SUPER_ADMIN"],
  },
  {
    path: "/admin/user-list",
    element: <UserList />,
    role: ["ADMIN", "SUPER_ADMIN"],
  },
  {
    path: "/admin/product-list",
    element: <ProductList />,
    role: ["ADMIN", "SUPER_ADMIN"],
  },
  {
    path: "/admin/products/:id",
    element: <EditProduct />,
    role: ["ADMIN", "SUPER_ADMIN"],
  },
  {
    path: "/admin/order-list",
    element: <OrderList />,
    role: ["ADMIN", "SUPER_ADMIN"],
  },
  {
    path: "/admin/users/:id",
    element: <UserDetail />,
    role: ["ADMIN", "SUPER_ADMIN"],
  },
];
