package spring.mine.us.mn.state.health.lims.common.form;

import java.lang.String;
import spring.mine.common.form.BaseForm;

public class WebTestInfoForm extends BaseForm {
  private String xmlWad = "";

  public String getXmlWad() {
    return this.xmlWad;
  }

  public void setXmlWad(String xmlWad) {
    this.xmlWad = xmlWad;
  }
}
