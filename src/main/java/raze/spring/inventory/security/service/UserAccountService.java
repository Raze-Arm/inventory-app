package raze.spring.inventory.security.service;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import raze.spring.inventory.security.model.UserAccount;

import java.util.NoSuchElementException;

public interface UserAccountService {

    UserAccount getUserByUsername(String username) throws UsernameNotFoundException;
    UserAccount addUserAccount( UserAccount userAccount) ;
    void updateUserAccount(UserAccount userAccount) throws NoSuchElementException;
    void removeUserAccount(Long id) ;
}
