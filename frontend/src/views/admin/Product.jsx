import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  clearMessage,
  get_products,
} from "../../store/Reducers/productReducer";
import toast from "react-hot-toast";
import { Pagination } from "antd";
import { useNavigate } from "react-router-dom";
import Loading from "./../components/Loading";

const Products = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const perPage = 10;

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loader, products, success, errorMessage, totalPage } = useSelector(
    (state) => state.products
  );

  useEffect(() => {
    dispatch(get_products({ currentPage, perPage }));
  }, [dispatch, currentPage]);

  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }

    dispatch(clearMessage());
  }, [success, errorMessage, dispatch]);

  if (loader) {
    return <Loading />;
  } else {
    return (
      <div className="px-2 lg:px-7 pt-5">
        <div className="flex lg:hidden justify-between items-center mb-5 p-4 bg-[#6a5fdf] rounded-md">
          <h1 className="text-[#d0d2d6] font-semibold text-lg">Product List</h1>
        </div>

        <div className="flex flex-wrap w-full">
          <div className="w-full p-4">
            <div className="bg-white p-5 rounded-md shadow-md">
              <div className="flex flex-wrap">
                <div className="w-full">
                  <h2 className="text-lg font-semibold mb-4">Products</h2>

                  {/* Table for larger screens */}
                  <div className="hidden lg:block">
                    <table className="min-w-full bg-white">
                      <thead>
                        <tr>
                          <th className="py-2 px-4 border-b border-gray-200">
                            ID
                          </th>
                          <th className="py-2 px-4 border-b border-gray-200">
                            Name
                          </th>
                          <th className="py-2 px-4 border-b border-gray-200">
                            Price
                          </th>
                          <th className="py-2 px-4 border-b border-gray-200">
                            Category
                          </th>
                          <th className="py-2 px-4 border-b border-gray-200">
                            Material
                          </th>
                          <th className="py-2 px-4 border-b border-gray-200">
                            Origin
                          </th>
                          <th className="py-2 px-4 border-b border-gray-200">
                            Size
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        {products.map((product) => (
                          <tr
                            key={product.id}
                            onClick={() => {
                              navigate(`/admin/products/${product.id}`);
                            }}
                            className="cursor-pointer hover:bg-gray-100"
                          >
                            <td className="py-2 px-4 border-b border-gray-200 text-center">
                              {product.id}
                            </td>
                            <td className="py-2 px-4 border-b border-gray-200 text-center">
                              {product.name}
                            </td>
                            <td className="py-2 px-4 border-b border-gray-200 text-center">
                              {product.price}
                            </td>
                            <td className="py-2 px-4 border-b border-gray-200 text-center">
                              {product.category_id.category_name}
                            </td>
                            <td className="py-2 px-4 border-b border-gray-200 text-center">
                              {product.material_id.material_name}
                            </td>
                            <td className="py-2 px-4 border-b border-gray-200 text-center">
                              {product.origin}
                            </td>
                            <td className="py-2 px-4 border-b border-gray-200 text-center">
                              {product.size}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>

                  {/* Cards for smaller screens */}
                  <div className="block lg:hidden">
                    {products.map((product) => (
                      <div
                        key={product.id}
                        className="mb-4 p-4 border rounded-md shadow-md bg-gray-50"
                      >
                        <p className="font-semibold">
                          <span className="text-gray-600">ID:</span>{" "}
                          {product.id}
                        </p>
                        <p className="font-semibold">
                          <span className="text-gray-600">Name:</span>{" "}
                          {product.name}
                        </p>
                        <p className="font-semibold">
                          <span className="text-gray-600">Price:</span>{" "}
                          {product.price}
                        </p>
                        <p className="font-semibold">
                          <span className="text-gray-600">Category:</span>{" "}
                          {product.category}
                        </p>
                        <p className="font-semibold">
                          <span className="text-gray-600">Material:</span>{" "}
                          {product.material}
                        </p>
                        <p className="font-semibold">
                          <span className="text-gray-600">Origin:</span>{" "}
                          {product.origin}
                        </p>
                        <p className="font-semibold">
                          <span className="text-gray-600">Size:</span>{" "}
                          {product.size}
                        </p>
                      </div>
                    ))}
                  </div>

                  <div className="w-full flex justify-end mt-4 bottom-4 right-4">
                    <Pagination
                      current={currentPage}
                      total={products.length * totalPage}
                      pageSize={perPage}
                      onChange={(page) => setCurrentPage(page)}
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
};

export default Products;
