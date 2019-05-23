
//????
function ltrim(str)
{
return str.replace(/^\s*/,"");
}

function rtrim(str)
{
return str.replace(/\s*$/,"");
}

function trim(str)
{
var strTmp;
strTmp=ltrim(str);
strTmp=rtrim(strTmp);
return strTmp;
}



//????
var Folder1=new Array("Tab1","TabActive","Page1");
var Folder2=new Array("Tab2","TabInActive","Page2");
var Folder3=new Array("Tab3","TabInActive","Page3");
var Folder4=new Array("Tab4","TabInActive","Page4");
var Folder5=new Array("Tab5","TabInActive","Page5");


//??tab?????????
function getTabPartCount()
{
var count=1;

while(eval("window.Folder"+count)){count++;}

count--;
return count;
}
function CreateFolder()
{

    document.write("<table bgColor='#6699cc' border=0 cellPadding=0 "+
           "cellSpacing=0 id='TabBar' style='background-color:transparent;' width=100%>");
    document.write("<tr height=24 vAlign='center'>");

    tabNum=getTabPartCount();
    i=1;
    while(i<tabNum+1){
        Folder=eval("Folder"+i);
        document.write("<td class='"+Folder[1]+"' id='"+Folder[0]+
                    "' align='center'>"+Folder[2]+"</td>");
        i++;
    }
    totalnum=tabNum+1;
    document.write("<td class='TabInactive'  width=100%> </td>");
    document.write("</tr><tr height=4><td colSpan='"+totalnum+"' valign=top bgColor='#6699cc'> </td></tr>");
    document.write("<tr><td bgColor='#F5F0E6' colSpan="+totalnum+" height=16 valign=top></td></tr></table>");

}

function getTabPartArray()
{
var objReturn=new Array();
var obj=null;
var count=getTabPartCount();
var i=1;
while(i<=count)
{
  obj=eval("window.Folder"+i);
  obj=document.all(obj[0]);
  if(obj!=null)
  {
   objReturn[objReturn.length]=obj;
  }
  i++;
}

return objReturn;
}

function getCurrentActiveTab()
{
var ttabArray=getTabPartArray();
for(var i=0;i<ttabArray.length;i++)
{
  if(ttabArray[i].className=="TabActive")
  { 
   return ttabArray[i];
  }
}

}
function getCurrentActivePage()
{
var obj=getCurrentActiveTab();
var pageID=obj.innerText;
var page=getPageByID(pageID); 
return page;
}
function getPageByID(pageID)
{
var obj=document.all(pageID);

return obj;
}

function getTabByID(tabID)
{
var obj=document.all(tabID);
return obj;
}

function tab_onclick()
{
var curTab=getCurrentActiveTab();
var curPage=getCurrentActivePage();

var objTab=getTDFromPoint();
var pageID=null;
var objPage=null;
if(objTab!=null)
{
  pageID=objTab.innerText;
  //alert(pageID);
  objPage=getPageByID(pageID);
  if(curTab.id==objTab.id)
  {
   return;
  }
  else
  { 
   objPage.style.display="block";
   curPage.style.display="none";

   objTab.className="TabActive";
   //alert(objTab.className);
   curTab.className="TabInActive";

  } 
}
}

function getTDFromPoint()
{
var obj=event.srcElement;
if((obj.tagName=="TD")&&((obj.className=="TabInActive")||(obj.className=="TabActive")))
{
  return obj;
}
else
{
  return null;
}
}

function fattachEvent()
{
var objTabArray=getTabPartArray();
for(i=0;i<objTabArray.length;i++)
{
  var tabID=objTabArray[i].id;
  //alert(tabID);
  objTabArray[i].attachEvent("onclick",new Function("tab_onclick()"));
}
}


CreateFolder();
fattachEvent();

