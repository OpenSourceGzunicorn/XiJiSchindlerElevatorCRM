<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<!--
	角色列表页工具栏
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","0","saveMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//保存
function saveMethod(){
/*
	var iFrame = window.frames("MainFrm");
	var rightFrm = iFrame.rightFrm;
	var saveFrm = rightFrm.frames("SaveFrm");

	var roleID = rightFrm.document.all.item("roleID").value;
	var allObj = rightFrm.document.all;
	var nodeRole = "";


	for(j=0;j<allObj.length;j++){
		if(allObj[j].type=="radio"){
		//设置权限字符串格式(formatStr格式为: nodeID1:writeFlag1#nodeID2:writeFlag2)
			if(allObj[j].checked == true && allObj[j].value != "A"){
				nodeRole = nodeRole + allObj[j].name + ":" + allObj[j].value + "#";
				}
			}
		}
	
    saveFrm.document.all.item("nodeRole").value = nodeRole;
    saveFrm.document.all.item("roleID").value = roleID;
  	saveFrm.document.nodeRoleForm.submit();
 */
 
 setDeletedIds();
}

function setDeletedIds() {
	var deletedIds = document.getElementById("deletedIds");
	var cbx = document.getElementsByName("roleIds"); 
	for(var i = 0;i<cbx.length;i++) {
      if(!cbx[i].checked) {
	    deletedIds.value+=cbx[i].value+",";
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