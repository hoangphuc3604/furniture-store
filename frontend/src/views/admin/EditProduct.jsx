import React, { useState, useEffect } from "react";
import { Form, Input, Button, Select, Modal, Row, Col } from "antd";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import {
  get_product,
  update_product,
  delete_product,
  clearMessage,
} from "../../store/Reducers/productReducer";
import Loading from "../components/Loading";
import { get_categories } from "../../store/Reducers/categoryReducer";
import { get_materials } from "../../store/Reducers/materialReducer";
import toast from "react-hot-toast";

const { Option } = Select;

const EditProduct = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { id } = useParams();
  const { product, errorMessage, success } = useSelector(
    (state) => state.products
  );
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const { categories } = useSelector((state) => state.categories);
  const { materials } = useSelector((state) => state.materials);

  useEffect(() => {
    if (id) {
      dispatch(get_product(id));
      dispatch(get_categories());
      dispatch(get_materials());
    }
  }, [dispatch, id]);

  useEffect(() => {
    if (product) {
      form.setFieldsValue({
        name: product.name,
        price: product.price,
        description: product.description,
        origin: product.origin,
        size: product.size,
        status: product.status,
        category_id: product.category_id?.id,
        material_id: product.material_id?.id,
      });
      setFileList(product.product_images || []);
    }
  }, [product, form]);

  // Handle form submission
  const handleSubmit = (values) => {
    const updatedProduct = {
      ...values,
      id: product.id,
    };
    console.log(updatedProduct);
    dispatch(update_product(updatedProduct));
  };

  const showDeleteConfirm = () => {
    setIsModalVisible(true);
  };

  const handleDelete = () => {
    dispatch(delete_product(id));
    setIsModalVisible(false);
    setTimeout(() => {
      navigate("/admin/product-list");
    }, 500);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }
    if (success) {
      toast.success("Successfully!");
    }
    dispatch(clearMessage());
  }, [errorMessage, success, dispatch]);

  if (!product) {
    return <Loading />;
  }

  return (
    <div className="px-4 py-6">
      <h1 className="text-2xl font-semibold mb-6">Edit Product Details</h1>
      <Form
        form={form}
        onFinish={handleSubmit}
        className="bg-white p-6 rounded-md shadow-md"
      >
        {/* Product Name */}
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item
              label="Product Name"
              name="name"
              rules={[
                { required: true, message: "Please enter product name!" },
              ]}
            >
              <Input placeholder="Enter product name" />
            </Form.Item>
          </Col>

          {/* Product Price */}
          <Col span={12}>
            <Form.Item
              label="Price"
              name="price"
              rules={[
                { required: true, message: "Please enter product price!" },
              ]}
            >
              <Input type="number" placeholder="Enter price" />
            </Form.Item>
          </Col>
        </Row>

        {/* Product Description */}
        <Form.Item
          label="Description"
          name="description"
          rules={[
            { required: true, message: "Please enter product description!" },
          ]}
        >
          <Input.TextArea placeholder="Enter product description" />
        </Form.Item>

        {/* Product Origin */}
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item
              label="Origin"
              name="origin"
              rules={[
                { required: true, message: "Please enter product origin!" },
              ]}
            >
              <Input placeholder="Enter product origin" />
            </Form.Item>
          </Col>

          {/* Product Size */}
          <Col span={12}>
            <Form.Item
              label="Size"
              name="size"
              rules={[
                { required: true, message: "Please enter product size!" },
              ]}
            >
              <Input placeholder="Enter product size" />
            </Form.Item>
          </Col>
        </Row>

        {/* Product Status */}
        <Form.Item
          label="Status"
          name="status"
          rules={[{ required: true, message: "Please select product status!" }]}
        >
          <Select placeholder="Select status">
            <Option value="ACTIVE">Active</Option>
            <Option value="INACTIVE">Inactive</Option>
          </Select>
        </Form.Item>

        {/* Category */}
        <Form.Item
          label="Category"
          name="category_id"
          rules={[{ required: true, message: "Please select a category!" }]}
        >
          <Select placeholder="Select category" value={product.category_id?.id}>
            {categories.map((category) => (
              <Option key={category.id} value={category.id}>
                {category.category_name}
              </Option>
            ))}
          </Select>
        </Form.Item>

        {/* Material */}
        <Form.Item
          label="Material"
          name="material_id"
          rules={[{ required: true, message: "Please select material!" }]}
        >
          <Select placeholder="Select material" value={product.material_id?.id}>
            {materials.map((material) => (
              <Option key={material.id} value={material.id}>
                {material.material_name}
              </Option>
            ))}
          </Select>
        </Form.Item>

        {/* Product Images - Read-Only */}
        <Form.Item label="Product Images">
          <div className="mt-4 flex flex-wrap gap-2">
            {fileList.map((file) => (
              <div key={file.image_link} className="relative">
                <img
                  src={
                    file.image_link || URL.createObjectURL(file.originFileObj)
                  }
                  alt="preview"
                  className="w-32 h-32 object-cover"
                />
              </div>
            ))}
          </div>
        </Form.Item>

        {/* Action Buttons */}
        <div className="flex justify-end gap-4">
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Save Changes
            </Button>
          </Form.Item>
          <Form.Item>
            <Button type="danger" onClick={showDeleteConfirm}>
              Delete Product
            </Button>
          </Form.Item>
        </div>
      </Form>

      {/* Confirmation Modal */}
      <Modal
        title="Are you sure?"
        open={isModalVisible}
        onOk={handleDelete}
        onCancel={handleCancel}
        okText="Yes"
        cancelText="No"
      >
        <p>
          Are you sure you want to delete this product? This action cannot be
          undone.
        </p>
      </Modal>
    </div>
  );
};

export default EditProduct;
