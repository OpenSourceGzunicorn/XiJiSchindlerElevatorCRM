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
<logic:equal name="typejsp" value="display">
AddToolBarItemEx("SaveSubmitBtn","../../common/images/toolbar/close.gif","","",'关闭',"60","0","window.close()");
</logic:equal>
<logic:equal name="typejsp" value="search">
AddToolBarItemEx("SaveSubmitBtn","../../common/images/toolbar/fresh_record.gif","","",'刷新列表',"80","0","searchone('search')");
</logic:equal>
<logic:equal name="typejsp" value="arrival">
AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","searchone('search')");
AddToolBarItemEx("searchBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","1","searchtwo('arrival')");
AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod('arrival','N')");
AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveMethod('arrival','Y')");
</logic:equal>
<logic:equal name="typejsp" value="complete">
AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","searchone('search')");
AddToolBarItemEx("searchBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","1","searchtwo('complete')");
AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod('complete','N')");
AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveMethod('complete','Y')");
</logic:equal>

  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//刷新列表
function searchone(value){
  window.location = '<html:rewrite page="/ServicingContractMasterTaskSearchAction.do"/>?typejsp='+value;
}
//查询
function searchtwo(value){
  document.wgchangeContractForm.typejsp.value=value;
  document.wgchangeContractForm.submit();
}
//保存
function saveMethod(value,value2){
	if(value=="arrival"){
	var errorstr=savecheckinfo("");
	}else{
	var errorstr=savecheckinfo2("");	
	}
	if(errorstr==""){
	document.wgchangeContractForm.isreturn.value=value2;
	document.wgchangeContractForm.method.value=value;
	document.wgchangeContractForm.submit();
	}else{
	alert(errorstr);	
	}
} 

function checkedallbox(obj){
	var selected=obj.checked;
	var checkboxids=document.getElementsByName("checkboxids");
	for(var i=0;i<checkboxids.length;i++){
		checkboxids[i].checked=selected;
	}
}
function savecheckinfo(error){
	var checkboxids=document.getElementsByName("checkboxids");//需要保存的复选框
	var forecastdates=document.getElementsByName("forecastdate");//预计到货日期
	var comfirmdates=document.getElementsByName("comfirmdate");//确认到货日期
	var boxnum=0;
	for(var i=0;i<checkboxids.length;i++){
		if(checkboxids[i].checked){
			var forecastdate=forecastdates[i].value;
			var comfirmdate=comfirmdates[i].value;
			if(forecastdate.trim()==""){
				error+=" 请选择预计到货日期！\n";						
			}
			boxnum++;
			if(error!=""){
				error="序号"+(i+1)+"：\n"+error;
				break;
			}
		}
	}
	if(boxnum==0){
		error="必须选择一条记录进行保存！";
	}
	return error;
}

function savecheckinfo2(error){
	var checkboxids=document.getElementsByName("checkboxids");//需要保存的复选框
	var itemUserId=document.getElementsByName("itemUserId");//项目负责人
	var appWorkDate=document.getElementsByName("appWorkDate");//派工日期
	var enterArenaDate=document.getElementsByName("enterArenaDate");//进场日期
	var epibolyFlag=document.getElementsByName("epibolyFlag");//外包标志
	var finishFlag=document.getElementsByName("finishFlag");//完工标志
	var finishDate=document.getElementsByName("finishDate");//完工日期
	var boxnum=0;
	for(var i=0;i<checkboxids.length;i++){
		if(checkboxids[i].checked){
			var finishflagstr=finishFlag[i].value;
			var ItemUserIdstr=itemUserId[i].value;
			var AppWorkDatestr=appWorkDate[i].value;
			var EnterArenaDatestr=enterArenaDate[i].value;
			var EpibolyFlagstr=epibolyFlag[i].value;
			var FinishDatestr=finishDate[i].value;
			if(finishflagstr.trim()==""){
				error+=" 请选择完工标志！\n";
			}else if(finishflagstr.trim()=="Y"){
				if(ItemUserIdstr.trim()==""){
					error+=" 项目负责人不能为空！\n";
				}
				if(AppWorkDatestr.trim()==""){
					error+=" 派工日期不能为空！\n";
				}
				if(EnterArenaDatestr.trim()==""){
					error+=" 进场日期不能为空！\n";
				}
				if(EpibolyFlagstr.trim()==""){
					error+=" 请选择外包标志！\n";
				}
				if(FinishDatestr.trim()==""){
					error+=" 完工日期不能为空！\n";
				}
			}
			boxnum++;
			if(error!=""){
				error="序号"+(i+1)+"：\n"+error;
				break;
			}
		}
	}
	if(boxnum==0){
		error="必须选择一条记录进行保存！";
	}
	return error;
}

/** 设值 */
function sethiddenvalue(obj,namestr,num){
	var nameval=document.getElementsByName(namestr)[num];
	if(obj.checked){
		nameval.value="Y";
	}else{
		nameval.value="N";
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