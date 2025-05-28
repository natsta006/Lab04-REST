package com.project.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name="projekt",
		indexes = {
				//Indeksowanie kolumn najczęściej używanych do wyszukiwania projektów
				@Index(name = "idx_nazwa", columnList = "nazwa"),
				@Index(name = "idx_data_oddania", columnList = "data_oddania")		
		}
) 
// Adnotacja umożliwiająca autoamtyczne ustawienie pola z datą dodania
@EntityListeners(AuditingEntityListener.class)
public class Projekt {
	// Zmienne reprezentujące pola tabeli
	@Id
	//Strategia IDENTITY - deleguje automatyczne generowanie indentyfikatora do bazy
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="projekt_id")
	private Integer projektId;
	@NotBlank(message = "Pole nazwa nie może być puste!")
	@Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
	@Column(nullable = false, length = 50)
	private String nazwa;
	@Size(min = 3, max = 1000, message = "Opis musi zawierać od {min} do {max} znaków!")
	@Column(length = 1000)
	private String opis;
	@CreatedDate
	//updatable = false – po zapisaniu wartości przy tworzeniu, 
	//Hibernate NIE będzie aktualizował tej kolumny podczas zmian rekordu.
	@Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
	private LocalDateTime dataCzasUtworzenia;
	@Column(name = "data_oddania")
	private LocalDate dataOddania;
	
	// Lista zadań przypisanych do projektu
	// cascade = all sprawia, że operacje na projekcie dotyczą też zadań
	//orphanRemoval = true, usuwa zadania przypisane podczas usuwania projektu
	@OneToMany(mappedBy = "projekt", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties({"projekt"})
	private List<Zadanie> zadania;
	
	
	// Studenci przypisani do projektu
	@ManyToMany
	 @JoinTable(name = "projekt_student",
	 joinColumns = {@JoinColumn(name="projekt_id")},
	 inverseJoinColumns = {@JoinColumn(name="student_id")})
	 private Set<Student> studenci;

	

	// Pusty konstruktor
	public Projekt() {
		
	}
	
	// Konstruktor ze zmniennymi nazwa i opis
	public Projekt(String nazwa, String opis) {
		this.nazwa = nazwa;
		this.opis = opis;
		
	}
	
	// Konstruktor z nazwa, opisem i data oddania - potrzebny do testow
	public Projekt(String nazwa, String opis, LocalDate dataOddania) {
	    this.nazwa = nazwa;
	    this.opis = opis;
	    this.dataOddania = dataOddania; 
	}
	
	
	// Konstruktor z projektId, nazwa, opis i odataOddania - potrzebny do testow
	 public Projekt(Integer projektId, String nazwa, String opis, LocalDate dataOddania){
		 this.projektId = projektId;
		 this.nazwa = nazwa;
		 this.opis = opis;
		 this.dataOddania = dataOddania;

	}
	
			
	
	// Konstruktor ze wszystkimi zmiennymi - potrzebny do testow
	 public Projekt(Integer projektId, String nazwa, String opis, LocalDateTime dataCzasUtworzenia, LocalDate dataOddania){
		 this.projektId = projektId;
		 this.nazwa = nazwa;
		 this.opis = opis;
		 this.dataCzasUtworzenia = dataCzasUtworzenia;
		 this.dataOddania = dataOddania;

	}

	

	// gettery i settery
	public Integer getProjektId() {
		return projektId;
	}
	public void setProjektId(Integer projektId) {
		this.projektId = projektId;
	}
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public LocalDateTime getDataCzasUtworzenia() {
		return dataCzasUtworzenia;
	}
	public void setDataCzasUtworzenia(LocalDateTime dataCzasUtworzenia) {
		this.dataCzasUtworzenia = dataCzasUtworzenia;
	}
	public LocalDate getDataOddania() {
		return dataOddania;
	}
	public void setDataOddania(LocalDate dataOddania) {
		this.dataOddania = dataOddania;
	}
	public List<Zadanie> getZadania() {
		return zadania;
	}

	public void setZadania(List<Zadanie> zadania) {
		this.zadania = zadania;
	}
	
}