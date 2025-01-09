import React, { useEffect, useState } from "react";
import { user_register, messageClear } from "../../store/Reducers/authReducer";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";
import Loading from "../components/Loading";

const Register = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loader, errorMessage, success } = useSelector((state) => state.auth);

  const [state, setState] = useState({
    fullname: "",
    username: "",
    password: "",
    email: "",
    address: "",
    phone: "",
    date_of_birth: "",
    gender: "",
    role: "USER",
  });

  const [errors, setErrors] = useState({
    emailError: "",
    passwordError: "",
    phoneError: "",
    fullNameError: "",
    addressError: "",
    dateOfBirthError: "",
    genderError: "",
    usernameError: "",
  });

  const validate = () => {
    let valid = true;
    let passwordError = "";
    let emailError = "";
    let phoneError = "";
    let fullNameError = "";
    let addressError = "";
    let dateOfBirthError = "";
    let genderError = "";
    let usernameError = "";

    if (!state.password) {
      passwordError = "Password is required";
      valid = false;
    } else if (state.password.length < 6) {
      passwordError = "Password must be at least 6 characters";
      valid = false;
    } else {
      passwordError = "";
    }

    if (!state.username) {
      usernameError = "Username is required";
      valid = false;
    } else {
      usernameError = "";
    }

    if (!state.email) {
      emailError = "Email is required";
      valid = false;
    } else if (!/\S+@\S+\.\S+/.test(state.email)) {
      emailError = "Email is invalid";
      valid = false;
    } else {
      emailError = "";
    }

    if (!state.phone) {
      phoneError = "Phone is required";
      valid = false;
    } else if (state.phone.length < 10) {
      phoneError = "Phone must be at least 10 characters";
      valid = false;
    } else if (/[^0-9]/.test(state.phone)) {
      phoneError = "Phone must be a number";
      valid = false;
    } else {
      phoneError = "";
    }

    if (!state.fullname) {
      fullNameError = "Name is required";
      valid = false;
    } else {
      fullNameError = "";
    }

    if (!state.address) {
      addressError = "Address is required";
      valid = false;
    } else {
      addressError = "";
    }

    if (!state.date_of_birth) {
      dateOfBirthError = "Date of birth is required";
      valid = false;
    } else {
      dateOfBirthError = "";
    }

    if (!state.gender) {
      genderError = "Gender is required";
      valid = false;
    } else {
      genderError = "";
    }

    setErrors({
      passwordError,
      emailError,
      phoneError,
      fullNameError,
      addressError,
      dateOfBirthError,
      genderError,
      usernameError,
    });

    return valid;
  };

  const inputHandle = (e) => {
    setState({
      ...state,
      [e.target.name]: e.target.value,
    });
  };

  const submit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    dispatch(user_register(state));
  };

  useEffect(() => {
    if (success) {
      toast.success("Register successfully");
      dispatch(messageClear());
      navigate("/login");
    }

    if (errorMessage) {
      toast.error(errorMessage);
      dispatch(messageClear());
    }
  }, [success, errorMessage, dispatch, navigate]);

  if (loader) <Loading />;
  else {
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
          Sign up htmlFor an account on FurniStyle.
        </div>
        <form className="flex flex-col" onSubmit={submit}>
          <div className="pb-2">
            <label
              htmlFor="fullname"
              className="block mb-2 text-sm font-medium text-[#111827]"
            >
              Full Name
            </label>
            <div className="relative text-gray-400">
              <input
                type="text"
                name="fullname"
                id="fullname"
                className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                  errors.fullNameError
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                placeholder="Full Name"
                autoComplete="off"
                value={state.fullname}
                onChange={inputHandle}
                onBlur={validate}
              />
              {errors.fullNameError && (
                <span className="text-sm text-red-500">
                  {errors.fullNameError}
                </span>
              )}
            </div>
          </div>
          <div className="pb-2">
            <label
              htmlFor="email"
              className="block mb-2 text-sm font-medium text-[#111827]"
            >
              Email
            </label>
            <div className="relative text-gray-400">
              <input
                type="email"
                name="email"
                id="email"
                className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                  errors.emailError
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                placeholder="name@company.com"
                autoComplete="off"
                value={state.email}
                onChange={inputHandle}
                onBlur={validate}
              />
              {errors.emailError && (
                <span className="text-sm text-red-500">
                  {errors.emailError}
                </span>
              )}
            </div>
          </div>
          <div className="pb-2">
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
                  errors.usernameError
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                placeholder="Username"
                autoComplete="off"
                value={state.username}
                onChange={inputHandle}
                onBlur={validate}
              />
              {errors.usernameError && (
                <span className="text-sm text-red-500">
                  {errors.usernameError}
                </span>
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
                  errors.passwordError
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                autoComplete="new-password"
                aria-autocomplete="list"
                value={state.password}
                onChange={inputHandle}
                onBlur={validate}
              />
              {errors.passwordError && (
                <span className="text-sm text-red-500">
                  {errors.passwordError}
                </span>
              )}
            </div>
          </div>
          <div className="pb-6">
            <label
              htmlFor="address"
              className="block mb-2 text-sm font-medium text-[#111827]"
            >
              Address
            </label>
            <div className="relative text-gray-400">
              <input
                type="text"
                name="address"
                id="address"
                placeholder="Your Address"
                className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                  errors.addressError
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                autoComplete="off"
                aria-autocomplete="list"
                value={state.address}
                onChange={inputHandle}
                onBlur={validate}
              />
              {errors.addressError && (
                <span className="text-sm text-red-500">
                  {errors.addressError}
                </span>
              )}
            </div>
          </div>
          <div className="pb-6">
            <label
              htmlFor="phone"
              className="block mb-2 text-sm font-medium text-[#111827]"
            >
              Phone
            </label>
            <div className="relative text-gray-400">
              <input
                type="text"
                name="phone"
                id="phone"
                placeholder="Your Phone Number"
                className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                  errors.phoneError
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                autoComplete="off"
                aria-autocomplete="list"
                value={state.phone}
                onChange={inputHandle}
                onBlur={validate}
              />
              {errors.phoneError && (
                <span className="text-sm text-red-500">
                  {errors.phoneError}
                </span>
              )}
            </div>
          </div>

          <div className="pb-2">
            <label
              htmlFor="date_of_birth"
              className="block mb-2 text-sm font-medium text-[#111827]"
            >
              Date of Birth
            </label>
            <div className="relative text-gray-400">
              <input
                type="date"
                name="date_of_birth"
                id="date_of_birth"
                className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                  errors.dateOfBirthError
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                value={state.date_of_birth}
                onChange={inputHandle}
                onBlur={validate}
              />
              {errors.dateOfBirthError && (
                <span className="text-sm text-red-500">
                  {errors.dateOfBirthError}
                </span>
              )}
            </div>
          </div>
          {/* New gender field */}
          <div className="pb-6">
            <label
              htmlFor="gender"
              className="block mb-2 text-sm font-medium text-[#111827]"
            >
              Gender
            </label>
            <div className="relative text-gray-400">
              <select
                name="gender"
                id="gender"
                className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                  errors.genderError
                    ? "border-red-500 focus:ring-red-500 bg-red-50"
                    : "border-gray-300 focus:ring-gray-400"
                }`}
                value={state.gender}
                onChange={inputHandle}
                onBlur={validate}
              >
                <option value="">Select Gender</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
              </select>
              {errors.genderError && (
                <span className="text-sm text-red-500">
                  {errors.genderError}
                </span>
              )}
            </div>
          </div>

          <button
            type="submit"
            className="w-full text-[#FFFFFF] bg-[#4F46E5] focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center mb-6"
          >
            Sign Up
          </button>
          <div className="text-sm font-light text-[#6B7280] ">
            Already have an account?{" "}
            <a
              href="/login"
              className="font-medium text-[#4F46E5] hover:underline"
            >
              Login
            </a>
          </div>
        </form>
      </div>
    );
  }
};

export default Register;
