  <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<!--
	客户地区表页工具栏
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()"); 
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintenanceworkplanturn" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'保 存',"65","1","saveMethod()");
  </logic:equal>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
	var edate1 = document.getElementById("edate1").value;
	var sdate1 =document.getElementById("sdate1").value;
	if(sdate1==""||edate1==""){
		alert("请先选择下达起止日期！");
		return;
	}
	if(sdate1==""&&edate1!="")
	{
		alert("请先选择起始时间！");
		document.getElementById("edate1").value="";
		return;
	}
	if(sdate1!=""&&edate1!=""){	
       if(sdate1>edate1)
     	{
      	alert("开始日期必须小于结束日期！");
      	document.getElementById("edate1").value="";
	     return;
	   }
    }
	
	
	maintenanceWorkPlanTurnActionForm.target = "_self";
	document.maintenanceWorkPlanTurnActionForm.method.value="";
	document.maintenanceWorkPlanTurnActionForm.submit();
	
}

function simpleOpenWindow(actionName,id){
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url=projectName+actionName+".do?method=toDisplayRecord&isOpen=Y&id="+id;
	window.open(url,'','height=500px, width=1000px,scrollbars=yes, resizable=yes,directories=no');
}

function openWindow(actionName,id){
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url=projectName+actionName+".do?method=toMaintenanceWorkPlanDisplayRecord&isOpen=Y&id="+id;
	window.open(url,'','scrollbars=yes,resizable=yes,directories=no,fullscreen=yes');
}

function changeisBox(){
	var checkboxids=document.getElementsByName("checkboxMcds");
	var isBoxs=document.getElementsByName("isBox");
	for(var i=0;i<checkboxids.length;i++){
		
		if(checkboxids[i].checked){
	      isBoxs[i].value="Y";
		}else
		{
			isBoxs[i].value="N";
		}  
	}
}

//保存
function saveMethod(){
	var errorstr=savecheckinfo("");
	if(errorstr==""){
	document.maintenanceWorkPlanTurnActionForm.method.value="toSave";
	document.getElementById("ViewBtn").disabled=true;
	document.getElementById("SaveBtn").disabled=true;
	document.maintenanceWorkPlanTurnActionForm.submit();
	}else{
	alert(errorstr);	
	}
} 

function savecheckinfo(error){
	var checkboxids=document.getElementsByName("checkboxMcds");//需要保存的复选框
	var maintPersonnels=document.getElementsByName("zpoperid");//确认分配维保工
	var zpdates=document.getElementsByName("zpdate");//确认转派日期
	
	var boxnum=0;
	for(var i=0;i<checkboxids.length;i++){
		
		if(checkboxids[i].checked){
			
		var zpdate=zpdates[i].value;
		if(zpdate=="")
		{
		 error+=" 请输入转派日期 ！;";
		}	
		var maintPersonnel=maintPersonnels[i].value;
		 if(maintPersonnel=="")
		{
		  error+=" 请分配维保工！";
		}
							
		 boxnum++;
		if(error!=""){
			error="序号"+(i+1)+"："+error+"\n";
			break;
		} 
		}
	}
	
	if(boxnum==0){
		error="必须选择一条记录进行保存！";
	}
	return error;
}

/* function rowstyle()
{
	var table=document.getElementById("dynamictable_0");
	table.style.display="none";
	var tr=table.rows;
	var len=tr.length>15?15:tr.length;
	
	for(var i=1;i<tr.length;i++)
	{
		
		if(i%2==0)
		{
			tr[i].className="oddRow3";
		}else
		{
			tr[i].className="evenRow3";
		}
		
	}
	table.style.display="";
	
}
 */
 function getmaintPersonnel(selectObj,station)
 {
	 //alert(selectObj.childNodes.length)
 	if(selectObj.childNodes.length<=2)
 	{
 		
 	
 	 var pathName = window.document.location.pathname;
 	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);
 	var url = projectName+"/maintenanceWorkPlanTurnAction.do?method=getMaintPersonnel" + 
 			"&mainStation="+station;

 	 var obj = $.ajax({
 			url: url,
 			async:false				
 		}); 
 	var maintPersonnels = eval(obj.responseText);
 	
 	for(var i=0;i<maintPersonnels.length;i++)
 	{
 		var opt = document.createElement("option");
 		opt.value = maintPersonnels[i].id;
 		opt.innerHTML = maintPersonnels[i].name;
 		selectObj.appendChild(opt);
 		//this.innerHTML+="<option value='"+maintPersonnels[i].id+"'>"+maintPersonnels[i].name+"</option>";	
 	}
 	}
    //alert(this.innerHTML);
 }

 function assignedSignFlagChange(value,index){
	 var checkboxids=document.getElementsByName("checkboxMcds");//需要保存的复选框
	 var inAssignedSignFlags=document.getElementsByName("inAssignedSignFlag");//是否签收/退回
	 var assignedSignFlags=document.getElementsByName("assignedSignFlag");//签收/退回标志 
	 if(index==1||index=="1"){
	 for(var i=0;i<checkboxids.length;i++){
				var inAssignedSignFlag=inAssignedSignFlags[i].value;
				if(inAssignedSignFlag==""||inAssignedSignFlag==null){	
					assignedSignFlags[i].value=value;
				}
		}
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