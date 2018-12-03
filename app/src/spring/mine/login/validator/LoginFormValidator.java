package spring.mine.login.validator;

import org.springframework.validation.Errors;

import spring.mine.common.validator.BaseFormValidator;
import spring.mine.us.mn.state.health.lims.common.form.LoginForm;
import us.mn.state.health.lims.common.util.StringUtil;

public class LoginFormValidator extends BaseFormValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LoginForm form = (LoginForm) target;
		
		validateBaseForm(form, errors);
		if (StringUtil.isNullorNill(form.getLoginName()) ) {
		    errors.rejectValue("login.loginName", "bad", "no username entered");
		}
		if (StringUtil.isNullorNill(form.getPassword()) ) {
		    errors.rejectValue("login.loginName", "bad", "no password entered");
		}
		
	}

}
