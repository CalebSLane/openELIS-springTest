package spring.mine.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;

@Controller
public class PermissionController extends BaseController {

    @RequestMapping(value = "/TestManagementConfigMenu", method = RequestMethod.GET)
    public ModelAndView showTestManagementConfigMenuPage(HttpServletRequest request) throws Exception {
	String forward = FWD_SUCCESS;

	BaseForm form = new BaseForm();
	form.setFormName("testManagementConfigForm");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	ModelAndView mv = checkUserAndSetup(form, errors, request);

	if (errors.hasErrors()) {
	    return mv;
	}

	if (FWD_SUCCESS.equalsIgnoreCase(forward)) {
	    return new ModelAndView("testManagementConfigDefinition", "form", form);
	} else {
	    return new ModelAndView("redirect:/" + forward + ".html");
	}
    }

    @RequestMapping(value = "/TestSectionManagement", method = RequestMethod.GET)
    public ModelAndView showTestSectionManagementPage(HttpServletRequest request) throws Exception {
	String forward = FWD_SUCCESS;

	BaseForm form = new BaseForm();
	form.setFormName("TestSectionManagementForm");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	ModelAndView mv = checkUserAndSetup(form, errors, request);

	if (errors.hasErrors()) {
	    return mv;
	}

	if (FWD_SUCCESS.equalsIgnoreCase(forward)) {
	    return new ModelAndView("testSectionManagementDefinition", "form", form);
	} else {
	    return new ModelAndView("redirect:/" + forward + ".html");
	}
    }

    @RequestMapping(value = "/SampleTypeManagement", method = RequestMethod.GET)
    public ModelAndView showSampleTypeManagementPage(HttpServletRequest request) throws Exception {
	String forward = FWD_SUCCESS;

	BaseForm form = new BaseForm();
	form.setFormName("sampleTypeManagementForm");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	ModelAndView mv = checkUserAndSetup(form, errors, request);

	if (errors.hasErrors()) {
	    return mv;
	}

	if (FWD_SUCCESS.equalsIgnoreCase(forward)) {
	    return new ModelAndView("sampleTypeManagementDefinition", "form", form);
	} else {
	    return new ModelAndView("redirect:/" + forward + ".html");
	}
    }

    @RequestMapping(value = "/UomManagement", method = RequestMethod.GET)
    public ModelAndView showUomManagementPage(HttpServletRequest request) throws Exception {
	String forward = FWD_SUCCESS;

	BaseForm form = new BaseForm();
	form.setFormName("uomManagement");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	ModelAndView mv = checkUserAndSetup(form, errors, request);

	if (errors.hasErrors()) {
	    return mv;
	}

	if (FWD_SUCCESS.equalsIgnoreCase(forward)) {
	    return new ModelAndView("uomManagementDefinition", "form", form);
	} else {
	    return new ModelAndView("redirect:/" + forward + ".html");
	}
    }

    @RequestMapping(value = "/PanelManagement", method = RequestMethod.GET)
    public ModelAndView showPanelManagementPage(HttpServletRequest request) throws Exception {
	String forward = FWD_SUCCESS;

	BaseForm form = new BaseForm();
	form.setFormName("panelManagement");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	ModelAndView mv = checkUserAndSetup(form, errors, request);

	if (errors.hasErrors()) {
	    return mv;
	}

	if (FWD_SUCCESS.equalsIgnoreCase(forward)) {
	    return new ModelAndView("panelManagementDefinition", "form", form);
	} else {
	    return new ModelAndView("redirect:/" + forward + ".html");
	}
    }

    @Override
    protected String getPageTitleKey() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected String getPageSubtitleKey() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected ModelAndView findLocalForward(String forward, BaseForm form) {
	// TODO Auto-generated method stub
	return null;
    }

}
