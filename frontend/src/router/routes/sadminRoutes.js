import { lazy } from "react";
const AdminDashboard = lazy(() => import("./../../views/admin/AdminDashboard"));
const RevenueDashboard = lazy(() =>
  import("./../../views/sadmin/RevenueDashboard")
);

export const sadminRoute = [
  {
    path: "/sadmin/dashboard",
    element: <AdminDashboard />,
    role: ["SUPER_ADMIN"],
  },
  {
    path: "/sadmin/revenue",
    element: <RevenueDashboard />,
    role: ["SUPER_ADMIN"],
  },
];
