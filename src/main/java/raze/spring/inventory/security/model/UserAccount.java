package raze.spring.inventory.security.model;

import lombok.*;
import raze.spring.inventory.security.role.UserRole;


import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode()
@Entity
public class UserAccount  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRoles  ;
    @ElementCollection
    private Set<String> userPermissions = new HashSet<>();
    private  Boolean isAccountNonExpired ;
    private  Boolean isAccountNonLocked ;
    private  Boolean isCredentialsNonExpired ;
    private  Boolean isEnabled ;
    private Date creationDate;
    private Date modifiedDate;


    @PreUpdate
    void preUpdate() {
        if(this.modifiedDate == null) {
            this.modifiedDate = new Date();
        }
    }


    @PrePersist
    void preInsert() {
        if(userRoles == null) this.userRoles =  UserRole.BASIC;
        if(isAccountNonExpired == null) this.isAccountNonExpired = true;
        if(isAccountNonLocked == null) this.isAccountNonLocked = true;
        if(isCredentialsNonExpired == null) this.isCredentialsNonExpired = true;
        if(this.isEnabled == null) this.isEnabled = true;
        if(this.creationDate == null) this.creationDate = new Date();
    }

    public  UserAccount copyWith(String username, String password , UserRole userRole , Boolean isAccountNonExpired
            , Boolean isAccountNonLocked, Boolean isCredentialsNonExpired, Boolean isEnabled) {
        if(username != null) this.username = username;
        if(password != null) this.password = password;
        if(userRole != null) {
            this.userPermissions = userRole.getPermissions().stream()
                    .map(userPermission -> userPermission.name()).collect(Collectors.toSet());
            this.userRoles = userRole;
        }
        if(isAccountNonExpired != null) this.isAccountNonExpired = isAccountNonExpired;
        if(isAccountNonLocked != null) this.isAccountNonLocked = isAccountNonLocked;
        if(isCredentialsNonExpired != null) this.isCredentialsNonExpired = isCredentialsNonExpired;
        if(isEnabled != null) this.isEnabled = isEnabled;
        return this;
    }

    public void setUsername(String username) {
        if(username != null) this.username = username;
    }

    public void setPassword(String password) {
        if(password != null)
            this.password = password;
    }

    public void setUserPermissions(Set<String> userPermissions) {
        if(userPermissions!=null &&  userPermissions.size() > 0) {
            this.userPermissions = userPermissions;
        }
    }

    public void setUserRoles(UserRole userRoles) {
        if(userRoles != null ) {
            if( this.userPermissions == null) this.userPermissions = userRoles.getPermissions().stream()
                    .map(userPermission -> userPermission.name()).collect(Collectors.toSet());
            this.userRoles = userRoles;
        }
    }

    public void setCreationDate(Date creationDate) {
        if(creationDate != null)this.creationDate = creationDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        if(modifiedDate != null)this.modifiedDate = modifiedDate;
    }


    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", isEnabled=" + isEnabled +
                '}';
    }
}
