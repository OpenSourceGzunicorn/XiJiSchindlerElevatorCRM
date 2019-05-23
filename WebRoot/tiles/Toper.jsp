<%@ page contentType="text/html;charset=GBK" %>

<script language="javascript">
//必须实现window_onload()函数
function window_onload(){
	if(window.pageInitial != null)
		pageInitial();
	if(document.all.item("TANGER_OCX")!=null){
	TANGER_OCX_OpenDoc("");
	}	
}
</script>

<!-- 框架显示效果 -->
<body  style="background-color:#ffffff;" onload="javascript:window_onload();">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr id="contentTr" name="contentTr" class="Default_contentTr">
    <td id="contentTd" name="contentTd" class="Default_contentTd">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <!--开始构造圆角上边框-->
        <tr id="frameTr" name="frameTr" class="Default_frameTr">
          <td id="frame_topLeftTd" name="frame_topLeftTd" class="Default_frame_topLeftTd">
          </td>
          <td id="frame_topMiddleTd" name="frame_topMiddleTd" class="Default_frame_topMiddleTd">
          </td>
          <td id="frame_topRightTd" name="frame_topRightTd" class="Default_frame_topRightTd">
          </td>
        </tr>
        <!--结束构造圆角上边框-->
        <tr>
          <td colspan="3" id="frame_outBorder" name="frame_outBorder" class="Default_frame_outBorder">
            <div id="frame_inBorder" name="frame_inBorder" class="Default_frame_inBorder">
