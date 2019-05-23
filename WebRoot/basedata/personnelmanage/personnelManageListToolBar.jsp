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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="npersonnelmanage" value="Y"> 
    AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'<bean:message key="toolbar.add"/>',"65","1","addMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modiyMethod()");
    AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod()");
    AddToolBarItemEx("ImportBtn","../../common/images/toolbar/dl_log.gif","","","员工档案导入","110","1","importMethod('U')");
  AddToolBarItemEx("ImportBtn","../../common/images/toolbar/dl_log.gif","","","培训档案导入","110","1","importMethod('P')");
  AddToolBarItemEx("ImportBtn","../../common/images/toolbar/dl_log.gif","","","证书档案导入","110","1","importMethod('Z')");
  AddToolBarItemEx("ImportBtn","../../common/images/toolbar/dl_log.gif","","","工具领取档案导入","125","1","importMethod('T')");
  </logic:equal>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
function CreateToolBar2(){
  RemoveAllItem();
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'员工档案导出',"110","0","excelMethod('1')");
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'培训档案导出',"110","1","excelMethod('2')");
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'证书档案导出',"110","1","excelMethod('3')");
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'工具领取档案导出',"125","1","excelMethod('4')");
  window.document.getElementById("toolbar2").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//导入excel
function importMethod(flag){
	window.location = '<html:rewrite page="/personnelManageAction.do"/>?method=toPrepareImportRecord&flag='+flag;
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
        window.location = '<html:rewrite page="/personnelManageAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDisplayRecord';
        return;
      }
    }
    if(l >0)
    {
      alert("<bean:message key="javascript.role.alert2"/>");
    }
  }else if(document.serveTableForm.ids.checked == true)
  {
    window.location = '<html:rewrite page="/personnelManageAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDisplayRecord';
  }
  else
  {
    alert("<bean:message key="javascript.role.alert2"/>");
  }
}
}

//增加
function addMethod(){
window.location = '<html:rewrite page="/personnelManageAction.do"/>?method=toPrepareAddRecord';
}
//修改
function modiyMethod(){
if(serveTableForm.ids)
{
  var l = document.serveTableForm.ids.length;
  if(l)
  {
    for(i=0;i<l;i++)
    {
      if(document.serveTableForm.ids[i].checked == true)
      {
      //alert(document.serveTableForm.ids[i].value);
        window.location = '<html:rewrite page="/personnelManageAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareUpdateRecord';
        return;
      }
    }
    if(l >0)
    {
      alert("<bean:message key="javascript.role.alert1"/>");
    }
  }else if(document.serveTableForm.ids.checked == true)
  {
    window.location = '<html:rewrite page="/personnelManageAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareUpdateRecord';
  }
  else
  {
    alert("<bean:message key="javascript.role.alert1"/>");
  }
}

}

//删除
function deleteMethod(){
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
        if(confirm("<bean:message key="javascript.role.deletecomfirm"/>"+document.serveTableForm.ids[i].value))
        {
          window.location = '<html:rewrite page="/personnelManageAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDeleteRecord';
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
    if(confirm("<bean:message key="javascript.role.deletecomfirm"/>"+document.serveTableForm.ids.value))
      {
        window.location = '<html:rewrite page="/personnelManageAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDeleteRecord';
      }
  }
  else
  {
    alert("<bean:message key="javascript.role.alert3"/>");
  }
}
}

//导出Excel
function excelMethod(valstr){
  serveTableForm.genReport.value="Y";
  serveTableForm.exceltype.value=valstr;
  serveTableForm.target = "_blank";
  document.serveTableForm.submit();
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
		 
		 var url='<html:rewrite page="/personnelManageAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
		 req.open("post",url,true);//post 异步
		 req.onreadystatechange=function getnextlist(){
			
				if(req.readyState==4 && req.status==200){
				 var xmlDOM=req.responseXML;
				 var rows=xmlDOM.getElementsByTagName('rows');
				 if(rows!=null){
					    selectfreeid.options.length=0;
					    selectfreeid.add(new Option("全部",""));	

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
              CreateToolBar();
            </script>
            </div>
            </td>
        </tr>
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar2" align="center">
            <script language="javascript">
              CreateToolBar2();
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>