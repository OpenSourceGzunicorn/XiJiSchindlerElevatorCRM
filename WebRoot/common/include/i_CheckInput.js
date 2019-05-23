// ===============================================================================
//      getStrAllocateLen - get string's length allocated in database
// ===============================================================================
function getStrAllocateLen(str){
	var iLen=0;
	var iUnicode;

	for(i=0;i<str.length;i++){
		iUnicode=str.charCodeAt(i);
		if (iUnicode<=128){				//英文字符
			iLen+=1;
		}else{
			if (iUnicode>128 && iUnicode<=255){
				iLen+=2;
			}else{
				if (iUnicode>=4112){	//中文字符
					iLen+=2;
				}else{
					iLen+=1;			//其他字符
				}
			}
		}
	}
	return(iLen);
}

// To clear starting & ending space in a string object
function Trim(a_strVal)
{
	return(a_strVal.replace(/^\s*|\s*$/g,""));
}

function checkint(data)
{
	if (data == "") return true;
	var re = /^[\-\+]?([1-9]\d*|0|[1-9]\d{0,2}(,\d{3})*)$/;
	if (re.test(data))
		return true;
	return false;
}

function checkday(year,month,day)
{

	if (year != Trim(year) || isNaN(year) || (year < 1900)) {
		return "年不合法";
	}
	if (month != Trim(month) || isNaN(month) || (month < 1) || (month > 12)){
		return "月不合法";
	}
	if (day != Trim(day) || isNaN(day) || (day < 1) || (day > 31)){
		return "日不合法";
	}
	switch (parseInt(month)){
		case 2:
			high =28;
			if ((year % 4 == 0) && (year % 100 != 0))
				{high =29;}
			else if (year % 400 == 0) {high=29;}
			break;
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			high =31;
			break;
		default:
			high =30;
	}
	if ((day < 1) || (day > high)){
		return "日期不合法";
	}
	return "";
}

function CheckDate(a_date)  // 判断输入的日期是否有效
{
	a_date = a_date.replace(/\-/g,"/");
	var ayDate = a_date.split("/");
	if (ayDate.length != 3)
		return false;
	var sY_M_D = checkday(ayDate[0],ayDate[1],ayDate[2]);
	if ( sY_M_D == "" )
		return true;
	alert(sY_M_D);
	var sY_D_M = checkday(ayDate[0],ayDate[2],ayDate[1]);
	if ( sY_D_M == "")
		return true;
	return false;
}

function IsDate(a_Year,a_Month,a_Day)
{
  if (isNaN(a_Year) || isNaN(a_Month) || isNaN(a_Day))
	return false;
  return true;
}

function Is_Birthday(a_Year,a_Month,a_Day)  // 判断输入的日期是否有效的生日
{ var curDate = new Date();
  var curYear=curDate.getYear();
  var curMonth=curDate.getMonth();
  var curDay=curDate.getDate();
  if (IsDate(a_Year,a_Month,a_Day))
  {
    if (!(a_Year<1900 || a_Month<1 || a_Month>12 || a_Day<1 || a_Day>31))
    {
	   return true;
    }
  }
  return false;
}

function Validate_Input()
{
	if (document.all == null){
		return "";
	}
	var l_objAll = document.all;
	var l_intLen = l_objAll.length;
	var i,l_obj;
	var l_strValue,l_strDesc,l_strDatatype;
	var l_id,l_temp,l_intMaxLen;
	for (i=0;i<l_intLen;i++)
	{
		l_obj = l_objAll.item(i);
		if (l_obj.autocheck == null || l_obj.autocheck != "1") continue;
		l_strDesc = "该项目";
		l_strValue = l_obj.value;

		if (l_obj.propname != null) l_strDesc = l_obj.propname;
		if (l_obj.cannull !=null && l_obj.cannull == "0" && l_strValue.replace(" ","") == "")
		{
			l_obj.focus();
			return(l_strDesc + "不能为空！");
		}

		l_intMaxLen = l_obj.maxlen;
		if (l_intMaxLen != null && !(isNaN(l_intMaxLen)))
		{
			if (getStrAllocateLen(l_strValue) > l_intMaxLen)
			{
				l_obj.focus();
				return (l_strDesc + "超出最大长度" + l_intMaxLen + "！");
			}
		}

		if (l_obj.cannull != "0" && l_strValue == "") continue;
		if (l_obj.illegal != null)
		{
			var ayIllegal = l_obj.illegal.split(",");
			for (iTmp=0; iTmp<ayIllegal.length; iTmp++)
			{
				if ( l_strValue.indexOf(ayIllegal[iTmp]) > -1 )
				{
					l_obj.focus();
					return (l_strDesc + "不能够包含字符 " +  ayIllegal[iTmp] + " ！");
				}
			}
		}

		l_strDatatype = l_obj.datatype;
		if (l_strDatatype == null) continue;
		switch(l_strDatatype)
		{
		case "1":
		case "number":
			if (l_strValue != Trim(l_strValue) || isNaN(l_strValue))
			{
				l_obj.focus();
				return (l_strDesc + "不是合法数字！");
			}
			if (l_obj.min_value != null && parseFloat(l_obj.min_value) > l_strValue)
			{
				l_obj.focus();
				return (l_strDesc + "不能小于" + l_obj.min_value + "！");
			}
			if (l_obj.max_value != null && parseFloat(l_obj.max_value) < l_strValue)
			{
				l_obj.focus();
				return (l_strDesc + "不能大于" + l_obj.max_value + "！");
			}
			break;
		case "3":
		case "email":
			if (l_strValue.indexOf("@") == -1)
			{
				l_obj.focus();
				return (l_strDesc + "不是合法的E-Mail地址！");
			}
			var re=/^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$/;
			if (!re.test(l_strValue)){
				l_obj.focus();
				return (l_strDesc + "不是合法的E-Mail地址！");
			}
			break;
		case "birthday":
			var l_Year ="",l_Month="",l_Day="";
			l_id = l_obj.year_id;
			if (l_id != null) l_temp = l_objAll(l_id);
			if (l_temp != null) l_Year = l_temp.value;
			l_id = l_obj.month_id;
			if (l_id != null) l_temp = l_objAll(l_id);
			if (l_temp != null) l_Month = l_temp.value;
			l_id = l_obj.day_id;
			if (l_id != null) l_temp = l_objAll(l_id);
			if (l_temp != null) l_Day = l_temp.value;
 			if (!Is_Birthday(l_Year,l_Month,l_Day))
			{
				l_obj.focus();
				return (l_strDesc + "不是合法的生日日期！");
			}
			break;
		case "2":
		case "date":
			if(!CheckDate(l_strValue)){
				l_obj.focus();
				return (l_strDesc + "不是合法的日期！");
			}
			break;
		default:
			break;
		}
	}
	return "";
}

function CheckAction()
{
	var l_return;
	l_return = Validate_Input();
	if (l_return != ""){
		alert(l_return);
		return false;
	}
	EncodeInput();
	return true;
}
function chkInput()
{
  CheckAction();
}
function EncodeInput(){
	var l_objAll;
	var l_length;
	var i;
	var l_obj;

	l_objAll = document.all;

	l_length = l_objAll.length;

	for(i=0;i<l_length;i++){
		l_obj = l_objAll.item(i);
		if(l_obj.encode != "1") continue;
		l_obj.value = escape(l_obj.value);
	}
}
function textarea_onkeypress(a_sID)
{
	return;
}
/*function doDelete(a_sURL)
{
if(confirm('真的要删除吗？')
{
	window.location = a_sURL;
	return true;
}
return false;
}*/
