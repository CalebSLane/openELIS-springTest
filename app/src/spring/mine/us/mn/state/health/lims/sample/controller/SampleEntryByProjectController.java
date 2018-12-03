package spring.mine.us.mn.state.health.lims.sample.controller;

import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.SampleEntryByProjectForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.formfields.FormFields;
import us.mn.state.health.lims.common.services.DisplayListService;
import us.mn.state.health.lims.common.services.SampleOrderService;
import us.mn.state.health.lims.common.services.DisplayListService.ListType;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.dictionary.ObservationHistoryList;
import us.mn.state.health.lims.organization.util.OrganizationTypeList;
import us.mn.state.health.lims.patient.action.bean.PatientManagementInfo;
import us.mn.state.health.lims.patient.action.bean.PatientSearch;
import us.mn.state.health.lims.patient.saving.Accessioner;
import us.mn.state.health.lims.patient.valueholder.ObservationData;
import us.mn.state.health.lims.sample.form.ProjectData;
import us.mn.state.health.lims.sample.util.CI.ProjectForm;

@Controller
public class SampleEntryByProjectController extends BaseSampleEntryController {
  @RequestMapping(
      value = "/SampleEntryByProject",
      method = RequestMethod.GET
  )
  public ModelAndView showSampleEntryByProject(HttpServletRequest request) throws Exception {
    String forward = FWD_SUCCESS;
    SampleEntryByProjectForm form = new SampleEntryByProjectForm();
    form.setFormName("sampleEntryByProjectForm");
    form.setFormAction("");
    BaseErrors errors = new BaseErrors();
    ModelAndView mv = checkUserAndSetup(form, errors, request);

    if (errors.hasErrors()) {
    	return mv;
    }
    
    request.getSession().setAttribute(IActionConstants.SAVE_DISABLED, IActionConstants.TRUE);


	// retrieve the current project, before clearing, so that we can set it on
	// later.
	    String projectFormId = "";
    	if (form.getObservations() != null) {
    	    projectFormId = Accessioner.findProjectFormName(form);
    	} else {
    	    form.setObservations(new ObservationData());
    	}
	updateRequestType(request);

	// Set received date and entered date to today's date
	Date today = Calendar.getInstance().getTime();
	String dateAsText = DateUtil.formatDateAsText(today);
	form.setCurrentDate(dateAsText);
	form.setReceivedDateForDisplay(dateAsText);
	form.setInterviewDate(dateAsText);
	form.setLabNo("");
	
	/*PropertyUtils.setProperty(form, "currentDate", dateAsText);
	PropertyUtils.setProperty(dynaForm, "receivedDateForDisplay", dateAsText);
	PropertyUtils.setProperty(dynaForm, "interviewDate", dateAsText);
	PropertyUtils.setProperty(dynaForm, "labNo", "");*/

	SampleOrderService sampleOrderService = new SampleOrderService();
	form.setSampleOrderItems(sampleOrderService.getSampleOrderItem());
	form.setPatientProperties(new PatientManagementInfo());
	form.setPatientSearch(new PatientSearch());
	form.setSampleTypes(DisplayListService.getList(ListType.SAMPLE_TYPE_ACTIVE));
	form.setTestSectionList(DisplayListService.getList(ListType.TEST_SECTION));
	/*PropertyUtils.setProperty(dynaForm, "sampleOrderItems", sampleOrderService.getSampleOrderItem());
	PropertyUtils.setProperty(dynaForm, "patientProperties", new PatientManagementInfo());
	PropertyUtils.setProperty(dynaForm, "patientSearch", new PatientSearch());
	PropertyUtils.setProperty(dynaForm, "sampleTypes", DisplayListService.getList(ListType.SAMPLE_TYPE_ACTIVE));
	PropertyUtils.setProperty(dynaForm, "testSectionList", DisplayListService.getList(ListType.TEST_SECTION));*/

	addGenderList(form);
	addProjectList(form);

	addOrganizationLists(form);
	addAllPatientFormLists(form);

	setProjectFormName(form, projectFormId);

	if (FormFields.getInstance().useField(FormFields.Field.InitialSampleCondition)) {
	    PropertyUtils.setProperty(form, "initialSampleConditionList",
		    DisplayListService.getList(ListType.INITIAL_SAMPLE_CONDITION));
	}

	forward = findForward(projectFormId);

    return findForward(forward, form);}

  protected ModelAndView findLocalForward(String forward, BaseForm form) {
    if ("success".equals(forward)) {
      return new ModelAndView("sampleEntryByProjectDefinition", "form", form);
    } else if ("eid_entry".equals(forward)) {
      return new ModelAndView("sampleEntryEIDDefinition", "form", form);
    } else if ("vl_entry".equals(forward)) {
      return new ModelAndView("sampleEntryVLDefinition", "form", form);
    } else if ("fail".equals(forward)) {
      return new ModelAndView("homePageDefinition", "form", form);
    } else {
      return new ModelAndView("PageNotFound");
    }
  }

  /**
   * One form ID at this time is actually on another JSP page, someday maybe
   * others
   * 
   * @param projectFormId
   * @return
   */
  private String findForward(String projectFormId) {
	ProjectForm projectForm = ProjectForm.findProjectFormByFormId(projectFormId);
	if (projectForm == null) {
	    return FWD_SUCCESS;
	}
	switch (projectForm) {
	case EID:
	    return FWD_EID_ENTRY;
	case VL:
	    return FWD_VL_ENTRY;
	default:
	    return FWD_SUCCESS;
	}
  }

  protected String getPageSubtitleKey() {
	String key = null;

	switch (requestType) {
	case INITIAL: {
	    key = "banner.menu.createSample.Initial";
	    break;
	}
	case VERIFY: {
	    key = "banner.menu.createSample.Verify";
	    break;
	}

	default: {
	    key = "banner.menu.sampleCreate";
	}
	}

	return key;
  }

  protected void addOrganizationLists(BaseForm form)
	    throws LIMSRuntimeException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

      SampleEntryByProjectForm samplentryByProjectForm = (SampleEntryByProjectForm) form;
      if (samplentryByProjectForm.getProjectData() == null) {
	  samplentryByProjectForm.setProjectData(new ProjectData());
      }
	// Get ARV Centers
      samplentryByProjectForm.getProjectData().setARVCenters(OrganizationTypeList.ARV_ORGS.getList());
      samplentryByProjectForm.setOrganizationTypeLists(OrganizationTypeList.MAP);
	/*PropertyUtils.setProperty(samplentryByProjectForm, "projectData.ARVCenters", OrganizationTypeList.ARV_ORGS.getList());
	PropertyUtils.setProperty(samplentryByProjectForm, "organizationTypeLists", OrganizationTypeList.MAP);*/

	// Get EID Sites
      samplentryByProjectForm.getProjectData().setEIDSites(OrganizationTypeList.EID_ORGS.getList());
      samplentryByProjectForm.getProjectData().setEIDSitesByName(OrganizationTypeList.EID_ORGS_BY_NAME.getList());
	/*PropertyUtils.setProperty(samplentryByProjectForm, "projectData.EIDSites", OrganizationTypeList.EID_ORGS.getList());
	PropertyUtils.setProperty(samplentryByProjectForm, "projectData.EIDSitesByName",
		OrganizationTypeList.EID_ORGS_BY_NAME.getList());*/

	// Get EID whichPCR List
	// PropertyUtils.setProperty(dynaForm, "ProjectData.eidWhichPCRList",
	// ObservationHistoryList.EID_WHICH_PCR.getList());

	// Get EID secondTestReason List
      samplentryByProjectForm.getProjectData().setEidSecondPCRReasonList(ObservationHistoryList.EID_SECOND_PCR_REASON.getList());
	/*PropertyUtils.setProperty(samplentryByProjectForm, "projectData.eidSecondPCRReasonList",
		ObservationHistoryList.EID_SECOND_PCR_REASON.getList());*/

	// Get SPE Request Reasons
	      samplentryByProjectForm.getProjectData().setRequestReasons(ObservationHistoryList.SPECIAL_REQUEST_REASONS.getList());
	/*PropertyUtils.setProperty(samplentryByProjectForm, "projectData.requestReasons",
		ObservationHistoryList.SPECIAL_REQUEST_REASONS.getList());*/

	      samplentryByProjectForm.getProjectData().setIsUnderInvestigationList(ObservationHistoryList.YES_NO.getList());
	/*PropertyUtils.setProperty(samplentryByProjectForm, "projectData.isUnderInvestigationList",
		ObservationHistoryList.YES_NO.getList());*/
  }

  protected String getPageTitleKey() {
    return null;
  }

}
