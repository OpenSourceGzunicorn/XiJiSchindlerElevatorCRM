var inputFocus;//??????????input
var bKeyDown=false;//?????????????????????true

function setRowClass(obj,className){
 obj.className=className;
 var oldClass,toClass;
 if(className=="tableData")  {oldClass="inputTableDataHit";toClass="inputTableData";}
 if(className=="tableDataHit") {oldClass="inputTableData";toClass="inputTableDataHit";}
 var objsInput=obj.all;
 for(var i=0;i<objsInput.length;i++)
  if(objsInput[i].tagName=="INPUT")
	 if(objsInput[i].className==oldClass)
	    objsInput[i].className=toClass;
}

function lightonRow(obj){
 if(obj.tagName!="TR")return;

 //????????????????
        var tableOnlineEdit=obj.parentElement;
        while(tableOnlineEdit.tagName!="TABLE")tableOnlineEdit=tableOnlineEdit.parentElement;
 var objsCheckBox=tableOnlineEdit.all("checkLine");
 for(var iCheckBox=1;iCheckBox<objsCheckBox.length;iCheckBox++)
  if(objsCheckBox[iCheckBox].checked==false) setRowClass(tableOnlineEdit.rows[iCheckBox+1],"tableData");

 //??????????
 setRowClass(obj,"tableDataHit");
}


//??obj?????TagName
function getUpperObj(obj,TagName){
 var strTagName=TagName.toUpperCase();
 var objUpper=obj.parentElement;
 while(objUpper){
  if(objUpper.tagName==strTagName) break;
  objUpper=objUpper.parentElement;
 }
 return objUpper;
}

function getPosition(obj,pos){
   var t=eval("obj."+pos);
   while(obj=obj.offsetParent){
      t+=eval("obj."+pos);
   }
   return t;
}
function showInputSelect(obj,objShow){
 inputFocus=obj;//??????input?????
 objShow.style.top =getPosition(obj,"offsetTop")+obj.offsetHeight+2;
 objShow.style.left=getPosition(obj,"offsetLeft");
 objShow.style.width=obj.offsetWidth;
 objShow.value=obj.value;
 objShow.style.display="block";
}

function setInputFromSelect(objTo,objShow){
 objTo.value=objShow.options[objShow.selectedIndex].value;
 //objShow.style.display="none";
}

function hideInput(obj){
 obj.style.display="none";
}

function clearRow(objTable){
  var tbodyOnlineEdit=objTable.getElementsByTagName("TBODY")[0];
  for (var i=tbodyOnlineEdit.children.length-1;i>=0;i--)
    tbodyOnlineEdit.deleteRow(i);
}


//¸´Ñ¡¿òÈ«Ñ¡
  function selAllNodezhz(obj,name){
	var idss=document.getElementsByName(name);
		for(var i=idss.length-1;i>=0;i--){
			idss[i].checked=obj.checked;
		}
	}

function deleteRow(objTable){
 var tbodyOnlineEdit=objTable.getElementsByTagName("TBODY")[0];
 for (var i=tbodyOnlineEdit.children.length-1; i>=0 ; i-- )
  if (tbodyOnlineEdit.children[i].firstChild.firstChild.checked)
   tbodyOnlineEdit.deleteRow(i);
}


function addRow(objTable){
 var tbodyOnlineEdit=objTable.getElementsByTagName("TBODY")[0];
 var theadOnlineEdit=objTable.getElementsByTagName("THEAD")[0];
 var elm = theadOnlineEdit.lastChild.cloneNode(true);
 elm.style.display="";
 tbodyOnlineEdit.insertBefore(elm);
}

//???????objRow????strName???strValue
function setInputValue(objRow,strName,strValue){
 var objs=objRow.all;
 
 for(var i=0;i<objs.length;i++){
  if(objs[i].id==strName){
	  if(objs[i].tagName=="SELECT"){
		 if(strValue!="")
		   
		   objs[i].options[strValue].selected=true;
	  }else{
		 objs[i].value=strValue;
	  }
  }
 }
}

//?????????
function addInstanceRow(objTable,Names,Values){
 var tbodyOnlineEdit=objTable.getElementsByTagName("TBODY")[0];
 var theadOnlineEdit=objTable.getElementsByTagName("THEAD")[0];
 var elm = theadOnlineEdit.lastChild.cloneNode(true)
 elm.style.display="";
        for(var i=0;i<Names.length;i++)
          setInputValue(elm,Names[i],Values[i]);
 tbodyOnlineEdit.insertBefore(elm);
}

//???????????
function setOnlineEditCheckBox(obj,value){
 var tbodyOnlineEdit=obj.getElementsByTagName("TBODY")[0];
 for (var i=tbodyOnlineEdit.children.length-1; i>=0 ; i-- )
  tbodyOnlineEdit.children[i].firstChild.firstChild.checked=value;
}

//?????????????,???????????????????onKeyDown="navigateKeys()" onKeyUp="setKeyDown(false)"
//??????????"->"??????????????????????
//??????Tab???
function navigateKeys(){
 if(bKeyDown) return;
 bKeyDown=true;
 var elm=event.srcElement;
 if(elm.tagName!="INPUT") return;//????INPUT??????????

 var objTD=elm.parentElement;
 var objTR=objTD.parentElement;
 var objTBODY=objTR.parentElement.parentElement;
 var objTable=objTBODY.parentElement;

 var nRow=objTR.rowIndex;
 var nCell=objTD.cellIndex;

 var nKeyCode=event.keyCode;
 switch(nKeyCode){
  case 37://<-
   if(getCursorPosition(elm)>0)return;
   nCell--;
   if(nCell==0){
    nRow--;//??????
    nCell=objTR.cells.length-1;//????
   }
   break;
  case 38://^
   nRow--;
   break;
  case 39://->
   if(getCursorPosition(elm)<elm.value.length)return;
   nCell++;
   if(nCell==objTR.cells.length){    
    nRow++;//?????????
    nCell=1;//???
   }
   break;
  case 40://\|/
   nRow++;
   if(nRow==objTBODY.rows.length){    
    addRow(objTable);//??????
    nCell=1;//??????
   }
   break;
  case 13://Enter
   nCell++;
   if(nCell==objTR.cells.length){    
    nRow++;//?????????
    nCell=1;//???
   }
   if(nRow==objTBODY.rows.length){    
    addRow(objTable);//??????
    nCell=1;//??????
   }

   break;
  default://do nothing
   return;
 }
 if(nRow<2 || nRow>=objTBODY.rows.length || nCell<1 ||nCell>=objTR.cells.length) return;

 objTR=objTBODY.rows[nRow];
 objTD=objTR.cells[nCell];
 var objs=objTD.all;
 for(var i=0;i<objs.length;i++){
  //????ojbs[0],?????????????,???????
  try{
   lightonRow(objTR);
   objs[i].focus();//setCursorPosition(objs[i],-1);
   return;
  }catch(e){
   continue;
   //if error occur,continue to next element
  }
 }//end for objs.length
}


//????????bKeyDown??
function setKeyDown(status){
 bKeyDown=status;
}


//???????
function getCursorPosition(obj){
 var qswh="@#%#^&#*$"
 obj.focus();
 rng=document.selection.createRange();
 rng.text=qswh;
 var nPosition=obj.value.indexOf(qswh)
 rng.moveStart("character", -qswh.length)
 rng.text="";
 return nPosition;
}


//??????
function setCursorPosition(obj,position){
 range=obj.createTextRange(); 
 range.collapse(true); 
 range.moveStart('character',position); 
 range.select();
}



function deleteRowSpecial(objTable){

 var tbodyOnlineEdit=objTable.getElementsByTagName("TBODY")[0];
  for (var i=0; i<=tbodyOnlineEdit.children.length-1 ; i++ )
	{
		if (document.quoteConnectForm.no[i+1].checked == true)
		{
			tbodyOnlineEdit.deleteRow(i);
			i=i-1;
		}
	}

}
