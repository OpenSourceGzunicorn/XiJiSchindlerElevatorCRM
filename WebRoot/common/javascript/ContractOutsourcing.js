
//去掉空格
 String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

 //检查金额是否为数字
 function f_check_number2(){
  	if((event.keyCode>=48 && event.keyCode<=57)){
  	}else{
		event.keyCode=0;
   	}
 }
 
 //检查金额是否为数字,不可以输入负号和可以输入点号
 function f_check_number3(){
  	if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==46 ){
  	}else{
		event.keyCode=0;
   	}
 }
  //检查金额是否为数字,不可以输入点号和可以输入负号
 function f_check_number4(){
  	if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==45){
  	}else{
		event.keyCode=0;
   	}
 }
  //检查金额是否为数字,可以输入负号和点号
 function f_check_number5(){
  	if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==45 || event.keyCode==46){
  	}else{
		event.keyCode=0;
   	}
 }

   /** 更新序号显示 */
 function xuhaonum(objname){
	var divs=document.getElementsByName(objname);
	for(var i=0;i<divs.length;i++){
		divs[i].innerText=i+1;
	}
 }
  //复选框全选
  function selAllNode(obj,name){
	var idss=document.getElementsByName(name);
		for(var i=idss.length-1;i>=0;i--){
			idss[i].checked=obj.checked;
		}
	}
  //删除
  function delSelNode22(ptable){
		var pbody=ptable.getElementsByTagName("TBODY")[0];
		var len=pbody.children.length;
		var nodevalue="";
		for(var i=len-1;i>=1;i--){
			//if(pbody.children[i].firstChild.firstChild.checked==true){//ids
				pbody.deleteRow(i);
			//}
		}
   }
  //删除 委托工料费的构成
  function delSelNode23(ptable,num){
		var pbody=ptable.getElementsByTagName("TBODY")[0];
		var len=pbody.children.length;
		var nodevalue="";
		if(num=0){
			for(var i=len-2;i>=1;i--){
				//if(pbody.children[i].firstChild.firstChild.checked==true){//ids
					pbody.deleteRow(i);
				//}
			}
		}else{
			for(var i=len-2;i>=1;i--){
				if(pbody.children[i].firstChild.firstChild.checked==true){//ids
					pbody.deleteRow(i);
				}
			}
		}
		xuhaonum("Wdivorderw");//序号
		jisuanxiaoji_w("WW");//小计
   }
  //删除并重新技术总费用
  function delSelNode222(ptable,name,type,hjtype){
  	
		var pbody=ptable.getElementsByTagName("TBODY")[0];
		var len=pbody.children.length;
		var nodevalue="";
		for(var i=len-1;i>=0;i--){
			if(pbody.children[i].firstChild.firstChild.checked==true){//ids
				pbody.deleteRow(i);
			}
		}
		xuhaonum(name);
		if(type=="W"){
			jisuanxiaoji_w(hjtype);
		}else if(type=="G"){
			jisuanxiaoji_g(hjtype);
		}else if(type=="J"){
			jisuanxiaoji_b(hjtype);
		}
   }

/** 显示隐藏层 */
function showdivpage(obj){
	var divshoww=document.getElementById("divshoww");
	var divshowg=document.getElementById("divshowg");
	var divshowj=document.getElementById("divshowj");
	var divshowb1=document.getElementById("divshowb1");
	var divshowb2=document.getElementById("divshowb2");
	var divshowpath=document.getElementById("divshowpath");//提交路径
	if(obj.value=="J"){//保养
		divshoww.style.display="none";
		divshowg.style.display="none";
		divshowj.style.display="inline";
		divshowpath.style.display="none";
		document.getElementById("submitpath").value="W";//保养提交路径默认为分部长
	}else if(obj.value=="G"){//改造
		divshoww.style.display="none";
		divshowg.style.display="inline";
		divshowj.style.display="none";
		divshowpath.style.display="inline";
		document.getElementById("submitpath").value="";
	}else if(obj.value=="W"){//维修
		divshoww.style.display="inline";
		divshowg.style.display="none";
		divshowj.style.display="none";
		divshowpath.style.display="inline";
		document.getElementById("submitpath").value="";
	}else if(obj.value=="1"){//保养-授权合同
		divshowb1.style.display="inline";
		divshowb2.style.display="none";
	}else if(obj.value=="2"){//保养-劳务协议
		divshowb1.style.display="none";
		divshowb2.style.display="inline";
	}
}
//保养设置申请正文的内容
function setBApplyContent(obj){
	 if(obj.value=="1"){//保养-授权合同
		document.getElementById("BApplyContent").value=content;
	}else if(obj.value=="2"){//保养-劳务协议
		document.getElementById("BApplyContent").value=content1;
	}
}

/** 弹出施工单位窗口 */
function openNewWindow2(type,typestr){
	var url='query/Search.jsp?path=/XJSCRM/SearchEngEpiCustomerAction.do?method=toSearchRecord_CustName';
	var obj = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
   	if(obj){
   		if(type=="W"){//维护
   			if(typestr=='W'){//合同申请
   				document.getElementById("WConstructId").value=obj.id;
   				document.getElementById("WConstructName").value=obj.name;
   			}else if(typestr=='D'){//吊装
   				document.getElementById("wDSCustId").value=obj.id;
   				document.getElementById("wDSCustName").value=obj.name;
   			}else if(typestr=='B'){//搭棚
   				document.getElementById("wBCustId").value=obj.id;
   				document.getElementById("wBCustName").value=obj.name;
   			}else if(typestr=='T'){//运输
   				document.getElementById("wTCustId").value=obj.id;
   				document.getElementById("wTCustName").value=obj.name;
   			}else if(typestr=='E'){//土建
   				document.getElementById("wECustId").value=obj.id;
   				document.getElementById("wECustName").value=obj.name;
   			}
   		}else if(type=="G"){//改造
   			if(typestr=='W'){//合同申请
   				document.getElementById("GConstructId").value=obj.id;
   				document.getElementById("GConstructName").value=obj.name;
   			}else if(typestr=='D'){//吊装
   				document.getElementById("gDSCustId").value=obj.id;
   				document.getElementById("gDSCustName").value=obj.name;
   			}else if(typestr=='B'){//搭棚
   				document.getElementById("gBCustId").value=obj.id;
   				document.getElementById("gBCustName").value=obj.name;
   			}else if(typestr=='T'){//运输
   				document.getElementById("gTCustId").value=obj.id;
   				document.getElementById("gTCustName").value=obj.name;
   			}else if(typestr=='E'){//土建
   				document.getElementById("gECustId").value=obj.id;
   				document.getElementById("gECustName").value=obj.name;
   			}
   		}else if(type=="J"){//保养
   			if(typestr=='S'){//授权合同
   				document.getElementById("BConstructId").value=obj.id;
   				document.getElementById("BConstructName").value=obj.name;
   			}else if(typestr=='L'){//劳务协议
   				document.getElementById("BConstructId1").value=obj.id;
   				document.getElementById("BConstructName1").value=obj.name;
   			}
   		}
   	}
}
/** 弹出合同窗口 */
function openNewWindow(obj){
	if(obj=='B'){//保养
		sendServicingContractDetailB();
	}else if(obj=='G'){//改造
		openwindowReturnValueCustomer('query/Search.jsp?path=/XJSCRM/SearchEngEpiCustomerAction.do?method=toSearchRecord_GaiZao','G');
	}else if(obj=='W'){//维修
		openwindowReturnValueCustomer('query/Search.jsp?path=/XJSCRM/SearchEngEpiCustomerAction.do?method=toSearchRecord_WeiXiu','W');
	}
}
function openwindowReturnValueCustomer(url,busstype){
   var obj = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
   if(obj){
   		if(busstype=="G"){
   			document.getElementById("GSoureConctractId").value=obj.contractid;
   			document.getElementById("GCustID").value=obj.custid;
   			document.getElementById("GCustName").value=obj.custname;
   			document.getElementById("divgProjectName").innerText=obj.custname;//吊装
   			document.getElementById("divgBProjectName").innerText=obj.custname;//搭棚
   			document.getElementById("divgTProjectName").innerText=obj.custname;//运输
   			document.getElementById("divgEProjectName").innerText=obj.custname;//土建
   			var billno=obj.billno;//改造合同流水号
   			document.getElementById("Gbillno").value=billno;
   			
   			sendServicingContractDetailG(billno);
   		}else if(busstype=="W"){
   			document.getElementById("WSoureConctractId").value=obj.contractid;
   			document.getElementById("WCustID").value=obj.custid;
   			document.getElementById("WCustName").value=obj.custname;
   			document.getElementById("divwProjectName").innerText=obj.custname;//吊装
   			document.getElementById("divwBProjectName").innerText=obj.custname;//搭棚
   			document.getElementById("divwTProjectName").innerText=obj.custname;//运输
   			document.getElementById("divwEProjectName").innerText=obj.custname;//土建
   			var billno=obj.billno;//维修合同流水号
   			document.getElementById("Wbillno").value=billno;
   			
   			sendServicingContractDetailW(billno);
   		}
   }
}
 /** ajax 取出维修合同的明细信息 */
var XMLHttpReq2 = false;
  //创建XMLHttpRequest对象       
function createXMLHttpRequest2() {
  if(window.XMLHttpRequest) { //Mozilla 浏览器
   XMLHttpReq2 = new XMLHttpRequest();
  }else if (window.ActiveXObject) { // IE浏览器
   XMLHttpReq2 = new ActiveXObject("Microsoft.XMLHTTP");
  }
 }

 /** 检查施工单位 */
 function checkConstructId(){
	var issubmit=true;
	var BussType=document.getElementById("BussType").value;
	if(BussType=="W"){
		var constructId=document.getElementById("WConstructId").value;//施工单位
		if(constructId.trim()==""){
	 		issubmit=confirm("维修委托合同申请,施工单位尚未填写,是否继续保存！\n");
	 	}
	}else if(BussType=="G"){
		var constructId=document.getElementById("GConstructId").value;//施工单位
		if(constructId.trim()==""){
	 		issubmit=confirm("改造委托合同申请,施工单位尚未填写,是否继续保存！\n");
	 	}
	}
	return issubmit;
}
/** 保存时检查是否填写完整 */
 function checksaveinfo(error){
 	var BussType=document.getElementById("BussType").value;
 	var ApplyDate=document.getElementById("ApplyDate").value;//申请日期
	if(ApplyDate.trim()==""){
		error="申请日期不能为空!\n";
	}else{
	 	if(BussType=="W"){
		 	var soureConctractId=document.getElementById("WSoureConctractId").value;//合同号
		 	//var constructId=document.getElementById("WConstructId").value;//施工单位
		 	if(soureConctractId.trim()==""){
		 		error+="合同号不能为空！\n";
		 	//}else if(constructId.trim()==""){
		 	//	error+="维修委托合同申请,施工单位不能为空！\n";
		 	}else{
		 		error+=wcheckweihuinfo2("","WW","");//委托工料费的构成
		 		if(error==""){
		 			error+=wcheckweihuinfo2("","D","W");//吊装
		 		}
		 		if(error==""){
		 			error+=wcheckweihuinfo2("","B","W");//搭棚
		 		}
		 		if(error==""){
		 			error+=wcheckweihuinfo2("","T","W");//运输
		 		}
		 		if(error==""){
		 			error+=wcheckweihuinfo2("","E","W");//土建
		 		}
		 	}
	 	}else if(BussType=="G"){
	 		//var applyContent=document.getElementById("GApplyContent").value;//申请正文
	 		//if(applyContent.trim()==""){
	 		//	error+="申请正文不能为空！\n";
	 		//}
	 		var soureConctractId=document.getElementById("GSoureConctractId").value;//合同号
		 	//var constructId=document.getElementById("GConstructId").value;//施工单位
		 	if(soureConctractId.trim()==""){
		 		error+="合同号不能为空！\n";
		 	//}else if(constructId.trim()==""){
		 	//	error+="改造委托合同申请,施工单位不能为空！\n";
		 	}else{
		 		error+=gcheckweihuinfo2("","D","G");//吊装
		 		if(error==""){
		 			error+=gcheckweihuinfo2("","B","G");//搭棚
		 		}
		 		if(error==""){
		 			error+=gcheckweihuinfo2("","T","G");//运输
		 		}
		 		if(error==""){
		 			error+=gcheckweihuinfo2("","E","G");//土建
		 		}
		 	}
	 	}else if(BussType=="J"){
	 		var appType=document.getElementById("AppType").value;
	 		var applyContent=document.getElementById("BApplyContent").value;//申请正文
	 		//var applyContent1=document.getElementById("BApplyContent1").value;//申请正文
	 		var saftyDesc=document.getElementById("BSaftyDesc").value;//安全责任协议书
	 		var divisorDesc=document.getElementById("BDivisorDesc").value;//反贿赂公约
	 		
	 		if(applyContent.trim()==""){
	 			error+="申请正文不能为空！\n";
	 		}
	 		if(saftyDesc.trim()==""){
	 			error+="安全责任协议书不能为空！\n";
	 		}
	 		if(divisorDesc.trim()==""){
	 			error+="反贿赂公约不能为空！\n";
	 		}
	 		var apptype=document.getElementById("AppType").value;
			if(apptype=="1"){//授权合同
				var constructId=document.getElementById("BConstructId").value;//外包单位
				if(constructId==""){
					error+="外包单位不能为空！\n";
				}else{
					error+=bcheckweihuinfo("","S","B");
				}
			}else if(apptype=="2"){//劳务协议
				var constructId=document.getElementById("BConstructId1").value;//外包单位
				if(constructId==""){
					error+="外包单位不能为空！\n";
				}else{
					error+=bcheckweihuinfo("","L","B");
					if(error==""){
						error+=bcheckweihuinfo("","W","B");
					}
				}
			}
	 		
	 	}
	}
	return error;
 }
 
 /**=================== 维修报价 start ===========================*/
 //发送请求函数
 function sendServicingContractDetailW(billno) {
 	var url = '/XJSCRM/SearchEngEpiCustomerAction.do?method=toServicingContractDetailW';
		url +='&wbillno='+billno;
  	createXMLHttpRequest2();
 	XMLHttpReq2.open("post", url, false);
  	XMLHttpReq2.onreadystatechange = processServicingContractDetailW;//指定响应函数
  	XMLHttpReq2.send(null);  // 发送请求
 }
 // 处理返回信息函数
 function processServicingContractDetailW() {
     	if (XMLHttpReq2.readyState == 4) { // 判断对象状态
         	if (XMLHttpReq2.status == 200) { // 信息已经成功返回，开始处理信息        	
         		var xmlDOM=XMLHttpReq2.responseXML;

            	var elevatorid=xmlDOM.getElementsByTagName("elevatorid");//[0].firstChild.data;
            	var floor=xmlDOM.getElementsByTagName("floor");
            	var stage=xmlDOM.getElementsByTagName("stage");
            	var door=xmlDOM.getElementsByTagName("door");
            	var high=xmlDOM.getElementsByTagName("high");
            	var num=xmlDOM.getElementsByTagName("num");
            	var angle=xmlDOM.getElementsByTagName("angle");
            	var amt=xmlDOM.getElementsByTagName("amt");
            	var detailrowid=xmlDOM.getElementsByTagName("detailrowid");
            	var quoteelevatorid=xmlDOM.getElementsByTagName("quoteelevatorid");
            	//先删除已经存在的电梯型号信息
            	delSelNode22(adjtb_wd);
            	var amtsum=0;
            	for(var i=0;i<elevatorid.length;i++){
            		var eleid=elevatorid[i].firstChild.data;
            		var floorstr=floor[i].firstChild.data;
            		var stagestr=stage[i].firstChild.data;
            		var doorstr=door[i].firstChild.data;
            		var highstr=high[i].firstChild.data;
            		var numstr=num[i].firstChild.data;
            		var eanglestr=angle[i].firstChild.data;
            		var amtstr=amt[i].firstChild.data;
            		var rowidstr=detailrowid[i].firstChild.data;
            		var quotestr=quoteelevatorid[i].firstChild.data;
            		
            		AddRow_WD(eleid,floorstr,stagestr,doorstr,highstr,numstr,eanglestr,rowidstr,quotestr);
            		
            		amtsum=parseFloat(amtsum)+parseFloat(amtstr);
            	}
            	//合同总价
				amtsum=parseFloat(amtsum).toFixed(2);
				var contractsum=document.getElementById("contractsum");
				contractsum.value=amtsum; //隐藏的合同总价	
				//document.getElementById("div_contractsum").innerText=amtsum; //合同总价
				
				//项目委托价格构成
				projectPriceConstitute("0");
				
            	var engcontent=xmlDOM.getElementsByTagName("engcontent");
            	var qty=xmlDOM.getElementsByTagName("qty");
            	var personprice=xmlDOM.getElementsByTagName("personprice");
            	var stuffprice=xmlDOM.getElementsByTagName("stuffprice");
            	var conrowid=xmlDOM.getElementsByTagName("conrowid");
            	var prodinfo=xmlDOM.getElementsByTagName("prodinfo");
            	//删除已经存在的委托工料费的构成
            	delSelNode23(adjtb_ww,0);
            	for(var j=0;j<engcontent.length;j++){
            		var engstrt=engcontent[j].firstChild.data;
            		var qtystr=qty[j].firstChild.data;
            		var perstre=personprice[j].firstChild.data;
            		var stustr=stuffprice[j].firstChild.data;
            		var conrowidstr=conrowid[j].firstChild.text;
            		var prodinfostr="";
            		if(prodinfo[j].firstChild){
            			prodinfostr=prodinfo[j].firstChild.text;
            		}

            		AddRow_WD2(engstrt,qtystr,perstre,stustr,conrowidstr,prodinfostr);
            	}
            	
         	} else { //页面不正常
               window.alert("您所请求的页面有异常。");
         	}
       }
    }
   /** 添加合同电梯明细信息 */
  function AddRow_WD(elevatorid,floor,stage,door,high,numkk,angle,rowidstr,quotestr){
		var adjtb_w=document.getElementById("adjtb_wd");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);

		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.innerHTML="";
		
		//电梯型号
		var name0="welevatorid";
		var name00="wdetailrowid";
		var name000="wquoteelevatorid";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.innerHTML=elevatorid+"<input type=\'hidden\' name=\'"+name0+"\' value=\'"+elevatorid+"\' /><input type=\'hidden\' name=\'"+name00+"\' value=\'"+rowidstr+"\' /><input type=\'hidden\' name=\'"+name000+"\' value=\'"+quotestr+"\' />";
		//层站门
		var name1="wfloor";
		var name2="wstage";
		var name3="wdoor";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML=floor+"/"+stage+"/"+door+"<input type=\'hidden\' name=\'"+name1+"\' value=\'"+floor+"\' /><input type=\'hidden\' name=\'"+name2+"\' value=\'"+stage+"\' /><input type=\'hidden\' name=\'"+name3+"\' value=\'"+door+"\' />";
		//提升高度
		var name4="whigh";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.align="right";
		ince3.innerHTML=high+"&nbsp;<input type=\'hidden\' name=\'"+name4+"\' value=\'"+high+"\' />";
		//倾斜角度
		var name5="wangle";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.align="right";
		ince4.innerHTML=angle+"&nbsp;<input type=\'hidden\' name=\'"+name5+"\' value=\'"+angle+"\' />";
		//数量
		var name6="wnum";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.align="right";
		ince5.innerHTML=numkk+"&nbsp;<input type=\'hidden\' name=\'"+name6+"\' value=\'"+numkk+"\' />";
 }
 
 /** 委托工料费的构成 */
   function AddRow_WD2(engstrt,qtystr,perstre,stustr,conrowidstr,prodinfostr){
		var adjtb_w=document.getElementById("adjtb_ww");
		var num=adjtb_w.rows.length-1;

		adjtb_w.insertRow(num);
		
		//复选框
		var name0="Wnodesw";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Wdivorderw";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'></div>";
		//项目
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'wengcontent\' value=\'"+engstrt+"\' class=\'default_input\' size=\'50\' /><input type=\'hidden\' name=\'wconrowid\' value=\'"+conrowidstr+"\' />";
		//数量
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.align="right";
		ince3.innerHTML="<input type=\'text\' name=\'wqty\' value=\'"+qtystr+"\' class=\'default_input\' onkeypress=\'f_check_number2();\'/>";
		//费用
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.align="right";
		ince4.innerHTML="<input type=\'text\' name=\'wpersonprice\' value=\'"+perstre+"\' class=\'default_input\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_w('WW')\" />";
		//材料费
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.align="right";
		ince5.innerHTML="<input type=\'text\' name=\'wstuffprice\' value=\'"+stustr+"\' class=\'default_input\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_w('WW')\" /><input type=\'hidden\' name=\'wprodinfo\' value=\'"+prodinfostr+"\' />";
 		
		xuhaonum(name1);
		jisuanxiaoji_w("WW");//小计
 }
 function AddRow_WW(tableid){
 	var errorstr=wcheckweihuinfo2("","WW","");//委托工料费的构成
 	if(errorstr==""){
 		AddRow_WD2("","","","","","");
 	}else{
 		alert(errorstr);
 	}
 }
  /** 吊装 */ 
  function AddRow_W(tableid){
  	var errorstr=wcheckweihuinfo2("","D","");
  	if(errorstr==""){
		var adjtb_w=document.getElementById("adjtb_w");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Wnodes";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Wdivorder";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'></div>";
		//层
		var name2="wDfloor";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' size=\'4\' onkeyup=\"setStageValue(this,'wDstage')\" onkeypress=\'f_check_number2();\' >";
		//站
		var name3="wDstage";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'4\' onkeypress=\'f_check_number2();\'>";
		//电梯载重（kg）
		var name4="wDZzl";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' onkeypress=\'f_check_number3();\' size=\'10\' class=\'default_input\'>";
		//吊装地点
		var name5="wDSuspendAddress";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\'>";
		//吊装项目
		var name6="wDSuspendItem";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' >";
		//吊装箱头
		var name7="wDSuspendBox";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.innerHTML="<input type=\'text\' name=\'"+name7+"\' class=\'default_input\' >";
		//计算公式（原吊装发包价）
		var name8="wDCountExp";
		var ince8=adjtb_w.rows(num).insertCell(8);
		//ince8.innerHTML="<input type=\'text\' name=\'wCountExp\' class=\'default_input\' >";
		ince8.innerHTML="<textarea name=\'"+name8+"\' rows=\'1\' cols=\'30\'>";
		//吊装台数
		var name9="wDSuspendNum";
		var ince9=adjtb_w.rows(num).insertCell(9);
		ince9.align="right";
		ince9.innerHTML="<input type=\'text\' name=\'"+name9+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_w('D')\" size=\'6\'>";
		//吊装费用
		var name10="wDSuspendPrice";
		var ince10=adjtb_w.rows(num).insertCell(10);
		ince10.align="right";
		ince10.innerHTML="<input type=\'text\' name=\'"+name10+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_w('D')\" size=\'10\'>";

		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
  /** 搭棚 */ 
  function AddRow_W1(tableid){
	  var errorstr=wcheckweihuinfo2("","B","");
	  if(errorstr==""){
  		var adjtb_w=document.getElementById("adjtb_w1");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Wnodes1";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Wdivorder1";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//层
		var name2="wBfloor";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' size=\'4\' onkeyup=\"setStageValue(this,'wBstage')\" onkeypress=\'f_check_number2();\' >";
		//站
		var name3="wBstage";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'4\' onkeypress=\'f_check_number2();\'>";
		//电梯载重（kg）
		var name4="wBZzl";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' onkeypress=\'f_check_number3();\' class=\'default_input\' size=\'10\'>";
		//搭棚地点
		var name5="wBBuildAddress";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\' size=\'40\'>";
		//搭棚项目
		var name6="wBBuildItem";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' size=\'40\' >";
		//井道总高（米）
		var name7="wBJdH";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.innerHTML="<input type=\'text\' name=\'"+name7+"\' class=\'default_input\' size=\'10\' onkeypress=\'f_check_number3();\'>"
		//搭棚台数
		var name8="wBBuildNum";
		var ince8=adjtb_w.rows(num).insertCell(8);
		ince8.align="right";
		ince8.innerHTML="<input type=\'text\' name=\'"+name8+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_w('B')\" size=\'6\'>";
		//搭棚费用
		var name9="wBBuildPrice";
		var ince9=adjtb_w.rows(num).insertCell(9);
		ince9.align="right";
		ince9.innerHTML="<input type=\'text\' name=\'"+name9+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_w('B')\" size=\'10\'>";

		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
  /** 运输 */
  function AddRow_W2(tableid){
	  var errorstr=wcheckweihuinfo2("","T","");
	  if(errorstr==""){
  		var adjtb_w=document.getElementById("adjtb_w2");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Wnodes2";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Wdivorder2";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//运输地点
		var name2="wTransAddress";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' size=\'40\' >";
		//运输项目
		var name3="wTransItem";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'40\'>";
		//发出车型
		var name4="wTSendType";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' class=\'default_input\' size=\'40\' >";
		//运输台数
		var name5="wTransNum";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.align="right";
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_w('T')\" size=\'6\'>";
		//运输费用
		var name6="wTransPrice";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.align="right";
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_w('T')\" size=\'10\'>";

		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
   /** 土建 */ 
  function AddRow_W3(tableid){
	  var errorstr=wcheckweihuinfo2("","E","");
	  if(errorstr==""){
  		var adjtb_w=document.getElementById("adjtb_w3");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Wnodes1e";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Wdivorder1e";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//层
		var name2="wEfloor";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' size=\'4\' onkeyup=\"setStageValue(this,'wEstage')\" onkeypress=\'f_check_number2();\' >";
		//站
		var name3="wEstage";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'4\' onkeypress=\'f_check_number2();\'>";
		//电梯载重（kg）
		var name4="wEZzl";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' onkeypress=\'f_check_number3();\' class=\'default_input\' size=\'10\'>";
		//土建地点
		var name5="wEBuildAddress";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\' size=\'40\'>";
		//土建项目
		var name6="wEBuildItem";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' size=\'40\' >";
		//井道总高（米）
		var name7="wEJdH";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.innerHTML="<input type=\'text\' name=\'"+name7+"\' class=\'default_input\' size=\'10\' onkeypress=\'f_check_number3();\'>"
		//土建台数
		var name8="wEBuildNum";
		var ince8=adjtb_w.rows(num).insertCell(8);
		ince8.align="right";
		ince8.innerHTML="<input type=\'text\' name=\'"+name8+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_w('E')\" size=\'6\'>";
		//土建费用
		var name9="wEBuildPrice";
		var ince9=adjtb_w.rows(num).insertCell(9);
		ince9.align="right";
		ince9.innerHTML="<input type=\'text\' name=\'"+name9+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_w('E')\" size=\'10\'>";

		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
  
   /** 检查明细信息,如果，施工方存在，就必须填写明细信息 */
 function wcheckweihuinfo2(error,typestr,showtype){
 	if(typestr=="WW"){
 		var Wdivorderw=document.getElementsByName("Wdivorderw");//序号
		var wengcontent=document.getElementsByName("wengcontent");//项目
		var wqty=document.getElementsByName("wqty"); //数量
		var wpersonprice=document.getElementsByName("wpersonprice");//费用
		var wstuffprice=document.getElementsByName("wstuffprice");//材料费
		
		for(var i=0;i<Wdivorderw.length;i++){
			var xuhao=Wdivorderw[i].innerText;
			if(wengcontent[i].value.trim()==""){
	 			error+="  序号"+xuhao+" 项目不可以为空！\n";
	 		}
			if(wqty[i].value.trim()==""){
	 			error+="  序号"+xuhao+" 数量不可以为空！\n";
	 		}else if(isNaN(wqty[i].value.trim())){
	 			error+="  序号"+xuhao+" 数量必须为数字！\n";
	 		}
	 		if(wpersonprice[i].value.trim()==""){
	 			error+="  序号"+xuhao+" 费用不可以为空！\n";
	 		}else if(isNaN(wpersonprice[i].value.trim())){
	 			error+="  序号"+xuhao+" 费用必须为数字！\n";
	 		}
	 		if(wstuffprice[i].value.trim()==""){
	 			error+="  序号"+xuhao+" 材料费不可以为空！\n";
	 		}else if(isNaN(wstuffprice[i].value.trim())){
	 			error+="  序号"+xuhao+" 材料费必须为数字！\n";
	 		}
		}
		if(error!=""){
			error="委托工料费的构成:\n"+error;
		}
 	}else if(typestr=='D'){//吊装
		var wSCustId=document.getElementById("wDSCustId").value.trim();
		if(wSCustId!=""){
 			if(document.getElementsByName("Wdivorder")==null || document.getElementsByName("Wdivorder").length==0){
 				if(showtype=="W"){
 					error="吊装发包申请，请添加明细信息！";
 				}
 			}else{
 				var divorder=document.getElementsByName("Wdivorder");
 				
 				var wfloor=document.getElementsByName("wDfloor");//层
			 	var wstage=document.getElementsByName("wDstage");//站
			 	var wZzl=document.getElementsByName("wDZzl");//电梯载重（kg）
			 	var wSuspendAddress=document.getElementsByName("wDSuspendAddress");//吊装地点
			 	var wSuspendItem=document.getElementsByName("wDSuspendItem");//吊装项目
			 	var wSuspendBox=document.getElementsByName("wDSuspendBox");//吊装箱头
			 	var wCountExp=document.getElementsByName("wDCountExp");//计算公式（原吊装发包价）
			 	var wSuspendNum=document.getElementsByName("wDSuspendNum");//吊装台数
			 	var wSuspendPrice=document.getElementsByName("wDSuspendPrice");//吊装费用
 				
 				for(var i=0;i<divorder.length;i++){
 					var xuhao=divorder[i].innerText;
 					if(wfloor[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 层不可以为空！\n";
			 		}else if(isNaN(wfloor[i].value.trim())){
			 			error+="吊装发包申请，序号"+xuhao+" 层必须为数字！\n";
			 		}
			 		if(wstage[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 站不可以为空！\n";
			 		}else if(isNaN(wstage[i].value)){
			 			error+="吊装发包申请，序号"+xuhao+" 站必须为数字！\n";
			 		}
			 		if(wZzl[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 电梯载重不可以为空！\n";
			 		}else if(isNaN(wZzl[i].value.trim())){
			 			error+="吊装发包申请，序号"+xuhao+" 电梯载重必须为数字！\n";
			 		}
			 		if(wSuspendAddress[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装地点不可以为空！\n";
			 		}
			 		if(wSuspendItem[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装项目不可以为空！\n";
			 		}
			 		if(wSuspendBox[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装箱头不可以为空！\n";
			 		}
			 		if(wCountExp[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 计算公式不可以为空！\n";
			 		}
			 		if(wSuspendNum[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装台数不可以为空！\n";
			 		}else if(isNaN(wSuspendNum[i].value.trim())){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装台数必须为数字！\n";
			 		}else if(parseFloat(wSuspendNum[i].value.trim())<=0){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装台数必须大于零！\n";
			 		}
			 		if(wSuspendPrice[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装费用不可以为空！\n";
			 		}else if(isNaN(wSuspendPrice[i].value.trim())){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装费用必须为数字！\n";
			 		}else if(parseFloat(wSuspendPrice[i].value.trim())<=0){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装费用必须大于零！\n";
			 		}
 				}
 			}
		}else{
			if(showtype==""){
				error+="请先选择施工方！";
			}
		}
 	}else if(typestr=='B'){//搭棚
 		var wBCustId=document.getElementById("wBCustId").value.trim();
		if(wBCustId!=""){			
 			if(document.getElementsByName("Wdivorder1")==null || document.getElementsByName("Wdivorder1").length==0){
 				if(showtype=="W"){
 					error="搭棚发包申请，请添加明细信息！";
 				}
 			}else{
 				var divorder=document.getElementsByName("Wdivorder1");
 				
 				var wBfloor=document.getElementsByName("wBfloor");//层
			 	var wBstage=document.getElementsByName("wBstage");//站
			 	var wBZzl=document.getElementsByName("wBZzl");//电梯载重（kg）
			 	var wBBuildAddress=document.getElementsByName("wBBuildAddress");//搭棚地点
			 	var wBBuildItem=document.getElementsByName("wBBuildItem");//搭棚项目
			 	var wBJdH=document.getElementsByName("wBJdH");//井道总高
			 	var wBBuildNum=document.getElementsByName("wBBuildNum");//搭棚台数
			 	var wBBuildPrice=document.getElementsByName("wBBuildPrice");//搭棚费用
 				
 				for(var i=0;i<divorder.length;i++){
 					var xuhao=divorder[i].innerText;
 					if(wBfloor[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 层不可以为空！\n";
			 		}else if(isNaN(wBfloor[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 层必须为数字！\n";
			 		}
			 		if(wBstage[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 站不可以为空！\n";
			 		}else if(isNaN(wBstage[i].value)){
			 			error+="搭棚发包申请，序号"+xuhao+" 站必须为数字！\n";
			 		}
			 		if(wBZzl[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 电梯载重不可以为空！\n";
			 		}else if(isNaN(wBZzl[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 电梯载重必须为数字！\n";
			 		}
			 		if(wBBuildAddress[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚地点不可以为空！\n";
			 		}
			 		if(wBBuildItem[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚项目不可以为空！\n";
			 		}
			 		if(wBJdH[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 井道总高不可以为空！\n";
			 		}else if(isNaN(wBJdH[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 井道总高必须为数字！\n";
			 		}
			 		if(wBBuildNum[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚台数不可以为空！\n";
			 		}else if(isNaN(wBBuildNum[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚台数必须为数字！\n";
			 		}else if(parseFloat(wBBuildNum[i].value.trim())<=0){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚台数必须大于零！\n";
			 		}
			 		if(wBBuildPrice[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚费用不可以为空！\n";
			 		}else if(isNaN(wBBuildPrice[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚费用必须为数字！\n";
			 		}else if(parseFloat(wBBuildPrice[i].value.trim())<=0){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚费用必须大于零！\n";
			 		}
 				}
 			}
		}else{
			if(showtype==""){
				error+="请先选择施工方！";
			}
		}
 	}else if(typestr=='T'){//运输
 		 var wTCustId=document.getElementById("wTCustId").value.trim();
		if(wTCustId!=""){		
 			if(document.getElementsByName("Wdivorder2")==null || document.getElementsByName("Wdivorder2").length==0){
 				if(showtype=="W"){
 					error="运输发包申请，请添加明细信息！";
 				}
 			}else{
 				var divorder=document.getElementsByName("Wdivorder2");
 				
 				var wTransAddress=document.getElementsByName("wTransAddress");//运输地点
			 	var wTransItem=document.getElementsByName("wTransItem");//运输项目
			 	var wTSendType=document.getElementsByName("wTSendType");//发出车型
			 	var wTransNum=document.getElementsByName("wTransNum");//运输台数
			 	var wTransPrice=document.getElementsByName("wTransPrice");//运输费用
 				
 				for(var i=0;i<divorder.length;i++){
 					var xuhao=divorder[i].innerText;
			 		if(wTransAddress[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 运输地点不可以为空！\n";
			 		}
			 		if(wTransItem[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 运输项目不可以为空！\n";
			 		}
			 		if(wTSendType[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 发出车型不可以为空！\n";
			 		}
			 		if(wTransNum[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 运输台数不可以为空！\n";
			 		}else if(isNaN(wTransNum[i].value.trim())){
			 			error+="运输发包申请，序号"+xuhao+" 运输台数必须为数字！\n";
			 		}else if(parseFloat(wTransNum[i].value.trim())<=0){
			 			error+="运输发包申请，序号"+xuhao+" 运输台数必须大于零！\n";
			 		}
			 		if(wTransPrice[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 运输费用不可以为空！\n";
			 		}else if(isNaN(wTransPrice[i].value.trim())){
			 			error+="运输发包申请，序号"+xuhao+" 运输费用必须为数字！\n";
			 		}else if(parseFloat(wTransPrice[i].value.trim())<=0){
			 			error+="运输发包申请，序号"+xuhao+" 运输费用必须大于零！\n";
			 		}
 				}
 			}
		}else{
			if(showtype==""){
				error+="请先选择施工方！";
			}
		}
 	}else if(typestr=='E'){//土建
 		var wECustId=document.getElementById("wECustId").value.trim();
		if(wECustId!=""){
 			if(document.getElementsByName("Wdivorder1e")==null || document.getElementsByName("Wdivorder1e").length==0){
 				if(showtype=="W"){
 					error="土建发包申请，请添加明细信息！";
 				}
 			}else{
 				var divorder=document.getElementsByName("Wdivorder1e");
 				
 				var wEfloor=document.getElementsByName("wEfloor");//层
			 	var wEstage=document.getElementsByName("wEstage");//站
			 	var wEZzl=document.getElementsByName("wEZzl");//电梯载重（kg）
			 	var wEBuildAddress=document.getElementsByName("wEBuildAddress");//土建地点
			 	var wEBuildItem=document.getElementsByName("wEBuildItem");//土建项目
			 	var wEJdH=document.getElementsByName("wEJdH");//井道总高
			 	var wEBuildNum=document.getElementsByName("wEBuildNum");//土建台数
			 	var wEBuildPrice=document.getElementsByName("wEBuildPrice");//土建费用
 				
 				for(var i=0;i<divorder.length;i++){
 					var xuhao=divorder[i].innerText;
 					if(wEfloor[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 层不可以为空！\n";
			 		}else if(isNaN(wEfloor[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 层必须为数字！\n";
			 		}
			 		if(wEstage[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 站不可以为空！\n";
			 		}else if(isNaN(wEstage[i].value)){
			 			error+="土建发包申请，序号"+xuhao+" 站必须为数字！\n";
			 		}
			 		if(wEZzl[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 电梯载重不可以为空！\n";
			 		}else if(isNaN(wEZzl[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 电梯载重必须为数字！\n";
			 		}
			 		if(wEBuildAddress[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 土建地点不可以为空！\n";
			 		}
			 		if(wEBuildItem[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 土建项目不可以为空！\n";
			 		}
			 		if(wEJdH[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 井道总高不可以为空！\n";
			 		}else if(isNaN(wEJdH[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 井道总高必须为数字！\n";
			 		}
			 		if(wEBuildNum[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 土建台数不可以为空！\n";
			 		}else if(isNaN(wEBuildNum[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 土建台数必须为数字！\n";
			 		}else if(parseFloat(wEBuildNum[i].value.trim())<=0){
			 			error+="土建发包申请，序号"+xuhao+" 土建台数必须大于零！\n";
			 		}
			 		if(wEBuildPrice[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 土建费用不可以为空！\n";
			 		}else if(isNaN(wEBuildPrice[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 土建费用必须为数字！\n";
			 		}else if(parseFloat(wEBuildPrice[i].value.trim())<=0){
			 			error+="土建发包申请，序号"+xuhao+" 土建费用必须大于零！\n";
			 		}
 				}
 			}
		}else{
			if(showtype==""){
				error+="请先选择施工方！";
			}
		}
 	}

 	return error;
 }
  /** 总台数 总费用 parseFloat();parseInt();isNaN();检查是否是字符 */
 function jisuanxiaoji_w(typestr){
 	
 	var prosum=0;
 	var costsum=0;
 	
 	if(typestr=='WW'){ 		
 		var personprice=document.getElementsByName("wpersonprice"); //费用
 		var stuffprice=document.getElementsByName("wstuffprice");//材料费
		for(var i=0;i<personprice.length;i++){
			var perstre=personprice[i].value;
			if(perstre==""){
				perstre="0";
			}
			var stustr=stuffprice[i].value;
			if(stustr==""){
				stustr="0";
			}
			prosum=parseFloat(prosum)+parseFloat(perstre)+parseFloat(stustr);
		}
 		//委托费用 保留2位小数
    	document.getElementById("divwxiaoji").innerText=Math.round(parseFloat(prosum)*100)/100;
 	}else if(typestr=='D'){
 		var name0="divwProSum";
 		var name1="divwProCostSum";
 		var name2="wDSuspendNum";
 		var name3="wDSuspendPrice";
 		var divwProSum=document.getElementById(name0);//吊装总台数 D
 		var divwProCostSum=document.getElementById(name1);//吊装总费用
 		var wDAllCost=document.getElementById("wDAllCost");
 		if(document.getElementsByName(name2)==null || document.getElementsByName(name2).length==0){
 			
 		}else{
 			var wSuspendNum=document.getElementsByName(name2);
 			var wSuspendPrice=document.getElementsByName(name3);
 			for(var i=0;i<wSuspendNum.length;i++){
 				var num=wSuspendNum[i].value;
		 		if(num==""){
		 			num="0";
		 		}
		 		var price=wSuspendPrice[i].value;
		 		if(price==""){
		 			price="0";
		 		}
		 		prosum=parseFloat(prosum)+parseFloat(num);
		 		costsum=parseFloat(costsum)+parseFloat(price);
 			}
 			
 		}
 		divwProSum.innerHTML=prosum;
 		divwProCostSum.innerHTML=parseFloat(costsum).toFixed(2);
 		wDAllCost.value=parseFloat(costsum).toFixed(2);
 	}else if(typestr=='B'){
 		var name0="divwBProSum";
 		var name1="divwBProCostSum";
 		var name2="wBBuildNum";
 		var name3="wBBuildPrice";
 		var divwBProSum=document.getElementById(name0);//搭棚总台数 B
 		var divwBProCostSum=document.getElementById(name1);//搭棚总费用
 		var wBAllCost=document.getElementById("wBAllCost");
 		if(document.getElementsByName(name2)==null || document.getElementsByName(name2).length==0){
 			
 		}else{
 			var wBBuildNum=document.getElementsByName(name2);
 			var wBBuildPrice=document.getElementsByName(name3);
 			for(var i=0;i<wBBuildNum.length;i++){
 				var num=wBBuildNum[i].value;
		 		if(num==""){
		 			num="0";
		 		}
		 		var price=wBBuildPrice[i].value;
		 		if(price==""){
		 			price="0";
		 		}
		 		prosum=parseFloat(prosum)+parseFloat(num);
		 		costsum=parseFloat(costsum)+parseFloat(price);
 			}
 			
 		}
 		divwBProSum.innerHTML=prosum;
 		divwBProCostSum.innerHTML=parseFloat(costsum).toFixed(2);
 		wBAllCost.value=parseFloat(costsum).toFixed(2);
 	}else if(typestr=='T'){
 		var name0="divwTProSum";
 		var name1="divwTProCostSum";
 		var name2="wTransNum";
 		var name3="wTransPrice";
 		var divwTProSum=document.getElementById(name0);//运输总台数 T
 		var divwTProCostSum=document.getElementById(name1);//运输总费用 
 		var wTAllCost=document.getElementById("wTAllCost");
 		if(document.getElementsByName(name2)==null || document.getElementsByName(name2).length==0){
 			
 		}else{
 			var wTransNum=document.getElementsByName(name2);
 			var wTransPrice=document.getElementsByName(name3);
 			for(var i=0;i<wTransNum.length;i++){
 				var num=wTransNum[i].value;
		 		if(num==""){
		 			num="0";
		 		}
		 		var price=wTransPrice[i].value;
		 		if(price==""){
		 			price="0";
		 		}
		 		prosum=parseFloat(prosum)+parseFloat(num);
		 		costsum=parseFloat(costsum)+parseFloat(price);
 			}
 		}
 		divwTProSum.innerHTML=prosum;
 		divwTProCostSum.innerHTML=parseFloat(costsum).toFixed(2);
 		wTAllCost.value=parseFloat(costsum).toFixed(2);
 		
 	}else if(typestr=='E'){//土建
 		var name0="divwEProSum";
 		var name1="divwEProCostSum";
 		var name2="wEBuildNum";
 		var name3="wEBuildPrice";
 		var divwBProSum=document.getElementById(name0);//土建总台数 B
 		var divwBProCostSum=document.getElementById(name1);//土建总费用
 		var wEAllCost=document.getElementById("wEAllCost");
 		if(document.getElementsByName(name2)==null || document.getElementsByName(name2).length==0){
 			
 		}else{
 			var wEBuildNum=document.getElementsByName(name2);
 			var wEBuildPrice=document.getElementsByName(name3);
 			for(var i=0;i<wEBuildNum.length;i++){
 				var num=wEBuildNum[i].value;
		 		if(num==""){
		 			num="0";
		 		}
		 		var price=wEBuildPrice[i].value;
		 		if(price==""){
		 			price="0";
		 		}
		 		prosum=parseFloat(prosum)+parseFloat(num);
		 		costsum=parseFloat(costsum)+parseFloat(price);
 			}
 			
 		}
 		divwEProSum.innerHTML=prosum;
 		divwEProCostSum.innerHTML=parseFloat(costsum).toFixed(2);
 		wEAllCost.value=parseFloat(costsum).toFixed(2);
 	}
 }
 
 /** 项目委托价格构成 */
 function projectPriceConstitute(num){ 
 	
 	/** 各个税率 */
	var yyshui=0.33;//33%成本额
 	var cjshui=0.04;//税费约4%

 	var contractsum=document.getElementById("contractsum"); //合同总价
	var costamount=document.getElementById("costamount"); //成本额
	var materialcost=document.getElementById("materialcost");//预计材料成本
	var taxes=document.getElementById("taxes"); //税费
	var costproject=document.getElementById("costproject"); //成本小计
	var workfee=document.getElementById("workfee"); //拟定委托工费
	var freight=document.getElementById("freight"); //运费
	var entrustproject=document.getElementById("entrustproject");//委托小计
	var maori=document.getElementById("maori"); //毛利
	var grossprofitmargin=document.getElementById("grossprofitmargin");//毛利率
	var costrate=document.getElementById("costrate"); //成本率
	
	var div_contractsum=document.getElementById("div_contractsum"); //合同总价
	var div_costamount=document.getElementById("div_costamount"); //成本额
	var div_taxes=document.getElementById("div_taxes"); //税费
	var div_costproject=document.getElementById("div_costproject"); //成本小计
	//var div_entrustproject=document.getElementById("div_entrustproject");//委托小计
	var div_maori=document.getElementById("div_maori"); //毛利
	var div_grossprofitmargin=document.getElementById("div_grossprofitmargin");//毛利率
	var div_costrate=document.getElementById("div_costrate"); //成本率
	
	var div_WAllCost=document.getElementById("divWAllCost");//委托费用
	var WAllCost=document.getElementById("WAllCost");//委托费用
	
	if(num=="0" || num=="1"){
		//成本额 (合同总价*0.33)
		var coststr=(parseFloat(contractsum.value)*yyshui).toFixed(2);
		costamount.value=coststr;
		div_costamount.innerText=coststr;  
		//税费 (合同总价*0.04)
		var taxestr=(parseFloat(contractsum.value)*cjshui).toFixed(2);
		taxes.value=taxestr;
		div_taxes.innerText=taxestr;
		//成本小计 (预计材料成本+税费)
		var mavalue=materialcost.value;
		if(mavalue.trim()==""){
			mavalue="0";
		}
		var costamstr=(parseFloat(mavalue)+parseFloat(taxestr)).toFixed(2);
		costproject.value=costamstr;
		div_costproject.innerText=costamstr;
		//委托小计 (拟定委托工费+运费)
		var workfeestr=workfee.value;
		var freightstr=freight.value;
		if(workfeestr.trim()==""){
			workfeestr="0";
		}
		if(freightstr.trim()==""){
			freightstr="0";
		}
		var entrstr=(parseFloat(workfeestr)+parseFloat(freightstr)).toFixed(2);
		entrustproject.value=entrstr;
		//div_entrustproject.innerText=entrstr;
		div_WAllCost.innerText=entrstr;//委托费用
		WAllCost.value=entrstr;//委托费用
		
		//毛利 (合同总价-成本小计-委托小计)
		var maostr=(parseFloat(contractsum.value)-costamstr-entrstr).toFixed(2);
		maori.value=maostr;
		div_maori.innerText=maostr;
		//毛利率 (毛利/合同总价)
		var rosstr=(maostr/parseFloat(contractsum.value)*100).toFixed(2);
		grossprofitmargin.value=rosstr;
		div_grossprofitmargin.innerText=rosstr+"%";
		//成本率 (委托小计+成本小计)/合同总价
		var cosvalue=((parseFloat(entrstr)+parseFloat(costamstr))/parseFloat(contractsum.value)*100).toFixed(2);
		costrate.value=cosvalue;
		div_costrate.innerText=cosvalue+"%";
		
	}else if(num=="3"){ //预计材料成本输入
		//成本小计 (预计材料成本+税费)
		var mavalue=materialcost.value;
		if(mavalue.trim()==""){
			mavalue="0";
		}
		var costamstr=(parseFloat(mavalue)+parseFloat(taxes.value)).toFixed(2);
		costproject.value=costamstr;
		div_costproject.innerText=costamstr;
		//毛利 (合同总价-成本小计-委托小计)
		var maostr=(parseFloat(contractsum.value)-costamstr-parseFloat(entrustproject.value)).toFixed(2);
		maori.value=maostr;
		div_maori.innerText=maostr;
		//毛利率 (毛利/合同总价)
		var rosstr=(maostr/parseFloat(contractsum.value)*100).toFixed(2);
		grossprofitmargin.value=rosstr;
		div_grossprofitmargin.innerText=rosstr+"%";
		//成本率 (委托小计+成本小计)/合同总价
		var cosvalue=((parseFloat(entrustproject.value)+parseFloat(costamstr))/parseFloat(contractsum.value)*100).toFixed(2);
		costrate.value=cosvalue;
		div_costrate.innerText=cosvalue+"%";
		
 	}else if(num=="6" || num=="7"){//拟定委托工费 or 运费 输入
		//委托小计 (拟定委托工费+运费)
		var workfeestr=workfee.value;
		var freightstr=freight.value;
		if(workfeestr.trim()==""){
			workfeestr="0";
		}
		if(freightstr.trim()==""){
			freightstr="0";
		}
		var entrstr=(parseFloat(workfeestr)+parseFloat(freightstr)).toFixed(2);
		entrustproject.value=entrstr;
		//div_entrustproject.innerText=entrstr;		
		div_WAllCost.innerText=entrstr;//委托费用
		WAllCost.value=entrstr;//委托费用
		
		//毛利 (合同总价-成本小计-委托小计)
		var maostr=(parseFloat(contractsum.value)-parseFloat(costproject.value)-entrstr).toFixed(2);
		maori.value=maostr;
		div_maori.innerText=maostr;
		//毛利率 (毛利/合同总价)
		var rosstr=(maostr/parseFloat(contractsum.value)*100).toFixed(2);
		grossprofitmargin.value=rosstr;
		div_grossprofitmargin.innerText=rosstr+"%";
		//成本率 (委托小计+成本小计)/合同总价
		var cosvalue=((parseFloat(entrstr)+parseFloat(costproject.value))/parseFloat(contractsum.value)*100).toFixed(2);
		costrate.value=cosvalue;
		div_costrate.innerText=cosvalue+"%";

 	}else if(num=="8"){//委托小计输入
 		var entrpstr = entrustproject.value;
 		div_WAllCost.innerText=entrpstr;//委托费用
		WAllCost.value=entrpstr;//委托费用
		
		//毛利 (合同总价-成本小计-委托小计)
		var maostr=(parseFloat(contractsum.value)-parseFloat(costproject.value)-parseFloat(entrpstr)).toFixed(2);
		maori.value=maostr;
		div_maori.innerText=maostr;
		//毛利率 (毛利/合同总价)
		var rosstr=(maostr/parseFloat(contractsum.value)*100).toFixed(2);
		grossprofitmargin.value=rosstr;
		div_grossprofitmargin.innerText=rosstr+"%";
		//成本率 (委托小计+成本小计)/合同总价
		var cosvalue=((parseFloat(entrpstr)+parseFloat(costproject.value))/parseFloat(contractsum.value)*100).toFixed(2);
		costrate.value=cosvalue;
		div_costrate.innerText=cosvalue+"%";
 	}
 }
/**=================== 维修报价 end ===========================*/
 
/**=================== 改造报价 start ===========================*/
 //发送请求函数
 function sendServicingContractDetailG(billno) {
 	var url = '/XJSCRM/SearchEngEpiCustomerAction.do?method=toServicingContractDetailG';
		url +='&gbillno='+billno;
  	createXMLHttpRequest2();
 	XMLHttpReq2.open("post", url, false);
  	XMLHttpReq2.onreadystatechange = processServicingContractDetailG;//指定响应函数
  	XMLHttpReq2.send(null);  // 发送请求
 }
 // 处理返回信息函数
 function processServicingContractDetailG() {
     	if (XMLHttpReq2.readyState == 4) { // 判断对象状态
         	if (XMLHttpReq2.status == 200) { // 信息已经成功返回，开始处理信息        	
         		var xmlDOM=XMLHttpReq2.responseXML;

            	var elevatorid=xmlDOM.getElementsByTagName("elevatorid");
            	var floor=xmlDOM.getElementsByTagName("floor");
            	var stage=xmlDOM.getElementsByTagName("stage");
            	var door=xmlDOM.getElementsByTagName("door");
            	var high=xmlDOM.getElementsByTagName("high");
            	var num=xmlDOM.getElementsByTagName("num");
            	var angle=xmlDOM.getElementsByTagName("angle");
            	var amt=xmlDOM.getElementsByTagName("amt");
            	var detailrowid=xmlDOM.getElementsByTagName("detailrowid");
            	var quoteelevatorid=xmlDOM.getElementsByTagName("quoteelevatorid");
            	//先删除已经存在的电梯型号信息
            	delSelNode22(adjtb_gd);
            	var amtsum=0;
            	for(var i=0;i<elevatorid.length;i++){
            		var eleid=elevatorid[i].firstChild.data;
            		var floorstr=floor[i].firstChild.data;
            		var stagestr=stage[i].firstChild.data;
            		var doorstr=door[i].firstChild.data;
            		var highstr=high[i].firstChild.data;
            		var numstr=num[i].firstChild.data;
            		var eanglestr=angle[i].firstChild.data;
            		var amtstr=amt[i].firstChild.data;
            		var rowidstr=detailrowid[i].firstChild.data;
            		var quotestr=quoteelevatorid[i].firstChild.data;
            		
            		AddRow_GD(eleid,floorstr,stagestr,doorstr,highstr,numstr,eanglestr,rowidstr,quotestr);
            	}
         	} else { //页面不正常
               window.alert("您所请求的页面有异常。");
         	}
       }
    }
  /** 添加合同电梯明细信息 */
  function AddRow_GD(elevatorid,floor,stage,door,high,numkk,angle,rowidstr,quotestr){
		var adjtb_w=document.getElementById("adjtb_gd");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);

		//电梯型号
		var name0="gelevatorid";
		var name00="gdetailrowid";
		var name000="gquoteelevatorid";
		var ince1=adjtb_w.rows(num).insertCell(0);
		ince1.innerHTML=elevatorid+"<input type=\'hidden\' name=\'"+name0+"\' value=\'"+elevatorid+"\' /><input type=\'hidden\' name=\'"+name00+"\' value=\'"+rowidstr+"\' /><input type=\'hidden\' name=\'"+name000+"\' value=\'"+quotestr+"\' />";
		//层站门
		var name1="gfloor";
		var name2="gstage";
		var name3="gdoor";
		var ince2=adjtb_w.rows(num).insertCell(1);
		ince2.innerHTML=floor+"/"+stage+"/"+door+"<input type=\'hidden\' name=\'"+name1+"\' value=\'"+floor+"\' /><input type=\'hidden\' name=\'"+name2+"\' value=\'"+stage+"\' /><input type=\'hidden\' name=\'"+name3+"\' value=\'"+door+"\' />";
		//提升高度
		var name4="ghigh";
		var ince3=adjtb_w.rows(num).insertCell(2);
		ince3.align="right";
		ince3.innerHTML=high+"&nbsp;<input type=\'hidden\' name=\'"+name4+"\' value=\'"+high+"\' />";
		//倾斜角度
		var name5="gangle";
		var ince4=adjtb_w.rows(num).insertCell(3);
		ince4.align="right";
		ince4.innerHTML=angle+"&nbsp;<input type=\'hidden\' name=\'"+name5+"\' value=\'"+angle+"\' />";
		//数量
		var name6="gnum";
		var ince5=adjtb_w.rows(num).insertCell(4);
		ince5.align="right";
		ince5.innerHTML=numkk+"&nbsp;<input type=\'hidden\' name=\'"+name6+"\' value=\'"+numkk+"\' />";
 }
 /** 吊装 */ 
  function AddRow_G(tableid){
  	var errorstr=gcheckweihuinfo2("","D","");
  	if(errorstr==""){
		var adjtb_w=document.getElementById("adjtb_g");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Gnodes";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Gdivorder";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//层
		var name2="gDfloor";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' size=\'4\'  onkeyup=\"setStageValue(this,'gDstage')\" onkeypress=\'f_check_number2();\' >";
		//站
		var name3="gDstage";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'4\' onkeypress=\'f_check_number2();\'>";
		//电梯载重（kg）
		var name4="gDZzl";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' onkeypress=\'f_check_number3();\' size=\'10\' class=\'default_input\'>";
		//吊装地点
		var name5="gDSuspendAddress";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\'>";
		//吊装项目
		var name6="gDSuspendItem";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' >";
		//吊装箱头
		var name7="gDSuspendBox";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.innerHTML="<input type=\'text\' name=\'"+name7+"\' class=\'default_input\' >";
		//计算公式（原吊装发包价）
		var name8="gDCountExp";
		var ince8=adjtb_w.rows(num).insertCell(8);
		//ince8.innerHTML="<input type=\'text\' name=\'wCountExp\' class=\'default_input\' >";
		ince8.innerHTML="<textarea name=\'"+name8+"\' rows=\'1\' cols=\'30\'>";
		//吊装台数
		var name9="gDSuspendNum";
		var ince9=adjtb_w.rows(num).insertCell(9);
		ince9.align="right";
		ince9.innerHTML="<input type=\'text\' name=\'"+name9+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_g('D')\" size=\'6\'>";
		//吊装费用
		var name10="gDSuspendPrice";
		var ince10=adjtb_w.rows(num).insertCell(10);
		ince10.align="right";
		ince10.innerHTML="<input type=\'text\' name=\'"+name10+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_g('D')\" size=\'10\'>";

		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
  /** 搭棚 */ 
  function AddRow_G1(tableid){
	  var errorstr=gcheckweihuinfo2("","B","");
	  if(errorstr==""){
  		var adjtb_w=document.getElementById("adjtb_g1");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Gnodes1";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Gdivorder1";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//层
		var name2="gBfloor";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' size=\'4\'  onkeyup=\"setStageValue(this,'gBstage')\" onkeypress=\'f_check_number2();\' >";
		//站
		var name3="gBstage";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'4\' onkeypress=\'f_check_number2();\'>";
		//电梯载重（kg）
		var name4="gBZzl";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' onkeypress=\'f_check_number3();\' class=\'default_input\' size=\'10\'>";
		//搭棚地点
		var name5="gBBuildAddress";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\' size=\'40\'>";
		//搭棚项目
		var name6="gBBuildItem";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' size=\'40\' >";
		//井道总高（米）
		var name7="gBJdH";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.innerHTML="<input type=\'text\' name=\'"+name7+"\' class=\'default_input\' size=\'10\' onkeypress=\'f_check_number3();\'>"
		//搭棚台数
		var name8="gBBuildNum";
		var ince8=adjtb_w.rows(num).insertCell(8);
		ince8.align="right";
		ince8.innerHTML="<input type=\'text\' name=\'"+name8+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_g('B')\" size=\'6\'>";
		//搭棚费用
		var name9="gBBuildPrice";
		var ince9=adjtb_w.rows(num).insertCell(9);
		ince9.align="right";
		ince9.innerHTML="<input type=\'text\' name=\'"+name9+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_g('B')\" size=\'10\'>";

		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
  /** 运输 */
  function AddRow_G2(tableid){
	  var errorstr=gcheckweihuinfo2("","T","");
	  if(errorstr==""){
  		var adjtb_w=document.getElementById("adjtb_g2");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Gnodes2";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Gdivorder2";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//运输地点
		var name2="gTransAddress";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' size=\'40\' >";
		//运输项目
		var name3="gTransItem";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'40\'>";
		//发出车型
		var name4="gTSendType";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' class=\'default_input\' size=\'40\' >";
		//运输台数
		var name5="gTransNum";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.align="right";
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_g('T')\" size=\'6\'>";
		//运输费用
		var name6="gTransPrice";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.align="right";
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_g('T')\" size=\'10\'>";

		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
    /** 土建 */ 
  function AddRow_G3(tableid){
	  var errorstr=gcheckweihuinfo2("","E","");
	  if(errorstr==""){
  		var adjtb_w=document.getElementById("adjtb_g3");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Gnodes1e";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Gdivorder1e";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//层
		var name2="gEfloor";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' size=\'4\'  onkeyup=\"setStageValue(this,'gEstage')\" onkeypress=\'f_check_number2();\' >";
		//站
		var name3="gEstage";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'4\' onkeypress=\'f_check_number2();\'>";
		//电梯载重（kg）
		var name4="gEZzl";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' onkeypress=\'f_check_number3();\' class=\'default_input\' size=\'10\'>";
		//土建地点
		var name5="gEBuildAddress";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\' size=\'40\'>";
		//土建项目
		var name6="gEBuildItem";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' size=\'40\' >";
		//井道总高（米）
		var name7="gEJdH";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.innerHTML="<input type=\'text\' name=\'"+name7+"\' class=\'default_input\' size=\'10\' onkeypress=\'f_check_number3();\'>"
		//土建台数
		var name8="gEBuildNum";
		var ince8=adjtb_w.rows(num).insertCell(8);
		ince8.align="right";
		ince8.innerHTML="<input type=\'text\' name=\'"+name8+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_g('E')\" size=\'6\'>";
		//土建费用
		var name9="gEBuildPrice";
		var ince9=adjtb_w.rows(num).insertCell(9);
		ince9.align="right";
		ince9.innerHTML="<input type=\'text\' name=\'"+name9+"\' class=\'default_input\' value=\'0\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_g('E')\" size=\'10\'>";

		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
   /** 总台数 总费用 parseFloat();parseInt();isNaN();检查是否是字符 */
 function jisuanxiaoji_g(typestr){
 	
 	var prosum=0;
 	var costsum=0;
 	
 	if(typestr=='D'){
 		var name0="divgProSum";
 		var name1="divgProCostSum";
 		var name2="gDSuspendNum";
 		var name3="gDSuspendPrice";
 		var divwProSum=document.getElementById(name0);//吊装总台数 D
 		var divwProCostSum=document.getElementById(name1);//吊装总费用
 		var gDAllCost=document.getElementById("gDAllCost");
 		if(document.getElementsByName(name2)==null || document.getElementsByName(name2).length==0){
 			
 		}else{
 			var wSuspendNum=document.getElementsByName(name2);
 			var wSuspendPrice=document.getElementsByName(name3);
 			for(var i=0;i<wSuspendNum.length;i++){
 				var num=wSuspendNum[i].value;
		 		if(num==""){
		 			num="0";
		 		}
		 		var price=wSuspendPrice[i].value;
		 		if(price==""){
		 			price="0";
		 		}
		 		prosum=parseFloat(prosum)+parseFloat(num);
		 		costsum=parseFloat(costsum)+parseFloat(price);
 			}
 			
 		}
 		divwProSum.innerHTML=prosum;
 		divwProCostSum.innerHTML=parseFloat(costsum).toFixed(2);
 		gDAllCost.value=parseFloat(costsum).toFixed(2);
 	}else if(typestr=='B'){
 		var name0="divgBProSum";
 		var name1="divgBProCostSum";
 		var name2="gBBuildNum";
 		var name3="gBBuildPrice";
 		var divwBProSum=document.getElementById(name0);//搭棚总台数 B
 		var divwBProCostSum=document.getElementById(name1);//搭棚总费用
 		var gBAllCost=document.getElementById("gBAllCost");
 		if(document.getElementsByName(name2)==null || document.getElementsByName(name2).length==0){
 			
 		}else{
 			var wBBuildNum=document.getElementsByName(name2);
 			var wBBuildPrice=document.getElementsByName(name3);
 			for(var i=0;i<wBBuildNum.length;i++){
 				var num=wBBuildNum[i].value;
		 		if(num==""){
		 			num="0";
		 		}
		 		var price=wBBuildPrice[i].value;
		 		if(price==""){
		 			price="0";
		 		}
		 		prosum=parseFloat(prosum)+parseFloat(num);
		 		costsum=parseFloat(costsum)+parseFloat(price);
 			}
 			
 		}
 		divwBProSum.innerHTML=prosum;
 		divwBProCostSum.innerHTML=parseFloat(costsum).toFixed(2);
 		gBAllCost.value=parseFloat(costsum).toFixed(2);
 	}else if(typestr=='T'){
 		var name0="divgTProSum";
 		var name1="divgTProCostSum";
 		var name2="gTransNum";
 		var name3="gTransPrice";
 		var divwTProSum=document.getElementById(name0);//运输总台数 T
 		var divwTProCostSum=document.getElementById(name1);//运输总费用 
 		var gTAllCost=document.getElementById("gTAllCost");
 		if(document.getElementsByName(name2)==null || document.getElementsByName(name2).length==0){
 			
 		}else{
 			var wTransNum=document.getElementsByName(name2);
 			var wTransPrice=document.getElementsByName(name3);
 			for(var i=0;i<wTransNum.length;i++){
 				var num=wTransNum[i].value;
		 		if(num==""){
		 			num="0";
		 		}
		 		var price=wTransPrice[i].value;
		 		if(price==""){
		 			price="0";
		 		}
		 		prosum=parseFloat(prosum)+parseFloat(num);
		 		costsum=parseFloat(costsum)+parseFloat(price);
 			}
 		}
 		divwTProSum.innerHTML=prosum;
 		divwTProCostSum.innerHTML=parseFloat(costsum).toFixed(2);
 		gTAllCost.value=parseFloat(costsum).toFixed(2);
 	}else if(typestr=='E'){//土建
 		var name0="divgEProSum";
 		var name1="divgEProCostSum";
 		var name2="gEBuildNum";
 		var name3="gEBuildPrice";
 		var divgEProSum=document.getElementById(name0);//搭棚总台数 B
 		var divgEProCostSum=document.getElementById(name1);//搭棚总费用
 		var gEAllCost=document.getElementById("gEAllCost");
 		if(document.getElementsByName(name2)==null || document.getElementsByName(name2).length==0){
 			
 		}else{
 			var gEBuildNum=document.getElementsByName(name2);
 			var gEBuildPrice=document.getElementsByName(name3);
 			for(var i=0;i<gEBuildNum.length;i++){
 				var num=gEBuildNum[i].value;
		 		if(num==""){
		 			num="0";
		 		}
		 		var price=gEBuildPrice[i].value;
		 		if(price==""){
		 			price="0";
		 		}
		 		prosum=parseFloat(prosum)+parseFloat(num);
		 		costsum=parseFloat(costsum)+parseFloat(price);
 			}
 			
 		}
 		divgEProSum.innerHTML=prosum;
 		divgEProCostSum.innerHTML=parseFloat(costsum).toFixed(2);
 		gEAllCost.value=parseFloat(costsum).toFixed(2);
 	}
 }
 
   /** 检查明细信息,如果，施工方存在，就必须填写明细信息 */
 function gcheckweihuinfo2(error,typestr,showtype){
 	if(typestr=='D'){//吊装
		var custId=document.getElementById("gDSCustId").value.trim();
		if(custId!=""){
 			if(document.getElementsByName("Gdivorder")==null || document.getElementsByName("Gdivorder").length==0){
 				if(showtype=="G"){
 					error="吊装发包申请，请添加明细信息！";
 				}
 			}else{
 				var divorder=document.getElementsByName("Gdivorder");
 				
 				var floor=document.getElementsByName("gDfloor");//层
			 	var stage=document.getElementsByName("gDstage");//站
			 	var Zzl=document.getElementsByName("gDZzl");//电梯载重（kg）
			 	var SuspendAddress=document.getElementsByName("gDSuspendAddress");//吊装地点
			 	var SuspendItem=document.getElementsByName("gDSuspendItem");//吊装项目
			 	var SuspendBox=document.getElementsByName("gDSuspendBox");//吊装箱头
			 	var CountExp=document.getElementsByName("gDCountExp");//计算公式（原吊装发包价）
			 	var SuspendNum=document.getElementsByName("gDSuspendNum");//吊装台数
			 	var SuspendPrice=document.getElementsByName("gDSuspendPrice");//吊装费用
 				
 				for(var i=0;i<divorder.length;i++){
 					var xuhao=divorder[i].innerText;
 					if(floor[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 层不可以为空！\n";
			 		}else if(isNaN(floor[i].value.trim())){
			 			error+="吊装发包申请，序号"+xuhao+" 层必须为数字！\n";
			 		}
			 		if(stage[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 站不可以为空！\n";
			 		}else if(isNaN(stage[i].value)){
			 			error+="吊装发包申请，序号"+xuhao+" 站必须为数字！\n";
			 		}
			 		if(Zzl[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 电梯载重不可以为空！\n";
			 		}else if(isNaN(Zzl[i].value.trim())){
			 			error+="吊装发包申请，序号"+xuhao+" 电梯载重必须为数字！\n";
			 		}
			 		if(SuspendAddress[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装地点不可以为空！\n";
			 		}
			 		if(SuspendItem[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装项目不可以为空！\n";
			 		}
			 		if(SuspendBox[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装箱头不可以为空！\n";
			 		}
			 		if(CountExp[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 计算公式不可以为空！\n";
			 		}
			 		if(SuspendNum[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装台数不可以为空！\n";
			 		}else if(isNaN(SuspendNum[i].value.trim())){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装台数必须为数字！\n";
			 		}else if(parseFloat(SuspendNum[i].value.trim())<=0){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装台数必须大于零！\n";
			 		}
			 		if(SuspendPrice[i].value.trim()==""){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装费用不可以为空！\n";
			 		}else if(isNaN(SuspendPrice[i].value.trim())){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装费用必须为数字！\n";
			 		}else if(parseFloat(SuspendPrice[i].value.trim())<=0){
			 			error+="吊装发包申请，序号"+xuhao+" 吊装费用必须大于零！\n";
			 		}
 				}
 			}
		}else{
			if(showtype==""){
				error+="请先选择施工方！";
			}
		}
 	}else if(typestr=='B'){//搭棚
 		var custId=document.getElementById("gBCustId").value.trim();
		if(custId!=""){			
 			if(document.getElementsByName("Gdivorder1")==null || document.getElementsByName("Gdivorder1").length==0){
 				if(showtype=="G"){
 					error="搭棚发包申请，请添加明细信息！";
 				}
 			}else{
 				var divorder=document.getElementsByName("Gdivorder1");
 				
 				var floor=document.getElementsByName("gBfloor");//层
			 	var stage=document.getElementsByName("gBstage");//站
			 	var Zzl=document.getElementsByName("gBZzl");//电梯载重（kg）
			 	var BuildAddress=document.getElementsByName("gBBuildAddress");//搭棚地点
			 	var BuildItem=document.getElementsByName("gBBuildItem");//搭棚项目
			 	var JdH=document.getElementsByName("gBJdH");//井道总高
			 	var BuildNum=document.getElementsByName("gBBuildNum");//搭棚台数
			 	var BuildPrice=document.getElementsByName("gBBuildPrice");//搭棚费用
 				
 				for(var i=0;i<divorder.length;i++){
 					var xuhao=divorder[i].innerText;
 					if(floor[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 层不可以为空！\n";
			 		}else if(isNaN(floor[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 层必须为数字！\n";
			 		}
			 		if(stage[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 站不可以为空！\n";
			 		}else if(isNaN(stage[i].value)){
			 			error+="搭棚发包申请，序号"+xuhao+" 站必须为数字！\n";
			 		}
			 		if(Zzl[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 电梯载重不可以为空！\n";
			 		}else if(isNaN(Zzl[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 电梯载重必须为数字！\n";
			 		}
			 		if(BuildAddress[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚地点不可以为空！\n";
			 		}
			 		if(BuildItem[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚项目不可以为空！\n";
			 		}
			 		if(JdH[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 井道总高不可以为空！\n";
			 		}else if(isNaN(JdH[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 井道总高必须为数字！\n";
			 		}
			 		if(BuildNum[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚台数不可以为空！\n";
			 		}else if(isNaN(BuildNum[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚台数必须为数字！\n";
			 		}else if(parseFloat(BuildNum[i].value.trim())<=0){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚台数必须大于零！\n";
			 		}
			 		if(BuildPrice[i].value.trim()==""){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚费用不可以为空！\n";
			 		}else if(isNaN(BuildPrice[i].value.trim())){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚费用必须为数字！\n";
			 		}else if(parseFloat(BuildPrice[i].value.trim())<=0){
			 			error+="搭棚发包申请，序号"+xuhao+" 搭棚费用必须大于零！\n";
			 		}
 				}
 			}
		}else{
			if(showtype==""){
				error+="请先选择施工方！";
			}
		}
 	}else if(typestr=='T'){//运输
 		 var custId=document.getElementById("gTCustId").value.trim();
		if(custId!=""){		
 			if(document.getElementsByName("Gdivorder2")==null || document.getElementsByName("Gdivorder2").length==0){
 				if(showtype=="G"){
 					error="运输发包申请，请添加明细信息！";
 				}
 			}else{
 				var divorder=document.getElementsByName("Gdivorder2");
 				
 				var TransAddress=document.getElementsByName("gTransAddress");//运输地点
			 	var TransItem=document.getElementsByName("gTransItem");//运输项目
			 	var TSendType=document.getElementsByName("gTSendType");//发出车型
			 	var TransNum=document.getElementsByName("gTransNum");//运输台数
			 	var TransPrice=document.getElementsByName("gTransPrice");//运输费用
 				
 				for(var i=0;i<divorder.length;i++){
 					var xuhao=divorder[i].innerText;
			 		if(TransAddress[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 运输地点不可以为空！\n";
			 		}
			 		if(TransItem[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 运输项目不可以为空！\n";
			 		}
			 		if(TSendType[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 发出车型不可以为空！\n";
			 		}
			 		if(TransNum[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 运输台数不可以为空！\n";
			 		}else if(isNaN(TransNum[i].value.trim())){
			 			error+="运输发包申请，序号"+xuhao+" 运输台数必须为数字！\n";
			 		}else if(parseFloat(TransNum[i].value.trim())<=0){
			 			error+="运输发包申请，序号"+xuhao+" 运输台数必须大于零！\n";
			 		}
			 		if(TransPrice[i].value.trim()==""){
			 			error+="运输发包申请，序号"+xuhao+" 运输费用不可以为空！\n";
			 		}else if(isNaN(TransPrice[i].value.trim())){
			 			error+="运输发包申请，序号"+xuhao+" 运输费用必须为数字！\n";
			 		}else if(parseFloat(TransPrice[i].value.trim())<=0){
			 			error+="运输发包申请，序号"+xuhao+" 运输费用必须大于零！\n";
			 		}
 				}
 			}
		}else{
			if(showtype==""){
				error+="请先选择施工方！";
			}
		}
 	}else if(typestr=='E'){//土建
 		var custId=document.getElementById("gECustId").value.trim();
		if(custId!=""){			
 			if(document.getElementsByName("Gdivorder1e")==null || document.getElementsByName("Gdivorder1e").length==0){
 				if(showtype=="G"){
 					error="土建发包申请，请添加明细信息！";
 				}
 			}else{
 				var divorder=document.getElementsByName("Gdivorder1e");
 				
 				var floor=document.getElementsByName("gEfloor");//层
			 	var stage=document.getElementsByName("gEstage");//站
			 	var Zzl=document.getElementsByName("gEZzl");//电梯载重（kg）
			 	var BuildAddress=document.getElementsByName("gEBuildAddress");//土建地点
			 	var BuildItem=document.getElementsByName("gEBuildItem");//土建项目
			 	var JdH=document.getElementsByName("gEJdH");//井道总高
			 	var BuildNum=document.getElementsByName("gEBuildNum");//土建台数
			 	var BuildPrice=document.getElementsByName("gEBuildPrice");//土建费用
 				
 				for(var i=0;i<divorder.length;i++){
 					var xuhao=divorder[i].innerText;
 					if(floor[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 层不可以为空！\n";
			 		}else if(isNaN(floor[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 层必须为数字！\n";
			 		}
			 		if(stage[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 站不可以为空！\n";
			 		}else if(isNaN(stage[i].value)){
			 			error+="土建发包申请，序号"+xuhao+" 站必须为数字！\n";
			 		}
			 		if(Zzl[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 电梯载重不可以为空！\n";
			 		}else if(isNaN(Zzl[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 电梯载重必须为数字！\n";
			 		}
			 		if(BuildAddress[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 土建地点不可以为空！\n";
			 		}
			 		if(BuildItem[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 土建项目不可以为空！\n";
			 		}
			 		if(JdH[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 井道总高不可以为空！\n";
			 		}else if(isNaN(JdH[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 井道总高必须为数字！\n";
			 		}
			 		if(BuildNum[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 土建台数不可以为空！\n";
			 		}else if(isNaN(BuildNum[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 土建台数必须为数字！\n";
			 		}else if(parseFloat(BuildNum[i].value.trim())<=0){
			 			error+="土建发包申请，序号"+xuhao+" 土建台数必须大于零！\n";
			 		}
			 		if(BuildPrice[i].value.trim()==""){
			 			error+="土建发包申请，序号"+xuhao+" 土建费用不可以为空！\n";
			 		}else if(isNaN(BuildPrice[i].value.trim())){
			 			error+="土建发包申请，序号"+xuhao+" 土建费用必须为数字！\n";
			 		}else if(parseFloat(BuildPrice[i].value.trim())<=0){
			 			error+="土建发包申请，序号"+xuhao+" 土建费用必须大于零！\n";
			 		}
 				}
 			}
		}else{
			if(showtype==""){
				error+="请先选择施工方！";
			}
		}
 	}

 	return error;
 }
/**=================== 改造报价 end ===========================*/
 
 /**=================== 保养报价 start ===========================*/
 /**   */
 function sendServicingContractDetailB(){
	var url='query/Search.jsp?path=/XJSCRM/SearchEngEpiCustomerAction.do?method=toSearchRecord_BaoYangDetail';
	var arr = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');

   	if(arr!=null){
		for(var i=0;i<arr.length;i++){
			var obj=arr[i];
			var apptype=document.getElementById("AppType").value;
			if(apptype=="1"){
				AddRow_B(obj);
			}else if(apptype=="2"){
				AddRow_B0(obj);
			}
		}
	}
}
  /** 保养型号信息 */ 
  function AddRow_B(obj){

		var adjtb_w=document.getElementById("adjtb_b");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Bnodes";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Bdivorder";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//客户名称
		var name2="bSourceCustId";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML=obj.custname+"<input type=\'hidden\' name=\'"+name2+"\' value=\'"+obj.custid+"\' />";
		//合同号
		var name3="bSoureConctractId";
		var name3_0="bBillNo";
		var name3_1="bDetailRowId";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML=obj.contractid+"<input type=\'hidden\' name=\'"+name3+"\' value=\'"+obj.contractid+"\' />"+
						"<input type=\'hidden\' name=\'"+name3_0+"\' value=\'"+obj.billno+"\' />"+
						"<input type=\'hidden\' name=\'"+name3_1+"\' value=\'"+obj.detailrowid+"\' />";
		//服务号
		var name4="bServiceNo";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML=obj.serviceno+"<input type=\'hidden\' name=\'"+name4+"\' value=\'"+obj.serviceno+"\' />";
		//电梯型号
		var name5="bElevatorId";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.innerHTML=obj.elevatorid+"<input type=\'hidden\' name=\'"+name5+"\' value=\'"+obj.elevatorid+"\' />";
		//层站门
		var name6="bfloor";
		var name6_0="bstage";
		var name6_1="bdoor";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML=obj.floor+"/"+obj.stage+"/"+obj.door+
						"<input type=\'hidden\' name=\'"+name6+"\' value=\'"+obj.floor+"\' />"+
						"<input type=\'hidden\' name=\'"+name6_0+"\' value=\'"+obj.stage+"\' />"+
						"<input type=\'hidden\' name=\'"+name6_1+"\' value=\'"+obj.door+"\' />";
		//提升高度
		var name7="bHigh";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.align="right";
		ince7.innerHTML=obj.high+"<input type=\'hidden\' name=\'"+name7+"\' value=\'"+obj.high+"\' />";
		//倾斜角度
		var name8="bAngle";
		var ince8=adjtb_w.rows(num).insertCell(8);
		ince8.align="right";
		ince8.innerHTML=obj.angle+"<input type=\'hidden\' name=\'"+name8+"\' value=\'"+obj.angle+"\' />";
		//载重量
		var name9="bZzl";
		var ince9=adjtb_w.rows(num).insertCell(9);
		ince9.align="right";
		ince9.innerHTML=obj.zzl+"<input type=\'hidden\' name=\'"+name9+"\' value=\'"+obj.zzl+"\' />";
		//速度
		var name10="bSpeed";
		var ince10=adjtb_w.rows(num).insertCell(10);
		ince10.innerHTML=obj.speed+"<input type=\'hidden\' name=\'"+name10+"\' value=\'"+obj.speed+"\' />";
		//数量
		var name11="bNum";
		var ince11=adjtb_w.rows(num).insertCell(11);
		ince11.align="right";
		ince11.innerHTML=obj.num+"<input type=\'hidden\' name=\'"+name11+"\' value=\'"+obj.num+"\' />";
		//保养地址
		var name12="baddress";
		var ince12=adjtb_w.rows(num).insertCell(12);
		ince12.innerHTML=obj.mugaddress+"<input type=\'hidden\' name=\'"+name12+"\' value=\'"+obj.mugaddress+"\' />";
		//价格
		var name13="bamt";
		var name13_0="bamthid";
		var price=obj.price;
		if(price==null || price.trim()==""){
			price="0";
		}
		price=(parseFloat(price)*0.7).toFixed(2);//显示默认单价70%
		var ince13=adjtb_w.rows(num).insertCell(13);
		ince13.align="right";
		ince13.innerHTML="<input type=\'text\' name=\'"+name13+"\' value=\'"+price+"\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_b('S')\" size=\'10\' class=\'default_input\'/>"+
						"<input type=\'hidden\' name=\'"+name13_0+"\' value=\'"+obj.price+"\' />";
		//保养月份
		var name14="bMugMonth";
		var ince14=adjtb_w.rows(num).insertCell(14);
		ince14.align="right";
		ince14.innerHTML="<input type=\'text\' name=\'"+name14+"\' value=\'"+obj.r18+"\' onkeypress=\'f_check_number2();\' onkeyup=\"jisuanxiaoji_b('S')\" size=\'6\' class=\'default_input\'/>";
		//保养开始日期
		var name15="bmugstartdate";
		var ince15=adjtb_w.rows(num).insertCell(15);
		ince15.innerHTML="<input type=\'text\' name=\'"+name15+"\' value=\'"+obj.mugstartdate+"\' class=\'default_input\' onclick=\'setday(this)\' readonly=\'readonly\' size=\'12\' />";
		//保养结束日期
		var name16="bmugenddate";
		var ince16=adjtb_w.rows(num).insertCell(16);
		ince16.innerHTML="<input type=\'text\' name=\'"+name16+"\' value=\'"+obj.mugenddate+"\' class=\'default_input\' onclick=\'setday(this)\' readonly=\'readonly\'  size=\'12\' />";

		xuhaonum(name1);
		jisuanxiaoji_b("S");
  }  

    /** 劳务人员费用和作业时间表 */ 
  function AddRow_B0(obj){
//  	var errorstr=bcheckweihuinfo("","L","");
//  	if(errorstr==""){
		var adjtb_w=document.getElementById("adjtb_b1");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Bnodes1";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Bdivorder1";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//客户名称
		var name2="bSourceCustId1";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML=obj.custname+"<input type=\'hidden\' name=\'"+name2+"\' value=\'"+obj.custid+"\' />";
		//合同号
		var name3="bSoureConctractId1";
		var name3_0="bBillNo1";
		var name3_1="bDetailRowId1";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML=obj.contractid+"<input type=\'hidden\' name=\'"+name3+"\' value=\'"+obj.contractid+"\' />"+
						"<input type=\'hidden\' name=\'"+name3_0+"\' value=\'"+obj.billno+"\' />"+
						"<input type=\'hidden\' name=\'"+name3_1+"\' value=\'"+obj.detailrowid+"\' />";
		//电梯型号
		var name4="bElevatorId1";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML=obj.elevatorid+"<input type=\'hidden\' name=\'"+name4+"\' value=\'"+obj.elevatorid+"\' />";
		//数量
		var name5="bNum1";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.align="right";
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' value=\'"+obj.num+"\' class=\'default_input\' onkeypress=\'f_check_number2();\' size=\'6\' />";
		//劳务开始日期	
		var name6="bmugstartdate1";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' onclick=\'setday(this)\' onpropertychange=\"setDateReduplicate(this)\" readonly=\'readonly\'  size=\'12\'/>";
		//劳务结束日期
		var name7="bmugenddate1";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.innerHTML="<input type=\'text\' name=\'"+name7+"\' class=\'default_input\' onclick=\'setday(this)\' onpropertychange=\"setDateReduplicate(this)\" readonly=\'readonly\'  size=\'12\'/>";
		//劳务月份
		var name8="bMugMonth1";
		var ince8=adjtb_w.rows(num).insertCell(8);
		ince8.align="right";
		ince8.innerHTML="<input type=\'text\' name=\'"+name8+"\' class=\'default_input\' onkeypress=\'f_check_number2();\' onkeyup=\"setDateReduplicate(this);jisuanxiaoji_b('L');\" size=\'6\' />";
		//工费单价/人/月 
		var name9="bamt1";
		var ince9=adjtb_w.rows(num).insertCell(9);
		ince9.align="right";
		ince9.innerHTML="<input type=\'text\' name=\'"+name9+"\' class=\'default_input\' onkeypress=\'f_check_number3();\' onkeyup=\"jisuanxiaoji_b('L')\" size=\'10\' />";
		//工费总价/月		
		var name10="div_amtsum";
		var ince10=adjtb_w.rows(num).insertCell(10);
		ince10.align="right";
		ince10.innerHTML="<input type=\'text\' name=\'"+name10+"\' style=\'border:0;text-align:right;\' readonly=\'readonly\'>";

		xuhaonum(name1);
//	}else{
//		alert(errorstr);
//	}
  }
   /** 劳务人员清单 */ 
   function AddRow_B1(tableid){
  	var errorstr=bcheckweihuinfo("","W","");
  	if(errorstr==""){
		var adjtb_w=document.getElementById("adjtb_b2");
		var num=adjtb_w.rows.length;

		adjtb_w.insertRow(num);		
		//复选框
		var name0="Bnodes2";
		var ince0=adjtb_w.rows(num).insertCell(0);
		ince0.align="center";
		ince0.innerHTML="<input type=\'checkbox\' name=\'"+name0+"\' >";
		//序号
		var name1="Bdivorder2";
		var ince1=adjtb_w.rows(num).insertCell(1);
		ince1.align="center";
		ince1.innerHTML="<div id=\'"+name1+"\'>";
		//姓名
		var name2="bPersonName";
		var ince2=adjtb_w.rows(num).insertCell(2);
		ince2.innerHTML="<input type=\'text\' name=\'"+name2+"\' class=\'default_input\' />";
		//性别
		var name3="bPersonGender";
		var ince3=adjtb_w.rows(num).insertCell(3);
		ince3.innerHTML="<input type=\'text\' name=\'"+name3+"\' class=\'default_input\' size=\'6\' />";
		//年龄
		var name4="bPersonAge";
		var ince4=adjtb_w.rows(num).insertCell(4);
		ince4.innerHTML="<input type=\'text\' name=\'"+name4+"\' onkeypress=\'f_check_number2();\' size=\'4\' class=\'default_input\' />";
		//职务
		var name5="bPersonDuty";
		var ince5=adjtb_w.rows(num).insertCell(5);
		ince5.innerHTML="<input type=\'text\' name=\'"+name5+"\' class=\'default_input\'>";
		//省操作证号码
		var name6="bPersonOperNo";
		var ince6=adjtb_w.rows(num).insertCell(6);
		ince6.innerHTML="<input type=\'text\' name=\'"+name6+"\' class=\'default_input\' >";
		//身份证号码
		var name7="bPersonCardNo";
		var ince7=adjtb_w.rows(num).insertCell(7);
		ince7.innerHTML="<input type=\'text\' name=\'"+name7+"\' class=\'default_input\' >";
	
		xuhaonum(name1);
	}else{
		alert(errorstr);
	}
  }
   /** 申请总费用 parseFloat();parseInt();isNaN();检查是否是字符 */
 function jisuanxiaoji_b(typestr){
 	
 	if(typestr=='L'){
 		var prosum=0;
 		var costsum=0;
 	
 		var name="BAllCost1";
 		var name_0="div_ballcost2";
 		var ballcost=document.getElementById(name);//申请总费用
 		var div_ballcost2=document.getElementById(name_0);
 		
 		var name1="bMugMonth1";
 		var name2="bamt1";
		var bMugMonth1=document.getElementsByName(name1);//劳务月份
		var bamt1=document.getElementsByName(name2);//工费单价/人/月 
		for(var i=0;i<bMugMonth1.length;i++){
			var num=bMugMonth1[i].value;
	 		if(num==""){
	 			num="0";
	 		}
	 		var price=bamt1[i].value;
	 		if(price==""){
	 			price="0";
	 		}
	 		prosum=(parseFloat(num)*parseFloat(price)).toFixed(2);
		 	costsum=parseFloat(costsum)+parseFloat(prosum);
		 	//alert("for: div_amtsum_"+(i+1));
		 	document.getElementsByName("div_amtsum")[i].value=prosum;//工费总价/月;
		}
		div_ballcost2.innerText=costsum;
 		ballcost.value=costsum;
 	}else if(typestr=='S'){
 		var prosum=0;
 		var costsum=0;
 	
 		var name="BAllCost";
 		var name_0="div_ballcost";
 		var ballcost=document.getElementById(name);//申请总费用
 		var div_ballcost=document.getElementById(name_0);

 		var name2="bamt";
 		var name3="bMugMonth";

		var bamt=document.getElementsByName(name2);//价格
		var bMugMonth=document.getElementsByName(name3);//保养月份
		for(var i=0;i<bMugMonth.length;i++){
			var num=bMugMonth[i].value;
	 		if(num==""){
	 			num="0";
	 		}
	 		var price=bamt[i].value;
	 		if(price==""){
	 			price="0";
	 		}
	 		prosum=(parseFloat(num)*parseFloat(price)).toFixed(2);
		 	costsum=parseFloat(costsum)+parseFloat(prosum);
		}
		div_ballcost.innerText=costsum;
 		ballcost.value=parseFloat(costsum);
 	}
 }
 /** 检查劳务人员费用和作业时间表	，劳务人员清单 */
 function bcheckweihuinfo(error,typestr,showtype){
 	if(typestr=='S'){//授权合同
 		if(document.getElementsByName("Bdivorder")==null || document.getElementsByName("Bdivorder").length==0){
			if(showtype=="B"){
				error="请添加电梯型号信息！";
			}
 		}else{
 			var divorder=document.getElementsByName("Bdivorder");
			var bmugstartdate=document.getElementsByName("bmugstartdate");//保养开始日期
		 	var bmugenddate=document.getElementsByName("bmugenddate");//保养结束日期
		 	var bMugMonth=document.getElementsByName("bMugMonth");//保养月份
		 	var bamt=document.getElementsByName("bamt");//价格
		 	
		 	for(var i=0;i<divorder.length;i++){
				var xuhao=divorder[i].innerText;
				
		 		if(bamt[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 价格不可以为空！\n";
		 		}else if(isNaN(bamt[i].value.trim())){
		 			error+="  序号"+xuhao+" 价格必须为数字！\n";
		 		}
		 		if(bMugMonth[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 保养月份不可以为空！\n";
		 		}else if(isNaN(bMugMonth[i].value.trim())){
		 			error+="  序号"+xuhao+" 保养月份必须为数字！\n";
		 		}
				if(bmugstartdate[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 保养开始日期不可以为空！\n";
		 		}
		 		if(bmugenddate[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 保养结束日期不可以为空！\n";
		 		}
			}
			if(error!=""){
				error="电梯型号信息\n"+error;
			}
 		}
 	}else if(typestr=='L'){//劳务人员费用和作业时间表	
		if(document.getElementsByName("Bdivorder1")==null || document.getElementsByName("Bdivorder1").length==0){
			if(showtype=="B"){
				error="请添加劳务人员费用和作业时间表！";
			}
 		}else{
			var divorder=document.getElementsByName("Bdivorder1");
			var bmugstartdate1=document.getElementsByName("bmugstartdate1");//劳务开始日期
		 	var bmugenddate1=document.getElementsByName("bmugenddate1");//劳务结束日期
		 	var bMugMonth1=document.getElementsByName("bMugMonth1");//劳务月份
		 	var bamt1=document.getElementsByName("bamt1");//工费单价/人/月
		 	var bNum1=document.getElementsByName("bNum1");//数量
			
			for(var i=0;i<divorder.length;i++){
				var xuhao=divorder[i].innerText;
				if(bNum1[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 台数不可以为空！\n";
		 		}else if(isNaN(bNum1[i].value.trim())){
		 			error+="  序号"+xuhao+" 台数必须为数字！\n";
		 		}
				if(bmugstartdate1[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 劳务开始日期不可以为空！\n";
		 		}
		 		if(bmugenddate1[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 劳务结束日期不可以为空！\n";
		 		}
		 		if(bMugMonth1[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 劳务月份不可以为空！\n";
		 		}else if(isNaN(bMugMonth1[i].value.trim())){
		 			error+="  序号"+xuhao+" 劳务月份必须为数字！\n";
		 		}
		 		if(bamt1[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 工费单价/人/月不可以为空！\n";
		 		}else if(isNaN(bamt1[i].value.trim())){
		 			error+="  序号"+xuhao+" 工费单价/人/月必须为数字！\n";
		 		}
			}
			if(error!=""){
				error="劳务人员费用和作业时间表\n"+error;
			}
 		}
 	}else if(typestr=='W'){//劳务人员清单
		if(document.getElementsByName("Bdivorder2")==null || document.getElementsByName("Bdivorder2").length==0){
			if(showtype=="B"){
				error="请添加劳务人员清单！";
			}
 		}else{
			var divorder=document.getElementsByName("Bdivorder2");
			var bPersonName=document.getElementsByName("bPersonName");//姓名
		 	var bPersonGender=document.getElementsByName("bPersonGender");//性别
		 	var bPersonAge=document.getElementsByName("bPersonAge");//年龄
		 	var bPersonDuty=document.getElementsByName("bPersonDuty");//职务
		 	var bPersonOperNo=document.getElementsByName("bPersonOperNo");//省操作证号码
		 	var bPersonCardNo=document.getElementsByName("bPersonCardNo");//身份证号码
			
			for(var i=0;i<divorder.length;i++){
				var xuhao=divorder[i].innerText;
				if(bPersonName[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 姓名不可以为空！\n";
		 		}
		 		if(bPersonGender[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 性别不可以为空！\n";
		 		}
		 		if(bPersonAge[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 年龄不可以为空！\n";
		 		}else if(isNaN(bPersonAge[i].value.trim())){
		 			error+="  序号"+xuhao+" 年龄必须为数字！\n";
		 		}else if(parseFloat(bPersonAge[i].value.trim())<=0){
		 			error+="  序号"+xuhao+" 年龄必须大于零！\n";
		 		}
		 		if(bPersonDuty[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 职务不可以为空！\n";
		 		}
		 		if(bPersonOperNo[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 省操作证号码不可以为空！\n";
		 		}
		 		if(bPersonCardNo[i].value.trim()==""){
		 			error+="  序号"+xuhao+" 身份证号码不可以为空！\n";
		 		}
			}
			if(error!=""){
				error="劳务人员清单\n"+error;
			}
 		}
 	}
 	return error;
 }
/**=================== 保养报价 end ===========================*/

 /** 输入层的值自动给站赋值 */
function setStageValue(obj,name){
	var objarr=document.getElementsByName(obj.name);
	var onum=0;
	for(var i=0;i<objarr.length;i++){
		if(objarr[i]==obj){
			onum=i;
			break;
		}
	}
	document.getElementsByName(name)[onum].value=obj.value;
}
/** 劳务开始日期,劳务结束日期,劳务月份  */
function setDateReduplicate(obj){
	var datearr=document.getElementsByName(obj.name);
	var dnum=0;
	for(var j=0;j<datearr.length;j++){
		if(datearr[j]==obj){
			dnum=j;
			break;
		}
	}
	var datevalue=datearr[dnum].value;
	for(var i=0;i<datearr.length;i++){
		if(i>dnum){
			datearr[i].value=datevalue;
		}else{
			if(datearr[i].value.trim()==""){
				datearr[i].value=datevalue;
			}
		}
	}
}


  
  