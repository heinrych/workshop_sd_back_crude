package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Animal;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.responses.Response;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {

    @Autowired
    private AnimalRepository animalRepository;

    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    @GetMapping("/{id}")
    public Animal findById(@PathVariable Long id) {
        return animalRepository.findById(id)
                .orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Response<Animal>> create(@Valid @RequestBody Animal animal, BindingResult result) {
        Response<Animal> response = new Response<Animal>();
        
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        
        
        Animal updatedCliente = animalRepository.save(animal);
        response.setData(updatedCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }





    @PutMapping("/{id}")
    public Animal update(@PathVariable Long id, @RequestBody Animal animalDetails) {
    	Animal animal = animalRepository.findById(id)
        		.orElseThrow(); 

        animal.setNome(animalDetails.getNome());
        animal.setEspecie(animalDetails.getEspecie());
        animal.setRaca(animalDetails.getRaca());
        animal.setIdade(animalDetails.getIdade());
    	animal.setEndereco(animalDetails.getEndereco());
        animal.setDescricao(animalDetails.getDescricao());
    	

        Animal updatedAnimal = animalRepository.save(animal);
        return updatedAnimal;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Animal> deleteAnimal(@PathVariable(value = "id") Long animalId) {
    	Animal animal = animalRepository.findById(animalId)
        		.orElseThrow(); 
        if (animal == null) {
            return ResponseEntity.notFound().build();
        }

        animalRepository.delete(animal);
        return ResponseEntity.ok().build();
    }
}