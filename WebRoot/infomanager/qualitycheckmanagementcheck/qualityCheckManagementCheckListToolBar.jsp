  <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>


<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nqualityCheckManagementCheck" value="Y"> 
    AddToolBarItemEx("ExcelBtn", "../../common/images/toolbar/print.gif", "", "",'打印检查单', "90", "1", "printMethod()");
  </logic:equal>
  
  //AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
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
				window.location = '<html:rewrite page="/qualityCheckManagementCheckAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDisplayRecord';
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/qualityCheckManagementCheckAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDisplayRecord';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
}


//打印
function printMethod(){
	var index = checkedIndex();
	if(index >= 0){	
	 	var processStatus=document.getElementsByName("processStatus")[index].value;//提交状态
		if(processStatus == "2"||processStatus == "3"){ 
		window.open('<html:rewrite page="/qualityCheckManagementCheckAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method=toPreparePrintRecord');

		} 
	 	else{
			alert("处理状态为已录入报告书后的,才可以打印"); 
			return;
		}
		
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
	}
}


//提交
function saveSubmitMethod(){
	if(serveTableForm.ids)
	{
		//alert(document.serveTableForm.ids);
		var l = document.serveTableForm.ids.length;
		if(l)
		{
			for(i=0;i<l;i++)
			{
				if(document.serveTableForm.ids[i].checked == true)
				{
					var issubmit=document.serveTableForm.submitType[i].value;
					if(issubmit=="N"){
						//alert(document.serveTableForm.ids[i].value);
						if(confirm("是否确定提交数据："+document.serveTableForm.ids[i].value+"？提交后将不能修改及删除该数据！"))
						{
							window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toSubmitRecord';
						}
						return;
					}else{
						alert("该维保质量检查项已提交，不能再次提交！");
						
					}
					return;
				}
			}
			if(l >0)
			{
				alert("<bean:message key="javascript.role.alert3"/>");
			}
		}
		else if(document.serveTableForm.ids.checked == true)
		{
			var issubmit=document.serveTableForm.submitType.value;
			if(issubmit=="N"){
			if(confirm("是否确定提交数据："+document.serveTableForm.ids.value+"？提交后将不能修改及删除该数据！"))
				{
					window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toSubmitRecord';
				}
			}else{
				alert("该维保质量检查项已提交，不能再次提交！");
			}
		}
		else
		{
			alert("<bean:message key="javascript.role.alert3"/>");
		}
	}
}

function checkedIndex(){
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

//AJAX动态显示维保站
var req;
function Evenmore(obj,listname){	
	var comid=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(comid!="" && comid!="%"){
		
		 if(window.XMLHttpRequest) {
			 req = new XMLHttpRequest();
		 }else if(window.ActiveXObject) {
			 req = new ActiveXObject("Microsoft.XMLHTTP");
		 }  //生成response
		 
		 var url='<html:rewrite page="/qualityCheckManagementAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
		 req.open("post",url,true);//post 异步
		 req.onreadystatechange=function getnextlist(){
			
				if(req.readyState==4 && req.status==200){
				 var xmlDOM=req.responseXML;
				 var rows=xmlDOM.getElementsByTagName('rows');
				 if(rows!=null){
					    selectfreeid.options.length=0;
					    selectfreeid.add(new Option("全部","%"));	

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
    	selectfreeid.add(new Option("全部","%"));
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