package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto implements DtoEntity {

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
    private WorkSpaceDto workSpace;
    private List<OfficeDto> offices;
    private List<RoleDto> roles;

}
