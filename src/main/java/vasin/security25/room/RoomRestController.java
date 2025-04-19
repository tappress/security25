package vasin.security25.room;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@AllArgsConstructor
public class RoomRestController {

    private final RoomService service;

    @GetMapping
    public List<Room> getRooms() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Room getOneRoom(@PathVariable String id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room create(@RequestBody Room room) {
        return service.create(room);
    }

    @PutMapping
    public Room update(@RequestBody Room room) {
        return service.update(room);
    }

    @GetMapping("/hello/user")
    @PreAuthorize("hasRole('USER')")
    public String helloUser() {
        return "Hello User!";
    }

    @GetMapping("hello/admin")
    public String helloAdmin() {
        return "Hello Admin!";
    }

    @GetMapping("hello/superadmin")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String helloSuperAdmin() {
        return "Hello SuperAdmin";
    }

    @GetMapping("hello/unknown")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'SUPERADMIN')")
    public String helloUnknown() {
        return "Hello Unknown!";
    }

    @GetMapping("hello/stranger")
    public String helloStranger() {
        return "Hello Stranger";
    }
}