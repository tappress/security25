package vasin.security25.room;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository repository;

    private List<Room> rooms;

    @PostConstruct
    void init() {
        rooms.add(new Room("1", "101", "Standard", 99.99, 2, true, "WiFi, TV, AC", "1"));
        rooms.add(new Room("2", "201", "Deluxe", 149.99, 3, true, "WiFi, TV, AC, Mini Bar", "2"));
        rooms.add(new Room("3", "301", "Suite", 249.99, 4, true, "WiFi, TV, AC, Mini Bar, Jacuzzi", "3"));
        repository.saveAll(rooms);
    }

    public List<Room> getAll() {
        return repository.findAll();
    }

    public Room getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Room create(Room room) {
        return repository.save(room);
    }

    public Room update(Room room) {
        return repository.save(room);
    }
}