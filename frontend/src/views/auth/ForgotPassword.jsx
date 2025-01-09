import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  user_forgot_password,
  messageClear,
} from "../../store/Reducers/authReducer";
import { toast } from "react-hot-toast";
import { useNavigate } from "react-router-dom";

const ForgotPassword = () => {
  const [email, setEmail] = useState("");
  const [emailError, setEmailError] = useState("");

  const validate = () => {
    if (!email) {
      setEmailError("Email is required");
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      setEmailError("Email is invalid");
    } else {
      setEmailError("");
    }
  };

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loader, errorMessage, success } = useSelector((state) => state.auth);

  const inputHandle = (e) => {
    setEmail(e.target.value);
  };

  const submit = (e) => {
    e.preventDefault();
    validate();
    dispatch(user_forgot_password(email));
  };

  useEffect(() => {
    if (success) {
      toast.success("Reset link sent to your email");
      dispatch(messageClear());
      // navigate("/login");
    }

    if (errorMessage) {
      toast.error(errorMessage);
      dispatch(messageClear());
    }
  }, [success, errorMessage]);

  return (
    <div className="flex flex-col w-full md:w-1/2 xl:w-2/5 mx-auto p-8 bg-[#ffffff] rounded-2xl shadow-xl my-12">
      {/* Header */}
      <div className="flex flex-row items-center gap-3 pb-4">
        <button
          onClick={() => navigate(-1)}
          className="flex items-center justify-center w-10 h-10 bg-gray-200 rounded-full hover:bg-gray-300"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
            className="lucide lucide-arrow-left"
          >
            <line x1="19" y1="12" x2="5" y2="12"></line>
            <polyline points="12 19 5 12 12 5"></polyline>
          </svg>
        </button>
        <h1 className="text-3xl font-bold text-[#4B5563]">FurniStyle</h1>
      </div>
      <div className="text-sm font-light text-[#6B7280] pb-8">
        Give us your email address and we will send you a link to reset your
        password.
      </div>
      {/* Form */}
      <form className="flex flex-col" onSubmit={submit}>
        <div className="pb-4">
          <label
            htmlFor="email"
            className="block mb-2 text-sm font-medium text-[#111827]"
          >
            Email
          </label>
          <div className="relative">
            <input
              type="email"
              name="email"
              id="email"
              className={`pl-6 bg-gray-50 text-gray-600 border sm:text-sm rounded-lg block w-full p-3 focus:outline-none focus:ring-1 ${
                emailError
                  ? "border-red-500 focus:ring-red-500 bg-red-50"
                  : "border-gray-300 focus:ring-gray-400"
              }`}
              placeholder="Your Email"
              autoComplete="off"
              onChange={inputHandle}
              value={email}
              onBlur={validate}
            />
          </div>
          {emailError && (
            <span className="text-red-500 text-sm font-light">
              {emailError}
            </span>
          )}
        </div>
        <button
          type="submit"
          className="w-full text-white bg-[#4F46E5] hover:bg-[#4338ca] focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center mb-6"
        >
          Get Reset Link
        </button>
      </form>
    </div>
  );
};

export default ForgotPassword;
