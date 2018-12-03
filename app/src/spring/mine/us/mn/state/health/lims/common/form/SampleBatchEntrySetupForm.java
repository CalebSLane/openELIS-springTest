package spring.mine.us.mn.state.health.lims.common.form;

import java.lang.Boolean;
import java.lang.String;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import spring.mine.common.form.BaseForm;
import us.mn.state.health.lims.sample.bean.SampleOrderItem;

public class SampleBatchEntrySetupForm extends BaseForm {
  private Timestamp lastupdated;

  private String currentDate = "";

  private String currentTime = "";

  private String project = "";

  private Collection projects;

  private List sampleTypes;

  private String sampleXML = "";

  private SampleOrderItem sampleOrderItems;

  private Collection initialSampleConditionList;

  private Collection testSectionList;

  private String method;

  private String facilityID;

  private Boolean facilityIDCheck = Boolean.FALSE;

  private Boolean patientInfoCheck = Boolean.FALSE;

  public Timestamp getLastupdated() {
    return this.lastupdated;
  }

  public void setLastupdated(Timestamp lastupdated) {
    this.lastupdated = lastupdated;
  }

  public String getCurrentDate() {
    return this.currentDate;
  }

  public void setCurrentDate(String currentDate) {
    this.currentDate = currentDate;
  }

  public String getCurrentTime() {
    return this.currentTime;
  }

  public void setCurrentTime(String currentTime) {
    this.currentTime = currentTime;
  }

  public String getProject() {
    return this.project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public Collection getProjects() {
    return this.projects;
  }

  public void setProjects(Collection projects) {
    this.projects = projects;
  }

  public List getSampleTypes() {
    return this.sampleTypes;
  }

  public void setSampleTypes(List sampleTypes) {
    this.sampleTypes = sampleTypes;
  }

  public String getSampleXML() {
    return this.sampleXML;
  }

  public void setSampleXML(String sampleXML) {
    this.sampleXML = sampleXML;
  }

  public SampleOrderItem getSampleOrderItems() {
    return this.sampleOrderItems;
  }

  public void setSampleOrderItems(SampleOrderItem sampleOrderItems) {
    this.sampleOrderItems = sampleOrderItems;
  }

  public Collection getInitialSampleConditionList() {
    return this.initialSampleConditionList;
  }

  public void setInitialSampleConditionList(Collection initialSampleConditionList) {
    this.initialSampleConditionList = initialSampleConditionList;
  }

  public Collection getTestSectionList() {
    return this.testSectionList;
  }

  public void setTestSectionList(Collection testSectionList) {
    this.testSectionList = testSectionList;
  }

  public String getMethod() {
    return this.method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getFacilityID() {
    return this.facilityID;
  }

  public void setFacilityID(String facilityID) {
    this.facilityID = facilityID;
  }

  public Boolean getFacilityIDCheck() {
    return this.facilityIDCheck;
  }

  public void setFacilityIDCheck(Boolean facilityIDCheck) {
    this.facilityIDCheck = facilityIDCheck;
  }

  public Boolean getPatientInfoCheck() {
    return this.patientInfoCheck;
  }

  public void setPatientInfoCheck(Boolean patientInfoCheck) {
    this.patientInfoCheck = patientInfoCheck;
  }
}
