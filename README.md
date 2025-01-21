# Furniture store

## Mô tả
Cửa hàng nội thất là một cửa hàng trực tuyến chuyên kinh doanh các sản phẩm nội thất. Nó cung cấp cho người dùng một môi trường trực tuyến, nơi họ có thể duyệt và lựa chọn những món đồ mà họ muốn mua.
## Thành viên
- 22120354 - Nguyễn Lê Anh Thư
- 22120278 - Nguyễn Văn Hoàng Phúc
- 22120302 - Đặng Quý
- 22120315 - Nguyễn Văn Tài
- 22120325 - Nguyễn Nhật Tân

## Hướng dẫn triển khai
### Frontend
#### Thiết lập môi trường
- Thư viện sử dụng thiết kế Component: ReactJS
- UI/UX hiện đại với TailwindCSS 
- Quá trình xây dựng và khởi chạy đơn giản với npm
#### Điều kiện tiên quyết
- Node.js
- npm đã được cài đặt trên máy
#### Chuẩn bị mã nguồn
- git clone https://github.com/QuyDang1108/FurnitureStore.git
- Truy cập vào thư mục frontend
- Chạy lệnh:
   ```bash
       npm install
   ````
#### Khởi chạy
- Chạy lệnh:
   ```bash
     npm run start
   ````
### Backend
#### Thiết lập môi trường
- Ngôn ngữ lập trình: Java
- Framework: Spring boot
- Hệ quản trị cơ sở dữ liệu: MySQL
- IDE: Intelllij hoặc công cụ mà bạn quen thuộc
#### Chuẩn bị mã nguồn
- git clone https://github.com/QuyDang1108/FurnitureStore.git
- truy cập vào thư mục backend
#### Tạo tệp môi trường
- Tạo file có tên .env trong thư mục backend
- Sao chép các biến môi trường từ file .env.sample bỏ vào file .env
- Thay đổi giá trị của biến môi trường phù hợp với máy của bạn
- Ý nghĩa các biến quan trọng trong file .env:
  - SERVER_LOCAL_PORT: cổng để truy cập vào backend
  - URL_DB: url để kết nối đến cơ sở dữ liệu
  - USERNAME_DB: tên người dùng cho cơ sở dữ liệu
  - PASSWORD_DB: mật khẩu truy cập với người dùng cho cơ sở dữ liệu
  - SECRET_KEY: khóa bí mật để dùng trong JWT token
  - EXPIRATION: thời gian hết hạn của token
  - USERNAME_MAIL_SERVER: email của host được dùng để gửi mail
  - PASSWORD_MAIL_SERVER: mật khẩu của host được dùng để gửi mail
  - DEFAULT_PASSWORD: mật khẩu mặc định khi người dùng quên mật khẩu
  - TIME_VALIDATE_OTP: thời gian hết hạn của OTP
#### Khởi chạy
- Cách 1: 
  - Truy cập vào ./backend/src/main/java/com/furnistyle/furniturebackend
  - Mở FurnitureBackendApplication và nhấn start
- Cách 2:
  - Mở terminal và truy cập vào ./backend/
  - Chạy lệnh 
   ```bash
       ./mvnw spring-boot:run
   ```
- **Chú ý**
   - Sau khi khởi chạy thành công, hệ thống tự động tạo 1 tài khoản mặc đình với vai trò là super admin
     ```bash
       email: superadmin
       password: 123456
     ```
## Kiểm tra code trong quá trình phát triển phần mềm
1. Kiểm tra phong cách code - duy trì tính đồng bộ trong quá trình viết mã của các thành viên
```bash
   ./mvnw checkstyle:checkstyle
```
2. Xây dựng phần mềm - kiểm tra ứng dụng không xuất hiện lỗi cú pháp hoặc bất kì lỗi gì liên quan khiến phần mềm không thể chạy
```bash
    ./mvnw clean install  
```

## Tài liệu tham khảo

### Tài liệu và công cụ
1. **Tài liệu Spring Boot**:
    - [Tài liệu Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
2. **Tài liệu NodeJs**
    - [Tài liệu NodeJs](https://nodejs.org/)
3. **ReactJs**
    - [ReactJs](https://legacy.reactjs.org/)
4. **Phân tích chất lượng mã nguồn**
   - [SonarQube](https://docs.sonarsource.com/sonarqube-cloud/)
5. **JWT token**
    - [Tham khảo](https://github.com/ali-bouali/spring-boot-3-jwt-security)
6. **Unit Test**
    - [Junit 5](https://junit.org/)
7. **Tạo data giả trong quá trình kiểm thử**
    - [Instancio](https://www.instancio.org/)
### Thực tiễn & Tiêu chuẩn tốt
1. **Clean Code**:
    - [Clean Code by Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)

2. **Tài liệu Checkstyle**:
    - [Tài liệu Checkstyle](https://checkstyle.sourceforge.io/)
### Tài liệu học thêm
1. **Tài liệu chính thức Java 17**:
    - [Tài liệu Java 17](https://docs.oracle.com/en/java/javase/17/)
