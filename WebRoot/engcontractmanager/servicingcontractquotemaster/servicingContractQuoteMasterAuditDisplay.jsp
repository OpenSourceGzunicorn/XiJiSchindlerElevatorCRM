<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='contractCSS'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<style type="text/css">
	.tab-on{
		cursor: hand;
     	width: 150px;
	 	text-align:center;
	 	background-color:#B8C4F4;
	  	line-height:22px; 
	  	font-weight:bold;
	  	color:#000000;
	  	background-image:url(/XJSCRM/common/images/line_bg.gif);
	}
	.tab-off {
	     background-color: #D1D9FC;
	     cursor: hand;
	     width: 150px;
		 text-align:center;
		 line-height:22px;
		 font-weight:bold;
		 color:#000000; 
		 background-image:url(/XJSCRM/common/images/line_bg_1.gif);
	}
	.td3{
	      text-align:right; 
	      background-color:#ADC6E7;
	      line-height:20px; 
	      width:100px;
	 }
</style>
<script>
	function switchCell(n) {
		var busstype=document.getElementById("BussType").value;
	   	if(busstype=="W"){
		   	for(i=0;i<wnavcell.length;i++){
		     	wnavcell[i].className="tab-off";
		     	wtb[i].style.display="none";
	     	}
	    }else if(busstype=="B"){
	    	for(i=0;i<bnavcell.length;i++){
		     	bnavcell[i].className="tab-off";
		     	btb[i].style.display="none";
	     	}
	    }else if(busstype=="G"){
	    	for(i=0;i<gnavcell.length;i++){
		     	gnavcell[i].className="tab-off";
		     	gtb[i].style.display="none";
	     	}
	    }

	   if(busstype=="W"){
	     	wnavcell[n].className="tab-on";
	   		wtb[n].style.display="";
	    }else if(busstype=="B"){
	    	bnavcell[n].className="tab-on";
	   		btb[n].style.display="";
	    }else if(busstype=="G"){
	    	gnavcell[n].className="tab-on";
	   		gtb[n].style.display="";
	    }
	   
	}
</script>

<html:errors/> 
<html:form action="/wgchangeAuditAction.do" enctype="multipart/form-data">
<html:hidden property="status" value="${ServicingContractQuoteMaster.status}"/>
<html:hidden property="taskId" styleId="taskId"/>
<html:hidden property="tokenId" styleId="tokenId"/>
<html:hidden property="taskType" styleId="taskType"/>
<html:hidden property="taskName" styleId="taskName"/>
<html:hidden property="flowname" styleId="flowName"/>
<input type="hidden" name="typejsp" id="typejsp" value="${ typejsp}"/>
<a href="" id="avf" target="_blank"></a>
<logic:equal name="typejsp" value="display">
<div id="divshoww" style="display:inline;">
	<%@ include file="Wgdisplay.jsp" %>	
</div>
 </logic:equal>	
<div id="divshowpath" style="display:none;">
<input type="hidden" name="submitpath" id="submitpath" value=""/>
</div>
<%@ include file="/workflow/processApproveMessage.jsp" %>
</html:form>



