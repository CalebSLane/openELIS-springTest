package spring.mine.us.mn.state.health.lims.organization.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.OrganizationMenuForm;

@Controller
public class CancelOrganizationController extends BaseController {
  @RequestMapping(
      value = "/CancelOrganization",
      method = RequestMethod.GET
  )
  public ModelAndView showCancelOrganization(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    OrganizationMenuForm form = new OrganizationMenuForm();
    form.setFormName("organizationMenuForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("/OrganizationMenu.do", "form", form);
    } else if ("close".equals(forward)) {
      return new ModelAndView("/OrganizationMenu.do", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("/Organization.do", "form", form);
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
