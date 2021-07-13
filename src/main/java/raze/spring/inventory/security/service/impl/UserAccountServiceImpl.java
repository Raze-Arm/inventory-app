package raze.spring.inventory.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raze.spring.inventory.registration.token.ConfirmationToken;
import raze.spring.inventory.registration.token.ConfirmationTokenService;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.security.repository.UserAccountRepository;
import raze.spring.inventory.security.service.UserAccountService;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccount getUserByUsername(String username) throws UsernameNotFoundException {
        final UserAccount userAccount = this.userAccountRepository.findByUsername(username).orElse(null);
        if(userAccount == null) throw new UsernameNotFoundException("Username " + username + " not found");
        return userAccount;
    }

    @Override
    public UserAccount addUserAccount(UserAccount userAccount)  {
        final UserAccount user =
                userAccount.withPassword(this.passwordEncoder.encode(userAccount.getPassword()));
        final UserAccount savedUser = this.userAccountRepository.save(user);
        return savedUser;
    }

    @Override
    public void updateUserAccount(UserAccount userAccount) throws NoSuchElementException {
        if(userAccount.getId() == null) throw new NoSuchElementException();
    final UserAccount user =
        userAccount.withPassword(this.passwordEncoder.encode(userAccount.getPassword()));
        this.userAccountRepository.save(user);
    }

    @Override
    public void removeUserAccount(Long id)  {
        this.userAccountRepository.deleteById(id);
    }


    @Transactional
    @Override
    public void changePassword(String username, String password) {
        final UserAccount userAccount
                = this.userAccountRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("username not found"));

        userAccount.setPassword(this.passwordEncoder.encode(password));

        this.userAccountRepository.save(userAccount);
    }

    @Override
    public void enableUser(String username) {
        this.userAccountRepository.enableUser(username);
    }

    @Override
    public void disableUser(String username) {
        this.userAccountRepository.disableUser(username);
    }
}
