<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  
	AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
	//AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","window.close()");

  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");

}
//关闭
function closeMethod(){
	window.top.close() 
}

//添加合同明细
function addElevators(tableId){

  var paramstring = "state=WB&elevatorNos=";    
  var elevatorNos = document.getElementsByName("elevatorNo");
  for(i=0;i<elevatorNos.length;i++){
    paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|"    
  }
  var returnarray = openWindowWithParams("searchElevatorSaleAction","toSearchRecord2",paramstring);//弹出框并返回值
  if(returnarray!=null && returnarray.length>0){          
    addRows(tableId,returnarray.length);//增加行
    toSetInputValue(returnarray,"maintContractForm");//向页面对应输入框赋值
  }
  
  setTopRowDateInputsPropertychange(document.getElementById(tableId));
  
  setotherval();
}
//设置使用单位名称=购买单位名称
function setotherval(){
	var projectName=document.getElementsByName("projectName");
	var projectAddress=document.getElementsByName("projectAddress");
	for(var i=0;i<projectName.length;i++){
		projectAddress[i].value=projectName[i].value
	}
}

//计算合同有效期
function setContractPeriod(){
  var sd = maintContractForm.contractSdate.value.split("-");
  var ed = maintContractForm.contractEdate.value.split("-");
  if(sd != "" && ed != ""){  
    var years = Number(ed[0])-Number(sd[0]);
    var months = years>0 ? 12*years - (Number(sd[1]) - Number(ed[1])) : Number(ed[1]) - Number(sd[1]);
    months = Number(ed[2])-Number(sd[2])>0 ? months+1 : months;  
    maintContractForm.contractPeriod.value = months;     
  }
}

function checkInput(){
  inputTextTrim();// 页面所有文本输入框去掉前后空格
  
  var boo = false;
  
  var maintStation=document.getElementById("maintStation").value;
  if(maintStation==""){
	  alert("请选择 所属维保站！");
  }else{
	  boo=true;
  }
  if(boo == true){
	  var elevatorNos = document.getElementsByName("elevatorNo"); 
	  if(elevatorNos !=null && elevatorNos.length>0){
	    boo = checkColumnInput(maintContractForm) && checkRowInput("dynamictable_0","titleRow_0");
	  }else{
	    alert("请添加 合同明细。");
	    boo = false;
	  }
  }
  return boo;
}


function pickStartDay(endDateId){
  var endDate = document.getElementById(endDateId).value;
  endDate = endDate == null || endDate == ""　? "2099-12-31" : endDate   
  var d = endDate.split('-');
  WdatePicker({isShowClear:false,maxDate:d[0]+'-'+d[1]+'-'+(Number(d[2])-1),readOnly:true});
}


function pickEndDay(startDateId){
	var date = new Date();
	var startDate = document.getElementById(startDateId).value;
	var todayDate = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	var d = startDate < todayDate ? todayDate.split('-') : startDate.split('-');
	WdatePicker({isShowClear:false,minDate:d[0]+'-'+d[1]+'-'+(Number(d[2])+1),readOnly:true});
}


function setDatesByName(name,newDate){
  if(newDate && newDate!=""){
    var dates = document.getElementsByName(name);
          
    for ( var i = 0; i < dates.length; i++) { 
      if(dates[i].value == ""){
        dates[i].value = newDate;
      }  
    }  
  }
}

//明细表中，当日期控件第一行选择日期时，自动填上同一列为空的日期
function setTopRowDateInputsPropertychange(table){
  
  var tbody = table.getElementsByTagName("tbody")[0]; 
  var rows = tbody.childNodes;
  
  for(var i=0;i<rows.length;i++){
    if(!rows[i].id){
      var inputs = rows[i].getElementsByTagName("input");
      for(var j=0;j<inputs.length;j++){
       
        if(inputs[j].className.indexOf("Wdate")>=0){
           inputs[j].onpropertychange = function(){setDatesByName(this.name,this.value);};
        }
      }
      break;
    }
  }

}

//判断保养方式是否免费，免费时发货日期、质检发证日期、移交客户日期、维保确认日期必填
function isFree(mode){
	if(mode=="FREE"){
		$(".freeRequire").each(function(){
			$(this).append('<span style="color:red;">*</span>');
		})
		$("#contractTotal")[0].value=0;
		$("#otherFee")[0].value=0;
	} else {
		$(".freeRequire").each(function(){		
			$(this).children("span").remove();
		})
		$("#contractTotal")[0].value='${maintContractBean.contractTotal}';
		$("#otherFee")[0].value='${maintContractBean.otherFee}';
	}
}

//选择新建
function selectAddElements(isSelectAdd){
    if(isSelectAdd == "Y"){
        $("#caption_0").html("<b>&nbsp;合同明细</b>");     
        $("#btGetSc").hide();
        $("#salesContractNo,#salesContractName,#deliveryAddress,#contractTotal,#otherFee").parent().html(function(){return this.innerHTML.replace("*","")})
        $("#salesContractNo,#salesContractName,#deliveryAddress,#contractTotal,#otherFee").attr("class","default_input_noborder");
        $("#salesContractName,#deliveryAddress,#contractTotal,#otherFee").attr("readonly","readonly");
      }
}

//
function changeSignWay(obj){
	var name=obj.name;
	var signWay=document.getElementsByName(name);
	if(obj==signWay[0]){
		for(var i=0;i<signWay.length;i++){
			signWay[i].value=signWay[0].value;
		}
	}
}

//AJAX动态显示维保站
var req;
function EvenmoreAdd(obj,listname){	
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

//当选择的是第一行的日期时，将后续的日期的值设置为第一行的日期值
function setDateChanged(obj){
	var datearr=document.getElementsByName(obj.name);
	var dnum=0;
	for(var j=0;j<datearr.length;j++){
		if(datearr[j]==obj){
			dnum=j;
			break;
		}
	}
	//判断是否是第一行日期
	if(dnum==0){
		var datevalue=datearr[dnum].value;
		for(var i=0;i<datearr.length;i++){
			if(datearr[i].value.trim()==""){
				datearr[i].value=datevalue;
			}
		}
	}
}

//设置合同开始日期
function setcontractSdate(){
	var mainSdate = document.getElementsByName('mainSdate');
	var contractSdateval = mainSdate[0].value;
	for(var i=1;i<mainSdate.length;i++){
		var date1 = mainSdate[i].value;
		if(date1<contractSdateval){
			contractSdateval = date1;
		}
	}
	document.getElementById('contractSdate').value=contractSdateval;
}

//设置合同结束日期
function setcontractEdate(){
	var mainEdate = document.getElementsByName('mainEdate');
	var contractEdateval = mainEdate[0].value;
	for(var i=1;i<mainEdate.length;i++){
		var date1 = mainEdate[i].value;
		if(date1>contractEdateval){
			contractEdateval = date1;
		}
	}
	document.getElementById('contractEdate').value=contractEdateval;
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