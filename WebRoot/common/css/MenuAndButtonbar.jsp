<%@ page contentType="text/css; charset=GBK" %>
/*开始一级菜单*/
.menu1_tr{
  height:52px;
  background-image:url('../images/toolbar/menu1_bg.gif');
  background-repeat:repeat;
}
.menu1_leftTd{
  border-top:1px #9c9c9c solid;
  border-left:1px #9c9c9c solid;
  word-wrap:break-word;
  vertical-align:bottom;
}
.menu1_rightTd{
  width:180px;
  border-top:1px #9c9c9c solid;
  background-color:transparent;
  background-image:url('../images/toolbar/menu1_bi.gif');
  background-repeat:no-repeat;
  background-position:right;
}
.menu1_selectItem_out{
  position:relative;
  left:0px;
  top:0px;
  background-color:#a8b293;
}
.menu1_selectItem_middle{
  position:relative;
  left:1px;
  top:2px;
  border-right:2px #8db881 solid;
  background-color:#cbd0af;
}
.menu1_selectItem_in{
  position:relative;
  left:2px;
  top:2px;
  border-top:2px #dce7bc solid;
  border-left:2px #dce7bc solid;
  background-color:#e6f1c6;
  text-align:center;
}
.menu1_normalItem_out{
  position:relative;
  left:0px;
  top:0px;
}
.menu1_normalItem_middle{
  position:relative;
  left:0px;
  top:0px;
}
.menu1_normalItem_in{
  position:relative;
  left:0px;
  top:0px;
  text-align:center;
  cursor:hand;
}
/*结束一级菜单*/
/*开始二级菜单*/
.menu2_tr{
  height:30px;
  background-color:#e6f1c6;
  background-image:url('../images/toolbar/menu2_bi.gif');
  background-repeat:no-repeat;
  background-position:right;
}
.menu2_Td{
  border-left:1px #9c9c9c solid;
  padding-top:5px;
}
.menu2_normalItem_out{
  text-align:center;
  cursor:hand;
}
.menu2_selectItem_out{
  border-top:1px #d4dcec solid;
  border-right:1px #646c94 solid;
  border-bottom:1px #646c94 solid;
  border-left:1px #d4dcec solid;
  background-image:url('../images/toolbar/menu2_btn_bg_select.gif');
  background-repeat:repeat-y;
  text-align:center;
}
.menu2_word{
  position:relative;
  top:2px;
  left:6px;
}
/*结束二级菜单*/
/*开始ButtonBar*/
.buttonUtil{
  height:29px;
  background-image:url('../images/toolbar/button_bg.gif');
  background-repeat:report-x;
}
.button_normal{
  position:relative;
  left:0px;
  top:3px;
  padding-top:3px;
  border:1px #98b7e0 solid;
  background-image:url('../images/toolbar/btn_normal_bg.gif');
  background-repeat:repeat-x;
  text-align:center;
  cursor:hand;
}
.button_normal_ex{
  position:relative;
  left:0px;
  top:3px;
  padding-top:5px;
  text-align: center;
  cursor:hand;
}
.button_over{
  position:relative;
  left:0px;
  top:3px;
  padding-top:3px;
  border:1px #557fbc solid;
  background-image:url('../images/toolbar/btn_over_bg.gif');
  background-repeat:repeat-x;
  text-align:center;
  cursor:hand;
}
.button_over_ex{
  position:relative;
  left:0px;
  top:3px;
  padding-top:4px;
  background-color:#FCF3A4;
  text-align: center;
  border-left:1px #CDCBCB solid;
  border-rigth:1px #666666 solid;
  border-bottom:1px #666666 solid;
  border-top:1px #CDCBCB solid;
  cursor:hand;
}
.button_mousedown{
  position:relative;
  left:0px;
  top:3px;
  padding-top:3px;
  border:1px #557fbc solid;
  background-image:url('../images/toolbar/btn_over_bg.gif');
  background-repeat:repeat-x;
  text-align:center;
  cursor:hand;
}
.button_mousedown_ex{
  position:relative;
  left:0px;
  top:3px;
  padding-top:4px;
  background-color:#FCF3A4;
  text-align: center;
  border-right:1px #CDCBCB solid;
  border-left:1px #666666 solid;
  border-top:1px #666666 solid;
  border-bottom:1px #CDCBCB solid;
  cursor:hand;
}
.button_word{
  position:relative;
  top:-2px;
  left:3px;
}
/*结束ButtonBar*/
