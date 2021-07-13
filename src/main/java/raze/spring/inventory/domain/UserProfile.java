package raze.spring.inventory.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.validator.constraints.Length;
import raze.spring.inventory.security.model.UserAccount;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(indexes = @Index(columnList = "firstName, lastName"))
public class UserProfile extends BaseEntity {
    @NotBlank(message = "first name is mandatory")
    @Length(min = 3, max = 30, message = "size must be between 3 and 30")
    private String firstName;
    @NotBlank(message = "last name is mandatory")
    @Length(min = 3, max = 30, message = "size must be between 3 and 30")
    private String lastName;

  @NotBlank(message = "email is mandatory")
  @Email(
      message = "not a valid email",
      regexp =
              "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?!-)(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
   private String email;


    private Boolean imageAvailable = false;

    @OneToOne(optional = false, cascade = {CascadeType.ALL})
    private UserAccount account;


}
