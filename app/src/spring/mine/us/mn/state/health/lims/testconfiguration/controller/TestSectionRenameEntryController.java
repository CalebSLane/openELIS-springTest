package spring.mine.us.mn.state.health.lims.testconfiguration.controller;

import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.TestSectionRenameEntryForm;

@Controller
public class TestSectionRenameEntryController extends BaseController {
  @RequestMapping(
      value = "/TestSectionRenameEntry",
      method = RequestMethod.GET
  )
  public ModelAndView showTestSectionRenameEntry(HttpServletRequest request) {
    String forward = FWD_SUCCESS;
    TestSectionRenameEntryForm form = new TestSectionRenameEntryForm();
    form.setFormName("testSectionRenameEntryForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("testSectionRenameDefinition", "form", form);
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
