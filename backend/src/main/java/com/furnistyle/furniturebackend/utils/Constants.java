package com.furnistyle.furniturebackend.utils;

public class Constants {
    private Constants() {
    }

    public static final class Message {
        private Message() {
        }

        public static final String NOT_FOUND_USER = "Không tìm thấy thông tin người dùng!";
        public static final String BLOCKED_ACCOUNT = "Tài khoản của người dùng đã bị khóa!";
        public static final String INCORRECT_USERNAME_OR_PASS = "Tên tài khoản hoặc mật khẩu không đúng!";
        public static final String INCORRECT_OLD_PASS = "Mật khẩu cũ không đúng!";
        public static final String DUPLICATE_OLD_NEW_PASS = "Mật khẩu cũ trùng với mật khẩu mới!";
        public static final String CAN_NOT_CHANGE_PASS = "Không thể thay đổi mật khẩu người dùng!";
        public static final String CAN_NOT_UPDATE_INFO_USER = "Không thể cập nhật thông tin người dùng!";
        public static final String UPDATE_USER_SUCCESSFUL = "Cập nhật thông tin người dùng thành công!";
        public static final String UPDATE_USER_FAILED = "Cập nhật thông tin người dùng thất bại!";
        public static final String BLOCK_USER_WITH_ID = "Khoá thành công tài khoản của người dùng có id: ";
        public static final String UNLOCK_USER_WITH_ID = "Kích hoạt thành công tài khoản của người dùng có id: ";
        public static final String REGISTER_SUCCESSFUL = "Đăng ký thông tin người dùng thành công!";
        public static final String REGISTER_FAILED = "Đăng ký thông tin người dùng thất bại!";
        public static final String ALREADY_USERNAME_REGISTER = "Tên người dùng đã tồn tại!";
        public static final String ALREADY_PHONE_REGISTER = "Số điện thoại đã được đăng ký!";
        public static final String ALREADY_MAIL_REGISTER = "Email đã được đăng ký!";

        public static final String NOT_FOUND_ORDER = "Không tìm thấy đơn hàng!";
        public static final String NOT_FOUND_PRODUCT = "Không tìm thấy sản phẩm!";
        public static final String FAILED_WHILE_SAVING_ORDER = "Lỗi trong quá trình lưu thông tin đơn hàng!";
        public static final String ORDER_IS_CONFIRMED = "Đơn hàng đã được xác nhận!";
        public static final String UPDATE_ORDER_SUCCESSFUL = "Cập nhật đơn hàng thành công!";
        public static final String UPDATE_ORDER_FAILED = "Cập nhật đơn hàng thất bại!";
        public static final String CREATE_ORDER_SUCCESSFUL = "Thêm đơn hàng thành công!";
        public static final String CREATE_ORDER_FAILED = "Thêm đơn hàng thành công!";
        public static final String DUPLICATE_CREATED_CONFIRMED_PERSION =
            "Admin không được xác nhận đơn hàng của chính mình!";
        public static final String UNAUTHORIZED = "Người dùng không đủ quyền hạn để thực hiện chức năng này!";
        public static final String NOT_FOUND_PRODUCT_IN_ORDER = "Không tìm thấy sản phẩm trong đơn hàng!";
        public static final String NOT_FOUND_CART_DETAIL = "Không tìm thấy sản phẩm trong giỏ hàng!";
        public static final String REMOVE_FROM_CART_SUCCESSFUL = "Đã xóa sản phẩm khỏi giỏ hàng!";
        public static final String ADD_TO_CART_SUCCESSFUL = "Đã thêm sản phẩm vào giỏ hàng!";
        public static final String UPDATE_AMOUNT_PRODUCT_IN_CART_SUCCESSFUL = "Cập nhật số lượng thành công!";

        public static final String ADD_CATEGORY_SUCCESSFUL = "Thêm phân loại mới thành công!";
        public static final String UPDATE_CATEGORY_SUCCESSFUL = "Cập nhật phân loại thành công!";
        public static final String DELETE_CATEGORY_SUCCESSFUL = "Xóa phân loại thành công!";
        public static final String NOT_FOUND_CATEGORY = "Không tìm thấy phân loại!";
        public static final String DELETE_CATEGORY_FAILED = "Không thể xóa phân loại vì có các phẩm liên quan!";
        public static final String UPDATE_CATEGORY_FAILED = "Lỗi cập nhật thông tin phân loại!";

        public static final String ADD_MATERIAL_SUCCESSFUL = "Thêm chất liệu mới thành công!";
        public static final String UPDATE_MATERIAL_SUCCESSFUL = "Cập nhật chất liệu thành công!";
        public static final String DELETE_MATERIAL_SUCCESSFUL = "Xóa chất liệu thành công!";
        public static final String NOT_FOUND_MATERIAL = "Không tìm thấy chất liệu!";
        public static final String DELETE_MATERIAL_FAILED = "Không thể xóa chất liệu vì có các phẩm liên quan!";
        public static final String UPDATE_MATERIAL_FAILED = "Lỗi cập nhật thông tin chất liệu!";

        public static final String ADD_PRODUCT_SUCCESSFUL = "Thêm sản phẩm mới thành công!";
        public static final String UPDATE_PRODUCT_SUCCESSFUL = "Cập nhật sản phẩm thành công!";
        public static final String DELETE_PRODUCT_SUCCESSFUL = "Xóa sản phẩm thành công!";

        public static final Integer MAXIMUM_IMAGES_PER_PRODUCT = 20;
        public static final Integer MAXIMUM_SIZE_PER_PRODUCT = 10;  //MB
        public static final String UPLOAD_IMAGES_MAX_20 = "Tổng số lượng ảnh của một sản phẩm không được vượt quá"
                                                            + MAXIMUM_IMAGES_PER_PRODUCT;
        public static final String UPLOAD_IMAGES_FILE_LARGE = "Dung lượng ảnh không được vượt quá "
                                                            + MAXIMUM_SIZE_PER_PRODUCT + "MB!";
        public static final String UPLOAD_IMAGES_FILE_MUST_BE_IMAGE = "File tải lên phải là hình ảnh!";
        public static final String UPLOAD_IMAGES_FILE_SUCCESSFUL = "Upload ảnh thành công!";
        public static final String INVALID_FORMAT_IMAGES = "File ảnh không hợp lệ!";
        public static final String NOT_FOUND_DATABASE_IMAGES = "Không tìm thấy hình ảnh trong database!";
        public static final String INVALID_URL = "URL không hợp lệ!";
        public static final String NOT_FOUND_CLOUD_FILE = "Không tìm thấy file trên Cloudinary!";
        public static final String DELETE_CLOUD_FILE_FAILED = "Lỗi khi xóa file trên Cloudinary!";
        public static final String CLOUD_FOLDER_NAME = "Product Medias";
        public static final String INVALID_STATUS = "Trạng thái cần cập nhật không hợp lệ!";
        public static final String EXPIRED_CODE = "Mã OTP đã hết hạn!";
        public static final String NOT_MATCH_CODE = "Mã OTP không đúng, vui lòng thử lại!";

        public static final String EXPIRED_TOKEN = "Phiên đăng nhập đã hết, vui lòng đăng nhập lại!";

        public static final String NOT_BOUGHT = "Người dùng chưa mua sản phẩm này!";
        public static final String ADD_REVIEW_SUCCESSFUL = "Thêm review thành công!";
    }
}
