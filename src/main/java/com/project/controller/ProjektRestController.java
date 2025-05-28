package com.project.controller;
import java.net.URI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.project.model.Projekt;
import com.project.service.ProjektService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@RequestMapping("/api") 
@Tag(name = "Projekt") 
public class ProjektRestController {
private static final Logger logger = LoggerFactory.getLogger(ProjektRestController.class);

 private ProjektService projektService; 
 @Autowired
 public ProjektRestController(ProjektService projektService) {
 this.projektService = projektService;
 }
 @GetMapping("/projekty/{projektId}")
 public ResponseEntity<Projekt> getProjekt(@PathVariable("projektId") Integer projektId){
logger.info("Pobieranie projektu o ID: {}", projektId);
return ResponseEntity.of(projektService.getProjekt(projektId)); 
 } 
 @PostMapping(path = "/projekty")
 public ResponseEntity<Void> createProjekt(@Valid @RequestBody Projekt projekt) {
logger.info("Tworzenie nowego projektu");
 Projekt createdProjekt = projektService.setProjekt(projekt); 
 logger.info("Projekt utworzony z ID: {}", createdProjekt.getProjektId());
 URI location = ServletUriComponentsBuilder.fromCurrentRequest() 
 .path("/{projektId}").buildAndExpand(createdProjekt.getProjektId()).toUri();
 return ResponseEntity.created(location).build();
 } 
 @PutMapping("/projekty/{projektId}")
 public ResponseEntity<Void> updateProjekt(@Valid @RequestBody Projekt projekt,
@PathVariable("projektId") Integer projektId) {
	 logger.info("Aktualizacja projektu o ID: {}", projektId);
 return projektService.getProjekt(projektId)
 .map(p -> {
 projekt.setProjektId(projektId);
 projektService.setProjekt(projekt);
 logger.info("Projekt o ID: {} został zaktualizowany", projektId);
 return new ResponseEntity<Void>(HttpStatus.OK); 
 })
 .orElseGet(() -> {
     logger.warn("Projekt do aktualizacji o ID: {} nie został znaleziony", projektId);
     return ResponseEntity.notFound().build();
 });
 }
 @DeleteMapping("/projekty/{projektId}")
 public ResponseEntity<Void> deleteProjekt(@PathVariable("projektId") Integer projektId) {
	 logger.info("Usuwanie projektu o ID: {}", projektId);
 return projektService.getProjekt(projektId).map(p -> {
 projektService.deleteProjekt(projektId);
 logger.info("Projekt o ID: {} został usunięty", projektId);
 return new ResponseEntity<Void>(HttpStatus.OK); // 200
 }).orElseGet(() -> {
     logger.warn("Projekt do usunięcia o ID: {} nie został znaleziony", projektId);
     return ResponseEntity.notFound().build();
 });
 }
 @GetMapping(value = "/projekty")
 public Page<Projekt> getProjekty(Pageable pageable) { 
	 logger.info("Pobieranie listy projektów");

 return projektService.getProjekty(pageable); 
 }
 @GetMapping(value = "/projekty", params="nazwa")
 Page<Projekt> getProjektyByNazwa(@RequestParam(name="nazwa") String nazwa, Pageable pageable) {
	 logger.info("Wyszukiwanie projektów po nazwie: {}", nazwa);
 return projektService.searchByNazwa(nazwa, pageable);
 }
}
