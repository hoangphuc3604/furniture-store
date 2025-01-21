import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  get_order_history,
  clearMessage,
} from "../../store/Reducers/orderReducer";
import toast from "react-hot-toast";
import { Pagination } from "antd";
import "../../App.css";
import Loading from "./../components/Loading";

const OrderHistory = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const perPage = 10;

  const dispatch = useDispatch();
  const { orderHistory, success, errorMessage, loader } = useSelector(
    (state) => state.orders
  );

  const total = orderHistory?.length;

  useEffect(() => {
    dispatch(get_order_history());
  }, [dispatch]);

  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(clearMessage());
  }, [success, errorMessage, dispatch]);

  const getStatusClass = (status) => {
    switch (status) {
      case "PENDING":
        return "status-pending";
      case "PROCESSING":
        return "status-processing";
      case "SHIPPED":
        return "status-shipped";
      case "DELIVERED":
        return "status-delivered";
      case "CANCELLED":
        return "status-cancelled";
      default:
        return "";
    }
  };

  if (loader) {
    return <Loading />;
  }

  return (
    <div className="px-2 lg:px-7 pt-5">
      <div className="flex lg:hidden justify-between items-center mb-5 p-4 bg-[#6a5fdf] rounded-md">
        <h1 className="text-[#d0d2d6] font-semibold text-lg">Order History</h1>
      </div>

      <div className="flex flex-wrap w-full">
        <div className="w-full p-4">
          <div className="bg-white p-5 rounded-md shadow-md">
            <div className="flex flex-wrap justify-between items-center mb-4">
              <h2 className="text-lg font-semibold">Orders</h2>
            </div>

            {/* Display table on large screens */}
            <div className="hidden lg:block">
              <table className="min-w-full bg-white">
                <thead>
                  <tr>
                    <th className="py-2 px-4 border-b border-gray-200">No</th>
                    <th className="py-2 px-4 border-b border-gray-200">
                      Order ID
                    </th>
                    <th className="py-2 px-4 border-b border-gray-200">
                      Address
                    </th>
                    <th className="py-2 px-4 border-b border-gray-200">
                      Created Date
                    </th>
                    <th className="py-2 px-4 border-b border-gray-200">
                      Status
                    </th>
                    <th className="py-2 px-4 border-b border-gray-200">
                      Total Amount
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {orderHistory?.map((order, i) => (
                    <tr key={order.id}>
                      <td className="py-2 px-4 border-b border-gray-200 text-center">
                        {i + 1}
                      </td>
                      <td className="py-2 px-4 border-b border-gray-200 text-center">
                        {order.id}
                      </td>
                      <td className="py-2 px-4 border-b border-gray-200 text-center">
                        {order.address}
                      </td>
                      <td className="py-2 px-4 border-b border-gray-200 text-center">
                        {new Date(order.createdDate).toLocaleDateString()}
                      </td>
                      <td
                        className={`py-2 px-4 border-b border-gray-200 text-center ${getStatusClass(
                          order.status
                        )}`}
                      >
                        {order.status}
                      </td>
                      <td className="py-2 px-4 border-b border-gray-200 text-center">
                        {order.total_amount}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Display cards on small screens */}
            <div className="block lg:hidden">
              {orderHistory?.map((order, i) => (
                <div
                  key={order.id}
                  className="mb-4 p-4 border rounded-md shadow-sm hover:bg-gray-100 cursor-pointer"
                >
                  <div className="flex justify-between">
                    <span className="font-semibold">No:</span>
                    <span>{i + 1}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-semibold">Order ID:</span>
                    <span>{order.id}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-semibold">Address:</span>
                    <span>{order.address}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-semibold">Created Date:</span>
                    <span>
                      {new Date(order.createdDate).toLocaleDateString()}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-semibold">Status:</span>
                    <span>{order.status}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-semibold">Total Amount:</span>
                    <span>{order.total_amount}</span>
                  </div>
                </div>
              ))}
            </div>

            <div className="w-full flex justify-end mt-4 bottom-4 right-4">
              <Pagination
                current={currentPage}
                total={total}
                pageSize={perPage}
                onChange={(page) => setCurrentPage(page)}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderHistory;
