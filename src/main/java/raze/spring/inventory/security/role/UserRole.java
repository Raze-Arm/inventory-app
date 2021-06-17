package raze.spring.inventory.security.role;


import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;



public enum UserRole {
    BASIC(Sets.newHashSet(UserPermission.BASIC)),
    USER(Sets.newHashSet(UserPermission.BASIC)),
    ADMIN(Sets.newHashSet(UserPermission.values())),

    ;

    private  Set<UserPermission> permissions;

       UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }


    public void addPermission(UserPermission userPermission) {
         if(this.permissions == null) this.permissions = new HashSet<>();
         this.permissions.add(userPermission);
    }

    public void setPermissions(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;

    }

    public List<String> getRolePermissions() {
    return getPermissions().stream()
        .map(UserPermission::getPermission)
        .collect(Collectors.toList());
    }

    public static UserRole getEnumByName(String role) {
           for(UserRole e: UserRole.values()){
               if(e.name().equals(role)) return e;
           }
           return  null;
    }


    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map((permission) -> new SimpleGrantedAuthority(permission.name())).collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
     }
}
