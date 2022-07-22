package uz.pdp.hrmanagementapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TourniquetHistory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private TourniquetCard tourniquetCard;

    @Column(nullable = false)
    private Date enteredAt;

    @Column(nullable = false)
    private Date exitedAt;

}
