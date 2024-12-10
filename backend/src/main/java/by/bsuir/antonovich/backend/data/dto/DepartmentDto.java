package by.bsuir.antonovich.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto implements DtoEntity {

    public DepartmentDto(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String name;
    private UserDto boss;
}
