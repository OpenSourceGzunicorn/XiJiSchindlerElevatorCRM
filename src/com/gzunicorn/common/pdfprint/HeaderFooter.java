package com.gzunicorn.common.pdfprint;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;

/**
 * 重写 HeaderFooter 的 onEndPage 方法
 * @author Lijun
 * 2016-08-23
 */
public class HeaderFooter extends PdfPageEventHelper{
	/**
	 * 页脚显示的内容
	 */
	private  String footerContent;
	/**
	 * 页脚显示的字体大小
	 */
	private  Font fontDetail;
	
	/**
	 * 重写 HeaderFooter 的 onEndPage 方法
	 * @param footerContent  页脚显示的内容
	 * @param fontDetail 页脚显示的字体大小
	 */
	public HeaderFooter(String footerContent,Font fontDetail){
		this.footerContent=footerContent;
		this.fontDetail=fontDetail;
    }
	
    public void onEndPage (PdfWriter writer, Document document) {

        //写入页眉
    	/**
        ColumnText.showTextAligned(writer.getDirectContent(), 
        		Element.ALIGN_CENTER, 
        		new Phrase(footerContent, fontDetail), 
        		document.left(), document.top() + 20, 0);
        */
    	
    	//写入页脚
        ColumnText.showTextAligned(writer.getDirectContent(), 
        		Element.ALIGN_CENTER, 
        		new Phrase(footerContent, fontDetail), 
        		(document.rightMargin()+document.right()+document.leftMargin()-document.left())/2.0F, 
        		document.bottom()-20, 0);
        
    }
    
}




