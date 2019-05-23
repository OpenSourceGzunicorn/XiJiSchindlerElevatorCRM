// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2005-8-10 11:33:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TableNavigator4Tag.java

package com.gzunicorn.common.taglib.htmltable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.zubarev.htmltable.taglib.TableNavigatorTagAbstract;


// Referenced classes of package com.zubarev.htmltable.taglib:
//            TableNavigatorTagAbstract

public class TableNavigator4Tag extends TableNavigatorTagAbstract
{

    public TableNavigator4Tag()
    {
    }

   /* protected StringBuffer drawBar(int from, int volume, int length)
    {
        StringBuffer sb = new StringBuffer();
            sb.append("<input type=\"text\" name=\"turnpage\" size=\"3\">");
            
            sb.append("<a href=\"" + getHref(5) + "\">Go</a>");
            sb.append(" \n");

        return sb;
    }*/
    
    /**
     * from = from record number;
     * volume = total record;
     * length = pagesize;
     */
    
    protected StringBuffer drawBar(int from, int volume, int length)
    throws JspException
{
    StringBuffer sb = new StringBuffer();
    javax.servlet.jsp.tagext.Tag tag = TagSupport.findAncestorWithClass(this, org.apache.struts.taglib.html.FormTag.class);
    if(tag == null)
        throw new JspException("TableNavigatorSubmitTag should be inside FormTag");
    if(volume == 0 || volume <=length)
    {
        
    } else
    {	sb.append("&nbsp;&nbsp;");
    	sb.append("<input type=\"text\" name=\"turnpage\" id=\"g\" size=\"2\" value=\""+((int)Math.ceil(from/length)+1)+"\">");
        sb.append("<a href=\"javascript:submitHTMLTableNavigator((serveTableForm.turnpage.value-1)*"+length+")\">GO</a>");
    }
    
    pageContext.setAttribute("com.zuvarev.htmltable.NavigatorSubmit", "com.zuvarev.htmltable.NavigatorSubmit");
    return sb;
}

public static final String NAVIGATOR_SUBMIT = "com.zuvarev.htmltable.NavigatorSubmit";
}