package raze.spring.inventory.registration;

import lombok.Data;
import raze.spring.inventory.registration.validator.PasswordsEqualConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@PasswordsEqualConstraint(message = "passwords are not equal")
public class ResetPasswordRequest {
    @NotNull
    @Size(min = 8, max = 30)
    private String password;

    @NotNull
    @Size(min = 8, max = 30)
    private String passwordRepeat;

}
