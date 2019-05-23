var menu1Html="";             //暂存menu1的html变量
var menu2Html="";             //暂存menu2的html变量
var menu1OnItemName="";       //保存menu1中被选中的按钮名称
var menu2OnItemName="";       //保存menu2中被选中的按钮名称
var ButtonBarHtml="";         //暂存Buttonbar的html变量
var menu2DisplayBranch="";    //当有多个二级菜单，menu2DisplayBranch保存当前正在显示的menu2的名称

/************************/
/*功能：给menu1添加一个按钮*/
/*参数：*/
/*       aName：按钮的名称*/
/*  aNormalImg：按钮在正常状态下显示的图片*/
/*  aSelectImg：按钮在被选中状态下显示的图片*/
/*    aOverImg：按钮在鼠标移上去状态下显示的图片*/
/*        aDes：按钮上的文字*/
/*     aAttach：按钮附带的动作，如onclick、onmouseover、onmouseout等*/
/*       aLeft：按钮距离左边前一元素的像素*/
/*      aWidth：按钮的宽（像素）*/
/*     aHeight：按钮的高（像素）*/
/************************/
function menu1_addItem(aName,aNormalImg,aSelectImg,aOverImg,aDes,aAttach,aLeft,aWidth,aHeight){
  var temp="";

  temp = "<span style=\"position:relative;width:"+aLeft+"px;\"></span><span id=\""+aName+"_out\" name=\""+aName+"_out\" widthValue=\""+aWidth+"\" heightValue=\""+aHeight+"\" class=\"menu1_normalItem_out\" style=\"width:"+aWidth+"px;height:"+aHeight+"px;\"><span id=\""+aName+"_middle\" name=\""+aName+"_middle\" widthValue=\""+aWidth+"\" heightValue=\""+aHeight+"\" class=\"menu1_normalItem_middle\" style=\"width:"+aWidth+"px;height:"+aHeight+"px;\"><span id=\""+aName+"_in\" name=\""+aName+"_in\" widthValue=\""+aWidth+"\" heightValue=\""+aHeight+"\" class=\"menu1_normalItem_in\" style=\"width:"+aWidth+"px;height:"+aHeight+"px;\""+aAttach+"><img id=\""+aName+"_img\" name=\""+aName+"_img\" normalImg=\""+aNormalImg+"\" selectImg=\""+aSelectImg+"\" overImg=\""+aOverImg+"\" src=\""+aNormalImg+"\" border=\"0\"><br>"+aDes+"</span></span></span>";
  menu1Html = menu1Html+temp;
}

/************************/
/*功能：生成menu1*/
/*参数：*/
/*aDivName：指定menu1所在位置的div名称*/
/************************/
function createMenu1(aDivName){
  menu1Html = "<table style=\"table-layout:fixed;\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr class=\"menu1_tr\"><td class=\"menu1_leftTd\">"+menu1Html+"</td></tr></table>";
  document.all.item(aDivName).innerHTML = menu1Html;
  menu1Html="";
}

/************************/
/*功能：给menu1设置当前选中按钮*/
/*参数：*/
/*aName：按钮的名称*/
/************************/
function setMenu1OnItem(aName){
  if(aName == menu1OnItemName)
    return;
  if(menu1OnItemName!=""){
    var iWidth1=document.all.item(menu1OnItemName+"_out").widthValue;
    var iHeight1=document.all.item(menu1OnItemName+"_out").heightValue;
    document.all.item(menu1OnItemName+"_out").className = "menu1_normalItem_out";
    document.all.item(menu1OnItemName+"_out").style.width = iWidth1+"px";
    document.all.item(menu1OnItemName+"_out").style.height = iHeight1+"px";
    document.all.item(menu1OnItemName+"_middle").className = "menu1_normalItem_middle";
    document.all.item(menu1OnItemName+"_middle").style.width = iWidth1+"px";
    document.all.item(menu1OnItemName+"_middle").style.height = iHeight1+"px";
    document.all.item(menu1OnItemName+"_in").className = "menu1_normalItem_in";
    document.all.item(menu1OnItemName+"_in").style.width = iWidth1+"px";
    document.all.item(menu1OnItemName+"_in").style.height = iHeight1+"px";
    document.all.item(menu1OnItemName+"_img").src = document.all.item(menu1OnItemName+"_img").normalImg;
  }
  if(aName!=""){
    var iWidth2=document.all.item(aName+"_out").widthValue;
    var iHeight2=document.all.item(aName+"_out").heightValue;
    document.all.item(aName+"_out").className = "menu1_selectItem_out";
    document.all.item(aName+"_out").style.width = (parseInt(iWidth2)+parseInt(5))+"px";
    document.all.item(aName+"_out").style.height = (parseInt(iHeight2)+parseInt(4))+"px";
    document.all.item(aName+"_middle").className = "menu1_selectItem_middle";
    document.all.item(aName+"_middle").style.width = (parseInt(iWidth2)+parseInt(4))+"px";
    document.all.item(aName+"_middle").style.height = (parseInt(iHeight2)+parseInt(2))+"px";
    document.all.item(aName+"_in").className = "menu1_selectItem_in";
    document.all.item(aName+"_in").style.width = iWidth2+"px";
    document.all.item(aName+"_in").style.height = iHeight2+"px";
    if(document.all.item(aName+"_img").selectImg != "")
      document.all.item(aName+"_img").src = document.all.item(aName+"_img").selectImg;
  }
  menu1OnItemName=aName;
}

/************************/
/*功能：给menu2添加一个按钮*/
/*参数：*/
/*       aName：按钮的名称*/
/*  aNormalImg：按钮在正常状态下显示的图片*/
/*  aSelectImg：按钮在被选中状态下显示的图片*/
/*    aOverImg：按钮在鼠标移上去状态下显示的图片*/
/*        aDes：按钮上的文字*/
/*     aAttach：按钮附带的动作，如onclick、onmouseover、onmouseout等*/
/*       aLeft：按钮距离左边前一元素的像素*/
/*      aWidth：按钮的宽（像素）*/
/*     aHeight：按钮的高（像素）*/
/************************/
function menu2_addItem(aName,aNormalImg,aSelectImg,aOverImg,aDes,aAttach,aLeft,aWidth,aHeight){
  var temp="";

  temp = "<span style=\"position:relative;width:"+aLeft+"px;\"></span><span id=\""+aName+"_out\" name=\""+aName+"_out\" widthValue=\""+aWidth+"\" heightValue=\""+aHeight+"\" class=\"menu2_normalItem_out\" style=\"width:"+aWidth+"px;height:"+aHeight+"px;\""+aAttach+"><img id=\""+aName+"_img\" name=\""+aName+"_img\" normalImg=\""+aNormalImg+"\" selectImg=\""+aSelectImg+"\" overImg=\""+aOverImg+"\" src=\""+aNormalImg+"\" border=\"0\" align=\"absmiddle\"><span class=\"menu2_word\">"+aDes+"</span></span>";
  menu2Html = menu2Html+temp;
}

/************************/
/*功能：生成menu2*/
/*参数：*/
/*      aDivName：指定menu2所在位置的div名称*/
/*    aTableName：当有多个二级菜单时，指定各个二级菜单的名称*/
/*aDefaultOnItem：指定二级菜单默认的选中按钮*/
/************************/
function createMenu2(aDivName,aTableName,aDefaultOnItem){
  menu2Html = "<table id=\""+aTableName+"\" name=\""+aTableName+"\" defaultOnItem=\""+aDefaultOnItem+"\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"display:none;\"><tr class=\"menu2_tr\"><td class=\"menu2_Td\">"+menu2Html+"</td></tr></table>";
  document.all.item(aDivName).innerHTML += menu2Html;
  menu2Html="";
}

/************************/
/*功能：当有多个二级菜单时，给每个二级菜单设置各自默认的选中按钮*/
/*参数：*/
/*aName：二级菜单的名称*/
/************************/
function setMenu2DefaultOnItem(aName){
  var itemName=document.all.item(aName).defaultOnItem;
  setMenu2OnItem(itemName);
}

/************************/
/*功能：给menu2设置当前选中按钮*/
/*参数：*/
/*aName：按钮的名称*/
/************************/
function setMenu2OnItem(aName){
  if(aName == menu2OnItemName)
    return;
  if(menu2OnItemName!=""){
    var iWidth1=document.all.item(menu2OnItemName+"_out").widthValue;
    document.all.item(menu2OnItemName+"_out").className = "menu2_normalItem_out";
    document.all.item(menu2OnItemName+"_out").style.width = iWidth1+"px";
    document.all.item(menu2OnItemName+"_img").src = document.all.item(menu2OnItemName+"_img").normalImg;
  }
  if(aName!=""){
    var iWidth2=document.all.item(aName+"_out").widthValue;
    document.all.item(aName+"_out").className = "menu2_selectItem_out";
    document.all.item(aName+"_out").style.width = (parseInt(iWidth2)-parseInt(30))+"px";
    if(document.all.item(aName+"_img").selectImg != "")
      document.all.item(aName+"_img").src = document.all.item(aName+"_img").selectImg;
  }
  menu2OnItemName=aName;
}

/************************/
/*功能：当有多个二级菜单时，设置当前显示那个二级菜单*/
/*参数：*/
/*aName：二级菜单的名称*/
/************************/
function displayMenu2Branch(aName){
  if(aName==menu2DisplayBranch)
    return;
  if(menu2DisplayBranch!="")
    document.all.item(menu2DisplayBranch).style.display = "none";
  if(aName!=""){
    document.all.item(aName).style.display = "block";
    setMenu2DefaultOnItem(aName);
  }
  menu2DisplayBranch=aName;
}

/************************/
/*功能：给Buttonbar添加一个按钮*/
/*参数：*/
/*          aName：按钮的名称*/
/*     aNormalImg：按钮在正常状态下显示的图片*/
/*  aMousedownImg：按钮在鼠标被按下去一瞬间显示的图片*/
/*       aOverImg：按钮在鼠标移上去状态下显示的图片*/
/*   aNormalClass：按钮在正常状态下所用的classname，缺省为button_normal*/
/*aMousedownClass：按钮在鼠标按下去的状态下所用的classname，缺省为button_mousedown*/
/*     aOverClass：按钮在鼠标放上去的状态下所用的classname，缺省为button_over*/
/*           aDes：按钮上的文字*/
/*        aAttach：按钮附带的动作，如onclick、onmouseover、onmouseout等*/
/*          aLeft：按钮距离左边前一元素的像素*/
/*         aWidth：按钮的宽（像素）*/
/*        aHeight：按钮的高（像素）*/
/************************/
function ButtonBar_addItem(aName,aNormalImg,aMousedownImg,aOverImg,aNormalClass,aMousedownClass,aOverClass,aDes,aAttach,aLeft,aWidth,aHeight){
  var temp="";
  var normalClass="button_normal",mousedownClass="button_mousedown",overClass="button_over";

  if(aNormalClass!=""&&aNormalClass!=null)
    normalClass=aNormalClass;
  if(aMousedownClass!=""&&aMousedownClass!=null)
    mousedownClass=aMousedownClass;
  if(aOverClass!=""&&aOverClass!=null)
    overClass=aOverClass;
  temp = "<span style=\"position:relative;width:"+aLeft+"px;\"></span><span id=\""+aName+"\" name=\""+aName+"\" normalClass=\""+normalClass+"\" mousedownClass=\""+mousedownClass+"\" overClass=\""+overClass+"\" class=\""+normalClass+"\" style=\"width:"+aWidth+"px;height:"+aHeight+"px;\""+aAttach+"><img id=\""+aName+"_img\" name=\""+aName+"_img\" normalImg=\""+aNormalImg+"\" keydownImg=\""+aMousedownImg+"\" overImg=\""+aOverImg+"\" src=\""+aNormalImg+"\" border=\"0\"><span class=\"button_word\">"+aDes+"</span></span>";
  ButtonBarHtml = ButtonBarHtml+temp;
}

/************************/
/*功能：生成Buttonbar*/
/*参数：*/
/*aName：指定Buttonbar所在位置的名称*/
/************************/
function createButtonBar(aName){
  ButtonBarHtml = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td>"+ButtonBarHtml+"</td></tr></table>";
  document.all.item(aName).innerHTML = ButtonBarHtml;
  ButtonBarHtml="";
}

/************************/
/*功能：鼠标移到ButtonBar按钮上的效果*/
/*参数：*/
/*aName：指定按钮的名称*/
/************************/
function buttonItemOver(aName){
  document.all.item(aName).className = document.all.item(aName).overClass;
}

/************************/
/*功能：鼠标点击ButtonBar按钮时的效果*/
/*参数：*/
/*aName：指定按钮的名称*/
/************************/
function buttonItemMouseDown(aName){
  document.all.item(aName).className = document.all.item(aName).mousedownClass;
}

/************************/
/*功能：鼠标松开点击ButtonBar按钮动作时的效果*/
/*参数：*/
/*aName：指定按钮的名称*/
/************************/
function buttonItemMouseUp(aName){
  document.all.item(aName).className = document.all.item(aName).overClass;
}

/************************/
/*功能：鼠标离开ButtonBar按钮的效果*/
/*参数：*/
/*aName：指定按钮的名称*/
/************************/
function buttonItemOut(aName){
  document.all.item(aName).className = document.all.item(aName).normalClass;
}
