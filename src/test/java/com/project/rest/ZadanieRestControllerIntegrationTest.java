package com.project.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.model.Zadanie;
import com.project.service.ZadanieService;

import org.springframework.http.MediaType;





@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin")
public class ZadanieRestControllerIntegrationTest {
	private final String apiPath = "/api/zadania";
	 @MockBean
	 private ZadanieService mockZadanieService;
	
	 @Autowired
	 private MockMvc mockMvc;

	 
	 // Sprawdzenie poprawnego pobierania zadania po ID
	 @Test
	    public void getZadanie_whenValidId_shouldReturnGivenZadanie() throws Exception {
	        Integer id = 1;
	        Zadanie zadanie = new Zadanie();
	        zadanie.setZadanieId(id);
	        zadanie.setNazwa("Test Zadanie");
	        when(mockZadanieService.getZadanie(id)).thenReturn(Optional.of(zadanie));

	        mockMvc.perform(get(apiPath + "/{zadanieId}", id).accept(MediaType.APPLICATION_JSON))
	                .andDo(print())
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.zadanieId").value(id))
	                .andExpect(jsonPath("$.nazwa").value("Test Zadanie"));

	        verify(mockZadanieService, times(1)).getZadanie(id);
	        verifyNoMoreInteractions(mockZadanieService);
	    }

	 // Sprawdzenie odpowiedzi 404 przy żądaniu nieisniejącego zadania
	    @Test
	    public void getZadanie_whenInvalidId_shouldReturnNotFound() throws Exception {
	        Integer id = 99;
	        when(mockZadanieService.getZadanie(id)).thenReturn(Optional.empty());

	        mockMvc.perform(get(apiPath + "/{zadanieId}", id).accept(MediaType.APPLICATION_JSON))
	                .andDo(print())
	                .andExpect(status().isNotFound());
	        verify(mockZadanieService, times(1)).getZadanie(id);
	        verifyNoMoreInteractions(mockZadanieService);
	    }

	    // Sprawdzenie poprawnego pobierania listy zadań z paginacją
	    @Test
	    public void getZadania_whenTwoAvailable_shouldReturnContentWithPagingParams() throws Exception {
	        Zadanie zadanie1 = new Zadanie();
	        zadanie1.setZadanieId(1);
	        zadanie1.setNazwa("Zadanie 1");
	        Zadanie zadanie2 = new Zadanie();
	        zadanie2.setZadanieId(2);
	        zadanie2.setNazwa("Zadanie 2");

	        Page<Zadanie> page = new PageImpl<>(List.of(zadanie1, zadanie2));
	        when(mockZadanieService.getZadania((org.springframework.data.domain.Pageable) any(Pageable.class))).thenReturn(page);
	        mockMvc.perform(get(apiPath).contentType(MediaType.APPLICATION_JSON))
	                .andDo(print())
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.content").exists())
	                .andExpect(jsonPath("$.content.length()").value(2))
	                .andExpect(jsonPath("$.content[0].zadanieId").value(1))
	                .andExpect(jsonPath("$.content[1].zadanieId").value(2));
	        verify(mockZadanieService, times(1)).getZadania((org.springframework.data.domain.Pageable) any(Pageable.class));
	        verifyNoMoreInteractions(mockZadanieService);
	    }
	    
	    // Konfiguracja JacksonTester
	    @BeforeEach
	    public void before(TestInfo testInfo) {
	        System.out.printf("-- METODA -> %s%n", testInfo.getTestMethod().get().getName());
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new JavaTimeModule());
	        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	        JacksonTester.initFields(this, mapper);
	    }

	    // Logowanie zakończenia testu
	    @AfterEach
	    public void after(TestInfo testInfo) {
	        System.out.printf("<- KONIEC -- %s%n", testInfo.getTestMethod().get().getName());
	    }
}
