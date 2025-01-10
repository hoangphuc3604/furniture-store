import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { Modal, Button } from "antd";
import {
  clearMessage,
  lock_user,
  active_user,
  get_user_by_id,
} from "../../store/Reducers/userReducer";
import toast from "react-hot-toast";
import Loading from "./../components/Loading";

const UserDetail = () => {
  const dispatch = useDispatch();
  const { user, loader, errorMessage, success } = useSelector(
    (state) => state.users
  );

  const [status, setStatus] = useState("");
  const [newStatus, setNewStatus] = useState("");

  const { id } = useParams();

  useEffect(() => {
    dispatch(get_user_by_id(id));
    return () => {
      dispatch(clearMessage());
    };
  }, [id]);

  useEffect(() => {
    if (user) setStatus(user.status);
  }, [user]);

  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(clearMessage());
  }, [success, errorMessage]);

  const handleStatusChange = (status) => {
    setNewStatus(status);
  };

  const handleConfirmChange = () => {
    if (newStatus === "ACTIVE") {
      dispatch(active_user(id));
    } else if (newStatus === "INACTIVE") {
      dispatch(lock_user(id));
    }
    setStatus(newStatus);
    setNewStatus("");
  };

  const handleCancelChange = () => {
    setNewStatus("");
  };

  const modalFooter = [
    <Button key="cancel" onClick={handleCancelChange}>
      Cancel
    </Button>,
    <Button key="confirm" type="primary" onClick={handleConfirmChange}>
      Confirm
    </Button>,
  ];

  if (loader) {
    return <Loading fullScreen={true} />;
  }

  return (
    <div className="p-6 bg-gray-100 min-h-screen">
      <div className="max-w-4xl mx-auto bg-white rounded-lg shadow-lg p-6">
        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">User Information</h1>
          <span
            className={`px-3 py-1 rounded-full text-white ${
              status === "ACTIVE"
                ? "bg-green-500"
                : status === "INACTIVE"
                ? "bg-yellow-500"
                : "bg-red-500"
            }`}
          >
            {status}
          </span>
        </div>

        {/* User Details */}
        {user ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
            <div>
              <p className="font-bold">User ID:</p>
              <p>{user.id}</p>
            </div>
            <div>
              <p className="font-bold">Name:</p>
              <p>{user.fullname}</p>
            </div>
            <div>
              <p className="font-bold">Email:</p>
              <p>{user.email}</p>
            </div>
            <div>
              <p className="font-bold">Phone:</p>
              <p>{user.phone}</p>
            </div>
            <div>
              <p className="font-bold">Address:</p>
              <p>{user.address}</p>
            </div>
            <div>
              <p className="font-bold">Date of Birth:</p>
              <p>{user.date_of_birth}</p>
            </div>
            <div>
              <p className="font-bold">Gender:</p>
              <p>{user.gender}</p>
            </div>
          </div>
        ) : (
          <p>User information not found.</p>
        )}

        {/* Status Change */}
        <div className="bg-gray-50 p-4 rounded-md shadow-md">
          <h2 className="font-bold mb-4">Change Account Status</h2>
          <div className="flex space-x-4">
            <Button
              onClick={() => handleStatusChange("ACTIVE")}
              className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600"
            >
              Activate
            </Button>
            <Button
              onClick={() => handleStatusChange("INACTIVE")}
              className="px-4 py-2 bg-yellow-500 text-white rounded-md hover:bg-yellow-600"
            >
              Deactivate
            </Button>
          </div>
        </div>
      </div>

      <Modal
        title="Confirm Status Change"
        open={newStatus !== ""}
        onCancel={handleCancelChange}
        footer={modalFooter}
      >
        <p>
          Are you sure you want to change the account status to{" "}
          <strong>{newStatus}</strong>?
        </p>
      </Modal>
    </div>
  );
};

export default UserDetail;
