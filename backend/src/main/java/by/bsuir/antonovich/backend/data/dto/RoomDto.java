package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomDto implements DtoEntity {
    private Integer id;
    private String name;
    private Double length;
    private Double width;

    private RoomTypeDto roomType;
    private DepartmentDto department;
    private List<WorkSpaceDto> workSpaces;
    private FloorDto floor;

    public RoomDto(final Integer id) {
        this.id = id;
    }
}
