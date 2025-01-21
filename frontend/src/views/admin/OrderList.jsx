import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  clearMessage,
  get_orders_by_status,
  update_order,
  confirm_order,
} from "../../store/Reducers/orderReducer";
import toast from "react-hot-toast";
import { Pagination, Select } from "antd";
import Loading from "./../components/Loading";
import "../../App.css";

const OrderList = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [orderStatus, setOrderStatus] = useState("PENDING");
  const perPage = 10;

  const dispatch = useDispatch();
  const { orders, success, errorMessage, loader } = useSelector(
    (state) => state.orders
  );
  const { userInfo } = useSelector((state) => state.auth);

  const total = orders?.length;

  useEffect(() => {
    dispatch(get_orders_by_status(orderStatus));
  }, [dispatch, orderStatus]);

  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }
    if (success) {
      toast.success("Order status updated successfully!");
    }
    dispatch(clearMessage());
  }, [success, errorMessage, dispatch]);

  const handleStatusChange = (orderId, newStatus) => {
    dispatch(update_order({ order_id: orderId, status: newStatus }));
    setTimeout(() => {
      dispatch(get_orders_by_status(orderStatus));
    }, 500);
  };

  const handleConfirmOrder = (orderId, adminId) => {
    dispatch(confirm_order({ order_id: orderId, admin_id: adminId }));
    setTimeout(() => {
      dispatch(get_orders_by_status(orderStatus));
    }, 2000);
  };

  const statusOptions = [
    { value: "PENDING", label: "Pending" },
    { value: "PROCESSING", label: "Processing" },
    { value: "SHIPPED", label: "Shipped" },
    { value: "DELIVERED", label: "Delivered" },
    { value: "CANCELLED", label: "Cancelled" },
  ];

  if (loader) {
    return <Loading />;
  } else {
    return (
      <div className="px-2 lg:px-7 pt-5">
        <div className="flex lg:hidden justify-between items-center mb-5 p-4 bg-[#6a5fdf] rounded-md">
          <h1 className="text-[#d0d2d6] font-semibold text-lg">Order List</h1>
        </div>

        <div className="flex flex-wrap w-full">
          <div className="w-full p-4">
            <div className="bg-white p-5 rounded-md shadow-md">
              <div className="flex flex-wrap justify-between items-center mb-4">
                <h2 className="text-lg font-semibold">Orders</h2>
                <Select
                  value={orderStatus}
                  onChange={(value) => setOrderStatus(value)}
                  options={statusOptions}
                  className="w-full lg:w-1/3"
                />
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
                      <th className="py-2 px-4 border-b border-gray-200">
                        Confirmed Admin
                      </th>
                      <th className="py-2 px-4 border-b border-gray-200">
                        Created Customer
                      </th>
                      <th className="py-2 px-4 border-b border-gray-200">
                        Action
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders?.map((order, i) => (
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
                        <td className="py-2 px-4 border-b border-gray-200 text-center">
                          <Select
                            value={order.status}
                            onChange={(value) =>
                              handleStatusChange(order.id, value)
                            }
                            options={statusOptions}
                            className="w-full"
                          />
                        </td>
                        <td className="py-2 px-4 border-b border-gray-200 text-center">
                          {order.total_amount}
                        </td>
                        <td className="py-2 px-4 border-b border-gray-200 text-center">
                          {order.confirmed_admin_id}
                        </td>
                        <td className="py-2 px-4 border-b border-gray-200 text-center">
                          {order.created_customer_id}
                        </td>
                        <td className="py-2 px-4 border-b border-gray-200 text-center">
                          <button
                            onClick={() =>
                              handleConfirmOrder(order.id, userInfo.id)
                            }
                            className="bg-[#6a5fdf] text-white px-3 py-1 rounded-md"
                          >
                            Confirm
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {/* Display cards on small screens */}
              <div className="block lg:hidden">
                {orders?.map((order, i) => (
                  <div
                    key={order.id}
                    onClick={() =>
                      (window.location.href = `/admin/orders/${order.id}`)
                    }
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
                      <Select
                        value={order.status}
                        onChange={(value) =>
                          handleStatusChange(order.id, value)
                        }
                        options={statusOptions}
                        className="w-full"
                      />
                    </div>
                    <div className="flex justify-between">
                      <span className="font-semibold">Total Amount:</span>
                      <span>{order.total_amount}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="font-semibold">Confirmed Admin:</span>
                      <span>{order.confirmed_admin_id}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="font-semibold">Created Customer:</span>
                      <span>{order.created_customer_id}</span>
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
  }
};

export default OrderList;
