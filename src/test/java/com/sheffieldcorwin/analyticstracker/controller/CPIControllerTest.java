package com.sheffieldcorwin.analyticstracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sheffieldcorwin.analyticstracker.service.CPIService;

@WebMvcTest(CPIController.class)
@ActiveProfiles("test")
public class CPIControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CPIService cpiService;

	@Test
	public void testAvailableSeriesByName_Success() throws Exception {
		when(cpiService.fetchAvailableSeriesByName()).thenReturn(List.of("Series1", "Series2"));
		mockMvc.perform(get("/manualRequest/availableSeriesByName")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value("Series1")).andExpect(jsonPath("$[1]").value("Series2"));
	}
	
	@Test
	public void testAvailableSeriesByName_Failure() throws Exception {
		when(cpiService.fetchAvailableSeriesByName()).thenReturn(List.of());
		mockMvc.perform(get("/manualRequest/availableSeriesByName")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").doesNotExist());
	}

}
