
////////////////////////////////////////////////////////////////////////////////////
////本程序生成windows风格工具条
////This Program generate windows style toolbar
////作者:黄子雄
////Author:Dickhome
////Modify:2001-11-11
////使用用方法:1.在调用页面所在目录建立两个子目录CSS和WebToolBar
////		   2.CSS中放入toolbar.css文件，WebToolBar放入webtoolbar.js(本文件)
////           3.在调用页面用AddToolBarItem加入按扭。用GenToolBarV(图片文本上下结构)
////			 和GenToolBar(图片文本左右结构)	生成所需工具条代码
////		   4.可以通过标示为public的函数设置工具条属性。
////modify 2002-5-9
////////////////////////////////////////////////////////////////////////////////////

var toolbarImgNormal  = new Array()//保存正常状态的图片数组
var toolbarImgOver	  = new Array()//保存鼠标移动状态的图片数组
var toolbarImgDown    = new Array()//保存鼠标按下状态的图片数组

var toolbarText = new Array()//保存按扭标题数组
var toolbarWidth  = new Array()//保存按扭宽度数组
var toolbarSplit  = new Array()//保存按扭前面是否需分隔条数组

var toolbarAction  = new Array()//保存按扭所触发时间名称数组
var toolbarName	   = new Array()//保存按扭的名称

var toolbarCount=0      //按扭数量

var toolbarHeight =22   //高度----外部程序可设置
var toolbarHandle = true//工具条前面是否需要把柄-------外部程序可设置

//设置工具条高度
//lHeight:高度
//public
function SetToolBarHeight(lHeight)
{
	toolbarHeight=lHeight
}

//设置是否有把柄
//lFlag=true(有),false(没有)
//public
function SetToolBarHandle(lFlag)
{
	toolbarHandle = lFlag
}
//增加一个按扭
//sImgxxxx,图片的路径
//plblic
function AddToolBarItem(sImgNormal,sImgOver,sImgDown,sText,sWidth,sSplit,sAction)
{
	toolbarImgNormal[toolbarCount]=sImgNormal
	toolbarImgOver[toolbarCount]=sImgOver
	toolbarImgDown[toolbarCount]=sImgDown
	toolbarText[toolbarCount] = sText
	toolbarWidth[toolbarCount] = sWidth
	toolbarSplit[toolbarCount] = sSplit

	toolbarAction[toolbarCount] = sAction

	toolbarName[toolbarCount] = "Tool"+toolbarCount //此方法没用指定名称参数,所以生成一个名称

	toolbarCount=toolbarCount+1

}

//增加一个按扭,此方法与AddToolBarItem类似，增加一个参数,指定该按扭的名称
//sImgxxxx,图片的路径
//plblic
function AddToolBarItemEx(sName,sImgNormal,sImgOver,sImgDown,sText,sWidth,sSplit,sAction)
{
	toolbarImgNormal[toolbarCount]=sImgNormal
	toolbarImgOver[toolbarCount]=sImgOver
	toolbarImgDown[toolbarCount]=sImgDown



	toolbarText[toolbarCount] = sText
	toolbarWidth[toolbarCount] = sWidth
	toolbarSplit[toolbarCount] = sSplit

	toolbarAction[toolbarCount] = sAction

	toolbarName[toolbarCount] = sName


	toolbarCount=toolbarCount+1

}

function RemoveAllItem()
{

	toolbarCount = 0

}


//取得工具条的Html代码,图片文字垂直方式
//public
function GenToolBarV(asTabClassName,asTextClassName,asOverClassName,asOutClassName,asDownClassName,asCheckClassName)
{
	sBarPad = "<TABLE border=0 cellPadding=0 cellSpacing=0 class=\"" + asTabClassName + "\"  width=\"100%\"><TBODY><TR>"
	if (toolbarHandle)
	{
		sBarPad =sBarPad + GenHandle()
	}
	sBarPad += "<TD><TABLE border=0 cellPadding=0 cellSpacing=0><TR><TD noWrap width=4></TD>"

	for (i=0;i<toolbarCount;i++)
	{
		sBarPad += GenTool(i,true,asTextClassName,asOverClassName,asOutClassName,asDownClassName,asCheckClassName)
	}

	sBarPad =sBarPad + "</TR></TBODY></TABLE>"
	return sBarPad
}
//取得工具条的Html代码,图片文字水平方式
//public
function GenToolBar()
{
	sBarPad = "<TABLE border=0 cellPadding=0 cellSpacing=0 class=\"" + asTabClassName + "\"  width=\"100%\"><TBODY><TR>"
	if (toolbarHandle)
	{
		sBarPad =sBarPad + GenHandle()
	}
	sBarPad += "<TD><TABLE border=0 cellPadding=0 cellSpacing=0><TR><TD noWrap width=4></TD>"

	for (i=0;i<toolbarCount;i++)
	{
		sBarPad +=  GenTool(i,false,asTextClassName,asOverClassName,asOutClassName,asDownClassName,asCheckClassName)
	}

	sBarPad =sBarPad + "</TR></TBODY></TABLE>"
	return sBarPad
}
//取得工具条的Html代码,图片文字水平方式
//public
function GenToolBar(asTabClassName,asTextClassName,asOverClassName,asOutClassName,asDownClassName,asCheckClassName)
{
	sBarPad = "<TABLE border=0 cellPadding=0 cellSpacing=0 class=\"" + asTabClassName + "\"  width=\"100%\"><TBODY><TR>"
	if (toolbarHandle)
	{
		sBarPad =sBarPad + GenHandle()
	}
	sBarPad += "<TD><TABLE border=0 cellPadding=0 cellSpacing=0><TR><TD noWrap width=4></TD>"

	for (i=0;i<toolbarCount;i++)
	{
		sBarPad +=  GenTool(i,false,asTextClassName,asOverClassName,asOutClassName,asDownClassName,asCheckClassName)
	}

	sBarPad =sBarPad + "</TR></TBODY></TABLE>"
	return sBarPad
}

//设置指定索引的按扭是否被check
//index:0开始
//flag:true:check
//pulbic
function SetCheck(sName,flag,asCheckClassName,asOutClassName)
{
        if(document.all.item(sName)!=null)
	{
		if (flag)
                        document.all.item(sName).className=asCheckClassName;
		else
			document.all.item(sName).className=asOutClassName;
	}
	else
	{
		alert("指定的按扭名称不存在。");
	}
}



//生成把柄
//private
function GenHandle()
{
	var sHandle=""
	if (toolbarHandle)
	{
		sHandle ="<TD noWrap width=10>"
        sHandle +="<TABLE border=0 cellPadding=0 cellSpacing=0 height=" + toolbarHeight
        sHandle +=  " style=\"BORDER-BOTTOM: #808080 1px solid; MARGIN-BOTTOM: 2px; MARGIN-LEFT: 4px; MARGIN-TOP: 2px\" width=3>"
        sHandle +=  "<TBODY>"
		sHandle +=	  "<TR>"
        sHandle +=      "<TD bgColor=#ffffff width=1>　</TD>"
        sHandle +=      "<TD bgColor=#d4d0c8 width=1>　</TD>"
        sHandle +=		"<TD bgColor=#808080 width=1>　</TD>"
        sHandle +=	   "</TR>"
        sHandle +=  "</TBODY>"
        sHandle +="</TABLE>"
		sHandle +="</TD>"
	}
	return sHandle
}

//生成分隔条
//private
function GenSplit()
{
		var sSplit
		sSplit = "<TD noWrap width=10>"
        sSplit += "<DIV align=center>"
        sSplit += "<TABLE border=0 cellPadding=0 cellSpacing=0 height=" + toolbarHeight
        sSplit += " style=\"MARGIN-BOTTOM: 2px; MARGIN-TOP: 2px\">"
        sSplit += " <TBODY>"
        sSplit += " <TR>"
        sSplit += "<TD bgColor=#808080 width=1>　</TD>"
        sSplit += "<TD bgColor=#ffffff width=1>　</TD>"
        sSplit += "</TR>"
        sSplit += "</TBODY>"
        sSplit += "</TABLE>"
        sSplit += "</DIV>"
        sSplit += "</TD>"
        return sSplit
}

//生成一个按扭
//index,按扭索引
//vertical,图片文字是否垂直
//private
function GenTool(index,vertical,asTextClassName,asOverClassName,asOutClassName,asDownClassName,asCheckClassName)
{
        var sTool
	var sOnClick
	var sOnMouseDown
	var sOnMouseOut
	var sOnMouseOver
	var sOnMouseUp
	var sWidth
	var sTmp
	var sImg="Img"+index
//	var sTool="Tool"+index
	sOnClick = " onclick=\"javascript:" + toolbarAction[index] + ";\" "
	//sOnClick = " onclick=\"javascript:funHello();\" "
	//sOnClick = "onclick=\"addPad('default')\" "
	sOnMouseDown = " onmousedown=\"OnDown(this,'" + asDownClassName + "','" + asCheckClassName + "')\" "
	if (toolbarImgOver[index]!='')
	{
                sOnMouseOut = " onmouseout=\"OnOut(this,'" + asOutClassName + "','" + asCheckClassName + "');document.all."+sImg+".src='"+ toolbarImgNormal[index]+"'\" "
                sOnMouseOver=  " onmouseover=\"OnOver(this,'" + asOverClassName + "','" + asCheckClassName + "'),document.all."+sImg+".src='"+ toolbarImgOver[index] +"'\" "
                sOnMouseUp=  " onmouseup=\"this.className='" + asOverClassName + "',document.all."+sImg+".src='"+ toolbarImgNormal[index]+"'\" "
	}else
	{
                sOnMouseOut = " onmouseout=\"OnOut(this,'" + asOutClassName + "','" + asCheckClassName + "')\" "
                sOnMouseOver=  " onmouseover=\"OnOver(this,'" + asOverClassName + "','" + asCheckClassName + "')\" "
                sOnMouseUp=  " onmouseup=\"this.className='" + asOverClassName + "'\" "
//		sTmp= toolbarImgNormal[index]
	}

	sWidth	  =" width='" + toolbarWidth[index] +"'"
	sHeight   =  toolbarHeight
	if (sHeight<10)
	{
		sHeight = 10
	}
	sHeight = " height='" +sHeight  +"' "
	sTool = "<TD><TABLE  border=0 cellPadding=0 cellSpacing=0 id="+toolbarName[index]

	sTool += sOnClick
	sTool += sOnMouseDown
	sTool += sOnMouseOut
	sTool += sOnMouseOver
	sTool += sOnMouseUp
	sTool += sWidth + sHeight
	sTool += "><TBODY><TR><TD><DIV align=center>"
	sTool += "<TABLE border=0 cellPadding=0 cellSpacing=0>"
	if (!vertical)
	{
		sTool += "<TBODY><TR> <TD>"
		sTool += " <P style=\"MARGIN-RIGHT: 2px\"><IMG border=0 "
		sTool += " height=16 id="+sImg+"  src=\""
		sTool +=  toolbarImgNormal[index] + "\" width=16>"
		sTool += "</P></TD>"
		sTool += "<TD><P class=\"" + asTextClassName + "\">"
		sTool += toolbarText[i]
		sTool += "</P></TD></TR></TBODY></TABLE></DIV></TD></TR></TBODY></TABLE></TD>"
    }else
    {
                sTool += "<TBODY><TR> <TD align=center>"
		sTool += " <P style=\"MARGIN-RIGHT: 2px\"><IMG border=0 "
		sTool += " height=16 id="+sImg+"  src=\""
		sTool +=  toolbarImgNormal[index] + "\" width=16>"
		sTool += "</P></TD>"
		sTool += "<TR><TD align=center><P class=\"" + asTextClassName + "\">"
		sTool += toolbarText[i]
		sTool += "</P></TD><TR></TR></TBODY></TABLE></DIV></TD></TR></TBODY></TABLE></TD>"
    }


    if (toolbarSplit[index]=="1")
    {
		sTool = GenSplit() +sTool
    }
    return sTool
}


function OnOver(obj,asOverClassName,asCheckClassName)
{
        if (obj.className!=asCheckClassName)
				obj.className=asOverClassName;
}

function OnOut(obj,asOutClassName,asCheckClassName)
{
        if (obj.className!=asCheckClassName)
				obj.className=asOutClassName;
}

function OnDown(obj,asDownClassName,asCheckClassName)
{
	if (obj.className!=asCheckClassName)
				obj.className=asDownClassName;
}



