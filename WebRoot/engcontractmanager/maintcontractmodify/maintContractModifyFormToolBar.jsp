<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

	  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
	  
	  <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintContractModify" value="Y"> 
	      AddToolBarItemEx("AuditBtn","../../common/images/toolbar/save.gif","","","保 存","65","1","saveMethod()");
	  </logic:equal>

  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//去掉空格
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

//关闭
function closeMethod(){
  window.close();
}
//返回
function returnMethod(){
	window.location = '<html:rewrite page="/maintContractModifySearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
	var maintDivision=document.getElementById("maintDivision").value;
	if(maintDivision.trim()=="" || maintDivision.trim()=="%"){
		alert("请选择 所属维保分部！");
		return;
	}
	
	var maintStation=document.getElementById("maintStation").value;
	if(maintStation.trim()=="" || maintStation.trim()=="%"){
		alert("请选择 所属维保站！");
		return;
	}
	
	var contractSdate=document.getElementById("contractSdate").value;
	if(contractSdate.trim()==""){
		alert("合同开始日期  不能为空！");
		return;
	}
	
	var contractEdate=document.getElementById("contractEdate").value;
	if(contractEdate.trim()==""){
		alert("合同结束日期  不能为空！");
		return;
	}
	
	//var assignedMainStation=document.getElementsByName("assignedMainStation");
	var elevatorNo=document.getElementsByName("elevatorNo");
	var mainEdate=document.getElementsByName("mainEdate");
	var iserrstr="";
	for(var i=0;i<mainEdate.length;i++){
		//if(assignedMainStation[i].value.trim()==""){
		//	iserrstr="第 "+(i+1)+"行 下达维保站 不能为空！";
		//	break;
		//}
		if(elevatorNo[i].value.trim()==""){
			iserrstr="第 "+(i+1)+"行 电梯编号 不能为空！";
			break;
		}

		if(mainEdate[i].value.trim()==""){
			iserrstr="第 "+(i+1)+"行 维保结束日期 不能为空！";
			break;
		}
	}
	if(iserrstr!=""){
		alert(iserrstr);
		return;
	}
	
	var contractTotal=document.getElementById("contractTotal").value;
	if(contractTotal.trim()==""){
		alert("合同总额 不能为空！");
		return;
	}
	document.maintContractModifyForm.submit();
}

//检查输入框是否输入数字
function checkthisvalue(obj){
  if(isNaN(obj.value)){
    alert("请输入数字!");
    obj.value="0";
    obj.focus();
    return false;
  }
}

function pickStartDay(endDateId){
  var endDate = document.getElementById(endDateId).value;
  endDate = endDate == null || endDate == ""　? "2099-12-31" : endDate    
  var d = endDate.split('-');
  WdatePicker({isShowClear:false,maxDate:d[0]+'-'+d[1]+'-'+(Number(d[2])-1),readOnly:true});
}
function pickEndDay(startDateId){
	//var date = new Date();
	var startDate = document.getElementById(startDateId).value;
	//var todayDate = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	//var d = startDate < todayDate ? todayDate.split('-') : startDate.split('-');
	var d=startDate.split('-');
	WdatePicker({isShowClear:false,minDate:d[0]+'-'+d[1]+'-'+(Number(d[2])+1),readOnly:true});
}
//计算合同有效期
function setContractPeriod(){
  var sd = maintContractModifyForm.contractSdate.value.split("-");
  var ed = maintContractModifyForm.contractEdate.value.split("-");
  if(sd != "" && ed != ""){  
    var years = Number(ed[0])-Number(sd[0]);
    var months = years>0 ? 12*years - (Number(sd[1]) - Number(ed[1])) : Number(ed[1]) - Number(sd[1]);
    months = Number(ed[2])-Number(sd[2])>0 ? months+1 : months;  
    maintContractModifyForm.contractPeriod.value = months;     
  }
}

function setAssignedMainStation(objval){
	 var amst=document.getElementsByName("assignedMainStation");//下达维保站
	 for(var i=0;i<amst.length;i++){
		 amst[i].value=objval;
	 }
	
}

//AJAX动态显示维保站
var req;
function EvenmoreUP(obj,listname){	
	var comid=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(comid!="" && comid!="%"){
		
		 if(window.XMLHttpRequest) {
			 req = new XMLHttpRequest();
		 }else if(window.ActiveXObject) {
			 req = new ActiveXObject("Microsoft.XMLHTTP");
		 }  //生成response
		 
		 var url='<html:rewrite page="/maintContractModifyAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
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