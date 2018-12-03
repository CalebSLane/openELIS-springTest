package spring.mine.barcode.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import spring.mine.barcode.form.PrintBarcodeForm;
import spring.mine.barcode.validator.PrintBarcodeFormValidator;
import spring.mine.common.controller.BaseController;
import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.BaseErrors;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.services.IPatientService;
import us.mn.state.health.lims.common.services.PatientService;
import us.mn.state.health.lims.common.services.StatusService;
import us.mn.state.health.lims.common.services.TestService;
import us.mn.state.health.lims.common.services.StatusService.AnalysisStatus;
import us.mn.state.health.lims.common.services.StatusService.SampleStatus;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.patient.action.bean.PatientSearch;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.bean.SampleEditItem;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

@Controller
public class BarcodeController extends BaseController {

    private static final TypeOfSampleDAO typeOfSampleDAO = new TypeOfSampleDAOImpl();
    private static final AnalysisDAO analysisDAO = new AnalysisDAOImpl();
    private static final SampleEditItemComparator testComparator = new SampleEditItemComparator();
    private static final Set<Integer> excludedAnalysisStatusList;
    private static final Set<Integer> ENTERED_STATUS_SAMPLE_LIST = new HashSet<Integer>();
    private static final Collection<String> ABLE_TO_CANCEL_ROLE_NAMES = new ArrayList<String>();

    static {
	excludedAnalysisStatusList = new HashSet<Integer>();
	excludedAnalysisStatusList
		.add(Integer.parseInt(StatusService.getInstance().getStatusID(AnalysisStatus.Canceled)));

	ENTERED_STATUS_SAMPLE_LIST.add(Integer.parseInt(StatusService.getInstance().getStatusID(SampleStatus.Entered)));
	ABLE_TO_CANCEL_ROLE_NAMES.add("Validator");
	ABLE_TO_CANCEL_ROLE_NAMES.add("Validation");
	ABLE_TO_CANCEL_ROLE_NAMES.add("Biologist");
    }

    @Autowired
    private PrintBarcodeFormValidator formValidator;

    @InitBinder("printBarcodeForm")
    protected void initBinder(WebDataBinder binder) {
	binder.addValidators(formValidator);
    }

    @RequestMapping(value = "/PrintBarcode", method = RequestMethod.GET)
    public ModelAndView showPrintBarcodePage(HttpServletRequest request) throws Exception {
	String forward = FWD_SUCCESS;

	PrintBarcodeForm form = new PrintBarcodeForm();
	form.setFormName("printBarcodeForm");
	form.setFormAction("PrintBarcode");
	BaseErrors errors = new BaseErrors();
	ModelAndView mv = checkUserAndSetup(form, errors, request);

	if (errors.hasErrors()) {
	    return mv;
	}

	String accessionNumber = form.getAccessionNumber();

	// if accession provided, data collected for display
	if (!GenericValidator.isBlankOrNull(accessionNumber)) {
	    Sample sample = getSample(accessionNumber);
	    if (sample != null && !GenericValidator.isBlankOrNull(sample.getId())) {
		List<SampleItem> sampleItemList = getSampleItems(sample);
		setPatientInfo(form, sample);
		List<SampleEditItem> currentTestList = getCurrentTestInfo(sampleItemList, accessionNumber, false);
		form.setExistingTests(currentTestList);
	    }
	}

	// search by accession number
	PatientSearch patientSearch = new PatientSearch();
	patientSearch.setLoadFromServerWithPatient(true);
	patientSearch.setSelectedPatientActionButtonText(StringUtil.getMessageForKey("label.patient.search.select"));
	form.setPatientSearch(patientSearch);

	if (FWD_SUCCESS.equalsIgnoreCase(forward)) {
	    return new ModelAndView("PrintBarcodeDefinition", "form", form);
	} else {
	    return new ModelAndView("redirect:/" + forward + ".html");
	}
    }

    @RequestMapping(value = "/PrintBarcode", method = RequestMethod.POST)
    public ModelAndView showPrintBarcodeResultPage(@ModelAttribute("form") PrintBarcodeForm form,
	    BindingResult result, ModelMap model, HttpServletRequest request) throws Exception {
	String forward = FWD_SUCCESS;

	form.setFormName("printBarcodeForm");
	form.setFormAction("PrintBarcode");
	ModelAndView mv = checkUserAndSetup(form, result, request);

	if (result.hasErrors()) {
	    return mv;
	}

	String accessionNumber = form.getAccessionNumber();

	// if accession provided, data collected for display
	if (!GenericValidator.isBlankOrNull(accessionNumber)) {
	    Sample sample = getSample(accessionNumber);
	    if (sample != null && !GenericValidator.isBlankOrNull(sample.getId())) {
		List<SampleItem> sampleItemList = getSampleItems(sample);
		setPatientInfo(form, sample);
		List<SampleEditItem> currentTestList = getCurrentTestInfo(sampleItemList, accessionNumber, false);
		form.setExistingTests(currentTestList);
	    }
	}

	// search by accession number
	PatientSearch patientSearch = new PatientSearch();
	patientSearch.setLoadFromServerWithPatient(true);
	patientSearch.setSelectedPatientActionButtonText(StringUtil.getMessageForKey("label.patient.search.select"));
	form.setPatientSearch(patientSearch);

	if (FWD_SUCCESS.equalsIgnoreCase(forward)) {
	    return new ModelAndView("PrintBarcodeDefinition", form.getFormName(), form);
	} else {
	    return new ModelAndView("redirect:/" + forward + ".html");
	}
    }

    /**
     * Get sample by accession number
     * 
     * @param accessionNumber
     *            That corresponds to the sample
     * @return The sample belonging to accession number
     */
    private Sample getSample(String accessionNumber) {
	SampleDAO sampleDAO = new SampleDAOImpl();
	return sampleDAO.getSampleByAccessionNumber(accessionNumber);
    }

    /**
     * Get list of SampleItems belonging to a sample
     * 
     * @param sample
     *            Containing all the individual sample items
     * @return The list of sample items belonging to sample
     */
    private List<SampleItem> getSampleItems(Sample sample) {
	SampleItemDAO sampleItemDAO = new SampleItemDAOImpl();

	return sampleItemDAO.getSampleItemsBySampleIdAndStatus(sample.getId(), ENTERED_STATUS_SAMPLE_LIST);
    }

    /**
     * Get list of tests corresponding to sample items
     * 
     * @param sampleItemList
     *            The list of sample items to fetch corresponding tests
     * @param accessionNumber
     *            The accession number corresponding to the sample
     * @param allowedToCancelAll
     * @return list of corresponding tests
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private List<SampleEditItem> getCurrentTestInfo(List<SampleItem> sampleItemList, String accessionNumber,
	    boolean allowedToCancelAll)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	List<SampleEditItem> currentTestList = new ArrayList<SampleEditItem>();
	for (SampleItem sampleItem : sampleItemList) {
	    addCurrentTestsToList(sampleItem, currentTestList, accessionNumber, allowedToCancelAll);
	}

	return currentTestList;
    }

    private void addCurrentTestsToList(SampleItem sampleItem, List<SampleEditItem> currentTestList,
	    String accessionNumber, boolean allowedToCancelAll) {

	TypeOfSample typeOfSample = new TypeOfSample();
	typeOfSample.setId(sampleItem.getTypeOfSampleId());
	typeOfSampleDAO.getData(typeOfSample);
	List<Analysis> analysisList = analysisDAO.getAnalysesBySampleItemsExcludingByStatusIds(sampleItem,
		excludedAnalysisStatusList);
	List<SampleEditItem> analysisSampleItemList = new ArrayList<SampleEditItem>();

	String collectionDate = DateUtil.convertTimestampToStringDate(sampleItem.getCollectionDate());
	String collectionTime = DateUtil.convertTimestampToStringTime(sampleItem.getCollectionDate());
	boolean canRemove = true;
	for (Analysis analysis : analysisList) {
	    SampleEditItem sampleEditItem = new SampleEditItem();
	    sampleEditItem.setTestId(analysis.getTest().getId());
	    sampleEditItem.setTestName(TestService.getUserLocalizedTestName(analysis.getTest()));
	    sampleEditItem.setSampleItemId(sampleItem.getId());

	    boolean canCancel = allowedToCancelAll
		    || (!StatusService.getInstance().matches(analysis.getStatusId(), AnalysisStatus.Canceled)
			    && StatusService.getInstance().matches(analysis.getStatusId(), AnalysisStatus.NotStarted));
	    if (!canCancel) {
		canRemove = false;
	    }
	    sampleEditItem.setCanCancel(canCancel);
	    sampleEditItem.setAnalysisId(analysis.getId());
	    sampleEditItem.setStatus(StatusService.getInstance().getStatusNameFromId(analysis.getStatusId()));
	    sampleEditItem.setSortOrder(analysis.getTest().getSortOrder());
	    sampleEditItem.setHasResults(
		    !StatusService.getInstance().matches(analysis.getStatusId(), AnalysisStatus.NotStarted));

	    analysisSampleItemList.add(sampleEditItem);
	    break;
	}

	if (!analysisSampleItemList.isEmpty()) {
	    Collections.sort(analysisSampleItemList, testComparator);
	    SampleEditItem firstItem = analysisSampleItemList.get(0);
	    firstItem.setAccessionNumber(accessionNumber + "-" + sampleItem.getSortOrder());
	    firstItem.setSampleType(typeOfSample.getLocalizedName());
	    firstItem.setCanRemoveSample(canRemove);
	    firstItem.setCollectionDate(collectionDate == null ? "" : collectionDate);
	    firstItem.setCollectionTime(collectionTime);
	    currentTestList.addAll(analysisSampleItemList);
	}
    }

    private void setPatientInfo(PrintBarcodeForm form, Sample sample)
	    throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

	Patient patient = new SampleHumanDAOImpl().getPatientForSample(sample);
	IPatientService patientService = new PatientService(patient);

	form.setPatientName(patientService.getLastFirstName());
	form.setDob(patientService.getEnteredDOB());
	form.setGender(patientService.getGender());
	form.setNationalId(patientService.getNationalId());
    }

    protected String getPageTitleKey() {
	return "barcode.print.title";
    }

    protected String getPageSubtitleKey() {
	return "barcode.print.title";
    }

    private static class SampleEditItemComparator implements Comparator<SampleEditItem> {

	public int compare(SampleEditItem o1, SampleEditItem o2) {
	    if (GenericValidator.isBlankOrNull(o1.getSortOrder())
		    || GenericValidator.isBlankOrNull(o2.getSortOrder())) {
		return o1.getTestName().compareTo(o2.getTestName());
	    }

	    try {
		return Integer.parseInt(o1.getSortOrder()) - Integer.parseInt(o2.getSortOrder());
	    } catch (NumberFormatException e) {
		return o1.getTestName().compareTo(o2.getTestName());
	    }
	}

    }

    @Override
    protected ModelAndView findLocalForward(String forward, BaseForm form) {
	// TODO Auto-generated method stub
	return null;
    }
}
