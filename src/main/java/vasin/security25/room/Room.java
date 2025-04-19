package vasin.security25.room;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Room extends AuditMetaData {

    @Id
    private String id;
    private String roomNumber;
    private String roomType;
    private double pricePerNight;
    private int capacity;
    private boolean available;
    private String amenities;
    private String floorLevel;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Room room)) return false;

        return getId().equals(room.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}