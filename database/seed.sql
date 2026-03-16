-- =====================================================
-- Tax Return System - Seed Data
-- =====================================================

USE tax_return_system;

-- =====================================================
-- Xoa du lieu cu (neu co)
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE TaxReturns;
TRUNCATE TABLE Users;
TRUNCATE TABLE Clients;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- Insert Users (10 users)
-- =====================================================

-- 2 Admin users
INSERT INTO Users (staff_id, username, password, role, full_name, email, phone_number) VALUES
('NV001', 'admin', 'admin123', 'ADMIN', 'Nguyen Van Quan Tri', 'admin@tuvanthe.vn', '0901000001'),
('NV002', 'admin2', 'Admin@456', 'ADMIN', 'Tran Thi Quan Ly', 'manager@tuvanthe.vn', '0901000002');

-- 8 Tax Staff users
INSERT INTO Users (staff_id, username, password, role, full_name, email, phone_number) VALUES
('NV003', 'nhanvien1', 'Staff@123', 'TAX_STAFF', 'Le Van Hung', 'hung.le@tuvanthe.vn', '0912000001'),
('NV004', 'nhanvien2', 'Staff@123', 'TAX_STAFF', 'Pham Thi Lan', 'lan.pham@tuvanthe.vn', '0912000002'),
('NV005', 'nhanvien3', 'Staff@123', 'TAX_STAFF', 'Hoang Duc Minh', 'minh.hoang@tuvanthe.vn', '0912000003'),
('NV006', 'nhanvien4', 'Staff@123', 'TAX_STAFF', 'Vo Thi Mai', 'mai.vo@tuvanthe.vn', '0912000004'),
('NV007', 'nhanvien5', 'Staff@123', 'TAX_STAFF', 'Dang Van Tuan', 'tuan.dang@tuvanthe.vn', '0912000005'),
('NV008', 'nhanvien6', 'Staff@123', 'TAX_STAFF', 'Bui Thi Hoa', 'hoa.bui@tuvanthe.vn', '0912000006'),
('NV009', 'nhanvien7', 'Staff@123', 'TAX_STAFF', 'Ly Van Duc', 'duc.ly@tuvanthe.vn', '0912000007'),
('NV010', 'nhanvien8', 'Staff@123', 'TAX_STAFF', 'Ngo Thi Nga', 'nga.ngo@tuvanthe.vn', '0912000008');

-- =====================================================
-- Insert Clients (30 clients)
-- =====================================================

INSERT INTO Clients (id, name, income, dependents, marital_status, email, phone_number, city) VALUES
('903-73-9276', 'Nguyen Ngan An', 26200000.0, 3, 'MARRIED', 'nguyenngan.an@gmail.com', '0978796918', 'Quy Nhon'),
('935-99-7718', 'Pham Huu Gia An', 132600000.0, 1, 'MARRIED', 'phamhuu.giaan@gmail.com', '0936688456', 'Ha Noi'),
('839-42-4140', 'Nguyen Tuan Anh', 36363636.0, 2, 'MARRIED', 'nguyentuan.anh@gmail.com', '0913146030', 'Vung Tau'),
('476-32-9824', 'Tran Tuan Anh', 36676767.0, 1, 'MARRIED', 'trantuan.anh@gmail.com', '0862177727', 'Da Nang'),
('531-14-9071', 'Mai Huy Dang', 26410000.0, 2, 'MARRIED', 'maihuy.dang@gmail.com', '0864593975', 'Can Tho'),
('771-50-1693', 'Nguyen Trong Dai', 34000000.0, 0, 'SINGLE', 'nguyentrong.dai@gmail.com', '0886764756', 'Ha Noi'),
('809-32-0405', 'To Hien Hai Dang', 6070000.0, 3, 'MARRIED', 'tohienhai.dang@gmail.com', '0934426601', 'Can Tho'),
('246-19-9279', 'Bui Dang Duong', 29530000.0, 1, 'WIDOWED', 'buidang.duong@gmail.com', '0919641453', 'Quy Nhon'),
('535-32-7373', 'Vu Ngoc Hai', 24000000.0, 3, 'MARRIED', 'vungoc.hai@gmail.com', '0909812594', 'Hai Phong'),
('126-43-3780', 'Dao Ngoc Hien', 9534000.0, 1, 'WIDOWED', 'daongoc.hien@gmail.com', '0899523747', 'Hue'),
('133-05-1329', 'Nguyen Thi Ngoc Han', 9480000.0, 1, 'DIVORCED', 'nguyenthi.ngochan@gmail.com', '0988173328', 'Ha Noi'),
('201-90-7753', 'Le Duc Minh', 4310000.0, 1, 'MARRIED', 'leduc.minh@gmail.com', '0984545275', 'Da Nang'),
('369-43-0810', 'Nguyen Hoang Tuan', 19750000.0, 1, 'MARRIED', 'nguyenhoang.tuan@gmail.com', '0986557795', 'Hue'),
('796-73-3024', 'Trinh Duc Thanh', 11630000.0, 0, 'SINGLE', 'trinhduc.thanh@gmail.com', '0931582322', 'Hai Phong'),
('536-67-4940', 'Do Quang Trung', 7650000.0, 3, 'MARRIED', 'doquang.trung@gmail.com', '0939459168', 'Bien Hoa'),
('121-11-3662', 'Nguyen Phung Hoa', 8797000.0, 0, 'SINGLE', 'nguyenphung.hoa@gmail.com', '0947349904', 'Hai Phong'),
('289-64-7709', 'Nguyen Khanh Vy', 2537000.0, 1, 'WIDOWED', 'nguyenkhanh.vy@gmail.com', '0915540755', 'Ho Chi Minh'),
('594-77-7260', 'Nguyen Duc Huy', 6649000.0, 3, 'WIDOWED', 'nguyenduc.huy@gmail.com', '0988992740', 'Ha Noi'),
('657-10-2807', 'Vu Quoc Huy', 30210000.0, 1, 'MARRIED', 'vuquoc.huy@gmail.com', '0965434434', 'Ho Chi Minh'),
('889-28-0700', 'Le Ba Phong', 15830000.0, 1, 'WIDOWED', 'leba.phong@gmail.com', '0932774046', 'Quy Nhon'),
('309-92-4299', 'Nguyen Bao Tai', 8060000.0, 3, 'MARRIED', 'nguyenbao.tai@gmail.com', '0937920331', 'Vung Tau'),
('734-01-9586', 'Phan Dang Vu', 15060000.0, 0, 'DIVORCED', 'phandang.vu@gmail.com', '0948539793', 'Can Tho'),
('496-96-7666', 'Duong Dinh Anh', 11650000.0, 3, 'MARRIED', 'duongdinh.anh@gmail.com', '0931825991', 'Can Tho'),
('903-85-9015', 'Nguyen Thi Nhien', 12100000.0, 3, 'DIVORCED', 'nguyenthi.nhien@gmail.com', '0972974359', 'Ho Chi Minh'),
('937-05-3032', 'Tran Khai Van', 5952000.0, 1, 'WIDOWED', 'trankhai.van@gmail.com', '0942561553', 'Hue'),
('839-53-4517', 'Nguyen Thanh Lan', 6680000.0, 3, 'MARRIED', 'nguyenthanh.lan@gmail.com', '0869356266', 'Quy Nhon'),
('172-63-0851', 'Pham Minh Duc', 16337000.0, 3, 'MARRIED', 'phamminh.duc@gmail.com', '0918034230', 'Vung Tau'),
('316-46-8514', 'Le Thi Mai', 18500000.0, 2, 'MARRIED', 'lethi.mai@gmail.com', '0975395881', 'Bien Hoa'),
('138-07-3809', 'Tran Van Nam', 23960000.0, 3, 'DIVORCED', 'tranvan.nam@gmail.com', '0885576509', 'Ha Noi'),
('263-92-4106', 'Hoang Thi Thu', 20500000.0, 3, 'MARRIED', 'hoangthi.thu@gmail.com', '0987641294', 'Ha Noi');

-- =====================================================
-- Verify data
-- =====================================================
SELECT COUNT(*) AS total_users FROM Users;
SELECT COUNT(*) AS total_clients FROM Clients;

SELECT 'Database seeded successfully!' AS status;