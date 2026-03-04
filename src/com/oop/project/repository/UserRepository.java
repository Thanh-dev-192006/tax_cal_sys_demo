package com.oop.project.repository;

import com.oop.project.model.User;
import com.oop.project.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * UserRepository - Lưu/đọc tài khoản từ users.txt (pipe-separated UTF-8)
 *
 * Format mỗi dòng:
 *   staffId|username|password|role|fullName|email|phoneNumber
 * Ví dụ:
 *   NV001|admin|admin123|ADMIN|Nguyen Van Quan Tri|admin@tuvanthe.vn|0901000001
 */
public class UserRepository {

    private static final String FILE_NAME = "users.txt";
    private static final String SEP = "|";

    public void saveUsers(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(String.join(SEP,
                    s(u.getStaffId()),
                    s(u.getUsername()),
                    s(u.getPassword()),
                    s(u.getRole()),
                    s(u.getFullName()),
                    s(u.getEmail()),
                    s(u.getPhoneNumber())));
        }
        FileUtil.writeLines(lines, FILE_NAME);
    }

    public List<User> loadUsers() {
        List<String> lines = FileUtil.readLines(FILE_NAME);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.split("\\|", -1);
            if (p.length >= 7) {
                users.add(new User(p[0], p[1], p[2], p[3], p[4], p[5], p[6]));
            } else if (p.length >= 3) {
                // Legacy 3-field format (username|password|role)
                users.add(new User(p[0], p[1], p[2]));
            }
        }
        return users;
    }

    public User findUserByUsername(String username) {
        return loadUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    private String s(String v) { return v == null ? "" : v; }
}
