import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { user_login, messageClear } from "../../store/Reducers/authReducer";
import { toast } from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import Loading from "./../components/Loading";

const Login = () => {
  const [state, setState] = useState({
    username: "",
    password: "",
  });
  const [errors, setErrors] = useState({
    password: "",
    username: "",
  });

  const validate = () => {
    let valid = true;
    let passwordError = "";
    let usernameError = "";

    if (!state.password) {
      passwordError = "Password is required";
      valid = false;
    } else if (state.password.length < 6) {
      passwordError = "Password must be at least 6 characters";
      valid = false;
    }

    if (!state.username) {
      usernameError = "Username is required";
      valid = false;
    } else {
      usernameError = "";
    }

    setErrors({
      password: passwordError,
      username: usernameError,
    });

    return valid;
  };

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loader, errorMessage, success } = useSelector((state) => state.auth);

  const inputHandle = (e) => {
    const { name, value } = e.target;

    setState({
      ...state,
      [name]: value,
    });

    let error = "";
    if (name === "password") {
      if (!value) {
        error = "Password is required";
      } else if (value.length < 6) {
        error = "Password must be at least 6 characters";
      }
    }

    if (name === "username") {
      if (!value) {
        error = "Username is required";
      }
    }

    setErrors({
      ...errors,
      [name]: error,
    });
  };

  const submit = (e) => {
    e.preventDefault();
    if (!validate()) {
      return;
    }
    dispatch(user_login(state));
  };

  useEffect(() => {
    if (success) {
      toast.success("Login Successful");
      dispatch(messageClear());
      navigate("/");
    }

    if (errorMessage) {
      toast.error(errorMessage);
      dispatch(messageClear());
    }
  }, [success, errorMessage, dispatch, navigate]);

  if (loader) {
    return <Loading />;
  } else {
    return (
      <div className="flex flex-col w-full md:w-1/2 xl:w-2/5 2xl:w-2/5 3xl:w-1/3 mx-auto p-8 md:p-10 2xl:p-12 3xl:p-14 bg-[#ffffff] rounded-2xl shadow-xl">
        <div className="flex flex-row gap-3 pb-4">
          <div>
            <img src="/images/export.svg" alt="Logo" width="50" />
          </div>
          <h1 className="text-3xl font-boldtext-[#4B5563] my-auto">
            FurniStyle
          </h1>
        </div>
        <div className="text-sm font-light text-[#6B7280] pb-8 ">
          Login to your account on FurniStyle.
        </div>
        <form className="flex flex-col" onSubmit={submit}>
          <div className="pb-6">
            <label
              htmlFor="username"
              className="block mb-2 text-sm font-medium text-[#111827]"
            >
              Username
            </label>
            <div className="relative text-gray-400">
              <input
                type="text"
                name="username"
                id="username"
                className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                  errors.username
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                placeholder="Your Username"
                autoComplete="off"
                aria-autocomplete="list"
                onChange={inputHandle}
                value={state.username}
              />
              {errors.username && (
                <span className="text-sm text-red-500">{errors.username}</span>
              )}
            </div>
          </div>
          <div className="pb-6">
            <label
              htmlFor="password"
              className="block mb-2 text-sm font-medium text-[#111827]"
            >
              Password
            </label>
            <div className="relative text-gray-400">
              <input
                type="password"
                name="password"
                id="password"
                placeholder="••••••••••"
                className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                  errors.password
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                autoComplete="new-password"
                aria-autocomplete="list"
                onChange={inputHandle}
                value={state.password}
                onBlur={validate}
              />
              {errors.password && (
                <span className="text-sm text-red-500">{errors.password}</span>
              )}
            </div>
          </div>
          <button
            type="submit"
            className="w-full text-[#FFFFFF] bg-[#4F46E5] focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center mb-6"
          >
            Login
          </button>
          <div className="text-sm font-light text-[#6B7280] ">
            Don't have an accout yet?{" "}
            <a
              href="/register"
              className="font-medium text-[#4F46E5] hover:underline"
            >
              Sign Up
            </a>
          </div>
          <div className="text-sm font-light text-[#6B7280] ">
            Forgot your password?{" "}
            <a
              href="/forgot-password"
              className="font-medium text-[#4F46E5] hover:underline"
            >
              Reset Password
            </a>
          </div>
        </form>
      </div>
    );
  }
};

export default Login;
