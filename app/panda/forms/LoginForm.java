package panda.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import play.data.validation.Constraints.Required;

@Getter
@Setter
@NoArgsConstructor
public  class LoginForm {
	
	@Required
	private String username;
	
	@Required
	private String password;
		
}
