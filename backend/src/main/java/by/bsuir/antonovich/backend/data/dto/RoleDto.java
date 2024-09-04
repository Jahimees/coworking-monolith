package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDto implements DtoEntity {

    private Integer id;
    private String name;

}
