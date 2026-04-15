package com.oop.project.service;

import com.oop.project.exception.InvalidDataException;
import com.oop.project.model.Client;
import com.oop.project.repository.ClientRepository;
import com.oop.project.util.ValidationUtil;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientService {
    private ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepository();
    }

    public void addClient(Client client) throws InvalidDataException {
        validateClient(client);
        clientRepository.addClient(client);
    }

    public void updateClient(Client client) throws InvalidDataException {
        validateClient(client);
        clientRepository.updateClient(client);
    }

    public void deleteClient(String clientId) {
        clientRepository.deleteClient(clientId);
    }

    public Client findClientById(String id) {
        return clientRepository.findClientById(id);
    }

    public List<Client> findClientsByName(String name) {
        return clientRepository.findClientsByName(name);
    }

    public List<Client> getAllClients() {
        return clientRepository.loadClients();
    }

    /**
     * Tự động sinh Tax ID 9 chữ số ngẫu nhiên, đảm bảo không trùng với ID đã có.
     */
    public String generateUniqueId() {
        List<Client> existing = clientRepository.loadClients();
        Set<String> usedIds = existing.stream()
                .map(Client::getId)
                .collect(Collectors.toSet());

        Random rng = new Random();
        String candidate;
        do {
            // Sinh số từ 100_000_000 đến 999_999_999 (luôn đủ 9 chữ số)
            int num = 100_000_000 + rng.nextInt(900_000_000);
            candidate = String.valueOf(num);
        } while (usedIds.contains(candidate));

        return candidate;
    }

    private void validateClient(Client client) throws InvalidDataException {
        if (client.getId() == null || client.getId().isEmpty()) {
            throw new InvalidDataException("Client ID is required");
        }
        if (client.getName() == null || client.getName().isEmpty()) {
            throw new InvalidDataException("Client name is required");
        }
        if (client.getIncome() < 0) {
            throw new InvalidDataException("Income cannot be negative");
        }
        // Validate Tax ID format (9 digits)
        if (!ValidationUtil.isValidTaxID(client.getId())) {
            throw new InvalidDataException("Invalid Tax ID format. Must be 9 digits.");
        }
    }
}