package raze.spring.inventory.security.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.security.repository.UserAccountRepository;
import raze.spring.inventory.security.service.UserAccountService;

import java.util.NoSuchElementException;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccount getUserByUsername(String username) throws NoSuchElementException {
        final UserAccount userAccount = this.userAccountRepository.findByUsername(username).orElseThrow();

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
}
