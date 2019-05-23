//添加select中的节点
function addDoc(obj1,obj2,obj3,obj4,obj5)
{
//obj1 text
//obj2 select
//obj3 radio
//obj4 hidden
//obj5 text extend use

var txt=trim(obj1.value)
if(txt=="")
{
alert("请输入档号!")
return;
}
if(contains(txt))
{
alert("输入字符串存在空格或|符合，请重新输入")
obj1.value=""
return;
}
var o2 = obj3;
var j=o2.length
var tag="null"
for(i=0;i<j;i++)
if(o2[i].checked==true) tag=o2[i].value
if(tag==2&&obj5!=null&&obj5.name=="page"&&obj5.value=="")
{
var msg="必须输入复制资料页数";
if(trim(document.all.item("copyAppr").value)=="")
msg=msg+"和复制审批人姓名。"
else msg=msg+"。";
alert(msg);
return;
}

if(tag!=2&&obj5!=null&&obj5.name=="page")
{
  if(trim(document.all.item("copyAppr").value)=="")
 { alert("必须输入复制审批人姓名。");
 return;
 }

  }

if(obj5!=null&&obj5.name=="returnDate"&&obj5.value=="")
{
var msg="必须输入归还日期";
alert(msg);
return;
}



if(obj5!=null&&obj5.name=="returnDate"&&obj5.value!="")
{
    if(trim(document.all.item("brrwDate").value)=="")
{var msg="必须输入外借日期。"
alert(msg);
return;
  }
  var late=isLater(trim(document.all.item("brrwDate").value),trim(document.all.item("returnDate").value))

  if(late!=-1)
  {
    alert("归还日期不能早于外借日期。");
    return;
    }
  var msg;
  var msgTag=false;
  if((trim(document.all.item("brrwAdmin").value)=="")&&(trim(document.all.item("brrwUser").value)!=""))
{msg="必须输入借阅管理员姓名。";msgTag=true;}
else if((trim(document.all.item("brrwAdmin").value)!="")&&(trim(document.all.item("brrwUser").value)==""))
{msg="必须输入借阅人姓名。";msgTag=true;}
else if((trim(document.all.item("brrwAdmin").value)=="")&&(trim(document.all.item("brrwUser").value)==""))
{msg="必须输入借阅管理员、借阅人姓名。";msgTag=true;}
if(msgTag)
{
alert(msg);
return;
}
  }
  var newValue;
  var newText;
  var newTag;
   newTag=txt+" "+tag
  if(obj5==null) newValue=txt+" "+tag
  else if(obj5!=null&&obj5.name=="page")
  {
    if(obj5.value=="") obj5.value="0"
 newValue=txt+" "+tag+" "+obj5.value+" "+document.all.item("copyAppr").value

 }
  else
 newValue=txt+" "+tag+" "+obj5.value+" "+document.all.item("brrwAdmin").value+" "+document.all.item("brrwUser").value

    if(tag==0)
 newText=txt+"(档案)";
  else if(tag==1)
 newText=txt+"(案卷)";
  else if(tag==2)
 newText=txt+"(普通资料)";

  if(isChosen(obj2,newTag)&&isChosenText(obj2,newText))
  {
    alert("该记录已经存在，请重新输入！")
    return;
    }



var oNewNode=document.createElement("option");
var o = obj2.appendChild(oNewNode);
o.text= newText
o.value=newValue
if(obj2.id=="moveList"&&tag==0)
document.all.item("move_doc_spn").innerText=parseInt(document.all.item("move_doc_spn").innerText)+1
if(obj2.id=="moveList"&&tag==1)
document.all.item("move_roll_spn").innerText=parseInt(document.all.item("move_roll_spn").innerText)+1
if(obj2.id=="moveList"&&tag==2)
document.all.item("move_res_spn").innerText=parseInt(document.all.item("move_res_spn").innerText)+1

if(obj2.id=="copyList"&&tag==0)
document.all.item("copy_doc_spn").innerText=parseInt(document.all.item("copy_doc_spn").innerText)+1
if(obj2.id=="copyList"&&tag==1)
document.all.item("copy_roll_spn").innerText=parseInt(document.all.item("copy_roll_spn").innerText)+1
if(obj2.id=="copyList"&&tag==2)
document.all.item("copy_res_spn").innerText=parseInt(document.all.item("copy_res_spn").innerText)+1


if(obj2.id=="brrwList"&&tag==0)
document.all.item("brrw_doc_spn").innerText=parseInt(document.all.item("brrw_doc_spn").innerText)+1
if(obj2.id=="brrwList"&&tag==1)
document.all.item("brrw_roll_spn").innerText=parseInt(document.all.item("brrw_roll_spn").innerText)+1
if(obj2.id=="brrwList"&&tag==2)
document.all.item("brrw_res_spn").innerText=parseInt(document.all.item("brrw_res_spn").innerText)+1

//设置隐含域的值
if(obj4.value=="") obj4.value=o.value
else
obj4.value=obj4.value+"|"+o.value

}

//删除select中的节点
function delDoc(obj1,obj2)
{
//obj1 select
//obj2 hidden
var o = obj1
var j = o.options.length;
for (i=0;i<j&&i>=0&&j>0;i++)
{
	if ( o.options[i].selected == true )
	{
    var len=o.options[i].text.length+2

    var tag= o.options[i].value.substring(len,1)

 if(obj1.id=="moveList"&&tag==0)
document.all.item("move_doc_spn").innerText=parseInt(document.all.item("move_doc_spn").innerText)-1
if(obj1.id=="moveList"&&tag==1)
document.all.item("move_roll_spn").innerText=parseInt(document.all.item("move_roll_spn").innerText)-1
if(obj1.id=="moveList"&&tag==2)
document.all.item("move_res_spn").innerText=parseInt(document.all.item("move_res_spn").innerText)-1

if(obj1.id=="copyList"&&tag==0)
document.all.item("copy_doc_spn").innerText=parseInt(document.all.item("copy_doc_spn").innerText)-1
if(obj1.id=="copyList"&&tag==1)
document.all.item("copy_roll_spn").innerText=parseInt(document.all.item("copy_roll_spn").innerText)-1
if(obj1.id=="copyList"&&tag==2)
document.all.item("copy_res_spn").innerText=parseInt(document.all.item("copy_res_spn").innerText)-1


if(obj1.id=="brrwList"&&tag==0)
document.all.item("brrw_doc_spn").innerText=parseInt(document.all.item("brrw_doc_spn").innerText)-1
if(obj1.id=="brrwList"&&tag==1)
document.all.item("brrw_roll_spn").innerText=parseInt(document.all.item("brrw_roll_spn").innerText)-1
if(obj1.id=="brrwList"&&tag==2)
document.all.item("brrw_res_spn").innerText=parseInt(document.all.item("brrw_res_spn").innerText)-1

		o.removeChild(o.options[i]);
					i =i-1;
		j = o.options.length;

	}
}

j = o.options.length;
obj2.value=""
for (i=0;i<j;i++)
{
if(obj2.value=="") obj2.value=o.options[i].value
else
obj2.value=obj2.value+"|"+o.options[i].value
}
}

// 删除前后空格
function trim(a_strVal)
{
	return(a_strVal.replace(/^\s*|\s*$/g,""));
}

function setPage(str)
{
document.all.item("tr_page").style.visibility=str
}

//判断a_oSelect中是否已存在a_sCode
function isChosen(a_oSelect,a_sCode)
{
	var i;
	for(i=0; i<a_oSelect.options.length; i++)
	{
		if(a_oSelect.options(i).value.substring(0,a_sCode.length) == a_sCode)
		{
			return(true);
		}
	}
	return(false);
}

//判断a_oSelect中是否已存在Text a_sText
function isChosenText(a_oSelect,a_sText){
	var i;
	for(i=0; i<a_oSelect.options.length; i++)
	{
		if(a_oSelect.options(i).text == a_sText)
		{
			return(true);
		}
	}
	return(false);
}

function contains(str)
{
j=str.length
for(i=0;i<j;i++)
if(str.charAt(i)==' '||str.charAt(i)=='|') return true

return false
}

function isLater(d1,d2){
  var dd1 = new String(d1);
  var dd2 = new String(d2);
  if (dd2.substr(0,4)<dd1.substr(0,4))
    return 1;
  else
  {
    if (dd2.substr(0,4)>dd1.substr(0,4))
      return -1;
    else
	{
      if (dd2.substr(5,2)<dd1.substr(5,2))
        return 1;
      else
	  {
        if (dd2.substr(5,2)==dd1.substr(5,2)&&dd2.substr(8,2)<dd1.substr(8,2))
          return 1;
        else
          return -1;
      }//end else
    }//end else
  }//end else

}
