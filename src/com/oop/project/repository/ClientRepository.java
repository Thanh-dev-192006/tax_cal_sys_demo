package com.oop.project.repository;

import com.oop.project.model.Client;
import com.oop.project.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ClientRepository - Lưu/đọc khách hàng từ clients.txt (pipe-separated UTF-8)
 *
 * Format mỗi dòng:
 *   id|name|income|dependents|maritalStatus|email|phoneNumber|city
 * Ví dụ:
 *   903-73-9276|Nguyen Ngan An|8500000.00|3|MARRIED|nguyenngan.an@gmail.com|0978796918|Quy Nhon
 */
public class ClientRepository {

    private static final String FILE_NAME = "clients.txt";
    private static final String SEP = "|";

    public void saveClients(List<Client> clients) {
        List<String> lines = new ArrayList<>();
        for (Client c : clients) {
            lines.add(String.join(SEP,
                    s(c.getId()),
                    s(c.getName()),
                    String.format("%.2f", c.getIncome()),
                    String.valueOf(c.getDependents()),
                    s(c.getMaritalStatus()),
                    s(c.getEmail()),
                    s(c.getPhoneNumber()),
                    s(c.getCity())));
        }
        FileUtil.writeLines(lines, FILE_NAME);
    }

    public List<Client> loadClients() {
        List<String> lines = FileUtil.readLines(FILE_NAME);
        List<Client> clients = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.split("\\|", -1);
            if (p.length >= 8) {
                try {
                    double income   = Double.parseDouble(p[2]);
                    int dependents  = Integer.parseInt(p[3]);
                    clients.add(new Client(p[0], p[1], income, dependents, p[4], p[5], p[6], p[7]));
                } catch (NumberFormatException ignored) {
                    // Skip malformed lines
                }
            }
        }
        return clients;
    }

    public void addClient(Client client) {
        List<Client> clients = loadClients();
        clients.add(client);
        saveClients(clients);
    }

    public void updateClient(Client updated) {
        List<Client> clients = loadClients();
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId().equals(updated.getId())) {
                clients.set(i, updated);
                break;
            }
        }
        saveClients(clients);
    }

    public void deleteClient(String clientId) {
        List<Client> clients = loadClients();
        clients.removeIf(c -> c.getId().equals(clientId));
        saveClients(clients);
    }

    public Client findClientById(String id) {
        return loadClients().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().orElse(null);
    }

    public List<Client> findClientsByName(String name) {
        String lower = name.toLowerCase();
        List<Client> result = new ArrayList<>();
        for (Client c : loadClients()) {
            if (c.getName().toLowerCase().contains(lower)) {
                result.add(c);
            }
        }
        return result;
    }

    private String s(String v) { return v == null ? "" : v; }
}
