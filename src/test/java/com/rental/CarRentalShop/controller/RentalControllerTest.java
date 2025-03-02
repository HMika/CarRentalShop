package com.rental.CarRentalShop.controller;

import com.rental.CarRentalShop.dto.CarDTO;
import com.rental.CarRentalShop.dto.RentalDTO;
import com.rental.CarRentalShop.dto.UserDTO;
import com.rental.CarRentalShop.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentalController.class)
@AutoConfigureMockMvc(addFilters = false)
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RentalService rentalService;

    private RentalDTO sampleRentalDTO;

    @BeforeEach
    void setUp() {
        CarDTO carDTO = CarDTO.builder()
                .id(1L)
                .make("Toyota")
                .model("Corolla")
                .year(2020)
                .registrationNumber("ABC123")
                .rentalPrice(BigDecimal.valueOf(50.00))
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .username("johndoe")
                .name("John")
                .surname("Doe")
                .contactInfo("johndoe@example.com")
                .password("pwd")
                .build();

        sampleRentalDTO = RentalDTO.builder()
                .id(1L)
                .car(carDTO)
                .user(userDTO)
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 5))
                .isPaid(false)
                .totalPrice(BigDecimal.valueOf(200.00))
                .build();
    }

    @Test
    void testGetAllRentals_ReturnsOkAndRentalList() throws Exception {
        List<RentalDTO> rentals = Collections.singletonList(sampleRentalDTO);
        BDDMockito.given(rentalService.getAllRentals()).willReturn(rentals);

        mockMvc.perform(get("/api/rentals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(sampleRentalDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].car.make", is("Toyota")))
                .andExpect(jsonPath("$[0].car.model", is("Corolla")))
                .andExpect(jsonPath("$[0].user.username", is("johndoe")));

        Mockito.verify(rentalService, Mockito.times(1)).getAllRentals();
    }

    @Test
    void testGetRentalById_ReturnsOkAndRental() throws Exception {
        BDDMockito.given(rentalService.getRentalById(1L))
                .willReturn(sampleRentalDTO);

        mockMvc.perform(get("/api/rentals/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleRentalDTO.getId().intValue())))
                .andExpect(jsonPath("$.car.make", is("Toyota")))
                .andExpect(jsonPath("$.user.username", is("johndoe")));

        Mockito.verify(rentalService, Mockito.times(1)).getRentalById(1L);
    }

    @Test
    void testCreateRental_ReturnsOkAndCreatedRental() throws Exception {
        BDDMockito.given(rentalService.createRental(org.mockito.ArgumentMatchers.any(RentalDTO.class)))
                .willReturn(sampleRentalDTO);

        String requestBody = """
                {
                  "car": { "id": 1 },
                  "user": { "id": 1 },
                  "startDate": "2023-01-01",
                  "endDate": "2023-01-05"
                }""";

        mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleRentalDTO.getId().intValue())))
                .andExpect(jsonPath("$.car.make", is("Toyota")))
                .andExpect(jsonPath("$.user.username", is("johndoe")));

        Mockito.verify(rentalService, Mockito.times(1))
                .createRental(ArgumentMatchers.any(RentalDTO.class));
    }

    @Test
    void testUpdateRental_ReturnsOkAndUpdatedRental() throws Exception {
        RentalDTO updatedRental = RentalDTO.builder()
                .id(1L)
                .car(sampleRentalDTO.getCar())
                .user(sampleRentalDTO.getUser())
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 5))
                .isPaid(true)
                .totalPrice(BigDecimal.valueOf(200.00))
                .build();

        BDDMockito.given(rentalService.updateRental(eq(1L), ArgumentMatchers.any(RentalDTO.class)))
                .willReturn(updatedRental);

        String requestBody = """
                {
                  "isPaid": true,
                  "startDate": "2023-01-01",
                  "endDate": "2023-01-05"
                }""";

        mockMvc.perform(put("/api/rentals/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedRental.getId().intValue()))
                .andExpect(jsonPath("$.isPaid").value(true));

        Mockito.verify(rentalService, Mockito.times(1)).updateRental(eq(1L), ArgumentMatchers.any(RentalDTO.class));
    }

    @Test
    void testDeleteRental_ReturnsNoContent() throws Exception {
        BDDMockito.doNothing().when(rentalService).deleteRental(1L);

        mockMvc.perform(delete("/api/rentals/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(rentalService, Mockito.times(1)).deleteRental(1L);
    }

    @Test
    void testGetRentalsByUser_ReturnsOkAndRentalList() throws Exception {
        BDDMockito.given(rentalService.getRentalsByUser(1L))
                .willReturn(Collections.singletonList(sampleRentalDTO));

        mockMvc.perform(get("/api/rentals/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.id", is(1)));

        Mockito.verify(rentalService, Mockito.times(1)).getRentalsByUser(1L);
    }

    @Test
    void testGetRentalsByCar_ReturnsOkAndRentalList() throws Exception {
        BDDMockito.given(rentalService.getRentalsByCar(1L))
                .willReturn(Collections.singletonList(sampleRentalDTO));

        mockMvc.perform(get("/api/rentals/car/{carId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].car.id", is(1)));

        Mockito.verify(rentalService, Mockito.times(1)).getRentalsByCar(1L);
    }
}
