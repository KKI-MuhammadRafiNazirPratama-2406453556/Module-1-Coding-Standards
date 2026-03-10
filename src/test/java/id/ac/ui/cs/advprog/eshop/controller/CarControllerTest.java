package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @InjectMocks
    CarController carController;

    @Mock
    CarService carService;

    MockMvc mockMvc;

    Car car;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(carController)
                .setViewResolvers(resolver)
                .build();
        car = new Car();
        car.setCarId("car-test-id");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);
    }

    @Test
    void testCreateCarPage() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("createCar"));
    }

    @Test
    void testCreateCarPost() throws Exception {
        when(carService.create(any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/car/createCar")
                        .param("carName", "Toyota")
                        .param("carColor", "Red")
                        .param("carQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));
    }

    @Test
    void testCarListPage() throws Exception {
        when(carService.findAll()).thenReturn(Arrays.asList(car));

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"));
    }

    @Test
    void testCarListPage_empty() throws Exception {
        when(carService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"));
    }

    @Test
    void testEditCarPage() throws Exception {
        when(carService.findById("car-test-id")).thenReturn(car);

        mockMvc.perform(get("/car/editCar/car-test-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("editCar"));
    }

    @Test
    void testEditCarPost() throws Exception {
        mockMvc.perform(post("/car/editCar")
                        .param("carId", "car-test-id")
                        .param("carName", "Toyota Updated")
                        .param("carColor", "Blue")
                        .param("carQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));
    }

    @Test
    void testDeleteCar() throws Exception {
        mockMvc.perform(post("/car/deleteCar")
                        .param("carId", "car-test-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));
    }
}
