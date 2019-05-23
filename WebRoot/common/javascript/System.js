
var theNodes = new Array();
var yjz = new Array();
var zIndex =1;

function actDiv(o)
{
	
	o.style.zIndex =++zIndex;
}

   

//获取对象
//方法一:getObject(name)---name 为id 或者jsid
function  getObject(name)
{
	var argv = getObject.arguments;  
	var argc = getObject.arguments.length; 
	if(argc==1)
	{
	return findObject(name);
	}
	else
	{
		return getObjectByAtt(name,argv[1]);
	}
	
}



function findObject(name)
{
	if(typeof(name) == "object")
	{
		return name;
	}
	else
	{
			if ( document.getElementById(name) != null )
		{
			return document.getElementById(name);
		}
		
		for( var i=0; i<document.forms.length; i++ ) 
		{
			var theForm = document.forms[i];
			if ( theForm[name] != null ) 
			{
				return theForm[name];
			}
		}
		
		if(GetFirstElementByAttributeValue("jsid",name)!= null)
		{
			return GetFirstElementByAttributeValue("jsid",name);
		}
		
		if(GetFirstElementByAttributeValue("myid",name)!= null)
		{
			return GetFirstElementByAttributeValue("myid",name);
		}
		return null;
		//return window.document.getElementById(id);
	}
}

function findHasAtt(po,attName)
{
	return getElementsByAttribute(po,attName).splice(0,theNodes.length);
}


function getObjectByAtt(n,v)
{
	return GetElementsByAttributeValue(n,v).splice(0,theNodes.length);

}

/**
*get the child of the parent,in spite of how many levels the childs are
*@param              p:                the parent node
*@param              cid:              the value of the child's attribute(cattName)
*@param              cattName          the attribute of the children
*for example:       <tr><td><input type="text" id="a" name="a" value="" onchange="getChild(this.parentElement.parentElement,"b","id")"></td>
*                       <td><input type="text" id="b" name="b" value=""></td>
*                    </tr>
*        getChild(this.parentElement.parentElement,"b","id")  
*        return the object that stand for '<input type="text" id="b" name="b" value="">'                  
*/
function getChild(p,cid,cattName)
{
	if(cattName)
	{
		return getElementsByAttributeValue(p,cattName,cid).splice(0,theNodes.length);
	}
	else
	{
		return getElementsByAttributeValue(p,"id",cid).splice(0,theNodes.length);
	}
}

function Body()
{
	return window.document.getElementsByTagName("BODY")[0];
}

///

function GetFirstElementByAttributeValue(attName,attVal)
		{
			return	getFirstElementByAttributeValue(Body(),attName,attVal);
		}
		
		//
function GetLastElementByAttributeValue(attName,attVal)
		{
			return getLastElementByAttributeValue(Body(),attName,attVal);
		}
		
		///
		
		function GetElementsByAttributeValue(attName,attVal)
		{
			return getElementsByAttributeValue(Body(),attName,attVal);
		}
		
		function GetElementsByAttribute(attName)
		{
			return getElementsByAttribute(Body(),attName);
		}
		
		///
					
function getFirstElementByAttributeValue(n,attName,attVal)
		{
			var theNode =null;			
			if(n.nodeType==1)
			{
				if(n.getAttribute(attName)==attVal)
				{
					return n;
				}
				if(n.hasChildNodes())
				{
					for(var c=n.firstChild;c!=null;c = c.nextSibling)
					{
						theNode =getFirstElementByAttributeValue(c,attName,attVal);
						if(theNode !=null)return theNode;
						
						
					}
					
					
				}
				
			}
			else
			{
				return null;
			}
			
			
		}
		
		
		///
		
		function getLastElementByAttributeValue(n,attName,attVal)
		{
			var theNode = null;
			var temtheNode;
				 temtheNode =getElementByAttributeValue(n,attName,attVal);
				if(temtheNode!=null)theNode =temtheNode;
				
				if(n.hasChildNodes())
				{
					for(var c=n.firstChild;c!=null;c = c.nextSibling)
					{
						 temtheNode =getLastElementByAttributeValue(c,attName,attVal);
						if(temtheNode!=null)theNode =temtheNode;
						
						
					}
					
				}
			
			
			
				return theNode;
			
			
		}
		
		///
		
		function getElementByAttributeValue(n,attName,attVal)
		{
			
			if(n.nodeType==1)
			{
				if(n.getAttribute(attName)==attVal)
				{
					return n;
				}
				else
				{
					return null;
				}
				
			}
			else
			{
				return null;
			}
			
			
		}
		
		///
		
		function getElementByAttribute(n,attName)
		{
			if(n.nodeType == null)return null;		
			if(n.nodeType==1)
			{
				if(n.getAttribute(attName))
				{
					return n;
				}
				else
				{
					return null;
				}
				
			}
			else
			{
				return null;
			}
			
			
		}
		
		function getElementsByAttribute(n,attName)
		{
			var theNode = null;
			
				theNode =getElementByAttribute(n,attName);
				if(theNode !=null&&theNode.nodeType==1)theNodes[theNodes.length] =theNode;
				//alert(theNodes.length);
				if(n.hasChildNodes())
				{
					for(var c=n.firstChild;c!=null;c = c.nextSibling)
					{
						var child =getElementsByAttribute(c,attName);
						if(child.length ==1)theNodes.concat(child);
						
					}
					
				}
			
			
			
				return theNodes;
			
			
		}
		
		////	
		
		
		
		function getElementsByAttributeValue(n,attName,attVal)
		{
			var theNode = null;
			
				theNode =getElementByAttributeValue(n,attName,attVal);
				if(theNode !=null&&theNode.nodeType==1)theNodes[theNodes.length] =theNode;
				//alert(theNodes.length);
				if(n.hasChildNodes())
				{
					for(var c=n.firstChild;c!=null;c = c.nextSibling)
					{
						var child =getElementsByAttributeValue(c,attName,attVal);
						if(child.length ==1)theNodes.concat(child);
						
					}
					
				}
			
			
			
				return theNodes;
			
			
		}
		
		
		
		function setGroupAtt(attName,attValue,att,value)
		{
			var groups =getObject(attName,attValue);
			
			for(i=0;i<groups.length;i++)
			{
				groups[i].setAttribute(att,value);
			}
		}
		
		function setGroupEvent(attName,attValue,eventName,method)
		{
			var groups =getObject(attName,attValue);
			
			for(i=0;i<groups.length;i++)
			{
				toEvent(groups[i],eventName,method);
				//groups[i].setAttribute(att,value);
			}
		}
		
		
		function getContainingParaNode(oNode,name)
		{
			///取父结点
			var testNode;
			testNode = oNode.parentNode;
			while(testNode)
			{
				if(testNode.nodeName ==name)
				{
					return testNode;
				}
				testNode = testNode.parentNode;
			}
			return null;
		}
		
		/**
		*get the parent node 
		*@param                  oNode                   the child node
		*@param                  name                    just like 'TR','TD','TABLE','DIV' and so on
		*/
		function getP(oNode,name)
		{
			if(oNode.nodeName ==name)
				{
					return oNode;
				}
				else
				{
					return getContainingParaNode(oNode,name);
					}
		}
		
		
		
		function getPhasAtt(oNode,name)
		{
			//alert(name);
			var testNode;
			testNode = oNode;
			while(testNode)
			{
				try
				{
					if(testNode.getAttributeNode(name)!=null)
					{
						return testNode;
					}
					testNode = testNode.parentNode;
				}
				catch(e)
				{
					return null;
					break;
				}
			}
			return null;
		
			
		}
		
		function getPhasAttV(oNode,name,v)
		{
			//alert(name);
			var testNode;
			testNode = oNode;
			while(testNode)
			{
				try
				{
					if(testNode.getAttributeNode(name)!=null)
					{
						if(testNode.getAttribute(name)==v)
						{
							return testNode;
						}
						else
						{
							testNode = testNode.parentNode;
						}
						
					}
					testNode = testNode.parentNode;
				}
				catch(e)
				{
					return null;
					break;
				}
			}
			return null;
		
			
		}
		
		
		
function getData(mainObj,type)
		{
			data =getChild(mainObj,"1","in");	
			var ps = new Array();
				var len = data.length;
			for(i=0;i<len;i++)
			{
				ps[i]=data[i].data;
				if(type)
				{
					//alert(data[i].innerText);
					if(type==1)
					{
						ps[data[i].data]=data[i].innerHTML;
					}
					else
					{
						ps[data[i].data]=data[i].innerText;
					}
				}
				else
				{
				ps[data[i].data]=data[i].value;
				}
			}
			return ps;
		}
		
		
		
	
	function request(name)
{
	var url = window.location.href.toLowerCase();
 if(url.indexOf("?")>-1)
 {
  var urls = url.split("?");
  var params = urls[1].split("&");
  var exit = false;
  for(i=0;i<params.length;i++)
   {
    if(params[i].indexOf(name.toLowerCase()+"=")>-1)
    {
     return params[i].split("=")[1];
     break;
    }
   }
}
return "";
}
	
function setValue(o,str,type)
{
	if(type==3)
	{
		o.innerText = str;
	}
	else
	{	try
		{
		o.innerHTML =str;
		}
		catch(e)
		{
			o.value =str;
		}
	}
}
		
	function Trim(text) 
	{
		return(text.toString().replace(/^\s*|\s*$/g, ""));
	}