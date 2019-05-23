<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
</head>
<body>
  <html:errors/>
  <html:form action="/maintContractAction.do?method=toRenewalRecord">    
    <html:hidden property="id" />
    <%@ include file="displayRenewal.jsp" %>    
  </html:form>  
  <script type="text/javascript">
    $(".renewal.hide").show();
    $(".renewal.show").hide();
    
    //明细表中，当日期控件第一行选择日期时，自动填上同一列为空的日期
    setTopRowDateInputsPropertychange(scrollTable);
    
    function saveMethod(){
      if(checkEmpty("contractInfoTable") && checkEmpty("scrollTable","titleRow")){
        document.maintContractForm.submit();      
      }
    }
  </script>
</body>