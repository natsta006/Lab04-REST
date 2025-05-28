package com.project.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import com.project.service.ZadanieService;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import com.project.model.Zadanie;
import org.springframework.data.domain.Page;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





@RestController
@RequestMapping("/api") 
@Tag(name = "Zadanie") 
public class ZadanieRestController {
	private static final Logger logger = LoggerFactory.getLogger(ZadanieRestController.class);
	private ZadanieService zadanieService;
	
	@Autowired
	public ZadanieRestController(ZadanieService zadanieService) {
		 this.zadanieService = zadanieService;
	 }
	
	// Pobieranie zadania na podstawie ID
	 @GetMapping("/zadania/{zadanieId}")
	public
	 ResponseEntity<Zadanie> getZadanie(@PathVariable("zadanieId") Integer zadanieId){
		 logger.info("Pobieranie zadania o ID: {}", zadanieId);
		 return ResponseEntity.of(zadanieService.getZadanie(zadanieId)); 
	 }
	 
	 // Tworzenie nowego zadania
	 @PostMapping(path = "/zadania")
	 ResponseEntity<Void> createZadanie(@Valid @RequestBody Zadanie zadanie) {
		 logger.info("Tworzenie nowego zadania");
		 Zadanie createdZadanie = zadanieService.setZadanie(zadanie); 
		 logger.info("Zadanie utworzone z ID: {}", createdZadanie.getZadanieId());
		 URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			 .path("/{zadanieId}").buildAndExpand(createdZadanie.getZadanieId()).toUri();
		 return ResponseEntity.created(location).build(); 
	 } 
	 
	 // Aktualizacja danych zadania
	 @PutMapping("/zadania/{zadanieId}")
	 ResponseEntity<Void> updateZadanie(@Valid @RequestBody Zadanie zadanie,
	@PathVariable("zadanieId") Integer zadanieId) {
		 logger.info("Aktualizacja zadania o ID: {}", zadanieId);
		 return zadanieService.getZadanie(zadanieId)
				 .map(p -> {
					 zadanieService.setZadanie(zadanie);
					 logger.info("Zadanie o ID: {} zaktualizowane", zadanieId);
					 return new ResponseEntity<Void>(HttpStatus.OK);
				 })
	                .orElseGet(() -> {
	                    logger.warn("Nie znaleziono zadania do aktualizacji o ID: {}", zadanieId);
	                    return ResponseEntity.notFound().build();
	                }); 
	 }
	 
	 // Usuwanie zadania
	 @DeleteMapping("/zadania/{zadanieId}")
	public
	 ResponseEntity<Void> deleteZadanie(@PathVariable("zadanieId") Integer zadanieId) {
		 logger.info("Usuwanie zadania o ID: {}", zadanieId);
		 return zadanieService.getZadanie(zadanieId).map(p -> {
			 zadanieService.deleteZadanie(zadanieId);
			 logger.info("Zadanie o ID: {} zostało usunięte", zadanieId);
			 return new ResponseEntity<Void>(HttpStatus.OK);
         }).orElseGet(() -> {
             logger.warn("Nie znaleziono zadania do usunięcia o ID: {}", zadanieId);
             return ResponseEntity.notFound().build();
         });
	 }
	 
	 // Pobranie listy zadań
	 @GetMapping(value = "/zadania")
	 Page<Zadanie> getZadania(Pageable pageable) { 
		 logger.info("Pobieranie listy zadań");
		 return zadanieService.getZadania(pageable);
	 }
	 
	 // Pobranie zadań na podstawie ID projektu, do którego są przypisane
	 @GetMapping(value = "/zadania", params="projektId")
	 Page<Zadanie> getZadaniaByProjektId(@RequestParam(name="projektId") Integer projektId, Pageable pageable) {
		 logger.info("Pobieranie zadań dla projektu o ID: {}", projektId);
		 return zadanieService.getZadaniaProjektu(projektId, pageable);
	 }

}
