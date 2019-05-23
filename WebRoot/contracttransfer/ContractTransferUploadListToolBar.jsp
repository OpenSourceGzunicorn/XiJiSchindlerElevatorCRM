 <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>

<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  
  AddToolBarItemEx("SearchBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nContractTransferUpload" value="Y">
 	AddToolBarItemEx("ReferBtn","../../common/images/toolbar/add.gif","","",'上传',"65","1","uploadMethod()");
  	AddToolBarItemEx("ReferBtn","../../common/images/toolbar/digital_confirm.gif","","",'批量上传',"80","1","uploadMethod2()");
  	AddToolBarItemEx("ReferBtn","../../common/images/toolbar/edit.gif","","",'批量反馈',"80","1","feedbackMethod()");
    AddToolBarItemEx("OutBtn","../../common/images/toolbar/disk_default.gif","","",'批量驳回',"80","1","outMethod()");
    <logic:equal name="transferflag" value="0">
    AddToolBarItemEx("ReferBtn","../../common/images/toolbar/edit.gif","","",'批量转派',"80","1","transferMethod()");
    </logic:equal>
  </logic:equal>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//查询
function searchMethod(){
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}

//驳回
function outMethod(){
	var msg = saveMethod("toPrepareOutRecord");
	if(msg!=""){
		alert(msg);
	}
}

//上传
function uploadMethod(){
	var index = getIndex();	
	toDoMethod(index,"toPrepareAddRecord","","请选择你要上传的记录！");
}

//批量上传
function uploadMethod2(){
	var msg = saveMethod("toPrepareAddRecord");
	if(msg!=""){
		alert(msg);
	}
}

//反馈
function feedbackMethod(){
	var msg = saveMethod("toPrepareFeedBackRecord");
	if(msg!=""){
		alert(msg);
	}
}

//批量转派
function transferMethod(){
	var msg = saveMethod("toPrepareTransferRecord");
	if(msg!=""){
		alert(msg);
	}
}

function saveMethod(method){
	var ids = serveTableForm.ids;
	var transfeSubmitType = document.getElementsByName("transfeSubmitType");
	var fileType = document.getElementsByName("fileType");
	var fFlag = document.getElementsByName("fFlag");
	var billnostr = "";
	var msg = "";
	var isflag = "Y";
	var l = ids.length;
	if(l){
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked == true){
				msg = "1";
				billnostr += ids[i].value+",";
				if(method=="toPrepareOutRecord"||method=="toPrepareTransferRecord"){
					if(transfeSubmitType[i].value=="Y"||transfeSubmitType[i].value=="R"){
						msg = ids[i].value+"是已提交或驳回记录！不能进行操作"
						return msg;
					}
				}else{
					if(transfeSubmitType[i].value=="Y"){
						msg = ids[i].value+"是已提交记录！不能进行操作"
						return msg;
					}
					
					for(var j=i+1;j<ids.length;j++){
						if(ids[j].checked == true){
							if(fileType[i].value != fileType[j].value){
								msg = ids[j].value+"附件类型不一致！不能进行操作";
								return msg;
							}
						}
					}
				}
				
				if(method=="toPrepareAddRecord"){
					isflag = "N";
					if(fFlag[i].value != "N"){
						msg = ids[i].value+"已上传附件！不能进行批量上传操作";
						return msg;
					}
				}
				
			}
		}
		if(msg == "1"){
			billnostr = billnostr.substring(0,billnostr.length-1);
			window.location = '<html:rewrite page="/ContractTransferUploadAction.do"/>?id='+billnostr+'&isflag='+isflag+'&method='+method;
			msg = "";
		}else{
			alert("请选择你要操作的记录！");
		}
	}else{
		if(method=="toPrepareOutRecord"){
			if(transfeSubmitType[0].value=="Y"||transfeSubmitType[0].value=="R"){
				msg = ids.value+"是已提交或驳回记录！不能进行驳回操作"
			}
		}else{
			if(transfeSubmitType[0].value=="Y"){
				msg = ids.value+"是已提交记录！不能进行操作"
			}
		}
		if(msg == ""){
			billnostr = ids.value;
			window.location = '<html:rewrite page="/ContractTransferUploadAction.do"/>?id='+billnostr+'&isflag='+isflag+'&method='+method;
		}
	}
	
	return msg;
	
}

//查看
function viewMethod(){
	var index = getIndex();	
	toDoMethod(index,"toDisplayRecord","","请选择你要查看的记录！");
}


//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(params==null){
		params='';
	}
	if(index >= 0){
		var domsg = "";
		
		if(method=="toPrepareOutRecord"){
			domsg = outcheck(index);
		}else if(method=="toDisplayRecord"){
			domsg = "";
		}else{
			domsg = feedbackcheck(index);
		}
		
		if(domsg==""){
			window.location = '<html:rewrite page="/ContractTransferUploadAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;	
		}else{
			alert(domsg);
		}
	}else if(alertMsg && alertMsg != ""){
		alert(alertMsg);
	}
}

//获取选中记录下标
function getIndex(){
	if(serveTableForm.ids){
		var ids = serveTableForm.ids;
		if(ids.length == null){
			return 0;
		}
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked == true){
				return i;
			}
		}		
	}
	return -1;	
}

//根据name和选中下标获取元素的值
function getVal(name,index){
	var obj = eval("serveTableForm."+name);
	if(obj && obj.length){
		obj = obj[index];
	}
	return obj ? obj.value : null;
}

//驳回检验
function outcheck(index){
	var transfeSubmitType = document.getElementsByName("transfeSubmitType");
	if(transfeSubmitType[index].value=="Y"||transfeSubmitType[index].value=="R"){
		return "请选择未提交的记录进行驳回！";
	}else{
		return "";
	}
}

//反馈检验
function feedbackcheck(index){
	var transfeSubmitType = document.getElementsByName("transfeSubmitType");
	
	if(transfeSubmitType[index].value=="Y"){
		return "该记录已提交！不能进行操作！";
	}else{
		return "";
	}
}



//AJAX动态显示维保站
var req;
function Evenmore(obj,listname){	
	var comid=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(comid!="" && comid!="%"){
		
		 if(window.XMLHttpRequest) {
			 req = new XMLHttpRequest();
		 }else if(window.ActiveXObject) {
			 req = new ActiveXObject("Microsoft.XMLHTTP");
		 }  //生成response
		 
		 var url='<html:rewrite page="/maintContractAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
		 req.open("post",url,true);//post 异步
		 req.onreadystatechange=function getnextlist(){
			
				if(req.readyState==4 && req.status==200){
				 var xmlDOM=req.responseXML;
				 var rows=xmlDOM.getElementsByTagName('rows');
				 if(rows!=null){
					    selectfreeid.options.length=0;
					    selectfreeid.add(new Option("全部","%"));	

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
		 };//回调方法
		 req.send(null);//不发送
	}else{		
		selectfreeid.options.length=0;
    	selectfreeid.add(new Option("全部","%"));
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