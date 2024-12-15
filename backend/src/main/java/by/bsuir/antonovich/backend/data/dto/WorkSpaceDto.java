package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkSpaceDto implements DtoEntity {

    private String id;
    private String name;
    private RoomDto room;
    private Double x;
    private Double y;
    private UserDto user;

    public WorkSpaceDto(String id) {
        this.id = id;
    }
}
