import { lazy } from "react";
const Login = lazy(() => import("./../../views/auth/Login"));
const Register = lazy(() => import("./../../views/auth/Register"));
const ForgotPassword = lazy(() => import("./../../views/auth/ForgotPassword"));
const Home = lazy(() => import("./../../views/Home"));
const ErrorPage = lazy(() => import("./../../views/components/ErrorPage"));

const publicRoutes = [
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />,
  },
  {
    path: "/forgot-password",
    element: <ForgotPassword />,
  },
  {
    path: "/unauthorized",
    element: (
      <ErrorPage
        status="403"
        title="Unauthorized"
        subTitle="You are not authorized to access this page"
        onButtonClick={() => {
          window.location.href = "/";
        }}
      />
    ),
  },
  {
    path: "/not-found",
    element: (
      <ErrorPage
        onButtonClick={() => {
          window.location.href = "/";
        }}
      />
    ),
  },
];

export default publicRoutes;
