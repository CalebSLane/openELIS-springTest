package spring.mine.us.mn.state.health.lims.patient.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.SamplePatientEntryForm;

@Controller
public class PatientManagementUpdateController extends BaseController {
  @RequestMapping(
      value = "/PatientManagementUpdate",
      method = RequestMethod.GET
  )
  public ModelAndView showPatientManagementUpdate(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    SamplePatientEntryForm form = new SamplePatientEntryForm();
    form.setFormName("samplePatientEntryForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("patientManagementDefinition", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("patientManagementDefinition", "form", form);
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
