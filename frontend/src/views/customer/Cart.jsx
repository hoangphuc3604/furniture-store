import React, { useState, useEffect } from "react";
import { FaSearch, FaTrash } from "react-icons/fa";
import { Input, Button, Table, Space, Typography, Checkbox, Modal } from "antd";
import { PlusOutlined, MinusOutlined } from "@ant-design/icons";
import { useDispatch, useSelector } from "react-redux";
import {
  delete_from_cart,
  get_cart,
  update_cart,
} from "./../../store/Reducers/cartReducer";
import { useNavigate } from "react-router-dom";
import { add_order } from "./../../store/Reducers/orderReducer";
import { toast } from "react-hot-toast";

const { Text } = Typography;

const Cart = () => {
  const [searchValue, setSearchValue] = useState("");
  const [selectAll, setSelectAll] = useState(false);
  const [filteredCart, setFilteredCart] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [shippingInfo, setShippingInfo] = useState({
    address: "",
    phone: "",
  });

  const { userInfo } = useSelector((state) => state.auth);
  const { cart } = useSelector((state) => state.cart);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const tempCart = JSON.parse(localStorage.getItem("cart")) || [];
    setFilteredCart(tempCart);
    if (userInfo.role) {
      dispatch(get_cart());
    }
  }, [dispatch, userInfo.role]);

  useEffect(() => {
    if (cart.length > 0) {
      setFilteredCart([...cart]);
    }
  }, [cart]);

  useEffect(() => {
    if (!userInfo.role) {
      localStorage.setItem("cart", JSON.stringify(filteredCart));
    }
  }, [filteredCart, userInfo.role]);

  const handleQuantityChange = (id, delta) => {
    const item = filteredCart.find((item) => item.product_id === id);
    if (item.amount + delta < 1) return;

    const updatedCart = filteredCart.map((item) =>
      item.product_id === id ? { ...item, amount: item.amount + delta } : item
    );
    setFilteredCart(updatedCart);
  };

  const handleRemoveItem = (id) => {
    const updatedCart = filteredCart.filter((item) => item.product_id !== id);
    dispatch(delete_from_cart(id));
    setFilteredCart(updatedCart);
  };

  const handleSelectItem = (id) => {
    const updatedCart = filteredCart.map((item) =>
      item.product_id === id ? { ...item, selected: !item.selected } : item
    );
    setFilteredCart(updatedCart);
  };

  const handleSelectAll = () => {
    const updatedSelectAll = !selectAll;
    setSelectAll(updatedSelectAll);
    const updatedCart = filteredCart.map((item) => ({
      ...item,
      selected: updatedSelectAll,
    }));
    setFilteredCart(updatedCart);
  };

  const handleBuyNow = () => {
    if (!userInfo.id) {
      navigate("/login", { state: { from: "/cart" } });
      return;
    }

    setIsModalVisible(true);
  };

  const handleShippingSubmit = () => {
    if (!shippingInfo.address) {
      toast.error("Please fill in all fields!");
      return;
    }

    const selectedItems = filteredCart.filter((item) => item.selected);
    console.log(selectedItems);
    const orderData = {
      created_customer_id: userInfo.id,
      address: shippingInfo.address,
      orderDetailDTOs: selectedItems.map((item) => ({
        product_id: item.product_id,
        amount: item.amount,
        price: item.price,
      })),
    };

    const remainingItems = filteredCart.filter((item) => !item.selected);
    dispatch(add_order(orderData));
    selectedItems.forEach((item) => {
      dispatch(delete_from_cart(item.product_id));
    });
    setFilteredCart([...remainingItems]);
    setIsModalVisible(false);
    toast.success("Order placed successfully!");
  };

  const displayedCart = filteredCart.filter((item) =>
    item?.name?.toLowerCase().includes(searchValue.toLowerCase())
  );

  const totalPrice = displayedCart.reduce(
    (total, item) => total + item.price * item.amount * (item.selected ? 1 : 0),
    0
  );

  const totalItems = displayedCart.filter((item) => item.selected).length;

  const columns = [
    {
      title: <Checkbox checked={selectAll} onChange={handleSelectAll} />,
      dataIndex: "selected",
      render: (text, record) => (
        <Checkbox
          checked={record.selected}
          onChange={() => handleSelectItem(record.id)}
        />
      ),
    },
    {
      title: "Product",
      dataIndex: "image",
      render: (text, record) => (
        <img
          src={record.image}
          alt={record.name}
          style={{ width: 60, height: 60, objectFit: "cover" }}
        />
      ),
    },
    {
      title: "Name",
      dataIndex: "name",
    },
    {
      title: "Price",
      dataIndex: "price",
      render: (text) => `$${text}`,
    },
    {
      title: "Quantity",
      dataIndex: "amount",
      render: (text, record) => (
        <Space>
          <Button
            icon={<MinusOutlined />}
            onClick={() => handleQuantityChange(record.product_id, -1)}
            size="small"
          />
          <Input
            value={text}
            readOnly
            style={{ width: 40, textAlign: "center" }}
          />
          <Button
            icon={<PlusOutlined />}
            onClick={() => handleQuantityChange(record.product_id, 1)}
            size="small"
          />
        </Space>
      ),
    },
    {
      title: "Total",
      dataIndex: "total",
      render: (text, record) => `$${record.price * record.amount}`,
    },
    {
      title: "Action",
      render: (text, record) => (
        <Button
          icon={<FaTrash />}
          onClick={() => handleRemoveItem(record.product_id)}
          type="text"
          danger
        />
      ),
    },
  ];

  return (
    <div className="px-2 lg:px-7 p-5 my-5 rounded shadow-md">
      <div className="flex justify-between items-center mb-5">
        <div className="flex items-center">
          <Input
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
            placeholder="Search products"
            prefix={<FaSearch />}
            style={{ width: 250, marginRight: 16 }}
          />
        </div>
      </div>

      <div className="flex flex-col gap-4">
        <Table
          columns={columns}
          dataSource={displayedCart}
          rowKey="product_id"
          pagination={false}
          scroll={{ x: 768 }}
        />
      </div>

      <div className="mt-5 p-4 border border-gray-300 rounded-md bg-white">
        <div className="flex justify-between items-center mb-3">
          <Checkbox checked={selectAll} onChange={handleSelectAll}>
            Select All
          </Checkbox>
          <div>
            <Text>Total Items: {totalItems}</Text>
          </div>
        </div>
        <div className="flex justify-between items-center">
          <Text strong>Total Price: ${totalPrice}</Text>
          <Button
            type="primary"
            size="large"
            disabled={totalItems === 0}
            onClick={handleBuyNow}
          >
            Buy Now
          </Button>
        </div>
      </div>

      <Modal
        title="Shipping Information"
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={[
          <Button key="back" onClick={() => setIsModalVisible(false)}>
            Cancel
          </Button>,
          <Button key="submit" type="primary" onClick={handleShippingSubmit}>
            Submit
          </Button>,
        ]}
      >
        <Space direction="vertical" style={{ width: "100%" }}>
          <label>Address</label>
          <Input
            placeholder="Enter your address"
            value={shippingInfo.address}
            onChange={(e) =>
              setShippingInfo({ ...shippingInfo, address: e.target.value })
            }
          />
          <label>Customer ID</label>
          <Input placeholder="Customer ID" value={userInfo.id} disabled />
        </Space>
      </Modal>
    </div>
  );
};

export default Cart;
