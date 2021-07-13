package raze.spring.inventory.registration.validator;

import raze.spring.inventory.registration.ResetPasswordRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsEqualConstraintValidator implements
        ConstraintValidator<PasswordsEqualConstraint, Object> {
    @Override
    public void initialize(PasswordsEqualConstraint arg0) {
    }

    @Override
    public boolean isValid(Object candidate, ConstraintValidatorContext arg1) {
        ResetPasswordRequest request = (ResetPasswordRequest) candidate;
        return request.getPassword().equals(request.getPasswordRepeat());
    }
}
