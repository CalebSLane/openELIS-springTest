package spring.mine.us.mn.state.health.lims.common.form;

import java.lang.String;
import java.util.List;
import spring.mine.common.form.BaseForm;

public class PatientTypeMenuForm extends BaseForm {
  private List menuList;

  private String[] selectedIDs;

  public List getMenuList() {
    return this.menuList;
  }

  public void setMenuList(List menuList) {
    this.menuList = menuList;
  }

  public String[] getSelectedIDs() {
    return this.selectedIDs;
  }

  public void setSelectedIDs(String[] selectedIDs) {
    this.selectedIDs = selectedIDs;
  }
}
