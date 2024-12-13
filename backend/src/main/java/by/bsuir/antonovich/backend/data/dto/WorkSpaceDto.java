package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkSpaceDto implements DtoEntity {

    private Integer id;
    private String name;
    private RoomDto room;
    private Double x;
    private Double y;

    public WorkSpaceDto(Integer id) {
        this.id = id;
    }
}
