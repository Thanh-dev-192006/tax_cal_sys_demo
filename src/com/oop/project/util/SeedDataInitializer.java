package com.oop.project.util;

import com.oop.project.model.Client;
import com.oop.project.model.User;
import com.oop.project.repository.ClientRepository;
import com.oop.project.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * SeedDataInitializer - Khởi tạo dữ liệu mẫu 30 khách hàng + 10 tài khoản
 *
 * Thu nhập đơn vị: VND/tháng (ví dụ 15000000 = 15 triệu VND/tháng)
 * Giảm trừ bản thân: 11,000,000 VND/tháng
 * Giảm trừ NPT: 4,400,000 VND/tháng/người
 */
public class SeedDataInitializer {

    public static void initializeAllData() {
        initializeUsers();
        initializeClients();
        System.out.println("=== DA KHOI TAO DU LIEU ===");
        System.out.println("- 10 users (2 Admin + 8 Staff)");
        System.out.println("- 30 clients (thu nhap tinh bang VND/thang)");
    }

    public static void initializeUsers() {
        UserRepository userRepo = new UserRepository();
        if (!userRepo.loadUsers().isEmpty()) {
            System.out.println("Users da ton tai, bo qua khoi tao.");
            return;
        }

        List<User> users = new ArrayList<>();

        // 2 Admin
        users.add(new User("NV001", "admin", "admin123", "ADMIN",
                "Nguyen Van Quan Tri", "admin@tuvanthe.vn", "0901000001"));
        users.add(new User("NV002", "admin2", "Admin@123", "ADMIN",
                "Tran Thi Quan Ly", "manager@tuvanthe.vn", "0901000002"));

        // 8 Tax Staff
        users.add(new User("NV003", "nhanvien1", "Staff@123", "TAX_STAFF",
                "Le Van Hung", "hung.le@tuvanthe.vn", "0912000001"));
        users.add(new User("NV004", "nhanvien2", "Staff@123", "TAX_STAFF",
                "Pham Thi Lan", "lan.pham@tuvanthe.vn", "0912000002"));
        users.add(new User("NV005", "nhanvien3", "Staff@123", "TAX_STAFF",
                "Hoang Duc Minh", "minh.hoang@tuvanthe.vn", "0912000003"));
        users.add(new User("NV006", "nhanvien4", "Staff@123", "TAX_STAFF",
                "Vo Thi Mai", "mai.vo@tuvanthe.vn", "0912000004"));
        users.add(new User("NV007", "nhanvien5", "Staff@123", "TAX_STAFF",
                "Dang Van Tuan", "tuan.dang@tuvanthe.vn", "0912000005"));
        users.add(new User("NV008", "nhanvien6", "Staff@123", "TAX_STAFF",
                "Bui Thi Hoa", "hoa.bui@tuvanthe.vn", "0912000006"));
        users.add(new User("NV009", "nhanvien7", "Staff@123", "TAX_STAFF",
                "Ly Van Duc", "duc.ly@tuvanthe.vn", "0912000007"));
        users.add(new User("NV010", "nhanvien8", "Staff@123", "TAX_STAFF",
                "Ngo Thi Nga", "nga.ngo@tuvanthe.vn", "0912000008"));

        userRepo.saveUsers(users);
        System.out.println("Da khoi tao 10 users.");
    }

    /**
     * 30 khách hàng Việt Nam — Thu nhập đơn vị VND/tháng
     * Format constructor: (id, name, income_VND, dependents, maritalStatus, email, phone, city)
     *
     * Dải thu nhập thực tế:
     *   8,500,000 – 12,000,000   : Thu nhập thấp (dưới mức chịu thuế hoặc bậc 1-2)
     *   12,000,000 – 25,000,000  : Thu nhập trung bình (bậc 1-3)
     *   25,000,000 – 60,000,000  : Thu nhập khá (bậc 3-5)
     *   60,000,000 – 120,000,000 : Thu nhập cao (bậc 5-7)
     *   120,000,000+             : Thu nhập rất cao (bậc 6-7)
     */
    public static void initializeClients() {
        ClientRepository clientRepo = new ClientRepository();
        if (!clientRepo.loadClients().isEmpty()) {
            System.out.println("Clients da ton tai, bo qua khoi tao.");
            return;
        }

        List<Client> clients = new ArrayList<>();

        // Thu nhập thấp (8.5M – 12M VND/tháng)
        clients.add(new Client("903-73-9276", "Nguyen Ngan An",     8_500_000,  3, "MARRIED",
                "nguyenngan.an@gmail.com",    "0978796918", "Quy Nhon"));
        clients.add(new Client("771-50-1693", "Nguyen Trong Dai",   9_200_000,  0, "SINGLE",
                "nguyentrong.dai@gmail.com",  "0886764756", "Ha Noi"));
        clients.add(new Client("309-92-4299", "Nguyen Bao Tai",     9_800_000,  3, "MARRIED",
                "nguyenbao.tai@gmail.com",    "0937920331", "Vung Tau"));
        clients.add(new Client("201-90-7753", "Le Duc Minh",       10_500_000,  1, "MARRIED",
                "leduc.minh@gmail.com",       "0984545275", "Da Nang"));
        clients.add(new Client("889-28-0700", "Le Ba Phong",       11_200_000,  1, "WIDOWED",
                "leba.phong@gmail.com",       "0932774046", "Quy Nhon"));

        // Thu nhập trung bình thấp (12M – 20M VND/tháng)
        clients.add(new Client("476-32-9824", "Tran Tuan Anh",     12_000_000,  1, "MARRIED",
                "trantuan.anh@gmail.com",     "0862177727", "Da Nang"));
        clients.add(new Client("369-43-0810", "Nguyen Hoang Tuan", 13_500_000,  1, "MARRIED",
                "nguyenhoang.tuan@gmail.com", "0986557795", "Hue"));
        clients.add(new Client("935-99-7718", "Pham Huu Gia An",   14_000_000,  1, "MARRIED",
                "phamhuu.giaan@gmail.com",    "0936688456", "Ha Noi"));
        clients.add(new Client("809-32-0405", "To Hien Hai Dang",  15_500_000,  3, "MARRIED",
                "tohienhai.dang@gmail.com",   "0934426601", "Can Tho"));
        clients.add(new Client("316-46-8514", "Le Thi Mai",        16_500_000,  2, "MARRIED",
                "lethi.mai@gmail.com",        "0975395881", "Bien Hoa"));
        clients.add(new Client("536-67-4940", "Do Quang Trung",    18_000_000,  3, "MARRIED",
                "doquang.trung@gmail.com",    "0939459168", "Bien Hoa"));
        clients.add(new Client("133-05-1329", "Nguyen Thi Ngoc Han",20_000_000, 1, "DIVORCED",
                "nguyenthi.ngochan@gmail.com","0988173328", "Ha Noi"));

        // Thu nhập trung bình (20M – 40M VND/tháng)
        clients.add(new Client("796-73-3024", "Trinh Duc Thanh",   22_000_000,  0, "SINGLE",
                "trinhduc.thanh@gmail.com",   "0931582322", "Hai Phong"));
        clients.add(new Client("903-85-9015", "Nguyen Thi Nhien",  24_000_000,  3, "DIVORCED",
                "nguyenthi.nhien@gmail.com",  "0972974359", "Ho Chi Minh"));
        clients.add(new Client("734-01-9586", "Phan Dang Vu",      27_000_000,  0, "DIVORCED",
                "phandang.vu@gmail.com",      "0948539793", "Can Tho"));
        clients.add(new Client("839-42-4140", "Nguyen Tuan Anh",   30_000_000,  2, "MARRIED",
                "nguyentuan.anh@gmail.com",   "0913146030", "Vung Tau"));
        clients.add(new Client("531-14-9071", "Mai Huy Dang",      32_000_000,  2, "MARRIED",
                "maihuy.dang@gmail.com",      "0864593975", "Can Tho"));
        clients.add(new Client("138-07-3809", "Tran Van Nam",      35_000_000,  3, "DIVORCED",
                "tranvan.nam@gmail.com",      "0885576509", "Ha Noi"));
        clients.add(new Client("839-53-4517", "Nguyen Thanh Lan",  38_000_000,  3, "MARRIED",
                "nguyenthanh.lan@gmail.com",  "0869356266", "Quy Nhon"));

        // Thu nhập khá (40M – 80M VND/tháng)
        clients.add(new Client("246-19-9279", "Bui Dang Duong",    42_000_000,  1, "WIDOWED",
                "buidang.duong@gmail.com",    "0919641453", "Quy Nhon"));
        clients.add(new Client("289-64-7709", "Nguyen Khanh Vy",   48_000_000,  1, "WIDOWED",
                "nguyenkhanh.vy@gmail.com",   "0915540755", "Ho Chi Minh"));
        clients.add(new Client("496-96-7666", "Duong Dinh Anh",    55_000_000,  3, "MARRIED",
                "duongdinh.anh@gmail.com",    "0931825991", "Can Tho"));
        clients.add(new Client("263-92-4106", "Hoang Thi Thu",     60_000_000,  3, "MARRIED",
                "hoangthi.thu@gmail.com",     "0987641294", "Ha Noi"));
        clients.add(new Client("657-10-2807", "Vu Quoc Huy",       65_000_000,  1, "MARRIED",
                "vuquoc.huy@gmail.com",       "0965434434", "Ho Chi Minh"));
        clients.add(new Client("535-32-7373", "Vu Ngoc Hai",       72_000_000,  3, "MARRIED",
                "vungoc.hai@gmail.com",       "0909812594", "Hai Phong"));
        clients.add(new Client("121-11-3662", "Nguyen Phung Hoa",  75_000_000,  0, "SINGLE",
                "nguyenphung.hoa@gmail.com",  "0947349904", "Hai Phong"));

        // Thu nhập cao (80M – 180M VND/tháng)
        clients.add(new Client("937-05-3032", "Tran Khai Van",     85_000_000,  1, "WIDOWED",
                "trankhai.van@gmail.com",     "0942561553", "Hue"));
        clients.add(new Client("126-43-3780", "Dao Ngoc Hien",    100_000_000,  1, "WIDOWED",
                "daongoc.hien@gmail.com",     "0899523747", "Hue"));
        clients.add(new Client("172-63-0851", "Pham Minh Duc",    130_000_000,  3, "MARRIED",
                "phamminh.duc@gmail.com",     "0918034230", "Vung Tau"));
        clients.add(new Client("594-77-7260", "Nguyen Duc Huy",   160_000_000,  3, "WIDOWED",
                "nguyenduc.huy@gmail.com",    "0988992740", "Ha Noi"));

        clientRepo.saveClients(clients);
        System.out.println("Da khoi tao 30 clients (thu nhap tinh bang VND/thang).");
    }
}
