package spring.mine.us.mn.state.health.lims.resultvalidation.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.ResultValidationForm;

@Controller
public class ResultValidationSaveController extends BaseController {
  @RequestMapping(
      value = "/ResultValidationSave",
      method = RequestMethod.GET
  )
  public ModelAndView showResultValidationSave(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    ResultValidationForm form = new ResultValidationForm();
    form.setFormName("ResultValidationForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("/ResultValidation.do", "form", form);
    } else if ("successRetroC".equals(forward)) {
      return new ModelAndView("/ResultValidationRetroC.do", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("homePageDefinition", "form", form);
    } else if ("error".equals(forward)) {
      return new ModelAndView("resultValidationDefinition", "form", form);
    } else {
      return new ModelAndView("PageNotFound");
    }
  }

  protected String getPageTitleKey() {
    return null;
  }

  protected String getPageSubtitleKey() {
    return null;
  }
}
