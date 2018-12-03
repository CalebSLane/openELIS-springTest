package spring.mine.testconfiguration.form;

import java.util.List;

import spring.mine.common.form.BaseForm;

public class TestRenameEntryForm extends BaseForm {
    
    List testList;
    String nameEnglish;
    String nameFrench;
    String reportNameEnglish;
    String reportNameFrench;
    String testId;
    public List getTestList() {
        return testList;
    }
    public void setTestList(List testList) {
        this.testList = testList;
    }
    public String getNameEnglish() {
        return nameEnglish;
    }
    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }
    public String getNameFrench() {
        return nameFrench;
    }
    public void setNameFrench(String nameFrench) {
        this.nameFrench = nameFrench;
    }
    public String getReportNameEnglish() {
        return reportNameEnglish;
    }
    public void setReportNameEnglish(String reportNameEnglish) {
        this.reportNameEnglish = reportNameEnglish;
    }
    public String getReportNameFrench() {
        return reportNameFrench;
    }
    public void setReportNameFrench(String reportNameFrench) {
        this.reportNameFrench = reportNameFrench;
    }
    public String getTestId() {
        return testId;
    }
    public void setTestId(String testId) {
        this.testId = testId;
    }
    
    

}
