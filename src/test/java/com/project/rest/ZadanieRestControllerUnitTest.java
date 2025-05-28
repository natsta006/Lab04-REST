package com.project.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.controller.ZadanieRestController;
import com.project.model.Zadanie;
import com.project.service.ZadanieService;

@ExtendWith(MockitoExtension.class)
class ZadanieRestControllerUnitTest {
    @InjectMocks
    private ZadanieRestController zadanieRestController;
    @Mock
    private ZadanieService mockZadanieService;

    // Sprawdzanie odpowiedzi przy prawidłowym pobieraniu zadania
    @Test
    void getZadanie_whenExists_shouldReturnOk() {
        Integer id = 1;
        Zadanie zadanie = new Zadanie();
        zadanie.setZadanieId(id);
        when(mockZadanieService.getZadanie(id)).thenReturn(Optional.of(zadanie));
        
        ResponseEntity<Zadanie> response = zadanieRestController.getZadanie(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(zadanie, response.getBody());
    }

    // Sprawdzenie odppwiedzi, jeśli zadanie nie isnieje
    @Test
    void getZadanie_whenNotExists_shouldReturnNotFound() {
        Integer id = 99;
        when(mockZadanieService.getZadanie(id)).thenReturn(Optional.empty());

        ResponseEntity<Zadanie> response = zadanieRestController.getZadanie(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    // Sprawdzenie usuwania istniejącego zadania
    @Test
    void deleteZadanie_whenExists_shouldReturnOk() {
        Integer id = 1;
        Zadanie zadanie = new Zadanie();
        zadanie.setZadanieId(id);
        when(mockZadanieService.getZadanie(id)).thenReturn(Optional.of(zadanie));

        ResponseEntity<Void> response = zadanieRestController.deleteZadanie(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
