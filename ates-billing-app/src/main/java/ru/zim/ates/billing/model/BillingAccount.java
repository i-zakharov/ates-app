package ru.zim.ates.billing.model;

import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "billing_account")
public class BillingAccount {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "public_id")
    private UUID publicId;
    @ManyToOne
    @JoinColumn(name="employee_id", nullable=false)
    private AppUser employee;
    @Column(name = "balance")
    @Digits(integer = 20, fraction = 2, message = "{javax.validation.constraints.Digits.message}")
    BigDecimal balance;

    @Column(name = "last_tran_id")
    private Long lastTranId;

    @Version()
    @Column(name = "VERSION")
    private Integer version;

}
