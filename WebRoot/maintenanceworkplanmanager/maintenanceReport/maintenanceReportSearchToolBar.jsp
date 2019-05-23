  <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<!--
	客户地区表页工具栏
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()"); 
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//查询
function searchMethod(){
	var maintDivision = document.getElementById("maintDivision").value;
	var mainStation = document.getElementById("mainStation").value;
	var maintPersonnel = document.getElementById("maintPersonnel").value;
	
	if(maintDivision=="")
	{
		alert("必须选择一个维保分部！");
		return;
	}
	if(mainStation=="")
	{
		alert("必须选着一个维保站！");
		return;
	}
	if(maintPersonnel=="")
	{
		alert("必须选择一个维保工！");
		return;
	}
	
	var edate1 = document.getElementById("edate1").value;
	var sdate1 =document.getElementById("sdate1").value;
	
	
	if(sdate1!=""&&edate1!=""){	
       if(sdate1>edate1)
     	{
      	alert("开始日期必须小于结束日期！");
      	document.getElementById("edate1").value="";
	     return;
	   }
    }else
    {
    	alert("必须选择开始结束日期！");
		return;
    }
	
	maintenanceReportForm.genReport.value="";
	maintenanceReportForm.target = "_blank";//_self
	document.maintenanceReportForm.submit();

}

 function addEdate(sdata,edate)
 {
     if(sdata!=null&&sdata!=""){
	 var year=Number(sdata.substr(0, 4)); 
     var edate1=document.getElementById(edate);
     
	 edate1.value=(year+1)+"-"+sdata.substr(5);
     }
 }


//导出Excel
function excelMethod(){
	var maintDivision = document.getElementById("maintDivision").value;
	var mainStation = document.getElementById("mainStation").value;
	var maintPersonnel = document.getElementById("maintPersonnel").value;
	
	if(maintDivision=="")
	{
		alert("必须选择一个维保分部！");
		return;
	}
	if(mainStation=="")
	{
		alert("必须选着一个维保站！");
		return;
	}
	if(maintPersonnel=="")
	{
		alert("必须选择一个维保工！");
		return;
	}
	
	var edate1 = document.getElementById("edate1").value;
	var sdate1 =document.getElementById("sdate1").value;
	
	
	if(sdate1!=""&&edate1!=""){	
       if(sdate1>edate1)
     	{
      	alert("开始日期必须小于结束日期！");
      	document.getElementById("edate1").value="";
	     return;
	   }
    }else
    {
    	alert("必须选择开始结束日期！");
		return;
    }
	maintenanceReportForm.genReport.value="Y";
	maintenanceReportForm.target = "_blank";
	document.maintenanceReportForm.submit();
}

var req;
function getMainStation(obj,listname){	
	var comid=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(comid!="" && comid!="%"){
	 if(window.XMLHttpRequest) {
		 req = new XMLHttpRequest();
	 }else if(window.ActiveXObject) {
		 req = new ActiveXObject("Microsoft.XMLHTTP");
	 }  //生成response
	 var url='<html:rewrite page="/maintenanceReportAction.do"/>?method=toMainStationList&comid='+comid;//跳转路径
	 req.open("post",url,false);//post 异步
	 req.onreadystatechange=function getnextlist(){
		
			if(req.readyState==4 && req.status==200){
			 var xmlDOM=req.responseXML;
			 var rows=xmlDOM.getElementsByTagName('rows');
			 if(rows!=null){
				    if(rows.length>0)
				    	{			    	
				    	selectfreeid.options.length=0;
				    	}
			 		for(var i=0;i<rows.length;i++){
						var colNodes = rows[i].childNodes;
						if(colNodes != null){
							var colLen = colNodes.length;
							for(var j=0;j<colLen;j++){
								var freeid = colNodes[j].getAttribute("name");
								var freename = colNodes[j].getAttribute("value");
								selectfreeid.add(new Option(freeid,freename));
				            }
			             }
			 		}
			 	}
			
			}
	 };//回调方法
	 req.send(null);//不发送
	 getMaintPersonnel(selectfreeid,"maintPersonnel");
	}else{		
		selectfreeid.options.length=0;
    	selectfreeid.add(new Option("全部",""));
    	var selectfreeid2=document.getElementById("maintPersonnel");
    	selectfreeid2.options.length=0;
    	selectfreeid2.add(new Option("全部",""));
	}
}

function getMaintPersonnel(obj,listname){	
	var mainStation=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(mainStation!=""){
	 if(window.XMLHttpRequest) {
		 req = new XMLHttpRequest();
	 }else if(window.ActiveXObject) {
		 req = new ActiveXObject("Microsoft.XMLHTTP");
	 }  //生成response
	 var url='<html:rewrite page="/maintenanceReportAction.do"/>?method=toMaintPersonnelList&mainStation='+mainStation;//跳转路径
	 req.open("post",url,false);//post 异步
	 req.onreadystatechange=function getnextlist(){
		
			if(req.readyState==4 && req.status==200){
			 var xmlDOM=req.responseXML;
			 var rows=xmlDOM.getElementsByTagName('rows');
			 if(rows!=null){
				    if(rows.length>0)
				    	{			    	
				    	selectfreeid.options.length=0;
				    	}
			 		for(var i=0;i<rows.length;i++){
						var colNodes = rows[i].childNodes;
						if(colNodes != null){
							var colLen = colNodes.length;
							for(var j=0;j<colLen;j++){
								var freeid = colNodes[j].getAttribute("name");
								var freename = colNodes[j].getAttribute("value");
								selectfreeid.add(new Option(freeid,freename));
				            }
			             }
			 		}
			 	}
			
			}
	 };//回调方法
	 req.send(null);//不发送
	}else{		
		selectfreeid.options.length=0;
    	selectfreeid.add(new Option("全部",""));
	}
}

</script>
  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar" align="center">
            <script language="javascript">
              //<!--
                CreateToolBar();
              //-->
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>