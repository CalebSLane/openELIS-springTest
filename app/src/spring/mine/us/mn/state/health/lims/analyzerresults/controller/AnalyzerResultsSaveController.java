package spring.mine.us.mn.state.health.lims.analyzerresults.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.AnalyzerResultsForm;

@Controller
public class AnalyzerResultsSaveController extends BaseController {
  @RequestMapping(
      value = "/AnalyzerResultsSave",
      method = RequestMethod.GET
  )
  public ModelAndView showAnalyzerResultsSave(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    AnalyzerResultsForm form = new AnalyzerResultsForm();
    form.setFormName("AnalyzerResultsForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("/AnalyzerResults.do", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("homePageDefinition", "form", form);
    } else if ("error".equals(forward)) {
      return new ModelAndView("analyzerResultsDefinition", "form", form);
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
