package spring.mine.us.mn.state.health.lims.patienttype.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.PatientTypeForm;

@Controller
public class PatientTypeController extends BaseController {
  @RequestMapping(
      value = "/PatientType",
      method = RequestMethod.GET
  )
  public ModelAndView showPatientType(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    PatientTypeForm form = new PatientTypeForm();
    form.setFormName("patientTypeForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("patientTypeDefinition", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("haitiMasterListsPageDefinition", "form", form);
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
