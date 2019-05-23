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

  <logic:present name="workisdisplay">  
	AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
	//AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","window.close()");
</logic:present>
<logic:notPresent name="workisdisplay"> 
  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintcontractquote" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  </logic:notPresent> 
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//关闭
function closeMethod(){
  window.close();
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/maintContractQuoteSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
	if(checkInput()){
		//var standardQuote=document.getElementsByName("standardQuote");//标准报价
		var finallyQuote=document.getElementsByName("finallyQuote");//最终报价
		for(var i=0;i<finallyQuote.length;i++){
			//if(standardQuote[i].value==""){
			//	alert("第 "+(i+1)+" 行，标准报价不能为空，请计算标准报价！");
			//	return;
			//}
			if(finallyQuote[i].value==""){
				alert("第 "+(i+1)+" 行，最终报价不能为空！");
				return;
			}
		}
		
		//计算标准价格
		if(jsQuote()){
			  maintContractQuoteForm.maintContractQuoteDetails.value=rowsToJSONArray2("dynamictable_0","details");
			  //alert(maintContractQuoteForm.maintContractQuoteDetails.value);
			  maintContractQuoteForm.isreturn.value = "N";
	  		  maintContractQuoteForm.submit();
		}
	}
}

//保存返回
function saveReturnMethod(){
	if(checkInput()){
		//var standardQuote=document.getElementsByName("standardQuote");//标准报价
		var finallyQuote=document.getElementsByName("finallyQuote");//最终报价
		for(var i=0;i<finallyQuote.length;i++){
			//if(standardQuote[i].value==""){
			//	alert("第 "+(i+1)+" 行，标准报价不能为空，请计算标准报价！");
			//	return;
			//}
			if(finallyQuote[i].value==""){
				alert("第 "+(i+1)+" 行，最终报价不能为空！");
				return;
			}
		}
		
		//计算标准价格
		if(jsQuote()){
			maintContractQuoteForm.maintContractQuoteDetails.value=rowsToJSONArray2("dynamictable_0","details");  		
	    	document.maintContractQuoteForm.isreturn.value = "Y";
	    	document.maintContractQuoteForm.submit();
		}
    } 
}

//添加电梯信息
function addElevators(tableId){
	var tableObj = document.getElementById(tableId);
	var paramstring = "state=WBBJ&elevatorNos=";		
	var elevatorNos = document.getElementsByName("elevatorNo");
	for(i=0;i<elevatorNos.length;i++){
		paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|"		
	}

	var returnarray = openWindowWithParams("searchElevatorSaleAction","toSearchRecord2",paramstring);

	if(returnarray!=null && returnarray.length>0){
		//alert(returnarray);
		addRows(tableId,returnarray.length);
		toSetInputValue(returnarray,"maintContractQuoteForm");
	}			
}

function checkInput(){  
  inputTextTrim();// 页面所有文本输入框去掉前后空格
  var boo = false;
  
  var companyName=document.getElementById("companyName").value;
  if(companyName==""){
	  alert("甲方单位名称 不能为空！");
	  boo = false;
  }else{
	  boo=true;
  }
  if(boo == true){
	  var maintStation=document.getElementById("maintStation").value;
	  if(maintStation==""){
		  alert("请选择 所属维保站！");
		  boo = false;
	  }else{
		  boo=true;
	  }
  }

  if(boo == true){
	  var paymentMethodApply=document.getElementById("paymentMethodApply").value;
	  if(paymentMethodApply==""){
		  alert("请选择 付款方式申请！");
		  boo = false;
	  }else{
		  //其他
		  if(paymentMethodApply=="99"){
			  var paymentMethodRem=document.getElementById("paymentMethodRem").value;
			  if(paymentMethodRem==""){
				  alert("付款方式申请备注 不能为空！");
				  boo = false;
			  }else{
				  boo=true;
			  }
		  }else{
			  boo=true;
		  }
	  }
   }
  if(boo == true){
	  var appnum=0;
	  var contractContentApply = document.getElementsByName("contractContentApply"); 
	  for(var c=0;c<contractContentApply.length;c++){
		if(contractContentApply[c].checked==true){
			appnum++;
			break;
		}  
	  }
	  if(appnum==0){
		  //alert("请选择 合同包含配件及服务！");
		  //boo = false;
	  }else{
		  var valnum=0;
		  for(var c=0;c<contractContentApply.length;c++){
			if(contractContentApply[c].checked==true && contractContentApply[c].value=="99"){
				valnum++;
				break;
			}  
		  }
		  if(valnum>0){
			  var contractContentRem=document.getElementById("contractContentRem").value;
			  if(contractContentRem==""){
				  alert("合同包含配件及服务备注 不能为空！");
				  boo = false;
			  }else{
				  boo=true;
			  }
		  }
	  }
  }
  
  if(boo == true){
	  var elevatorNos = document.getElementsByName("elevatorNo"); 
	  if(elevatorNos !=null && elevatorNos.length>0){
	    boo = checkRowInput("dynamictable_0","titleRow_0");
	  }else{
	    alert("请添加 电梯信息 。");
	    boo = false;
	  }
   }
  
  return boo;
}

function isShowHidden(obj){
	var target=document.getElementById("trok");
	if(obj.value=="99"){
		target.style.display="";
	}else{
		target.style.display="none";
	}
}
function isShowHidden2(){
	var appnum=0;
	var contractContentApply = document.getElementsByName("contractContentApply"); 
	for(var c=0;c<contractContentApply.length;c++){
	  if(contractContentApply[c].checked==true && contractContentApply[c].value=="99"){
			appnum++;
			break;
	  }  
	}
	var target=document.getElementById("trok2");
	if(appnum>0){
		target.style.display="";
	}else{
		target.style.display="none";
	}
}

/**
function jsQuote2(){
	 var etcfarray = new Array(); 
	//电梯年龄系数
	<logic:iterate id="ele" name="returnhmap" property="etcfList">
		var obj=new Object();
		obj.elevatortype="${ele.elevatortype}";
		obj.coefficient="${ele.coefficient}";
		obj.basicprice="${ele.basicprice}";
		etcfarray.push(obj);
	</logic:iterate>

	for(var i=0;i<etcfarray.length;i++){
		var obj=etcfarray[i];
		alert(obj.elevatortype+"=="+obj.coefficient+"=="+obj.basicprice);
	}
}
*/

/**===========================计算标准报价=======================*/
/** 在字符串中提取数字 */
function getNum(text){
	//var value = text.replace(/[^0-9]/ig,""); //获取数字
	var value = text.replace(/[^\d.]/g,"");//获取数字和小数点
	return value;
}
/** 计算公式 
 "直梯的标准价格/台 = 月数*基础*电梯类型系数*载重系数*梯速系数*门系数*电梯年龄系数*维保站系数*合同包含配件及服务系数*
                                                支付方式系数*合同年限系数+年检费金额+限速器校验金额+润滑油脂+灯"

"扶梯的标准价格/台 = 月数*基础*电梯类型系数*电梯年龄系数*维保站系数*合同包含配件及服务系数*
                                            支付方式系数*合同年限系数+年检费金额+限速器校验金额+润滑油脂+灯"
                                            
                                            限速器校验金额+润滑油脂+灯 需要乘以月份

    范围比较公式：大于等于最小的 小于等于最大的（>=0 and <=11）
 */

function jsQuote(){
	
	<logic:present name="returnhmap">
	if(checkInput()){

		var sqtotal=0;
		var fqtotal=0;
		
		//获取维保站系数
		var mscf='';
		var maintStation=document.getElementById("maintStation").value;
	 	<logic:iterate id="ele" name="returnhmap" property="mscfList">
			if(maintStation=="${ele.maintstation}"){
				mscf="${ele.coefficient}";
			}
		</logic:iterate>
		if(mscf==''){
			alert("维保站系数不存在！");
			return false;
		}
		
		//付款方式申请
		var pmcf='';
		var paymentMethodApply=document.getElementById("paymentMethodApply").value;
	 	<logic:iterate id="ele" name="returnhmap" property="pmcfList">
			if(paymentMethodApply=="${ele.paymentmethod}"){
				pmcf="${ele.coefficient}";
			}
		</logic:iterate>
		if(pmcf==''){
			alert("付款方式系数不存在！");
			return false;
		}
		
		
		//合同包含配件及服务
		var cacf="";//配件系数
		var smoney=0;//服务金额
		var smoneyname="";//服务名称
		var smoney7=0;//含限速器校验费服务金额
		var smoneyname7="";//含限速器校验费服务名称
		var smoney8=0;//含灯金额
		var smoneyname8="";//含灯服务名称
		var smoney9=0;//含润滑油脂服务金额
		var smoneyname9="";//含润滑油脂服务名称
		var jyf='';//是否包含年检费
		var contractContentApply = document.getElementsByName("contractContentApply"); 
		var contractContentApplyName = document.getElementsByName("contractContentApplyName"); 
		  for(var c=0;c<contractContentApply.length;c++){
			if(contractContentApply[c].checked==true){
				//配件
				<logic:iterate id="ele" name="returnhmap" property="cacfList">
					if(contractContentApply[c].value=="${ele.contentapplyid}"){
						cacf="${ele.coefficient}";
					}
				</logic:iterate>
				
				//是否勾选了包含年检费
				if(contractContentApply[c].value=="6"){
					jyf='6';
				}else{
					//服务金额
					<logic:iterate id="ele2" name="returnhmap" property="mssList">
						if(contractContentApply[c].value=="${ele2.serviceid}"){
							if(contractContentApply[c].value=="7"){
								//含限速器校验费,需要乘以月数
								smoney7=parseFloat("${ele2.servicemoney}");
								smoneyname7=contractContentApplyName[c].value+"(${ele2.servicemoney})";
							}else if(contractContentApply[c].value=="8"){
								//含灯,需要乘以月数
								smoney8=parseFloat("${ele2.servicemoney}");
								smoneyname8=contractContentApplyName[c].value+"(${ele2.servicemoney})";
							}else if(contractContentApply[c].value=="9"){
								//含润滑油脂,需要乘以月数
								smoney9=parseFloat("${ele2.servicemoney}");
								smoneyname9=contractContentApplyName[c].value+"(${ele2.servicemoney})";
							}else{
								smoney+=parseFloat("${ele2.servicemoney}");
								smoneyname+="+"+contractContentApplyName[c].value+"(${ele2.servicemoney})";
							}
						}
					</logic:iterate>
				}
			}  
		  }
		  if(cacf==''){
			  //不含配件=0.93
			  cacf='0.93';
			  //alert("合同包含配件及服务系数不存在！");
			  //return;
		  }
	
		  /** 电梯明细信息 */
		  var standardQuote=document.getElementsByName("standardQuote");//标准报价（元）
		  var finallyQuote=document.getElementsByName("finallyQuote");//最终报价（元）
		  
		  var quotedis=document.getElementsByName("standardQuoteDis");//标准报价计算描述
		  
		  var contractPeriods=document.getElementsByName("contractPeriod");//合同有效期（月）
		  var elevatorAges=document.getElementsByName("elevatorAge");//电梯年龄
		  var elevatorTypes=document.getElementsByName("elevatorType");//电梯类型
		  var floors=document.getElementsByName("floor");//层
		var weights=document.getElementsByName("weight");//载重
		var speeds=document.getElementsByName("speed");//速度
		var jymoney=document.getElementsByName("jymoney");//年检费
		
		//台量系数
		var tlcf='';
		<logic:iterate id="ele" name="returnhmap" property="tlcfList">
			if(parseFloat(elevatorTypes.length)>=parseFloat("${ele.stl}") 
				&& parseFloat(elevatorTypes.length)<=parseFloat("${ele.etl}")){
				tlcf="${ele.coefficient}";
			}
		</logic:iterate>
		if(tlcf==''){
			alert("台量系数不存在！");
			return false;
		}
		
		for(var i=0;i<elevatorTypes.length;i++){
			//电梯类型系数
			var etcf="";
			var basicprice="";
			<logic:iterate id="ele" name="returnhmap" property="etcfList">
				var et="${ele.elevatortype}";
				if(et=="T"){
					et="直梯";
				}else if(et=="F"){
					et="扶梯";
				}
				if(elevatorTypes[i].value==et){
					etcf="${ele.coefficient}";
					basicprice="${ele.basicprice}";
				}
			</logic:iterate>
			if(etcf==''){
				alert("电梯类型系数不存在！");
				return false;
			}
			if(basicprice==''){
				alert("电梯类型基础价格不存在！");
				return false;
			}
			//电梯年龄系数
			var eafc='';
		 	<logic:iterate id="ele" name="returnhmap" property="eafcList">
				if(parseFloat(elevatorAges[i].value)>=parseFloat("${ele.selevatorage}") 
						&& parseFloat(elevatorAges[i].value)<=parseFloat("${ele.eelevatorage}")){
					eafc="${ele.coefficient}";
				}
			</logic:iterate>
			if(eafc==''){
				alert("电梯年龄系数不存在！");
				return false;
			}
			//合同有效期系数 
			var cpfc='';
		 	<logic:iterate id="ele" name="returnhmap" property="cpfcList">
				if(parseFloat(contractPeriods[i].value)>=parseFloat("${ele.scontractperiod}") 
						&& parseFloat(contractPeriods[i].value)<=parseFloat("${ele.econtractperiod}")){
					cpfc="${ele.coefficient}";
				}
			</logic:iterate>
			if(cpfc==''){
				alert("合同有效期系数不存在！");
				return false;
			}

			if(jyf!=""){
				if(jymoney[i].value==""){
					alert("第 "+(i+1)+" 行，年检费不能为空！");
					return false;
				}else if(isNaN(jymoney[i].value)){
					alert("第 "+(i+1)+" 行，年检费必须为数字！");
					return false;
				}
			}
			
			//直梯才有，门，载重，速度的计算
			if(elevatorTypes[i].value=="直梯"){
				//门系数
				var dfc='';
			 	<logic:iterate id="ele" name="returnhmap" property="dfcList">
					if(parseFloat(floors[i].value)>=parseFloat("${ele.sdoor}") 
							&& parseFloat(floors[i].value)<=parseFloat("${ele.edoor}")){
						dfc="${ele.coefficient}";
					}
				</logic:iterate>
				if(dfc==''){
					alert("门系数不存在！");
					return false;
				}
				//载重系数
				var weight=getNum(weights[i].value);
				if(weight==""){
					alert("第 "+(i+1)+" 行，获取载重的数字失败！");
					return false;
				}
				var wfc='';
			 	<logic:iterate id="ele" name="returnhmap" property="wfcList">
					if(parseFloat(weight)>=parseFloat("${ele.sweight}") 
							&& parseFloat(weight)<=parseFloat("${ele.eweight}")){
						wfc="${ele.coefficient}";
					}
				</logic:iterate>
				if(wfc==''){
					alert("载重系数不存在！");
					return false;
				}
				//速度系数
				var speed=getNum(speeds[i].value);
				if(speed==""){
					alert("第 "+(i+1)+" 行，获取速度的数字失败！");
					return false;
				}
				var sfc='';
			 	<logic:iterate id="ele" name="returnhmap" property="sfcList">
					if(parseFloat(speed)>=parseFloat("${ele.sspeed}") 
							&& parseFloat(speed)<=parseFloat("${ele.espeed}")){
						sfc="${ele.coefficient}";
					}
				</logic:iterate>
				if(sfc==''){
					alert("速度系数不存在！");
					return false;
				}
				
				//直梯计算
				//"直梯的标准价格/台 = 月数*基础*电梯类型系数*载重系数*梯速系数*门系数*电梯年龄系数*维保站系数*合同包含配件及服务系数*
				//支付方式系数*合同年限系数+年检费金额+限速器校验金额+润滑油脂+灯"
	 			var squ=parseFloat(contractPeriods[i].value)*parseFloat(basicprice)*parseFloat(etcf)
	 					*parseFloat(wfc)*parseFloat(sfc)*parseFloat(dfc)*parseFloat(eafc)*parseFloat(mscf)
	 					*parseFloat(cacf)*parseFloat(pmcf)*parseFloat(cpfc)*parseFloat(tlcf)
	 					+parseFloat(smoney);
				var squdis="直梯的标准价格=合同有效期("+contractPeriods[i].value+")*基础价格("+basicprice+")*电梯类型系数("+etcf
						+")*载重系数("+wfc+")*梯速系数("+sfc+")*门系数("+dfc+")*电梯年龄系数("+eafc+")*维保站系数("+mscf
						+")*合同包含配件及服务系数("+cacf+")*支付方式系数("+pmcf+")*合同年限系数("+cpfc+")*台量系数("+tlcf+")"
						+smoneyname;
				//含限速器校验费,需要乘以月数
				if(smoneyname7!=""){
					squ+=(parseFloat(smoney7)*parseFloat(contractPeriods[i].value));
					squdis+="+("+smoneyname7+"*"+contractPeriods[i].value+")";
				}
				//含灯,需要乘以月数
				if(smoneyname8!=""){
					squ+=(parseFloat(smoney8)*parseFloat(contractPeriods[i].value));
					squdis+="+("+smoneyname8+"*"+contractPeriods[i].value+")";
				}
				//含润滑油脂,需要乘以月数
				if(smoneyname9!=""){
					squ+=(parseFloat(smoney9)*parseFloat(contractPeriods[i].value));
					squdis+="+("+smoneyname9+"*"+contractPeriods[i].value+")";
				}
				//年检费
				if(jyf!=""){
					squ+=parseFloat(jymoney[i].value);
					squdis+="+年检费("+jymoney[i].value+")";
				}
				
	
				standardQuote[i].value=parseFloat(squ).toFixed(2);//标准报价（元）
				quotedis[i].value=squdis;//标准报价计算描述
				
				sqtotal+=parseFloat(standardQuote[i].value);
				/**
				if(finallyQuote[i].value==""){
					finallyQuote[i].value=parseFloat(squ).toFixed(2);//最终报价
					fqtotal+=parseFloat(finallyQuote[i].value);
				}else{
					fqtotal+=parseFloat(finallyQuote[i].value);
				}
				*/
			}else{
				//扶梯计算
				//"扶梯的标准价格/台 = 月数*基础*电梯类型系数*电梯年龄系数*维保站系数*合同包含配件及服务系数*
				//支付方式系数*合同年限系数+年检费金额+限速器校验金额+润滑油脂+灯"
				var squ=parseFloat(contractPeriods[i].value)*parseFloat(basicprice)*parseFloat(etcf)
	 					*parseFloat(eafc)*parseFloat(mscf)
	 					*parseFloat(cacf)*parseFloat(pmcf)*parseFloat(cpfc)*parseFloat(tlcf)
	 					+parseFloat(smoney);
				var squdis="扶梯的标准价格=合同有效期("+contractPeriods[i].value+")*基础价格("+basicprice+")*电梯类型系数("+etcf
						+")*电梯年龄系数("+eafc+")*维保站系数("+mscf
						+")*合同包含配件及服务系数("+cacf+")*支付方式系数("+pmcf+")*合同年限系数("+cpfc+")*台量系数("+tlcf+")"
						+smoneyname;
				//含限速器校验费,需要乘以月数
				if(smoneyname7!=""){
					squ+=(parseFloat(smoney7)*parseFloat(contractPeriods[i].value));
					squdis+="+("+smoneyname7+"*"+contractPeriods[i].value+")";
				}
				//含灯,需要乘以月数
				if(smoneyname8!=""){
					squ+=(parseFloat(smoney8)*parseFloat(contractPeriods[i].value));
					squdis+="+("+smoneyname8+"*"+contractPeriods[i].value+")";
				}
				//含润滑油脂服务金额,需要乘以月数
				if(smoneyname9!=""){
					squ+=(parseFloat(smoney9)*parseFloat(contractPeriods[i].value));
					squdis+="+("+smoneyname9+"*"+contractPeriods[i].value+")";
				}
				//年检费
				if(jyf!=""){
					squ+=parseFloat(jymoney[i].value);
					squdis+="+年检费("+jymoney[i].value+")";
				}
	
				standardQuote[i].value=parseFloat(squ).toFixed(2);//标准报价（元）
				quotedis[i].value=squdis;//标准报价计算描述
				
				sqtotal+=parseFloat(standardQuote[i].value);
				/**
				if(finallyQuote[i].value==""){
					finallyQuote[i].value=parseFloat(squ).toFixed(2);//最终报价
					fqtotal+=parseFloat(finallyQuote[i].value);
				}else{
					fqtotal+=parseFloat(finallyQuote[i].value);
				}
				*/
			}
			
			
		}
	
		//业务费
		var bcosts=document.getElementById("businessCosts").value;
		if(bcosts==""){
			bcosts="0";
		}
		//标准报价合计 标准合同总额= 各台标准价格总和+业务费
		document.getElementById("standardQuoteTotal").value=(parseFloat(sqtotal)+parseFloat(bcosts)).toFixed(2);
		//最终报价合计
		//document.getElementById("finallyQuoteTotal").value=parseFloat(fqtotal).toFixed(2);
		
		jsr8();//计算折扣率
	}
	</logic:present>
	
	return true;
}

//计算折扣率【 折扣率=最终合同总额/标准合同总额*100%】
function jsr8(){
	sumValuesByName('finallyQuote','finallyQuoteTotal');
	//最终报价合计
	var finallyQuoteTotal=document.getElementById("finallyQuoteTotal").value;
	//标准报价合计
	var standardQuoteTotal=document.getElementById("standardQuoteTotal").value;
	if(standardQuoteTotal!="" && standardQuoteTotal!=0 && finallyQuoteTotal!=""){
		document.getElementById("discountRate").value=(parseFloat(finallyQuoteTotal)/parseFloat(standardQuoteTotal)*100).toFixed(2);
	}
}

function jssdqtotal(val){
	if(val==""){
		val="0";
	}
	var total=0;
	var standardQuote=document.getElementsByName("standardQuote");//标准报价（元）
	for(var i=0;i<standardQuote.length;i++){
		if(standardQuote[i].value!=""){
			total+=parseFloat(standardQuote[i].value);
		}
	}
	document.getElementById("standardQuoteTotal").value=(parseFloat(total)+parseFloat(val)).toFixed(2);
	
	jsr8();//计算折扣率
}
/**===========================计算标准报价=======================*/

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