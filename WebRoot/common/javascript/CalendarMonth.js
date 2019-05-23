//----------------------------------------------------------------------------
//  这是一个日历 Javascript 页面脚本控件，适用于微软的 IE （5.0以上）浏览器
//  主调用函数是 setday(this,[object])和setday(this)，[object]是控件输出的控件名，举两个例子：
//  一、<input name=txt><input type=button value=setday onclick="setday(this,document.all.txt)">
//  二、<input onfocus="setday(this)">
//  本日历的年份限制是（1000 - 9999）
//  按ESC键关闭该控件
//  在年和月的显示地方点击时会分别出年与月的下拉框
//  控件外任意点击一点即可关闭该控件
/*
Ver	2.0
修改内容：
1.*全新修改使用iframe作为日历的载体，不再被select和flash等控件挡住。
2.修正了移植到iframe后移动日历控件的问题。

Ver	1.5
修改内容：
1.选中的日期显示为凹下去的样式
2.修改了关闭层的方法，使得失去焦点的时候能够关闭日历。
3.修改按键处理，使得Tab切换焦点的时候可以关闭控件
4.*可以自定义日历是否可以拖动

Ver 1.4
修改内容：
1.修正选中年/月份下拉框后按Esc键导致年/月份不显示的问题
2.修正使用下拉框选择月份造成的日期错误（字符串转化为数字的问题）
3.*外观样式的改进，使得控件从丑小鸭变成了美丽的天鹅，从灰姑娘变成了高贵的公主
4.再次增大年/月份的点击空间，并对下拉框的位置稍作调整

注：*号表示比较关键的改动

说明：
1.受到iframe的限制，如果拖动出日历窗口，则日历会停止移动。
*/

//==================================================== 参数设定部分 =======================================================
var bMoveable=true;		//设置日历是否可以拖动

//==================================================== WEB 页面显示部分 =====================================================
var strFrame;		//存放日历层的HTML代码
document.writeln('<iframe id=meizzDateLayer frameborder=0 style="position:absolute;width:145;z-index:9998;display:none;"></iframe>');
strFrame='<link rel="stylesheet" type="text/css" href="/XJSCRM/common/css/SelectMonth.css">';
strFrame+='<scr' + 'ipt language=javascript>';
strFrame+='var datelayerx,datelayery;	/*存放日历控件的鼠标位置*/';
strFrame+='var bDrag;	/*标记是否开始拖动*/';
strFrame+='function document.onmousemove()	/*在鼠标移动事件中，如果开始拖动日历，则移动日历*/';
strFrame+='{if(bDrag && window.event.button==1)';
strFrame+='	{var DateLayer=parent.document.all.meizzDateLayer.style;';
strFrame+='		DateLayer.posLeft += window.event.clientX-datelayerx;/*由于每次移动以后鼠标位置都恢复为初始的位置，因此写法与div中不同*/';
strFrame+='		DateLayer.posTop += window.event.clientY-datelayery;}}';
strFrame+='function DragStart()		/*开始日历拖动*/';
strFrame+='{var DateLayer=parent.document.all.meizzDateLayer.style;';
strFrame+='	datelayerx=window.event.clientX;';
strFrame+='	datelayery=window.event.clientY;';
strFrame+='	bDrag=true;}';
strFrame+='function DragEnd(){		/*结束日历拖动*/';
strFrame+='	bDrag=false;}';
strFrame+='</scr' + 'ipt>';
strFrame+='<div style="z-index:9999;position: absolute; left:0; top:0;" onselectstart="return false">';
strFrame+='<span id=tmpSelectYearLayer style="z-index: 9999;position: absolute;top: 3; left: 19;display: none;"></span>';
strFrame+='<span id=tmpSelectMonthLayer style="z-index: 9999;position: absolute;top: 3; left: 78;display: none"></span>';
strFrame+='<table border=1 cellspacing=0 cellpadding=0 class="outtable" bordercolor=#80a6f6>';
strFrame+='<tr id=trSelectYM><td bgcolor=#ffffff>';
strFrame+='<table border=0 cellspacing=1 cellpadding=0 class="yearandmonthtable">';
strFrame+='<tr align=center>';
strFrame+='<td align=center class="nexttd" onclick="parent.meizzPrevM()" title="向前翻 1 月">&lt;</td>';
strFrame+='<td align=center class="yeartd" onclick="parent.tmpSelectYearInnerHTML(this.innerText.substring(0,4))" title="点击这里选择年份"><span id="meizzYearHead"></span></td>';
strFrame+='<td align=center class="monthtd" onclick="parent.tmpSelectMonthInnerHTML(this.innerText.length==3?this.innerText.substring(0,1):this.innerText.substring(0,2))" title="点击这里选择月份"><span id="meizzMonthHead"></span></td>';
strFrame+='<td align=center class="nexttd" onclick="parent.meizzNextM()" title="向后翻 1 月">&gt;</td>';
strFrame+='</tr>';
strFrame+='</table>';
strFrame+='</td></tr>';
strFrame+='<tr id=trWeekTitle><td>';
strFrame+='<table border=1 cellspacing=0 cellpadding=0 ' + (bMoveable? 'onmousedown="DragStart()" onmouseup="DragEnd()"':'') + ' class="weektable" bordercolorlight=#80A6F6 bordercolordark=#ffffca style="cursor:' + (bMoveable ? 'move':'default') + '">';
strFrame+='<tr align=center valign=bottom>';
strFrame+='<td class="weektd">日</td>';
strFrame+='<td class="weektd">一</td>';
strFrame+='<td class="weektd">二</td>';
strFrame+='<td class="weektd">三</td>';
strFrame+='<td class="weektd">四</td>';
strFrame+='<td class="weektd">五</td>';
strFrame+='<td class="weektd">六</td>';
strFrame+='</tr>';
strFrame+='</table>';
strFrame+='</td></tr>';
strFrame+='<tr id=trMonthDate><td>';
strFrame+='<table border=1 cellspacing=2 bordercolordark=#ffffff class="31table">';
var n=0; for (j=0;j<5;j++){ strFrame+= ' <tr align=center>'; for (i=0;i<7;i++){
strFrame+='<td id=meizzDay'+n+' class=31td onclick=parent.meizzDayClick(this.innerText,0)></td>';n++;}
strFrame+='</tr>';}
strFrame+='<tr align=center>';
for (i=35;i<37;i++)strFrame+='<td id=meizzDay'+i+' class=31td onclick="parent.meizzDayClick(this.innerText,0)"></td>';
strFrame+='<td colspan=5 align=right><span onclick=parent.sureLayer() class="closestyle"><u>确定</u></span><span style="width:6px;"></span><span onclick=parent.resetLayer() class="closestyle"><u>重置</u></span><span style="width:6px;"></span><span onclick=parent.closeLayer() class="closestyle"><u>取消</u></span></td></tr>';
strFrame+='</table></td></tr><tr id=trTime><td>';
strFrame+='<table border=0 cellspacing=1 cellpadding=0 class="timetable">';
strFrame+='<tr><td align=center><span id=spanHour class="timetd"></span><span id=spanMinute class="timetd"></span><span id=spanSecond class="timetd"></span></td></tr>';
strFrame+='</table>';
strFrame+='</td></tr>';
strFrame+='</table>';
strFrame+='</div>';

window.frames.meizzDateLayer.document.writeln(strFrame);
window.frames.meizzDateLayer.document.close();		//解决ie进度条不结束的问题

//==================================================== WEB 页面显示部分 ======================================================
var outObject;
var outButton;		//点击的按钮
var hiddenTimeVal="";
var onFlag="";
var odatelayer=window.frames.meizzDateLayer.document.all;		//存放日历对象
function setmonth(tt,obj,showDateTime) //主调函数
{
        if (arguments.length >  3){alert("对不起！传入本控件的参数太多！");return;}
        if (arguments.length == 0){alert("对不起！您没有传回本控件任何参数！");return;}
        var dads  = document.all.meizzDateLayer.style;
        var th = tt;
        var ttop  = tt.offsetTop;     //TT控件的定位点高
        var thei  = tt.clientHeight;  //TT控件本身的高
        var twid  = tt.clientWidth;   //TT控件本身的宽
        var tleft = tt.offsetLeft;    //TT控件的定位点宽
        var ttyp  = tt.type;          //TT控件的类型
        while (tt = tt.offsetParent){ttop+=tt.offsetTop; tleft+=tt.offsetLeft;}
        var oWidth = dads.width;
        oWidth = oWidth.replace("px","");
        var tWidth = parseInt(oWidth)+parseInt(tleft);
        var bodyWidth = document.body.clientWidth;
        if (tWidth > bodyWidth)
        {
          var temp = parseInt(bodyWidth) - oWidth;
          if (temp < 0)
            dads.left = 0;
          else
            dads.left = temp;
        }
        else
          dads.left = tleft;

//##
	if ( arguments.length < 3 )
	{
	        outObject = (arguments.length == 1) ? th : obj;
	        outButton = (arguments.length == 1) ? null : th;	//设定外部点击的按钮
	}else{
        	outObject = ( arguments[1] == null ) ? th : obj;
	        outButton = ( arguments[1] == null ) ? null : th;	//设定外部点击的按钮
        }

        //根据当前输入框的日期显示日历的年月
        var rr = outObject.value;
        var r = rr.split(" ");

        if(r!=null){
          if(r.length==1){    //没有时分秒的情况
            var r1 = rr.split("-");
            if(r1.length==3){
              hiddenTimeVal = rr;
              meizzSetDay(r1[0],r1[1]-1+1);
            }else{
              hiddenTimeVal = "";
              meizzSetDay(new Date().getFullYear(), new Date().getMonth()+1);
            }
          }else{
            if(r.length==2){   //有时分秒的情况
              var r1 = r[0].split("-");
              if(r1.length==3){
                hiddenTimeVal = rr;
                meizzSetDay(r1[0],r1[1]-1+1);
              }else{
                hiddenTimeVal = "";
                meizzSetDay(new Date().getFullYear(), new Date().getMonth()+1);
              }
            }else{
              hiddenTimeVal="";
              meizzSetDay(new Date().getFullYear(), new Date().getMonth()+1);
            }
          }
        }else{
          hiddenTimeVal = "";
          meizzSetDay(new Date().getFullYear(), new Date().getMonth()+1);
        }
        
//##        outObject.showTime = "1";   //工作流用
//##	加上不显示日历的功能
	if ( ( outObject.showDate != null && outObject.showDate == "0" )
	  || ( showDateTime != null && showDateTime == 'showDate=0' ) )
	{
	        dads.height = "28px";
		odatelayer.trSelectYM.style.display = "none";
		odatelayer.trWeekTitle.style.display = "none";
		odatelayer.trMonthDate.style.display = "none";
          	odatelayer.trTime.style.display = ""; //显示时分秒的选择框
	}else{
		odatelayer.trSelectYM.style.display = "";
		odatelayer.trWeekTitle.style.display = "";
		odatelayer.trMonthDate.style.display = "";
	
	        if ( ( outObject.showTime != null && outObject.showTime == "1" )
	          || ( showDateTime != null && showDateTime == 'showTime=1' ) )
	        {
	          dads.height = "215px";
	          odatelayer.trTime.style.display = ""; //不隐藏时分秒的选择框
	        }else{
	          dads.height = "190px";
	          odatelayer.trTime.style.display = "none"; //隐藏时分秒的选择框
	        }
	}
        var oHeight = dads.height;
        oHeight = oHeight.replace("px","");
        var divScrollTop = 0;
/*##        try{
          divScrollTop = GenForm.scrollTop;    //工作流用
        }catch(e){}
*/
        var tHeight = parseInt(oHeight)+parseInt(ttop)+parseInt(thei)-parseInt(divScrollTop);
        var bodyHeight = document.body.clientHeight;
        if (tHeight > bodyHeight)
        {
          var temp = parseInt(ttop)-parseInt(oHeight)-parseInt(divScrollTop);
          if (temp < 0)
            dads.top = 0;
          else
            dads.top = temp;
        }
        else
          dads.top  = (ttyp=="image")? ttop+thei-divScrollTop : ttop+thei-divScrollTop+8;

        if(r.length==2){
          var tmp=r[1].split(":");
          if(tmp.length==3)
            tmpSelectHourInnerHTML(tmp[0],tmp[1],tmp[2]); //时分秒的设置
          else
            tmpSelectHourInnerHTML("0","0","0"); //时分秒的设置
        }else{
          tmpSelectHourInnerHTML("0","0","0"); //时分秒的设置
        }

//##	如果是不显示日历的情况，加上显示时间的功能
	if ( outObject != null && odatelayer.trSelectYM.style.display == "none" )
	{
          var tmp=rr.split(":");
          if(tmp.length==3)
            tmpSelectHourInnerHTML(tmp[0],tmp[1],tmp[2]); //时分秒的设置
          else
            tmpSelectHourInnerHTML("0","0","0"); //时分秒的设置
	}

        dads.display = '';

        event.returnValue=false;
}

var MonHead = new Array(12);    		   //定义阳历中每个月的最大天数
    MonHead[0] = 31; MonHead[1] = 28; MonHead[2] = 31; MonHead[3] = 30; MonHead[4]  = 31; MonHead[5]  = 30;
    MonHead[6] = 31; MonHead[7] = 31; MonHead[8] = 30; MonHead[9] = 31; MonHead[10] = 30; MonHead[11] = 31;

var meizzTheYear=new Date().getFullYear(); //定义年的变量的初始值
var meizzTheMonth=new Date().getMonth()+1; //定义月的变量的初始值
var meizzWDay=new Array(37);               //定义写日期的数组

function document.onclick() //任意点击时关闭该控件	//ie6的情况可以由下面的切换焦点处理代替
{
  with(window.event)
  { if (srcElement.getAttribute("Author")==null && srcElement != outObject && srcElement != outButton)
    closeLayer();
  }
}

function document.onkeyup()		//按Esc键关闭，切换焦点关闭
  {
    if (window.event.keyCode==27){
                if(outObject)outObject.blur();
                closeLayer();
        }
        else if(document.activeElement)
                if(document.activeElement.getAttribute("Author")==null && document.activeElement != outObject && document.activeElement != outButton)
                {
                        closeLayer();
                }
  }

function meizzWriteHead(yy,mm)  //往 head 中写入当前的年与月
  {
        odatelayer.meizzYearHead.innerText  = yy + " 年";
    odatelayer.meizzMonthHead.innerText = mm + " 月";
  }

function tmpSelectYearInnerHTML(strYear) //年份的下拉框
{
  if (strYear.match(/\D/)!=null){alert("年份输入参数不是数字！");return;}
  var m = (strYear) ? strYear : new Date().getFullYear();
  if (m < 1000 || m > 9999) {alert("年份值不在 1000 到 9999 之间！");return;}
  var n = m - 10;
  if (n < 1000) n = 1000;
  if (n + 26 > 9999) n = 9974;
  var s = "<select name=tmpSelectYear style='font-size: 12px' "
     s += "onblur='document.all.tmpSelectYearLayer.style.display=\"none\"' "
     s += "onchange='document.all.tmpSelectYearLayer.style.display=\"none\";"
     s += "parent.meizzTheYear = this.value; parent.meizzSetDay(parent.meizzTheYear,parent.meizzTheMonth)'>\r\n";
  var selectInnerHTML = s;
  for (var i = n; i < n + 26; i++)
  {
    if (i == m)
       {selectInnerHTML += "<option Author=wayx value='" + i + "' selected>" + i + "年" + "</option>\r\n";}
    else {selectInnerHTML += "<option Author=wayx value='" + i + "'>" + i + "年" + "</option>\r\n";}
  }
  selectInnerHTML += "</select>";
  odatelayer.tmpSelectYearLayer.style.display="";
  odatelayer.tmpSelectYearLayer.innerHTML = selectInnerHTML;
  odatelayer.tmpSelectYear.focus();
}

function tmpSelectMonthInnerHTML(strMonth) //月份的下拉框
{
  if (strMonth.match(/\D/)!=null){alert("月份输入参数不是数字！");return;}
  var m = (strMonth) ? strMonth : new Date().getMonth() + 1;
  var s = "<select name=tmpSelectMonth style='font-size: 12px' "
     s += "onblur='document.all.tmpSelectMonthLayer.style.display=\"none\"' "
     s += "onchange='document.all.tmpSelectMonthLayer.style.display=\"none\";"
     s += "parent.meizzTheMonth = this.value; parent.meizzSetDay(parent.meizzTheYear,parent.meizzTheMonth)'>\r\n";
  var selectInnerHTML = s;
  for (var i = 1; i < 13; i++)
  {
    if (i == m)
       {selectInnerHTML += "<option Author=wayx value='"+i+"' selected>"+i+"月"+"</option>\r\n";}
    else {selectInnerHTML += "<option Author=wayx value='"+i+"'>"+i+"月"+"</option>\r\n";}
  }
  selectInnerHTML += "</select>";
  odatelayer.tmpSelectMonthLayer.style.display="";
  odatelayer.tmpSelectMonthLayer.innerHTML = selectInnerHTML;
  odatelayer.tmpSelectMonth.focus();
}

function tmpSelectHourInnerHTML(strHour,strMinute,strSecond) //时分秒的下拉框
{
  if (strHour.match(/\D/)!=null){strHour="0";}//alert("时输入参数不是数字！");return;}
  if (strMinute.match(/\D/)!=null){strHour="0";}//alert("分输入参数不是数字！");return;}
  if (strSecond.match(/\D/)!=null){strHour="0";}//alert("秒输入参数不是数字！");return;}

  var m = (strHour) ? strHour : new Date().getHour();
  var s = "<select id=selHour name=tmpSelectHour style='width:37px;height:20px;font-size:12px' "
     s += "class='select' onchange='parent.changeHMS();'>\r\n";
  var selectInnerHTML = s;
  for (var i = 0; i < 24; i++)
  {
    if (i == m)
       {selectInnerHTML += "<option value='"+i+"' selected>"+i+""+"</option>\r\n";}
    else {selectInnerHTML += "<option value='"+i+"'>"+i+""+"</option>\r\n";}
  }
  selectInnerHTML += "</select>：";
  odatelayer.spanHour.innerHTML = selectInnerHTML;

  m = (strMinute) ? strMinute : new Date().getMinute();
  s = "<select id=selMinute name=tmpSelectMinute style='width:37px;height:20px;font-size:12px' "
     s += "class='select' onchange='parent.changeHMS();'>\r\n";
  selectInnerHTML = s;
  for (var i = 0; i < 60; i++)
  {
    if (i == m)
       {selectInnerHTML += "<option value='"+i+"' selected>"+i+""+"</option>\r\n";}
    else {selectInnerHTML += "<option value='"+i+"'>"+i+""+"</option>\r\n";}
  }
  selectInnerHTML += "</select>：";
  odatelayer.spanMinute.innerHTML = selectInnerHTML;

  m = (strSecond) ? strSecond : new Date().getSecond();
  s = "<select id=selSecond name=tmpSelectSecond style='width:37px;height:20px;font-size:12px' "
     s += "class='select' onchange='parent.changeHMS();'>\r\n";
  selectInnerHTML = s;
  for (var i = 0; i < 60; i++)
  {
    if (i == m)
       {selectInnerHTML += "<option value='"+i+"' selected>"+i+""+"</option>\r\n";}
    else {selectInnerHTML += "<option value='"+i+"'>"+i+""+"</option>\r\n";}
  }
  selectInnerHTML += "</select>";
  odatelayer.spanSecond.innerHTML = selectInnerHTML;
}

function closeLayer()               //这个层的关闭
{
//##如果不显示日历，则关闭这个层的时候也把值传出去
  if ( outObject != null && odatelayer.trSelectYM.style.display == "none" )
  {
	  if (outObject)
	  {
		if ( hiddenTimeVal == null || hiddenTimeVal == "" )
			hiddenTimeVal = "00:00:00";

	    	outObject.value=hiddenTimeVal;
	  }
	  else
	    alert("您所要输出的控件对象并不存在！");
  }

  document.all.meizzDateLayer.style.display="none";
}

function IsPinYear(year)            //判断是否闰平年
{
  if (0==year%4&&((year%100!=0)||(year%400==0))) return true;else return false;
}

function GetMonthCount(year,month)  //闰年二月为29天
{
  var c=MonHead[month-1];if((month==2)&&IsPinYear(year)) c++;return c;
}
function GetDOW(day,month,year)     //求某天的星期几
{
  var dt=new Date(year,month-1,day).getDay()/7; return dt;
}

function meizzPrevY()  //往前翻 Year
{
  if(meizzTheYear > 999 && meizzTheYear <10000){meizzTheYear--;}
  else{alert("年份超出范围（1000-9999）！");}
  meizzSetDay(meizzTheYear,meizzTheMonth);
}
function meizzNextY()  //往后翻 Year
{
  if(meizzTheYear > 999 && meizzTheYear <10000){meizzTheYear++;}
  else{alert("年份超出范围（1000-9999）！");}
  meizzSetDay(meizzTheYear,meizzTheMonth);
}
function meizzToday()  //Today Button
{
  var today;
  meizzTheYear = new Date().getFullYear();
  meizzTheMonth = new Date().getMonth()+1;
  today=new Date().getDate();
  if(outObject){
    outObject.value=meizzTheYear + "-" + meizzTheMonth + "-" + today;
  }
  closeLayer();
}
function meizzPrevM()  //往前翻月份
{
  if(meizzTheMonth>1){meizzTheMonth--}else{meizzTheYear--;meizzTheMonth=12;}
  meizzSetDay(meizzTheYear,meizzTheMonth);
}
function meizzNextM()  //往后翻月份
{
  if(meizzTheMonth==12){meizzTheYear++;meizzTheMonth=1}else{meizzTheMonth++}
  meizzSetDay(meizzTheYear,meizzTheMonth);
}

function meizzSetDay(yy,mm)   //主要的写程序**********
{
  var curDate="";
  if(hiddenTimeVal!=""){
    var r=hiddenTimeVal.split(" ");
    if(r.length==1)
      curDate=hiddenTimeVal;
    if(r.length==2)
      curDate=r[0];
    var rr=curDate.split("-");
    curDate=new Date(rr[0],rr[1]-1,rr[2]);
  }

  onFlag="";
  meizzWriteHead(yy,mm);     //写日历中的年、月
  //设置当前年月的公共变量为传入值
  meizzTheYear=yy;
  meizzTheMonth=mm;

  for (var i = 0; i < 37; i++){meizzWDay[i]=""};  //将显示框的内容全部清空
  var day1 = 1,day2=1,firstday = new Date(yy,mm-1,1).getDay();  //某月第一天的星期几
  for (i=0;i<firstday;i++)meizzWDay[i]=GetMonthCount(mm==1?yy-1:yy,mm==1?12:mm-1)-firstday+i+1	//上个月的最后几天
  for (i = firstday; day1 < GetMonthCount(yy,mm)+1; i++){meizzWDay[i]=day1;day1++;}
  for (i=firstday+GetMonthCount(yy,mm);i<37;i++){meizzWDay[i]=day2;day2++}
  for (i = 0; i < 37; i++)
  { var da = eval("odatelayer.meizzDay"+i)     //书写新的一个月的日期星期排列
    if (meizzWDay[i]!="")
      {
                //初始化边框
                da.borderColorLight="#80A6F6";
                da.borderColorDark="#FFFFFF";
                if(i<firstday)		//上个月的部分
                {
                        da.innerHTML="<b><font color=gray>" + meizzWDay[i] + "</font></b>";
                        da.title=(mm==1?12:mm-1) +"月" + meizzWDay[i] + "日";
                        da.onclick=Function("meizzDayClick(this.innerText,-1)");
                        if(!curDate)
                                da.style.backgroundColor = ((mm==1?yy-1:yy) == new Date().getFullYear() &&
                                        (mm==1?12:mm-1) == new Date().getMonth()+1 && meizzWDay[i] == new Date().getDate()) ?
                                         "#FCFC73":"#F1ECEC";
                        else
                        {
                                da.style.backgroundColor =((mm==1?yy-1:yy)==curDate.getFullYear() && (mm==1?12:mm-1)== curDate.getMonth() + 1 &&
                                meizzWDay[i]==curDate.getDate())? "#7AFDFD" :
                                (((mm==1?yy-1:yy) == new Date().getFullYear() && (mm==1?12:mm-1) == new Date().getMonth()+1 &&
                                meizzWDay[i] == new Date().getDate()) ? "#FCFC73":"#F1ECEC");
                                //将选中的日期显示为凹下去
                                if((mm==1?yy-1:yy)==curDate.getFullYear() && (mm==1?12:mm-1)== curDate.getMonth() + 1 &&
                                meizzWDay[i]==curDate.getDate())
                                {
                                        da.borderColorLight="#FFFFFF";
                                        da.borderColorDark="#80A6F6";
                                        onFlag=da.id;
                                }
                        }
                }
                else if (i>=firstday+GetMonthCount(yy,mm))		//下个月的部分
                {
                        da.innerHTML="<b><font color=gray>" + meizzWDay[i] + "</font></b>";
                        da.title=(mm==12?1:mm+1) +"月" + meizzWDay[i] + "日";
                        da.onclick=Function("meizzDayClick(this.innerText,1)");
                        if(!curDate)
                                da.style.backgroundColor = ((mm==12?yy+1:yy) == new Date().getFullYear() &&
                                        (mm==12?1:mm+1) == new Date().getMonth()+1 && meizzWDay[i] == new Date().getDate()) ?
                                         "#FCFC73":"#F1ECEC";
                        else
                        {
                                da.style.backgroundColor =((mm==12?yy+1:yy)==curDate.getFullYear() && (mm==12?1:mm+1)== curDate.getMonth() + 1 &&
                                meizzWDay[i]==curDate.getDate())? "#7AFDFD" :
                                (((mm==12?yy+1:yy) == new Date().getFullYear() && (mm==12?1:mm+1) == new Date().getMonth()+1 &&
                                meizzWDay[i] == new Date().getDate()) ? "#FCFC73":"#F1ECEC");
                                //将选中的日期显示为凹下去
                                if((mm==12?yy+1:yy)==curDate.getFullYear() && (mm==12?1:mm+1)== curDate.getMonth() + 1 &&
                                meizzWDay[i]==curDate.getDate())
                                {
                                        da.borderColorLight="#FFFFFF";
                                        da.borderColorDark="#80A6F6";
                                        onFlag=da.id;
                                }
                        }
                }
                else		//本月的部分
                {
                        da.innerHTML="<b>" + meizzWDay[i] + "</b>";
                        da.title=mm +"月" + meizzWDay[i] + "日";
                        da.onclick=Function("meizzDayClick(this.innerText,0)");		//给td赋予onclick事件的处理
                        //如果是当前选择的日期，则显示亮蓝色的背景；如果是当前日期，则显示暗黄色背景
                        if(!curDate)
                                da.style.backgroundColor = (yy == new Date().getFullYear() && mm == new Date().getMonth()+1 && meizzWDay[i] == new Date().getDate())?
                                        "#FCFC73":"#F1ECEC";
                        else
                        {
                                da.style.backgroundColor =(yy==curDate.getFullYear() && mm== curDate.getMonth() + 1 && meizzWDay[i]==curDate.getDate())?
                                        "#7AFDFD":((yy == new Date().getFullYear() && mm == new Date().getMonth()+1 && meizzWDay[i] == new Date().getDate())?
                                        "#FCFC73":"#F1ECEC");
                                //将选中的日期显示为凹下去
                                if(yy==curDate.getFullYear() && mm== curDate.getMonth() + 1 && meizzWDay[i]==curDate.getDate())
                                {
                                        da.borderColorLight="#FFFFFF";
                                        da.borderColorDark="#80A6F6";
                                        onFlag=da.id;
                                }
                        }
                }
        da.style.cursor="hand"
      }
    else{da.innerHTML="";da.style.backgroundColor="";da.style.cursor="default"}
  }
}

function meizzDayClick(n,ex)  //点击显示框选取日期，主输入函数*************
{
  if(onFlag!=""){
    var oldObj=meizzDateLayer.document.all.item(onFlag);
    oldObj.borderColorLight="#80A6F6";
    oldObj.borderColorDark="#FFFFFF";
    oldObj.style.backgroundColor="#F1ECEC";
    onFlag="";
  }
  var newObj=meizzDateLayer.event.srcElement;
  if(newObj.tagName.toLowerCase()=="font")
    newObj=newObj.parentElement;
  if(newObj.tagName.toLowerCase()=="b")
    newObj=newObj.parentElement;
  newObj.borderColorLight="#FFFFFF";
  newObj.borderColorDark="#80A6F6";
  newObj.style.backgroundColor="#7AFDFD";
  onFlag=newObj.id;


  var hour,min,sec;
  var yy=meizzTheYear;
  var mm = parseInt(meizzTheMonth)+ex;	//ex表示偏移量，用于选择上个月份和下个月份的日期
  //判断月份，并进行对应的处理
  if(mm<1){
          yy--;
          mm=12+mm;
  }
  else if(mm>12){
          yy++;
          mm=mm-12;
  }
  if (mm < 10){mm = "0" + mm;}
  if (!n) {//outObject.value="";
    return;}
  if ( n < 10){n = "0" + n;}
  hiddenTimeVal= yy + "-" + mm ; //注：在这里你可以输出改成你想要的格式
  //hiddenTimeVal= yy + mm  + n ; //注：输出格式为:yyyymmdd                                                                                    

  if ( odatelayer.trTime.style.display == "" ) //如果有时间选择框的，输出也加上时分秒
  {
    hour = odatelayer.selHour.value;
    if ( hour < 10 ) hour = "0" + hour;
    min = odatelayer.selMinute.value;
    if ( min < 10 ) min = "0" + min;
    sec = odatelayer.selSecond.value;
    if ( sec < 10 ) sec = "0" + sec;
    hiddenTimeVal += " " + hour + ":" + min + ":" + sec;
  }
  sureLayer();
}

function changeHMS(){
  var r=hiddenTimeVal.split(" ");

//##如果不是不显示日历的，则要求先选择日期
  if ( odatelayer.trSelectYM.style.display == "" )
  {
	  if(r[0]==""){
	    alert("请先选择日期！");
	    return;
	  }  
  }

  hour = odatelayer.selHour.value;
  if ( hour < 10 ) hour = "0" + hour;
  min = odatelayer.selMinute.value;
  if ( min < 10 ) min = "0" + min;
  sec = odatelayer.selSecond.value;
  if ( sec < 10 ) sec = "0" + sec;
//##如果不是不显示日历的，则输出值加上日期部分
  if ( odatelayer.trSelectYM.style.display == "" )
  {
    hiddenTimeVal = r[0]+" "+hour+":"+min+":"+sec;
  }else{
    hiddenTimeVal = hour+":"+min+":"+sec;
  }

}

function sureLayer(){
  if (outObject)
    outObject.value=hiddenTimeVal;
  else
    alert("您所要输出的控件对象并不存在！");
  closeLayer();
}

function resetLayer(){
  if (outObject)
    outObject.value="";
  else
    alert("您所要输出的控件对象并不存在！");
  closeLayer();
}

function add_zero(temp)
{
  if(temp<10) return "0"+temp;
  else return temp;
}
