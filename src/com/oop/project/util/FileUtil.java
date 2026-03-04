package com.oop.project.util;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtil - Đọc/ghi file văn bản UTF-8 (Sequential File I/O)
 *
 * Format dữ liệu: mỗi dòng = 1 bản ghi, các trường phân cách bởi '|'
 * Ví dụ clients.txt:
 *   903-73-9276|Nguyen Ngan An|8500000.00|3|MARRIED|email@gmail.com|0978796918|Quy Nhon
 */
public final class FileUtil {

    /**
     * Ghi danh sách dòng văn bản ra file (UTF-8, ghi đè)
     */
    public static void writeLines(List<String> lines, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error writing file \"" + fileName + "\": " + e.getMessage(),
                    "File I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Đọc tất cả dòng từ file văn bản UTF-8
     * Bỏ qua dòng trắng
     */
    public static List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return lines;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    lines.add(trimmed);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error reading file \"" + fileName + "\": " + e.getMessage(),
                    "File I/O Error", JOptionPane.ERROR_MESSAGE);
        }
        return lines;
    }

    private FileUtil() {}
}
