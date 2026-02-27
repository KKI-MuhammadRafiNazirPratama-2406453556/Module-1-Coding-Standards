package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class CarRepository {
    private List<Car> carData = new ArrayList<>();

    public Car create(Car car) {
        if (car.getCarId() == null) {
            car.setCarId(UUID.randomUUID().toString());
        }
        carData.add(car);
        return car;
    }

    public Iterator<Car> findAll() {
        return carData.iterator();
    }

    public Car findById(String id) {
        for (Car car : carData) {
            if (car.getCarId().equals(id)) {
                return car;
            }
        }
        return null;
    }

    public Car update(String id, Car updatedCar) {
        for (int i = 0; i < carData.size(); i++) {
            if (carData.get(i).getCarId().equals(id)) {
                updatedCar.setCarId(id);
                carData.set(i, updatedCar);
                return updatedCar;
            }
        }
        return null;
    }

    public void delete(String id) {
        carData.removeIf(car -> car.getCarId().equals(id));
    }
}
