package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomDto implements DtoEntity {
    private String id;
    private String name;
    private Double length;
    private Double width;
    private Double x;
    private Double y;

    private RoomTypeDto type;
    private RoomStatusDto status;
    private DepartmentDto department;
    private List<WorkSpaceDto> workspaces;
    private FloorDto floor;

    public RoomDto(final String id) {
        this.id = id;
    }
}
