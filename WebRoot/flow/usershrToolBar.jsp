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
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'<bean:message key="toolbar.selectreturn"/>',"110","1","selectReturnMethod()");
  AddToolBarItemEx("CloseBtn","../../common/images/toolbar/close.gif","","",'<bean:message key="toolbar.close"/>',"65","1","closeMethod()"); 
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function closeMethod(){
  	window.close();
 }
 //查询
 function searchMethod(){
 	serveTableForm.target = "_self";
	document.serveTableForm.submit();
 }
 //选择返回
function selectReturnMethod(){
if(serveTableForm.ids)
{

	var l = document.serveTableForm.ids.length;
	if(l)
	{
		var returnarray = Array(l);
		var flag = false;
		var d = 0;
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{
				returnarray[d] = new Array(4);
				
				returnarray[d][0] = document.getElementsByName("userid")[i].value;
				returnarray[d][1] = document.getElementsByName("username")[i].value;
				returnarray[d][2] = document.getElementsByName("grcid")[i].value;
				returnarray[d][3] = document.getElementsByName("grcname")[i].value;
				d++;
				flag = true;
			}
		}	
		if(l >0 && !flag)
		{
			alert("<bean:message key='javascript.search.alert1'/>");
		}
		else
		{
		 parent.window.returnuu(returnarray);
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		var returnarray = Array(1);
		returnarray[0] = new Array(4);
		returnarray[0][0] = document.getElementsByName("userid")[0].value;
		returnarray[0][1] = document.getElementsByName("username")[0].value;
		returnarray[0][2] = document.getElementsByName("grcid")[0].value;
		returnarray[0][3] = document.getElementsByName("grcname")[0].value;
		parent.window.returnuu(returnarray);
	}
	else
	{
		alert("<bean:message key='javascript.search.alert1'/>");
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