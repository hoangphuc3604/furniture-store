import { privateRoute } from "./privateRoutes";
import MainLayout from "../../layout/MainLayout";
import ProtectRoute from "./ProtectRoute";

export const getRoute = () => {
  privateRoute.forEach((route) => {
    route.element = <ProtectRoute route={route}>{route.element}</ProtectRoute>;
  });

  return {
    path: "/",
    element: <MainLayout />,
    children: privateRoute,
  };
};
