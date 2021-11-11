package ru.zim.ates.auth.model;

import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.zim.ates.auth.mapper.CommaSeparatedStringsToRolesConverter;
import ru.zim.ates.common.model.AppRole;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "APP_USER")
public class AppUser {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private UUID id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @Column(name = "ROLES")
    @Convert(converter = CommaSeparatedStringsToRolesConverter.class)
    private List<AppRole> roles;
}
