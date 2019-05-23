<%@ page contentType="text/css; charset=GBK" %>
body{
  background-color:white;
  font-size:9pt;
  font-family: 宋体;
  margin:0px 0px 0px 0px;  /*top right bottom left*/
  padding:0px 0px 0px 0px;
  scrollbar-face-color: #DEE3E7;
  scrollbar-highlight-color: #FFFFFF;
  scrollbar-shadow-color: #DEE3E7;
  scrollbar-3dlight-color: #D1D7DC;
  scrollbar-arrow-color:  #006699;
  scrollbar-track-color: #EFEFEF;
  scrollbar-darkshadow-color: #98AAB1;
}
td{
  font-size:9pt;
  font-family: 宋体;
}
.subBody{
  margin:3px 5px 0px 5px;
}
.Default_menuFrame{  /*对应MenuAndButtonbar.css里的menu1_tr和menu2_tr*/
  width:100%;
  height:82px;
}
.Default_menu1Frame{  /*对应MenuAndButtonbar.css里的menu1_tr*/
  width:100%;
  height:52px;
}
.Default_menu2Frame{  /*对应MenuAndButtonbar.css里的menu2_tr*/
  width:100%;
  height:30px;
}
.Default_searchFrame{  /*对应searchTable*/
  width:100%;
  height:78px;
}
.Default_listFrame{   /*对应QueryList.css里的listUtil*/
  width:100%;
  height:355px;
}
.Default_contentTr{
  background-color:#ffffff;
}
.Default_contentTd{
  padding:6px;
  border-right:1px #ffffff solid;
  border-left:1px #ffffff solid;
}
.Default_frameTr{
  height:9px;
}
.Default_frame_topLeftTd{
  width:14px;
  background-image:url('../images/frame_corner_topleft.gif');
  background-repeat:no-repeat;
  background-position:left;
}
.Default_frame_topMiddleTd{
  width:97%;
  background-image:url('../images/frame_topbg.gif');
  background-repeat:repeat-x;
}
.Default_frame_topRightTd{
  width:14px;
  background-image:url('../images/frame_corner_topright.gif');
  background-repeat:no-repeat;
  background-position:right;
}
.Default_frame_bottomLeftTd{
  width:14px;
  background-image:url('../images/frame_corner_bottomleft.gif');
  background-repeat:no-repeat;
  background-position:left;
}
.Default_frame_bottomMiddleTd{
  width:97%;
  background-image:url('../images/frame_bottombg.gif');
  background-repeat:repeat-x;
}
.Default_frame_bottomRightTd{
  width:14px;
  background-image:url('../images/frame_corner_bottomright.gif');
  background-repeat:no-repeat;
  background-position:right;
}
.Default_frame_outBorder{
  width:100%;
  border-right:1px #d4d4d4 solid;
  border-left:1px #d4d4d4 solid;
	background-color:#ffffff;
}
.Default_frame_inBorder{
  width:100%;
  padding:0px 5px 5px 5px;
  border-right:2px #e4e2dc solid;
  border-left:2px #e4e2dc solid;
  background-color:#f4f2f2;
}
.searchUtil{
  /*height:78px;*/
  border-bottom:1px #d4d4cc solid;
  background-image:url('../images/search_bg.gif');
  background-repeat:no-repeat;
  background-position:left;
}
.searchTable{
  height:78px;
  border-bottom:1px #d4d4cc solid;
  background-image:url('../images/search_bg.gif');
  background-repeat:no-repeat;
  background-position:left;
  background-color:#f4f2f2;
}
.searchUtil_input{
  width:120px;
  height:22px;
  border:1px #859bb5 solid;
}
.searchUtil_select{
  width:120px;
  height:22px;
}
.searchUtil_btnOut{
  position:relative;
  top:0px;
  width:50px;
  border:1px #939EB2 solid;
}
.searchUtil_btnIn{
  width:48px;
  height:18px;
  padding-top:3px;
  border-right:1px #dcdcd9 solid;
  border-bottom:1px #dcdcd9 solid;
  text-align:center;
  background-color:white;
  background-image:url('../images/Btn_united_bg.gif');
  cursor:hand;
}
/*
.searchUtil_btnOut{
  position:relative;
  top:-1px;
  width:50px;
  height:18px;
  border:1px #949794 solid;
}
.searchUtil_btnIn{
  width:48px;
  height:16px;
  padding-top:3px;
  border-right:1px #dcdcd9 solid;
  border-bottom:1px #dcdcd9 solid;
  text-align:center;
  background-color:white;
  cursor:hand;
}
/**对话框**/
/*弹出信息提示框背景*/
.dialog_body
{
	background-color:D3E0FF;
	font-size:9pt;
	font-family: 宋体;
	margin:0px 0px 0px 0px;  /*top right bottom left*/
	padding:0px 0px 0px 0px;
	scrollbar-face-color: #DEE3E7;
	scrollbar-highlight-color: #FFFFFF;
	scrollbar-shadow-color: #DEE3E7;
	scrollbar-3dlight-color: #D1D7DC;
	scrollbar-arrow-color:  #006699;
	scrollbar-track-color: #EFEFEF;
	scrollbar-darkshadow-color: #98AAB1;
}
.dialog_info_bg
{
	background-image:url('../images/dialog/win_info_bg.jpg');
	background-repeat:no-repeat;
	background-position:left top;
	vertical-align:top;
}
.dialog_info_td
{
	padding-left:15px;
	padding-top:20px;
}
.dialog_pic_td
{
	padding-left:20px;
	padding-top:20px;
	width:30%;
}
.dialog_btn_td
{
	height:35px;
}
*/
/**
* showpage.jsp页面 用于减少td的显示宽度
*/
.showpage_td{
	padding-left:4px;
	padding-right:4px;
}
/**
* 导航条：用户 模块
*/
.top_navigation{
	border:#d4d4d4 1px solid;
	background-color:white;
}
/**
* EditFile.jsp
*/
.EditFile_bg{
  /*height:78px;*/
  /*border-bottom:1px #d4d4cc solid;*/
  background-image:url('../images/content_block_bg.gif');
  background-repeat:no-repeat;
  background-position:left top;
  border-right:1px #646c94 solid;
  border-bottom:1px #646c94 solid;
  border-left:1px #646c94 solid;
  border-top:1px #646c94 solid;
	background-color:#ffffff;
}
