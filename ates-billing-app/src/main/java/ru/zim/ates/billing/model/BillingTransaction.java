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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "billing_transaction")
public class BillingTransaction {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "public_id")
    private UUID publicId;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BillingTransactionType type;
    @Column(name = "time")
    private LocalDateTime time;
    @Column(name = "amount")
    @Digits(integer = 20, fraction = 2, message = "{javax.validation.constraints.Digits.message}")
    private BigDecimal amount;
    @Column(name = "balanceBefore")
    @Digits(integer = 20, fraction = 2, message = "{javax.validation.constraints.Digits.message}")
    private BigDecimal balanceBefore;
    @Column(name = "purpose")
    private String purpose;

    @PrePersist
    public void prePersist() {
        if (this.id == null && this.publicId == null) {
            this.publicId = java.util.UUID.randomUUID();
        }
        if (this.time == null) {
            this.time = LocalDateTime.now();
        }
    }
}
