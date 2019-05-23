//相同名称的所有输入框求和
function sumValuesByName(name,outputid){  
  var objs = document.getElementsByName(name);
  var sum = 0;
  var value = 0;
  for(var i=0;i<objs.length;i++){
    value = parseFloat(objs[i].value);
    sum =  isNaN(value) ? sum + 0 : accAdd(sum, value);    
  }
  document.getElementById(outputid).value = sum;
}

//2个不同名称的所有输入框求和
function sumValuesByName2(name1,name2,outputid){  
  var objs1 = document.getElementsByName(name1);
  var objs2 = document.getElementsByName(name2);
  var sum1 = 0;
  var value1 = 0;
  var sum2 = 0;
  var value2 = 0;
  for(var i=0;i<objs1.length;i++){
    value1 = parseFloat(objs1[i].value);
    sum1 =  isNaN(value1) ? sum1 + 0 : accAdd(sum1, value1);    
  }
  for(var i=0;i<objs2.length;i++){
	    value2 = parseFloat(objs2[i].value);
	    sum2 =  isNaN(value2) ? sum2 + 0 : accAdd(sum2, value2);    
	  }
  document.getElementById(outputid).value = accAdd(sum1,sum2);
}

/**
 ** 加法函数，用来得到精确的加法结果
 ** 说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
 ** 调用：accAdd(arg1,arg2)
 ** 返回值：arg1加上arg2的精确结果
 **/
function accAdd(arg1, arg2) {
    var r1, r2, m, c;
    try {
        r1 = arg1.toString().split(".")[1].length;
    }
    catch (e) {
        r1 = 0;
    }
    try {
        r2 = arg2.toString().split(".")[1].length;
    }
    catch (e) {
        r2 = 0;
    }
    c = Math.abs(r1 - r2);
    m = Math.pow(10, Math.max(r1, r2));
    if (c > 0) {
        var cm = Math.pow(10, c);
        if (r1 > r2) {
            arg1 = Number(arg1.toString().replace(".", ""));
            arg2 = Number(arg2.toString().replace(".", "")) * cm;
        } else {
            arg1 = Number(arg1.toString().replace(".", "")) * cm;
            arg2 = Number(arg2.toString().replace(".", ""));
        }
    } else {
        arg1 = Number(arg1.toString().replace(".", ""));
        arg2 = Number(arg2.toString().replace(".", ""));
    }
    return (arg1 + arg2) / m;
}

function formatMoney(num,a) {
	var arrStr = (parseFloat(num).toFixed(a)+"").split(".");
	var reg = /([-+]?\d{3})(?=\d)/g;
    return arrStr[0].split("").reverse().join("").replace(reg, "$1,").split("").reverse().join("")+"."+arrStr[1];
}

function setOhterVal(obj){
	var name=obj.name;
	
	var valarr=document.getElementsByName(name);
	var num=0;
	for(var i=0;i<valarr.length;i++){
		if(valarr[i]==obj){
			num=i;
			break;
		}
	}
	if(num==0){
		for(var i=1;i<valarr.length;i++){
			valarr[i].value=obj.value;
		}
	}
}



