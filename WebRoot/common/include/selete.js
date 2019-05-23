selectObjs = false;
curSelectObj = null;
g_sDOWN_ARROW = '/images/down_arrow.gif';

//window.onresize=TryAdjustSelect;
//setTimeout("SetOnFocusToHideSelect();",500);

function BuildSelect(fld_name,ayValue,ayDesc,a_sLen,a_Value,a_Desc,a_sIMG)
{
	var sSelect, i, iSelected, sSelected, sIMG;
	sSelected = (a_Desc == null) ? '' : a_Desc;
	sIMG = (a_sIMG == null) ? g_sDOWN_ARROW : a_sIMG;
	sIMG = "<IMG SRC=\"" + sIMG + "\" STYLE=\"cusor:hand;\" BORDER=0>";
	if(a_sLen == null) 
		a_sLen = 100;
		
	sSelect = "<DIV ID=\"selon_" + fld_name + "\"  CLASS=\"select_selectOn\" ONBLUR=\"DelayHideSelect('selobj_" + fld_name + "');\">"
	sSelect += "<SCRIPT LANGUAGE=\"javascript\">"
	sSelect += "tmp_select_vname='" + fld_name + "';"

	iSelected = -1;
	for (i=0;i<ayValue.length;i++)
	{
		if (a_Value != null)
		{
			if (a_Value == ayValue[i])
			{
				iSelected = i;
				if (a_Desc == null)
					sSelected = ayDesc[i];
			}
		}
		sSelect += "GenSelItem(" + i + ",'" + ayValue[i] + "','" + ayDesc[i] + "');"
	}

	sSelect = "<DIV ID=\"seloff_" + fld_name + "\" CLASS=\"select_selectOff\" ONCLICK=\"ToggleSelect(selobj_" + fld_name + ");\">"
				+ "<INPUT CLASS=\"ednonedisable\" READONLY STYLE=\"width:" + a_sLen + ";cursor:default;BORDER-BOTTOM: #aaaaaa 1px solid;\" NAME=\"selvarV_" + fld_name + "\" VALUE=\"" + sSelected + "\" SIZE=\"20\">"
				+ sIMG
				+ "</DIV>"
				+ sSelect;

	sSelect += "</SCRIPT>"
	sSelect += "</DIV>"

	sSelect += "<INPUT TYPE=\"HIDDEN\" NAME=\"" + fld_name + "\" VALUE=\"" + a_Value + "\">"
	sSelect += "<SCRIPT LANGUAGE=\"javascript\">"
	sSelect += "selobj_" + fld_name + "=new TSelectObject('" + fld_name + "','selvarV_" + fld_name + "','selon_" + fld_name + "','seloff_" + fld_name + "'," + iSelected + "," + ayValue.length + ");"
	sSelect += "RegisterSelect(selobj_" + fld_name + ");"
	sSelect += "</SCRIPT>"

//	alert(sSelect);
	document.write(sSelect);
}

function TryHideSelect()
{
	if (curSelectObj!=null)
		HideSelect(curSelectObj);
}
function SetOnFocusToHideSelect()
{
	
	var obj;
	var i;
	if (document.all){
		document.body.onfocus = TryHideSelect;
		obj = document.all.tags("TABLE");
		for (i=0;i<obj.length;i++){
			obj.item(i).onfocus = TryHideSelect;
		}
		obj = document.all.tags("TD");
		for (i=0;i<obj.length;i++){
			obj.item(i).onfocus = TryHideSelect;
		}
		obj = document.all.tags("A");
		for (i=0;i<obj.length;i++){
			obj.item(i).onfocus = TryHideSelect;
		}
	}
}
/*function TryAdjustSelect()
{
	if (selectObjs){
		for (i=0;i<selectObjs.length;i++)
			if (selectObjs[i].visible){
				CalcSelectPosition(selectObjs[i]);
			}
	}
}*/
function SetFormChanged(formObj)
{
	if (formObj==null)
		return;
	if (typeof(formObj.tuiFormChanged)=="undefined")
		return;
	formObj.tuiFormChanged.value = 1;
}

function FindFormByVarName(vname)
{
	for (i=0;i<document.forms.length;i++){
		if (document.forms[i].all[vname]!=null)
			return document.forms[i];
	}
	return null;
}

function NormalOption(selobj,i)
{
	if (i < 0)
		return;
	var obj = document.all[selobj.idOn+"_"+i];
	obj.style.backgroundColor = "#f0f0f0";
	obj.style.color = "#000";
}


function OptionClicked(selobj,idx,val)
{
	var obj = document.all[selobj.idOn+"_"+idx];
	var vstr = obj.innerText;
	vstr = vstr.replace(/^( )+/,"");
	var formObj=null;

	formObj = FindFormByVarName(selobj.formVarH);
	SetFormChanged(formObj);
	if (formObj!=null){
		eval("formObj."+selobj.formVarV+".value = vstr;");
		eval("formObj."+selobj.formVarH+".value = '"+val+"';");
	}
	selobj.selectedIndex = idx;
	HideSelect(selobj);
}

function ShowSelect(selobj)
{
	if (document.all){
		if (selobj == null)
			return;
		if (curSelectObj!=null)
			HideSelect(curSelectObj);
		selobj.visible = true;
		document.all[selobj.idOn].style.visibility="visible";
		CalcSelectPosition(selobj);
//		document.all[selobj.idOn].focus();
//		document.all[selobj.idOn+"_"+selobj.selectedIndex].scrollIntoView();
		HighlightOption(selobj,selobj.selectedIndex);
		curSelectObj = selobj;
	}
}

function HideSelect(selobj)
{
	if (document.all){
		obj = document.all[selobj.idOn].style;
		obj.visibility="hidden";
		selobj.visible = false;
		curSelectObj = null;
	}
}


function CalcSelectPosition(selobj)
{
	if (document.all){
		var obj = document.all[selobj.idOff];
		var left = 0;
		var top = 0;
		for (; obj!=document.body; obj=obj.offsetParent){
			left += obj.offsetLeft;
			top  += obj.offsetTop;
		}
		obj = document.all[selobj.idOn].style;
		obj.pixelLeft = left;
		obj.pixelTop = top+document.all[selobj.idOff].offsetHeight+1;
		obj.pixelWidth = document.all[selobj.idOff].offsetWidth;
		if (selobj.nItems<=10){
			obj.pixelHeight = selobj.nItems*16+2;
			obj.overflow = "visible";
		}
		else{
			obj.pixelHeight = 10*16+2;
			obj.overflow = "auto";
		}
	}
}

function HighlightOption(selobj,i)
{
	if (i < 0)
		return;
	NormalOption(selobj,selobj.selectedIndex);
	var obj = document.all[selobj.idOn+"_"+i];
	obj.style.backgroundColor = "#113399";
	obj.style.color = "#ffffff";
}


function RegisterSelect(selobj)
{
	if (!selectObjs)
		selectObjs = new Array();
	selectObjs[selectObjs.length] = selobj;
}


function DelayHideSelect(objname)
{

	setTimeout("HideSelect("+objname+");",200);
}

function ToggleSelect(selobj)
{
	if (selobj.visible)
		HideSelect(selobj);
	else
		ShowSelect(selobj);
	return;
}


function TSelectObject(formVarH,formVarV,idOn,idOff,selectedIndex,nItems)
{
	this.formVarH = formVarH;
	this.formVarV = formVarV;
	this.idOn = idOn;
	this.idOff = idOff;
	this.selectedIndex = selectedIndex;
	this.visible = false;
	this.nItems = nItems;
	SetOnFocusToHideSelect();
	return this;
}

function GenSelItem(num,k,v)
{
	var sobj = "selobj_"+tmp_select_vname;
	var id = "selon_"+tmp_select_vname;
 	document.write("<DIV CLASS=\"selectItem\" ID=\""+id+"_"+num+"\" ONCLICK=\"OptionClicked("+sobj+","+num+",'"+k+"');\" ONMOUSEOVER=\"HighlightOption("+sobj+","+num+");\" ONMOUSEOUT=\"NormalOption("+sobj+","+num+");\">"+v+"</DIV>");
}
