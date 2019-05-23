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
   // alert("saveMethod()");
    var iFrame = window.frames("MainFrm");
	var rightFrm = iFrame.rightFrm;
	// var headerId = rightFrm.document.all.item("headerId").value;//隐藏域的上司id
    //alert("hi");
    setDeletedIds();
    setAddedIds();
  	rightFrm._submit();// 
   
}


function setDeletedIds() {
    var iFrame = window.frames("MainFrm");
	var rightFrm = iFrame.rightFrm;
    var deletedIds = rightFrm.document.getElementById("deletedIds");
     
	var cbx = rightFrm.document.getElementsByName("check"); 
	for(var i = 0;i<cbx.length;i++) {
      if(!cbx[i].checked) {
	    deletedIds.value+=cbx[i].value+",";
      }
	}
    //alert("deletedIds : " + deletedIds.value);
}

function setAddedIds(){
    var iFrame = window.frames("MainFrm");
	var rightFrm = iFrame.rightFrm;
    var addedIds = rightFrm.document.getElementById("addedIds");
     
	var cbx = rightFrm.document.getElementsByName("check"); 
	for(var i = 0;i<cbx.length;i++) {
      if(cbx[i].checked) {
	    addedIds.value+=cbx[i].value+",";
      }
	}
    //alert("addedIds : " + addedIds.value);
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