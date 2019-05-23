<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/maintContractAction.do?method=toAssignTasks">
    <html:hidden property="id" />
    <%@ include file="display.jsp" %>
  </html:form>  
  <script type="text/javascript">   
    $(".assign.show").hide();
    $(".assign.hide").show(); 
    
        		
	$(":input[name=assignedMainStation]").each(function(){
		if(this.value == ""){
			this.value = "${maintContractBean.maintStation}"
		}
	})
	
    function saveMethod(){
    	var msg = "";
		$(":input[name=assignedMainStation]").each(function(i,obj){
			if(obj.value == ""){
				msg+="合同明细：第"+(i+1)+"行请选择下达维保站！";
				return false;
			}
		})  
		
		if(msg!=""){
			alert(msg);
			return;
		}
		
        document.maintContractForm.submit();      
    }

  </script>
</body>