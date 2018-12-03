<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="us.mn.state.health.lims.common.action.IActionConstants,
			us.mn.state.health.lims.common.util.StringUtil" %>
<%@ page isELIgnored="false" %> 

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script language="JavaScript1.2">
    function validateForm(form) {
        return validateLoginForm(form);
    }

function submitOnEnter(e){

 if (enterKeyPressed(e)) {
   var button = document.getElementById("submitButton");
   e.returnValue=false;
   e.cancel = true;
   button.click();
 }

}


function submitOnClick(button){
	window.document.forms[0].submit();
}

</script>
<form:form name='${form.formName}' action='${form.formAction}' modelAttribute="form" onSubmit="return submitForm(this);" method="POST">

<form:hidden path="formName" id="formName" />
<form:hidden path="formAction" id="formAction" />
<form:hidden path="lastupdated" id="lastUpdated" />
<table width="100%">
<tr>
    <td width="50%" valign="top">
        <table width="95%">
        <tr>
            <td width="20%">&nbsp;</td>
            <td colspan="2">
                <spring:message code="login.notice.message" />
            </td>
            <td width="20%">&nbsp;</td>
        </tr>
        <tr>
            <td width="20%">&nbsp;<br/><br/></td>
            <td colspan="2">
                <%= StringUtil.getContextualMessageForKey("login.notice.notification") %>
            </td>
            <td width="20%">&nbsp;</td>
        </tr>
        </table>
        <br>
        <table width="95%">
        <tr>
            <td width="20%">&nbsp;</td>
            <td width="10%" noWrap><spring:message code="login.msg.userName"/>:</td>
            <td colspan="2" align="left">
                <%--bugzilla 2173, 2376--%>
                <form:input path="loginName" id="loginName" onkeypress="submitOnEnter(event)"/>
            </td>
        </tr>
        <tr>
            <td width="20%">&nbsp;</td>
            <td width="10%" noWrap><spring:message code="login.msg.password"/>:</td>
            <td colspan="2" align="left">
                
                <form:password path="password" id="password" onkeypress="submitOnEnter(event)"/>
            </td>
        </tr>
        </table>
    </td>
</tr>
</table>
</form:form>



