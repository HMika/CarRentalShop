package com.rental.carrentalshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.carrentalshop.dto.CarDTO;
import com.rental.carrentalshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllCars() throws Exception {
        List<CarDTO> cars = List.of(CarDTO.builder()
                .id(1L)
                .make("Toyota")
                .model("Camry")
                .year(2022)
                .rentalPrice(new BigDecimal("50.0"))
                .build());
        when(carService.getAllCars()).thenReturn(cars);

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(cars.size()));
    }

    @Test
    void testGetCarById() throws Exception {
        CarDTO carDTO = CarDTO.builder()
                .id(1L)
                .make("Honda")
                .model("Civic")
                .year(2021)
                .rentalPrice(new BigDecimal("45.0"))
                .build();
        when(carService.getCarById(1L)).thenReturn(carDTO);

        mockMvc.perform(get("/api/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.make").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"));
    }

    @Test
    void testCreateCar() throws Exception {
        CarDTO carDTO = CarDTO.builder()
                .make("Ford")
                .model("Focus")
                .year(2023)
                .rentalPrice(new BigDecimal("55.0"))
                .build();
        CarDTO createdCar = CarDTO.builder()
                .id(1L)
                .make("Ford")
                .model("Focus")
                .year(2023)
                .rentalPrice(new BigDecimal("55.0"))
                .build();
        when(carService.createCar(any(CarDTO.class))).thenReturn(createdCar);

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.make").value("Ford"))
                .andExpect(jsonPath("$.model").value("Focus"));
    }

    @Test
    void testUpdateCar() throws Exception {
        CarDTO updatedCar = CarDTO.builder()
                .id(1L)
                .make("BMW")
                .model("X5")
                .year(2022)
                .rentalPrice(new BigDecimal("100.0"))
                .build();
        when(carService.updateCar(eq(1L), any(CarDTO.class))).thenReturn(updatedCar);

        mockMvc.perform(put("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.make").value("BMW"))
                .andExpect(jsonPath("$.model").value("X5"));
    }

    @Test
    void testDeleteCar() throws Exception {
        doNothing().when(carService).deleteCar(1L);

        mockMvc.perform(delete("/api/cars/1"))
                .andExpect(status().isNoContent());
    }
}
