# Hướng Dẫn Tính Thuế Thu Nhập Cá Nhân (TNCN)

> **Hệ thống:** Tax Return Management System — DSAI1004 Project 6
> **Căn cứ pháp lý:** Luật Thuế TNCN Việt Nam, Thông tư 111/2013/TT-BTC

---

## 1. Tổng Quan Quy Trình

Hệ thống tính thuế theo **phương pháp lũy tiến từng phần** — nghĩa là mỗi phần thu nhập chịu thuế rơi vào bậc nào thì chỉ phần đó bị tính theo thuế suất của bậc đó, **không phải toàn bộ thu nhập**.

```
Thu nhập tháng (gộp)
        │
        ▼
 Trừ các khoản giảm trừ
        │
        ▼
 Thu nhập chịu thuế / tháng
        │
        ▼
  × 12 tháng
        │
        ▼
 Thu nhập chịu thuế / năm
        │
        ▼
 Áp biểu thuế lũy tiến 7 bậc
        │
        ▼
 Thuế TNCN phải nộp / năm
```

---

## 2. Các Khoản Giảm Trừ

Trước khi tính thuế, hệ thống trừ đi các khoản giảm trừ theo quy định:

| Khoản giảm trừ | Mức giảm trừ |
|---|---|
| **Giảm trừ bản thân** | **11.000.000 VND / tháng** |
| **Giảm trừ người phụ thuộc** | **4.400.000 VND / người / tháng** |

**Công thức tính giảm trừ:**

```
Tổng giảm trừ/tháng = 11.000.000 + (4.400.000 × số người phụ thuộc)
Thu nhập chịu thuế/tháng = max(0, Thu nhập tháng − Tổng giảm trừ)
Thu nhập chịu thuế/năm  = Thu nhập chịu thuế/tháng × 12
```

> Nếu thu nhập sau khi trừ ≤ 0, người đó **không phải nộp thuế**.

---

## 3. Biểu Thuế Lũy Tiến 7 Bậc

| Bậc | Thu nhập chịu thuế / năm | Thuế suất |
|:---:|---|:---:|
| 1 | Đến **60.000.000** VND (≤ 5 tr/tháng) | **5%** |
| 2 | 60.000.000 → **120.000.000** VND (5–10 tr/tháng) | **10%** |
| 3 | 120.000.000 → **216.000.000** VND (10–18 tr/tháng) | **15%** |
| 4 | 216.000.000 → **384.000.000** VND (18–32 tr/tháng) | **20%** |
| 5 | 384.000.000 → **624.000.000** VND (32–52 tr/tháng) | **25%** |
| 6 | 624.000.000 → **960.000.000** VND (52–80 tr/tháng) | **30%** |
| 7 | Trên **960.000.000** VND (> 80 tr/tháng) | **35%** |

---

## 4. Ví Dụ Minh Họa

---

### Ví dụ 1 — Không phải nộp thuế
> **Nhân viên văn phòng** · Thu nhập: **8.500.000 VND/tháng** · Không có người phụ thuộc

**Bước 1 — Tính giảm trừ:**
```
Giảm trừ bản thân  = 11.000.000 VND
Giảm trừ phụ thuộc =  0 × 4.400.000 = 0 VND
Tổng giảm trừ      = 11.000.000 VND
```

**Bước 2 — Thu nhập chịu thuế:**
```
Thu nhập chịu thuế/tháng = 8.500.000 − 11.000.000 = −2.500.000 → tính là 0
```

**Kết quả:**
```
✅ Thu nhập thấp hơn mức giảm trừ bản thân
   → Thuế TNCN phải nộp = 0 VND
   → Không cần nộp tờ khai (hoặc nộp với thuế = 0)
```

---

### Ví dụ 2 — Thu nhập trung bình thấp (1 bậc thuế)
> **Chuyên viên** · Thu nhập: **20.000.000 VND/tháng** · 1 người phụ thuộc

**Bước 1 — Tính giảm trừ:**
```
Giảm trừ bản thân   = 11.000.000 VND
Giảm trừ phụ thuộc  =  1 × 4.400.000 = 4.400.000 VND
Tổng giảm trừ       = 15.400.000 VND
```

**Bước 2 — Thu nhập chịu thuế:**
```
Thu nhập chịu thuế/tháng = 20.000.000 − 15.400.000 = 4.600.000 VND
Thu nhập chịu thuế/năm  = 4.600.000 × 12 = 55.200.000 VND
```

**Bước 3 — Áp biểu thuế lũy tiến:**

| Bậc | Phần thu nhập chịu thuế | Thuế suất | Thuế phải nộp |
|:---:|---:|:---:|---:|
| Bậc 1 | 55.200.000 VND | 5% | **2.760.000 VND** |

*(55.200.000 < 60.000.000 nên toàn bộ rơi vào Bậc 1)*

**Kết quả:**
```
Thuế TNCN / năm  = 2.760.000 VND
Thuế TNCN / tháng = 2.760.000 ÷ 12 = 230.000 VND
Thu nhập ròng / năm = (20.000.000 × 12) − 2.760.000 = 237.240.000 VND
Thuế suất thực tế = 2.760.000 ÷ 240.000.000 ≈ 1,15%
```

---

### Ví dụ 3 — Thu nhập khá (nhiều bậc thuế)
> **Kỹ sư cao cấp** · Thu nhập: **40.000.000 VND/tháng** · 2 người phụ thuộc

**Bước 1 — Tính giảm trừ:**
```
Giảm trừ bản thân   = 11.000.000 VND
Giảm trừ phụ thuộc  =  2 × 4.400.000 = 8.800.000 VND
Tổng giảm trừ       = 19.800.000 VND
```

**Bước 2 — Thu nhập chịu thuế:**
```
Thu nhập chịu thuế/tháng = 40.000.000 − 19.800.000 = 20.200.000 VND
Thu nhập chịu thuế/năm  = 20.200.000 × 12 = 242.400.000 VND
```

**Bước 3 — Áp biểu thuế lũy tiến:**

| Bậc | Khoảng | Phần chịu thuế | Thuế suất | Thuế phải nộp |
|:---:|---|---:|:---:|---:|
| Bậc 1 | 0 → 60.000.000 | 60.000.000 VND | 5% | 3.000.000 VND |
| Bậc 2 | 60.000.000 → 120.000.000 | 60.000.000 VND | 10% | 6.000.000 VND |
| Bậc 3 | 120.000.000 → 216.000.000 | 96.000.000 VND | 15% | 14.400.000 VND |
| Bậc 4 | 216.000.000 → **242.400.000** | 26.400.000 VND | 20% | 5.280.000 VND |

*(242.400.000 < 384.000.000 nên dừng ở Bậc 4)*

**Kết quả:**
```
Thuế TNCN / năm   = 3.000.000 + 6.000.000 + 14.400.000 + 5.280.000
                  = 28.680.000 VND
Thuế TNCN / tháng = 28.680.000 ÷ 12 = 2.390.000 VND
Thu nhập ròng / năm = (40.000.000 × 12) − 28.680.000 = 451.320.000 VND
Thuế suất thực tế = 28.680.000 ÷ 480.000.000 ≈ 5,98%
```

---

### Ví dụ 4 — Thu nhập cao (6 bậc thuế)
> **Giám đốc điều hành** · Thu nhập: **80.000.000 VND/tháng** · Không có người phụ thuộc

**Bước 1 — Tính giảm trừ:**
```
Giảm trừ bản thân   = 11.000.000 VND
Giảm trừ phụ thuộc  = 0 VND
Tổng giảm trừ       = 11.000.000 VND
```

**Bước 2 — Thu nhập chịu thuế:**
```
Thu nhập chịu thuế/tháng = 80.000.000 − 11.000.000 = 69.000.000 VND
Thu nhập chịu thuế/năm  = 69.000.000 × 12 = 828.000.000 VND
```

**Bước 3 — Áp biểu thuế lũy tiến:**

| Bậc | Khoảng | Phần chịu thuế | Thuế suất | Thuế phải nộp |
|:---:|---|---:|:---:|---:|
| Bậc 1 | 0 → 60.000.000 | 60.000.000 VND | 5% | 3.000.000 VND |
| Bậc 2 | 60.000.000 → 120.000.000 | 60.000.000 VND | 10% | 6.000.000 VND |
| Bậc 3 | 120.000.000 → 216.000.000 | 96.000.000 VND | 15% | 14.400.000 VND |
| Bậc 4 | 216.000.000 → 384.000.000 | 168.000.000 VND | 20% | 33.600.000 VND |
| Bậc 5 | 384.000.000 → 624.000.000 | 240.000.000 VND | 25% | 60.000.000 VND |
| Bậc 6 | 624.000.000 → **828.000.000** | 204.000.000 VND | 30% | 61.200.000 VND |

*(828.000.000 < 960.000.000 nên dừng ở Bậc 6)*

**Kết quả:**
```
Thuế TNCN / năm   = 3.000.000 + 6.000.000 + 14.400.000 + 33.600.000
                  + 60.000.000 + 61.200.000
                  = 178.200.000 VND
Thuế TNCN / tháng = 178.200.000 ÷ 12 = 14.850.000 VND
Thu nhập ròng / năm = (80.000.000 × 12) − 178.200.000 = 781.800.000 VND
Thuế suất thực tế = 178.200.000 ÷ 960.000.000 ≈ 18,56%
```

---

## 5. Bảng So Sánh Tổng Hợp

| Tình huống | Thu nhập/tháng | Phụ thuộc | Thuế/năm | Thuế/tháng | Thuế suất TT |
|---|---:|:---:|---:|---:|:---:|
| Nhân viên VP | 8.500.000 VND | 0 | **0 VND** | 0 VND | 0% |
| Chuyên viên | 20.000.000 VND | 1 | 2.760.000 VND | 230.000 VND | 1,15% |
| Kỹ sư cao cấp | 40.000.000 VND | 2 | 28.680.000 VND | 2.390.000 VND | 5,98% |
| Giám đốc | 80.000.000 VND | 0 | 178.200.000 VND | 14.850.000 VND | 18,56% |

> **Nhận xét:** Thuế suất thực tế luôn thấp hơn thuế suất của bậc cao nhất áp dụng. Đây là bản chất của hệ thống **lũy tiến từng phần** — chỉ phần vượt ngưỡng mới bị tính thuế suất cao hơn.

---

## 6. Trạng Thái Tờ Khai & Hạn Nộp

| Trạng thái | Ý nghĩa | Điều kiện |
|---|---|---|
| `Filed` (Đã nộp) | Tờ khai được nộp đúng hạn | Nộp trước hoặc vào **30/04** hàng năm |
| `Pending` (Chưa nộp) | Khách hàng chưa có tờ khai nào | Chưa từng nộp tờ khai |
| `Overdue` (Quá hạn) | Nộp trễ hạn | Nộp sau **30/04** hàng năm |

> **Căn cứ:** Điều 44, Luật Quản lý Thuế Việt Nam — Hạn nộp tờ khai quyết toán thuế TNCN là **ngày 30 tháng 4** của năm tiếp theo năm tính thuế.

---

## 7. Sơ Đồ Code Tương Ứng

```
Người dùng nhập:          monthlyIncome, dependents
        │
        ▼
TaxCalculator.calculateTax(monthlyIncome, dependents)
        │
        ├─► totalDeduction  = 11.000.000 + (4.400.000 × dependents)
        ├─► monthlyTaxable  = max(0, monthlyIncome − totalDeduction)
        ├─► annualTaxable   = monthlyTaxable × 12
        └─► applyProgressiveBrackets(annualTaxable)
                │
                └─► Duyệt 7 bậc, cộng dồn thuế từng phần
                         │
                         ▼
                   Trả về tổng thuế năm (VND)
        │
        ▼
TaxReturnService.fileTaxReturn(...)
        │
        ├─► Xác định status: nộp trước/sau 30/4?
        └─► Lưu vào returns.txt
```

---

*Tài liệu này được tạo tự động từ mã nguồn hệ thống. Mọi tính toán đều tuân thủ biểu thuế TNCN Việt Nam hiện hành.*
