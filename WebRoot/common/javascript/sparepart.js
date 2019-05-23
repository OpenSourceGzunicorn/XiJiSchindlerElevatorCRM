
//选择仓库名称时，显示他的结算开始日期
 function ChoiceStorageID(key){
        if(key==""){
        	return null;
        }
		var detailRequest = new AjaxRequest();
		var url = '/XJSCRM/goodsBalanceAction.do?method=getStartDate';
		var detailRequest1 = new AjaxRequest();
		var url1 = '/XJSCRM/goodsBalanceAction.do?method=getEdnDate';
		url+="&key="+key;
		url1+="&key1="+key;
		detailRequest.url = url;
		detailRequest1.url = url1;
		detailRequest.method="POST";
		detailRequest1.method="POST";
		detailRequest.onComplete = putSelectStartDate;
		detailRequest1.onComplete = putSelectEndDate;
		detailRequest.process();
		detailRequest1.process();
		
}
function putSelectEndDate(req){

   			var enddate=document.getElementById("enddate");
			enddate.value=req.responseText;			
 }	
// 让读取过来的结算开始日期到页面中显示出来的了。 
function putSelectStartDate(req){
    try{
		var objXml = req.responseXML;
		var obj=objXml.getElementsByTagName("root");
		if(obj.length==0){
			var startdate=document.getElementById("startdate");
			startdate.value=req.responseText;
			 var showDate=document.getElementById("showDate");
		    showDate.style.display="none";
		}else{
		    alert("您所选择的仓库有些单据还没有进行审核，不能够进行月结。");
		    var showDate=document.getElementById("showDate");
		    showDate.style.display="";
			var startdate=document.getElementById("startdate");
			startdate.value="";
			clearRow(matter);
	   		var keys=new Array();
			keys = ['seq','billno','operdate','operid','title'];
			var values=new Array();
	   		var xml=req.responseXML;
	   		var root=xml.getElementsByTagName("rows");
	   		for(var i=0;i<root.length;i++){
	   			var property=root[i];
	   			var cols=property.childNodes;
	   			for(var j=0;j<cols.length;j++){
	   				var colsN=cols[j].getAttribute("name");
	   				values[0]=i+1;
	   				if(colsN=="billno"){
	   					values[1]=cols[j].firstChild.nodeValue;
	   				}
	   				if(colsN=="operdate"){
	   					values[2]=cols[j].firstChild.nodeValue;
	   				}
	   				
	   				if(colsN=="operid"){
	   					values[3]=cols[j].firstChild.nodeValue;
	   				}
	   				
	   				if(colsN=="title"){
	   					values[4]=cols[j].firstChild.nodeValue;
	   				}
	   			}
	   			addInstanceRow(matter,keys,values);
	   		}
	 }
	}catch(e){}
}




/**********
 * 根据所属仓库求出其的最大月结日。
 */
 
 getMaxMonthByStorage=function(key){
 	if(key=="")return false;
 	var req=new AjaxRequest();
 	var url = '/XJSCRM/goodsBalanceAction.do?method=getMaxMonthByStorageDate';
		url+="&key="+key;
	req.url=url;
	req.method="POST";
	req.onComplete=showMaxDate;
	req.process();
 }
 
 showMaxDate=function(req){
 	//最大月结日期
 	var txt=req.responseText;
 	//页面中显示的入库或者出库日期
 	var date=document.getElementsByName("masterR3")[0].value;
 	if(date>txt){
 		//前端的脚本验证内容
 		checkDate();
 		return true;
 	}else{
 		alert("此仓库在"+date+"后（"+txt+"）已经进行了月结，不再能够进行出入库操作。");
 		return false;		
 	}
 }
  
  /**
   * 调用严整程序
   */
  function showMaxD(){
   		var storageid=document.getElementsByName("masterR1")[0].value;
   	    getMaxMonthByStorage(storageid);
  }
/***************************************
*当如果选择的采购方式是要自动的生成出库单的时候，把出库单中的必须输入项目
显示出来给用户进行输入。
****************************************/

function StockModeStyle(key){
	var smRequest=new AjaxRequest();
	var url='/XJSCRM/sparepartAction.do?method=getStockModeStyle';
	url+="&key="+key;
	smRequest.url=url;
	smRequest.method="POST";
	smRequest.onComplete=putStockMode;
	smRequest.process();
}


/********************************
*把读取过的内容，放到一个控件中去的了。然后根据这控件中的VALUE进行是否显示输入
*领料部门和领料人的信息等。
*********************************/
function putStockMode(req){
	var txt=req.responseText;
	var stockmode=document.getElementById("stockmodestyle");
	stockmode.value=txt;
	showTakeGoods();
}


//根据stockmodestyle中的Value进行判断，如果为Y,则显示那些输入的内容，否则不显示的了
function showTakeGoods(){
	var stockmode=document.getElementById("stockmodestyle");
	if(stockmode && stockmode.value=="Y"){
		 document.getElementById("takeGoods").style.display="";
		 document.getElementById("takeG").style.display="";
	}else{
		 document.getElementById("takeGoods").style.display="none";
		 document.getElementById("takeG").style.display="none";
	}
}


