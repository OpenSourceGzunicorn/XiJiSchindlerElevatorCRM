
//取数字转成大写中文        !!限两位小数   返回中文大写
function Number_to_Chinese1(Num){
   for(i=Num.length-1;i>=0;i--)
   {
    Num = Num.replace(",","")//替换Num中的“,”
    Num = Num.replace(" ","")//替换Num中的空格
   }
   Num = Num.replace("￥","")//替换掉可能出现的￥字符
   if(isNaN(Num)) { //验证输入的字符是否为数字
    alert("请检查小写金额是否正确");
    return;
   }
   //---字符处理完毕，开始转换，转换采用前后两部分分别转换---//
   var fuhao="";//负数时,大写前面添加符号"-"
   if(Num<0){
   	fuhao="负";
   }
   part = String(Num).split(".");
   newchar = ""; 
   //小数点前进行转化
   for(i=part[0].length-1;i>=0;i--){
   if(part[0].length > 10){ alert("位数过大，无法计算");return "";}//若数量超过拾亿单位，提示
    tmpnewchar = ""
    perchar = part[0].charAt(i);
    //整数数字转为大写中文
    switch(perchar){
    case "0": tmpnewchar="零" + tmpnewchar ;break;
    case "1": tmpnewchar="壹" + tmpnewchar ;break;
    case "2": tmpnewchar="贰" + tmpnewchar ;break;
    case "3": tmpnewchar="叁" + tmpnewchar ;break;
    case "4": tmpnewchar="肆" + tmpnewchar ;break;
    case "5": tmpnewchar="伍" + tmpnewchar ;break;
    case "6": tmpnewchar="陆" + tmpnewchar ;break;
    case "7": tmpnewchar="柒" + tmpnewchar ;break;
    case "8": tmpnewchar="捌" + tmpnewchar ;break;
    case "9": tmpnewchar="玖" + tmpnewchar ;break;
    }
     //判断单位
    switch(part[0].length-i-1){
    case 0: tmpnewchar = tmpnewchar +"元" ;break;
    case 1: if(perchar!=0)tmpnewchar= tmpnewchar +"拾" ;break;
    case 2: if(perchar!=0)tmpnewchar= tmpnewchar +"佰" ;break;
    case 3: if(perchar!=0)tmpnewchar= tmpnewchar +"仟" ;break;
    case 4: tmpnewchar= tmpnewchar +"万" ;break;
    case 5: if(perchar!=0)tmpnewchar= tmpnewchar +"拾" ;break;
    case 6: if(perchar!=0)tmpnewchar= tmpnewchar +"佰" ;break;
    case 7: if(perchar!=0)tmpnewchar= tmpnewchar +"仟" ;break;
    case 8: tmpnewchar= tmpnewchar +"亿" ;break;
    case 9: tmpnewchar= tmpnewchar +"拾" ;break;
    }
    newchar = tmpnewchar + newchar;
   }
   //小数点之后进行转化
   if(Num.indexOf(".")!=-1){
   if(part[1].length > 2) {
    alert("小数点之后只能保留两位,系统将自动截段");
    part[1] = part[1].substr(0,2)
    }
   for(i=0;i<part[1].length;i++){
    tmpnewchar = ""
    perchar = part[1].charAt(i)
     //小数数字转为大写中文
    switch(perchar){
    case "0": tmpnewchar="零" + tmpnewchar ;break;
    case "1": tmpnewchar="壹" + tmpnewchar ;break;
    case "2": tmpnewchar="贰" + tmpnewchar ;break;
    case "3": tmpnewchar="叁" + tmpnewchar ;break;
    case "4": tmpnewchar="肆" + tmpnewchar ;break;
  case "5": tmpnewchar="伍" + tmpnewchar ;break;
    case "6": tmpnewchar="陆" + tmpnewchar ;break;
    case "7": tmpnewchar="柒" + tmpnewchar ;break;
    case "8": tmpnewchar="捌" + tmpnewchar ;break;
    case "9": tmpnewchar="玖" + tmpnewchar ;break;
    }
   //判断单位
    if(i==0)tmpnewchar =tmpnewchar + "角";
    if(i==1)tmpnewchar = tmpnewchar + "分";
    newchar = newchar + tmpnewchar;
   }
   }
   //替换所有无用汉字
   while(newchar.search("零零") != -1)
    newchar = newchar.replace("零零", "零");
   newchar = newchar.replace("零亿", "亿");
   newchar = newchar.replace("亿万", "亿");
   newchar = newchar.replace("零万", "万");
   newchar = newchar.replace("零元", "元");
   newchar = newchar.replace("零角", "");
   newchar = newchar.replace("零分", ""); 


   if (newchar.charAt(newchar.length-1) == "元" || newchar.charAt(newchar.length-1) == "角")
    newchar = fuhao+newchar+"整"
    //document.write(newchar);
    return newchar;
  }
  
//取数字转成大写中文赋值给一个text控件
  function Number_to_Chinese2(num,objName)
{
	document.getElementById(objName).value=Number_to_Chinese1(num);
}
  