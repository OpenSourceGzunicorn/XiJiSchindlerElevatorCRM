
//增加行的模版行
sampleRows = new Array();

//设置动态表格
function setDynamicTable(tableId,sampleRowId){
	var table = document.getElementById(tableId);
	var sampleRow = document.getElementById(sampleRowId);

	var cloneRow = sampleRow.cloneNode(true);
	cloneRow.attributes.removeNamedItem("id");
	cloneRow.style.display="block";
	
	sampleRows.push(cloneRow);
	
	table.setAttribute("index",sampleRows.length - 1);
	
	table.deleteRow(sampleRow.rowIndex);
	addTableListener(tableId);
}

//增加一行
function addOneRow(tableId) {
	var tableObj = document.getElementById(tableId);
	var index = tableObj.getAttribute("index");
	tableObj.getElementsByTagName("tbody")[0].appendChild(sampleRows[index].cloneNode(true));
	cancelCheckAll(tableId);  
	
	setFlag(tableId,"add_"+tableObj.rows.length);//标记增加动作
}

//增加多行
function addRows(tableId,count) {
	var tableObj = document.getElementById(tableId);
	var index = Number(tableObj.getAttribute("index"));
	for(var i= 0;i<count;i++){
		tableObj.getElementsByTagName("tbody")[0].appendChild(sampleRows[index].cloneNode(true));
	}
	cancelCheckAll(tableId);

	setFlag(tableId,"add_"+tableObj.rows.length);//标记增加动作
}

//该方法适用于页面未加载完的情况
function addOneRow2(tableId,sampleRowId) {
	var tableObj = document.getElementById(tableId);
	var newRow = document.getElementById(sampleRowId).cloneNode(true);
	newRow.attributes.removeNamedItem("id");
	tableObj.getElementsByTagName("tbody")[0].appendChild(newRow);
}

//删除行
function deleteRow(tableId){
	
	var tableObj = document.getElementById(tableId); 
	var inputs = tableObj.getElementsByTagName("input");
	var checkboxs = new Array(); //table的所有checkbox
	for(var i=0;i<inputs.length;i++){
		if(inputs[i].type=="checkbox"){
			checkboxs.push(inputs[i]);          
		}
	}
  
  	checkboxs[0].checked = false;//全选checkbox取消选中 
  	var flag = false;
  	//在table中从下至上删除选中的行
  	for(var i=checkboxs.length-1;i>0;i--){  
		if(checkboxs[i].checked == true){
			tableObj.deleteRow(getParentNode("tr",checkboxs[i]).rowIndex);
			flag = true;
		}        
	}
  	if(flag){
  		setFlag(tableId,"delete_"+tableObj.rows.length);//标记删除动作
  	}
}

//表格checkbox全选反选, obj可以为布尔值或一个checkbox对象
function checkTableAll(tableId,obj){
	var tableObj = document.getElementById(tableId); 
	var isChecked = false;
 
	if(obj == true){
		isChecked = obj;
	}
	if(obj && obj.checked){
		isChecked = obj.checked; 
	}

	if(tableObj){ 	
		var rows = tableObj.rows
		for(var i = 0;i<rows.length;i++){
			var inputs = rows[i].cells[0].getElementsByTagName("input");
			for(var j=0;j<inputs.length;j++){
				if(inputs[j].type=="checkbox"){
					inputs[j].checked = isChecked;
				}
			}
		}  
	}

}

//全选checkbox取消选中
function cancelCheckAll(tableId,name){ 
	var tableObj = document.getElementById(tableId);  
	var obj = document.getElementsByName(name);
	for(var i=0;i<obj.length;i++){
		obj[i].checked = false;
	}
}

//删除所有行
function deleteAllRows(tableId){	
	checkTableAll(tableId);
	deleteRow(tableId);
}

//返回元素指定的父节点对象
function getParentNode(parentTagName,childObj){
	var parentObj = childObj;  
	while(parentObj){
		if(parentObj.nodeName.toLowerCase() == parentTagName.toLowerCase()){
			return parentObj;
		}
		parentObj = parentObj.parentNode;
	}
	return null;
}

//将table的tbody里所有行封装成json数组格式的字符串，
function rowsToJSONArray(tableId,key){
	var table = document.getElementById(tableId);
	var tbody = table.getElementsByTagName("tbody")[0];
	var trs = tbody.childNodes
	var result = '{"'+key+'":[';
  
	for(var i=0;i<trs.length;i++){
		var tds = trs[i].cells;
		var rowJson = "";
    
		for(var j=0;j<tds.length;j++){
			var cNodes = tds[j].childNodes;
           
			for(var k=0;k<cNodes.length;k++){
				if(cNodes[k].value && cNodes[k].name && cNodes[k].name!='' && cNodes[k].name.indexOf("_")<0 && isNaN(cNodes[k].name)){
					var temp = '"'+cNodes[k].name+'":"'+cNodes[k].value+'"';
					if(tds.length-1<=1){
						rowJson+=j<tds.length ? temp+',' : temp;
					}else{
						rowJson += j<tds.length-1? temp+',' : temp;
					}
					 
				}
			}    
    	}
		//var rowJson = '{'+rowJson+'}';
		if(rowJson!=null && rowJson!=""){
			rowJson = '{'+rowJson+'}';
		}
		result += i<trs.length-1 ? rowJson+',':rowJson;
	}
	result = result+']}';  
	return result;
}

//将table的tbody里所有行封装成json数组格式的字符串，
function rowsToJSONArray2(tableId,key){
	var table = document.getElementById(tableId);
	var tbody = table.getElementsByTagName("tbody")[0];
	var trs = tbody.childNodes
	var result = '{"'+key+'":[';
  
	for(var i=0;i<trs.length;i++){
		var tds = trs[i].cells;
		var rowJson = "";
    
		for(var j=0;j<tds.length;j++){
			var cNodes = tds[j].childNodes;
           
			for(var k=0;k<cNodes.length;k++){
				if(cNodes[k].value && cNodes[k].name && cNodes[k].name!='' && cNodes[k].name.indexOf("_")<0 && isNaN(cNodes[k].name)){
					var temp = '"'+cNodes[k].name+'":"'+cNodes[k].value+'"';
					if(cNodes.length-1<=1){
						rowJson+=k<cNodes.length ? temp+',' : temp;
					}else{
						rowJson += k<cNodes.length-1? temp+',' : temp;
					}
					 
				}
			}    
    	}
		//var rowJson = '{'+rowJson+'}';
		if(rowJson!=null && rowJson!=""){
			rowJson = '{'+rowJson+'}';
		}
		result += i<trs.length-1 ? rowJson+',':rowJson;
	}
	result = result+']}';  
	return result;
}

//设置固定行头的滚动表格
function setScrollTable(scrollBoxId,scrollTableId,displayRowsLen,isReSet){
	
	var scrollBox = document.getElementById(scrollBoxId);
	var scrollTable = document.getElementById(scrollTableId);

	var scrollHeadBoxId = "scrollHeadBox_"+scrollTableId;
	var scrollHeadTableId = "scrollHeadTable_"+scrollTableId;	

	if(document.getElementById(scrollHeadBoxId) == null){
				
		var tableWidth = scrollTable.offsetWidth;
		var header = scrollTable.tHead ? scrollTable.tHead : scrollTable.rows[0];
		
		scrollTable.style.tableLayout=""; //设置表格的列宽度由单元格内容设定
				
		var headBox = document.createElement("div");
		var cellsWidth = {};
		for(var i=0;i<header.childNodes.length;i++){
			var headerRow = header.childNodes[i];
			for(var j=0;j<headerRow.cells.length;j++){
				cellsWidth[j] = headerRow.cells[j].offsetWidth;
			}
			headerRow.style.display = "none";// 先设为隐藏再设置单元格宽度可加快运行速度
			for(var j=0;j<headerRow.cells.length;j++){
				headerRow.cells[j].width = cellsWidth[j];
			}
			headerRow.style.display = "block";
		}		
		
		headBox.id = scrollHeadBoxId;
		headBox.style.width=scrollBox.offsetWidth;
		headBox.style.overflow="hidden"
		headBox.innerHTML = "<table id="+scrollHeadTableId+">"+header.innerHTML.replace(/id=[^\s>]*/g,'')+"</table>";
		
		//创建固定表头
		scrollBox.parentNode.insertBefore(headBox,scrollBox);
		
		var table = document.getElementById(scrollHeadTableId);
		table.className=scrollTable.className;
		table.style.position = "relative"				
		table.style.right = scrollBox.scrollLeft;	
		
		//重新设置表格的列宽由表格宽度和列宽度设定
		scrollTable.attributes.removeNamedItem("width");		
		table.style.tableLayout="fixed";
		scrollTable.style.tableLayout="fixed"			
		table.style.width = tableWidth;
		scrollTable.style.width = tableWidth;

		scrollTable.style.marginTop = -(header.offsetHeight + 1);
		
		scrollBox.onscroll = function () {	
			try{document.getElementById(scrollHeadTableId).style.right = this.scrollLeft;}catch (e){}			
		}
		
		adaptiveBox(scrollBoxId,scrollTableId,getHeight(scrollTableId,displayRowsLen));
	}
		
	if(isReSet != "isReSet"){//防止重复绑定事件		
		setOnTableChange(scrollTableId,function(){
			try{
				document.getElementById(scrollHeadBoxId).parentNode.removeChild(document.getElementById(scrollHeadBoxId));
				setScrollTable(scrollBoxId,scrollTableId,displayRowsLen,"isReSet");
			}catch (e){
			}
		})
	}		
}

//滚动表格宽高自适应
function adaptiveBox(boxId,scrollTableId,maxHeight){
	var box = document.getElementById(boxId);
	var scrollTable = document.getElementById(scrollTableId);
	var header = scrollTable.tHead ? scrollTable.tHead : scrollTable.rows[0];
	var scrollHeadBox = document.getElementById("scrollHeadBox_"+scrollTableId);
	var scrollHeadTable = document.getElementById("scrollHeadTable_"+scrollTableId);

	box.style.width = 0;
    if(scrollHeadBox){
    	scrollHeadBox.style.width = 0;    	
    }
	
    box.style.width = box.parentNode.offsetWidth;
    box.style.height = scrollTable.offsetHeight - header.offsetHeight + 16;
    
    if(scrollHeadBox){
    	scrollHeadBox.style.width = box.style.width;	
    }    
    if(scrollHeadTable){
    	if(scrollTable.offsetWidth <= box.offsetWidth){
    		scrollTable.style.width = box.offsetWidth;
    		//box.style.overflowX = "hidden";
    		//box.style.height = box.offsetHeight - 16;
    	}
    	scrollHeadTable.style.width = scrollTable.offsetWidth;  	
    }
    
    if(parseInt(box.style.height) > maxHeight){
		box.style.height = maxHeight;
		box.style.overflowY = "scroll";
	}else{
		box.style.overflowY = "hidden";
	}

    window.onresize=function(){
		adaptiveBox(boxId,scrollTableId,maxHeight);
    }
}

//监听table增加或删除行
function addTableListener(tableId){	
	var tableObj = document.getElementById(tableId);	
	
	if(tableObj){
		var listenerInput = document.createElement("input");
		listenerInput.id="on"+tableId+"change";
		listenerInput.type="hidden"
		tableObj.parentNode.appendChild(listenerInput);
	}
}

//设置当table增加或删除行时调用的方法
function setOnTableChange(tableId,fn){
	var listenerInput = document.getElementById("on"+tableId+"change");
	if(listenerInput){
		if(window.attachEvent){
			listenerInput.attachEvent('onpropertychange', fn);            
		}else{    
			listenerInput.addEventListener('onpropertychange', fn , false);
		}
	}
}

//设置监听table增加或删除行的标记
function setFlag(tableId,flag){
	var listenerInput = document.getElementById("on"+tableId+"change");
	if(listenerInput){
		listenerInput.value=flag;
	}
}

//根据行数，计算高度和
function getHeight(tableId,length){
	var table = document.getElementById(tableId);
	var height = 0;
	var headLength = table.tHead ? table.tHead.childNodes.length : 0;
	var footLength = table.tFoot ? table.tFoot.childNodes.length : 0;

	length = length + headLength + footLength > table.rows.length ? table.rows.length : length + headLength;
	
	if(length == table.rows.length){
		return table.offsetHeight;
	}
	
	var i = headLength > 0 ? headLength - 1 : 0;	
	
	for(;i<length;i++){
		height += table.rows[i].offsetHeight;
	}

	return height;
}