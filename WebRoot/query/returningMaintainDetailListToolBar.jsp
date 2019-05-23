<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  <logic:notPresent name="isOpen">
  	AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  	AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'<bean:message key="toolbar.selectreturn"/>',"110","1","selectReturnMethod()");
  	AddToolBarItemEx("CloseBtn","../../common/images/toolbar/close.gif","","",'<bean:message key="toolbar.close"/>',"65","1","closeMethod()"); 
  </logic:notPresent>
  <logic:present name="isOpen">
  	AddToolBarItemEx("CloseBtn","../../common/images/toolbar/close.gif","","",'<bean:message key="toolbar.close"/>',"65","0","closeMethod()");
  </logic:present>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//关闭
function closeMethod(){
  window.close();
}
//查询
function searchMethod(){
  serveTableForm.target = "_self";
  document.serveTableForm.submit();
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
	 var url='<html:rewrite page="/searchCustomerlevelAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
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
</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar" align="center">
            <script language="javascript">
              <!--
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