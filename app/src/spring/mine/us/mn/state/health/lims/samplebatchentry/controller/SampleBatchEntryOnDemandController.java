package spring.mine.us.mn.state.health.lims.samplebatchentry.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.SampleBatchEntryForm;

@Controller
public class SampleBatchEntryOnDemandController extends BaseController {
  @RequestMapping(
      value = "/SampleBatchEntryOnDemand",
      method = RequestMethod.GET
  )
  public ModelAndView showSampleBatchEntryOnDemand(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    SampleBatchEntryForm form = new SampleBatchEntryForm();
    form.setFormName("sampleBatchEntryForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("sampleBatchEntryOnDemandDefinition", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("/SampleBatchEntrySetup.do", "form", form);
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
