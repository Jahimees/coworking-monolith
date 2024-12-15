package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto implements DtoEntity {

    public UserDto(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String username;
    private String email;

    /**
     * !RAW password!
     */
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private DepartmentDto department;
    private List<OfficeDto> offices;
    private List<RoleDto> roles;

}
