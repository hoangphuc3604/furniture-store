import { AiOutlineDashboard, AiOutlineShoppingCart } from "react-icons/ai";
import { BiSolidCategory } from "react-icons/bi";
import { FaHome, FaHistory, FaUser, FaProductHunt, FaListAlt } from "react-icons/fa";

export const allNav = [
  {
    id: 1,
    title: "Dashboard",
    icon: <AiOutlineDashboard />,
    role: ["ADMIN", "SUPER_ADMIN"],
    path: "/admin/dashboard",
  },
  {
    id: 6,
    title: "User List",
    icon: <FaUser />,
    role: ["ADMIN", "SUPER_ADMIN"],
    path: "/admin/user-list",
  },
  {
    id: 8,
    title: "Home",
    icon: <FaHome />,
    role: ["USER"],
    path: "/customer/homepage",
  },
  {
    id: 9,
    title: "Product List",
    icon: <FaProductHunt />,
    role: ["USER"],
    path: "/customer/product-list",
  },
  {
    id: 10,
    title: "Cart",
    icon: <AiOutlineShoppingCart />,
    role: ["USER"],
    path: "/customer/cart",
  },
  {
    id: 12,
    title: "Account",
    icon: <FaUser />,
    role: ["USER"],
    path: "/customer/user-profile",
  },
  {
    id: 13,
    title: "Add Product",
    icon: <FaProductHunt />,
    role: ["ADMIN", "SUPER_ADMIN"],
    path: "/admin/add-product",
  },
  {
    id: 14,
    title: "Product List",
    icon: <FaProductHunt />,
    role: ["ADMIN", "SUPER_ADMIN"],
    path: "/admin/product-list",
  },
  {
    id: 15,
    title: "Order List",
    icon: <FaListAlt />,
    role: ["ADMIN", "SUPER_ADMIN"],
    path: "/admin/order-list",
  },
  {
    id: 16,
    title: "Order History",
    icon: <FaHistory />,
    role: ["USER"],
    path: "/customer/order-history",
  },
  {
    id: 17,
    title: "Revenue",
    icon: <BiSolidCategory />,
    role: ["SUPER_ADMIN"],
    path: "/sadmin/revenue",
  },
];
