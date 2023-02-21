package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.responses.Response;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cliente findById(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Response<Cliente>> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
        Response<Cliente> response = new Response<Cliente>();
        
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        
        
        Cliente updatedCliente = clienteRepository.save(cliente);
        response.setData(updatedCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        Cliente cliente = clienteRepository.findById(id)
        		.orElseThrow();        
        cliente.setNome(clienteDetails.getNome());
        cliente.setCpf(clienteDetails.getCpf());
        cliente.setTelefone(clienteDetails.getTelefone());
        cliente.setEndereco(clienteDetails.getEndereco());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setSenha(clienteDetails.getSenha());
        Cliente updatedCliente = clienteRepository.save(cliente);
        return updatedCliente;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
        		.orElseThrow();
        clienteRepository.delete(cliente);
    }
}