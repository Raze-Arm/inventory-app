package raze.spring.inventory.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Timestamp created;
    private Timestamp lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreated() {
        return created;
    }

    @PrePersist
    public void setCreated() {
        this.created = Timestamp.from(Instant.now(Clock.systemUTC()));
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getLastUpdated() {
        if (lastUpdated == null)
            return created;
        return lastUpdated;
    }

    @PreUpdate
    public void setLastUpdated() {
        this.lastUpdated = Timestamp.from(Instant.now(Clock.systemUTC()));
    }

    public Timestamp getTime() {
        if (this.lastUpdated != null)
            return this.lastUpdated;
        return this.created;
    }

    public String getReadableDateTime(Timestamp date) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm a");
        final LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.of("Asia/Tehran")).toLocalDateTime();
//        DateFormat dateFormat = DateUtils.getReadableDateTimeFormat();
//        return dateFormat.format(date);
        return formatter.format(localDateTime);
    }

    public String getReadableDayMonth(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("dd MMMM").format(date);
    }

    public String getReadableDateWithoutTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd yyyy");
        return sdf.format(date);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", created=" + created +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}