//1±ä³ÉÒ»,2±ä¶þ

function ChangeEasy(str){
	var newstr="";
	if(str!=null&&str!=""){
		newstr=str.replace(/1/g,"Ò»");
		newstr=newstr.replace(/2/g,"¶þ");
		newstr=newstr.replace(/3/g,"Èý");
		newstr=newstr.replace(/4/g,"ËÄ");
		newstr=newstr.replace(/5/g,"Îå");
		newstr=newstr.replace(/6/g,"Áù");
		newstr=newstr.replace(/7/g,"Æß");
		newstr=newstr.replace(/8/g,"°Ë");
		newstr=newstr.replace(/9/g,"¾Å");
	}	
	return newstr;
}

function ChangeMoney(str){
	var newstr="";
	if(str!=null&&str!=""){	
		newstr=str.replace(/1/g,"Ò¼");
		newstr=newstr.replace(/2/g,"·¡");
		newstr=newstr.replace(/3/g,"Èþ");
		newstr=newstr.replace(/4/g,"ËÁ");
		newstr=newstr.replace(/5/g,"Îé");
		newstr=newstr.replace(/6/g,"Â½");
		newstr=newstr.replace(/7/g,"Æâ");
		newstr=newstr.replace(/8/g,"°Æ");
		newstr=newstr.replace(/9/g,"¾Á");
		newstr=newstr.replace(/0/g,"Áã");
	}
	return newstr;
}

function ChangeMoneyB(st){
	var str = st.toString();
	var newstr="";
	if(str!=null&&str!=""){
	if(str.length>=1){
		if(str.substring(str.length-1,str.length)!=0){
		newstr=str.substring(str.length-1,str.length)+newstr;
		}
	}
	if(str.length>=2){
		if(str.substring(str.length-2,str.length-1)!=0){
		newstr=str.substring(str.length-2,str.length-1)+"Ê°"+newstr;
		}else if(str.substring(str.length-1,str.length)!=0){
		newstr="0"+newstr;
		}
	}
	if(str.length>=3){
		if(str.substring(str.length-3,str.length-2)!=0){
		newstr=str.substring(str.length-3,str.length-2)+"°Û"+newstr;
		}else if(str.substring(str.length-2,str.length-1)!=0){
		newstr="0"+newstr;
		}
	}
	if(str.length>=4){
		if(str.substring(str.length-4,str.length-3)!=0){
		newstr=str.substring(str.length-4,str.length-3)+"Çª"+newstr;
		}else if(str.substring(str.length-3,str.length-2)!=0){
		newstr="0"+newstr;
		}
	}
	if(str.length>=5){
		var strwan=str.substring(0,str.length-4);
		newstr="Íò"+newstr;
		if(strwan.length>=1){
		if(strwan.substring(strwan.length-1,strwan.length)!=0){
		newstr=strwan.substring(strwan.length-1,strwan.length)+newstr;
		}
	}
	if(strwan.length>=2){
		if(strwan.substring(strwan.length-2,strwan.length-1)!=0){
		newstr=strwan.substring(strwan.length-2,strwan.length-1)+"Ê°"+newstr;
		}else if(strwan.substring(strwan.length-1,strwan.length)!=0){
		newstr="0"+newstr;
		}
	}
	if(strwan.length>=3){
		if(strwan.substring(strwan.length-3,strwan.length-2)!=0){
		newstr=strwan.substring(strwan.length-3,strwan.length-2)+"°Û"+newstr;
		}else if(strwan.substring(strwan.length-2,strwan.length-1)!=0){
		newstr="0"+newstr;
		}
	}
	if(strwan.length>=4){
		if(strwan.substring(strwan.length-4,strwan.length-3)!=0){
		newstr=strwan.substring(strwan.length-4,strwan.length-3)+"Çª"+newstr;
		}
	}
	}
	}
	if(newstr!=null&&newstr!=""){	
		newstr=newstr.replace(/1/g,"Ò¼");
		newstr=newstr.replace(/2/g,"·¡");
		newstr=newstr.replace(/3/g,"Èþ");
		newstr=newstr.replace(/4/g,"ËÁ");
		newstr=newstr.replace(/5/g,"Îé");
		newstr=newstr.replace(/6/g,"Â½");
		newstr=newstr.replace(/7/g,"Æâ");
		newstr=newstr.replace(/8/g,"°Æ");
		newstr=newstr.replace(/9/g,"¾Á");
		newstr=newstr.replace(/0/g,"Áã");
	}
	return newstr;
}
