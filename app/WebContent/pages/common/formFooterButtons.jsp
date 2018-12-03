<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="us.mn.state.health.lims.common.action.IActionConstants,
			org.apache.struts.Globals, 
			us.mn.state.health.lims.common.util.Versioning"
%>
<%@ page isELIgnored="false"%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%!
String path = "";
String basePath = "";
%>

<c:set var="success" value="${success}" />

<%--bugzilla 1908 changed some disabled values for Vietnam tomcat/linux--%>
	<%
	       path = request.getContextPath();
           basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

		    String previousDisabled = "false";
            String nextDisabled = "false";
            if (request.getAttribute(IActionConstants.PREVIOUS_DISABLED) != null) {
               previousDisabled = (String)request.getAttribute(IActionConstants.PREVIOUS_DISABLED);
            }
            if (request.getAttribute(IActionConstants.NEXT_DISABLED) != null) {
               nextDisabled = (String)request.getAttribute(IActionConstants.NEXT_DISABLED);
            }
            String saveDisabled = (String)request.getAttribute(IActionConstants.SAVE_DISABLED);

        %>

<script language="JavaScript1.2">
//bugzilla 1467 this is for next/previous buttons on EDIT screens (in case user made changes without saving)
function confirmSaveForwardPopup(direction)
{
  var myWin = createSmallConfirmPopup( "", null , null );

   <%

      out.println("var message = null;");

      java.util.Locale locale = (java.util.Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
      String message =
					us.mn.state.health.lims.common.util.resources.ResourceLocator.getInstance().getMessageResources().getMessage(
					locale,
                    "message.popup.confirm.saveandforward");

     out.println("message = '" + message +"';");

     String button1 = 	us.mn.state.health.lims.common.util.resources.ResourceLocator.getInstance().getMessageResources().getMessage(
					    locale,
                       "label.button.yes");
     String button2 = 	us.mn.state.health.lims.common.util.resources.ResourceLocator.getInstance().getMessageResources().getMessage(
					    locale,
                       "label.button.no");
     String button3 = 	us.mn.state.health.lims.common.util.resources.ResourceLocator.getInstance().getMessageResources().getMessage(
					    locale,
                       "label.button.cancel");

     String title = 	us.mn.state.health.lims.common.util.resources.ResourceLocator.getInstance().getMessageResources().getMessage(
					    locale,
                       "title.popup.confirm.saveandforward");

     String space = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";


 %>

    var href = "<%=basePath%>css/openElisCore.css?ver=<%= Versioning.getBuildNumber() %>";

    var strHTML = "";

    strHTML = '<html><link rel="stylesheet" type="text/css" href="' + href + '" /><head><';
    //bugzilla 1510 cross-browser use hasFocus not hasFocus()
    //bugzilla 1903 fix problem - go back to hasFocus() for now
    strHTML += 'SCRIPT LANGUAGE="JavaScript">var tmr = 0;function fnHandleFocus(){if(window.opener.document.hasFocus()){window.focus();clearInterval(tmr);tmr = 0;}else{if(tmr == 0)tmr = setInterval("fnHandleFocus()", 500);}}</SCRIPT';
    strHTML += '><';
    strHTML += 'SCRIPT LANGUAGE="javascript" >'
    strHTML += 'var imp= null; function impor(){imp="norefresh";} ';
    strHTML += ' function fcl(){  if(imp!="norefresh") {  window.opener.reFreshCurWindow(); }}';

    strHTML += '  function goToNextActionSave(){ ';
    strHTML += ' var reqParms = "?direction=next&ID="; ';
    strHTML += ' window.opener.setAction(window.opener.document.forms[0], "UpdateNextPrevious", "yes", reqParms);self.close();} ';
    strHTML += '  function goToPreviousActionSave(){ ';
    strHTML += ' var reqParms = "?direction=previous&ID="; ';
    strHTML += ' window.opener.setAction(window.opener.document.forms[0], "UpdateNextPrevious", "yes", reqParms);self.close();} ';

    strHTML += '  function goToNextActionNoSave(){ ';
    strHTML += ' var reqParms = "?direction=next&ID="; ';
    strHTML += ' window.opener.setAction(window.opener.document.forms[0], "NextPrevious", "no", reqParms);self.close();} ';
    strHTML += '  function goToPreviousActionNoSave(){ ';
    strHTML += ' var reqParms = "?direction=previous&ID="; ';
    strHTML += ' window.opener.setAction(window.opener.document.forms[0], "NextPrevious", "no", reqParms);self.close();} ';

    strHTML += ' setTimeout("impor()",359999);</SCRIPT';
    strHTML += '><title>' + "<%=title%>" + '</title></head>';
    strHTML += '<body onBlur="fnHandleFocus();" onLoad="fnHandleFocus();" ><form name="confirmSaveIt" method="get" action=""><div id="popupBody"><table><tr><td class="popuplistdata">';
    strHTML += message;
    if (direction == 'next') {
     strHTML += '<br><center><input type="button"  name="save" value="' + "<%=button1%>" + '" onClick="goToNextActionSave();" />';
     strHTML += "<%=space%>";
     strHTML += '<input type="button"  name="save" value="' + "<%=button2%>" + '" onClick="goToNextActionNoSave();"/>';
    } else if (direction == 'previous') {
     strHTML += '<br><center><input type="button"  name="save" value="' + "<%=button1%>" + '" onClick="goToPreviousActionSave();" />';
     strHTML += "<%=space%>";
     strHTML += '<input type="button"  name="save" value="' + "<%=button2%>" + '" onClick="goToPreviousActionNoSave();"/>';
    }
    strHTML += "<%=space%>";
    strHTML += '<input type="button"  name="cancel" value="' + "<%=button3%>" + '" onClick="self.close();" /></center></div>';
    strHTML += '</td></tr></table></form></body></html>';

     myWin.document.write(strHTML);

     //bugzilla 1579
     myWin.window.document.close();

     //bugzilla 1579
     setTimeout ('myWin.close()', 360000);
}

//bugzilla 1467
function previousAction(form, ignoreFields) {
  if (isDirty(form, ignoreFields)) {
     confirmSaveForwardPopup('previous');
  } else {
     setAction(window.document.forms[0], 'NextPrevious', 'no', '?direction=previous&ID=');
  }
}

//bugzilla 1467
function nextAction(form, ignoreFields) {
  if (isDirty(form, ignoreFields)) {
      //popup to give user option to save, don't save AND go to next, cancel
      confirmSaveForwardPopup('next');
  } else {
      setAction(window.document.forms[0], 'NextPrevious', 'no', '?direction=next&ID=');
  }
}

<%if( request.getAttribute(IActionConstants.FWD_SUCCESS) != null &&
      ((Boolean)request.getAttribute(IActionConstants.FWD_SUCCESS)) ) { %>
if( typeof(showSuccessMessage) != 'undefined' ){
	showSuccessMessage( true );
}
<% } %>
</script>
<center>
<table border="0" cellpadding="0" cellspacing="0">
	<tbody valign="middle">
		<tr>
	      	<td>
  			<form:button onclick="if(checkClicked())
							 {
							 	return false;
							 }
							 else {
                window.onbeforeunload = null; // Added to flag that formWarning alert isn't needed. Used in unifiedSystemUser.jsp
							  setAction(window.document.forms[0], 'Update', 'yes', '?ID=');

							 }" property="save" disabled="<%=Boolean.valueOf(saveDisabled).booleanValue()%>">
  			   <spring:message code="label.button.save" />
  			</form:button>
  	    </td>

		<td>&nbsp;</td>
		<td>
  			<form:button onclick="setAction(window.document.forms[0], 'Cancel', 'no', '');"  property="cancel" >
  			   <%--AIS - bugzilla 1860--%>
  			   <spring:message code="label.button.exit"/>
  			</form:button>
	    </td>
   		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
  		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
  		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
  		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	    <td>
  			<form:button onclick="previousAction(window.document.forms[0], '');" property="previous" disabled="<%=Boolean.valueOf(previousDisabled).booleanValue()%>">
  			   label.button.previous
  			</form:button>
	    </td>
     	<td>&nbsp;</td>
	    <td>
  			<form:button onclick="nextAction(window.document.forms[0], '');"  property="next" disabled="<%=Boolean.valueOf(nextDisabled).booleanValue()%>">
  			   <spring:message code="label.button.next"/>
  			</form:button>
	    </td>
	    </tr>
	 </tbody>
</table>
</center>