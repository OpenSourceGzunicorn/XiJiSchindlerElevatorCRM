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
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncustomerlevel" value="Y"> 
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'等级维护',"80","1","modiyMethod()");
    AddToolBarItemEx("AllModifyBtn","../../common/images/toolbar/edit.gif","","",'计算等级',"80","1","allModiyMethod()");
  </logic:equal>
  
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//查询
function searchMethod(){
	serveTableForm.genReport.value = "";
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
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
				window.location = '<html:rewrite page="/customerLevelAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareUpdateRecord';
				return;
			}
		}
		if(l>0)
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/customerLevelAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareUpdateRecord';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert1"/>");
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
	 var url='<html:rewrite page="/customerLevelAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
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

function allModiyMethod()
{
	/* if(confirm("你确信要重新计算客户等级！"))
    { */
		document.getElementById("ViewBtn").disabled=true;
		document.getElementById("ModifyBtn").disabled=true;
		document.getElementById("AllModifyBtn").disabled=true;
		window.location ='<html:rewrite page="/customerLevelAction.do"/>?method=toALLUpdateRecord';
     /* } */
}

//导出Excel
function excelMethod(){
	serveTableForm.genReport.value="Y";
	serveTableForm.target = "_blank";
	document.serveTableForm.submit();
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