package spring.mine.us.mn.state.health.lims.sample.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.SampleEntryByProjectForm;

@Controller
public class SampleEntryVLSaveController extends BaseController {
  @RequestMapping(
      value = "/SampleEntryVLSave",
      method = RequestMethod.GET
  )
  public ModelAndView showSampleEntryVLSave(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    SampleEntryByProjectForm form = new SampleEntryByProjectForm();
    form.setFormName("sampleEntryByProjectForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("/SampleEntryVL.do?forward=success", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("sampleEntryVLDefinition", "form", form);
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
