import React, { useEffect, useState } from "react";
import { MdCurrencyYen, MdOutlineShoppingCart } from "react-icons/md";
import { FaUser } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-hot-toast";
import {
  clearMessage,
  get_revennue_stats,
} from "../../store/Reducers/statReducer";
import { get_recent_orders } from "../../store/Reducers/orderReducer";
import Loading from "./../components/Loading";

const AdminDashboard = () => {
  const [unit, setUnit] = useState("VND");
  const [s, setSales] = useState(0);
  const [p, setProducts] = useState(0);
  const [u, setUsers] = useState(0);
  const { recentOrders } = useSelector((state) => state.orders);

  const dispatch = useDispatch();

  const { loader, totalSales, totalProducts, totalUsers, errorMessage } =
    useSelector((state) => state.stat);

  useEffect(() => {
    dispatch(get_recent_orders());
    dispatch(get_revennue_stats({ year: new Date().getFullYear() }));
  }, [dispatch]);

  useEffect(() => {
    setProducts(totalProducts);
    setUsers(totalUsers);

    if (totalSales > 1000000000) {
      setUnit("B");
      setSales(totalSales / 1000000000);
    } else if (totalSales > 1000000) {
      setUnit("M");
      setSales(totalSales / 1000000);
    } else if (totalSales > 1000) {
      setUnit("K");
      setSales(totalSales / 1000);
    } else {
      setSales(totalSales);
    }
  }, [totalSales, totalProducts, totalUsers]);

  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(clearMessage());
  }, [errorMessage, dispatch]);

  if (loader) {
    return <Loading />;
  }

  return (
    // Main dashboard
    <div className="px-2 md:px-7 py-5">
      {/* // Summary tabs */}
      <div className="w-full grid grid-cols-1 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-4 gap-7">
        {/* // Total sales */}
        <div className="flex justify-between items-center p-5 bg-[#fae8e8] rounded-md gap-3">
          <div className="flex flex-col justify-start items-start text-[#5C5A5A]">
            <h2 className="font-bold text-3xl">
              {s}
              {unit}
            </h2>
            <span className="text-md font-medium">Total sales</span>
          </div>

          <div
            className="w-[40px] h-[47px] rounded-full bg-[#FF0305] flex justify-center
                    items-center text-xl"
          >
            <MdCurrencyYen className="text-[white] shadow-lg" />
          </div>
        </div>

        {/* //Products */}
        <div className="flex justify-between items-center p-5 bg-[#FDE2FF] rounded-md gap-3">
          <div className="flex flex-col justify-start items-start text-[#5C5A5A]">
            <h2 className="font-bold text-3xl">{p}</h2>
            <span className="text-md font-medium">Products</span>
          </div>

          <div
            className="w-[40px] h-[47px] rounded-full bg-[#7E0080] flex justify-center
                    items-center text-xl"
          >
            <MdOutlineShoppingCart className="text-[white] shadow-lg" />
          </div>
        </div>

        {/* // Orders
        <div className="flex justify-between items-center p-5 bg-[#ECEBFF] rounded-md gap-3">
          <div className="flex flex-col justify-start items-start text-[#5C5A5A]">
            <h2 className="font-bold text-3xl">{orders}</h2>
            <span className="text-md font-medium">Orders</span>
          </div>

          <div
            className="w-[40px] h-[47px] rounded-full bg-[#0200FD] flex justify-center
                    items-center text-xl"
          >
            <RiShoppingCartFill className="text-[white] shadow-lg" />
          </div>
        </div> */}

        {/* // Customers */}
        <div className="flex justify-between items-center p-5 bg-[#E6F9FF] rounded-md gap-3">
          <div className="flex flex-col justify-start items-start text-[#5C5A5A]">
            <h2 className="font-bold text-3xl">{u}</h2>
            <span className="text-md font-medium">Users</span>
          </div>

          <div
            className="w-[40px] h-[47px] rounded-full bg-[#0080FF] flex justify-center
                    items-center text-xl"
          >
            <FaUser className="text-[white] shadow-lg" />
          </div>
        </div>
      </div>

      {/* // Orders table */}
      <div className="w-full p-4 bg-white rounded-md mt-6">
        {/* // Header  */}
        <div className="flex justify-between items-center pr-4">
          <h2 className="font-semibold text-lg text-black pb-3">
            Recent Orders
          </h2>
          <Link
            to={"/admin/order-list"}
            className="font-semibold text-sm text-black"
          >
            View All
          </Link>
        </div>

        {/* // Table */}
        <div className="relative overflow-x-auto">
          <table className="w-full text-sm text-center text-black">
            <thead className="text-sm text-black uppercase border-b border-gray-300">
              <tr>
                <th scope="col" className="py-3 px-4">
                  Order Id
                </th>
                <th scope="col" className="py-3 px-4">
                  Customer ID
                </th>
                <th scope="col" className="py-3 px-4">
                  Status
                </th>
                <th scope="col" className="py-3 px-4">
                  Created Date
                </th>
                <th scope="col" className="py-3 px-4">
                  Confirmed Admin ID
                </th>
              </tr>
            </thead>

            <tbody>
              {recentOrders.map((order, i) => (
                <tr key={i}>
                  <td className="py-3 px-4 font-medium whitespace-nowrap">
                    {order.id}
                  </td>
                  <td className="py-3 px-4 font-medium whitespace-nowrap">
                    {order.created_customer_id}
                  </td>
                  <td className="py-3 px-4 font-medium whitespace-nowrap">
                    {order.status}
                  </td>
                  <td className="py-3 px-4 font-medium whitespace-nowrap">
                    {new Date(order.createdDate).toLocaleDateString()}
                  </td>
                  <td className="py-3 px-4 font-medium whitespace-nowrap">
                    {order.confirmed_admin_id}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
