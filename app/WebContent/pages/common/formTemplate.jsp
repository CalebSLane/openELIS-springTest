<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	import="us.mn.state.health.lims.common.action.IActionConstants,
			us.mn.state.health.lims.common.services.LocalizationService,
			us.mn.state.health.lims.common.util.ConfigurationProperties,
			org.owasp.encoder.Encode,
			us.mn.state.health.lims.common.util.Versioning"%>
<%@ page isELIgnored="false"%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<%!
String path = "";
String basePath = "";
%>
<%
path = request.getContextPath();
basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";


String form = (String)request.getAttribute(IActionConstants.FORM_NAME);

if (form == null) {
	form = "n/a";
}

  int startingRecNo = 1;

  if (request.getAttribute("startingRecNo") != null) {
       startingRecNo = Integer.parseInt((String)request.getAttribute("startingRecNo"));
  }

   request.setAttribute("ctx", request.getContextPath());

%>

<head>
<link rel="stylesheet" media="screen" type="text/css"
	href="<%=basePath%>css/openElisCore.css?ver=<%= Versioning.getBuildNumber() %>" />
<script type="text/javascript"
	src="<%=basePath%>scripts/jquery-1.8.0.min.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/jquery.dataTables.min.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/bootstrap.min.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript">
var  $jq = jQuery.noConflict();
</script>
<script language="JavaScript1.2"
	src="<%=basePath%>scripts/utilities.jsp"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/prototype-1.5.1.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/scriptaculous.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/overlibmws.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/ajaxtags-1.2.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/Tooltip-0.6.0.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/lightbox.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script language="JavaScript1.2">


function setAction(form, action, validate, parameters) {
    //alert("Iam in setAction " + form.name + " " + form.action);
   //for (var i = 0; i < form.elements.length; i++) {

      //alert("This is a form element " + form.elements[i].name + " " + form.elements[i].value);

    //}

    var sessionid = getSessionFromURL(form.action);
	var context = '<%= request.getContextPath() %>';
	var formName = form.name;
	//alert("form name " + formName);
	var parsedFormName = formName.substring(1, formName.length - 4);
	parsedFormName = formName.substring(0,1).toUpperCase() + parsedFormName;
    //alert("parsedFormName " + parsedFormName);

    var idParameter = '<%= Encode.forJavaScript((String)request.getParameter("ID")) %>';
    var startingRecNoParameter = '<%= Encode.forJavaScript((String)request.getParameter("startingRecNo")) %>';
    //alert("This is idParameter " + idParameter);
    if (!idParameter) {
       idParameter = '0';
    }

    if (!startingRecNoParameter) {
       startingRecNoParameter = '1';
    }

    if (parameters != '') {
	   parameters = parameters + idParameter;
	} else {
	   parameters = parameters + "?ID=" + idParameter;
	}
    parameters = parameters + "&startingRecNo=" + startingRecNoParameter;


	form.action = context + '/' + action + parsedFormName + ".do"  + sessionid + parameters ;
	form.validateDocument = new Object();
	form.validateDocument.value = validate;
	//alert("Going to validatedAnDsubmitForm this is action " + form.action);
	validateAndSubmitForm(form);

}

//default behavior -- overide for behavior
function /*boolean*/ handleEnterEvent(){
	return true;
}

function enterIntercepter(e) {
	var code = e ? e.which : window.event.keyCode;
	if( code == 13){
		return handleEnterEvent()
	}
	
	return true
}
document.onkeypress = enterIntercepter;
if (document.layers) {
	document.captureEvents(Event.KEYPRESS);
}
</script>
<%
	if (request.getAttribute("cache") != null && request.getAttribute("cache").toString().equals("false"))
	{
%>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, proxy-revalidate, must-revalidate" />
<%-- HTTP 1.1 --%>
<meta http-equiv="Pragma" content="no-cache" />
<%-- HTTP 1.0 --%>
<meta http-equiv="Expires" content="0" />
<%
	}
%>

<title>
	<c:if test="${!empty title}">
		<c:out value="${title}" />
	</c:if> 
	<c:if test="${empty title}">
		<%=LocalizationService.getLocalizedValueById( ConfigurationProperties.getInstance().getPropertyValue( ConfigurationProperties.Property.BANNER_TEXT ) )%>
	</c:if>
</title>
<tiles:insertAttribute name="banner" />
<tiles:insertAttribute name="login" />
</head>

<%-- check_width()--%>
<body onLoad="focusOnFirstInputField();check_width();onLoad()">

	<%--action is set in BaseAction--%>

	<!-- for optimistic locking-->
	<table cellpadding="0" cellspacing="1" width="100%">
		<tr>
			<td><tiles:insertAttribute name="error" /></td>
		</tr>
		<tr>
			<td><tiles:insertAttribute name="header" /></td>
		</tr>
		<tr>
			<td><tiles:insertAttribute name="preSelectionHeader" /></td>
		</tr>
		<tr>
			<td><tiles:insertAttribute name="body" /></td>
		</tr>
		<tr>
			<td><tiles:insertAttribute name="footer" /></td>
		</tr>
	</table>

</body>



</html>

