package raze.spring.inventory.security.service.impl;

import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raze.spring.inventory.security.AppUserDetail;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.security.service.UserAccountService;


@Service("appUserDetailService")
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserAccountService userAccountService;

    public UserDetailServiceImpl(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final UserAccount userAccount = this.userAccountService.getUserByUsername(username);
    final AppUserDetail appUserDetail =
        AppUserDetail.builder()
                .id(userAccount.getId())
            .username(userAccount.getUsername())
            .password(userAccount.getPassword())
            .grantedAuthorities(userAccount.getUserRoles().getGrantedAuthorities())
            .isAccountNonExpired(userAccount.getIsAccountNonExpired())
            .isAccountNonLocked(userAccount.getIsAccountNonLocked())
            .isCredentialsNonExpired(userAccount.getIsCredentialsNonExpired())
            .isEnabled(userAccount.getIsEnabled())
            .build();
        return appUserDetail;
    }
}
