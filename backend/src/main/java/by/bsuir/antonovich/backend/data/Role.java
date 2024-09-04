package by.bsuir.antonovich.backend.data;

import by.bsuir.antonovich.backend.data.dto.RoleDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role implements GrantedAuthority, SimpleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public Role(RoleDto roleDto) {
        this.id = roleDto.getId();
        this.name = roleDto.getName();
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
