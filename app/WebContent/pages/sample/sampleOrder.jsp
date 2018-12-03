<%@ page language="java" contentType="text/html; charset=utf-8"
         import="us.mn.state.health.lims.common.formfields.FormFields.Field,
                 us.mn.state.health.lims.common.provider.validation.AccessionNumberValidatorFactory,
                 us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator,
                 us.mn.state.health.lims.common.services.PhoneNumberService,
                 us.mn.state.health.lims.common.util.ConfigurationProperties,
                 us.mn.state.health.lims.common.util.ConfigurationProperties.Property,
                 us.mn.state.health.lims.common.util.StringUtil,
                 us.mn.state.health.lims.common.util.IdValuePair,
                 us.mn.state.health.lims.common.util.Versioning,
                 us.mn.state.health.lims.common.util.DateUtil,
                 us.mn.state.health.lims.common.action.IActionConstants,
                 us.mn.state.health.lims.common.formfields.FormFields,
                 us.mn.state.health.lims.common.services.LocalizationService" %>
<%@ page isELIgnored="false"%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="formName" value="${form.formName}" />


<%!
    String path = "";
    String basePath = "";
    boolean useCollectionDate = true;
    boolean useInitialSampleCondition = false;
    boolean useCollector = false;
    boolean autofillCollectionDate = true;
    boolean useReferralSiteList = false;
    boolean useReferralSiteCode = false;
    boolean useProviderInfo = false;
    boolean patientRequired = false;
    boolean trackPayment = false;
    boolean requesterLastNameRequired = false;
    boolean acceptExternalOrders = false;
    boolean restrictNewReferringSiteEntries = false;
    IAccessionNumberValidator accessionNumberValidator;
%>
<%
    path = request.getContextPath();
    basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    useCollectionDate = FormFields.getInstance().useField( Field.CollectionDate );
    useInitialSampleCondition = FormFields.getInstance().useField( Field.InitialSampleCondition );
    useCollector = FormFields.getInstance().useField( Field.SampleEntrySampleCollector );
    autofillCollectionDate = ConfigurationProperties.getInstance().isPropertyValueEqual( Property.AUTOFILL_COLLECTION_DATE, "true" );
    useReferralSiteList = FormFields.getInstance().useField( FormFields.Field.RequesterSiteList );
    useReferralSiteCode = FormFields.getInstance().useField( FormFields.Field.SampleEntryReferralSiteCode );
    useProviderInfo = FormFields.getInstance().useField( FormFields.Field.ProviderInfo );
    patientRequired = FormFields.getInstance().useField( FormFields.Field.PatientRequired );
    trackPayment = ConfigurationProperties.getInstance().isPropertyValueEqual( Property.TRACK_PATIENT_PAYMENT, "true" );
    accessionNumberValidator = new AccessionNumberValidatorFactory().getValidator();
    requesterLastNameRequired = FormFields.getInstance().useField( Field.SampleEntryRequesterLastNameRequired );
    acceptExternalOrders = ConfigurationProperties.getInstance().isPropertyValueEqual( Property.ACCEPT_EXTERNAL_ORDERS, "true" );
    restrictNewReferringSiteEntries = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.restrictFreeTextRefSiteEntry, "true");

%>

<script type="text/javascript" src="<%=basePath%>scripts/utilities.jsp"></script>
<script type="text/javascript" src="scripts/jquery.asmselect.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/ajaxCalls.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/laborder.js?ver=<%= Versioning.getBuildNumber() %>"></script>



<link rel="stylesheet" type="text/css" href="css/jquery.asmselect.css?ver=<%= Versioning.getBuildNumber() %>"/>


<script type="text/javascript">
    var useReferralSiteList = <%= useReferralSiteList%>;
    var useReferralSiteCode = <%= useReferralSiteCode %>;

    function checkAccessionNumber(accessionNumber) {
        //check if empty
        if (!fieldIsEmptyById("labNo")) {
            validateAccessionNumberOnServer(false, false, accessionNumber.id, accessionNumber.value, processAccessionSuccess, null);
        }
        else {
             selectFieldErrorDisplay(false, $("labNo"));
        }

        setCorrectSave();
    }

    function processAccessionSuccess(xhr) {
        //alert(xhr.responseText);
        var formField = xhr.responseXML.getElementsByTagName("formfield").item(0);
        var message = xhr.responseXML.getElementsByTagName("message").item(0);
        var success = false;

        if (message.firstChild.nodeValue == "valid") {
            success = true;
        }
        var labElement = formField.firstChild.nodeValue;
        selectFieldErrorDisplay(success, $(labElement));

        if (!success) {
            alert(message.firstChild.nodeValue);
        }

        setCorrectSave();
    }

    function setCorrectSave(){
        if( window.setSave){
            setSave();
        }else if(window.setSaveButton){
            setSaveButton();
        }
    }

    function getNextAccessionNumber() {
        generateNextScanNumber(processScanSuccess);
    }

    function processScanSuccess(xhr) {
        //alert(xhr.responseText);
        var formField = xhr.responseXML.getElementsByTagName("formfield").item(0);
        var returnedData = formField.firstChild.nodeValue;

        var message = xhr.responseXML.getElementsByTagName("message").item(0);

        var success = message.firstChild.nodeValue == "valid";

        if (success) {
            $("labNo").value = returnedData;

        } else {
            alert("<%= StringUtil.getMessageForKey("error.accession.no.next") %>");
            $("labNo").value = "";
        }

        selectFieldErrorDisplay(success, $("labNo"));
        setValidIndicaterOnField(success, "labNo");

        setCorrectSave();
    }

    function siteListChanged(textValue) {
        var siteList = $("requesterId");

        //if the index is 0 it is a new entry, if it is not then the textValue may include the index value
        if (siteList.selectedIndex == 0 || siteList.options[siteList.selectedIndex].label != textValue) {
            $("newRequesterName").value = textValue;
        } else if (useReferralSiteCode) {
            getCodeForOrganization(siteList.options[siteList.selectedIndex].value, processCodeSuccess);
        }
    }

    function processCodeSuccess(xhr) {
        //alert(xhr.responseText);
        var code = xhr.responseXML.getElementsByTagName("code").item(0);
        var success = xhr.responseXML.getElementsByTagName("message").item(0).firstChild.nodeValue == "valid";

        if (success) {
            $jq("#requesterCodeId").val(code.getAttribute("value"));
        }
    }

    function testLocationCodeChanged(element) {
        if (element.length - 1 == element.selectedIndex) {
            $("testLocationCodeOtherId").show();
        } else {
            $("testLocationCodeOtherId").hide();
            $("testLocationCodeOtherId").value = "";
        }
    }

    function setOrderModified(){
        $jq("#orderModified").val("true");
        orderChanged = true;
        if( window.makeDirty ){ makeDirty(); }

        setCorrectSave();
    }

</script>


<!-- This define may not be needed, look at usages (not in any other jsp or js page may be radio buttons for ci LNSP-->
<c:set var="sampleOrderItem" value="${sampleOrderItems}"/>

<form:hidden path="sampleOrderItems.newRequesterName" id="newRequesterId" />
<form:hidden path="sampleOrderItems.modified" id="orderModified"/>

<div id=orderDisplay <%= acceptExternalOrders? "style='display:none'" : ""  %> >
<table style="width:100%">

<tr>
<td>
<table>
<c:if test="${empty sampleOrderItems.labNo}" >
    <tr>
        <td style="width:35%">
            <%=StringUtil.getContextualMessageForKey( "quick.entry.accession.number" )%>
            :
            <span class="requiredlabel">*</span>
        </td>
        <td style="width:65%">
            <form:input path="sampleOrderItems.labNo"
                      maxlength='<%= Integer.toString(accessionNumberValidator.getMaxAccessionLength())%>'
                      onchange="checkAccessionNumber(this);"
                      cssClass="text"
                      id="labNo"/>

            <spring:message code="sample.entry.scanner.instructions"/>
            <input type="button" value='<%=StringUtil.getMessageForKey("sample.entry.scanner.generate")%>'
                   onclick="setOrderModified();getNextAccessionNumber(); " class="textButton">
        </td>
    </tr>
</c:if>
<%-- <logic:notEmpty name="${formName}" property="sampleOrderItems.labNo" >
    <tr><td style="width:35%"></td><td style="width:65%"></td></tr>
</logic:notEmpty> --%>
<c:if test="${!empty form.sampleOrderItems.labNo }" >
    <tr><td style="width:35%"></td><td style="width:65%"></td></tr>
</c:if>
<% if( FormFields.getInstance().useField( Field.SampleEntryUseRequestDate ) ){ %>
<tr>
    <td><spring:message code="sample.entry.requestDate"/>:
        <span class="requiredlabel">*</span><span
                style="font-size: xx-small; "><%=DateUtil.getDateUserPrompt()%></span></td>
    <td><form:input path="sampleOrderItems.requestDate"
                   id="requestDate"
                   cssClass="required"
                   onchange="setOrderModified();checkValidEntryDate(this, 'past')"
                   onkeyup="addDateSlashes(this, event);"
                   maxlength="10"/>
</tr>
<% } %>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "quick.entry.received.date" ) %>
        :
        <span class="requiredlabel">*</span>
        <span style="font-size: xx-small; "><%=DateUtil.getDateUserPrompt()%>
        </span>
    </td>
    <td colspan="2">
         <form:input path="sampleOrderItems.receivedDateForDisplay"
                  onchange="setOrderModified();checkValidEntryDate(this, 'past');"
                  onkeyup="addDateSlashes(this, event);"
                  maxlength="10"
                  cssClass="text required"
                  id="receivedDateForDisplay"/> 

        <% if( FormFields.getInstance().useField( Field.SampleEntryUseReceptionHour ) ){ %>
        <spring:message code="sample.receptionTime"/>:
        <form:input                    onkeyup="filterTimeKeys(this, event);"
                   path="sampleOrderItems.receivedTime"
                   id="receivedTime"
                   maxlength="5"
                   onblur="setOrderModified(); checkValidTime(this, true);"/>

        <% } %>
    </td>
</tr>

<% if( FormFields.getInstance().useField( Field.SampleEntryNextVisitDate ) ){ %>
<tr>
    <td><spring:message code="sample.entry.nextVisit.date"/>&nbsp;<span style="font-size: xx-small; ">
        <%=DateUtil.getDateUserPrompt()%>
    </span>:
    </td>
    <td>
        <form:input path="sampleOrderItems.nextVisitDate"
                   onchange="setOrderModified();checkValidEntryDate(this, 'future', true)"
                   onkeyup="addDateSlashes(this, event);"
                   id="nextVisitDate"
                   maxlength="10"/>
    </td>
</tr>
<% } %>

<tr class="spacerRow">
    <td>&nbsp;</td>
</tr>
<% if( FormFields.getInstance().useField( Field.SampleEntryRequestingSiteSampleId ) ){%>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "sample.clientReference" ) %>:
    </td>
    <td>
         <form:input path="sampleOrderItems.requesterSampleID"
                  size="50"
                  maxlength="50"
                  onchange="setOrderModified();"/> 
    </td>
    <td style="width:10%">&nbsp;</td>
    <td style="width:45%">&nbsp;</td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField( Field.SAMPLE_ENTRY_USE_REFFERING_PATIENT_NUMBER ) ){%>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "sample.referring.patientNumber" ) %>:
    </td>
    <td>
        <form:input path="sampleOrderItems.referringPatientNumber"
                  id="referringPatientNumber"
                  size="50"
                  maxlength="50"
                  onchange="setOrderModified();"/> 
    </td>
    <td style="width:10%">&nbsp;</td>
    <td style="width:45%">&nbsp;</td>
</tr>
<% } %>
<% if( useReferralSiteList ){ %>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "sample.entry.project.siteName" ) %>:
        <% if( FormFields.getInstance().useField( Field.SampleEntryReferralSiteNameRequired ) ){%>
        <span class="requiredlabel">*</span>
        <% } %>
    </td>
    <td colspan="3" >
    	<c:if test="${form.sampleOrderItems.readOnly == false}" >
    		<form:select path="sampleOrderItems.referringSiteId" 
    				 id="requesterId" 
                     onchange="setOrderModified();siteListChanged(this);setCorrectSave();"
                     onkeyup="capitalizeValue( this.value );" >
            <option value=""></option>
            <c:forEach items="${form.sampleOrderItems.referringSiteList}" var="referringSite" >
           		<option value="${referringSite.id}">${referringSite.value}</option>
            </c:forEach>
            </form:select>
    	</c:if>
    	<c:if test="${form.sampleOrderItems.readOnly}" >
            <form:input path="sampleOrderItems.referringSiteName"  style="width:300px" />
    	</c:if>
        <%-- <logic:equal value="false" name='${formName}' property="sampleOrderItems.readOnly" >
        <html:select styleId="requesterId"
                     name="${formName}"
                     property="sampleOrderItems.referringSiteId"
                     onchange="setOrderModified();siteListChanged(this);setCorrectSave();"
                     onkeyup="capitalizeValue( this.value );"
                     
                >
            <option value=""></option>
            <html:optionsCollection name="${formName}" property="sampleOrderItems.referringSiteList" label="value"
                                    value="id"/>
        </html:select>
        </logic:equal>
        <logic:equal value="true" name='${formName}' property="sampleOrderItems.readOnly" >
            <html:text property="sampleOrderItems.referringSiteName" name="${formName}" style="width:300px" />
        </logic:equal> --%>
    </td>
</tr>
<% } %>
<% if( useReferralSiteCode ){ %>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "sample.entry.referringSite.code" ) %>:
    </td>
    <td>
        <form:input path="sampleOrderItems.referringSiteCode"
                   onchange="setOrderModified();setCorrectSave();"
                   id="requesterCodeId"/>
    </td>
</tr>
<% } %>
<% if( ConfigurationProperties.getInstance().isPropertyValueEqual( Property.ORDER_PROGRAM, "true" )){ %>
<tr class="spacerRow">
    <td>&nbsp;</td>
</tr>
<tr>
    <td><spring:message code="label.program"/>:</td>
    <td>
    	<form:select path="sampleOrderItems.program" onchange="setOrderModified();" >
    		<form:options items="${form.sampleOrderItems.programList}" itemValue="id" itemLabel="value" />
    	</form:select>
        <%-- <html:select name="${formName}" property="sampleOrderItems.program" onchange="setOrderModified();" >
            <logic:iterate id="optionValue" name='${formName}' property="sampleOrderItems.programList"
                           type="IdValuePair">
                <option value='<%=optionValue.getId()%>' <%=optionValue.getId().equals(sampleOrderItem.getProgram() ) ? "selected='selected'" : ""%>>
                    <bean:write name="optionValue" property="value"/>
                </option>
            </logic:iterate>
        </html:select> --%>
    </td>
</tr>
<% } %>
<tr class="spacerRow">
    <td>&nbsp;</td>
</tr>
<% if( useProviderInfo ){ %>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "sample.entry.provider.name" ) %>:
        <% if( requesterLastNameRequired ){ %>
        <span class="requiredlabel">*</span>
        <% } %>
    </td>
    <td>
        <form:input path="sampleOrderItems.providerLastName"
                   id="providerLastNameID"
                   onchange="setOrderModified();setCorrectSave();"
                   size="30"/>
        <spring:message code="humansampleone.provider.firstName.short"/>:
        <form:input path="${form.sampleOrderItems.providerFirstName}" onchange="setOrderModified();"
                   size="30"/>

    </td>
</tr>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "humansampleone.provider.workPhone" ) + ": " + PhoneNumberService.getPhoneFormat()%>
    </td>
    <td>
         <form:input path="sampleOrderItems.providerWorkPhone"
                  id="providerWorkPhoneID"
                  size="30"
                  maxlength="30"
                  cssClass="text"
                  onchange="setOrderModified();validatePhoneNumber(this)"/> 
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField( Field.SampleEntryProviderFax ) ){ %>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "sample.entry.project.faxNumber" )%>:
    </td>
    <td>
        <form:input path="sampleOrderItems.providerFax"
                  id="providerFaxID"
                  size="20"
                  cssClass="text"
                  onchange="setOrderModified();makeDirty()"/> 
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField( Field.SampleEntryProviderEmail ) ){ %>
<tr>
    <td>
        <%= StringUtil.getContextualMessageForKey( "sample.entry.project.email" )%>:
    </td>
    <td> 
        <form:input path="sampleOrderItems.providerEmail"
                  id="providerEmailID"
                  size="20"
                  cssClass="text"
                  onchange="setOrderModified();makeDirty()"/> 
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField( Field.SampleEntryHealthFacilityAddress ) ){%>
<tr>
    <td><spring:message code="sample.entry.facility.address"/>:</td>
</tr>
<tr>
    <td>&nbsp;&nbsp;<spring:message code="sample.entry.facility.street"/>
    <td>
        <form:input 
                   path="sampleOrderItems.facilityAddressStreet"
                   cssClass="text"
                   onchange="setOrderModified();makeDirty()"/>
    </td>
</tr>
<tr>
    <td>&nbsp;&nbsp;<spring:message code="sample.entry.facility.commune"/>:
    <td>
        <form:input
                   path="sampleOrderItems.facilityAddressCommune"
                   cssClass="text"
                   onchange="setOrderModified();makeDirty()"/>
    </td>
</tr>
<tr>
    <td><spring:message code="sample.entry.facility.phone"/>:
    <td>
        <form:input               path="sampleOrderItems.facilityPhone"
                   cssClass="text"
                   maxlength="17"
                   onchange="setOrderModified(); validatePhoneNumber( this );"/>
    </td>
</tr>
<tr>
    <td><spring:message code="sample.entry.facility.fax"/>:
    <td>
        <form:input 
                   path="sampleOrderItems.facilityFax"
                   cssClass="text"
                   onchange="setOrderModified();makeDirty()"/>
    </td>
</tr>
<% } %>
<tr class="spacerRow">
    <td>&nbsp;</td>
</tr>
<% if( trackPayment ){ %>
<tr>
    <td><spring:message code="sample.entry.patientPayment"/>:</td>
    <td>
        <form:select path="sampleOrderItems.paymentOptionSelection" onchange="setOrderModified();" >
            <option value=''></option>
            <c:forEach var="optionValue" items="${form.sampleOrderItems.paymentOptions}">
                <option value='${optionValue.id}'<%--  <%=optionValue.getId().equals(sampleOrderItem.getPaymentOptionSelection() ) ? "selected='selected'" : ""%> --%>>
                    ${optionValue.value}
                </option>
            </c:forEach>
        </form:select>
    </td>
</tr>
<% } %>
<tr>
<% if( ConfigurationProperties.getInstance().isPropertyValueEqual( Property.USE_BILLING_REFERENCE_NUMBER, "true" )){ %>
    <td><label for="billingReferenceNumber">
        <%= LocalizationService.getLocalizedValueById( ConfigurationProperties.getInstance().getPropertyValue( Property.BILLING_REFERENCE_NUMBER_LABEL ))%>
    </label>
    </td>
    <td>
        <form:input path="sampleOrderItems.billingReferenceNumber"
                    cssClass="text"
                    id="billingReferenceNumber"
                    onchange="setOrderModified();makeDirty()" />
    </td>
</tr>
<% } %>
<% if( FormFields.getInstance().useField( Field.TEST_LOCATION_CODE ) ){%>
<tr>
    <td><spring:message code="sample.entry.sample.period"/>:</td>
    <td>
        <form:select
                     path="sampleOrderItems.testLocationCode"
                     onchange="setOrderModified(); testLocationCodeChanged( this )"
                     id="testLocationCodeId">
            <option value=''></option>
            <c:forEach var="optionValue" items="${form.sampleOrderItems.testLocationCodeList}">                           
                <option value='${optionValue.id}' <%-- <%=optionValue.getId().equals(sampleOrderItem.getTestLocationCode() ) ? "selected='selected'" : ""%> --%> >
                    ${optionValue.value}
                </option>
            </c:forEach>
        </form:select>
        &nbsp;
        <form:input
                   path="sampleOrderItems.otherLocationCode"
                   id="testLocationCodeOtherId"
                   style='display:none'
                    />
    </td>
</tr>
<% } %>
<tr class="spacerRow">
    <td>
        &nbsp;
    </td>
</tr>
</table>
</td>
</tr>
</table>
</div>

<script type="text/javascript">

    <% if( FormFields.getInstance().useField( Field.TEST_LOCATION_CODE ) ){%>
    function showTestLocationCode(){
            if(( $jq("#testLocationCodeId option").length -1 ) == $jq("#testLocationCodeId option:selected").index() ){
                $jq("#testLocationCodeOtherId").show();
            }
    }
    <% } %>

    $jq(document).ready(function () {
        var dropdown = $jq("select#requesterId");
        autoCompleteWidth = dropdown.width() + 66 + 'px';
        <% if(restrictNewReferringSiteEntries) { %>
       			clearNonMatching = true;
        <% } else {%>
        		clearNonMatching = false;
        <% } %>
        capitialize = true;
        // Actually executes autocomplete
        dropdown.combobox();
        invalidLabID = '<spring:message code="error.site.invalid"/>'; // Alert if value is typed that's not on list. FIX - add bad message icon
        maxRepMsg = '<spring:message code="sample.entry.project.siteMaxMsg"/>';

        resultCallBack = function (textValue) {
            siteListChanged(textValue);
            setOrderModified();
            setCorrectSave();
        };

        <% if( FormFields.getInstance().useField( Field.TEST_LOCATION_CODE ) ){%>
            showTestLocationCode();
        <% } %>
    });

</script>

