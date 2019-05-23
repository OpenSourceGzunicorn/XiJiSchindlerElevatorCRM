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
    <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nhotlinefaultclassification" value="Y"> 
      AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
      AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
    </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/hotlineFaultClassificationSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
  if(validateHotlineFaultClassificationForm(document.all.item("hotlineFaultClassificationForm"))==true){
    document.hotlineFaultClassificationForm.isreturn.value = "N";
    document.hotlineFaultClassificationForm.submit();
  }
}

//保存返回
function saveReturnMethod(){
  if(validateHotlineFaultClassificationForm(document.all.item("hotlineFaultClassificationForm"))==true){
    document.hotlineFaultClassificationForm.isreturn.value = "Y";
    document.hotlineFaultClassificationForm.submit();
  }  
}

function openNewWindow(){
  openwindowReturnValueCustomer('query/Search.jsp?path=<html:rewrite page="/hotlineFaultClassificationSearchAction.do" />?method=toPrincipalSelectList','hotlineFaultClassificationForm','hfcId','hfcName','enabledFlag');
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