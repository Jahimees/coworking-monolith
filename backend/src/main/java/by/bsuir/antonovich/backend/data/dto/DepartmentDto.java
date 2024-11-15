package by.bsuir.antonovich.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto implements DtoEntity {

    private Integer id;
    private String name;
}
