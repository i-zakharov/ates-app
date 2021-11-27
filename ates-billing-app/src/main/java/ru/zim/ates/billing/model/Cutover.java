package ru.zim.ates.billing.model;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
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
@Table(name = "cutover")
public class Cutover {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "public_id")
    private UUID publicId;
    @Column(name = "time")
    private LocalDateTime time;

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
