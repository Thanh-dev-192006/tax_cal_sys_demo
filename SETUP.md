# Hướng Dẫn Chạy Project: Tax Return Calculation System

## 📋 Tổng Quan
Đây là hệ thống tính thuế tự động được xây dựng bằng Java Swing với MySQL database. Hệ thống hỗ trợ quản lý client, tính thuế, và quản lý tax returns với giao diện người dùng thân thiện.

## 🛠️ Yêu Cầu Hệ Thống

### Phần Mềm Cần Thiết
- **Java 17+** (OpenJDK hoặc Oracle JDK)
- **MySQL Server 8.0+**
- **MySQL Connector/J** (đã có trong thư mục `lib/`)

### Kiểm Tra Phiên Bản Java
```bash
java -version
```
Output mong đợi: `openjdk version "17.x.x"` hoặc cao hơn

## 🚀 Cách Chạy Project

### Cách 1: Sử Dụng Script Tự Động (Khuyến Nghị)

#### Windows PowerShell
```powershell
.\run.ps1
```

#### Windows Command Prompt
```cmd
run.bat
```

### Cách 2: Chạy Thủ Công

#### Bước 1: Compile Project
```bash
javac -encoding UTF-8 -source 17 -target 17 -d bin src/com/oop/project/Main.java src/com/oop/project/exception/*.java src/com/oop/project/model/*.java src/com/oop/project/repository/*.java src/com/oop/project/service/*.java src/com/oop/project/ui/*.java src/com/oop/project/util/*.java
```

#### Bước 2: Chạy Ứng Dụng
```bash
java -cp "lib/*;bin" com.oop.project.Main
```

## 🗄️ Setup Database

### Bước 1: Khởi Động MySQL Server
Đảm bảo MySQL service đang chạy trên máy của bạn.

### Bước 2: Tạo Database và Import Dữ Liệu
```bash
# Đăng nhập MySQL với quyền root
mysql -u root -p

# Trong MySQL shell, chạy các lệnh sau:
SOURCE database/schema.sql;
SOURCE database/seed.sql;

# Thoát MySQL
EXIT;
```

### Bước 3: Kiểm Tra Database
```sql
USE tax_return_system;
SHOW TABLES;
SELECT * FROM Users LIMIT 5;
```

## ⚙️ Cấu Hình Database (Tùy Chọn)

Nếu bạn muốn thay đổi thông tin kết nối database, có 2 cách:

### Cách 1: Sử Dụng System Properties
```bash
java -Ddb.url="jdbc:mysql://localhost:3306/tax_return_system?useSSL=false&serverTimezone=UTC" -Ddb.user=root -Ddb.pass=yourpassword -cp "lib/*;bin" com.oop.project.Main
```

### Cách 2: Sửa File Source Code
Sửa file `src/com/oop/project/util/DatabaseUtil.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

Sau đó compile lại project.

## 📁 Cấu Trúc Thư Mục

```
tax_cal_sys_demo/
├── src/                    # Source code Java
│   └── com/oop/project/
│       ├── Main.java       # Entry point
│       ├── exception/      # Custom exceptions
│       ├── model/          # Data models
│       ├── repository/     # Database access layer
│       ├── service/        # Business logic
│       ├── ui/             # Swing GUI components
│       └── util/           # Utilities
├── bin/                    # Compiled classes
├── lib/                    # Dependencies (MySQL Connector)
├── database/               # SQL scripts
│   ├── schema.sql         # Database schema
│   └── seed.sql           # Sample data
├── run.ps1                # PowerShell run script
├── run.bat                # Batch run script
└── README.md              # Project documentation
```

## 🔐 Tài Khoản Mặc Định

Sau khi setup database, bạn có thể đăng nhập với:

- **Username:** admin
- **Password:** admin123

## 🐛 Xử Lý Lỗi Thường Gặp

### Lỗi: "Java version mismatch"
```
Error: UnsupportedClassVersionError
```
**Giải pháp:** Sử dụng Java 17+ và compile với `-source 17 -target 17`

### Lỗi: "Access denied for user"
```
Access denied for user 'root'@'localhost'
```
**Giải pháp:**
1. Kiểm tra MySQL service đang chạy
2. Đảm bảo password đúng trong `DatabaseUtil.java`
3. Kiểm tra user `root` có quyền truy cập database

### Lỗi: "MySQL JDBC Driver not found"
```
ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
**Giải pháp:** Đảm bảo file `mysql-connector-j-*.jar` có trong thư mục `lib/`

### Lỗi: "Database connection failed"
**Giải pháp:**
1. Chạy lại script setup database
2. Kiểm tra URL, username, password
3. Đảm bảo MySQL server đang chạy trên port 3306

## 📊 Tính Năng Chính

- ✅ Đăng nhập với phân quyền (Admin/Staff)
- ✅ Quản lý thông tin client
- ✅ Tính thuế tự động theo biểu thuế
- ✅ Quản lý tax returns
- ✅ Xuất báo cáo CSV
- ✅ Giao diện Swing hiện đại
- ✅ Lưu trữ dữ liệu MySQL

## 📞 Hỗ Trợ

Nếu gặp vấn đề khi setup, hãy kiểm tra:
1. Java version: `java -version`
2. MySQL service status
3. Database connection
4. File permissions

## 🔄 Cập Nhật Project

Khi có thay đổi code:
1. Compile lại: `javac -encoding UTF-8 -source 17 -target 17 -d bin ...`
2. Chạy lại: `java -cp "lib/*;bin" com.oop.project.Main`

---

**Lưu ý:** Đảm bảo MySQL server luôn chạy trước khi khởi động ứng dụng!