package ru.zim.ates.common.standartimpl.consumer.user.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.zim.ates.common.model.AppRole;

@Data
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "APP_USER")
public class AppUser {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PUBLIC_ID")
    private UUID publicId;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private AppRole role;
    /**
     * Версия сущности в последнем примененном CUD событии
     */
    @Column(name = "VERSION")
    private Integer version;
    /**
     * Версия сущности в послднем примененном событии по смене роли
     */
    @Column(name = "ROLE_VERSION")
    private Integer roleChangedVersion;


}
