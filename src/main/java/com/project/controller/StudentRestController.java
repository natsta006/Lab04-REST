package com.project.controller;

import java.net.URI;
import java.util.Optional;

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

import com.project.service.StudentService;

import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import com.project.model.Student;


import org.springframework.data.domain.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api") 
@Tag(name = "Student") 
public class StudentRestController {
	private static final Logger logger = LoggerFactory.getLogger(StudentRestController.class);
	private StudentService studentService;
	
	@Autowired
	public StudentRestController(StudentService studentService) {
		 this.studentService = studentService;
	 }
	
	// Pobieranie studenta na podstawie ID
	 @GetMapping("/studenci/{studentId}")
	 ResponseEntity<Student> getStudent(@PathVariable("studentId") Integer studentId){
		 logger.info("Pobieranie studenta o ID: {}", studentId);
		 return ResponseEntity.of(studentService.getStudent(studentId)); 
	 }
	 
	 // Tworzenie nowego studenta
	 @PostMapping(path = "/studenci")
	 ResponseEntity<Void> createStudent(@Valid @RequestBody Student student) {
		 logger.info("Tworzenie nowego studenta");
		 Student createdStudent = studentService.setStudent(student); 
		 logger.info("Student utworzony z ID: {}", createdStudent.getStudentId());
		 URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			 .path("/{studentId}").buildAndExpand(createdStudent.getStudentId()).toUri();
		 return ResponseEntity.created(location).build(); 
	 } 
	 
	 // Aktualizacja danych studenta
	 @PutMapping("/studenci/{studentId}")
	 public ResponseEntity<Void> updateStudent(@Valid @RequestBody Student student,
	@PathVariable("studentId") Integer studentId) {
		 logger.info("Aktualizacja studenta o ID: {}", studentId);
		 return studentService.getStudent(studentId)
				 .map(p -> {
					 student.setStudentId(studentId);
					 studentService.setStudent(student);
					 logger.info("Student o ID: {} został zaktualizowany", studentId);
					 return new ResponseEntity<Void>(HttpStatus.OK);
				 })
		            .orElseGet(() -> {
		                logger.warn("Nie znaleziono studenta o ID: {}", studentId);
		                return ResponseEntity.notFound().build();
		            });		
	 }
	 
	 // Usunięcie studenta za pomocą ID
	 @DeleteMapping("/studenci/{studentId}")
	 public ResponseEntity<Void> deleteStudent(@PathVariable("studentId") Integer studentId) {
		 logger.info("Usuwanie studenta o ID: {}", studentId);
		 return studentService.getStudent(studentId).map(p -> {
			 studentService.deleteStudent(studentId);
			 logger.info("Student o ID: {} został usunięty", studentId);
			 return new ResponseEntity<Void>(HttpStatus.OK);
	       }).orElseGet(() -> {
	            logger.warn("Nie znaleziono studenta o ID: {}", studentId);
	            return ResponseEntity.notFound().build();
	        });
	 }
	 
	 // Zwraca stronę ze studentami
	 @GetMapping(value = "/studenci")
	 Page<Student> getStudenci(Pageable pageable) { 
		 logger.info("Pobieranie listy studentów");
		 return studentService.getStudenci(pageable);
	 }
	 
	 
	 // Wyszukiwanie studenta po nr indeksu
	 // Parametr 'exact' odróżnia ten endpoint od metody poniżej
	 @GetMapping(value = "/studenci", params={"nrIndeksu", "exact"})
	 Optional<Student> getStudenciByNrIndeksu(@RequestParam(name="nrIndeksu") String nrIndeksu) {
		 logger.info("Wyszukiwanie studenta o nr indeksu: {} ", nrIndeksu);
		 return studentService.searchByIndeks(nrIndeksu);
	 }
	 
	 // Wyszukiwanie studentów, których nr indeksu zaczyna się od podanego ciągu
	 @GetMapping(value = "/studenci", params="nrIndeksu")
	 Page<Student> getStudenciByNrIndeksuStartsWith(@RequestParam(name="nrIndeksu") String nrIndeksu, Pageable pageable) {
		 logger.info("Wyszukiwanie studentów, których nr indeksu zaczyna się na: {}", nrIndeksu);
		 return studentService.searchByNrIndeksuStartsWith(nrIndeksu, pageable);
	 }
	 
	// Wyszukiwanie studentów, których nazwisko zaczyna się od podanego ciągu
	 @GetMapping(value = "/studenci", params="nazwisko")
	 Page<Student> getStudenciByNazwiskoStartsWith(@RequestParam(name="nazwisko") String nazwisko, Pageable pageable) {
		 logger.info("Wyszukiwanie studentów, których nazwisko zaczyna się na: {}", nazwisko);
		 return studentService.searchByNazwiskoStartsWith(nazwisko, pageable);
	 }

}
