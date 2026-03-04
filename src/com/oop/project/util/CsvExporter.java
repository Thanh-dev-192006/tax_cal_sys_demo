package com.oop.project.util;

import com.oop.project.model.Client;
import com.oop.project.model.TaxReturn;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * CsvExporter — Export client list / tax returns to a CSV file.
 * Writes a UTF-8 BOM so Excel opens Vietnamese names correctly.
 */
public final class CsvExporter {

    public static void exportClients(List<Client> clients, JComponent parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save CSV — Client List");
        chooser.setSelectedFile(new File("clients_export.csv"));
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(chooser.getSelectedFile()),
                        StandardCharsets.UTF_8))) {
            w.write('\uFEFF'); // UTF-8 BOM
            w.write("Tax ID,Full Name,Monthly Income (VND),Dependents,Marital Status,Email,Phone,City");
            w.newLine();
            for (Client c : clients) {
                w.write(String.join(",",
                        esc(c.getId()),
                        esc(c.getName()),
                        String.format("%.0f", c.getIncome()),
                        String.valueOf(c.getDependents()),
                        esc(c.getMaritalStatus()),
                        esc(c.getEmail()),
                        esc(c.getPhoneNumber()),
                        esc(c.getCity())));
                w.newLine();
            }
            JOptionPane.showMessageDialog(parent,
                    "CSV exported successfully:\n" + chooser.getSelectedFile().getAbsolutePath(),
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent,
                    "Export failed: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void exportReturns(List<TaxReturn> returns, JComponent parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save CSV — Tax Returns");
        chooser.setSelectedFile(new File("returns_export.csv"));
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(chooser.getSelectedFile()),
                        StandardCharsets.UTF_8))) {
            w.write('\uFEFF');
            w.write("Tax ID,Filing Date,Tax Liability (VND),Status,Marital Status");
            w.newLine();
            for (TaxReturn tr : returns) {
                w.write(String.join(",",
                        esc(tr.getClientId()),
                        String.valueOf(tr.getFilingDate()),
                        String.format("%.0f", tr.getTaxLiability()),
                        esc(tr.getStatus()),
                        esc(tr.getMaritalStatus())));
                w.newLine();
            }
            JOptionPane.showMessageDialog(parent,
                    "CSV exported successfully:\n" + chooser.getSelectedFile().getAbsolutePath(),
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent,
                    "Export failed: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String esc(String v) {
        if (v == null) return "";
        if (v.contains(",") || v.contains("\"") || v.contains("\n"))
            return "\"" + v.replace("\"", "\"\"") + "\"";
        return v;
    }

    private CsvExporter() {}
}
