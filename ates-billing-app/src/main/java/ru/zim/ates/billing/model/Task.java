package ru.zim.ates.billing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;

@Data
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "public_id")
    private UUID publicId;
    @Column(name = "title")
    private String title;
    @Column(name = "is_closed")
    private Boolean isClosed;
    @ManyToOne
    @JoinColumn(name="assignee_id", nullable=false)
    private AppUser assignee;
    @Column(name = "assigne_price")
    @Digits(integer = 20, fraction = 2, message = "{javax.validation.constraints.Digits.message}")
    private BigDecimal assignePrice;
    @Column(name = "close_price")
    @Digits(integer = 20, fraction = 2, message = "{javax.validation.constraints.Digits.message}")
    private BigDecimal closePrice;
    @Column(name = "VERSION")
    private Integer version;

}
