import React, { useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import { Card, Input, Button, Select, Row, Col, Slider, Checkbox } from "antd";
import { useInView } from "react-intersection-observer";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useLocation } from "react-router-dom";
import {
  clearMessage,
  get_products,
} from "../../store/Reducers/productReducer";
import { toast } from "react-hot-toast";
import { get_categories } from "./../../store/Reducers/categoryReducer";
import { get_materials } from "../../store/Reducers/materialReducer";
import Loading from "./../components/Loading";

const { Option } = Select;

const Shop = () => {
  const { products, errorMessage, loader } = useSelector(
    (state) => state.products
  );
  const { categories } = useSelector((state) => state.categories);
  const { materials } = useSelector((state) => state.materials);

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const [searchValue, setSearchValue] = useState("");
  const [sortAttribute, setSortAttribute] = useState("name");
  const [priceRange, setPriceRange] = useState([0, 5000000]);
  const [selectedCategory, setSelectedCategory] = useState([]);
  const [selectedMaterial, setSelectedMaterial] = useState([]);

  const cate = categories.map((category) => category.category_name);
  const mate = materials.map((material) => material.material_name);

  const updateQueryParams = (key, value) => {
    const params = new URLSearchParams(location.search);
    if (value || value === 0) {
      params.set(key, value);
    } else {
      params.delete(key);
    }
    navigate({ search: params.toString() });
  };

  useEffect(() => {
    const params = new URLSearchParams(location.search);

    setSearchValue(params.get("search") || "");
    setPriceRange([
      Number(params.get("price_min") || 0),
      Number(params.get("price_max") || 5000000),
    ]);
    setSelectedCategory(params.get("category")?.split(",") || []);
    setSelectedMaterial(params.get("material")?.split(",") || []);
  }, [location.search]);

  useEffect(() => {
    dispatch(get_products({ currentPage: 1, perPage: 100 }));
    dispatch(get_categories());
    dispatch(get_materials());
  }, [dispatch]);

  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(clearMessage());
  }, [errorMessage, dispatch]);

  const handlePriceChange = (value) => {
    setPriceRange(value);
    updateQueryParams("price_min", value[0]);
    updateQueryParams("price_max", value[1]);
  };

  const handleCategoryChange = (values) => {
    setSelectedCategory(values);
    updateQueryParams("category", values.join(","));
  };

  const handleMaterialChange = (values) => {
    setSelectedMaterial(values);
    updateQueryParams("material", values.join(","));
  };

  // const resetFilters = () => {
  //   navigate({ search: "" });
  //   setSearchValue("");
  //   setPriceRange([0, 5000000]);
  //   setSelectedCategory([]);
  //   setSelectedMaterial([]);
  // };

  const filteredProducts = products
    .filter((product) =>
      product.name.toLowerCase().includes(searchValue.toLowerCase())
    )
    .filter(
      (product) =>
        product.price >= priceRange[0] && product.price <= priceRange[1]
    )
    .filter(
      (product) =>
        selectedCategory.length === 0 ||
        selectedCategory.includes(product?.category_id?.category_name)
    )
    .filter(
      (product) =>
        selectedMaterial.length === 0 ||
        selectedMaterial.includes(product?.material_id?.material_name)
    )
    .sort((a, b) => {
      if (sortAttribute === "name") return a.name.localeCompare(b.name);
      if (sortAttribute === "price") return a.price - b.price;
      return 0;
    });

  // if (loader) return <Loading />;

  return (
    <div className="px-2 lg:px-7 p-5 bg-slate-100">
      {/* Bộ lọc */}
      <div className="mb-5 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 p-2">
        <div>
          <h4 className="font-semibold">Price Range</h4>
          <Slider
            range
            defaultValue={[0, 5000000]}
            min={0}
            max={10000000}
            step={100000}
            onChange={handlePriceChange}
          />
          <p>
            {new Intl.NumberFormat("vi-VN", {
              style: "currency",
              currency: "VND",
            }).format(priceRange[0])}{" "}
            -{" "}
            {new Intl.NumberFormat("vi-VN", {
              style: "currency",
              currency: "VND",
            }).format(priceRange[1])}
          </p>
        </div>

        <div>
          <h4 className="font-semibold">Category</h4>
          <Checkbox.Group
            options={cate}
            value={selectedCategory}
            onChange={handleCategoryChange}
          />
        </div>

        <div>
          <h4 className="font-semibold">Material</h4>
          <Checkbox.Group
            options={mate}
            value={selectedMaterial}
            onChange={handleMaterialChange}
          />
        </div>
      </div>

      {/* Thanh tìm kiếm và sắp xếp */}
      <div className="flex justify-between items-center mb-5">
        <div className="flex items-center">
          <Input
            value={searchValue}
            onChange={(e) => {
              setSearchValue(e.target.value);
              updateQueryParams("search", e.target.value);
            }}
            placeholder="Search..."
            addonAfter={<Button type="text" icon={<FaSearch />} />}
            style={{ width: 300 }}
          />
        </div>
        <div>
          <label htmlFor="sort" className="mr-2">
            Sort by:
          </label>
          <Select
            id="sort"
            value={sortAttribute}
            onChange={(value) => setSortAttribute(value)}
            style={{ width: 150 }}
          >
            <Option value="name">Name</Option>
            <Option value="price">Price</Option>
          </Select>
        </div>
      </div>

      {/* Danh sách sản phẩm */}
      <Row gutter={[16, 16]}>
        {filteredProducts.map((product) => (
          <LazyProductCard key={product.id} product={product} />
        ))}
      </Row>
    </div>
  );
};

const LazyProductCard = ({ product }) => {
  const { ref, inView } = useInView({
    triggerOnce: true,
    threshold: 0.1,
  });
  const navigate = useNavigate();

  return (
    <Col xs={24} sm={12} md={8} lg={6} ref={ref}>
      {inView && (
        <Card
          hoverable
          cover={
            <img
              alt={product.name}
              src={product.product_images[0]?.image_link}
              style={{ height: 200, objectFit: "cover" }}
            />
          }
          onClick={() => navigate(`/customer/product/${product.id}`)}
        >
          <div style={{ textAlign: "start" }}>
            <h3
              style={{
                margin: "10px 0",
                fontSize: "1.1rem",
                fontWeight: "bold",
              }}
            >
              {product.name}
            </h3>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
              }}
            >
              <p
                style={{
                  margin: 0,
                  fontWeight: "bold",
                  fontSize: "1.1rem",
                  color: "#ff4d4f",
                }}
              >
                {new Intl.NumberFormat("vi-VN", {
                  style: "currency",
                  currency: "VND",
                }).format(product.price)}
              </p>
              <p
                style={{
                  margin: 0,
                  fontSize: "0.85rem",
                  color: "#555",
                  whiteSpace: "nowrap",
                }}
              >
                {`Origin: ${product.origin}`}
              </p>
            </div>
          </div>
        </Card>
      )}
    </Col>
  );
};

export default Shop;
