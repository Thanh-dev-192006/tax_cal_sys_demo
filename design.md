# Design Document — VTAX: Vietnam Tax Return Management System

## 1. Danh sách các lớp và vai trò (Class List & Responsibilities)

| Class | Package | Vai trò |
|---|---|---|
| `Main` | `com.oop.project` | Điểm khởi chạy ứng dụng; cấu hình Look & Feel Nimbus, gọi `DataInitializer`, mở `LoginFrame` |
| `User` | `model` | Đối tượng đại diện cho nhân viên/admin; lưu thông tin tài khoản và hồ sơ cá nhân |
| `Client` | `model` | Đối tượng đại diện cho khách hàng nộp thuế; lưu mã số thuế, thu nhập, người phụ thuộc |
| `TaxReturn` | `model` | Đối tượng đại diện cho một tờ khai thuế TNCN đã nộp; lưu ngày nộp, số tiền, trạng thái |
| `TaxBracket` | `model` | Đóng gói bảng bậc thuế (threshold + rate); cung cấp logic tra cứu thuế suất theo thu nhập |
| `AuthenticationService` | `service` | Xử lý đăng nhập: xác thực username/password thông qua `UserRepository`, ghi log |
| `ClientService` | `service` | Nghiệp vụ quản lý khách hàng: thêm, sửa, xoá, tìm kiếm; gọi `ValidationUtil` để kiểm tra đầu vào |
| `TaxCalculationService` | `service` | Tính thuế TNCN dựa trên `TaxBracket` theo tháng (wrapper đơn giản) |
| `TaxReturnService` | `service` | Nộp và quản lý tờ khai thuế; tính thuế qua `TaxCalculator`; cung cấp các thống kê cho Dashboard |
| `UserRepository` | `repository` | Truy cập bảng `Users` trong MySQL: load, save, find, update, delete |
| `ClientRepository` | `repository` | Truy cập bảng `Clients` trong MySQL: CRUD đầy đủ với `PreparedStatement` |
| `TaxReturnRepository` | `repository` | Truy cập bảng `TaxReturns` trong MySQL: lưu, load, tìm theo client ID |
| `LoginFrame` | `ui` | Màn hình đăng nhập (JFrame); thu thập username/password, gọi `AuthenticationService` |
| `MainFrame` | `ui` | Khung ứng dụng chính; sidebar điều hướng + `CardLayout` chuyển đổi giữa các panel |
| `DashboardPanel` | `ui` | Panel tổng quan: hiển thị KPI (số tờ khai, tổng thuế, khách chưa nộp) |
| `ClientPanel` | `ui` | Panel quản lý khách hàng: bảng danh sách, thêm/sửa/xoá, tìm kiếm |
| `TaxCalculationPanel` | `ui` | Panel khai thuế: tìm khách theo Tax ID, nhập thông số, tính thuế theo bậc luỹ tiến, lưu tờ khai |
| `ReturnsPanel` | `ui` | Panel lịch sử tờ khai: bảng toàn bộ tờ khai, lọc trạng thái, xuất CSV |
| `AdministrationPanel` | `ui` | Panel quản trị (chỉ ADMIN): quản lý tài khoản nhân viên |
| `TaxCalculator` | `util` | Tiện ích tĩnh tính thuế TNCN theo 7 bậc luỹ tiến của Việt Nam; tạo HTML receipt |
| `ValidationUtil` | `util` | Kiểm tra hợp lệ: mã số thuế (9 chữ số), email, số điện thoại, tình trạng hôn nhân |
| `DatabaseUtil` | `util` | Quản lý kết nối JDBC tới MySQL; đọc thông số từ `ConfigLoader` |
| `DataInitializer` | `util` | Kiểm tra kết nối database khi khởi động ứng dụng |
| `AppTheme` | `util` | Hằng số màu sắc, font chữ, và các factory method tạo component UI có style đồng nhất |
| `VndFormatter` | `util` | Format số tiền VND sang dạng hiển thị ngắn gọn (ví dụ: 1.2 tỷ) |
| `CsvExporter` | `util` | Xuất danh sách tờ khai ra file CSV |
| `ConfigLoader` | `config` | Đọc `config.properties`; cung cấp các thuộc tính cấu hình database |
| `InvalidCredentialException` | `exception` | Checked exception ném ra khi đăng nhập thất bại |
| `InvalidDataException` | `exception` | Checked exception ném ra khi dữ liệu đầu vào không hợp lệ |

---

## 2. Áp dụng các nguyên lý OOP

### 2.1. Encapsulation

**Các thuộc tính `private`:**

Tất cả các class trong package `model` đều khai báo trường dữ liệu là `private`:
- `User`: `username`, `password`, `role`, `staffId`, `fullName`, `email`, `phoneNumber`
- `Client`: `id`, `name`, `income`, `maritalStatus`, `dependents`, `email`, `phoneNumber`, `city`
- `TaxReturn`: `clientId`, `filingDate`, `taxLiability`, `status`, `maritalStatus`
- `TaxBracket`: `thresholds`, `rates`

**Truy cập thông qua getter/setter:**

Mỗi trường đều có getter tương ứng (ví dụ `getUsername()`, `getIncome()`). Các trường cần thay đổi từ bên ngoài thì có setter (ví dụ `setIncome()`, `setEmail()`). Đặc biệt `Client.setEmail()` giữ đồng bộ trường `contactInfo` cũ để backward compatibility.

**Lý do áp dụng:**

> Các class model là đối tượng dữ liệu trung tâm được truyền qua nhiều tầng (service → repository → UI). Đóng gói private giúp kiểm soát được việc ai có thể đọc/ghi dữ liệu, tránh trạng thái không nhất quán — ví dụ `email` và `contactInfo` trong `Client` luôn đồng bộ nhờ setter.

---

### 2.2. Inheritance

**Class cha và class con:**

| Class cha | Class con | Ghi chú |
|---|---|---|
| `Exception` (Java standard) | `InvalidCredentialException` | Kế thừa checked exception |
| `Exception` (Java standard) | `InvalidDataException` | Kế thừa checked exception |
| `JFrame` (Swing) | `LoginFrame`, `MainFrame` | Kế thừa cửa sổ ứng dụng |
| `JPanel` (Swing) | `DashboardPanel`, `ClientPanel`, `TaxCalculationPanel`, `ReturnsPanel`, `AdministrationPanel` | Kế thừa panel Swing |

**Lý do sử dụng kế thừa:**

> - **Exception hierarchy**: Tạo ra các loại lỗi có ngữ nghĩa rõ ràng (`InvalidCredentialException` dành riêng cho lỗi xác thực, `InvalidDataException` cho dữ liệu sai định dạng), giúp caller bắt từng loại lỗi cụ thể thay vì bắt `Exception` chung chung.
> - **Swing inheritance**: Đây là yêu cầu bắt buộc của framework Swing — mọi cửa sổ phải kế thừa `JFrame`, mọi panel phải kế thừa `JPanel` để tận dụng bộ máy vẽ giao diện và hệ thống event của Swing. Các lớp UI của VTAX kế thừa và tuỳ biến lại hành vi thông qua `override` các phương thức như `paintComponent()` (trong `LoginFrame` để vẽ họa tiết nền).

---

### 2.3. Polymorphism

**Các phương thức được override:**

- `paintComponent(Graphics g)` trong `LoginFrame.buildBrandPanel()` — override để vẽ các đường kẻ chéo tạo texture cho panel thương hiệu.
- `toString()` trong `User`, `Client`, `TaxReturn` — override để cho ra chuỗi mô tả có ý nghĩa khi debug/log.

**Được gọi thông qua reference kiểu cha:**

- `Exception ex` được dùng trong các khối `catch` tổng quát trong service/repository để log lỗi chưa xử lý.
- `JPanel`, `JComponent`, `Component` — Swing nội bộ gọi `paintComponent()` trên reference kiểu `JComponent`, nhưng ở runtime thực thi phiên bản override trong `LoginFrame`'s anonymous panel — đây là **runtime polymorphism** điển hình.
- Trong `MainFrame`, các panel (`DashboardPanel`, `ClientPanel`, ...) đều được thêm vào `CardLayout` dưới dạng kiểu cha `Component`, giúp `CardLayout` điều hướng giữa chúng mà không cần biết kiểu cụ thể.

**Mô tả:**

> Polymorphism được tận dụng chủ yếu qua hệ thống component của Swing và qua cơ chế exception. Thiết kế không dùng nhiều polymorphism trong tầng business logic vì logic tính thuế không có nhiều biến thể cần override — điều này là hợp lý cho scope hiện tại.

---

### 2.4. Interface

**Các interface được sử dụng:**

| Interface | Class implement | Vai trò |
|---|---|---|
| `java.io.Serializable` | `User`, `Client`, `TaxReturn`, `TaxBracket` | Đánh dấu đối tượng có thể serialize/deserialize |
| `javax.swing.table.TableCellRenderer` | Anonymous class trong `AdministrationPanel` | Tuỳ chỉnh cách render ô trong JTable |
| `javax.swing.table.TableCellEditor` | Anonymous class trong `AdministrationPanel` | Cho phép inline edit trong JTable |
| `javax.swing.event.DocumentListener` | Anonymous class trong `TaxCalculationPanel` | Lắng nghe thay đổi text để tính thuế real-time |
| `java.awt.event.ActionListener` | Lambda trong `LoginFrame`, `MainFrame`, ... | Xử lý sự kiện button click |

**Vai trò của interface trong thiết kế:**

> `Serializable` được implement trên tất cả model class như một "marker interface" — tuy hiện tại dữ liệu lưu ở MySQL, nhưng việc implement sẵn đảm bảo các object có thể được serialize nếu sau này cần cache hoặc truyền qua mạng. Các interface Swing (`TableCellRenderer`, `DocumentListener`) cho phép tuỳ biến hành vi UI mà không cần tạo subclass mới — thay vào đó dùng anonymous class hoặc lambda, giữ code gọn gàng.

---

### 2.5. Abstraction

**Abstract class / method được sử dụng:**

- `javax.swing.table.AbstractTableModel` — `AdministrationPanel` dùng inner class `UserTableModel extends AbstractTableModel`, chỉ cần implement 3 phương thức abstract: `getRowCount()`, `getColumnCount()`, `getValueAt()`.

**Phần chi tiết được ẩn đi:**

- `TaxCalculator` là `final class` với constructor `private` — ẩn hoàn toàn việc khởi tạo, chỉ expose các phương thức `static`. Người dùng không cần biết bên trong có mảng `ANNUAL_THRESHOLDS` hay vòng lặp tính luỹ tiến — chỉ gọi `TaxCalculator.calculateTax(monthly, dependents)`.
- `DatabaseUtil` là `final class` với constructor `private` — ẩn chi tiết khởi tạo JDBC driver, quản lý connection pool. Tầng repository chỉ gọi `DatabaseUtil.getConnection()`.
- `ConfigLoader` là `final class` — ẩn logic đọc file `config.properties` từ classpath hoặc filesystem; tầng trên chỉ gọi `ConfigLoader.getDbUrl()`.
- `TaxBracket` đóng gói mảng ngưỡng và thuế suất, cung cấp phương thức `getTaxRate(income)` để tra cứu — ẩn cấu trúc dữ liệu parallel arrays bên trong.

**Mô tả:**

> Abstraction được áp dụng nhất quán ở tầng utility: mỗi utility class đóng gói một nhóm trách nhiệm và chỉ lộ ra phương thức cần thiết. Đây là hình thức abstraction thực dụng, phù hợp với quy mô dự án — không cần abstract class phức tạp khi chỉ có một cách triển khai.

---

## 3. Design Patterns được sử dụng

| Design Pattern | Áp dụng ở đâu | Mục đích |
|---|---|---|
| **Repository Pattern** | `UserRepository`, `ClientRepository`, `TaxReturnRepository` | Tách biệt logic truy cập CSDL (SQL + JDBC) khỏi tầng business logic; nếu sau này đổi từ MySQL sang PostgreSQL chỉ cần sửa repository |
| **Service Layer** | `AuthenticationService`, `ClientService`, `TaxReturnService`, `TaxCalculationService` | Tập trung nghiệp vụ vào một tầng riêng; UI chỉ gọi service, không trực tiếp thao tác repository hoặc tính toán |
| **MVC (Model-View-Controller)** | Toàn bộ hệ thống | Model = `model.*`; View = `ui.*`; Controller = `service.*` + event listener trong UI |
| **Static Factory / Utility Class** | `TaxCalculator`, `ValidationUtil`, `AppTheme`, `DatabaseUtil`, `ConfigLoader` | Nhóm các hàm tiện ích liên quan vào một class `final` với constructor `private`; không cần khởi tạo, truy cập trực tiếp qua tên class |
| **CardLayout Navigation** | `MainFrame` | Dùng `CardLayout` + `Map<String, JButton>` để điều hướng giữa các panel — tương tự **Strategy** trong UI navigation: thay đổi "trang hiện tại" mà không phải tạo/huỷ window |
| **Template Method** (một phần) | `AbstractTableModel` trong `AdministrationPanel` | Swing định nghĩa khung xử lý bảng, subclass chỉ override phần cụ thể |

---

## 4. Luồng hoạt động chính (Main Application Flows)

### 4.1. Đăng nhập (Login)

1. Ứng dụng khởi động: `Main.main()` cấu hình Nimbus L&F, gọi `DataInitializer.initializeSampleData()` để kiểm tra kết nối MySQL, sau đó mở `LoginFrame`.
2. Người dùng nhập username và password, nhấn **LOGIN** (hoặc Enter).
3. `LoginFrame.performLogin()` gọi `AuthenticationService.authenticate(username, password)`.
4. `AuthenticationService` gọi `UserRepository.findUserByUsername(username)` — truy vấn `SELECT * FROM Users WHERE username = ?`.
5. Nếu user không tồn tại hoặc password không khớp → ném `InvalidCredentialException` → hiển thị thông báo lỗi, reset trường mật khẩu.
6. Nếu hợp lệ → gọi `authService.logLogin(username)` → tạo `MainFrame(user)`, đóng `LoginFrame`.

---

### 4.2. Tính thuế và nộp tờ khai (Tax Filing)

1. Người dùng điều hướng sang tab **Tax Filing** trong `MainFrame`.
2. Nhập Tax ID (mã số thuế 9 chữ số) vào ô tìm kiếm, nhấn **Load Data**.
3. `TaxCalculationPanel.performSearch()` gọi `ClientService.findClientById(id)` → truy vấn MySQL → trả về object `Client`.
4. Thông tin thu nhập, số người phụ thuộc, tình trạng hôn nhân được điền tự động vào form.
5. Mỗi thay đổi giá trị trên form kích hoạt `DocumentListener` → gọi `calculate()`:
   - Gọi `TaxCalculator.calculateTax(monthlyIncome, dependents)`.
   - Thuật toán progressive brackets: trừ giảm trừ gia cảnh (11 triệu/tháng bản thân + 4.4 triệu/tháng/người phụ thuộc), nhân 12 ra thu nhập tính thuế năm, áp dụng 7 bậc luỹ tiến.
   - Cập nhật 3 summary card (Annual Income, Annual Tax, Net Income).
   - Render HTML receipt chi tiết vào `JEditorPane`.
6. Người dùng nhấn **File Tax Return (Save & Finish)**.
7. `TaxReturnService.fileTaxReturn()` được gọi: tính `taxLiability`, xác định `status` (Filed nếu trước 30/4, Overdue nếu sau), tạo `TaxReturn`, lưu vào bảng `TaxReturns` qua `TaxReturnRepository`.
8. Hiển thị dialog xác nhận thành công với thông tin tóm tắt.

---

### 4.3. Quản lý khách hàng (Client Management)

1. Người dùng điều hướng sang tab **Clients**.
2. `ClientPanel` tải danh sách từ `ClientService.getAllClients()` → `ClientRepository.loadClients()` → `SELECT * FROM Clients ORDER BY name`.
3. Người dùng có thể:
   - **Thêm**: Điền form → `ClientService.addClient(client)` → `validateClient()` kiểm tra Tax ID (9 chữ số), tên, thu nhập ≥ 0 → `ClientRepository.addClient()` → INSERT vào MySQL.
   - **Sửa**: Chọn dòng → sửa thông tin → `ClientService.updateClient()` → UPDATE.
   - **Xoá**: Chọn dòng → xác nhận → `ClientService.deleteClient(id)` → DELETE.
   - **Tìm kiếm**: Nhập tên → `ClientRepository.findClientsByName(name)` → `SELECT ... WHERE name LIKE ?`.

---

### 4.4. Xem lịch sử tờ khai (Returns)

1. Khi chuyển sang tab **Returns**, `MainFrame.onNavigate("returns")` gọi `returnsPanel.loadData()`.
2. `ReturnsPanel` tải toàn bộ tờ khai từ `TaxReturnService.getAllTaxReturns()`.
3. Dữ liệu hiển thị trong `JTable` với các cột: Client ID, Ngày nộp, Nghĩa vụ thuế, Trạng thái, Tình trạng hôn nhân.
4. Người dùng có thể lọc theo trạng thái (Filed / Overdue) hoặc xuất CSV qua `CsvExporter`.

---

### 4.5. Quản trị hệ thống (Administration — chỉ ADMIN)

1. Tab **Administration** chỉ hiển thị khi `currentUser.getRole().equals("ADMIN")`.
2. `AdministrationPanel` tải danh sách user qua `UserRepository.loadUsers()`.
3. Admin có thể thêm user mới, thay đổi role, đổi mật khẩu, xoá tài khoản trực tiếp qua inline table editor.

---

## 5. Class Diagram

Sơ đồ class thể hiện quan hệ giữa các lớp chính:

```
┌─────────────────────────────────────────────────────────────────────┐
│                          <<model>>                                  │
│                                                                     │
│  ┌──────────┐    ┌──────────┐    ┌────────────┐    ┌────────────┐  │
│  │   User   │    │  Client  │    │ TaxReturn  │    │ TaxBracket │  │
│  │──────────│    │──────────│    │────────────│    │────────────│  │
│  │-username │    │-id       │    │-clientId   │    │-thresholds │  │
│  │-password │    │-name     │    │-filingDate │    │-rates      │  │
│  │-role     │    │-income   │    │-taxLiab.   │    │────────────│  │
│  │-staffId  │    │-dependents│   │-status     │    │+getTaxRate │  │
│  └──────────┘    │-marital  │    └────────────┘    └────────────┘  │
│  implements      └──────────┘    implements              ▲          │
│  Serializable    implements      Serializable            │ uses     │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                          <<service>>                                │
│                                                                     │
│  ┌────────────────────┐    ┌────────────────┐    ┌──────────────┐  │
│  │AuthenticationService│   │ ClientService  │    │TaxReturnSvc  │  │
│  │────────────────────│    │────────────────│    │──────────────│  │
│  │-userRepository     │    │-clientRepo     │    │-taxReturnRepo│  │
│  │+authenticate()     │    │+addClient()    │    │+fileTaxReturn│  │
│  │+logLogin()         │    │+updateClient() │    │+getStats()   │  │
│  └────────────────────┘    │+deleteClient() │    └──────────────┘  │
│           │uses             └────────────────┘          │uses       │
└───────────┼─────────────────────────┼────────────────────┼─────────┘
            ▼                         ▼                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        <<repository>>                               │
│  ┌────────────────┐  ┌──────────────────┐  ┌───────────────────┐  │
│  │ UserRepository │  │ ClientRepository │  │TaxReturnRepository│  │
│  │────────────────│  │──────────────────│  │───────────────────│  │
│  │+findByUsername │  │+loadClients()    │  │+addTaxReturn()    │  │
│  │+addUser()      │  │+addClient()      │  │+findByClientId()  │  │
│  │+deleteUser()   │  │+updateClient()   │  │+findAll()         │  │
│  └────────────────┘  │+deleteClient()   │  └───────────────────┘  │
│           │uses       │+findClientById() │           │uses          │
│           └───────────┴──────────────────┴───────────┘             │
│                               │uses                                 │
└───────────────────────────────┼─────────────────────────────────────┘
                                ▼
                     ┌─────────────────┐
                     │  DatabaseUtil   │◄── ConfigLoader
                     │─────────────────│
                     │+getConnection() │
                     └─────────────────┘
```

**Quan hệ kế thừa UI:**
```
JFrame ◄── LoginFrame
JFrame ◄── MainFrame (chứa CardLayout với các JPanel sau)
JPanel ◄── DashboardPanel
JPanel ◄── ClientPanel
JPanel ◄── TaxCalculationPanel
JPanel ◄── ReturnsPanel
JPanel ◄── AdministrationPanel

Exception ◄── InvalidCredentialException
Exception ◄── InvalidDataException
```

📌 **Yêu cầu:** Xuất sơ đồ thành file ảnh (PNG hoặc JPG) và lưu tại `docs/class-diagram.png`.

---

## 6. Thiết kế lưu trữ dữ liệu (Database / File Design)

### 6.1. Hình thức lưu trữ

- [x] **Database (MySQL)**

**Mô tả lý do lựa chọn:**

> MySQL được chọn vì hệ thống cần lưu trữ dữ liệu quan hệ (tờ khai tham chiếu đến khách hàng qua foreign key), hỗ trợ query phức tạp (lọc, tổng hợp thống kê), và đảm bảo tính toàn vẹn dữ liệu (ACID). So với file CSV/JSON thì MySQL cho phép concurrent access an toàn và dễ scale khi dữ liệu tăng trưởng. So với SQLite thì MySQL phù hợp hơn khi triển khai cho nhiều máy trạm kết nối cùng một server. Kết nối được quản lý qua JDBC thuần (`DatabaseUtil`) và cấu hình qua `config.properties`.

---

### 6.2. Cấu trúc dữ liệu lưu trữ

| Tên bảng | Mô tả | Dữ liệu chính |
|---|---|---|
| `Users` | Tài khoản đăng nhập và hồ sơ nhân viên | `staff_id` (PK), `username` (UNIQUE), `password`, `role` (ADMIN/TAX_STAFF), `full_name`, `email`, `phone_number` |
| `Clients` | Hồ sơ khách hàng nộp thuế TNCN | `id` (PK — mã số thuế 9 chữ số), `name`, `income`, `dependents`, `marital_status`, `email`, `phone_number`, `city` |
| `TaxReturns` | Lịch sử tờ khai thuế đã nộp | `id` (AUTO_INCREMENT PK), `client_id` (FK → Clients), `filing_date`, `tax_liability`, `status` (Filed/Overdue), `marital_status` |

**Ràng buộc quan trọng:**
- `TaxReturns.client_id` có `FOREIGN KEY ... ON DELETE CASCADE` — xoá khách hàng sẽ tự động xoá toàn bộ tờ khai của khách đó.
- Index trên `Users(username)`, `Clients(name)`, `TaxReturns(client_id)`, `TaxReturns(filing_date)` để tăng tốc các query tìm kiếm thường xuyên.

---

## 7. Nhận xét về thiết kế

### Ưu điểm

- **Phân tầng rõ ràng** theo mô hình `UI → Service → Repository → Database`. Mỗi tầng có trách nhiệm đơn lẻ, dễ bảo trì và kiểm thử từng phần độc lập.
- **Tính nhất quán UI**: `AppTheme` tập trung toàn bộ hằng số màu sắc, font, và factory method tạo component — đảm bảo giao diện đồng nhất mà không hardcode giá trị rải rác.
- **Thuật toán tính thuế tách biệt**: `TaxCalculator` là class độc lập, không phụ thuộc UI hay database — dễ unit test và tái sử dụng.
- **SQL injection được ngăn ngừa**: Tất cả repository đều dùng `PreparedStatement` với tham số `?`, không nối chuỗi SQL trực tiếp.
- **Cấu hình ngoài code**: Thông số database trong `config.properties`, không hardcode — dễ deploy ở môi trường khác nhau.

### Hạn chế

- **Password lưu plain text**: `User.password` không được hash (có comment trong code: *"In real app, this should be hashed"*) — là lỗ hổng bảo mật nghiêm trọng nếu triển khai thực tế.
- **Không có connection pooling**: `DatabaseUtil.getConnection()` mở kết nối mới mỗi lần gọi — kém hiệu quả, có thể gây cạn connection khi tải cao.
- **Tính thuế đơn giản hoá**: `TaxCalculationService.calculateTax()` chỉ dùng flat rate từ `TaxBracket.getTaxRate()`, trong khi `TaxCalculator.applyProgressiveBrackets()` mới tính đúng luỹ tiến — hai nơi dùng logic khác nhau, dễ gây kết quả không nhất quán.
- **Không có phân quyền ở tầng service**: `ClientService` và `TaxReturnService` không kiểm tra role của người dùng — việc kiểm soát quyền chỉ dựa vào việc UI có hiển thị tab hay không.

### Hướng cải tiến trong tương lai

- Hash mật khẩu bằng **BCrypt** trước khi lưu database.
- Thay `DatabaseUtil` bằng **HikariCP** hoặc **c3p0** để connection pooling.
- Thống nhất logic tính thuế — loại bỏ `TaxCalculationService` hoặc delegate toàn bộ sang `TaxCalculator`.
- Thêm **Role-Based Access Control (RBAC)** ở tầng service.
- Viết **unit test** cho `TaxCalculator` và `ValidationUtil` với **JUnit 5**.

---

## 8. Kết luận

VTAX được thiết kế theo kiến trúc **3-tier (UI — Service — Repository)**, áp dụng đầy đủ 4 nguyên lý OOP cốt lõi của Java:

- **Encapsulation** giữ trạng thái object nhất quán qua getter/setter trong tầng Model.
- **Inheritance** tạo ra cây exception có ngữ nghĩa và tận dụng framework Swing.
- **Polymorphism** thông qua override (`paintComponent`, `toString`) và Swing event system.
- **Abstraction** che giấu chi tiết kỹ thuật (JDBC, thuật toán thuế) sau các API đơn giản.

Hệ thống triển khai đúng **Repository Pattern** để tách biệt truy cập dữ liệu, dùng **MySQL** làm lớp lưu trữ với schema quan hệ rõ ràng (3 bảng, foreign key, index), và sử dụng **CardLayout** để điều hướng single-window mượt mà. Phạm vi hiện tại phù hợp với mục tiêu học thuật về OOP; các điểm cần cải thiện chủ yếu nằm ở bảo mật và hiệu năng database.

---

## Design Decisions

### Vì sao dùng inheritance thay vì if-else?

Inheritance được dùng thay vì if-else ở hai nơi then chốt:

1. **Exception handling**: Thay vì `if (errorType == "credential") { ... } else if (errorType == "data") { ... }`, hệ thống khai báo `InvalidCredentialException` và `InvalidDataException` là hai class riêng. Caller có thể bắt từng loại chính xác:
   ```java
   try {
       authService.authenticate(user, pass);
   } catch (InvalidCredentialException e) {  // chỉ bắt lỗi đăng nhập
       showLoginError();
   }
   ```
   Cách này an toàn hơn (compiler kiểm tra checked exception), rõ ràng hơn, và dễ mở rộng thêm loại lỗi mới mà không cần sửa code cũ.

2. **Swing UI components**: Thay vì dùng if-else kiểm tra loại "màn hình" để vẽ, mỗi Panel kế thừa `JPanel` và tự quản lý layout/component của mình. `MainFrame` chỉ cần gọi `cardLayout.show(contentPane, key)` mà không cần biết panel đó render gì bên trong — đây chính là **polymorphism + inheritance** phối hợp loại bỏ chuỗi if-else phức tạp.

### Phần nào khó nhất?

**Thuật toán tính thuế luỹ tiến (Progressive Tax Bracket)** trong `TaxCalculator.applyProgressiveBrackets()` là phần phức tạp nhất về logic nghiệp vụ:

- Phải áp dụng đúng theo Luật Thuế TNCN Việt Nam: thu nhập chịu thuế = thu nhập bruto − giảm trừ bản thân (11M/tháng) − giảm trừ người phụ thuộc (4.4M/tháng/người).
- Tính theo năm nhưng input là thu nhập tháng.
- Mỗi phần thu nhập rơi vào bậc nào chỉ chịu thuế suất của bậc đó (luỹ tiến từng phần, không phải áp toàn bộ vào bậc cao nhất).
- Song song đó phải sinh ra HTML receipt chi tiết từng bậc để hiển thị cho người dùng.

Bên cạnh đó, việc dùng **JDBC thuần không có ORM** cũng đòi hỏi viết nhiều boilerplate code (try-with-resources, `PreparedStatement`, mapping `ResultSet` thủ công) nhưng giúp kiểm soát hoàn toàn câu SQL, phù hợp với mục tiêu học Java core và OOP.
