import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Table, Tag, Typography, Button, Modal, Select } from "antd";
import { get_orders_by_status } from "../../store/Reducers/orderReducer";

const { Title, Text } = Typography;
const { Option } = Select;

const OrderHistory = () => {
  const { orders } = useSelector((state) => state.orders);
  const { userInfo } = useSelector((state) => state.auth);
  const dispatch = useDispatch();

  const [selectedOrder, setSelectedOrder] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedStatus, setSelectedStatus] = useState("PENDING");

  useEffect(() => {
    if (userInfo) {
      dispatch(get_orders_by_status(selectedStatus));
    }
  }, [dispatch, userInfo, selectedStatus]);

  const handleViewDetails = (order) => {
    setSelectedOrder(order);
    setIsModalVisible(true);
  };

  const closeModal = () => {
    setSelectedOrder(null);
    setIsModalVisible(false);
  };

  const handleStatusChange = (status) => {
    setSelectedStatus(status);
    dispatch(get_orders_by_status(status));
  };

  const columns = [
    {
      title: "Order ID",
      dataIndex: "id",
      key: "id",
      render: (text) => <Text>{text}</Text>,
    },
    {
      title: "Created Date",
      dataIndex: "createdDate",
      key: "createdDate",
      render: (text) => <Text>{new Date(text).toLocaleDateString()}</Text>,
    },
    {
      title: "Address",
      dataIndex: "address",
      key: "address",
      render: (text) => <Text>{text}</Text>,
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (status) => {
        let color = "";
        switch (status) {
          case "PENDING":
            color = "gold";
            break;
          case "PROCESSING":
            color = "orange";
            break;
          case "SHIPPED":
            color = "blue";
            break;
          case "DELIVERED":
            color = "green";
            break;
          case "CANCELLED":
            color = "red";
            break;
          default:
            color = "default";
        }
        return <Tag color={color}>{status}</Tag>;
      },
    },
    {
      title: "Total Amount",
      dataIndex: "total_amount",
      key: "total_amount",
      render: (amount) => <Text>{amount.toLocaleString()} VND</Text>,
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Button type="link" onClick={() => handleViewDetails(record)}>
          View Details
        </Button>
      ),
    },
  ];

  return (
    <div className="px-4 lg:px-8 py-6 bg-gray-50 rounded-lg shadow-md">
      <Title level={3} className="text-center mb-6">
        Order History
      </Title>

      {/* Dropdown to select order status */}
      <div className="mb-4 flex justify-end">
        <Select
          value={selectedStatus}
          onChange={handleStatusChange}
          style={{ width: 200 }}
        >
          <Option value="PENDING">PENDING</Option>
          <Option value="PROCESSING">PROCESSING</Option>
          <Option value="SHIPPED">SHIPPED</Option>
          <Option value="DELIVERED">DELIVERED</Option>
          <Option value="CANCELLED">CANCELLED</Option>
        </Select>
      </div>

      <Table
        columns={columns}
        dataSource={orders}
        rowKey="id"
        pagination={{ pageSize: 5 }}
      />

      {selectedOrder && (
        <Modal
          title={`Order Details (ID: ${selectedOrder.id})`}
          open={isModalVisible}
          onCancel={closeModal}
          footer={[
            <Button key="close" onClick={closeModal}>
              Close
            </Button>,
          ]}
        >
          <div className="mb-4">
            <Text>
              <b>Order ID:</b> {selectedOrder.id}
            </Text>
            <br />
            <Text>
              <b>Created Date:</b>{" "}
              {new Date(selectedOrder.createdDate).toLocaleDateString()}
            </Text>
            <br />
            <Text>
              <b>Address:</b> {selectedOrder.address}
            </Text>
            <br />
            <Text>
              <b>Status:</b> {selectedOrder.status}
            </Text>
            <br />
            <Text>
              <b>Total Amount:</b> {selectedOrder.total_amount.toLocaleString()}{" "}
              VND
            </Text>
          </div>
        </Modal>
      )}
    </div>
  );
};

export default OrderHistory;
