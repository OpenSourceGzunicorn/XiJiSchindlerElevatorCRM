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
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","1","searchMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nentrustcontractquotemaster" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/add.gif","","",'下一步',"65","1","addMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/entrustContractQuoteMasterSearchAction.do"/>?method=toSearchRecord';
}

//查询
function searchMethod(){
  serveTableForm.target = "_self";
  document.serveTableForm.submit();
}

//增加
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
          var billNo = document.serveTableForm.ids[i].value;
          
          window.location = '<html:rewrite page="/entrustContractQuoteMasterAction.do"/>?billNo='+billNo+'&method=toPrepareAddRecord';
          return;       
        }
      }
      if(l >0)
      {
        alert("请选择一条记录！");
      }
    }else if(document.serveTableForm.ids.checked == true)
    {  
      var billNo = document.serveTableForm.ids.value;
        
      window.location = '<html:rewrite page="/entrustContractQuoteMasterAction.do"/>?billNo='+billNo+'&method=toPrepareAddRecord';
      return; 
    }
    else
    {
      alert("请选择一条记录！");
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