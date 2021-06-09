package raze.spring.inventory.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import raze.spring.inventory.security.model.UserAccount;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userAgent;
    private String ip;
    private String expires;
    @OneToOne
    @JsonBackReference
    private UserAccount user;
    private String requestMethod;
    private Integer responseStatus;
    private String url;
    private String entity;
    private String parameter;
    private Timestamp created;
    private Timestamp lastUpdated;



    @PrePersist
    public void beforeSave() {
        if(created == null) {
            final LocalDateTime localDateTime = Instant.now().atZone(ZoneId.of("Asia/Tehran")).toLocalDateTime();
            created =  Timestamp.valueOf(localDateTime);
        }
    }

    @PreUpdate
    public void beforeUpdate() {
        final LocalDateTime localDateTime = Instant.now().atZone(ZoneId.of("Asia/Tehran")).toLocalDateTime();
        lastUpdated =  Timestamp.valueOf(localDateTime);
    }
}

