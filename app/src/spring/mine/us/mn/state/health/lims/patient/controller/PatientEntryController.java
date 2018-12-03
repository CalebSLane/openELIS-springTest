package spring.mine.us.mn.state.health.lims.patient.controller;

import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import spring.mine.us.mn.state.health.lims.common.form.LoginForm;
import spring.mine.us.mn.state.health.lims.common.form.PatientEditByProjectForm;
import spring.mine.us.mn.state.health.lims.common.form.PatientEntryByProjectForm;
import spring.mine.us.mn.state.health.lims.common.form.SamplePatientEntryForm;
import us.mn.state.health.lims.address.dao.AddressPartDAO;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.provider.query.PatientSearchResults;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.patient.action.PatientManagementUpdateAction.PatientUpdateStatus;
import us.mn.state.health.lims.patient.action.bean.PatientManagementInfo;
import us.mn.state.health.lims.patient.action.bean.PatientSearch;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.patienttype.dao.PatientPatientTypeDAO;
import us.mn.state.health.lims.patienttype.daoimpl.PatientPatientTypeDAOImpl;
import us.mn.state.health.lims.patienttype.util.PatientTypeMap;
import us.mn.state.health.lims.patienttype.valueholder.PatientPatientType;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.sample.dao.SearchResultsDAO;
import us.mn.state.health.lims.sample.daoimpl.SearchResultsDAOImp;

@Controller
public class PatientEntryController extends BaseController {

    @Override
    protected ModelAndView findLocalForward(String forward, BaseForm form) {
	// TODO Auto-generated method stub
	return null;
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

   /* protected Patient patient;
    protected Person person;
    private List<PatientIdentity> patientIdentities;
    private String patientID = "";
    private static PatientIdentityDAO identityDAO = new PatientIdentityDAOImpl();
    private static PatientDAO patientDAO = new PatientDAOImpl();
    private static PersonAddressDAO personAddressDAO = new PersonAddressDAOImpl();
    private static final String AMBIGUOUS_DATE_CHAR = ConfigurationProperties.getInstance()
	    .getPropertyValue(ConfigurationProperties.Property.AmbiguousDateHolder);
    private static final String AMBIGUOUS_DATE_HOLDER = AMBIGUOUS_DATE_CHAR + AMBIGUOUS_DATE_CHAR;
    protected PatientUpdateStatus patientUpdateStatus = PatientUpdateStatus.NO_ACTION;

    private static String ADDRESS_PART_VILLAGE_ID;
    private static String ADDRESS_PART_COMMUNE_ID;
    private static String ADDRESS_PART_DEPT_ID;

    public static enum PatientUpdateStatus {
	NO_ACTION, UPDATE, ADD
    }

    static {
	AddressPartDAO addressPartDAO = new AddressPartDAOImpl();
	List<AddressPart> partList = addressPartDAO.getAll();

	for (AddressPart addressPart : partList) {
	    if ("department".equals(addressPart.getPartName())) {
		ADDRESS_PART_DEPT_ID = addressPart.getId();
	    } else if ("commune".equals(addressPart.getPartName())) {
		ADDRESS_PART_COMMUNE_ID = addressPart.getId();
	    } else if ("village".equals(addressPart.getPartName())) {
		ADDRESS_PART_VILLAGE_ID = addressPart.getId();
	    }
	}
    }

    @RequestMapping(value = "/PatientEntryByProject", method = RequestMethod.GET)
    public ModelAndView showPatientEntryByProject(HttpServletRequest request) {
	String forward = FWD_SUCCESS;
	PatientEntryByProjectForm form = new PatientEntryByProjectForm();
	form.setFormName("patientEntryByProjectForm");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	forward = checkUserAndSetup(form, errors, request);

	if ("success".equals(forward)) {
	    return new ModelAndView("patientEntryByProjectDefinition", "form", form);
	} else if ("fail".equals(forward)) {
	    return new ModelAndView("homePageDefinition", "form", form);
	} else {
	    return new ModelAndView("forward:LoginPage.html");
	}
    }

    @RequestMapping(value = "/PatientEntryByProjectUpdate", method = RequestMethod.GET)
    public ModelAndView showPatientEntryByProjectUpdate(HttpServletRequest request) {
	String forward = FWD_SUCCESS;
	PatientEntryByProjectForm form = new PatientEntryByProjectForm();
	form.setFormName("patientEntryByProjectForm");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	forward = checkUserAndSetup(form, errors, request);

	if ("success".equals(forward)) {
	    return new ModelAndView("/PatientEntryByProject.do?forward=success", "form", form);
	} else if ("fail".equals(forward)) {
	    return new ModelAndView("patientEntryByProjectDefinition", "form", form);
	} else {
	    return new ModelAndView("forward:LoginPage.html");
	}
    }

    @RequestMapping(value = "/PatientEditByProject", method = RequestMethod.GET)
    public ModelAndView showPatientEditByProject(HttpServletRequest request) {
	String forward = FWD_SUCCESS;
	PatientEditByProjectForm form = new PatientEditByProjectForm();
	form.setFormName("PatientEditByProjectForm");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	forward = checkUserAndSetup(form, errors, request);

	if ("success".equals(forward)) {
	    return new ModelAndView("patientEditByProjectDefinition", "form", form);
	} else if ("fail".equals(forward)) {
	    return new ModelAndView("homePageDefinition", "form", form);
	} else {
	    return new ModelAndView("forward:LoginPage.html");
	}
    }

    @RequestMapping(value = "/PatientEditByProjectSave", method = RequestMethod.GET)
    public ModelAndView showPatientEditByProjectSave(HttpServletRequest request) {
	String forward = FWD_SUCCESS;
	PatientEditByProjectForm form = new PatientEditByProjectForm();
	form.setFormName("PatientEditByProjectForm");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	forward = checkUserAndSetup(form, errors, request);

	if ("success".equals(forward)) {
	    return new ModelAndView("/PatientEditByProject.do?forward=success", "form", form);
	} else if ("fail".equals(forward)) {
	    return new ModelAndView("patientEditByProjectDefinition", "form", form);
	} else {
	    return new ModelAndView("forward:LoginPage.html");
	}
    }

    @RequestMapping(value = "/PatientManagement", method = RequestMethod.GET)
    public ModelAndView showPatientManagement(HttpServletRequest request) {
	String forward = FWD_SUCCESS;
	SamplePatientEntryForm form = new SamplePatientEntryForm();
	form.setFormName("samplePatientEntryForm");
	form.setFormAction("");
	BaseErrors errors = new BaseErrors();
	forward = checkUserAndSetup(form, errors, request);

	if (errors.hasErrors()) {
	    return new ModelAndView("forward:" + forward + "html");
	}

	request.getSession().setAttribute(IActionConstants.SAVE_DISABLED, IActionConstants.TRUE);
	form.setPatientProperties(new PatientManagementInfo());
	form.setPatientSearch(new PatientSearch());
	form.getPatientProperties().setPatientProcessingStatus("add");

	if ("success".equals(forward)) {
	    return new ModelAndView("patientManagementDefinition", "form", form);
	} else if ("fail".equals(forward)) {
	    return new ModelAndView("homePageDefinition", "form", form);
	} else {
	    return new ModelAndView("forward:LoginPage.html");
	}
    }

    @RequestMapping(value = "/PatientManagementUpdate", method = RequestMethod.POST)
    public ModelAndView showPatientManagementUpdate(@ModelAttribute("form") SamplePatientEntryForm form,
	    BindingResult result, ModelMap model, HttpServletRequest request)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	String forward = FWD_SUCCESS;
	form.setFormName("samplePatientEntryForm");
	form.setFormAction("");
	forward = checkUserAndSetup(form, result, request);
	if ("loginPage".equals(forward)) {
	    return new ModelAndView("forward:LoginPage.html");
	}

	form.setPatientSearch(new PatientSearch());

	PatientManagementInfo patientInfo = form.getPatientProperties();
	setPatientUpdateStatus(patientInfo);

	if (patientUpdateStatus != PatientUpdateStatus.NO_ACTION) {

	    preparePatientData(result, request, patientInfo);

	    if (result.hasErrors()) {
		
		 * saveErrors(request, errors); request.setAttribute(Globals.ERROR_KEY, errors);
		 * return mapping.findForward(FWD_FAIL);
		 
		model.addAttribute("errors", result.getAllErrors());
		model.addAttribute("form", form);
		return new ModelAndView("patientManagementDefinition", model);
	    }

	    Transaction tx = HibernateUtil.getSession().beginTransaction();

	    try {

		persistPatientData(patientInfo);

		tx.commit();

	    } catch (LIMSRuntimeException lre) {
		tx.rollback();

		if (lre.getException() instanceof StaleObjectStateException) {
		    result.reject("errors.OptimisticLockException", "errors.OptimisticLockException");
		    
		     * );errors.add(ActionMessages.GLOBAL_MESSAGE, new
		     * ActionError("errors.OptimisticLockException", null, null));
		     
		} else {
		    lre.printStackTrace();
		    result.reject("errors.UpdateException", "errors.UpdateException");
		    
		     * errors.add(ActionMessages.GLOBAL_MESSAGE, new
		     * ActionError("errors.UpdateException", null, null));
		     
		}

		
		 * saveErrors(request, errors); request.setAttribute(Globals.ERROR_KEY, errors);
		 
		model.addAttribute("errors", result.getAllErrors());
		request.setAttribute(ALLOW_EDITS_KEY, "false");
		if (result.hasErrors()) {
		    
		     * saveErrors(request, errors); request.setAttribute(Globals.ERROR_KEY, errors);
		     * return mapping.findForward(FWD_FAIL);
		     
		    model.addAttribute("errors", result.getAllErrors());
		    model.addAttribute("form", form);
		    return new ModelAndView("patientManagementDefinition", model);
		}

	    } finally {
		HibernateUtil.closeSession();
	    }

	}

	setSuccessFlag(request, forward);

	if ("success".equals(forward)) {
	    return new ModelAndView("patientManagementDefinition", "form", form);
	} else if ("fail".equals(forward)) {
	    return new ModelAndView("patientManagementDefinition", "form", form);
	} else {
	    return new ModelAndView("forward:LoginPage.html");
	}
    }

    
     * (non-Javadoc)
     *
     * @see us.mn.state.health.lims.patient.action.IPatientUpdate#preparePatientData
     * (org.apache.struts.action.ActionMapping,
     * javax.servlet.http.HttpServletRequest,
     * us.mn.state.health.lims.common.action.BaseActionForm)
     
    public void preparePatientData(Errors errors, HttpServletRequest request, PatientManagementInfo patientInfo)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	if (currentUserId == null) {
	    currentUserId = getSysUserId(request);
	}

	validatePatientInfo(errors, patientInfo);
	if (errors.hasErrors()) {
	    return;
	}

	initMembers();

	if (patientUpdateStatus == PatientUpdateStatus.UPDATE) {
	    loadForUpdate(patientInfo);
	}

	copyFormBeanToValueHolders(patientInfo);

	setSystemUserID();

	setLastUpdatedTimeStamps(patientInfo);

    }

    private void validatePatientInfo(Errors errors, PatientManagementInfo patientInfo) {
	if (ConfigurationProperties.getInstance()
		.isPropertyValueEqual(ConfigurationProperties.Property.ALLOW_DUPLICATE_SUBJECT_NUMBERS, "false")) {
	    String newSTNumber = GenericValidator.isBlankOrNull(patientInfo.getSTnumber()) ? null
		    : patientInfo.getSTnumber();
	    String newSubjectNumber = GenericValidator.isBlankOrNull(patientInfo.getSubjectNumber()) ? null
		    : patientInfo.getSubjectNumber();
	    String newNationalId = GenericValidator.isBlankOrNull(patientInfo.getNationalId()) ? null
		    : patientInfo.getNationalId();

	    SearchResultsDAO search = new SearchResultsDAOImp();
	    List<PatientSearchResults> results = search.getSearchResults(null, null, newSTNumber, newSubjectNumber,
		    newNationalId, null, null, null);

	    if (!results.isEmpty()) {

		for (PatientSearchResults result : results) {
		    if (!result.getPatientID().equals(patientInfo.getPatientPK())) {
			if (newSTNumber != null && newSTNumber.equals(result.getSTNumber())) {
			    
			     * errors.add(ActionMessages.GLOBAL_MESSAGE, new
			     * ActionError("error.duplicate.STNumber", null, null));
			     
			    errors.reject("error.duplicate.STNumber", "error.duplicate.STNumber");
			}
			if (newSubjectNumber != null && newSubjectNumber.equals(result.getSubjectNumber())) {
			    
			     * errors.add(ActionMessages.GLOBAL_MESSAGE, new
			     * ActionError("error.duplicate.subjectNumber", null, null));
			     
			    errors.reject("error.duplicate.subjectNumber", "error.duplicate.subjectNumber");
			}
			if (newNationalId != null && newNationalId.equals(result.getNationalId())) {
			    
			     * errors.add(ActionMessages.GLOBAL_MESSAGE, new
			     * ActionError("error.duplicate.nationalId", null, null));
			     
			    errors.reject("error.duplicate.nationalId", "error.duplicate.nationalId");
			}
		    }
		}
	    }
	}

	validateBirthdateFormat(patientInfo, errors);

    }

    private void validateBirthdateFormat(PatientManagementInfo patientInfo, Errors errors) {
	String birthDate = patientInfo.getBirthDateForDisplay();
	boolean validBirthDateFormat = true;

	if (!GenericValidator.isBlankOrNull(birthDate)) {
	    validBirthDateFormat = birthDate.length() == 10;
	    // the regex matches ambiguous day and month or ambiguous day or completely
	    // formed date
	    if (validBirthDateFormat) {
		validBirthDateFormat = birthDate.matches("(((" + AMBIGUOUS_DATE_HOLDER + "|\\d{2})/\\d{2})|"
			+ AMBIGUOUS_DATE_HOLDER + "/(" + AMBIGUOUS_DATE_HOLDER + "|\\d{2}))/\\d{4}");
	    }

	    if (!validBirthDateFormat) {
		// errors.add(ActionMessages.GLOBAL_MESSAGE, new
		// ActionError("error.birthdate.format", null, null));
		errors.reject("error.birthdate.format", "error.birthdate.format");
	    }
	}
    }

    private void setLastUpdatedTimeStamps(PatientManagementInfo patientInfo) {
	String patientUpdate = patientInfo.getPatientLastUpdated();
	if (!GenericValidator.isBlankOrNull(patientUpdate)) {
	    Timestamp timeStamp = Timestamp.valueOf(patientUpdate);
	    patient.setLastupdated(timeStamp);
	}

	String personUpdate = patientInfo.getPersonLastUpdated();
	if (!GenericValidator.isBlankOrNull(personUpdate)) {
	    Timestamp timeStamp = Timestamp.valueOf(personUpdate);
	    person.setLastupdated(timeStamp);
	}
    }

    private void initMembers() {
	patient = new Patient();
	person = new Person();
	patientIdentities = new ArrayList<PatientIdentity>();
    }

    private void loadForUpdate(PatientManagementInfo patientInfo) {

	patientID = patientInfo.getPatientPK();
	patient = patientDAO.readPatient(patientID);
	person = patient.getPerson();

	patientIdentities = identityDAO.getPatientIdentitiesForPatient(patient.getId());
    }

    
     * (non-Javadoc)
     *
     * @see
     * us.mn.state.health.lims.patient.action.IPatientUpdate#setPatientUpdateStatus
     * (us.mn.state.health.lims.common.action.BaseActionForm)
     
    public void setPatientUpdateStatus(PatientManagementInfo patientInfo) {

	String status = patientInfo.getPatientProcessingStatus();

	if ("noAction".equals(status)) {
	    patientUpdateStatus = PatientUpdateStatus.NO_ACTION;
	} else if ("update".equals(status)) {
	    patientUpdateStatus = PatientUpdateStatus.UPDATE;
	} else {
	    patientUpdateStatus = PatientUpdateStatus.ADD;
	}
    }

    
     * (non-Javadoc)
     *
     * @see
     * us.mn.state.health.lims.patient.action.IPatientUpdate#getPatientUpdateStatus
     * ()
     
    public PatientUpdateStatus getPatientUpdateStatus() {
	return patientUpdateStatus;
    }

    private void copyFormBeanToValueHolders(PatientManagementInfo patientInfo)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	PropertyUtils.copyProperties(patient, patientInfo);
	PropertyUtils.copyProperties(person, patientInfo);
    }

    private void setSystemUserID() {
	patient.setSysUserId(currentUserId);
	person.setSysUserId(currentUserId);

	for (PatientIdentity identity : patientIdentities) {
	    identity.setSysUserId(currentUserId);
	}
    }

    public void persistPatientData(PatientManagementInfo patientInfo) throws LIMSRuntimeException {
	PersonDAO personDAO = new PersonDAOImpl();

	if (patientUpdateStatus == PatientUpdateStatus.ADD) {
	    personDAO.insertData(person);
	} else if (patientUpdateStatus == PatientUpdateStatus.UPDATE) {
	    personDAO.updateData(person);
	}
	patient.setPerson(person);

	if (patientUpdateStatus == PatientUpdateStatus.ADD) {
	    patientDAO.insertData(patient);
	} else if (patientUpdateStatus == PatientUpdateStatus.UPDATE) {
	    patientDAO.updateData(patient);
	}

	persistPatientRelatedInformation(patientInfo);
	patientID = patient.getId();
    }

    protected void persistPatientRelatedInformation(PatientManagementInfo patientInfo) {
	persistIdentityTypes(patientInfo);
	persistExtraPatientAddressInfo(patientInfo);
	persistPatientType(patientInfo);
    }

    protected void persistIdentityTypes(PatientManagementInfo patientInfo) {

	persistIdentityType(patientInfo.getSTnumber(), "ST");
	persistIdentityType(patientInfo.getMothersName(), "MOTHER");
	persistIdentityType(patientInfo.getAka(), "AKA");
	persistIdentityType(patientInfo.getInsuranceNumber(), "INSURANCE");
	persistIdentityType(patientInfo.getOccupation(), "OCCUPATION");
	persistIdentityType(patientInfo.getSubjectNumber(), "SUBJECT");
	persistIdentityType(patientInfo.getMothersInitial(), "MOTHERS_INITIAL");
	persistIdentityType(patientInfo.getEducation(), "EDUCATION");
	persistIdentityType(patientInfo.getMaritialStatus(), "MARITIAL");
	persistIdentityType(patientInfo.getNationality(), "NATIONALITY");
	persistIdentityType(patientInfo.getHealthDistrict(), "HEALTH DISTRICT");
	persistIdentityType(patientInfo.getHealthRegion(), "HEALTH REGION");
	persistIdentityType(patientInfo.getOtherNationality(), "OTHER NATIONALITY");
    }

    private void persistExtraPatientAddressInfo(PatientManagementInfo patientInfo) {
	PersonAddress village = null;
	PersonAddress commune = null;
	PersonAddress dept = null;
	List<PersonAddress> personAddressList = personAddressDAO.getAddressPartsByPersonId(person.getId());

	for (PersonAddress address : personAddressList) {
	    if (address.getAddressPartId().equals(ADDRESS_PART_COMMUNE_ID)) {
		commune = address;
		commune.setValue(patientInfo.getCommune());
		commune.setSysUserId(currentUserId);
		personAddressDAO.update(commune);
	    } else if (address.getAddressPartId().equals(ADDRESS_PART_VILLAGE_ID)) {
		village = address;
		village.setValue(patientInfo.getCity());
		village.setSysUserId(currentUserId);
		personAddressDAO.update(village);
	    } else if (address.getAddressPartId().equals(ADDRESS_PART_DEPT_ID)) {
		dept = address;
		if (!GenericValidator.isBlankOrNull(patientInfo.getAddressDepartment())
			&& !patientInfo.getAddressDepartment().equals("0")) {
		    dept.setValue(patientInfo.getAddressDepartment());
		    dept.setType("D");
		    dept.setSysUserId(currentUserId);
		    personAddressDAO.update(dept);
		}
	    }
	}

	if (commune == null) {
	    insertNewPatientInfo(ADDRESS_PART_COMMUNE_ID, patientInfo.getCommune(), "T");
	}

	if (village == null) {
	    insertNewPatientInfo(ADDRESS_PART_VILLAGE_ID, patientInfo.getCity(), "T");
	}

	if (dept == null && patientInfo.getAddressDepartment() != null
		&& !patientInfo.getAddressDepartment().equals("0")) {
	    insertNewPatientInfo(ADDRESS_PART_DEPT_ID, patientInfo.getAddressDepartment(), "D");
	}

    }

    private void insertNewPatientInfo(String partId, String value, String type) {
	PersonAddress address;
	address = new PersonAddress();
	address.setPersonId(person.getId());
	address.setAddressPartId(partId);
	address.setType(type);
	address.setValue(value);
	address.setSysUserId(currentUserId);
	personAddressDAO.insert(address);
    }

    public void persistIdentityType(String paramValue, String type) throws LIMSRuntimeException {

	Boolean newIdentityNeeded = true;
	String typeID = PatientIdentityTypeMap.getInstance().getIDForType(type);

	if (patientUpdateStatus == PatientUpdateStatus.UPDATE) {

	    for (PatientIdentity listIdentity : patientIdentities) {
		if (listIdentity.getIdentityTypeId().equals(typeID)) {

		    newIdentityNeeded = false;

		    if ((listIdentity.getIdentityData() == null && !GenericValidator.isBlankOrNull(paramValue))
			    || (listIdentity.getIdentityData() != null
				    && !listIdentity.getIdentityData().equals(paramValue))) {
			listIdentity.setIdentityData(paramValue);
			identityDAO.updateData(listIdentity);
		    }

		    break;
		}
	    }
	}

	if (newIdentityNeeded && !GenericValidator.isBlankOrNull(paramValue)) {
	    // either a new patient or a new identity item
	    PatientIdentity identity = new PatientIdentity();
	    identity.setPatientId(patient.getId());
	    identity.setIdentityTypeId(typeID);
	    identity.setSysUserId(currentUserId);
	    identity.setIdentityData(paramValue);
	    identity.setLastupdatedFields();
	    identityDAO.insertData(identity);
	}
    }

    protected void persistPatientType(PatientManagementInfo patientInfo) {

	PatientPatientTypeDAO patientPatientTypeDAO = new PatientPatientTypeDAOImpl();

	String typeName = null;

	try {
	    typeName = patientInfo.getPatientType();
	} catch (Exception ignored) {
	}

	if (!GenericValidator.isBlankOrNull(typeName) && !"0".equals(typeName)) {
	    String typeID = PatientTypeMap.getInstance().getIDForType(typeName);

	    PatientPatientType patientPatientType = patientPatientTypeDAO
		    .getPatientPatientTypeForPatient(patient.getId());

	    if (patientPatientType == null) {
		patientPatientType = new PatientPatientType();
		patientPatientType.setSysUserId(currentUserId);
		patientPatientType.setPatientId(patient.getId());
		patientPatientType.setPatientTypeId(typeID);
		patientPatientTypeDAO.insertData(patientPatientType);
	    } else {
		patientPatientType.setSysUserId(currentUserId);
		patientPatientType.setPatientTypeId(typeID);
		patientPatientTypeDAO.updateData(patientPatientType);
	    }
	}
    }

    public String getPatientId(BaseActionForm dynaForm) {
	return GenericValidator.isBlankOrNull(patientID) ? dynaForm.getString("patientPK") : patientID;
    }

    protected String getPageTitleKey() {
	return null;
    }

    protected String getPageSubtitleKey() {
	return null;
    }*/
}
