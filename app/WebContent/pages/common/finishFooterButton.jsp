<%@ page language="java" contentType="text/html; charset=utf-8"
	import="us.mn.state.health.lims.common.action.IActionConstants"%>
<%@ page isELIgnored="false"%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


	<table border="0" cellpadding="0" cellspacing="4" width="100%">
		<tbody valign="middle">
			<tr>
				<td align="middle">
				<spring:message code="footer.button.finish" var="finishButton"></spring:message>
				<input type="button" onclick="finish();" id="finishButtonId" value="${finishButton}" />
				</td>
			</tr>
		</tbody>
	</table>

<script type="text/javascript">

<%if( request.getAttribute(IActionConstants.FWD_SUCCESS) != null &&
      ((Boolean)request.getAttribute(IActionConstants.FWD_SUCCESS)) ) { %>
if( typeof(showSuccessMessage) != 'undefined' ){
	showSuccessMessage( true );
}
<% } %>

</script>