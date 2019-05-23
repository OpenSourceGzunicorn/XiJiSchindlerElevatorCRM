<%@ page contentType="application/x-javascript;charset=GBK" %>
//==这里开始 为了实现树形显示，对树模型对象进行适当的扩充的代码

//图片路径常量t
var imagePath = "../common/images/tree/";
var StrLeafImage = "<img src=\"" + imagePath + "line.gif\" border=0>";
var StrLeafImage1 = "<img src=\"" + imagePath + "line1.gif\" border=0>";
var StrEmptyImage = "<img src=\"" + imagePath + "dot.gif\" border=0 style=\"width:15px\">";

var StrCloseImage = "<img src=\"" + imagePath + "add.gif\" border=\"0\">";
var StrExpandImage = "<img src=\"" + imagePath + "sub.gif\" border=\"0\">";

var StrCloseImageTop = "<img src=\"" + imagePath + "top1.gif\" border=\"0\">";
var StrExpandImageTop = "<img src=\"" + imagePath + "top2.gif\" border=\"0\">";
var StrCloseImageBottom = "<img src=\"" + imagePath + "bottom1.gif\" border=\"0\">";
var StrExpandImageBottom = "<img src=\"" + imagePath + "bottom2.gif\" border=\"0\">";
var StrCloseImageLeft = "<img src=\"" + imagePath + "left1.gif\" border=\"0\">";
var StrExpandImageLeft = "<img src=\"" + imagePath + "left2.gif\" border=\"0\">";

var StrExpandImage2 = "<img src=\"" + imagePath + "open.gif\" border=\"0\">";
var StrCloseImage2 = "<img src=\"" + imagePath + "close.gif\" border=\"0\">";

//当前节点的值
var StrLastNode="";
var StrNewNodeName="新节点";
var StrNewNodeDesc="新节点";

var sMsg="";
var sToolBar;
//--树结点类-------------------------------------------------------------

function tNode(a_NodeName,a_ParaName,a_ParaNode,a_tAry,a_Tree)
{
        this.nodeName = a_NodeName; //结点代码
        this.paraName = a_ParaName; //结点父代码
        this.paraNode = a_ParaNode; //结点的父结点
        this.array = a_tAry; //其他参数，例如结点的描述等，放在一个tArray增强型数组里
        this.tree = a_Tree; //结点所在的tTree类树对象
        this.children = new tArray(); //结点的儿子集合，是一个tArray类对象

        this.Update = tNodeUpdate; //更新一个结点的方法
        this.toString = tNodetoString; //Debug用的打出结点信息的方法
        this.isLeaf = tNodeisLeaf; //判断本结点是否叶子结点的方法
        this.Layer = tNodeLayer; //返回本结点在树中的第几层，从0开始计数
        this.Index = tNodeIndex; //返回在同一层中本结点是第几个结点，从0开始计数
        this.BrotherCount = tNodeBrotherCount; //返回同一层中有多少个兄弟结点，包括自己
        this.Clone = tNodeClone; //克隆本结点

        this.isRender = false;
        this.ExpandNode = tExpandNode;

        //上面的是通用的，下面的是为了实现树形显示而写的
        this.RenderTable = tNodeRenderTable;
        this.HideTable = tNodeHideTable;
        this.ExpandImage = tNodeExpandImage;
        this.OnClick = tNodeOnClick;
        this.OnContextMenu = tNodeOnContextMenu;
        this.VLine = tNodeVLine;
        this.AdjustPic = tNodeAdjustPic;

        this.onmouseupex = tNodeOnMouseUp;
        this.onmouseoverex = tNodeOnMouseOver;
        this.onmouseoutex = tNodeOnMouseOut;
        this.onmousedownex = tNodeOnMouseDown;

}


function tNodeClone()
{
        var l_node = new tNode(this.nodeName,this.paraName,this.paraNode,this.array,this.tree);
        l_node.children = this.children;
        return l_node;
}

function tNodeOnMouseDown()
{
        this.tree.OnMouseDownNode = this;
}

function tNodeOnMouseOver()
{
        return;
        self.status += this.nodeName + ':';
//	if ( this.tree.OnMouseDownNode != this )
//		this.tree.OnMouseOverNode = this;
        if ( this.tree.OnMouseOverNode != null )
                return;
        this.tree.OnMouseOverNode = this;
        alert('MouseOver:' + this.nodeName);
}

function tNodeOnMouseOut()
{
        this.tree.OnMouseOverNode = null;
}

/*
function tNodeVLine()
功能：调整一个结点的前面应该放什么虚线style
输入：
输出：
*/
function tNodeVLine()
{
        var l_td = document.all.item(this.nodeName + "_vline");
        if ( l_td == null ){
                return;
        }
        if ( ! ( this.paraNode != null
                        && ( parseInt( this.paraNode.Index() ) == parseInt( this.paraNode.BrotherCount() -1 ) )
                        )
                )
        {
                l_td.style.backgroundPositionX = "4px";
                l_td.style.backgroundImage = "url(" + imagePath + "line-bg.gif)";
                l_td.style.backgroundRepeat = "repeat-y";
        }else{
                l_td.style.backgroundImage = "";
                l_td.style.backgroundRepeat = "";
                }
}

//调整结点的加碱号、虚线、文件夹图片
function tNodeAdjustPic(a_expand)
{
        var l_tb = document.all.item(this.nodeName + '_childrentable');
        var l_expand = document.all.item(this.nodeName + "_expand");
        l_dis = l_tb.style.display;

        var l_folder = document.all.item(this.nodeName + '_folder');

        l_expand.style.cursor = ( this.children.count > 0 ) ? "hand" : "";

        if (l_dis == '' && this.children.count > 0 )
        {
                l_expand.innerHTML = this.ExpandImage(1);
                if (this.array.Find('ExpandImage') != null ) //如果有自定义的展开图片就用
                        l_folder.innerHTML = "<img src=\"" + imagePath + this.array.Find('ExpandImage') + "\" border=\"0\">";
                else //否则用缺省的展开图片
                        l_folder.innerHTML = StrExpandImage2;
        }else{
                l_expand.innerHTML = this.ExpandImage(0);
                if (this.array.Find('CloseImage') != null ) //如果有自定义的收缩图片就用
                        l_folder.innerHTML = "<img src=\"" + imagePath + this.array.Find('CloseImage') + "\" border=\"0\">";
                else //否则用缺省的收缩图片
                        l_folder.innerHTML = StrCloseImage2;
        }

        this.VLine();
}

//结点的右键菜单事件
function tNodeOnContextMenu()
{
        /*
   popupoptions = [
                                                   new ContextItem("刷新本页",function(){window.location.reload();}),
                                                new ContextItem("Disabled Item",null,true),
                                                new ContextSeperator(),
                                                new ContextItem("打印",function(){window.print()}),
                                                new ContextItem("查看结点代码",function(){tNodeOnClick();document.body.focus();}),
                                                new ContextSeperator(),
                                                   new ContextItem("哈哈",function(){alert('哈哈');document.body.focus();})
                                     ]

   ContextMenu.display(popupoptions)*/
}

//结点的OnClick事件
function tNodeOnClick()
{
        var elem = window.event.srcElement;
        var strNodeID;

        strNodeID=elem.id;
        SetDBFocus(strNodeID);
}

//设置节点的焦点
//设置节点的焦点
function SetDBFocus(strNodeID)
{
        //alert(strNodeID)
        var oRightiFrm = null;
        if (document.all.item(strNodeID) == null)
                return;
        if (StrLastNode != "")
        {
                document.all.item(StrLastNode).style.fontWeight = 'normal';
        }
        document.all.item(strNodeID).style.fontWeight = 'bold';
        StrLastNode = strNodeID;

				var oNode = document.all.item(strNodeID);
				//alert(oNode.outerHTML);
				//alert(oNode.oTreeName);

				if ( oNode.oTreeName != '' )
					eval('oTree = ' + oNode.oTreeName + ';');
				//alert('ok');

        if (StrLastNode != "")
        {
                StrCurrNode = StrLastNode.replace("_Value","");

                oNode = oTree.Find(StrCurrNode);
                sLink = oNode.array.Find('cate_link');
                sTarget = oNode.array.Find('cate_target');
                try
                {
                        oRightiFrm = window.parent.frames(sTarget);
                }
                catch (e){}
                if (oRightiFrm == null)
                        window.location = sLink;
                else
                        oRightiFrm.location = sLink;
        }
}
/*
function SetDBFocus(strNodeID)
{
        //alert(strNodeID)
        var oRightiFrm = null;
        if (document.all.item(strNodeID) == null)
                return;
        if (StrLastNode != "")
        {
                document.all.item(StrLastNode).style.fontWeight = 'normal';
        }
        document.all.item(strNodeID).style.fontWeight = 'bold';
        StrLastNode = strNodeID;

        if (StrLastNode != "")
        {
                StrCurrNode = StrLastNode.replace("_Value","");
				
                oNode = oTree.Find(StrCurrNode);
                sLink = oNode.array.Find('cate_link');
                sTarget = oNode.array.Find('cate_target');
                try
                {
                        oRightiFrm = window.parent.frames(sTarget);
                }
                catch (e){}
                if (oRightiFrm == null)
                        window.location = sLink;
                else
                        oRightiFrm.location = sLink;
        }
}
*/

/*
function tNodeHideTable()
功能：处理点击一个结点前面的加号、减号的事件
输入：
输出：
*/
function tNodeHideTable()
{
        var l_tb,l_dis,l_expand,l_folder;
        if ( this.children.count < 1 )
                {return;}
        l_tb = document.all.item(this.nodeName + '_childrentable');
        l_dis = l_tb.style.display;
        l_expand = document.all.item(this.nodeName + '_expand');
        l_folder = document.all.item(this.nodeName + '_folder');
        if ( l_dis == 'none' )
        {
          this.ExpandNode();

                l_tb.style.display = '';
                l_expand.innerHTML = this.ExpandImage(1);
                if (this.array.Find('ExpandImage') != null ) //如果有自定义的展开图片就用
                        l_folder.innerHTML = "<img src=\"" + imagePath + this.array.Find('ExpandImage')  + "\" border=\"0\">";
                else //否则用缺省的展开图片
                        l_folder.innerHTML = StrExpandImage2;
        }else{
                l_tb.style.display = 'none';
                l_expand.innerHTML = this.ExpandImage(0);
                if (this.array.Find('CloseImage') != null ) //如果有自定义的收缩图片就用
                        l_folder.innerHTML =  "<img src=\"" + imagePath +  this.array.Find('CloseImage')   + "\" border=\"0\">";
                else //否则用缺省的收缩图片
                        l_folder.innerHTML = StrCloseImage2;
        }
}

/*
function tNodeExpandImage(a_expand)
功能：判断一个结点的前面应该放哪个加号、减号的图片
输入：
        a_expand：本结点是否展开状态，1：展开，0：收缩
输出：
        应该放的图片的字符串
*/
function tNodeExpandImage(a_expand)
{
        //加减号图片
        if ( this.children.count > 0 ) //非叶子结点
        {
                if ( this.Layer() == 0 && this.BrotherCount() == 1 ) //第一层中只有一个结点
                        return (a_expand == 1) ? StrExpandImage : StrCloseImage;
                else if ( this.Index() == 0 && this.Layer() == 0 ) //第一层的第一个结点
                        return (a_expand == 1) ? StrExpandImageTop : StrCloseImageTop;
                else if ( this.Index() == this.BrotherCount() - 1 ) //一层中的最后一个结点
                        return (a_expand == 1) ? StrExpandImageBottom : StrCloseImageBottom;
                else
                        return (a_expand == 1) ? StrExpandImageLeft : StrCloseImageLeft; //一般的结点
        }else{ //叶子结点
                if ( this.Index() == this.BrotherCount() - 1 ) //一层中的最后一个结点
                        return StrLeafImage1;
                else
                        return StrLeafImage; //非一层中的最后一个结点
        }
}


function tNodeOnMouseUp()
{
        var el,tar, oTar, el_tr, tar_tr, tar_el, l_table, el_tr_0, l_node;

        //把本结点的位置还原
        el = document.all.item(this.nodeName + '_Move'); //得到本结点的tr的移动Div
        el.style['z-index'] = 0;
        el.style.pixelLeft = 0;
        el.style.pixelTop = 0;

        if ( this.paraNode == null || this.paraNode.children.count < 2 ) //如果本结点是根结点或者本结点没有兄弟结点就退出
                return;

        tar_el = document.elementFromPoint(event.clientX ,event.clientY); //当前鼠标位置html元素
        tar = getReal(tar_el); //目标结点Div
        if ( tar == null || tar.nodeName == null ) //如果目标结点的信息不足就退出
                return;

        el_tr = document.all.item(this.nodeName + '_Tr'); //本结点Tr
        tar_tr = document.all.item(tar.nodeName + '_Tr'); //目标结点Tr
        oTar = this.tree.Find(tar.nodeName); //目标结点对象

//	alert(el_tr.outerHTML);
//	alert(tar_tr.outerHTML);
//	alert(tar_tr.rowIndex + '\n' + el_tr.rowIndex);

        if ( oTar != null //目标结点是tNode结点
         && oTar.paraNode != null //目标结点不是根结点
         && tar.nodeName != this.nodeName //目标结点不是本结点
         && this.children.Find(tar.nodeName) == null //目标结点不是本结点的儿子
         && this.paraNode.nodeName != tar.nodeName //目标结点不是本结点的父亲
         && this.paraNode.nodeName == oTar.paraNode.nodeName //目标结点和本结点是兄弟结点
        )
        {
                el_tr_0 = document.createElement("TR");
                l_table = el_tr.parentElement;
                l_table.insertBefore(el_tr_0,tar_tr);
                el_tr.swapNode(el_tr_0);
                el_tr_0.removeNode();

                l_node = this.Clone();
                rmv = l_node.tree.Remove(this.nodeName, 1); //只去掉本结点，不去掉儿子结点
                ins = l_node.tree.insertBefore(tar.nodeName,l_node.nodeName,l_node);
                ins2 = '';
                if ( l_node.paraNode != null )
                {
                        ins2 = l_node.paraNode.children.insertBefore(tar.nodeName,l_node.nodeName,l_node);
                }
                l_node.tree.AdjustPic();
//		alert('rmv=' + rmv + '\nins=' + ins + '\nins2=' + ins2);
//		alert('l_node.nodeName=' + l_node.nodeName + '\ntar.nodeName=' + tar.nodeName);
//		divDebug.innerText = l_node.tree.toString();

                //修改各节点在数据库中的次序
                //1、首先得到本节点的父节点，并对父节点的childern进行循环得到各节点的顺序
                ChangeNodeIndex(l_node);
        }
}


//重新排序
function ChangeNodeIndex(a_node)
{
        var l_strchild,l_oNode;
        var l_SQL;
        l_SQL="";
        if (a_node.paraNode!=null)
                {

                        for (l_strchild in a_node.paraNode.children.nodes)
                        {
                                l_oNode = a_node.paraNode.children.nodes[l_strchild];
                                l_SQL += l_oNode.nodeName + "," + l_oNode.Index() + ";"
                        }

                ModuleMain.HOrder.value = l_SQL;
                ModuleMain.target="update";
                ModuleMain.action="_AdjustOrder.asp";
                ModuleMain.submit ();
                ModuleMain.HOrder.value = "";
                SetDBFocus(a_node.nodeName+"_Value");
                }

}
/*
function tNodeRenderTable()

功能：构造一个结点的界面，返回html字符串
输入：
输出：
        本结点的html字符串
*/
function tNodeRenderTable()
{
        var l_name,l_node,l_return = "";
        l_return += "<tr id='" + this.nodeName + "_tr'>\n";

        //虚线竖线
        l_node = this.paraNode;
        if ( l_node != null )
        {
                l_return += "<td id='" + this.nodeName + "_vline' style=\"width:18px;";
        if ( ! ( this.paraNode != null
                        && this.paraNode.Index() == ( this.paraNode.BrotherCount() -1 )
                        )
                )
        {
                l_return += "background-position-x:4px;background-repeat:repeat-y;background-image:url(" + imagePath + "line-bg.gif);";
                l_return += "\">";
/*		l_return += this.nodeName + ';' + this.BrotherCount()
                                         + ';' + this.Index()
                                         + ';' + this.paraNode.BrotherCount()
                                         + ';' + this.paraNode.Index();
*/		l_return +="&nbsp;</td>\n";
        }
        else
        {
                l_return += "\">&nbsp;</td>\n";
        }
        }

        l_return += "<td style=\"width:4px\"></td>\n";

        l_return += "<td>\n";

        l_return += "<table border=0 cellspacing=0 cellpadding=0>";
        l_return += "<tr >";

        l_return += "<td style=\"width:0%\">";
        l_return += "<span id='" + this.nodeName + "_expand'";

        l_return += " onclick=\"javascript:oTree.Find('" + this.nodeName + "').HideTable();\"";

        //加减号图片
        if ( this.children.count > 0 ) //非叶子结点
        {
                l_return += " style=\"cursor:hand;width:18px\">";
        }else{ //叶子结点
                l_return += " style=\"width:18px\">";
        }
        l_return += this.ExpandImage(this.tree.ShowOnRender);
        l_return += "</span>";
        l_return += "</td >";

        l_return += "<td style=\"width:0%\">";

        //文件夹图片
        l_return += "<span id='" + this.nodeName + "_folder'>";
	if(this.array.Find('CloseImage') != null){
	        l_return += "<img src=\"" + imagePath + this.array.Find('CloseImage') + "\" border=\"0\">";
	}else{
	        l_return += StrCloseImage2;
	}
        l_return += "</span>";

        l_return += "</td>";
        l_return += "<td align=left>";

        //分类的描述
        l_return += "<div nodeName=\"" + this.nodeName + "\"";

        l_return += " onmousedownex=\"javascript:oTree.Find('" + this.nodeName + "').onmousedownex();\"";

        l_return += " onmouseupex=\"javascript:oTree.Find('" + this.nodeName + "').onmouseupex();\"";
        l_return += " id='" + this.nodeName + "_Move'";
        l_return += " class=moveme style=\"position:relative;\">";
        l_return += "<div id='" + this.nodeName + "_Value' type=text readonly style=\"word-break:keep-all;height:13px;cursor:hand;border:0\"";
        l_return += " id='" + this.nodeName + "_desc'";

//        l_return += " onclick=\"javascript:oTree.Find('" + this.nodeName + "').OnClick();\"";
//##
				if ( this.tree.oTreeName != null )
        	l_return += " onclick=\"javascript:" + this.tree.oTreeName + ".Find('" + this.nodeName + "').OnClick();\"";
        else
	        l_return += " onclick=\"javascript:oTree.Find('" + this.nodeName + "').OnClick();\"";

        l_return += " oncontextmenu=\"javascript:oTree.Find('" + this.nodeName + "').OnContextMenu()\"";
        l_return += " class=\"node\"";
//##
				if ( this.tree.oTreeName != null )
	        l_return += " oTreeName=\"" + this.tree.oTreeName + "\"";

        l_return += " >";
        l_return += this.array.Find('cate_desc');
        l_return += "</div>";
        l_return += "</div>";

        l_return += "</td>";

        l_return += "</tr>";

        //子分类的table
        l_return += "<tr>";

        l_return += "<td colspan=3>";
        l_return += "<table border=0 cellspacing=0 cellpadding=0 width=\"100%\"";
        if ( this.tree.ShowOnRender == 0 )
                l_return += " style=\"display:none\"";
        l_return += " id='" + this.nodeName + "_childrentable'>\n";

        if(this.tree.ShowOnRender == 1){
          for ( l_name in this.children.nodes )
          {
            l_node = this.children.nodes[l_name];
          l_return += l_node.RenderTable();
          }
        }

        l_return += "</table>\n";
        l_return += "</td>";

        l_return += "</tr>";
        l_return += "</table>";

        //结束
        l_return += "</td>\n";
        l_return += "</tr>\n";
        return l_return;
}

//modified by AlexZeng
/**
 * 生成子节点
 * @return
 */
function tExpandNode(){
  var l_return = "";
  var l_ctbl = document.all.item(this.nodeName + '_childrentable');
  if(!this.isRender){
    l_return += "<table border=0 cellspacing=0 cellpadding=0 width=\"100%\"";
    l_return += " id='" + this.nodeName + "_childrentable'>\n";

    for ( l_name in this.children.nodes )
    {
            l_node = this.children.nodes[l_name];
            l_return += l_node.RenderTable();
    }

    l_return += "</table>\n";

    l_ctbl.outerHTML = l_return;
    l_ctbl.style.display = '';
    this.isRender = true;
  }
}
//end by AlexZeng

//--树类-------------------------------------------------------------

function tTree() //构造树类
{
        this.nodes = new tArray(); //树的结点集合
        this.count = tTreeCount; //树的结点个数
        this.Add = tAddNode; //增加一个结点的方法
        this.Find = tFindNode; //根据代码找到一个结点的方法
        this.Remove = tRemoveNode; //删除一个结点的方法
        this.Update = tUpdateNode; //更新一个结点的方法
        this.insertBefore = tInsertBefore; //插入一个结点
        this.toString = tTreetoString; //用于Debug时，返回树的信息的方法

//上面的是通用的，下面的是为了实现树形显示而写的
        this.ShowOnRender = 0; //缺省树的开始状态是不展开的
        this.RenderTable = tRenderTable;
        this.AdjustPic = tAdjustPic;
        this.UpdateEx = tUpdateNodeEx;

}

function setoTree(newoTree)
{
	this.oTree = newoTree;
}

function tInsertBefore(a_NodeName1,a_NodeName2,a_node)
{
        return this.nodes.insertBefore(a_NodeName1,a_NodeName2,a_node);
}

function tUpdateNodeEx(a_nodeName,a_paraName,a_ary)
{
        var l_tAry = new tArray(), l_s;
        for ( l_s in a_ary )
        {
                l_tAry.Add( l_s, a_ary[l_s] );
        }
        this.Update(a_nodeName,a_paraName,l_tAry);
}

function tAdjustPic(pareID)
{
        var l_node;
        var l_nodeName;
        if(pareID == "")
          pareID = null;
        if(pareID != null && pareID != ""){
          this.nodes.Find(pareID).AdjustPic();
        }
        for ( l_nodeName in this.nodes.nodes )
        {
          l_node = this.nodes.nodes[l_nodeName];
          if ((pareID == null && l_node.paraNode == null) || (l_node.paraNode != null && l_node.paraNode.nodeName == pareID) || l_nodeName == pareID){
            l_node.AdjustPic();
          }
        }
}

/*
function tRenderTable(a_ShowOnRender)
功能：构造一棵树的界面，返回html字符串
输入：
        a_ShowOnRender：是否展开整棵树，1：展开，0：收缩
输出：
        本树的html字符串
*/
function tRenderTable(a_ShowOnRender)
{
        var l_name,l_node,l_return = "";

        this.ShowOnRender = a_ShowOnRender;
        this.tTreeTable = 'tTreeTable';

        while ( document.all.item(this.tTreeTable) != null )
                this.tTreeTable += '0';

        l_return += "<table ID=\"" + this.tTreeTable + "\" border=0 cellspacing=0 cellpadding=0>\n";
        for ( l_name in this.nodes.nodes )
        {
                l_node = this.nodes.nodes[l_name];
                if ( l_node.paraNode != null )
                        continue;
                l_return += l_node.RenderTable();
        }
        l_return += "</table>\n";
        return l_return;
}

/*删除当前选中的节点
*/
function OnDelete(){
                var TrID,NodeName,NodeDesc;
                var oNode,bReturn;

                if ( StrLastNode != "" && StrLastNode != "0_Value" ){
                        //得到当前行的ID
                        NodeName = StrLastNode.substr(0,StrLastNode.length-6);
                        //得到当前的节点

                        oNode = oTree.Find(NodeName)
                        //得到节点的描述
                        NodeDesc = oNode.array.Find('cate_desc')
                        //判断当前节点是否有子节点，如果有则不允许删除
                        if (oNode.children.count > 0 )
                                {
                                        alert("请先删除节点["+NodeDesc+"]的子节点。");
                                        return;
                                }
                        bReturn=confirm("确定要删除节点["+NodeDesc+"]吗?");
                        if (bReturn!=true)
                                {
                                        return;
                                }
                        //从数据库删除
                        document.all.item("HNodeValue").value = NodeName;
                        ModuleMain.target="update";
                        ModuleMain.action="_TreeAdd.asp?m_Operation=del";
                        ModuleMain.submit ();
                        document.all.item("HNodeValue").value = "";
                }

}

//在数据库删除数据后再删除节点
function OnDeleteNode(NodeName)
{
        var oNode,sPara;
        var strNodeID="",i,l_s;

        oNode = oTree.Find(NodeName);

        //判断是否有兄弟节点，如果有则将焦点移动到兄弟节点。
        //如果没有兄弟节点则移动到该节点的父节点上。
        if (oNode.paraNode==null)
                {
                        strNodeID="";

                        for ( l_name in oTree.nodes.nodes )
                        {
                                l_node = oTree.nodes.nodes[l_name];
                                if ( l_node.paraNode == null ){
                                  strNodeID = l_name;
                                  break;
                                }
                        }

                }
        else
                {
                        for (l_s in oNode.paraNode.children.nodes)
                        {
                                if (l_s!=NodeName){
                                        strNodeID=l_s;
                                        break;
                                        }
                        }
                        if (strNodeID=="")
                                {
                                        strNodeID=oNode.paraNode.nodeName;
                                }
                }
        TrID = NodeName + "_TR";
        document.all.item(TrID).removeNode(true);
        oTree.Remove(NodeName);
        StrLastNode = "";
        oTree.AdjustPic();
        if (strNodeID!=""&&strNodeID!="undefined")
        {	strNodeID=strNodeID+"_Value";}
        SetDBFocus(strNodeID);
}

//给当前节点增加一个子节点
function OnAddNew(){
        //如果当前没有选中节点则返回
        var paraTable,TrID,l_array,oTr,oNode,sReturn,oPara;
        var para_id,modu_id,modu_title,modu_description

        if (StrLastNode=="")
                return;

        SwitchPage("property");

        //假设得到相关的资料,并建立一个新节点
        para_id=StrLastNode.substr(0,StrLastNode.length-6);

        //
        AddToDB(para_id);
}

//向数据库添加一条记录,返回新的记录的NodeValue
function AddToDB(a_paraid)
{

        if (a_paraid == '0') {a_paraid='';}
        document.all.item("HNodeValue").value = a_paraid;
        document.all.item("HNodeName").value = StrNewNodeName;
        document.all.item("HNodeDesc").value = StrNewNodeDesc;
                //ModuleMain.target="update";
        ModuleMain.target="_blank";
        ModuleMain.action="_TreeAdd.asp?m_Operation=add";

        ModuleMain.submit ();
        document.all.item("HNodeValue").value = "";
        document.all.item("HNodeName").value = "";
        document.all.item("HNodeDesc").value = "";
}

function AddNewNode(a_ParaID,a_NewNodeID,a_NewNodeDesc,a_NewNodeLink,a_NewNodeTarget)
{
        var paraTable,l_array,oTr,oTd,oNode,sReturn,oDiv;

        if (a_ParaID == "")
        {
                a_ParaID = null;
        }
        if(a_ParaID != null)

          oTree.Find(a_ParaID).ExpandNode();

        l_array = new tArray();
        l_array.Add('cate_desc', a_NewNodeDesc);
        l_array.Add('cate_value', a_NewNodeID);
        l_array.Add('cate_link', a_NewNodeLink);
        l_array.Add('cate_target',a_NewNodeTarget);
        l_array.Add('cate_data',"");
        l_array.Add('cate_para',a_ParaID);

        sReturn=oTree.Add(a_NewNodeID,a_ParaID,l_array);

        if (a_ParaID == null)  //新建根目录
        {
                oDiv = document.all.item("divRender");
                paraTable = oDiv.firstChild;
        }
        else	//新建子目录
        {
                //先在外面的大Table增加一个<TR>
                paraTable=document.all.item(a_ParaID+"_childrentable");
                paraTable.style.display = "block";
        }
        oTr=paraTable.insertRow();
        oTr.id = a_NewNodeID + "_tr";

        //得到当前节点

        oNode = oTree.Find(a_NewNodeID);
        //在当前节点下增加一个子节点，默认为newModule
        GetTD1(oNode,oTr);

        SetDBFocus(a_NewNodeID + "_Value");
        paraTable.style.display="";

        oTree.AdjustPic(a_ParaID);
}

function ModifyNode(as_NodeID,as_NodeDesc)
{
        Temp = as_NodeID + "_Value";
        oNode = document.all.item(Temp);
        oNode.innerHTML = as_NodeDesc;
}

//如果新增或者删除发生错误则显示错误
function MsgError(a_Error)
{
        alert('错误'+ a_Error);
}

//刷新页面
function RefreshPage()
{
        //ShowTree();
        document.all.item('divRender').innerHTML = oTree.RenderTable(0);
        //展开树的第一级

        oNode = oTree.Find('0');
        if (oNode!=null){
                oNode.HideTable();
        }
        StrLastNode="";
}

function GetTD1(oNode,oTr)
{
        var l_name,l_node,l_return = "";
        var oTD,l_HTML;

        //虚线竖线
        l_node = oNode.paraNode;
        if ( l_node != null )
        {
                //插入一个TD
                oTD = oTr.insertCell();
                oTD.width = "18px";
                if ( document.all.item(oNode.nodeName + "_vline") != null )
                {
                        alert(document.all.item(oNode.nodeName + "_vline").outerHTML);
                }
                oTD.id = oNode.nodeName + "_vline";
        }

        oTD=oTr.insertCell();

        oTD.width="4px";
        oTD.innerHTML="&nbsp;"

        oTD=oTr.insertCell();

        l_HTML = GetTD3(oNode);
        oTD.innerHTML=l_HTML;
}

function GetTD3(oNode)
{
        var l_name,l_node,l_return="";

        l_return += "<table border=0 cellspacing=0 cellpadding=0>";
        l_return += "<tr >";

        l_return += "<td style=\"width:0%\">";
        l_return += "<span  id='" + oNode.nodeName + "_expand'";

        l_return += " onclick=\"javascript:oTree.Find('" + oNode.nodeName + "').HideTable();\"";

        //加减号图片
        if ( oNode.children.count > 0 ) //非叶子结点
        {
                l_return += " style=\"cursor:hand;width:18px\">";
        }else{ //叶子结点
                l_return += " style=\"width:0%\">";
        }
        l_return += oNode.ExpandImage(0);
        l_return += "</span>";
        l_return += "</td >";

        l_return += "<td style=\"width:0%\">";

        //文件夹图片
        l_return += "</span><span id='" + oNode.nodeName + "_folder'>";
	if(this.array != null && this.array.Find('CloseImage') != null){
	        l_return += "<img src=\"" + imagePath + this.array.Find('CloseImage') + "\" border=\"0\">";
	}else{
	        l_return += StrCloseImage2;
	}
        l_return += "</span>";

        l_return += "</td>";
        l_return += "<td align=left>";

        //分类的描述
        l_return += "<div nodeName=\"" + oNode.nodeName + "\"";

        l_return += " onmousedownex=\"javascript:oTree.Find('" + oNode.nodeName + "').onmousedownex();\"";
//	l_return += " onmouseout=\"javascript:oTree.Find('" + oNode.nodeName + "').onmouseoutex();\"";
//	l_return += " onmouseover=\"javascript:oTree.Find('" + oNode.nodeName + "').onmouseoverex();\"";

        l_return += " onmouseupex=\"javascript:oTree.Find('" + oNode.nodeName + "').onmouseupex();\"";
        l_return += " id='" + oNode.nodeName + "_Move'";
        l_return += " class=moveme style=\"position:relative;\">";
        l_return += "<div id='" + oNode.nodeName + "_Value' type=text readonly style=\"word-break:keep-all;height:13px;cursor:hand;border:0\"";
        l_return += " id='" + oNode.nodeName + "_desc'";

        l_return += " onclick=\"javascript:oTree.Find('" + oNode.nodeName + "').OnClick();this.blur();\"";
        l_return += " oncontextmenu=\"javascript:oTree.Find('" + oNode.nodeName + "').OnContextMenu()\"";
        l_return += " class=\"node\"";
        l_return += " >";
        l_return += oNode.array.Find('cate_desc');
        l_return += "</div>";
        l_return += "</div>";

        l_return += "</td>";

        l_return += "</tr>";

        //子分类的table
        l_return += "<tr>";

        l_return += "<td colspan=3>";
        l_return += "<table border=0 cellspacing=0 cellpadding=0 width=\"100%\"";
        if ( oNode.tree.ShowOnRender == 0 )
                l_return += " style=\"display:none\"";
        l_return += " id='" + oNode.nodeName + "_childrentable'>\n";

        for ( l_name in oNode.children.nodes )
        {
                l_node = oNode.children.nodes[l_name];
                l_return += l_node.RenderTable();
        }
        l_return += "</table>\n";
        l_return += "</td>";

        l_return += "</tr>";
        l_return += "</table>";

        return l_return;
}



//==这里结束 为了实现树形显示，对树模型对象进行适当的扩充的代码
        function Layout()
        {
        var l_moduID;

        if (StrLastNode=="" || StrLastNode=="0_Value")
        {
                alert("请先选择具体的模块");
                return;
        }
        l_moduID = StrLastNode.substr(0,StrLastNode.length-6);

        window.main.location="DragAdmin.asp?modu_id=" + l_moduID;
        }

        //得到对应的功能列表
        function GetFuncList()
        {
        var l_moduID,l_nodeName,oNode;

        if (StrLastNode=="" || StrLastNode=="0_Value") { alert("请先选择具体的模块");return;}
        l_moduID = StrLastNode.substr(0,StrLastNode.length-6);
        //0 : 修改 1 :新增
        window.main.location="funcList.asp?modu_id=" + l_moduID ;
	}
