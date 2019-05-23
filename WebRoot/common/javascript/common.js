// 直接查看
function displayMethod(url,method,value){
		if(url=="" || method=="" || value=="" || value=='null'){
			alert("传入的参数有空值，不可以查看！");
			return false;
		}else{
			var URL=url+'?method='+method+'&id='+value;
				read = window.open (URL,"","width=700,height=550,resizable=yes,scrollbars=yes");
				read.window.focus();
				read.window.moveTo(150,100);		
		}
}

function displayzhzycMethod(url,method,value){
		if(url=="" || method=="" || value=="" || value=='null'){
			alert("传入的参数有空值，不可以查看！");
			return false;
		}else{
			var URL=url+'?method='+method+'&disbillNo='+value;
				read = window.open (URL,"","width=700,height=550,resizable=yes,scrollbars=yes");
				read.window.focus();
				read.window.moveTo(150,100);		
		}
}
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
function openNewWindow(url,elevatorno){

window.open(url+'?method=toPrintRecord&id='+elevatorno);
}

//特别提示框

//Author:Daviv 
//Blog:http://blog.163.com/jxdawei 
//Date:2006-10-27 
//Email:jxdawei@gmail.com 
   function sAlert(str){ 
   var msgw,msgh,bordercolor; 
   msgw=400;//提示窗口的宽度 
   msgh=100;//提示窗口的高度 
   titleheight=25 //提示窗口标题高度 
   bordercolor="#336699";//提示窗口的边框颜色 
   titlecolor="#99CCFF";//提示窗口的标题颜色 
    
   var sWidth,sHeight; 
   sWidth=document.body.offsetWidth; 
   sHeight=screen.height; 
   var bgObj=document.createElement("div"); 
   bgObj.setAttribute('id','bgDiv'); 
   bgObj.style.position="absolute"; 
   bgObj.style.top="0"; 
   bgObj.style.background="#777"; 
   bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75"; 
   bgObj.style.opacity="0.6"; 
   bgObj.style.left="0"; 
   bgObj.style.width=sWidth + "px"; 
   bgObj.style.height=sHeight + "px"; 
   bgObj.style.zIndex = "10000"; 
   document.body.appendChild(bgObj); 
    
   var msgObj=document.createElement("div") 
   msgObj.setAttribute("id","msgDiv"); 
   msgObj.setAttribute("align","center"); 
   msgObj.style.background="white"; 
   msgObj.style.border="1px solid " + bordercolor; 
      msgObj.style.position = "absolute"; 
            msgObj.style.left = "50%"; 
            msgObj.style.top = "50%"; 
            msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif"; 
            msgObj.style.marginLeft = "-225px" ; 
            msgObj.style.marginTop = -75+document.documentElement.scrollTop+"px"; 
            msgObj.style.width = msgw + "px"; 
            msgObj.style.height =msgh + "px"; 
            msgObj.style.textAlign = "center"; 
            msgObj.style.lineHeight ="25px"; 
            msgObj.style.zIndex = "10001"; 
    
     var title=document.createElement("h4"); 
     title.setAttribute("id","msgTitle"); 
     title.setAttribute("align","right"); 
     title.style.margin="0"; 
     title.style.padding="3px"; 
     title.style.background=bordercolor; 
     title.style.filter="progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=75,finishOpacity=100);"; 
     title.style.opacity="0.75"; 
     title.style.border="1px solid " + bordercolor; 
     title.style.height="18px"; 
     title.style.font="12px Verdana, Geneva, Arial, Helvetica, sans-serif"; 
     title.style.color="white"; 
     title.style.cursor="pointer"; 
     title.innerHTML="关闭"; 
     title.onclick=function(){ 
          document.body.removeChild(bgObj); 
                document.getElementById("msgDiv").removeChild(title); 
                document.body.removeChild(msgObj); 
                } 
     document.body.appendChild(msgObj); 
     document.getElementById("msgDiv").appendChild(title); 
     var txt=document.createElement("p"); 
     txt.style.margin="1em 0" 
     txt.setAttribute("id","msgTxt"); 
     txt.innerHTML=str; 
           document.getElementById("msgDiv").appendChild(txt); 
            } 
