package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FloorDto implements DtoEntity {

    private Integer id;
    private String name;
    private String description;
    private OfficeDto office;
    private List<RoomDto> rooms;

    public FloorDto(final Integer id) {
        this.id = id;
    }
}
