import { adminRoute } from "./adminRoutes";
import { customerRoute } from "./customerRoutes";
import { sadminRoute } from "./sadminRoutes";

export const privateRoute = [...adminRoute, ...customerRoute, ...sadminRoute];
