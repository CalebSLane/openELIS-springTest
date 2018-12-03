package spring.mine.us.mn.state.health.lims.role.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.RoleForm;

@Controller
public class NextPreviousRoleController extends BaseController {
  @RequestMapping(
      value = "/NextPreviousRole",
      method = RequestMethod.GET
  )
  public ModelAndView showNextPreviousRole(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    RoleForm form = new RoleForm();
    form.setFormName("roleForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("/Role.do", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("roleDefinition", "form", form);
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
