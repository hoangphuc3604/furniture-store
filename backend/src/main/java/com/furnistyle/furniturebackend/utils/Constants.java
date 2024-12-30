package com.furnistyle.furniturebackend.utils;

public class Constants {
    private Constants() {
    }

    public static final class Message {
        private Message() {
        }

        public static final String NOT_FOUND_USER = "Không tìm thấy thông tin người dùng!";
        public static final String BLOCKED_ACOUNT = "Tài khoản của người dùng đã bị khóa!";
        public static final String INCORRECT_USERNAME_OR_PASSWORD_MESSAGE = "Tên tài khoản hoặc mật khẩu không đúng!";
        public static final String INCORRECT_OLD_PASSWORD_MESSAGE = "Mật khẩu cũ không đúng!";
        public static final String DUPLICATE_OLD_NEW_PASSWORD_MESSAGE = "Mật khẩu cũ trùng với mật khẩu mới!";
        public static final String CAN_NOT_CHANGE_PASSWORD_MESSAGE = "Không thể thay đổi mật khẩu người dùng!";
        public static final String CAN_NOT_UPDATE_INFO_USER = "Không thể cập nhật thông tin người dùng!";
        public static final String UPDATE_USER_SUCCESSFUL = "Cập nhật thông tin người dùng thành công!";
        public static final String UPDATE_USER_FAILED = "Cập nhật thông tin người dùng thất bại!";
        public static final String BLOCK_USER_WITH_ID = "Kích hoạt thành công tài khoản của người dùng có id: ";
        public static final String UNLOCK_USER_WITH_ID = "Khoá thành công tài khoản của người dùng có id: ";
        public static final String REGISTER_SUCCESSFUL = "Đăng ký thông tin người dùng thành công!";
        public static final String REGISTER_FAILED = "Đăng ký thông tin người dùng thất bại!";
    }
}
