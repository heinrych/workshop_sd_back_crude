package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Historico;
import com.example.demo.repository.HistoricoRepository;
import com.example.demo.responses.Response;

@RestController
@RequestMapping("/historico")
public class HistoricoController {

  @Autowired
  private HistoricoRepository historicoRepository;

  @GetMapping
  public List<Historico> listarTodos() {
    return historicoRepository.findAll();
  }

  @GetMapping("/{id}")
  public Historico findById(@PathVariable Long id) {
      return historicoRepository.findById(id)
              .orElseThrow();
  }
  
  
  @PutMapping("/{id}")
  public Historico update(@PathVariable Long id, @RequestBody Historico historicoDetails) {
      Historico historico = historicoRepository.findById(id)
      		.orElseThrow();        
      historico.setIdAnimal(historicoDetails.getIdAnimal());
      historico.setIdCliente(historicoDetails.getIdCliente());
      historico.setData(historicoDetails.getData());
      historico.setTipoOperacao(historicoDetails.getTipoOperacao());
      Historico updateHistorico = historicoRepository.save(historico);
      return updateHistorico;
  }

  @PostMapping
  public ResponseEntity<Response<Historico>> create(@Valid @RequestBody Historico historico, BindingResult result) {
      Response<Historico> response = new Response<Historico>();
      
      if (result.hasErrors()) {
          result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
          return ResponseEntity.badRequest().body(response);
      }
      
      
      Historico updatedCliente = historicoRepository.save(historico);
      response.setData(updatedCliente);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  
  @DeleteMapping("/{id}")
  public ResponseEntity<Historico> deleteHistorico(@PathVariable(value = "id") Long historicoID) {
  	Historico historico = historicoRepository.findById(historicoID)
      		.orElseThrow(); 
      if (historico == null) {
          return ResponseEntity.notFound().build();
      }

      historicoRepository.delete(historico);
      return ResponseEntity.ok().build();
  }

}
