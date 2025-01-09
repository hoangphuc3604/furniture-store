import React, { useState, useEffect } from "react";
import { Line, Bar } from "react-chartjs-2";
import { Select, Button } from "antd";
import "antd/dist/reset.css";
import { useDispatch, useSelector } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { get_revennue_stats } from "./../../store/Reducers/statReducer";
import { toast } from "react-hot-toast";
import Loading from "./../components/Loading";
import Chart from "chart.js/auto";

const RevenueDashboard = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const [year, setYear] = useState(searchParams.get("year") || "2025");
  const [chartType, setChartType] = useState(
    searchParams.get("chartType") || "line"
  );

  const [chartData, setChartData] = useState({});

  const dispatch = useDispatch();

  const {
    loader,
    totalSales,
    totalOrders,
    totalUsers,
    totalProducts,
    errorMessage,
    chart,
  } = useSelector((state) => state.stat);

  useEffect(() => {
    setSearchParams({
      year,
      chartType,
    });
  }, [year, chartType, setSearchParams]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      dispatch(get_revennue_stats({ year }));
    }, 300);

    return () => clearTimeout(timeout);
  }, [year, dispatch]);

  useEffect(() => {
    const chartData = {
      labels: chart?.labels || [],
      datasets: [
        {
          label: "Revenue",
          data: chart?.data || [],
          backgroundColor: "rgba(75, 192, 192, 0.2)",
          borderColor: "rgba(75, 192, 192, 1)",
          borderWidth: 1,
        },
      ],
    };

    setChartData(chartData);
  }, [chart]);

  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }
  }, [errorMessage]);

  if (loader) {
    return <Loading />;
  } else {
    return (
      <div className="p-5 bg-gray-100 min-h-screen">
        <div className="bg-white shadow-md p-4 rounded-md">
          {/* Header */}
          <div className="flex flex-wrap justify-between items-center mb-5">
            <h1 className="text-xl font-bold w-full sm:w-auto">
              Revenue Statistics
            </h1>
            <div className="flex flex-wrap items-center space-x-4 mt-3 sm:mt-0">
              <Select
                value={year}
                onChange={(value) => setYear(value)}
                options={Array.from({ length: 10 }, (_, i) => {
                  const y = new Date().getFullYear() - i;
                  return { value: String(y), label: String(y) };
                })}
                placeholder="Select year"
                className="w-full sm:w-auto"
              />
              <Button
                type="primary"
                onClick={() => dispatch(get_revennue_stats({ year }))}
                className="w-full sm:w-auto sm:mt-0"
              >
                Apply
              </Button>
            </div>
          </div>

          {/* Cards */}
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mb-5">
            <div className="bg-blue-100 p-4 rounded-md shadow">
              <p className="text-sm font-semibold">Total Revenue</p>
              <p className="text-lg font-bold">{totalSales} VND</p>
            </div>
            <div className="bg-green-100 p-4 rounded-md shadow">
              <p className="text-sm font-semibold">Total Orders</p>
              <p className="text-lg font-bold">{totalOrders}</p>
            </div>
            <div className="bg-yellow-100 p-4 rounded-md shadow">
              <p className="text-sm font-semibold">Total Products</p>
              <p className="text-lg font-bold">{totalProducts}</p>
            </div>
            <div className="bg-red-100 p-4 rounded-md shadow">
              <p className="text-sm font-semibold">New User</p>
              <p className="text-lg font-bold">{totalUsers}</p>
            </div>
          </div>

          {/* Chart Section */}
          <div className="mb-5">
            <div className="flex flex-wrap justify-between items-center mb-3">
              <h2 className="text-lg font-bold w-full sm:w-auto">
                Revenue Chart
              </h2>
              <div className="flex space-x-4 mt-3 sm:mt-0">
                <Button
                  type={chartType === "line" ? "primary" : "default"}
                  onClick={() => setChartType("line")}
                  className="w-full sm:w-auto mb-2 sm:mb-0"
                >
                  Line Chart
                </Button>
                <Button
                  type={chartType === "bar" ? "primary" : "default"}
                  onClick={() => setChartType("bar")}
                  className="w-full sm:w-auto"
                >
                  Bar Chart
                </Button>
              </div>
            </div>
            {chartData?.datasets?.length ? (
              chartType === "line" ? (
                <Line data={chartData} />
              ) : (
                <Bar data={chartData} />
              )
            ) : (
              <Loading />
            )}
          </div>
        </div>
      </div>
    );
  }
};

export default RevenueDashboard;
