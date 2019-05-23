<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  <logic:present name="workisdisplay">  
	AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
	//AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","window.close()");
</logic:present>
<logic:notPresent name="workisdisplay">  

	  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
	  <logic:notPresent name="display">  
	  <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncustreturnregister" value="Y"> 
	    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
	  </logic:equal>
	  </logic:notPresent>
  
 </logic:notPresent> 
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//关闭
function closeMethod(){
  window.close();
}

//返回
function returnMethod(){
	
	var isreturn=document.getElementById("isreturn2").value;
	if(isreturn=="2")
		{
		window.location = '<html:rewrite page="/custReturnRegisterAction.do"/>?id='+ document.getElementById("contactPhone").value +'&method=toHistoryDisplayRecord';
		}else
			{
            window.location = '<html:rewrite page="/custReturnRegisterSearchAction.do"/>?method=toSearchRecord';
			}
}

//判断是否为数字
function onlyNumber(c){
	  if(!/^\d+$/.test(c.value)){
		   var tile="<bean:message key='markingitems.check.error.cgprice.required'/>";
		  alert(tile);
		  c.value=0;
	  }
}

//只能输入数字
function f_check_number2(){
	  	if((event.keyCode>=48 && event.keyCode<=57)){
	  	}else{
			event.keyCode=0;
	   	}
	 }
//保存
function saveMethod(){
	var handle=document.getElementById("ministerHandle");	
	  if(handle.value!=""){
		  document.custReturnRegisterForm.isreturn.value = "Y";
	    document.custReturnRegisterForm.submit();
	   }else
	   {
		   alert("请选择是否分部长处理！");
	   }
}

function contractdisplay(wflag,val){
	if(wflag=="wt"){
		var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
		var url=projectName+"returningMaintainDetailAction.do?method=toDisplayRecord&isOpen=Y&id="+val;
		window.open(url,'_blank');
	}else{
		var paramstring = "typejsp=Yes";
		var paramstring1 = "id="+val;
		var paramstring2 = "HFflag=Y";
		var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
		var url=projectName+"maintContractAction.do?method=toDisplayRecord&isOpen=Y&"+paramstring+"&"+paramstring1+"&"+paramstring2;
		window.open(url,'_blank');//弹出框
		  
	}
}

</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" >
        <tr >
          <td height="22" class="table_outline3" valign="bottom" width="100%" >
            <div id="toolbar" align="center">
            <script language="javascript">
              
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