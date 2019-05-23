<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
<logic:notPresent name="display">
 <%--  判断登录用户模块loginUser是否有可写的权限,在property	--%>
 <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nflow" value="Y">
  AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
  AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
 </logic:equal>
</logic:notPresent>
 window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  	 window.location = '<html:rewrite page="/flowSearchAction.do"/>?method=toSearchRecord';
 }

//保存
function saveMethod(){
	document.flowForm.isreturn.value = "N";
    document.flowForm.submit();
}

//保存返回
function saveReturnMethod(){
	document.flowForm.isreturn.value = "Y";
	document.flowForm.submit();
}

function decChange(obj,nid){
	//alert("dec_sco_"+nid);
	var dec=document.getElementById("dec_"+nid);
	var divs=dec.getElementsByTagName("div");
	//alert(divs.length);
	for(var i=0;i<divs.length;i++){
		if(divs[i].name!=null && divs[i].name!="" && divs[i].name.match("dec")!=-1){
			divs[i].style.display=divs[i].style.display=="none"?"":"none";
		}
	}
}
function add(obj){
	var thead=obj.getElementsByTagName("thead")[0];
	var tbody=obj.getElementsByTagName("tbody")[0];
	var tr=thead.lastChild.cloneNode(true);
	tr.style.display="";
	tbody.appendChild(tr);
	//tbody.insertBefore(tr);
}
function del(obj){
	var tbody=obj.getElementsByTagName("tbody")[0];
	for(var i=tbody.childNodes.length-1;i>=0;i--){
		if(tbody.childNodes[i].firstChild.firstChild.checked==true){
			tbody.deleteRow(i);
		}
	}
}
function setSel(url,tableid){
	/*var obj = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
	var len=obj.length;
	var ss="";
	for(var i=0;i<len;i++){
		ss+=obj[i];
	}
	alert(ss);
	*/
	var keys = ['','actors','ext1','typeid','','ext2'];
	openWindow3(url,tableid,keys);
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
                //SetToolBarAttribute();
              //-->
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>