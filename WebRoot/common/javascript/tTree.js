//--增强数组类----------------------------------------------------
function tArray() //构造增强数组类
{
	this.nodes = new Array();
	this.count = 0;
	this.Find = tArrayFindNode;
	this.Add = tArrayAddNode;
	this.AddA = tArrayAddNodeA;
	this.Remove = tArrayRemoveNode;
	this.Update = tArrayUpdateNode;
	this.Clone = tArrayCloneNode;
	this.insertBefore = tArrayInsertBefore;
	this.toString = tArraytoString;
}

function tArraytoString()
{
	var l_node,l_node2,l_return = '';
	for ( l_node in this.nodes )
	{
		l_return += "\n==tArraytoString==this.nodes['" + l_node + "']=";
		l_node2 = this.nodes[l_node];
		if ( l_node2.toString != null )
		{
			l_return += l_node2.toString();
		}else{
			l_return += "null";
		}
	}
	return l_return;
}

function tArrayFindNode(a_NodeName) //根据代码找一个结点
{
	if ( this.nodes == null )
		return null;
	return this.nodes[a_NodeName];
}

/*
function tArrayAddNodeA(a_NodeName,a_NodeValue)
功能：简单增加一个数组元素
输入：
	a_NodeName：元素代码
	a_NodeValue：元素值
输出：
	失败：
		null：存在同名结点
	成功：返回数组元素的总数
*/
function tArrayAddNodeA(a_NodeName,a_NodeValue)
{
	var l_node = this.Find(a_NodeName);
	if (l_node != null) //已经存在同名结点
		return null;
	this.nodes[a_NodeName] = a_NodeValue;
	this.count++;
	return this.count;
}

/*
function tArrayAddNode(a_NodeName,a_NodeValue)
功能：增加一个数组元素的增强版本
	如果输入只有一个参数，则认为输入的是一个数组，把该数组的元素成批加入本数组中
输入：
	a_NodeName：元素代码
	a_NodeValue：元素值
输出：
	失败：
		null：存在同名结点
		-2：输入只有一个参数，但不是Array或tArray类型
		-3：取得输入数组指针出错
		-4：输入参数个数不是1或2
	成功：返回数组元素的总数
*/
function tArrayAddNode(a_NodeName,a_NodeValue)
{
	var l_node, l_nodes;

	switch(arguments.length)
	{
	case 1: //只有一个参数
		if ( arguments[0].constructor != tArray && arguments[0].constructor != Array )
			return -2;
		l_nodes = (arguments[0].constructor == tArray) ? arguments[0].nodes : l_nodes = arguments[0];
		if ( l_nodes == null )
			return -3;
		for ( l_node in l_nodes )
		{
			this.AddA(l_node,l_nodes[l_node]);
		}
		break;
	case 2: //有两个参数
		return this.AddA(a_NodeName,a_NodeValue);
		break;
	default:
		return -4;
	}

	return this.count;
}

function tArrayInsertBefore(a_NodeName1,a_NodeName2,a_node)
{
	var l_node, l_array, o, o2;
	o = this.Find(a_NodeName1);
	o2 = this.Find(a_NodeName2);
	if ( o == null || o2 != null || a_NodeName2 == null || a_node == null)
		return null;
	l_array= new Array();
	for ( l_node in this.nodes )
	{
		if ( l_node == a_NodeName1 )
		{
			l_array[a_NodeName2] = a_node;
			this.count++;
		}
		l_array[l_node] = this.nodes[l_node];
	}
	this.nodes = l_array;
	return this.count;
}

function tArrayCloneNode()
{
	var l_node, l_array;
	l_array= new Array();
	for ( l_node in this.nodes )
	{
		l_array[l_node] = this.nodes[l_node];
	}
	return l_array;
}

function tArrayRemoveNode(a_NodeName)
{
	var l_node = this.Find(a_NodeName),l_node1;
	var l_array;

	l_array= new Array();
	if (l_node == null) //找不到要删除的结点
		return null;
	//----------------hupw modify-----------
	for ( l_node1 in this.nodes )
	{
		if (l_node1!=a_NodeName)
		{l_array[l_node1] = this.nodes[l_node1];}
	}
	this.nodes = l_array;
	//--------------------------------------
	//this.nodes[a_NodeName] = null;
	this.count--;
	return this.count;
}

function tArrayUpdateNode(a_NodeName,a_NodeValue)
{
	var l_node = this.Find(a_NodeName);
	if (l_node == null) //找不到要更改的结点
		return false;
	this.nodes[a_NodeName] = a_NodeValue;
	return true;
}


//--树结点类-------------------------------------------------------------

function tNodeBrotherCount()
{
	var l_node,l_ary,i=0;
	if ( this.paraNode == null )
	{
		l_ary = this.tree.nodes.nodes;
		for ( l_node in l_ary )
		{
			if ( l_ary[l_node].paraNode != null )
				continue;
			i++;
		}
		return i;
	}else{
		return this.paraNode.children.count;
	}
}

function tNodeIndex()
{
	var l_node,l_ary,i=0,s='';
	if ( this.paraNode == null )
	{
		l_ary = this.tree.nodes.nodes;
		for ( l_node in l_ary )
		{
			if ( l_ary[l_node].paraNode != null )
				continue;
			if ( l_node == this.nodeName )
			{
				return i;
			}
			i++;
		}
		return -1;
	}else{
		l_ary = this.paraNode.children.nodes;
		for ( l_node in l_ary )
		{
			s += l_node + ':';
			if ( l_node == this.nodeName )
			{
				return i;
//				s+= i;
//				return s;
			}
			i++;
		}
		return -1;
	}
}

function tNodeLayer()
{
	if ( this.paraNode == null )
		return 0;
	else
		return ( this.paraNode.Layer() + 1 );
}

function tNodeisLeaf()
{
	return ( this.children.count == 0 );
}

function tNodetoString()
{
	var l_return = '';
	l_return += "\n==tNodetoString==";
	l_return += "\nnodeName=" + this.nodeName;
	l_return += "\nparaName=" + this.paraName;
	l_return += "\nLayer=" + this.Layer();
	l_return += "\nIndex=" + this.Index();
	l_return += "\nBrotherCount=" + this.BrotherCount();
	l_return += "\nchildren.count=" + this.children.count;
	return l_return;
}

function tNodeUpdate(a_ParaName,a_ParaNode,a_tAry)
{
	this.paraName = a_ParaName;
	this.paraNode = a_ParaNode;
	this.array = a_tAry;
}


//--树类-------------------------------------------------------------

function tTreetoString()
{
	return this.nodes.toString();
}

function tTreeCount()
{
	return this.nodes.count;
}

function tFindNode(a_NodeName) //根据代码找一个结点
{
	if ( this.nodes == null )
		return null;
	return this.nodes.Find(a_NodeName);
}

function tAddNode(a_NodeName,a_ParaName,a_tAry)
{
	var l_node = null,l_node2;
/*
这个判断逻辑在tArray类的Add方法中中已经有了
	l_node = this.Find(a_NodeName);
	if ( l_node != null ) //已经存在同名结点
		return null;
*/
	if ( a_ParaName != null )
	{
		l_node = this.Find(a_ParaName);
		if ( l_node == null && (a_ParaName == "" || a_ParaName=="null")) //找不到父结点
			a_ParaName = null; //找不到父节点，把节点变成跟节点
    else if(l_node == null) //父节点值不为空或null
			return -1;
	}
	l_node2 = new tNode(a_NodeName,a_ParaName,l_node,a_tAry,this);

	//在父结点的儿子集合中添加本结点
	if ( l_node != null && l_node.children.Add(a_NodeName,l_node2) == null )
		return -2;

	return this.nodes.Add(a_NodeName,l_node2);
}

function tRemoveNode(a_NodeName,a_ifRemoveChildren)
{
	var l_node,l_node2;

	l_node = this.nodes.Find(a_NodeName); //l_node是tNode类

	if ( a_ifRemoveChildren == null )
	{
		//去除本结点的儿子集合中的所有节点
		for ( l_node2 in l_node.children.nodes ) //l_node.children是tArray类
		{
			this.Remove(l_node2);
		}
	}

	//在父结点的儿子集合中去除本结点
	if ( l_node.paraNode != null ) //l_node.paraNode是tNode类
		l_node.paraNode.children.Remove(a_NodeName);

	return this.nodes.Remove(a_NodeName);
}

function tUpdateNode(a_NodeName,a_ParaName,a_tAry)
{
	var l_node = null,l_node2;

	l_node2 = this.Find(a_NodeName); //l_node2是tNode类
	if ( l_node2 == null ) //找不到结点
		return -1;

	if ( a_ParaName != null)
	{
		l_node = this.Find(a_ParaName); //l_node是tNode类
		if ( l_node == null ) //找不到父结点
			return -2;

		if ( l_node2.children.Find(a_ParaName) != null ) //l_node2.children是tArray类
			return -3; //不能够将自己的子结点设置成为父结点
	}

	if ( l_node2.paraName != a_ParaName ) //父结点发生改变了
	{
		//从原来的父结点的儿子集合中去除本结点
		if ( l_node2.paraNode.children.Remove(a_ParaName) == null )
			return -4;

		//在新的父结点的儿子集合中添加本结点
		if ( l_node.children.Add(a_NodeName,l_node2) == null )
			return -5;
	}

	l_node2.Update(a_ParaName,l_node,a_tAry);

	return this.nodes.Update(a_NodeName,l_node2);
}
