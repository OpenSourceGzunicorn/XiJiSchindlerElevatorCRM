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
    <%-- 是否有可写的权限--%>
    <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nMaintainProjectInfo" value="Y"> 
      AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
      AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
    </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/MaintainProjectInfoSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
   var elevatorType=document.MaintainProjectInfoForm.elevatorType.value;
   var maintType=document.MaintainProjectInfoForm.maintType.value;
   var maintItem=document.MaintainProjectInfoForm.maintItem.value;
   var maintContents=document.MaintainProjectInfoForm.maintContents.value;
   var orderby=document.MaintainProjectInfoForm.orderby.value;
   //var itemgroup=document.MaintainProjectInfoForm.itemgroup.value;
     if(elevatorType=="")
      {
    	 alert("请选择电梯类型");
    	 return;
      }
           if(maintType=="")
      {
    	 alert("请选择保养类型");
    	 return;
      }
           if(maintItem=="")
      {
    	 alert("请填写维保项目");
    	 return;
      }
           if(maintContents=="")
      {
    	 alert("请填写维保基本要求");
    	 return;
      }
       if(orderby=="")
      {
    	 alert("请填写排序号");
    	 return;
      }
	  document.MaintainProjectInfoForm.isreturn.value = "N";
      document.MaintainProjectInfoForm.submit();
  

}

//保存返回
function saveReturnMethod(){
 var elevatorType=document.MaintainProjectInfoForm.elevatorType.value;
   var maintType=document.MaintainProjectInfoForm.maintType.value;
   var maintItem=document.MaintainProjectInfoForm.maintItem.value;
   var maintContents=document.MaintainProjectInfoForm.maintContents.value;
   var orderby=document.MaintainProjectInfoForm.orderby.value;
   //var itemgroup=document.MaintainProjectInfoForm.itemgroup.value;
     if(elevatorType=="")
      {
    	 alert("请选择电梯类型");
    	 return;
      }
           if(maintType=="")
      {
    	 alert("请选择保养类型");
    	 return;
      }
           if(maintItem=="")
      {
    	 alert("请填写维保项目");
    	 return;
      }
           if(maintContents=="")
      {
    	 alert("请填写维保基本要求");
    	 return;
      }
       if(orderby=="")
      {
    	 alert("请填写排序号");
    	 return;
      }
	  document.MaintainProjectInfoForm.isreturn.value = "Y";
      document.MaintainProjectInfoForm.submit();
  

}

function openNewWindow(){
  openwindowReturnValueCustomer('query/Search.jsp?path=<html:rewrite page="/MaintainProjectInfoSearchAction.do" />?method=toPrincipalSelectList','MaintainProjectInfoForm','examType','checkItem','issueCoding','issueContents','enabledFlag');
}

function openwindowReturnValueCustomer(url,formname,key1,key2,key3){
  var obj = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
  if(obj)
  {
  var l = obj.length;
  var k=0;
  var kl = eval(formname+"."+key1).length;
  if(kl)
  {
    var t = 0;
    if(kl > l)
    {
      t = l;
    }
    else
    {
      t = kl;
    }
    
    for(i =0;i<t;i++)
    {
      if(key1 != "")
      {
        eval(formname+"."+key1+"["+i+"]").value = obj[i][0];
      }
      if(key2 != "")
      { 
        eval(formname+"."+key2+"["+i+"]").value = obj[i][1];
      }
      if(key3 != "")
      { 
        eval(formname+"."+key3+"["+i+"]").value = obj[i][2];
      }
    }
  }
  else if(l >=0)
  {
    if(key1 != "")
    {
        eval(formname+"."+key1).value = obj[0][0];
    }
    if(key2 != "")
    {
      eval(formname+"."+key2).value = obj[0][1];
    }
    if(key3 != "")
    {
      eval(formname+"."+key3).value = obj[0][2];
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
              CreateToolBar();
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>