import React from "react";
import { Spin } from "antd";

const Loading = ({ message = "Loading...", fullScreen = true }) => {
  return (
    <div
      className={`flex items-center justify-center ${
        fullScreen
          ? "fixed inset-0 bg-gray-100 bg-opacity-70 z-50"
          : "w-full h-full"
      }`}
    >
      <div className="text-center">
        <Spin size="large" />
        <p className="mt-60 text-gray-600 text-lg font-semibold">{message}</p>
      </div>
    </div>
  );
};

export default Loading;
