package spring.mine.us.mn.state.health.lims.common.form;

import java.lang.String;
import java.util.List;
import spring.mine.common.form.BaseForm;

public class OrganizationMenuForm extends BaseForm {
  private List menuList;

  private String[] selectedIDs;

  private String searchString = "";

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

  public String getSearchString() {
    return this.searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }
}
