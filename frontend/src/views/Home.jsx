import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { get_user_info } from "../store/Reducers/authReducer";
import Loading from "./components/Loading";

const Home = () => {
  const { userInfo, loader } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserInfo = async () => {
      const token = localStorage.getItem("accessToken");
      if (token) {
        await dispatch(get_user_info());
        if (!userInfo.role) {
          navigate("/login", { replace: true });
        } else if (userInfo.role === "USER") {
          navigate("/customer/homepage", { replace: true });
        } else if (
          userInfo.role === "ADMIN" ||
          userInfo.role === "SUPER_ADMIN"
        ) {
          navigate("/admin/dashboard", { replace: true });
        }
      } else {
        navigate("/login", { replace: true });
      }
    };

    fetchUserInfo();
  }, [dispatch, navigate, userInfo.role]);

  if (loader) return <Loading />;

  return null;
};

export default Home;
