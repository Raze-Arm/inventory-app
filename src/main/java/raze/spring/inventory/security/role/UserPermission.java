package raze.spring.inventory.security.role;


public  enum UserPermission {
    FULL("FULL");


    private  final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public static UserPermission getEnumByName(String permission) {
        for(UserPermission e: UserPermission.values()) {
            if(e.permission.equals(permission)) return e;
        }
        return null;
    }


    public String getPermission() {
        return permission;
    }
}
