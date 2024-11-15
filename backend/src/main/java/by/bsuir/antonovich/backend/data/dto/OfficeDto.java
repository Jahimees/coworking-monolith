package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OfficeDto implements DtoEntity {

    private Integer id;
    private String name;
    private String address;
    private List<UserDto> userOwnerList;
    private List<FloorDto> floors;

    public OfficeDto(Integer id) {
        this.id = id;
    }
}
