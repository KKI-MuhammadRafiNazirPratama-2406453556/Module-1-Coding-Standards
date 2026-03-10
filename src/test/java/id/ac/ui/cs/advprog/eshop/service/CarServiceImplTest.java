package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    CarServiceImpl carService;

    @Mock
    CarRepository carRepository;

    Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("test-id");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);
    }

    @Test
    void testCreate() {
        when(carRepository.create(car)).thenReturn(car);
        Car result = carService.create(car);
        assertNotNull(result);
        assertEquals("test-id", result.getCarId());
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testFindAll() {
        List<Car> carList = Arrays.asList(car);
        Iterator<Car> iterator = carList.iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test-id", result.get(0).getCarId());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(carRepository.findById("test-id")).thenReturn(car);
        Car result = carService.findById("test-id");
        assertNotNull(result);
        assertEquals("test-id", result.getCarId());
        verify(carRepository, times(1)).findById("test-id");
    }

    @Test
    void testUpdate() {
        carService.update("test-id", car);
        verify(carRepository, times(1)).update("test-id", car);
    }

    @Test
    void testDeleteCarById() {
        carService.deleteCarById("test-id");
        verify(carRepository, times(1)).delete("test-id");
    }
}
