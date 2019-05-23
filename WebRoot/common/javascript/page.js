function openWindow(url,formname,key1,key2,key3,key4)
{
   var obj = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
   if(obj)
   {
	var l = obj.length;
	var k=0;
	var kl = eval(formname+"."+key1).length;
	if(kl)
	{
		var t = 0;
		if(kl > l)
		{
			t = l;
		}
		else
		{
			t = kl;
		}
		
		for(i =0;i<t;i++)
		{
			if(key1 != "")
			{
			  eval(formname+"."+key1+"["+i+"]").value = obj[i][0];
			}
			if(key2 != "")
			{
				eval(formname+"."+key2+"["+i+"]").value = obj[i][1];
			}
			if(key3 != "")
			{
				eval(formname+"."+key3+"["+i+"]").value = obj[i][2];
			}
			if(key4 != "")
			{
				eval(formname+"."+key4+"["+i+"]").value = obj[i][3];
			}

		}
	}
	else if(l >=0)
	{
		if(key1 != "")
		{
		  eval(formname+"."+key1).value = obj[0][0];
		}
		if(key2 != "")
		{
			eval(formname+"."+key2).value = obj[0][1];
		}
		if(key3 != "")
		{
			eval(formname+"."+key3).value = obj[0][2];
		}
		if(key4 != "")
		{
			eval(formname+"."+key4).value = obj[0][3];
		}
	}
	
   }
}

var fileCount = 1;
/**
*add the row dyna
@param      objTable     the table that you want to add rows
@param      Names        the rows property
@param      Values       the values of the rows property
*/
function addInstanceRow(objTable,Names,Values){
 var tbodyOnlineEdit=objTable.getElementsByTagName("TBODY")[0];
 var theadOnlineEdit=objTable.getElementsByTagName("THEAD")[0];
 //true表示深度克隆
 var elm = theadOnlineEdit.lastChild.cloneNode(true)
 elm.style.display="";
        for(var i=0;i<Names.length;i++){
          setInputValue(elm,Names[i],Values[i]);
          }
        fileCount = fileCount + 1;
 tbodyOnlineEdit.insertBefore(elm);
}

/**
*set value 
@param      objRow          the row
@param      strName         the property
@param      strValue    the value
*/
function setInputValue(objRow,strName,strValue){
 var objs=objRow.all;
 for(var i=0;i<objs.length;i++){
  if(objs[i].id == 'fileid' || objs[i].id == 'checkboxkey'){
  	objs[i].value = fileCount;
  }else if(objs[i].id =='br_file'){
  	objs[i].name = 'br_file' + fileCount;
  }else if(objs[i].id == 'no_file'){
  	objs[i].name = 'no_file' + fileCount;
  }else if(objs[i].id == 'getdata' && strName == 'getdata'){
  	objs[i].href = strValue;
  }else if(objs[i].id==strName && objs[i].id != 'getdata'){
  	objs[i].value=strValue;
  }
 }
}

/*
because some of special char will infection spelling ,the string need format 
eg: ' or "
*/
function formatStr(arg){
	var rs="";
	if(arg!=null){
		var s=arg;
		var len=s.length;
		for(var i=0;i<len;i++){
			if(s.charAt(i)=='\''){
				rs+="\'";
			}else if(s.charAt(i)=='"'){
				rs+="\\\"";
			}
			else{
				rs+=s.charAt(i);
			}
		}
	}
	//rs="\""+rs+"\"";
	return rs;
}

/*
url  	:open win url
formname:form name
tableid	:table id
keys 	:dynamic array
*/
function openWindow3(url,tableid,keys)
{
  //window.open(url,'wttt');
  //var obj;
   var obj = window.showModalDialog(url,window,'dialogWidth:850px;dialogHeight:550px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
   if(obj)
   {
	var len_key=keys.length;
	
		var key="";
		var value="";
		for(i=0;i<len_key;i++){
			key+="'"+keys[i]+"',";	
		}
		
		if(key!=""){
			key=key.substring(0,key.length-1);
		}
		var len_obj = obj.length;
		len_obj2=obj[0].length;
		
		for(j=0;j<len_obj;j++){
			if(obj[j])
			{
				value="";
				for(k=0;k<len_key;k++){
					value+="'"+formatStr(obj[j][k])+"',";
					
					
				}
				if(value!=""){
					value=value.substring(0,value.length-1);
				}
				
				eval("addInstanceRow(eval(tableid),["+key+"],["+value+"])");
				

				
			}

		}
}
}

function isMoney(pObj,errMsg){
 var obj = eval(pObj);
 strRef = "1234567890.";
 for (k=0;k<obj.value.length;k++) {
  tempChar= obj.value.substring(k,k+1);
  if (strRef.indexOf(tempChar,0)==-1) {
   if (errMsg == null || errMsg =="")
    alert("???????,???");
   else
    alert(errMsg);   
   if(obj.type=="text") 
    obj.focus(); 
   return false; 
  }else{
   tempLen=obj.value.indexOf(".");
   if(tempLen!=-1){
    strLen=obj.value.substring(tempLen+1,obj.value.length);
    if(strLen.length>2){
     if (errMsg == null || errMsg =="")
      alert("???????,???");
     else
      alert(errMsg);   
     if(obj.type=="text") 
     obj.focus(); 
     return false; 
    }
   }
  }
 }
 return true;
}


//为空判断
function isEmpty(pObj,errMsg){
 var obj = eval(pObj);
 if( obj == null || Trim(obj.value) == ""){
  if (errMsg == null || errMsg =="")
   alert("????!");
  else
   alert(errMsg); 
  obj.focus(); 
  return false;
 }
 return true;
}

//checkDay start
//eg: checkDay('2005-10-01','2005-10-31',31)
function checkDay(st,en,da){
	var s=st.split("-");
	var e=en.split("-");
	var sall=0;
	var eall=0;
	//var d=da.split("-");
	if(checkyy(pInt(s[0]),2001,2100) && checkyy(pInt(e[0]),2001,2100)){
		sall=ynum(pInt(s[0])-2001)+mnum((pInt(s[0])-2001),pInt(s[1]))+pInt(s[2]);
		eall=ynum(pInt(e[0])-2001)+mnum((pInt(e[0])-2001),pInt(e[1]))+pInt(e[2]);
		if((eall-sall)<=da && (eall-sall)>=0){
			return true;
		}
		else{
			return false;
		}
	}
	else {
		return false;
	}
}

function checkyy(yy,st,en){
	if(yy>st && yy<en){
		return true;
	}
	else {
		return false;
	}
}

function ynum(yy){//yy
	return (yy*365+yy/4);
}

function mnum(yy,mm){//mm
var rm=0;
if(mm>=2){//1
	rm+=31;
}
if(mm>=3){//2
	if(yy%4==0){
		rm+=29;
	}
	else {
		rm+=28;
	}
}
if(mm>=4){//3
	rm+=31;
}
if(mm>=5){//4
	rm+=30;
}
if(mm>=6){//5
	rm+=31;
}
if(mm>=7){//6
	rm+=30;
}
if(mm>=8){//7
	rm+=31;
}
if(mm>=9){//8
	rm+=31;
}
if(mm>=10){//9
	rm+=30;
}
if(mm>=11){//10
	rm+=31;
}
if(mm>=12){//11
	rm+=30;
}
return rm;
}

function pInt(psi){
var len=psi.length;
var i=0;
var sum=0;
var temp;
for(;i<len;i++){
sum=sum*10+parseInt(psi.substring(i,i+1));
}
return sum;
}

//checkDay end 

/*
Description:可以进行多行选择进行添加 
url  	:open win url
formname:form name
tableid	:table id
keys 	:dynamic array which contain the attribute of the form 
change for the need of the proc window's width and height
*/
function openGoodsInfoWindow(url,tableid,keys)
{
   var obj = window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:500px;center:yes;help:yes;resizable:yes;status:yes');
   if(obj)
   {
	    var len_key=keys.length;
		var key="";
		var value="";
		
		//把要进行赋值的ID对象的进行赋值
		for(i=0;i<len_key;i++){
			key+="'"+keys[i]+"',";	
		}
		
		if(key!=""){
			key=key.substring(0,key.length-1);
		}
	
		//判断返回过来的数组的长度
		var len_obj = obj.length;
		//保存每个数组里面的数据的长度
		for(j=0;j<len_obj;j++){
			if(obj[j])
			{
				value="";
				for(k=0;k<len_key;k++){
					value+="'"+formatProcStr(obj[j][k])+"',";	
				}
				if(value!=""){
					value=value.substring(0,value.length-1);
				}
				eval("addInstanceRow(eval(tableid),["+key+"],["+value+"])");
			}

		}
}
}

/*
because some of special char will infection spelling ,the string need format 
eg: ' or "
*/
function formatProcStr(arg){
	var rs="";
	if(arg!=null){
		var s=arg;
		var len=s.length;
		for(var i=0;i<len;i++){
			if(s.charAt(i)=='\''){
				rs+="\\'";
			}else if(s.charAt(i)=='"'){
				rs+="\\\"";
			}
			else{
				rs+=s.charAt(i);
			}
		}
	}
	return rs;
}


//设置序号的。
function MatterSeq(){
	var seq=document.getElementsByName("seq");
	for(var i=1;i<seq.length;i++){
		seq[i].value=i;
	}
}

//计算所有的产品数量
function Numbers(){
  var num1=document.getElementsByName("num1");
  var numbers=0;
  for(var i=1;i<num1.length;i++){
  	 num1[i].value=num1[i].value==""?"0":num1[i].value;
  	 numbers+=parseFloat(num1[i].value);   
  }
  document.getElementById("numbers").value=FormatNumberFour(numbers);
}
//进货价格设置，如果为空设置为0
function PriceZero(){
	var price1=document.getElementsByName("price1");
	for(var i=1;i<price1.length;i++){
		price1[i].value=price1[i].value==""?"0":price1[i].value;
	}
}
//当税率为空时，设置为0
function CessZero(){
	var detailr1=document.getElementsByName("detailr1");
	for(var i=1;i<detailr1.length;i++){
		detailr1[i].value=detailr1[i].value==""?"0":detailr1[i].value;
	}
}

//计算合计
function OpCounts(){
    Numbers();
	PriceZero();
	CessZero();
	
	var num1=document.getElementsByName("num1");
	var price1=document.getElementsByName("price1");
	var detailr1=document.getElementsByName("detailr1");
	var r2=document.getElementsByName("r2");
	var r4=document.getElementsByName("r4");
	var r5=document.getElementsByName("r5");
	var counts=0;
	var counts2=0;
	var counts3=0;
	for(var i=1;i<num1.length;i++){
	    //合计=单位数量*单价
		r2[i].value=FormatNumberFour(parseFloat(num1[i].value)*parseFloat(price1[i].value));
		//税额=合计*税率
		r4[i].value=FormatNumberFour(parseFloat(r2[i].value)*parseFloat(detailr1[i].value)/100);
		//不含税金额=合计-税额
		r5[i].value=FormatNumberFour(parseFloat(r2[i].value)-parseFloat(r4[i].value));
		counts+=parseFloat(r2[i].value);
		counts2+=parseFloat(r4[i].value);
		counts3+=parseFloat(r5[i].value);
	}
	document.getElementById("count").value=counts;
	document.getElementById("cessMoney").value=FormatNumberFour(counts2);
	document.getElementById("notCessMoney").value=FormatNumberFour(counts3);
}

//审核时，用于设置审核数量的。
function CheckNum2(){
	var num2=document.getElementsByName("num2");
	var num1=document.getElementsByName("num1");
	var counts=0;
	for(var i=1;i<num2.length;i++){
	    //审核数量默认是为他的输入数量.
		num2[i].value=num2[i].value==""?num1[i].value:num2[i].value;
		counts+=parseFloat(num2[i].value);
	}
	document.getElementById("checkNum").value=counts;
}

/*****************************
出库管理中的JS函数
*****************************/
function OpNumbers(){
	//单位数量
	var num1=document.getElementsByName("num1");
	//出货价格
	var price1=document.getElementsByName("price1");

	//合计
	var r2=document.getElementsByName("r3");
	
	var counts=0;
	var counts1=0;
	for(var i=0;i<num1.length;i++){
		num1[i].value=num1[i].value==""?"0":num1[i].value;
		price1[i].value=price1[i].value==""?"0":price1[i].value;
		r2[i].value=FormatNumberFour(parseFloat(num1[i].value)*parseFloat(price1[i].value));
		counts+=parseFloat(r2[i].value);
		counts1+=parseFloat(num1[i].value);
	}
	document.getElementById("numCounts").value=FormatNumberFour(counts);
	document.getElementById("numbers").value=FormatNumberFour(counts1);
	document.getElementsByName("price1").value=FormatNumberFour(price1);
}
//用于到审核的时候计算审核的数量内容。
function OpCheckNumber(){
	var num2=document.getElementsByName("num2");
	var num1=document.getElementsByName("num1");
	var counts=0;
	for(var i=1;i<num2.length;i++){
	    //审核数量默认是为他的输入数量.
		num2[i].value=num2[i].value==""?num1[i].value:num2[i].value;
		counts+=parseFloat(num2[i].value);
	}
	document.getElementById("checkNumber").value=FormatNumberFour(counts);
}

//进货价格设置，如果为空设置为0
function Price(){
	var price1=document.getElementsByName("price1");
	for(var i=1;i<price1.length;i++){
		price1[i].value=price1[i].value==""?"0":price1[i].value;
	}
}

//计算合计
function Counts(){
    Numbers();
	Price();
	
	var num1=document.getElementsByName("num1");
	var price1=document.getElementsByName("price1");
	var r2=document.getElementsByName("r2");
	var counts=0;
	for(var i=1;i<num1.length;i++){
	    //合计=单位数量*单价
		r2[i].value=parseFloat(num1[i].value)*parseFloat(price1[i].value);

		counts+=parseFloat(r2[i].value);

	}
	document.getElementById("count").value=counts;
}

//用于归还时
function BNumbers(){
	   //checknum(num);
	//借用数量
	var num1=document.getElementsByName("num1");

	//归还数量
	var detailr1=document.getElementsByName("detailr1");	
	//剩余数量
	var num2=document.getElementsByName("num2");

	for(var i=0;i<num1.length;i++){
		num1[i].value=num1[i].value==""?"0":num1[i].value;
		detailr1[i].value=detailr1[i].value==""?"0":detailr1[i].value;
		if(isNaN(parseInt(detailr1[i].value))){
		      alert("不能输入字符，请输入数字");//不要提示可去调
		      detailr1[i].value='';
		      return '';
		}
		if(detailr1[i].value.indexOf("-")!=-1){
			      alert("不能输入负数，请输入正数");
			      detailr1[i].value='';
			      return '';
		      }
		      if(detailr1[i].value.indexOf(".")!=-1){
			      alert("不能输入小数，请输入整数");
			      detailr1[i].value='';
			      return '';
		      }
		if(parseInt(num1[i].value)<parseInt(detailr1[i].value)){
		         alert("归还数量不能大于借用数量");
		         detailr1[i].value='';
                 return '';
		}
		num2[i].value=parseInt(num1[i].value)-parseInt(detailr1[i].value);
	}
}


function checknum(num){
 
		var err = "";
		var qty="";
	    if (isNaN(num)){
		      alert("请输入数字");//不要提示可去调
		      return '';
	    }
	    else{
	          if(num.indexOf("-")!=-1){
			      alert("请输入正数");
			      return '';
		      }
	    }
	    return num;
}

/**
* 可用库存数量，到出库管理中，要进行这些数据的判断，出库的数量，不能够大于库存中存在的数量
*/
function opinionCheckOutUseqty(value){
//alert(Math.abs(value.value));
    var redblueflag=document.getElementsByName("redblueflag");
	var materialscode=document.getElementsByName("materialscode");
	for(var i=2;i<materialscode.length;i++){
		if(redblueflag[0].value=="B"&&redblueflag[0].checked==true){
			if(materialscode[i].value==materialscode[i-1].value){
				alert("不能重复出库同一物品！");
				value.value="";
				return false;
				}
			}else{
				return true;
			}
	}
	var num1=document.getElementsByName("num1");
	var userQty=document.getElementsByName("userQty");
    var rowIndex=parseInt(value.parentElement.parentElement.rowIndex);
    if(parseFloat(value.value)>parseFloat(userQty[rowIndex-1].value)){
    	alert("此物品的当前可用库存不足"+parseFloat(value.value)+" ,请及时进货。");
    	value.value="";
    	return;
    }
}


//进行四舌五入(保留2位小数)
function FormatNumber(num)  
{
	var  dd=1;  
	var  tempnum;  
	for(i=0;i<2;i++){  
	   dd*=10;  
	} 
	var temp=num==""?0:num;
	 
	if(isNaN(temp)){
		return 0;
	} 
 	tempnum=num*dd;  
	tempnum=Math.round(tempnum);  
    return tempnum/dd;
} 
//进行四舌五入(保留4位小数)
function FormatNumberFour(num)  
{
	var  dd=1;  
	var  tempnum;  
	for(i=0;i<2;i++){  
	   dd*=100;  
	} 
	var temp=num==""?0:num;
	 
	if(isNaN(temp)){
		return 0;
	} 
 	tempnum=num*dd;  
	tempnum=Math.round(tempnum);  
    return tempnum/dd;
} 
//delete table row
/**
*@param               objTable      : the id of the table you want to delete rows
*@param               theForm       : the form of the page
*@param               checkboxId    : the id of the checkbox control in the rows 
*/
function deleteLine(objTable,theForm,checkboxId){
 var tbodyOnlineEdit=objTable.getElementsByTagName("TBODY")[0];
 for(var i=0; i<=tbodyOnlineEdit.children.length-1 ; i++ )
	{
		if(eval("document."+theForm+"."+checkboxId + "[" + parseInt(parseInt(i)+1) +"]"+".checked") == true)
		{
			tbodyOnlineEdit.deleteRow(i);
			i=i-1;
		}
	}
}
function keyCodeNum(){
    if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==13 || event.keyCode==46){
    } else{
		event.keyCode=0;
   }
}
/*
 * 描述：使innerHTML中包含的javascript执行
 * 允许插入的 HTML 代码中包含 script 和 style
 * docObj: 合法的 DOM 树中的节点
 * htmlCode: 合法的 HTML 代码
 */
var setInnerHTML = function (docObj, htmlCode) {
 var ua = navigator.userAgent.toLowerCase();
 if (ua.indexOf('msie') >= 0 && ua.indexOf('opera') < 0) {
 htmlCode = '<div style="display:none">for IE</div>' + htmlCode;
 htmlCode = htmlCode.replace(/<script([^>]*)>/gi,
 '<script$1 defer="true">');
 docObj.innerHTML = htmlCode;
 docObj.removeChild(docObj.firstChild);
 }
 else {
 var docObj_next = docObj.nextSibling;
 var docObj_parent = docObj.parentNode;
 docObj_parent.removeChild(docObj);
 docObj.innerHTML = htmlCode;
 if (docObj_next) {
 docObj_parent.insertBefore(docObj, docObj_next)
 } else {
 docObj_parent.appendChild(docObj);
 }
 }
 
 /*
 * 描述：通过重定义 document.write 函数，避免在使用 setInnerHTML 时，
 * 插入的 HTML 代码中包含 document.write ，导致原页面受到破坏的情况。
 */
 document.write = function() {
	 var body = document.getElementsByTagName('body')[0];
	 for (var i = 0; i < arguments.length; i++) {
	 argument = arguments[i];
	 if (typeof argument == 'string') {
	 var docObj = body.appendChild(document.createElement('div'));
	 setInnerHTML(docObj, argument)
	 }
	 }
 }
}

function openWindow1(url,formname,keys)
{
   var obj = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
   
	if(obj)
   {
   		var len_key=keys.length;
		for(j=0;j<len_key;j++){
		
			if(keys[j]!=null && keys[j]!=''){
				obj[0][j]="\""+formatStr(obj[0][j])+"\"";
				//alert(formname + "." + keys[j] + ".value=" + obj[0][j]);
				eval(formname + "." + keys[j] + ".value=" + obj[0][j]);
			}
		}
   }

}
function deleteRow10(objTable,theForm,checkboxId){
 var tbodyOnlineEdit=objTable.getElementsByTagName("TBODY")[0];
 for(var i=0; i<=tbodyOnlineEdit.children.length-1 ; i++ )
	{
		//alert(eval("document."+theForm+"."+checkboxId + "[" + parseInt(parseInt(i)+1) +"]"+".checked"));
		if(eval("document."+theForm+"."+checkboxId + "[" + parseInt(parseInt(i)+1) +"]"+".checked") == true)
		{
			tbodyOnlineEdit.deleteRow(i);
			i=i-1;
		}
	}
}
	 