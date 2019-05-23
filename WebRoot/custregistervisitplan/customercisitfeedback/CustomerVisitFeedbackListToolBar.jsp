  <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<!--
	客户地区表页工具栏
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncustomercisitfeedback" value="Y"> 
  AddToolBarItemEx("UpdateBtn","../../common/images/toolbar/edit.gif","","","转 派","65","1","updateMethod()"); 
    AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","","拜访反馈","80","1","addMethod()"); 
    AddToolBarItemEx("ReferBtn","../../common/images/toolbar/digital_confirm.gif","","","提 交","65","1","referMethod()"); 
  </logic:equal>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//查询
function searchMethod(){
	var edate1 = document.getElementById("edate1").value;
	var sdate1 =document.getElementById("sdate1").value;
	if(sdate1!=""&&edate1!=""){	
       if(sdate1>edate1)
     	{
      	alert("开始日期必须小于结束日期！");
      	document.getElementById("edate1").value="";
	     return;
	   }
    
}
	serveTableForm.genReport.value = "";
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}

//查看
function viewMethod(){
	if(serveTableForm.ids)
	{
		var l = document.serveTableForm.ids.length;
		if(l)
		{
			for(i=0;i<l;i++)
			{
				if(document.serveTableForm.ids[i].checked == true)
				{
					window.location = '<html:rewrite page="/customerVisitFeedbackAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDisplayRecord';
					return;
				}
			}
			if(l>0)
			{
				alert("<bean:message key="customerVisitFeedback.role.alert"/>");
			}
		}else if(document.serveTableForm.ids.checked == true)
		{
			window.location = '<html:rewrite page="/customerVisitFeedbackAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDisplayRecord';
		}
		else
		{
			alert("<bean:message key="customerVisitFeedback.role.alert"/>");
		}
	 }

	}



//反馈
function addMethod(){
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{
				if(document.serveTableForm.submitType[i].value=="是")
				{
					alert("该记录已提交，不能重复反馈");
				}else
				{
					window.location = '<html:rewrite page="/customerVisitFeedbackAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareAddRecord';
				}
				return;
			}
		}
		if(l>0)
		{
			alert("<bean:message key="customerVisitFeedback.role.alert"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		if(document.serveTableForm.submitType.value=="是")
		{
			alert("该记录已提交，不能重复反馈");
		}else
		{
			window.location = '<html:rewrite page="/customerVisitFeedbackAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareAddRecord';
		}
	}
	else
	{
		alert("<bean:message key="customerVisitFeedback.role.alert"/>");
	}
 }

}

var req;
function Evenmore(obj,listname,td){	
	var comid=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(comid!="" && comid!="%"){
	 if(window.XMLHttpRequest) {
		 req = new XMLHttpRequest();
	 }else if(window.ActiveXObject) {
		 req = new ActiveXObject("Microsoft.XMLHTTP");
	 }  //生成response
	 var url='<html:rewrite page="/customerVisitFeedbackAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
	 req.open("post",url,true);//post 异步
	 req.onreadystatechange=function getnextlist(){
		
			if(req.readyState==4 && req.status==200){
			 var xmlDOM=req.responseXML;
			 var rows=xmlDOM.getElementsByTagName('rows');
			 if(rows!=null){
				    if(rows.length>0)
				    	{			    	
				    	selectfreeid.options.length=0;
				    	selectfreeid.add(new Option("请选择",""));
				    	}
				    else
				    	{
				    	selectfreeid.options.length=0;
				    	selectfreeid.add(new Option("全部",""));	
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
			document.getElementById(td).style.display="";
	 };//回调方法
	 req.send(null);//不发送
	}else{		
		selectfreeid.options.length=0;
    	selectfreeid.add(new Option("请选择",""));
	}
}

function updateMethod(){
	var index = getIndex();	
	if(index >= 0){	
    var isVisit=getVal("isVisit",index)
	if(isVisit=="是"){
		alert("该记录已反馈，不能转派!");
		return;
	}
    var visitPosition=getVal("visitPosition",index);
    if(visitPosition=="维保站长"){
		alert("维保站长,不能转派!");
		return;
	}
    if(visitPosition=="服务销售专员"){
		alert("服务销售专员,不能转派!");
		return;
	}
	toDoMethod(index,"toPrepareUpdateRecord","");
	}else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
}

function referMethod(){
	var index = getIndex();	
	if(index >= 0){	
	var submitType=getVal("submitType",index)
	if(submitType=="是"){
		alert("该记录已提交，不能重复提交!");
		return;
	}
	toDoMethod(index,"toSubmitRecord","");
	}else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
}



//跳转方法
function toDoMethod(index,method,params){
		window.location = '<html:rewrite page="/customerVisitFeedbackAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;
}

//获取选中记录下标
function getIndex(){
	if(document.getElementsByName("ids").length){	
		var ids = document.getElementsByName("ids");
		for(var i=0;i<ids.length;i++){
		  if(ids[i].checked == true){
		    return i;
		  }
		}				
	}
	return -1;	
}

//根据name和选中下标获取元素的值
function getVal(name,index){
	var obj = eval("serveTableForm."+name);
	if(obj && obj.length){
		obj = obj[index];
	}
	return obj ? obj.value : null;
}

</script>
  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar" align="center">
            <script language="javascript">
                CreateToolBar();
            </script>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>