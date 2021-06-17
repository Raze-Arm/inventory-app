package raze.spring.inventory.chat;


import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class OnlineUsers {
    private static final Set<StompUser> users = new HashSet<>();


    public static void addUser(StompUser user) {
        users.add(user);
    }

    public static void removeUserByUsername(String username) {
        users.removeIf(stompUser -> stompUser.getUsername().equals(username));
    }

    public static Set<StompUser> getUsers() {
        return users;
    }

}
