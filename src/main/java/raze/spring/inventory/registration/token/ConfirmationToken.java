package raze.spring.inventory.registration.token;


import lombok.*;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.security.model.UserAccount;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(indexes = @Index(columnList = "token"))
public class ConfirmationToken {


    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Timestamp createdDate;

    @Column(nullable = false)
    private Timestamp expiresAt;

    private Timestamp confirmAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_profile_id"
    )
    private UserProfile userProfile;



}
