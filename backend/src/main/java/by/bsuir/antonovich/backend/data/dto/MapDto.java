package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class MapDto {

    private Integer officeId;
    private Integer floorId;
    private List<RoomDto> rooms;
}
