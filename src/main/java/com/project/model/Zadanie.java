package com.project.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="zadanie")
// Adnotacja umożliwiająca autoamtyczne ustawienie pola z datą dodania
@EntityListeners(AuditingEntityListener.class)
public class Zadanie {
	// Zmienne reprezentujące pola tabeli zadanie
	@Id
	//Strategia IDENTITY - deleguje automatyczne generowanie indentyfikatora do bazy
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="zadanie_id")
	private Integer zadanieId;
	@ManyToOne
	@JoinColumn(name = "projekt_id")
	private Projekt projekt;
	@NotBlank(message = "Pole nazwa nie może być puste!")
	@Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
	@Column(nullable = false, length = 50)
	private String nazwa;
	private Integer kolejnosc;
	@Size(min = 3, max = 1000, message = "Opis musi zawierać od {min} do {max} znaków!")
	@Column(length = 1000)
	private String opis;
	@CreatedDate
	//updatable = false – po zapisaniu wartości przy tworzeniu, 
	//Hibernate NIE będzie aktualizował tej kolumny podczas zmian rekordu.
	@Column(name = "dataczas_dodania", nullable = false, updatable = false)
	private LocalDateTime dataCzasDodania;
	
	//Pusty konstruktor
	public Zadanie() {
		
	}
	
	// Konstruktor ze zmiennymi nazwa, opis i kolejnosc
	public Zadanie(String nazwa, String opis, Integer kolejnosc) {
		this.nazwa = nazwa;
		this.opis = opis;
		this.kolejnosc = kolejnosc;
	}
	
	
	// Gettry i settery
	public Integer getZadanieId() {
		return zadanieId;
	}

	public void setZadanieId(Integer zadanieId) {
		this.zadanieId = zadanieId;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public Integer getKolejnosc() {
		return kolejnosc;
	}

	public void setKolejnosc(Integer kolejnosc) {
		this.kolejnosc = kolejnosc;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public LocalDateTime getDataCzasDodania() {
		return dataCzasDodania;
	}

	public void setDataCzasDodania(LocalDateTime dataCzasDodania) {
		this.dataCzasDodania = dataCzasDodania;
	}
	
}
	


