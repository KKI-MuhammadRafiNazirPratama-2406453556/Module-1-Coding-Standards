package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {

    @InjectMocks
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
    void testCreate_withId() {
        Car result = carRepository.create(car);
        assertNotNull(result);
        assertEquals("test-id", result.getCarId());
    }

    @Test
    void testCreate_withoutId() {
        Car noIdCar = new Car();
        noIdCar.setCarName("Honda");
        noIdCar.setCarColor("Blue");
        noIdCar.setCarQuantity(3);

        Car result = carRepository.create(noIdCar);
        assertNotNull(result);
        assertNotNull(result.getCarId());
    }

    @Test
    void testFindAll_empty() {
        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAll_withCars() {
        carRepository.create(car);
        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        assertEquals("test-id", iterator.next().getCarId());
    }

    @Test
    void testFindById_found() {
        carRepository.create(car);
        Car found = carRepository.findById("test-id");
        assertNotNull(found);
        assertEquals("test-id", found.getCarId());
    }

    @Test
    void testFindById_notFound() {
        Car found = carRepository.findById("non-existent");
        assertNull(found);
    }

    @Test
    void testUpdate_found() {
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Toyota Updated");
        updatedCar.setCarColor("Blue");
        updatedCar.setCarQuantity(10);

        Car result = carRepository.update("test-id", updatedCar);
        assertNotNull(result);
        assertEquals("test-id", result.getCarId());
        assertEquals("Toyota Updated", result.getCarName());
    }

    @Test
    void testUpdate_notFound() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Ghost Car");
        updatedCar.setCarColor("White");
        updatedCar.setCarQuantity(1);

        Car result = carRepository.update("non-existent", updatedCar);
        assertNull(result);
    }

    @Test
    void testDelete() {
        carRepository.create(car);
        carRepository.delete("test-id");
        assertNull(carRepository.findById("test-id"));
    }

    @Test
    void testDelete_nonExistent() {
        carRepository.delete("non-existent");
        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindById_existsButDifferentId() {
        carRepository.create(car);
        Car found = carRepository.findById("different-id");
        assertNull(found);
    }

    @Test
    void testUpdate_existsButDifferentId() {
        carRepository.create(car);
        Car updatedCar = new Car();
        updatedCar.setCarName("Ghost Car");
        updatedCar.setCarColor("White");
        updatedCar.setCarQuantity(0);
        Car result = carRepository.update("different-id", updatedCar);
        assertNull(result);
    }

    @Test
    void testDelete_existsButDifferentId() {
        carRepository.create(car);
        carRepository.delete("different-id");
        assertNotNull(carRepository.findById("test-id"));
    }
}
