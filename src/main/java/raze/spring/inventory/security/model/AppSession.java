package raze.spring.inventory.security.model;


public  class AppSession {
    private String username;
    private String token;


    public AppSession(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public AppSession() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
