package raze.spring.inventory.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" , strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36 , columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    @PrePersist
    public void beforeSave() {
        if(createdDate == null) {
            final LocalDateTime localDateTime = Instant.now().atZone(ZoneId.of("Asia/Tehran")).toLocalDateTime();
            createdDate =  Timestamp.valueOf(localDateTime);
        }
    }

    @PreUpdate
    public void beforeUpdate() {
        final LocalDateTime localDateTime = Instant.now().atZone(ZoneId.of("Asia/Tehran")).toLocalDateTime();
        modifiedDate =  Timestamp.valueOf(localDateTime);
    }
}

