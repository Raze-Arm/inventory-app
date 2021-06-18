package raze.spring.inventory.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppSockMessage {
    @Length(min = 3, max = 35)
    private String from;
    @Length(min = 3, max = 35)
    private String to;
    @Length(min = 1, max = 80)
    private String text;
    private boolean typing;
    @Length(min = 1, max = 30)
    private String date;
    private boolean error ;
}
