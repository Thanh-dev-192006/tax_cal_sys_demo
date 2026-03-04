package com.oop.project.service;

import com.oop.project.exception.InvalidDataException;
import com.oop.project.model.Client;
import com.oop.project.repository.ClientRepository;

import java.util.List;

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
        // Validate SSN format (simple check)
        if (!client.getId().matches("\\d{3}-\\d{2}-\\d{4}")) {
            throw new InvalidDataException("Invalid SSN format");
        }
    }
}