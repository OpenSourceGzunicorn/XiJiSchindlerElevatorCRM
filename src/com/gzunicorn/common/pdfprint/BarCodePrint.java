package com.gzunicorn.common.pdfprint;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.actions.DispatchAction;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.qualitycheckmanagement.QualityCheckManagement;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class BarCodePrint extends DispatchAction {

	/**
	 * 下拉框管理调用的打印二维码标签，测试使用
	 * @param response
	 * @param barCodeList
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws WriterException
	 */
	public ActionForward toPrintTwoRecord(HttpServletRequest request,HttpServletResponse response, List barCodeList)
		throws Exception {

		try {
			//纸张大小。 第一个参数为宽,第2个参数为高
			//Rectangle pageSize = new Rectangle(285.767441860465f, 208.372093023256f);
			//Rectangle pageSize = new Rectangle(285.767441860465f, 130.372093023256f);		
			
			//边距设置。 四位数字依次是:1-左边框距离,3-为上边距
			//Document document = new Document(pageSize, 5.953488372093f, 5.953488372093f, 5.953488372093f, 5.953488372093f);
			Document document = new Document();
			document.setPageSize(PageSize.A4);// 设置页面大小   
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();			
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			PdfWriter pdfwriter = PdfWriter.getInstance(document, baos);// 文件的输出路径+文件的实际名称
			document.open();// 打开文档
 
			Image png = null;
			int k = 0;

			// barCodeList是获取打印条码的信息
			if (barCodeList != null && barCodeList.size() > 0) {
				for (int i = 0; i < barCodeList.size(); i++) {
					Map barCodeMap = (Map) barCodeList.get(i);
					String pullid=(String)barCodeMap.get("pullid");
					String typeflag=(String)barCodeMap.get("typeflag");
					String pullname=(String)barCodeMap.get("pullname");
					String enabledflag=(String)barCodeMap.get("enabledflag");
					String pullrem=(String)barCodeMap.get("pullrem");
					
					//控制表格宽度大小	
					float[] headwidth = { 50.604651162791f, 98.232558139535f, 50.604651162791f, 83.348837209302f };			
					PdfPTable headtable = new PdfPTable(headwidth);					 
					headtable.setHorizontalAlignment(Element.ALIGN_CENTER);
					headtable.setWidthPercentage(100);
					headtable.setSplitLate(false);  
					
					//图形码
					PdfPCell ct_0 = new PdfPCell(new Paragraph("图形码", new Font(bfChinese, 11, Font.NORMAL)));
					ct_0.setHorizontalAlignment(Element.ALIGN_CENTER);
					ct_0.setVerticalAlignment(Element.ALIGN_CENTER);
					ct_0.setColspan(2);
					ct_0.setRowspan(5);
					
					//获取图片路径
//					String path = request.getSession().getServletContext().getRealPath("/");
//					path=path+"layout\\images\\xjs_2.jpg";
//					//System.out.println(">>>>>2="+path);
//					
//					png = Image.getInstance(path);
//					png.setWidthPercentage(100);//控制图形大小
//					png.setAlignment(Image.ALIGN_CENTER);
//					png.setAbsolutePosition(250, 30);
//					ct_0.addElement(png);
										
					//String pdfstr="代码:"+pullid+"; 名称:"+pullname+"; 类型:"+typeflag
					//			+"; 启用标志:"+enabledflag+"; 描述:"+pullrem;
					
					//String path = request.getContextPath();
					//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
					
					String pdfstr="http://192.168.4.149:8080/XJSCRM/DownLoadApkServlet";//本地系统
					//String pdfstr="http://10.10.0.5:8080/XJSCRM/DownLoadApkServlet";//测试系统
					//String pdfstr="http://www.xjelevator.com:9000/XJSCRM/DownLoadApkServlet";//正式系统
					
					System.out.println(">>>>>2="+pdfstr);
					
					pdfstr = new String(pdfstr.getBytes("UTF-8"), "ISO-8859-1"); //防止二维码出现中文乱码
					BitMatrix byteMatrix = new MultiFormatWriter().encode(pdfstr, BarcodeFormat.QR_CODE, 150, 140);
					
					BufferedImage image = MatrixToImageWriter.toBufferedImage(byteMatrix);
					png = Image.getInstance(image, null, true);
					png.setWidthPercentage(100);//控制图形大小
					png.setAlignment(Image.ALIGN_CENTER);
					png.setAbsolutePosition(250, 30);
					ct_0.addElement(png);
					
					//第一行
					PdfPCell c1_1 = new PdfPCell(new Paragraph("序号", new Font(bfChinese, 10, Font.BOLD)));
					c1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
					c1_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c1_1.setFixedHeight(17.8604651162793f);
					
					PdfPCell c1_2 = new PdfPCell(new Paragraph(pullid,new Font(bfChinese, 11, Font.NORMAL))); 
					c1_2.setHorizontalAlignment(Element.ALIGN_LEFT);
					c1_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c1_2.setFixedHeight(17.8604651162793f);
					
					//第二行
					PdfPCell c2_1 = new PdfPCell(new Paragraph("名称", new Font(bfChinese, 10, Font.BOLD)));
					c2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
					c2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c2_1.setFixedHeight(17.8604651162793f);

					PdfPCell c2_2 = new PdfPCell(new Paragraph(pullname, new Font(bfChinese, 11, Font.NORMAL)));  
					c2_2.setHorizontalAlignment(Element.ALIGN_LEFT);
					c2_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c2_2.setFixedHeight(17.8604651162793f);
					
					//第三行
					PdfPCell c3_1 = new PdfPCell(new Paragraph("类型", new Font(bfChinese, 10, Font.BOLD)));
					c3_1.setHorizontalAlignment(Element.ALIGN_LEFT);
					c3_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c3_1.setFixedHeight(17.8604651162793f);

					PdfPCell c3_2 = new PdfPCell(new Paragraph(typeflag,new Font(bfChinese, 11, Font.NORMAL)));  
					c3_2.setHorizontalAlignment(Element.ALIGN_LEFT);
					c3_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c3_2.setFixedHeight(17.8604651162793f);
					
					//第四行
					PdfPCell c4_1 = new PdfPCell(new Paragraph("启用标志", new Font(bfChinese, 10, Font.BOLD)));
					c4_1.setHorizontalAlignment(Element.ALIGN_LEFT);
					c4_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c4_1.setFixedHeight(17.8604651162793f);

					PdfPCell c4_2 = new PdfPCell(new Paragraph(enabledflag,new Font(bfChinese, 11, Font.NORMAL)));  
					c4_2.setHorizontalAlignment(Element.ALIGN_LEFT);
					c4_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c4_2.setFixedHeight(17.8604651162793f);
					
					//第五行
					PdfPCell c5_1 = new PdfPCell(new Paragraph("描述", new Font(bfChinese, 10, Font.BOLD)));
					c5_1.setHorizontalAlignment(Element.ALIGN_LEFT);
					c5_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c5_1.setFixedHeight(17.8604651162793f);

					PdfPCell c5_2 = new PdfPCell(new Paragraph(pullrem,new Font(bfChinese, 11, Font.NORMAL)));  
					c5_2.setHorizontalAlignment(Element.ALIGN_LEFT);
					c5_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c5_2.setFixedHeight(17.8604651162793f);
					
					//第六行
					PdfPCell c6_1 = new PdfPCell(new Paragraph("经办人", new Font(bfChinese, 10, Font.BOLD)));
					c6_1.setHorizontalAlignment(Element.ALIGN_LEFT);
					c6_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c6_1.setFixedHeight(17.8604651162793f);
					c6_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
					c6_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
					c6_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
					c6_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
					
					PdfPCell c6_2 = new PdfPCell(new Paragraph("批准", new Font(bfChinese, 10, Font.BOLD)));
					c6_2.setHorizontalAlignment(Element.ALIGN_LEFT);
					c6_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c6_2.setFixedHeight(17.8604651162793f);
					c6_2.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
					c6_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
					c6_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
					c6_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
					
					PdfPCell c6_3 = new PdfPCell(new Paragraph("", new Font(bfChinese, 10, Font.BOLD)));
					c6_3.setHorizontalAlignment(Element.ALIGN_LEFT);
					c6_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c6_3.setFixedHeight(17.8604651162793f);
					c6_3.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
					c6_3.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
					c6_3.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
					c6_3.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框


					headtable.addCell(c1_1);
					headtable.addCell(c1_2);
					headtable.addCell(ct_0);
											
					headtable.addCell(c2_1);
					headtable.addCell(c2_2);
					
					headtable.addCell(c3_1);
					headtable.addCell(c3_2);
					
					headtable.addCell(c4_1);
					headtable.addCell(c4_2);
					
					headtable.addCell(c5_1);
					headtable.addCell(c5_2);	
					
					headtable.addCell(c6_1);
					headtable.addCell(c6_3);	
					headtable.addCell(c6_2);
					headtable.addCell(c6_3);

					document.add(headtable);

					k++;
					document.newPage();

				}
			}
			document.close();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();
			out.close();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
	//发票出具通知单
	public ActionForward toPrintTwoRecord2(HttpServletResponse response, List barCodeList, List noticeList)
			throws Exception {

			try {
				//纸张大小。 第一个参数为宽,第2个参数为高
				//Rectangle pageSize = new Rectangle(285.767441860465f, 208.372093023256f);
				//Rectangle pageSize = new Rectangle(285.767441860465f, 130.372093023256f);		
				
				//边距设置。 四位数字依次是:1-左边框距离,3-为上边距
				//Document document = new Document(pageSize, 5.953488372093f, 5.953488372093f, 5.953488372093f, 5.953488372093f);
				Document document = new Document();
				document.setPageSize(PageSize.A4);// 设置页面大小 
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();			
				BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				PdfWriter pdfwriter = PdfWriter.getInstance(document, baos);// 文件的输出路径+文件的实际名称
				document.open();// 打开文档
	 
				Image png = null;
				int k = 0;
				
				int namefontsize=12;
				int valuefontsize=11;
				float fixedheight=16.8604651162793f;//设置 【通知单日期，开票金额】 空白行的高度
				float fixedheight2=30.8604651162793f;//设置【  经办人，批准 】行的高度
				
				// barCodeList是获取打印条码的信息
				if (barCodeList != null && barCodeList.size() > 0) {
					for (int i = 0; i < barCodeList.size(); i++) {
						Map barCodeMap = (Map) barCodeList.get(i);
						String depart=(String)barCodeMap.get("depart");//开票部门
						String invoiceDate=(String)barCodeMap.get("invoiceDate");//通知单日期
						String contractNo=(String)barCodeMap.get("contractNo");//合同号
						String invoiceType=(String)barCodeMap.get("invoiceType");//发票类型
						String invoiceName=(String)barCodeMap.get("invoiceName");//开票名称
						String rem=(String)barCodeMap.get("rem");//备注
						Integer num=(Integer) barCodeMap.get("num");//台数
						String contractSDate=(String)barCodeMap.get("contractSDate");//合同开始日期
						String contractEDate=(String)barCodeMap.get("contractEDate");//合同结束日期
						Double contractTotal=Double.valueOf(barCodeMap.get("contractTotal").toString());//合同总额
						String maintStation=(String) barCodeMap.get("maintStation");//维保站
						String manager=(String) barCodeMap.get("manager");//维保经理
						String maintScope=(String) barCodeMap.get("maintScope");//维保区间
						String recName=(String)barCodeMap.get("recName");//应收款名称
						
						String operId=(String)barCodeMap.get("operId");//经办人
						String auditOperid2=(String)barCodeMap.get("auditOperid2");//经办人
						String auditOperid4=(String)barCodeMap.get("auditOperid4");//经办人
						String fbzuser=(String)barCodeMap.get("fbzuser");//分部长
						String companyname=(String)barCodeMap.get("companyname");//甲方单位名称
						
						
						//控制表格宽度大小	
						//float[] headwidth = { 50.604651162791f, 98.232558139535f, 50.604651162791f, 83.348837209302f };	
						float[] headwidth = { 30.627907f,60.627907f,30.627907f,50.627907f,30.627907f,60.627907f};			
						PdfPTable headtable = new PdfPTable(headwidth);					 
						headtable.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable.setWidthPercentage(100);
						headtable.setSplitLate(false);  

						//表头
						PdfPCell ct_0 = new PdfPCell(new Paragraph("发票出具通知单", new Font(bfChinese, 18, Font.BOLD)));
						ct_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						ct_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ct_0.setColspan(6);
						ct_0.setFixedHeight(30.8604651162793f);
						ct_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						ct_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ct_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ct_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//第一行
						PdfPCell c1_1 = new PdfPCell(new Paragraph("开票部门", new Font(bfChinese, namefontsize, Font.BOLD)));
						c1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(12.8604651162793f);
						
						PdfPCell c1_2 = new PdfPCell(new Paragraph("财务部",new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c1_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c1_3 = new PdfPCell(new Paragraph("开票日期", new Font(bfChinese, namefontsize, Font.BOLD)));
						c1_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(12.8604651162793f);
						
						PdfPCell c1_4 = new PdfPCell(new Paragraph(invoiceDate,new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c1_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c1_5 = new PdfPCell(new Paragraph("合同号", new Font(bfChinese, namefontsize, Font.BOLD)));
						c1_5.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(12.8604651162793f);
						
						PdfPCell c1_6 = new PdfPCell(new Paragraph(contractNo,new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c1_6.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_2.setFixedHeight(12.8604651162793f);
						
						//第二行
						PdfPCell c2_1 = new PdfPCell(new Paragraph("台数", new Font(bfChinese, namefontsize, Font.BOLD)));
						c2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_1.setFixedHeight(12.8604651162793f);

						PdfPCell c2_2 = new PdfPCell(new Paragraph(String.valueOf(num), new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c2_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c2_3 = new PdfPCell(new Paragraph("发票类型", new Font(bfChinese, namefontsize, Font.BOLD)));
						c2_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_1.setFixedHeight(12.8604651162793f);

						PdfPCell c2_4 = new PdfPCell(new Paragraph(invoiceType, new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c2_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c2_5 = new PdfPCell(new Paragraph("合同期限", new Font(bfChinese, namefontsize, Font.BOLD)));
						c2_5.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_1.setFixedHeight(12.8604651162793f);

						PdfPCell c2_6 = new PdfPCell(new Paragraph(contractSDate+" 至 "+contractEDate, new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c2_6.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c7_1 = new PdfPCell(new Paragraph("维保区间", new Font(bfChinese, namefontsize, Font.BOLD)));
						c7_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c7_2 = new PdfPCell(new Paragraph(maintScope, new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c7_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c7_3 = new PdfPCell(new Paragraph("款项名称", new Font(bfChinese, namefontsize, Font.BOLD)));
						c7_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c7_4 = new PdfPCell(new Paragraph(recName, new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c7_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c7_5 = new PdfPCell(new Paragraph("维保站", new Font(bfChinese, namefontsize, Font.BOLD)));
						c7_5.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_5.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c7_6 = new PdfPCell(new Paragraph(maintStation, new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c7_6.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_6.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c8_1 = new PdfPCell(new Paragraph("维保经理", new Font(bfChinese, namefontsize, Font.BOLD)));
						c8_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c8_2 = new PdfPCell(new Paragraph(manager,new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c8_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c8_2.setColspan(5);
						
						PdfPCell c8_3 = new PdfPCell(new Paragraph("甲方单位", new Font(bfChinese, namefontsize, Font.BOLD)));
						c8_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c8_4 = new PdfPCell(new Paragraph(companyname,new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c8_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c8_4.setColspan(3);
						
						
						//第三行
						PdfPCell c3_1 = new PdfPCell(new Paragraph("开票名称", new Font(bfChinese, namefontsize, Font.BOLD)));
						c3_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//	c3_1.setFixedHeight(12.8604651162793f);
						c3_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c3_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c3_1.setBorderWidthBottom(1f);//边框宽度-底; 0f表示隐藏边框
						//c3_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c3_1.setRowspan(2);

						PdfPCell c3_2 = new PdfPCell(new Paragraph(invoiceName,new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c3_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_2.setFixedHeight(12.8604651162793f);
						c3_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c3_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c3_2.setBorderWidthBottom(1f);//边框宽度-底; 0f表示隐藏边框
						//c3_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c3_2.setColspan(2);
						c3_2.setRowspan(2);
						
						//第四行
						PdfPCell c4_1 = new PdfPCell(new Paragraph("合同总额", new Font(bfChinese, namefontsize, Font.BOLD)));
						c4_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						if(noticeList.size()>=5){
							c4_1.setRowspan(noticeList.size()-1);
						}else{
							c4_1.setRowspan(4);
						}

						PdfPCell c4_2 = new PdfPCell(new Paragraph(String.valueOf(contractTotal),new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c4_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c4_2.setColspan(2);
						if(noticeList.size()>=5){
							c4_2.setRowspan(noticeList.size()-1);
						}else{
							c4_2.setRowspan(4);
						}
						
						PdfPCell c4_t1 = new PdfPCell(new Paragraph("开票日期", new Font(bfChinese, namefontsize, Font.BOLD)));
						c4_t1.setHorizontalAlignment(Element.ALIGN_CENTER);
						c4_t1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_t1.setFixedHeight(12.8604651162793f);
						c4_t1.setColspan(2);
						
						PdfPCell c4_t2 = new PdfPCell(new Paragraph("开票金额", new Font(bfChinese, namefontsize, Font.BOLD)));
						c4_t2.setHorizontalAlignment(Element.ALIGN_CENTER);
						c4_t2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_t2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c4_t3 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c4_t3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_t3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c4_t3.setFixedHeight(fixedheight);
						c4_t3.setColspan(2);
						
						PdfPCell c4_t4 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c4_t4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_t4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c4_t4.setFixedHeight(fixedheight);
						
						//第五行
						PdfPCell c5_1 = new PdfPCell(new Paragraph("备注", new Font(bfChinese, namefontsize, Font.BOLD)));
						c5_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c5_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c5_1.setFixedHeight(12.8604651162793f);

						PdfPCell c5_2 = new PdfPCell(new Paragraph(rem,new Font(bfChinese, valuefontsize, Font.NORMAL)));  
						c5_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c5_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c5_2.setFixedHeight(12.8604651162793f);
						c5_2.setColspan(5);
						
						//第六行
						String c6str="经办人："+operId
								+"                批准人："+fbzuser
								+"                复核人："+auditOperid2
								+"                财务复核人："+auditOperid4;
						PdfPCell c6_1 = new PdfPCell(new Paragraph(c6str, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c6_1.setHorizontalAlignment(Element.ALIGN_CENTER);
						c6_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c6_1.setFixedHeight(fixedheight2);
						c6_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c6_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c6_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c6_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c6_1.setColspan(6);
						/**
						PdfPCell c6_2 = new PdfPCell(new Paragraph("批准人："+fbzuser, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c6_2.setHorizontalAlignment(Element.ALIGN_CENTER);
						c6_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c6_2.setFixedHeight(fixedheight2);
						c6_2.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c6_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c6_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c6_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c6_2.setColspan(2);
						
						PdfPCell c6_3 = new PdfPCell(new Paragraph("复核人："+auditOperid2, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c6_3.setHorizontalAlignment(Element.ALIGN_CENTER);
						c6_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c6_3.setFixedHeight(fixedheight2);
						c6_3.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c6_3.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c6_3.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c6_3.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c6_3.setColspan(2);
						
						PdfPCell c6_4 = new PdfPCell(new Paragraph("财务复核人："+auditOperid4, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c6_4.setHorizontalAlignment(Element.ALIGN_CENTER);
						c6_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c6_4.setFixedHeight(fixedheight2);
						c6_4.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c6_4.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c6_4.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c6_4.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c6_4.setColspan(2);
						*/
						
						headtable.addCell(ct_0);
					//	headtable.addCell(ct_1);
						
						headtable.addCell(c1_1);
						headtable.addCell(c1_2);
						headtable.addCell(c1_3);
						headtable.addCell(c1_4);
						headtable.addCell(c1_5);
						headtable.addCell(c1_6);
						
						headtable.addCell(c2_1);
						headtable.addCell(c2_2);
						headtable.addCell(c2_3);
						headtable.addCell(c2_4);
						headtable.addCell(c2_5);
						headtable.addCell(c2_6);
						
						headtable.addCell(c7_1);
						headtable.addCell(c7_2);
						headtable.addCell(c7_3);
						headtable.addCell(c7_4);
						headtable.addCell(c7_5);
						headtable.addCell(c7_6);
						
						headtable.addCell(c8_1);
						headtable.addCell(c8_2);
						headtable.addCell(c8_3);
						headtable.addCell(c8_4);
						
						headtable.addCell(c3_1);
						headtable.addCell(c3_2);
						
						headtable.addCell(c4_t1);
						headtable.addCell(c4_t2);
						if(noticeList!=null && noticeList.size()>0){
							for(int j=0;j<noticeList.size();j++){
								Map noticeMap=(Map) noticeList.get(j);
								String InvoiceDate=(String) noticeMap.get("InvoiceDate");
								Double InvoiceMoney=Double.valueOf( noticeMap.get("InvoiceMoney").toString());
								
								PdfPCell c4_d1 = new PdfPCell(new Paragraph(InvoiceDate,new Font(bfChinese, valuefontsize, Font.NORMAL)));  
								c4_d1.setHorizontalAlignment(Element.ALIGN_CENTER);
								c4_d1.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//	c4_d1.setFixedHeight(fixedheight);
								if(j==0){
									c4_d1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
									c4_d1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
									c4_d1.setBorderWidthBottom(1f);//边框宽度-底; 0f表示隐藏边框
									//c4_d1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
								}
								c4_d1.setColspan(2);
								
								PdfPCell c4_d2 = new PdfPCell(new Paragraph(String.valueOf(InvoiceMoney),new Font(bfChinese, valuefontsize, Font.NORMAL)));  
								c4_d2.setHorizontalAlignment(Element.ALIGN_CENTER);
								c4_d2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//	c4_d2.setFixedHeight(fixedheight);
								if(j==0){
									c4_d2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
									//c4_d2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
									c4_d2.setBorderWidthBottom(1f);//边框宽度-底; 0f表示隐藏边框
									//c4_d2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
								}
								
								headtable.addCell(c4_d1);
								headtable.addCell(c4_d2);
								if(j==0){
									headtable.addCell(c4_1);
									headtable.addCell(c4_2);
								}
									
							}
						}
						if(noticeList.size()<5){
							for(int j=0;j<5-noticeList.size();j++){
								headtable.addCell(c4_t3);
								headtable.addCell(c4_t4);
							}
						}
						
						headtable.addCell(c5_1);
						headtable.addCell(c5_2);	
						
						headtable.addCell(c6_1);	
						//headtable.addCell(c6_2);
						//headtable.addCell(c6_3);
						//headtable.addCell(c6_4);
						
						
						String invoiceNameStr=(String)barCodeMap.get("invoiceName");
						String taxId=(String)barCodeMap.get("taxId");
						String accountHolder=(String)barCodeMap.get("accountHolder");
						String bank=(String)barCodeMap.get("bank");
						String account=(String)barCodeMap.get("account");
						//第一行
						PdfPCell c10_1 = new PdfPCell(new Paragraph("开票名称", new Font(bfChinese, namefontsize, Font.BOLD)));
						c10_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c10_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(12.8604651162793f);
						PdfPCell c10_2 = new PdfPCell(new Paragraph(invoiceNameStr,new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c10_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c10_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c10_2.setColspan(5);
					//	c1_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c11_1 = new PdfPCell(new Paragraph("纳税人识别号", new Font(bfChinese, namefontsize, Font.BOLD)));
						c11_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c11_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(12.8604651162793f);
						PdfPCell c11_2 = new PdfPCell(new Paragraph(taxId,new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c11_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c11_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c11_2.setColspan(5);
					//	c1_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c12_1 = new PdfPCell(new Paragraph("地址、电话", new Font(bfChinese, namefontsize, Font.BOLD)));
						c12_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c12_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(12.8604651162793f);
						PdfPCell c12_2 = new PdfPCell(new Paragraph(accountHolder,new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c12_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c12_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c12_2.setColspan(5);
					//	c1_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c13_1 = new PdfPCell(new Paragraph("开户银行", new Font(bfChinese, namefontsize, Font.BOLD)));
						c13_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c13_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(12.8604651162793f);
						PdfPCell c13_2 = new PdfPCell(new Paragraph(bank,new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c13_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c13_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c13_2.setColspan(5);
					//	c1_2.setFixedHeight(12.8604651162793f);
						
						PdfPCell c14_1 = new PdfPCell(new Paragraph("银行帐号", new Font(bfChinese, namefontsize, Font.BOLD)));
						c14_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c14_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(12.8604651162793f);
						PdfPCell c14_2 = new PdfPCell(new Paragraph(account,new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c14_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c14_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c14_2.setColspan(5);
					//	c1_2.setFixedHeight(12.8604651162793f);
						
						headtable.addCell(c10_1);
						headtable.addCell(c10_2);
						headtable.addCell(c11_1);
						headtable.addCell(c11_2);	
						headtable.addCell(c12_1);
						headtable.addCell(c12_2);
						headtable.addCell(c13_1);
						headtable.addCell(c13_2);	
						headtable.addCell(c14_1);
						headtable.addCell(c14_2);

						document.add(headtable);

						k++;
						document.newPage();

					}
				}
				document.close();
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();

			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

			return null;
		}
	/**
	 * 付款通知单
	 * @param response
	 * @param barCodeList
	 * @param noticeList
	 * @return null
	 * @throws Exception
	 */
	public ActionForward toPrintTwoRecord3(HttpServletResponse response, List barCodeList)
			throws Exception {

			try {
				//纸张大小。 第一个参数为宽,第2个参数为高
				//Rectangle pageSize = new Rectangle(235.767441860465f, 208.372093023256f);
				//Rectangle pageSize = new Rectangle(285.767441860465f, 130.372093023256f);		
				
				//边距设置。 四位数字依次是:1-左边框距离,3-为上边距
				//Document document = new Document(pageSize, 5.953488372093f, 5.953488372093f, 5.953488372093f, 5.953488372093f);
				Document document = new Document();
				document.setPageSize(PageSize.A4);// 设置页面大小 
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();			
				BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				PdfWriter pdfwriter = PdfWriter.getInstance(document, baos);// 文件的输出路径+文件的实际名称
				document.open();// 打开文档
	 
				Image png = null;
				int k = 0;
				
				int namefontsize=13;
				int valuefontsize=12;
				
				// barCodeList是获取打印条码的信息
				if (barCodeList != null && barCodeList.size() > 0) {
					Map barCodeMap = (Map) barCodeList.get(0);
					String comfullname=(String) barCodeMap.get("comfullname");//所属分部
				    String entrustContractNo=(String) barCodeMap.get("entrustContractNo");//委托合同号
				    String companyName=(String) barCodeMap.get("companyName");//客户单位名称
				    String bank=(String) barCodeMap.get("bank");//客户开户银行
				    String account=(String) barCodeMap.get("account");//客户银行号码
				    double invoiceMoney=Double.valueOf(barCodeMap.get("invoiceMoney").toString());//发票金额
			        String invoiceMoney_CN=(String) barCodeMap.get("invoiceMoney_CN");//中文发票金额
			        String userName=(String) barCodeMap.get("userName");//当前登录人
			        String date =CommonUtil.getToday().replaceAll("-", "");
			        String y=date.substring(0, 4);
			        String m=date.substring(4, 6);
			        String d=date.substring(6, 8);
						
				        //控制表格宽度大小	
						float[] headwidth = { 50.604651162791f, 98.232558139535f, 50.604651162791f, 83.348837209302f };	
			            //float[] headwidth = { 30.604651162791f, 35.232558139535f, 30.604651162791f, 35.348837209302f };	
						
						PdfPTable headtable = new PdfPTable(headwidth);					 
						headtable.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable.setWidthPercentage(100);
						headtable.setSplitLate(false);  
						//表头
						PdfPCell ct_0 = new PdfPCell(new Paragraph("付款通知单", new Font(bfChinese, 20, Font.BOLD)));
						ct_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						ct_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ct_0.setColspan(6);
						ct_0.setFixedHeight(30.8604651162793f);
						ct_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						ct_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ct_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ct_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell ct_1 = new PdfPCell(new Paragraph(y+" 年 "+m+" 月 "+d+" 日", new Font(bfChinese, 10, Font.NORMAL)));
						ct_1.setHorizontalAlignment(Element.ALIGN_CENTER);
						ct_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ct_1.setColspan(6);
						ct_1.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						ct_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ct_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ct_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						
						//第一行
						PdfPCell c1_1 = new PdfPCell(new Paragraph("请款单位", new Font(bfChinese, namefontsize, Font.BOLD)));
						c1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c1_2 = new PdfPCell(new Paragraph(comfullname,new Font(bfChinese, valuefontsize, Font.NORMAL))); 
						c1_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_2.setFixedHeight(8.8604651162793f);
						
						PdfPCell c1_3 = new PdfPCell(new Paragraph("委托合同号", new Font(bfChinese, namefontsize, Font.BOLD)));
						c1_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_3.setFixedHeight(8.8604651162793f);
						
						PdfPCell c1_4 = new PdfPCell(new Paragraph(entrustContractNo, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c1_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_4.setFixedHeight(8.8604651162793f);
						
						//第二行
						PdfPCell c2_1 = new PdfPCell(new Paragraph("请款人", new Font(bfChinese, namefontsize, Font.BOLD)));
						c2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c2_2= new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c2_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c2_3 = new PdfPCell(new Paragraph("用途", new Font(bfChinese, namefontsize, Font.BOLD)));
						c2_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c2_4= new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c2_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_4.setFixedHeight(8.8604651162793f);
						
						//第三行
						PdfPCell c3_1 = new PdfPCell(new Paragraph("汇入地点", new Font(bfChinese, namefontsize, Font.BOLD)));
						c3_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c3_2= new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c3_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c3_3 = new PdfPCell(new Paragraph("汇入银行", new Font(bfChinese, namefontsize, Font.BOLD)));
						c3_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c3_4= new PdfPCell(new Paragraph(bank, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c3_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_4.setFixedHeight(8.8604651162793f);
						
						//第四行
						PdfPCell c4_1 = new PdfPCell(new Paragraph("银行账号", new Font(bfChinese, namefontsize, Font.BOLD)));
						c4_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c4_2= new PdfPCell(new Paragraph(account, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c4_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c4_3 = new PdfPCell(new Paragraph("支付方式", new Font(bfChinese, namefontsize, Font.BOLD)));
						c4_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c4_4= new PdfPCell(new Paragraph("□现金 □汇款", new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c4_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_4.setFixedHeight(8.8604651162793f);

						//第五行
						PdfPCell c5_1 = new PdfPCell(new Paragraph("收款单位", new Font(bfChinese, namefontsize, Font.BOLD)));
						c5_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c5_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c5_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c5_2= new PdfPCell(new Paragraph(companyName, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c5_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c5_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c5_2.setFixedHeight(8.8604651162793f);
						c5_2.setColspan(3);
						
						//第六行
						PdfPCell c6_1 = new PdfPCell(new Paragraph("付款金额(小写)", new Font(bfChinese, namefontsize, Font.BOLD)));
						c6_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c6_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c6_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c6_2= new PdfPCell(new Paragraph("￥："+invoiceMoney+" 元", new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c6_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c6_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c6_2.setFixedHeight(8.8604651162793f);
						c6_2.setColspan(3);
						
						//第七行
						PdfPCell c7_1 = new PdfPCell(new Paragraph("付款金额(大写)", new Font(bfChinese, namefontsize, Font.BOLD)));
						c7_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c7_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c7_2= new PdfPCell(new Paragraph("￥："+invoiceMoney_CN, new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c7_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c7_2.setFixedHeight(8.8604651162793f);
						c7_2.setColspan(3);
					
						//第八行
						PdfPCell c8 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize, Font.BOLD)));
						c8.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c8.setFixedHeight(12.8604651162793f);
						c8.setColspan(4);
						c8.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c8.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c8.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c8.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//第九行
						PdfPCell c9_1 = new PdfPCell(new Paragraph("拒付原因", new Font(bfChinese,namefontsize, Font.BOLD)));
						c9_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c9_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c9_1.setFixedHeight(22.8604651162793f);
						
						PdfPCell c9_2= new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize, Font.NORMAL)));
						c9_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c9_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c9_2.setFixedHeight(42.8604651162793f);
						c9_2.setColspan(3);
						
						//第十行
						PdfPCell c10_1 = new PdfPCell(new Paragraph("备注", new Font(bfChinese,namefontsize, Font.BOLD)));
						c10_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c10_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c10_1.setFixedHeight(22.8604651162793f);
						
						PdfPCell c10_2= new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize, Font.NORMAL)));
						c10_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c10_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c10_2.setFixedHeight(42.8604651162793f);
						c10_2.setColspan(3);
						
						//第十一行
						PdfPCell c11 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize, Font.BOLD)));
						c11.setHorizontalAlignment(Element.ALIGN_LEFT);
						c11.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c11.setFixedHeight(12.8604651162793f);
						c11.setColspan(4);
						c11.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c11.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c11.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c11.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//第十二行
						PdfPCell c12_1 = new PdfPCell(new Paragraph("经办人", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c12_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c12_1.setFixedHeight(9.8604651162793f);
						
						PdfPCell c12_2= new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize, Font.NORMAL)));
						c12_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c12_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c12_2.setFixedHeight(9.8604651162793f);
						
						PdfPCell c12_3 = new PdfPCell(new Paragraph("部门负责人", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c12_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c12_3.setFixedHeight(9.8604651162793f);
						
						PdfPCell c12_4= new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize, Font.NORMAL)));
						c12_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c12_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c12_4.setFixedHeight(9.8604651162793f);
						
						//第十三行
						PdfPCell c13_1 = new PdfPCell(new Paragraph("公司负责人", new Font(bfChinese,namefontsize, Font.BOLD)));
						c13_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c13_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c13_1.setFixedHeight(9.8604651162793f);
						
						PdfPCell c13_2= new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize, Font.NORMAL)));
						c13_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c13_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c13_2.setFixedHeight(9.8604651162793f);
						
						PdfPCell c13_3 = new PdfPCell(new Paragraph("复核", new Font(bfChinese,namefontsize, Font.BOLD)));
						c13_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c13_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c13_3.setFixedHeight(9.8604651162793f);
						
						PdfPCell c13_4= new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize, Font.NORMAL)));
						c13_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c13_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c13_4.setFixedHeight(9.8604651162793f);
						
						headtable.addCell(ct_0);
						headtable.addCell(ct_1);
						
						headtable.addCell(c1_1);
						headtable.addCell(c1_2);
						headtable.addCell(c1_3);
						headtable.addCell(c1_4);
						
						headtable.addCell(c2_1);
						headtable.addCell(c2_2);
						headtable.addCell(c2_3);
						headtable.addCell(c2_4);
						
						headtable.addCell(c3_1);
						headtable.addCell(c3_2);
						headtable.addCell(c3_3);
						headtable.addCell(c3_4);
						
						headtable.addCell(c4_1);
						headtable.addCell(c4_2);
						headtable.addCell(c4_3);
						headtable.addCell(c4_4);
						
						headtable.addCell(c5_1);
						headtable.addCell(c5_2);
						
						headtable.addCell(c6_1);
						headtable.addCell(c6_2);
						
						headtable.addCell(c7_1);
						headtable.addCell(c7_2);
						
						headtable.addCell(c8);
		
						headtable.addCell(c9_1);
						headtable.addCell(c9_2);
						
						headtable.addCell(c10_1);
						headtable.addCell(c10_2);

						headtable.addCell(c11);
						
						headtable.addCell(c12_1);
						headtable.addCell(c12_2);
						headtable.addCell(c12_3);
						headtable.addCell(c12_4);
						
						headtable.addCell(c13_1);
						headtable.addCell(c13_2);
						headtable.addCell(c13_3);
						headtable.addCell(c13_4);
						
						document.add(headtable);

						k++;
						document.newPage();

				}
				document.close();
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();

			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

	/**
	 * 整改通知单
	 * @param response
	 * @param barCodeList
	 * @param noticeList
	 * @return null
	 * @throws Exception
	 */
	public ActionForward toPrintTwoRecord4(HttpServletRequest request,HttpServletResponse response,
			List barCodeList,String billno,String isdownload)
			throws Exception {

			try {
				Document document = new Document();
				document.setPageSize(PageSize.A4);// 设置页面大小 
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();			
				BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				PdfWriter pdfwriter = PdfWriter.getInstance(document, baos);// 文件的输出路径+文件的实际名称
				
				//写入页脚
				String footerContent="厂检流水号："+billno;
				Font fontDetail = new Font(bfChinese, 10, Font.NORMAL);// 数据体字体
				HeaderFooter header=new HeaderFooter(footerContent,fontDetail);
				pdfwriter.setPageEvent(header);
				
		        document.open();// 打开文档
	            
		        Image png = null;
				int k = 0;
				
				int namefontsize=13;
				int valuefontsize=12;
				int namefontsize2=10;
				int valuefontsize2=9;

				// barCodeList是获取打印条码的信息
				if (barCodeList != null && barCodeList.size() > 0) {
					ElevatorTransferCaseRegister register = (ElevatorTransferCaseRegister) barCodeList.get(0);
					List hecirList = (List) barCodeList.get(1);
					List specialRegister=(List) barCodeList.get(2);
				        //控制表格宽度大小	
						float[] headwidth = { 50.604651162791f, 98.232558139535f, 50.604651162791f, 83.348837209302f };		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable = new PdfPTable(headwidth);					 
						headtable.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable.setWidthPercentage(100);
						headtable.setSplitLate(false);
						//headtable.setSplitRows(true); 
						//表头
						PdfPCell ct_0 = new PdfPCell(new Paragraph("电梯整改通知单", new Font(bfChinese, 20, Font.BOLD)));
						ct_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						ct_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ct_0.setColspan(4);
						ct_0.setFixedHeight(30.8604651162793f);
						ct_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						ct_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ct_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ct_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//第一行
						PdfPCell c1_1 = new PdfPCell(new Paragraph("实际厂检时间", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(20.8604651162793f);
						
						String CheckDate=register.getCheckDate();
						if(CheckDate==null){
							CheckDate="";
						}
						String CheckTime2=register.getCheckTime2();
						if(CheckTime2==null){
							CheckTime2="";
						}
						if(CheckDate.equals("")){
							CheckDate=register.getCheckTime();
						}
						PdfPCell c1_2 = new PdfPCell(new Paragraph(CheckDate+" "+CheckTime2,new Font(bfChinese, valuefontsize2, Font.NORMAL))); 
						c1_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_2.setFixedHeight(20.8604651162793f);
						
						PdfPCell c1_3 = new PdfPCell(new Paragraph("厂检次数", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c1_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_3.setFixedHeight(8.8604651162793f);
						
						PdfPCell c1_4 = new PdfPCell(new Paragraph(register.getCheckNum().toString(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c1_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_4.setFixedHeight(8.8604651162793f);
						
						//第二行
						PdfPCell c2_1 = new PdfPCell(new Paragraph("电梯编号", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c2_2= new PdfPCell(new Paragraph(register.getElevatorNo(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c2_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c2_3 = new PdfPCell(new Paragraph("项目名称", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c2_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c2_4= new PdfPCell(new Paragraph(register.getProjectName(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c2_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_4.setFixedHeight(8.8604651162793f);
						
						//第三行
						PdfPCell c3_1 = new PdfPCell(new Paragraph("层/站/门", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c3_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c3_2= new PdfPCell(new Paragraph(register.getR1(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_2.setFixedHeight(8.8604651162793f);
					
						String highname="";
						String highval="";
						if(register.getHigh()!=null && register.getHigh()>0){
							highname="提升高度";
							highval=register.getHigh().toString();
						}

						PdfPCell c3_3 = new PdfPCell(new Paragraph(highname, new Font(bfChinese, namefontsize2, Font.BOLD)));
						c3_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_3.setFixedHeight(8.8604651162793f);
						
						PdfPCell c3_4= new PdfPCell(new Paragraph(highval, new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_4.setFixedHeight(8.8604651162793f);
						
						//第四行
						PdfPCell c4_1 = new PdfPCell(new Paragraph("载重", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c4_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c4_2= new PdfPCell(new Paragraph(register.getWeight(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c4_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c4_3 = new PdfPCell(new Paragraph("额定数度", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c4_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c4_4= new PdfPCell(new Paragraph(register.getSpeed(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c4_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c4_4.setFixedHeight(8.8604651162793f);

						//第五行
						PdfPCell c5_1 = new PdfPCell(new Paragraph("安装公司名称", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c5_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c5_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c5_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c5_2= new PdfPCell(new Paragraph(register.getInsCompanyName(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c5_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c5_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c5_2.setFixedHeight(8.8604651162793f);
						
						//第六行
						PdfPCell c6_1 = new PdfPCell(new Paragraph("厂检员名称", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c6_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c6_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c6_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c6_2= new PdfPCell(new Paragraph(register.getStaffName(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c6_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c6_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c6_2.setFixedHeight(8.8604651162793f);
						
						//第七行
						PdfPCell c7_1 = new PdfPCell(new Paragraph("厂检结果", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c7_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c7_2= new PdfPCell(new Paragraph(register.getFactoryCheckResult(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c7_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_2.setFixedHeight(8.8604651162793f);
						//c7_2.setColspan(3);
						
						//第七行
						PdfPCell c7_3 = new PdfPCell(new Paragraph("厂检员电话", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c7_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c7_4= new PdfPCell(new Paragraph(register.getStaffLinkPhone(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c7_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_2.setFixedHeight(8.8604651162793f);
						
						//第七行
						PdfPCell c7_1_1 = new PdfPCell(new Paragraph("电梯位置", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c7_1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_1_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c7_2_1= new PdfPCell(new Paragraph(register.getElevatorAddress(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c7_2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_2.setFixedHeight(8.8604651162793f);
						c7_2_1.setColspan(3);
						
					
						//第八行
						PdfPCell c8 = new PdfPCell(new Paragraph("", new Font(bfChinese, namefontsize, Font.BOLD)));
						c8.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c8.setFixedHeight(12.8604651162793f);
						c8.setColspan(4);
						c8.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c8.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c8.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c8.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//第九行
						PdfPCell c9_1 = new PdfPCell(new Paragraph("特殊要求", new Font(bfChinese,namefontsize, Font.BOLD)));
						c9_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c9_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c9_1.setFixedHeight(9.8604651162793f);
						
                        String specialRegisterStr="";
						if(specialRegister!=null&&specialRegister.size()>0){
							for(int i = 0;i<specialRegister.size();i++){
								specialRegisterStr+=specialRegister.get(i)+"\n";
							}
						}
						PdfPCell c9_2= new PdfPCell(new Paragraph(specialRegisterStr, new Font(bfChinese,valuefontsize, Font.NORMAL)));
						c9_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c9_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c9_2.setColspan(3);
						
						//第十行
						PdfPCell c10_1 = new PdfPCell(new Paragraph("厂检有问题项", new Font(bfChinese,namefontsize, Font.BOLD)));
						c10_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c10_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c10_1.setFixedHeight(8.8604651162793f);
						c10_1.setColspan(5);
						
						//第十一行
						PdfPCell c11 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize, Font.NORMAL)));
						c11.setHorizontalAlignment(Element.ALIGN_LEFT);
						c11.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c11.setFixedHeight(12.8604651162793f);
						c11.setColspan(5);
						c11.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c11.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c11.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c11.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						
						//第十二行
						float[] headwidth3 = {18.604651162791f, 46.232558139535f, 108.604651162791f, 93.348837209302f, 34.348837209302f};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable3 = new PdfPTable(headwidth3);					 
						headtable3.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable3.setWidthPercentage(100);
						
						PdfPCell c12_0= new PdfPCell(new Paragraph("序号", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c12_3= new PdfPCell(new Paragraph("检查类型", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_3.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c12_1 = new PdfPCell(new Paragraph("检验要求", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_1.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c12_1.setFixedHeight(9.8604651162793f);
						//c12_1.setColspan(2);
						
						PdfPCell c12_2= new PdfPCell(new Paragraph("问题内容", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_2.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c12_2.setFixedHeight(9.8604651162793f);
						//c12_2.setColspan(2);
						
						PdfPCell c12_22= new PdfPCell(new Paragraph("是否整改", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_22.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_22.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c12_2.setFixedHeight(9.8604651162793f);
						//c12_2.setColspan(2);
						
						if(hecirList!=null&&hecirList.size()>0){
							headtable3.addCell(c10_1);
							headtable3.addCell(c12_0);
							headtable3.addCell(c12_3);
							headtable3.addCell(c12_1);
							headtable3.addCell(c12_2);
							headtable3.addCell(c12_22);
							
							String examTypeName="";
							int etnum=0;
							for(int i=0;i<hecirList.size();i++){
								HashMap map=(HashMap) hecirList.get(i);
								
								String examType=(String)map.get("examType");
								if(i==0){
									examTypeName=examType;
								}
								if(examTypeName.equals(examType)){
									etnum++;
								}else{
									etnum=1;
									examTypeName=examType;
								}
								
								PdfPCell c13_0 = new PdfPCell(new Paragraph(String.valueOf(etnum), new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_0.setHorizontalAlignment(Element.ALIGN_CENTER);
								c13_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_0.setColspan(2);
								//j++;
								
								
								PdfPCell c13_3 = new PdfPCell(new Paragraph(examType, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_3.setHorizontalAlignment(Element.ALIGN_LEFT);
								c13_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_3.setColspan(2);
								
								String issueContents1=(String) map.get("issueContents1");
								
								PdfPCell c13_1 = new PdfPCell(new Paragraph(issueContents1, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_1.setHorizontalAlignment(Element.ALIGN_LEFT);
								c13_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_1.setColspan(2);
								
								String issueContents=(String) map.get("issueContents");
							
								PdfPCell c13_2 = new PdfPCell(new Paragraph(issueContents, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_2.setHorizontalAlignment(Element.ALIGN_LEFT);
								c13_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_2.setColspan(2);
								
								String isyzg=(String) map.get("isyzg");
								PdfPCell c13_22 = new PdfPCell(new Paragraph(isyzg, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_22.setHorizontalAlignment(Element.ALIGN_CENTER);
								c13_22.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_2.setColspan(2);
								
								headtable3.addCell(c13_0);
								headtable3.addCell(c13_3);
								headtable3.addCell(c13_1);
								headtable3.addCell(c13_2);
								headtable3.addCell(c13_22);
							}
						}
						PdfPCell cd_0 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));

						cd_0.setColspan(4);
						cd_0.addElement(headtable3);
						cd_0.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						cd_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cd_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cd_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//获取图片路径
						String path = request.getSession().getServletContext().getRealPath("/");
						path=path+"layout\\images\\xjs_2.jpg";
						Image image= Image.getInstance(path);
						image.setWidthPercentage(75);//控制图形大小
						image.setAlignment(Image.ALIGN_LEFT);
						image.setAbsolutePosition(250, 30);
						
						PdfPCell cx_0 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));//
						cx_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						cx_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_0.setColspan(2);
						cx_0.setRowspan(3);
						//cx_0.setFixedHeight(30.8604651162793f);
						cx_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_0.setBorderWidthBottom(0.7f);//边框宽度-底; 0f表示隐藏边框
						cx_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						cx_0.addElement(image);
						
						PdfPCell cx_1 = new PdfPCell(new Paragraph("  PH：0374-3130588/3136373   Fax: 0374-8318800-6690", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_1.setColspan(2);
						cx_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cx_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell cx_2 = new PdfPCell(new Paragraph("  西继迅达（许昌）电梯有限公司", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_2.setColspan(2);
						cx_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cx_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell cx_3 = new PdfPCell(new Paragraph("  河南省许昌西经济技术开发区延安路南段2120号", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_3.setColspan(2);
						cx_3.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_3.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_3.setBorderWidthBottom(0.7f);//边框宽度-底; 0f表示隐藏边框
						cx_3.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						headtable.addCell(cx_0);
						headtable.addCell(cx_1);
						headtable.addCell(cx_2);
						headtable.addCell(cx_3);
						headtable.addCell(c8);
						
                        headtable.addCell(ct_0);
						
						headtable.addCell(c1_1);
						headtable.addCell(c1_2);
						headtable.addCell(c1_3);
						headtable.addCell(c1_4);
						
						headtable.addCell(c2_1);
						headtable.addCell(c2_2);
						headtable.addCell(c2_3);
						headtable.addCell(c2_4);
						
						headtable.addCell(c3_1);
						headtable.addCell(c3_2);
						headtable.addCell(c3_3);
						headtable.addCell(c3_4);
						
						headtable.addCell(c4_1);
						headtable.addCell(c4_2);
						headtable.addCell(c4_3);
						headtable.addCell(c4_4);
						
						headtable.addCell(c5_1);
						headtable.addCell(c5_2);
						
						headtable.addCell(c6_1);
						headtable.addCell(c6_2);
						
						headtable.addCell(c7_1);
						headtable.addCell(c7_2);
						headtable.addCell(c7_3);
						headtable.addCell(c7_4);
						
						headtable.addCell(c7_1_1);
						headtable.addCell(c7_2_1);
						
						//headtable.addCell(c8);
						//headtable.addCell(c9_1);//不显示特殊要求
						//headtable.addCell(c9_2);
						
						headtable.addCell(c11);
						
						headtable.addCell(cd_0);

						headtable.addCell(c8);
						headtable.addCell(c8);
						headtable.addCell(c8);
						headtable.addCell(c8);
						
						
						
						float[] headwidth1 = { 40, 15, 25, 20};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable1 = new PdfPTable(headwidth1);					 
						headtable1.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable1.setWidthPercentage(100);
						
						PdfPCell ch_0 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						ch_0.setHorizontalAlignment(Element.ALIGN_LEFT);
						ch_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ch_0.setFixedHeight(12);
						ch_0.setColspan(4);
						ch_0.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						ch_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ch_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ch_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell ch_1 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						ch_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						ch_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ch_1.setFixedHeight(12);
						ch_1.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						ch_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ch_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ch_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell ch_2 = new PdfPCell(new Paragraph("安装负责人(签字)：", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						ch_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						ch_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ch_2.setFixedHeight(14);
						ch_2.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						ch_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ch_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ch_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell ch_3 = new PdfPCell(new Paragraph("维保负责人(签字)：", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						ch_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						ch_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ch_3.setFixedHeight(14);
						ch_3.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						ch_3.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ch_3.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ch_3.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell ch_4 = new PdfPCell(new Paragraph(" 时间：", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						ch_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						ch_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ch_4.setFixedHeight(14);
						ch_4.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						ch_4.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ch_4.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ch_4.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						headtable1.addCell(ch_0);
						headtable1.addCell(ch_2);
						headtable1.addCell(ch_1);
						headtable1.addCell(ch_4);
						headtable1.addCell(ch_1);
						headtable1.addCell(ch_0);
						headtable1.addCell(ch_3);
						headtable1.addCell(ch_1);
						headtable1.addCell(ch_4);
						headtable1.addCell(ch_1);
						headtable1.addCell(ch_0);
						
						PdfPCell cd_1 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						cd_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						cd_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cd_1.setColspan(2);
						cd_1.addElement(headtable1);
						
						float[] headwidth2 = { 16, 70};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable2 = new PdfPTable(headwidth2);					 
						headtable2.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable2.setWidthPercentage(100);
						
						PdfPCell ch2_1 = new PdfPCell(new Paragraph("  整改结果(维保填写)", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						ch2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						ch2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						ch2_1.setFixedHeight(70);
						ch2_1.setBorderWidthBottom(0f);
						ch2_1.setBorderWidthLeft(0f);
						ch2_1.setBorderWidthTop(0f);
						ch2_1.setPadding(0f);
						
						PdfPCell ch2_2 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						ch2_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						ch2_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ch2_2.setBorderWidthBottom(0f);
						ch2_2.setBorderWidthLeft(0f);
						ch2_2.setBorderWidthTop(0f);
						ch2_2.setBorderWidthRight(0f);
						ch2_2.setPadding(0f);
						ch2_2.setPaddingTop(0f);
						ch2_2.setPaddingBottom(0f);
						
						headtable2.addCell(ch2_1);
						headtable2.addCell(ch2_2);
						
						PdfPCell cd_2 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						cd_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						cd_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cd_2.setColspan(2);
						cd_2.setTop(0f);
						cd_2.setBottom(0f);
						cd_2.addElement(headtable2);
						
						String rem="备注：安装班组应在出具整改通知单5日内单梯整改完毕，n台电梯给予（5+n）天的整改时间；未在规定时间内整改完毕应书面通知安装部具体原因；否则安装部将派人全部接管，从安装费中扣除整改相关费用，设备合同则代理公司缴纳相应的整改费用。";
						PdfPCell cd_3 = new PdfPCell(new Paragraph(rem, new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						cd_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						cd_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cd_3.setColspan(4);
						
						PdfPCell c10 = new PdfPCell(new Paragraph("", new Font(bfChinese, 11, Font.BOLD)));
						c10.setHorizontalAlignment(Element.ALIGN_LEFT);
						c10.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c10.setFixedHeight(12.8604651162793f);
						c10.setColspan(4);
						c10.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c10.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c10.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c10.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						/**=======================添加客户签名=========================*/
						float[] headwidth5 = { 12,70};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable5 = new PdfPTable(headwidth5);					 
						headtable5.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable5.setWidthPercentage(100);
						headtable5.setSplitLate(false);
						
						PdfPCell c18_1 = new PdfPCell(new Paragraph("客户签名：", new Font(bfChinese, 11, Font.BOLD)));
						c18_1.setHorizontalAlignment(Element.ALIGN_RIGHT);//Element.ALIGN_LEFT
						c18_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c18_1.setColspan(2);
						c18_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c18_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c18_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c18_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						headtable5.addCell(c18_1);
						
						if(register.getCustomerSignature()!=null && !register.getCustomerSignature().trim().equals("")){
							String folder2 = PropertiesUtil.getProperty("ElevatorTransferCaseRegister.file.upload.folder");
							String path2=folder2+register.getCustomerSignature();//上传的客户签名图片路径
							
							Image image2= Image.getInstance(path2);
							image2.setWidthPercentage(25);//控制图形大小
							image2.setAlignment(Image.ALIGN_LEFT);
							image2.setAbsolutePosition(250, 30);
							
							PdfPCell c18_2 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));
							c18_2.setHorizontalAlignment(Element.ALIGN_CENTER);
							c18_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//c18_2.setColspan(2);
							//c18_2.setRowspan(3);
							//cx_0.setFixedHeight(30.8604651162793f);
							c18_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
							c18_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
							c18_2.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
							c18_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
							c18_2.addElement(image2);
	
							headtable5.addCell(c18_2);
						}else{
							PdfPCell c18_2 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));
							c18_2.setHorizontalAlignment(Element.ALIGN_CENTER);
							c18_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//c18_2.setColspan(2);
							//c18_2.setRowspan(3);
							//cx_0.setFixedHeight(30.8604651162793f);
							c18_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
							c18_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
							c18_2.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
							c18_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
	
							headtable5.addCell(c18_2);
						}
						
						PdfPCell cd_55 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						cd_55.setHorizontalAlignment(Element.ALIGN_LEFT);
						cd_55.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cd_55.setColspan(4);
						cd_55.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cd_55.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cd_55.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
						cd_55.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						cd_55.addElement(headtable5);
						/**=======================添加客户签名=========================*/
						
						headtable.addCell(cd_1);
						headtable.addCell(cd_2);
						headtable.addCell(cd_3);
						
						//headtable.addCell(c10);
						headtable.addCell(c10);
						headtable.addCell(cd_55);
						
						document.add(headtable);

						k++;
						
						//document.newPage();
				}
 
				document.close();
				
				
				//网页预览
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				if("Y".equals(isdownload)){
					//下载PDF
					response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("厂检通知单_"+billno+".pdf", "utf-8"));
				}
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();
				
				/**
				//下载pdf
				response.setContentType("application/x-msdownload");
				response.setContentLength(baos.size());
				response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("厂检通知单_"+billno+".pdf", "utf-8"));
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();
				*/
				
				/**
				//保存pdf文件到本地
				String stodaytime=DateUtil.getNowTime("yyyyMMddHHmmss");
				String savepath="D:\\Download\\厂检通知单_"+billno+".pdf";
				FileOutputStream fos=new FileOutputStream(savepath);
				baos.writeTo(fos);
				fos.flush();
				fos.close();
				*/
				
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

			return null;
		}
	
	//电梯维修记录单
	public ActionForward toPrintTwoRecord5(HttpServletRequest request,HttpServletResponse response, List barCodeList,String billno)
			throws Exception {

			try {
				Document document = new Document();
				document.setPageSize(PageSize.A4);// 设置页面大小 
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();			
				BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				PdfWriter pdfwriter = PdfWriter.getInstance(document, baos);// 文件的输出路径+文件的实际名称
				//写入页脚
				String footerContent="报修热线：400-811-6869";
				Font fontDetail = new Font(bfChinese, 10, Font.NORMAL);// 数据体字体
				HeaderFooter header=new HeaderFooter(footerContent,fontDetail);
				pdfwriter.setPageEvent(header);
				
		        document.open();// 打开文档
	            
		        Image png = null;
				int k = 0;
				
				int namefontsize=13;
				int valuefontsize=12;
				int namefontsize2=10;
				int valuefontsize2=10;

				// barCodeList是获取打印条码的信息
				if (barCodeList != null && barCodeList.size() > 0) {
					CalloutMaster CM = (CalloutMaster) barCodeList.get(0);
					CalloutProcess CP = (CalloutProcess) barCodeList.get(1);
						float[] headwidth = { 50.604651162791f, 98.232558139535f, 50.604651162791f, 83.348837209302f };		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable = new PdfPTable(headwidth);					 
						headtable.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable.setWidthPercentage(100);
						headtable.setSplitLate(false);
						//headtable.setSplitRows(true); 
						//表头
						PdfPCell ct_0 = new PdfPCell(new Paragraph("电梯维修记录单", new Font(bfChinese, 20, Font.BOLD)));
						ct_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						ct_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ct_0.setColspan(4);
						ct_0.setFixedHeight(30.8604651162793f);
						ct_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						ct_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ct_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ct_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//维修单编号
						PdfPCell c0_1 = new PdfPCell(new Paragraph("维修单编号："+CM.getCalloutMasterNo(), new Font(bfChinese, namefontsize2, Font.BOLD)));
						c0_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c0_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c0_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c0_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c0_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c0_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c0_1.setColspan(4);
						
					/*	PdfPCell c0_2= new PdfPCell(new Paragraph(CM.getCalloutMasterNo(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c0_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c0_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c0_2.setColspan(3);
						c0_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c0_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c0_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c0_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
*/						
						//第一行
						PdfPCell c1_1 = new PdfPCell(new Paragraph("报修类型", new Font(bfChinese, 11, Font.BOLD)));
						c1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(20.8604651162793f);
						
						PdfPCell c1_2 = new PdfPCell(new Paragraph(CM.getRepairMode(),new Font(bfChinese, valuefontsize2, Font.NORMAL))); 
						c1_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_2.setFixedHeight(20.8604651162793f);
						
						PdfPCell c1_3 = new PdfPCell(new Paragraph("甲方名称", new Font(bfChinese, 11, Font.BOLD)));
						c1_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_3.setFixedHeight(8.8604651162793f);
						
						PdfPCell c1_4 = new PdfPCell(new Paragraph(CM.getCompanyId().toString(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c1_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_4.setFixedHeight(8.8604651162793f);
						
						//第二行
						PdfPCell c2_1 = new PdfPCell(new Paragraph("电梯编号", new Font(bfChinese, 11, Font.BOLD)));
						c2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c2_2= new PdfPCell(new Paragraph(CM.getElevatorNo(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c2_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c2_3 = new PdfPCell(new Paragraph("电梯位置", new Font(bfChinese, 11, Font.BOLD)));
						c2_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c2_4= new PdfPCell(new Paragraph(CM.getProjectAddress(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c2_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_4.setFixedHeight(8.8604651162793f);
						
						//第三行
						PdfPCell c3_1 = new PdfPCell(new Paragraph("报修人姓名", new Font(bfChinese, 11, Font.BOLD)));
						c3_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c3_2= new PdfPCell(new Paragraph(CM.getRepairUser(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c3_3 = new PdfPCell(new Paragraph("报修人电话", new Font(bfChinese, 11, Font.BOLD)));
						c3_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c3_4= new PdfPCell(new Paragraph(CM.getRepairTel().toString(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_4.setFixedHeight(8.8604651162793f);
						
						//第四行
						PdfPCell c4_1 = new PdfPCell(new Paragraph("报修时间", new Font(bfChinese, 11, Font.BOLD)));
						c4_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c4_2= new PdfPCell(new Paragraph(CM.getRepairTime(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c4_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c4_3 = new PdfPCell(new Paragraph("是否困人", new Font(bfChinese, 11, Font.BOLD)));
						c4_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c4_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c4_4= new PdfPCell(new Paragraph(CM.getIsTrap(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c4_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c4_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c4_4.setFixedHeight(8.8604651162793f);

						//第五行
						PdfPCell c5_1 = new PdfPCell(new Paragraph("报修内容", new Font(bfChinese, 11, Font.BOLD)));
						c5_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c5_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c5_1.setColspan(1);
						c5_1.setFixedHeight(40);
						//c7_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c5_2= new PdfPCell(new Paragraph(CM.getRepairDesc(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c5_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						/*c5_2.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
						//c7_2.setFixedHeight(8.8604651162793f);
						c5_2.setColspan(3);
						c5_2.setPadding(0f);
						c5_2.setPaddingTop(0f);
						c5_2.setPaddingBottom(0f);
						
						//第六行
						PdfPCell c6 = new PdfPCell(new Paragraph("", new Font(bfChinese, 11, Font.BOLD)));
						c6.setHorizontalAlignment(Element.ALIGN_LEFT);
						c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c6.setFixedHeight(12.8604651162793f);
						c6.setColspan(4);
						c6.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c6.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c6.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c6.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//第七行
						PdfPCell c7_1 = new PdfPCell(new Paragraph("维修人员", new Font(bfChinese, 11, Font.BOLD)));
						c7_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_1.setFixedHeight(8.8604651162793f);
						/*c7_1.setColspan(4);*/
						PdfPCell c7_2= new PdfPCell(new Paragraph(CP.getR5(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c7_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c7_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_2.setFixedHeight(8.8604651162793f);
						c7_2.setColspan(3);
						
						//第七行
						PdfPCell c8_1 = new PdfPCell(new Paragraph("提交到场时间", new Font(bfChinese, 11, Font.BOLD)));
						c8_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c8_2= new PdfPCell(new Paragraph(CP.getArriveDateTime(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c8_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_2.setFixedHeight(8.8604651162793f);
						c8_2.setColspan(3);
						//第七行
						PdfPCell c8_3 = new PdfPCell(new Paragraph("实际到场时间", new Font(bfChinese, 11, Font.BOLD)));
						c8_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_1.setFixedHeight(8.8604651162793f);
						
						String ArriveDate=CP.getArriveDate();
						if(ArriveDate==null){
							ArriveDate="";
						}
						String ArriveTime=CP.getArriveTime();
						if(ArriveTime==null){
							ArriveTime="";
						}
						PdfPCell c8_4= new PdfPCell(new Paragraph(ArriveDate+" "+ArriveTime, new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c8_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c8_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c7_2.setFixedHeight(8.8604651162793f);
						c8_4.setColspan(3);
										
						
						//第九行
						PdfPCell c9_1 = new PdfPCell(new Paragraph("故障状态", new Font(bfChinese, 11, Font.BOLD)));
						c9_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c9_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c9_1.setFixedHeight(40);
						PdfPCell c9_2= new PdfPCell(new Paragraph(CP.getFaultStatus(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c9_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						/*c9_2.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
						c9_2.setColspan(3);
						c9_2.setPadding(0f);
						c9_2.setPaddingTop(0f);
						c9_2.setPaddingBottom(0f);
						
						//第十行
						PdfPCell c10 = new PdfPCell(new Paragraph("", new Font(bfChinese, 11, Font.BOLD)));
						c10.setHorizontalAlignment(Element.ALIGN_LEFT);
						c10.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c10.setFixedHeight(12.8604651162793f);
						c10.setColspan(4);
						c10.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c10.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c10.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c10.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//第十一行
						PdfPCell c11_1 = new PdfPCell(new Paragraph("完工时间", new Font(bfChinese, 11, Font.BOLD)));
						c11_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c11_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						/*c11_1.setFixedHeight(15);*/
						
						PdfPCell c11_2= new PdfPCell(new Paragraph(CP.getCompleteTime(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c11_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c11_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c11_2.setColspan(3);
						
						
						//第十二行
						PdfPCell c12_1 = new PdfPCell(new Paragraph("维修描述", new Font(bfChinese, 11, Font.BOLD)));
						c12_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c12_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c12_1.setFixedHeight(40);
						PdfPCell c12_2= new PdfPCell(new Paragraph(CP.getProcessDesc(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c12_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						/*c12_2.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
						c12_2.setColspan(3);
						c12_2.setPadding(0f);
						c12_2.setPaddingTop(0f);
						c12_2.setPaddingBottom(0f);
						//第十三行
						PdfPCell c13_1 = new PdfPCell(new Paragraph("维修备注", new Font(bfChinese, 11, Font.BOLD)));
						c13_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c13_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c13_1.setFixedHeight(40);
						PdfPCell c13_2= new PdfPCell(new Paragraph(CP.getServiceRem(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c13_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						/*c13_2.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
						c13_2.setColspan(3);
						c13_2.setPadding(0f);
						c13_2.setPaddingTop(0f);
						c13_2.setPaddingBottom(0f);
						//第十四行
						PdfPCell c14 = new PdfPCell(new Paragraph("", new Font(bfChinese, 11, Font.BOLD)));
						c14.setHorizontalAlignment(Element.ALIGN_LEFT);
						c14.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c14.setFixedHeight(12.8140414511142793f);
						c14.setColspan(4);
						c14.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c14.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c14.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c14.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						//第十五行
						PdfPCell c15_1 = new PdfPCell(new Paragraph("是否更换配件", new Font(bfChinese, 11, Font.BOLD)));
						c15_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c15_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c15_2= new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c15_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c15_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c15_2.setColspan(3);
						
						
						//第十六行
						PdfPCell c16_1 = new PdfPCell(new Paragraph("配件名称（图号）", new Font(bfChinese, 11, Font.BOLD)));
						c16_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c16_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c16_2= new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c16_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c16_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c16_2.setColspan(3);
						
						//第十七行
						PdfPCell c17_1 = new PdfPCell(new Paragraph("是否收费", new Font(bfChinese, 11, Font.BOLD)));
						c17_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c17_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						PdfPCell c17_2= new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c17_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c17_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c17_2.setColspan(3);
						
						/**=======================添加客户签名=========================*/
						float[] headwidth5 = { 12,70};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable5 = new PdfPTable(headwidth5);					 
						headtable5.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable5.setWidthPercentage(100);
						headtable5.setSplitLate(false);
						
						PdfPCell c18_1 = new PdfPCell(new Paragraph("客户签名：", new Font(bfChinese, 11, Font.BOLD)));
						c18_1.setHorizontalAlignment(Element.ALIGN_RIGHT);//Element.ALIGN_LEFT
						c18_1.setVerticalAlignment(Element.ALIGN_MIDDLE);//ALIGN_MIDDLE
						//c18_1.setColspan(2);
						c18_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c18_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c18_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c18_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						headtable5.addCell(c18_1);
						
						if(CP.getCustomerSignature()!=null && !CP.getCustomerSignature().trim().equals("")){
							String folder2 = PropertiesUtil.getProperty("CalloutProcess.file.upload.folder");
							String path2=folder2+CP.getCustomerSignature();//上传的客户签名图片路径
							
							Image image2= Image.getInstance(path2);
							image2.setWidthPercentage(25);//控制图形大小
							image2.setAlignment(Image.ALIGN_LEFT);
							image2.setAbsolutePosition(250, 30);
							
							PdfPCell c18_2 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));
							c18_2.setHorizontalAlignment(Element.ALIGN_CENTER);
							c18_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//c18_2.setColspan(2);
							//c18_2.setRowspan(3);
							//cx_0.setFixedHeight(30.8604651162793f);
							c18_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
							c18_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
							c18_2.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
							c18_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
							c18_2.addElement(image2);
	
							headtable5.addCell(c18_2);
						}else{
							PdfPCell c18_2 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));
							c18_2.setHorizontalAlignment(Element.ALIGN_CENTER);
							c18_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//c18_2.setColspan(2);
							//c18_2.setRowspan(3);
							//cx_0.setFixedHeight(30.8604651162793f);
							c18_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
							c18_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
							c18_2.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
							c18_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
	
							headtable5.addCell(c18_2);
						}
						
						PdfPCell cd_55 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						cd_55.setHorizontalAlignment(Element.ALIGN_LEFT);
						cd_55.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cd_55.setColspan(4);
						cd_55.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cd_55.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cd_55.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
						cd_55.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						cd_55.addElement(headtable5);
						/**=======================添加客户签名=========================*/
						
						//获取图片路径
						String path = request.getSession().getServletContext().getRealPath("/");
						path=path+"layout\\images\\xjs_2.jpg";
						Image image= Image.getInstance(path);
						image.setWidthPercentage(75);//控制图形大小
						image.setAlignment(Image.ALIGN_LEFT);
						image.setAbsolutePosition(250, 30);
						
						PdfPCell cx_0 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));//
						cx_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						cx_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_0.setColspan(2);
						cx_0.setRowspan(3);
						//cx_0.setFixedHeight(30.8604651162793f);
						cx_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_0.setBorderWidthBottom(0.7f);//边框宽度-底; 0f表示隐藏边框
						cx_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						cx_0.addElement(image);
						
						PdfPCell cx_1 = new PdfPCell(new Paragraph(" ", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_1.setColspan(2);
						cx_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cx_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell cx_2 = new PdfPCell(new Paragraph("  西继迅达（许昌）电梯有限公司", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_2.setColspan(2);
						cx_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cx_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell cx_3 = new PdfPCell(new Paragraph("  河南省许昌西经济技术开发区延安路南段2120号", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_3.setColspan(2);
						cx_3.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_3.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_3.setBorderWidthBottom(0.7f);//边框宽度-底; 0f表示隐藏边框
						cx_3.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						headtable.addCell(cx_0);
						headtable.addCell(cx_1);
						headtable.addCell(cx_2);
						headtable.addCell(cx_3);
						headtable.addCell(c6);
						
                        headtable.addCell(ct_0);
                        headtable.addCell(c6);
                        headtable.addCell(c0_1);
					
						headtable.addCell(c6);
						//headtable.addCell(c6);
						
						headtable.addCell(c1_1);
						headtable.addCell(c1_2);
						headtable.addCell(c1_3);
						headtable.addCell(c1_4);
						
						headtable.addCell(c2_1);
						headtable.addCell(c2_2);
						headtable.addCell(c2_3);
						headtable.addCell(c2_4);
						
						headtable.addCell(c3_1);
						headtable.addCell(c3_2);
						headtable.addCell(c3_3);
						headtable.addCell(c3_4);
						
						headtable.addCell(c4_1);
						headtable.addCell(c4_2);
						headtable.addCell(c4_3);
						headtable.addCell(c4_4);
						headtable.addCell(c5_1);
						headtable.addCell(c5_2);
						headtable.addCell(c6);
						headtable.addCell(c6);
						
						headtable.addCell(c7_1);
						headtable.addCell(c7_2);
						
						headtable.addCell(c8_3);
						headtable.addCell(c8_4);
						headtable.addCell(c8_1);
						headtable.addCell(c8_2);
		
						headtable.addCell(c9_1);
						headtable.addCell(c9_2);
						
						headtable.addCell(c10);
						
						headtable.addCell(c11_1);
						headtable.addCell(c11_2);
						
						
						headtable.addCell(c12_1);
						headtable.addCell(c12_2);
						
		
						headtable.addCell(c13_1);
						headtable.addCell(c13_2);
						
						headtable.addCell(c14);
						headtable.addCell(c15_1);
						headtable.addCell(c15_2);
						headtable.addCell(c16_1);
						headtable.addCell(c16_2);
						headtable.addCell(c17_1);
						headtable.addCell(c17_2);
						
						//headtable.addCell(c6);
						headtable.addCell(c6);
						headtable.addCell(cd_55);
						
						
						document.add(headtable);

 
				document.close();
				
				
				//网页预览
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				//response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("厂检通知单_"+billno+".pdf", "utf-8"));
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();
				
				/**
				//下载pdf
				response.setContentType("application/x-msdownload");
				response.setContentLength(baos.size());
				response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("厂检通知单_"+billno+".pdf", "utf-8"));
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();
				*/
				
				/**
				//保存pdf文件到本地
				String stodaytime=DateUtil.getNowTime("yyyyMMddHHmmss");
				String savepath="D:\\Download\\厂检通知单_"+billno+".pdf";
				FileOutputStream fos=new FileOutputStream(savepath);
				baos.writeTo(fos);
				fos.flush();
				fos.close();
				*/
				}		
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

	//维保质量检查单
	public ActionForward toPrintTwoRecord6(HttpServletRequest request,HttpServletResponse response, List barCodeList,String billno)
			throws Exception {

			try {
				Document document = new Document();
				document.setPageSize(PageSize.A4);// 设置页面大小 
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();			
				BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				PdfWriter pdfwriter = PdfWriter.getInstance(document, baos);// 文件的输出路径+文件的实际名称
				//写入页脚
				/*String footerContent="报修热线：400-811-6869";*/

				
		        document.open();// 打开文档
	            
		        Image png = null;
				int k = 0;
				
				int namefontsize=13;
				int valuefontsize=12;
				int namefontsize2=11;
				int valuefontsize2=10;

				// barCodeList是获取打印条码的信息
				if (barCodeList != null && barCodeList.size() > 0) {
					QualityCheckManagement QM = (QualityCheckManagement) barCodeList.get(0);
					List hecirList = (List) barCodeList.get(1);
						float[] headwidth = { 50.604651162791f, 98.232558139535f, 50.604651162791f, 83.348837209302f };		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable = new PdfPTable(headwidth);					 
						headtable.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable.setWidthPercentage(100);
						headtable.setSplitLate(false);
						//headtable.setSplitRows(true);
						
						//写入页脚
						String footerContent="检查单编号："+QM.getBillno();
						Font fontDetail = new Font(bfChinese, 10, Font.NORMAL);// 数据体字体
						HeaderFooter header=new HeaderFooter(footerContent,fontDetail);
						pdfwriter.setPageEvent(header);
						//表头
						PdfPCell ct_0 = new PdfPCell(new Paragraph("维保质量检查单", new Font(bfChinese, 20, Font.BOLD)));
						ct_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						ct_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ct_0.setColspan(4);
						ct_0.setFixedHeight(30.8604651162793f);
						ct_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						ct_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ct_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ct_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						
						//检查单主信息
						PdfPCell c0_1 = new PdfPCell(new Paragraph("检查单编号：" + QM.getBillno(), new Font(bfChinese, namefontsize2, Font.BOLD)));
						c0_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c0_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c0_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c0_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c0_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c0_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c0_1.setColspan(4);
						
						
						//检查问题项
						PdfPCell c0_2 = new PdfPCell(new Paragraph("检查问题项", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c0_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c0_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c0_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c0_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c0_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c0_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c0_2.setColspan(4);
						
					/*	PdfPCell c0_2= new PdfPCell(new Paragraph(CM.getCalloutMasterNo(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c0_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c0_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c0_2.setColspan(3);
						c0_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c0_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c0_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c0_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
*/						
						//第一行
						PdfPCell c1_1 = new PdfPCell(new Paragraph("维保员工", new Font(bfChinese, 11, Font.BOLD)));
						c1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(20.8604651162793f);
						
						PdfPCell c1_2 = new PdfPCell(new Paragraph(QM.getMaintPersonnel(),new Font(bfChinese, valuefontsize2, Font.NORMAL))); 
						c1_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_2.setFixedHeight(20.8604651162793f);
						
						PdfPCell c1_3 = new PdfPCell(new Paragraph("甲方名称", new Font(bfChinese, 11, Font.BOLD)));
						c1_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_3.setFixedHeight(8.8604651162793f);
						
						PdfPCell c1_4 = new PdfPCell(new Paragraph(QM.getR3().toString(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c1_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_4.setFixedHeight(8.8604651162793f);
						
						//第二行
						PdfPCell c2_1 = new PdfPCell(new Paragraph("电梯编号", new Font(bfChinese, 11, Font.BOLD)));
						c2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c2_2= new PdfPCell(new Paragraph(QM.getElevatorNo(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c2_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c2_3 = new PdfPCell(new Paragraph("电梯位置", new Font(bfChinese, 11, Font.BOLD)));
						c2_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c2_4= new PdfPCell(new Paragraph(QM.getR2(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c2_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_4.setFixedHeight(8.8604651162793f);
						
						//第三行
						PdfPCell c3_1 = new PdfPCell(new Paragraph("检查时间", new Font(bfChinese, 11, Font.BOLD)));
						c3_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c3_2= new PdfPCell(new Paragraph(QM.getChecksDateTime(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c3_3 = new PdfPCell(new Paragraph("检查得分", new Font(bfChinese, 11, Font.BOLD)));
						c3_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c3_4= new PdfPCell(new Paragraph(QM.getTotalPoints().toString(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_4.setFixedHeight(8.8604651162793f);
						
						//第四行
						PdfPCell c3_5 = new PdfPCell(new Paragraph("督查人员", new Font(bfChinese, 11, Font.BOLD)));
						c3_5.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c3_6= new PdfPCell(new Paragraph(QM.getR4(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_6.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c3_7 = new PdfPCell(new Paragraph("", new Font(bfChinese, 11, Font.BOLD)));
						c3_7.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_7.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c3_8= new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_8.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_8.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_4.setFixedHeight(8.8604651162793f);
						
						//第六行
						PdfPCell c6 = new PdfPCell(new Paragraph("", new Font(bfChinese, 11, Font.BOLD)));
						c6.setHorizontalAlignment(Element.ALIGN_LEFT);
						c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c6.setFixedHeight(12.8604651162793f);
						c6.setColspan(4);
						c6.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c6.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c6.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c6.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						/**=======================添加客户签名=========================*/
						float[] headwidth5 = { 12,70};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable5 = new PdfPTable(headwidth5);					 
						headtable5.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable5.setWidthPercentage(100);
						headtable5.setSplitLate(false);
						
						PdfPCell c18_1 = new PdfPCell(new Paragraph("维保工签字：", new Font(bfChinese, 11, Font.BOLD)));
						c18_1.setHorizontalAlignment(Element.ALIGN_RIGHT);//Element.ALIGN_LEFT
						c18_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c18_1.setColspan(2);
						c18_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c18_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c18_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c18_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						headtable5.addCell(c18_1);
						
						if(QM.getCustomerSignature()!=null && !QM.getCustomerSignature().trim().equals("")){
							String folder2 = PropertiesUtil.getProperty("QualityCheckManagement.file.upload.folder");
							String path2=folder2+QM.getCustomerSignature();//上传的客户签名图片路径
							
							Image image2= Image.getInstance(path2);
							image2.setWidthPercentage(25);//控制图形大小
							image2.setAlignment(Image.ALIGN_LEFT);
							image2.setAbsolutePosition(250, 30);
							
							PdfPCell c18_2 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));
							c18_2.setHorizontalAlignment(Element.ALIGN_CENTER);
							c18_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//c18_2.setColspan(2);
							//c18_2.setRowspan(3);
							//cx_0.setFixedHeight(30.8604651162793f);
							c18_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
							c18_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
							c18_2.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
							c18_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
							c18_2.addElement(image2);
	
							headtable5.addCell(c18_2);
						}else{
							PdfPCell c18_2 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));
							c18_2.setHorizontalAlignment(Element.ALIGN_CENTER);
							c18_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//c18_2.setColspan(2);
							//c18_2.setRowspan(3);
							//cx_0.setFixedHeight(30.8604651162793f);
							c18_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
							c18_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
							c18_2.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
							c18_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
	
							headtable5.addCell(c18_2);
						}
						
						PdfPCell cd_55 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						cd_55.setHorizontalAlignment(Element.ALIGN_LEFT);
						cd_55.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cd_55.setColspan(4);
						cd_55.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cd_55.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cd_55.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
						cd_55.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						cd_55.addElement(headtable5);
						/**=======================添加客户签名=========================*/
						
						//获取图片路径
						String path = request.getSession().getServletContext().getRealPath("/");
						path=path+"layout\\images\\xjs_2.jpg";
						Image image= Image.getInstance(path);
						image.setWidthPercentage(75);//控制图形大小
						image.setAlignment(Image.ALIGN_LEFT);
						image.setAbsolutePosition(250, 30);
						
						PdfPCell cx_0 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));//
						cx_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						cx_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_0.setColspan(2);
						cx_0.setRowspan(3);
						//cx_0.setFixedHeight(30.8604651162793f);
						cx_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_0.setBorderWidthBottom(0.7f);//边框宽度-底; 0f表示隐藏边框
						cx_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						cx_0.addElement(image);
						
						PdfPCell cx_1 = new PdfPCell(new Paragraph(" ", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_1.setColspan(2);
						cx_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cx_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell cx_2 = new PdfPCell(new Paragraph("  西继迅达（许昌）电梯有限公司", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_2.setColspan(2);
						cx_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cx_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell cx_3 = new PdfPCell(new Paragraph("  河南省许昌西经济技术开发区延安路南段2120号", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_3.setColspan(2);
						cx_3.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_3.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_3.setBorderWidthBottom(0.7f);//边框宽度-底; 0f表示隐藏边框
						cx_3.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						headtable.addCell(cx_0);
						headtable.addCell(cx_1);
						headtable.addCell(cx_2);
						headtable.addCell(cx_3);
						headtable.addCell(c6);
						
                        headtable.addCell(ct_0);
                        headtable.addCell(c6);
                        headtable.addCell(c0_1);
					
/*						headtable.addCell(c6);*/
						//headtable.addCell(c6);
						
						headtable.addCell(c1_1);
						headtable.addCell(c1_2);
						headtable.addCell(c1_3);
						headtable.addCell(c1_4);
						
						headtable.addCell(c2_1);
						headtable.addCell(c2_2);
						headtable.addCell(c2_3);
						headtable.addCell(c2_4);
						
						headtable.addCell(c3_1);
						headtable.addCell(c3_2);
						headtable.addCell(c3_3);
						headtable.addCell(c3_4);
						
						headtable.addCell(c3_5);
						headtable.addCell(c3_6);
						headtable.addCell(c3_7);
						headtable.addCell(c3_8);
						
						headtable.addCell(c6);
						headtable.addCell(c0_2);
						
						//headtable.addCell(c6);
						/*headtable.addCell(c6);
*/
						
						//第十二行
						float[] headwidth3 = {35.232558139535f, 93.348837209302f, 93.348837209302f};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable3 = new PdfPTable(headwidth3);					 
						headtable3.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable3.setWidthPercentage(100);
						
						PdfPCell c12_0= new PdfPCell(new Paragraph("序号", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_0.setVerticalAlignment(Element.ALIGN_MIDDLE);						
						
						PdfPCell c12_1 = new PdfPCell(new Paragraph("问题内容", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_1.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c12_1.setFixedHeight(9.8604651162793f);
						//c12_1.setColspan(2);
						
						PdfPCell c12_2= new PdfPCell(new Paragraph("备注", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_2.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c12_2.setFixedHeight(9.8604651162793f);
						//c12_2.setColspan(2);
						
						
						headtable3.addCell(c12_0);
						headtable3.addCell(c12_1);
						headtable3.addCell(c12_2);
						
						if(hecirList!=null && hecirList.size()>0){
							String examTypeName="";
							int etnum=0;
							for(int i=0;i<hecirList.size();i++){
								HashMap map=(HashMap) hecirList.get(i);
								
						
								String msid=(String) map.get("msid");
								PdfPCell c13_0 = new PdfPCell(new Paragraph(msid, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_0.setHorizontalAlignment(Element.ALIGN_CENTER);
								c13_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_0.setColspan(2);
								//j++;
								
								String msName=(String) map.get("msname");
								
								PdfPCell c13_1 = new PdfPCell(new Paragraph(msName, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_1.setHorizontalAlignment(Element.ALIGN_LEFT);
								c13_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_1.setColspan(2);
								
								String rem=(String) map.get("rem");
							
								PdfPCell c13_2 = new PdfPCell(new Paragraph(rem, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_2.setHorizontalAlignment(Element.ALIGN_LEFT);
								c13_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_2.setColspan(2);
								
								headtable3.addCell(c13_0);
								headtable3.addCell(c13_1);
								headtable3.addCell(c13_2);
							}
						}
						PdfPCell c14_1 = new PdfPCell(new Paragraph("督查意见", new Font(bfChinese, 11, Font.BOLD)));
						c14_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c14_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c14_1.setFixedHeight(40);
						PdfPCell c14_2= new PdfPCell(new Paragraph(QM.getSupervOpinion(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c14_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						/*c13_2.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
						c14_2.setColspan(3);
						c14_2.setPadding(0f);
						c14_2.setPaddingTop(0f);
						c14_2.setPaddingBottom(0f);
						headtable3.addCell(c14_1);
						headtable3.addCell(c14_2);
						
						PdfPCell cd_0 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));

						cd_0.setColspan(4);
						cd_0.addElement(headtable3);
						cd_0.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						cd_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cd_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cd_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						headtable.addCell(cd_0);
						headtable.addCell(c6);
						headtable.addCell(cd_55);
						
						document.add(headtable);

 
				document.close();
				
				
				//网页预览
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				//response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("厂检通知单_"+billno+".pdf", "utf-8"));
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();
				
				/**
				//下载pdf
				response.setContentType("application/x-msdownload");
				response.setContentLength(baos.size());
				response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("厂检通知单_"+billno+".pdf", "utf-8"));
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();
				*/
				
				/**
				//保存pdf文件到本地
				String stodaytime=DateUtil.getNowTime("yyyyMMddHHmmss");
				String savepath="D:\\Download\\厂检通知单_"+billno+".pdf";
				FileOutputStream fos=new FileOutputStream(savepath);
				baos.writeTo(fos);
				fos.flush();
				fos.close();
				*/
				}		
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

	//保养通知单
	public ActionForward toPrintTwoRecord7(HttpServletRequest request,HttpServletResponse response, List barCodeList,String billno)
			throws Exception {

			try {
				Document document = new Document();
				document.setPageSize(PageSize.A4);// 设置页面大小 
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();			
				BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				PdfWriter pdfwriter = PdfWriter.getInstance(document, baos);// 文件的输出路径+文件的实际名称
				//写入页脚
				/*String footerContent="报修热线：400-811-6869";*/

				
		        document.open();// 打开文档
	            
		        Image png = null;
				int k = 0;
				
				int namefontsize=13;
				int valuefontsize=12;
				int namefontsize2=11;
				int valuefontsize2=10;

				// barCodeList是获取打印条码的信息
				if (barCodeList != null && barCodeList.size() > 0) {
					MaintenanceWorkPlanDetail QM = (MaintenanceWorkPlanDetail) barCodeList.get(0);
					List hecirList = (List) barCodeList.get(1);
					List etcpList = (List) barCodeList.get(2);
					HashMap map1=(HashMap) etcpList.get(0);
					String elevatorNo = (String)map1.get("elevatorNo");
					String elevatorType = (String)map1.get("elevatorType");
					if(elevatorType.equals("T")){
						elevatorType="直梯";
							}else{
								elevatorType="扶梯";
							}
					String enddate = QM.getMaintEndTime();
					enddate = enddate.substring(0, enddate.length()-9);
						float[] headwidth = { 50.604651162791f, 98.232558139535f, 50.604651162791f, 83.348837209302f };		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable = new PdfPTable(headwidth);					 
						headtable.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable.setWidthPercentage(100);
						headtable.setSplitLate(false);
						//headtable.setSplitRows(true);
						
						//写入页脚
						String footerContent="保养单号:"+QM.getSingleno();
						Font fontDetail = new Font(bfChinese, 10, Font.NORMAL);// 数据体字体
						HeaderFooter header=new HeaderFooter(footerContent,fontDetail);
						pdfwriter.setPageEvent(header);
						//表头
						PdfPCell ct_0 = new PdfPCell(new Paragraph("电梯保养记录单", new Font(bfChinese, 20, Font.BOLD)));
						ct_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						ct_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ct_0.setColspan(4);
						ct_0.setFixedHeight(30.8604651162793f);
						ct_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						ct_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						ct_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						ct_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						
						//检查单主信息
						PdfPCell c0_1 = new PdfPCell(new Paragraph("保养单号："+QM.getSingleno() , new Font(bfChinese, namefontsize2, Font.BOLD)));
						c0_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c0_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c0_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c0_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c0_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c0_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c0_1.setColspan(4);
						
						
						//检查问题项
						PdfPCell c0_2 = new PdfPCell(new Paragraph("检查问题项", new Font(bfChinese, namefontsize2, Font.BOLD)));
						c0_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c0_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c0_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c0_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c0_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c0_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						c0_2.setColspan(4);
						
					/*	PdfPCell c0_2= new PdfPCell(new Paragraph(CM.getCalloutMasterNo(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c0_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c0_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c0_2.setColspan(3);
						c0_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c0_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c0_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c0_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
*/						
						//第一行
						PdfPCell c1_1 = new PdfPCell(new Paragraph("保养类型", new Font(bfChinese, 11, Font.BOLD)));
						c1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_1.setFixedHeight(20.8604651162793f);
						
						PdfPCell c1_2 = new PdfPCell(new Paragraph(QM.getMaintType(),new Font(bfChinese, valuefontsize2, Font.NORMAL))); 
						c1_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_2.setFixedHeight(20.8604651162793f);
						
						PdfPCell c1_3 = new PdfPCell(new Paragraph("甲方名称", new Font(bfChinese, 11, Font.BOLD)));
						c1_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_3.setFixedHeight(8.8604651162793f);
						
						PdfPCell c1_4 = new PdfPCell(new Paragraph(QM.getR3(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c1_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c1_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c1_4.setFixedHeight(8.8604651162793f);
						
						//第二行
						PdfPCell c2_1 = new PdfPCell(new Paragraph("电梯编号", new Font(bfChinese, 11, Font.BOLD)));
						c2_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c2_2= new PdfPCell(new Paragraph(elevatorNo, new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c2_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c2_3 = new PdfPCell(new Paragraph("电梯位置", new Font(bfChinese, 11, Font.BOLD)));
						c2_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c2_4= new PdfPCell(new Paragraph(QM.getR2(), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c2_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c2_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c2_4.setFixedHeight(8.8604651162793f);
						
						//第三行
						PdfPCell c3_1 = new PdfPCell(new Paragraph("电梯类型", new Font(bfChinese, 11, Font.BOLD)));
						c3_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c3_2= new PdfPCell(new Paragraph(elevatorType, new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_2.setFixedHeight(8.8604651162793f);
					
						
						PdfPCell c3_3 = new PdfPCell(new Paragraph("保养完工日期", new Font(bfChinese, 11, Font.BOLD)));
						c3_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_3.setFixedHeight(8.8604651162793f);
						

						PdfPCell c3_4= new PdfPCell(new Paragraph(enddate, new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_4.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_4.setFixedHeight(8.8604651162793f);
						
						//第三行
						PdfPCell c3_11 = new PdfPCell(new Paragraph("保养人员", new Font(bfChinese, 11, Font.BOLD)));
						c3_11.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_11.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_1.setFixedHeight(8.8604651162793f);
						
						PdfPCell c3_21= new PdfPCell(new Paragraph((String)map1.get("r5name"), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c3_21.setHorizontalAlignment(Element.ALIGN_LEFT);
						c3_21.setColspan(3);
						c3_21.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//	c3_2.setFixedHeight(8.8604651162793f);
						
						//第六行
						PdfPCell c6 = new PdfPCell(new Paragraph("", new Font(bfChinese, 11, Font.BOLD)));
						c6.setHorizontalAlignment(Element.ALIGN_LEFT);
						c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c6.setFixedHeight(12.8604651162793f);
						c6.setColspan(4);
						c6.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						c6.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c6.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c6.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						/**=======================添加客户签名=========================*/
						float[] headwidth5 = { 12,70};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable5 = new PdfPTable(headwidth5);					 
						headtable5.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable5.setWidthPercentage(100);
						headtable5.setSplitLate(false);
						
						PdfPCell c18_1 = new PdfPCell(new Paragraph("客户签名：", new Font(bfChinese, 11, Font.BOLD)));
						c18_1.setHorizontalAlignment(Element.ALIGN_RIGHT);//Element.ALIGN_LEFT
						c18_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c18_1.setColspan(2);
						c18_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						c18_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						c18_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						c18_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						headtable5.addCell(c18_1);
						
						if(QM.getCustomerSignature()!=null && !QM.getCustomerSignature().trim().equals("")){
							String folder2 = PropertiesUtil.getProperty("MaintenanceWorkPlanDetail.file.upload.folder");
							String path2=folder2+QM.getCustomerSignature();//上传的客户签名图片路径
							
							Image image2= Image.getInstance(path2);
							image2.setWidthPercentage(25);//控制图形大小
							image2.setAlignment(Image.ALIGN_LEFT);
							image2.setAbsolutePosition(250, 30);
							
							PdfPCell c18_2 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));
							c18_2.setHorizontalAlignment(Element.ALIGN_CENTER);
							c18_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//c18_2.setColspan(2);
							//c18_2.setRowspan(3);
							//cx_0.setFixedHeight(30.8604651162793f);
							c18_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
							c18_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
							c18_2.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
							c18_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
							c18_2.addElement(image2);
	
							headtable5.addCell(c18_2);
						}else{
							PdfPCell c18_2 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));
							c18_2.setHorizontalAlignment(Element.ALIGN_CENTER);
							c18_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							//c18_2.setColspan(2);
							//c18_2.setRowspan(3);
							//cx_0.setFixedHeight(30.8604651162793f);
							c18_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
							c18_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
							c18_2.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
							c18_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
	
							headtable5.addCell(c18_2);
						}
						
						PdfPCell cd_55 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));
						cd_55.setHorizontalAlignment(Element.ALIGN_LEFT);
						cd_55.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cd_55.setColspan(4);
						cd_55.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cd_55.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cd_55.setBorderWidthBottom(0.0f);//边框宽度-底; 0f表示隐藏边框
						cd_55.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						cd_55.addElement(headtable5);
						/**=======================添加客户签名=========================*/
						
						//获取图片路径
						String path = request.getSession().getServletContext().getRealPath("/");
						path=path+"layout\\images\\xjs_2.jpg";
						Image image= Image.getInstance(path);
						image.setWidthPercentage(75);//控制图形大小
						image.setAlignment(Image.ALIGN_LEFT);
						image.setAbsolutePosition(250, 30);
						
						PdfPCell cx_0 = new PdfPCell(new Paragraph("", new Font(bfChinese, valuefontsize2, Font.BOLD)));//
						cx_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						cx_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_0.setColspan(2);
						cx_0.setRowspan(3);
						//cx_0.setFixedHeight(30.8604651162793f);
						cx_0.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_0.setBorderWidthBottom(0.7f);//边框宽度-底; 0f表示隐藏边框
						cx_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						cx_0.addElement(image);
						
						PdfPCell cx_1 = new PdfPCell(new Paragraph(" ", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_1.setColspan(2);
						cx_1.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_1.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_1.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cx_1.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell cx_2 = new PdfPCell(new Paragraph("  西继迅达（许昌）电梯有限公司", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_2.setColspan(2);
						cx_2.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_2.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_2.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cx_2.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						PdfPCell cx_3 = new PdfPCell(new Paragraph("  河南省许昌西经济技术开发区延安路南段2120号", new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						cx_3.setHorizontalAlignment(Element.ALIGN_LEFT);
						cx_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cx_3.setColspan(2);
						cx_3.setBorderWidthTop(0f);//边框宽度-顶;0f表示隐藏边框
						cx_3.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cx_3.setBorderWidthBottom(0.7f);//边框宽度-底; 0f表示隐藏边框
						cx_3.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						headtable.addCell(cx_0);
						headtable.addCell(cx_1);
						headtable.addCell(cx_2);
						headtable.addCell(cx_3);
						headtable.addCell(c6);
						
                        headtable.addCell(ct_0);
                        headtable.addCell(c6);
                        headtable.addCell(c0_1);
					
/*						headtable.addCell(c6);*/
						//headtable.addCell(c6);
						
						headtable.addCell(c1_1);
						headtable.addCell(c1_2);
						headtable.addCell(c1_3);
						headtable.addCell(c1_4);
						
						headtable.addCell(c2_1);
						headtable.addCell(c2_2);
						headtable.addCell(c2_3);
						headtable.addCell(c2_4);
						
						headtable.addCell(c3_1);
						headtable.addCell(c3_2);
						headtable.addCell(c3_3);
						headtable.addCell(c3_4);
						
						headtable.addCell(c3_11);
						headtable.addCell(c3_21);
						
						headtable.addCell(c6);
						headtable.addCell(c0_2);
						
						//headtable.addCell(c6);
						/*headtable.addCell(c6);
*/
						
						//第十二行
						float[] headwidth3 = {15.232558139535f, 73.348837209302f, 93.348837209302f,40};		
			            //float[] headwidth = { 35.232558139535f, 35.232558139535f, 35.232558139535f, 35.232558139535f };			
						PdfPTable headtable3 = new PdfPTable(headwidth3);					 
						headtable3.setHorizontalAlignment(Element.ALIGN_CENTER);
						headtable3.setWidthPercentage(100);
						
						PdfPCell c12_0= new PdfPCell(new Paragraph("序号", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_0.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_0.setVerticalAlignment(Element.ALIGN_MIDDLE);						
						
						PdfPCell c12_1 = new PdfPCell(new Paragraph("维保项目（内容）", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_1.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c12_1.setFixedHeight(9.8604651162793f);
						//c12_1.setColspan(2);
						
						PdfPCell c12_2= new PdfPCell(new Paragraph("维保基本要求", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_2.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						//c12_2.setFixedHeight(9.8604651162793f);
						//c12_2.setColspan(2);
						PdfPCell c12_3= new PdfPCell(new Paragraph("保养情况", new Font(bfChinese,namefontsize, Font.BOLD)));
						c12_3.setHorizontalAlignment(Element.ALIGN_CENTER);
						c12_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						
						headtable3.addCell(c12_0);
						headtable3.addCell(c12_1);
						headtable3.addCell(c12_2);
						headtable3.addCell(c12_3);
						if(hecirList!=null && hecirList.size()>0){	
							String examTypeName="";
							int etnum=0;
							for(int i=0;i<hecirList.size();i++){
								HashMap map=(HashMap) hecirList.get(i);
								
						
								String orderby=(String) map.get("orderby");
								PdfPCell c13_0 = new PdfPCell(new Paragraph(orderby, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_0.setHorizontalAlignment(Element.ALIGN_CENTER);
								c13_0.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_0.setColspan(2);
								//j++;
								
								String maintitem=(String) map.get("maintitem");
								
								PdfPCell c13_1 = new PdfPCell(new Paragraph(maintitem, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_1.setHorizontalAlignment(Element.ALIGN_LEFT);
								c13_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_1.setColspan(2);
								
								String maintcontents=(String) map.get("maintcontents");
							
								PdfPCell c13_2 = new PdfPCell(new Paragraph(maintcontents, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_2.setHorizontalAlignment(Element.ALIGN_LEFT);
								c13_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
								//c13_2.setColspan(2);
								
								String ismaintain=(String) map.get("ismaintain");
								if(ismaintain.equals("Y")){
									ismaintain= "√";
								}else{
									ismaintain="－";
								}
								PdfPCell c13_3 = new PdfPCell(new Paragraph(ismaintain, new Font(bfChinese,valuefontsize, Font.NORMAL)));
								c13_3.setHorizontalAlignment(Element.ALIGN_LEFT);
								c13_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
								
								headtable3.addCell(c13_0);
								headtable3.addCell(c13_1);
								headtable3.addCell(c13_2);
								headtable3.addCell(c13_3);
							}
						}
						PdfPCell c14_1 = new PdfPCell(new Paragraph("备注", new Font(bfChinese, 11, Font.BOLD)));
						c14_1.setHorizontalAlignment(Element.ALIGN_LEFT);
						c14_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						c14_1.setFixedHeight(40);
						PdfPCell c14_2= new PdfPCell(new Paragraph((String)map1.get("byrem"), new Font(bfChinese, valuefontsize2, Font.NORMAL)));
						c14_2.setHorizontalAlignment(Element.ALIGN_LEFT);
						/*c13_2.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
						c14_2.setColspan(3);
						c14_2.setPadding(0f);
						c14_2.setPaddingTop(0f);
						c14_2.setPaddingBottom(0f);
						headtable3.addCell(c14_1);
						headtable3.addCell(c14_2);
						
						PdfPCell cd_0 = new PdfPCell(new Paragraph("", new Font(bfChinese,valuefontsize2, Font.NORMAL)));

						cd_0.setColspan(4);
						cd_0.addElement(headtable3);
						cd_0.setBorderWidthTop(0f);//边框宽度-顶; 0f表示隐藏边框
						cd_0.setBorderWidthRight(0f);//边框宽度-右; 0f表示隐藏边框
						cd_0.setBorderWidthBottom(0f);//边框宽度-底; 0f表示隐藏边框
						cd_0.setBorderWidthLeft(0f);//边框宽度-左; 0f表示隐藏边框
						
						headtable.addCell(cd_0);
						headtable.addCell(c6);
						headtable.addCell(cd_55);
						
						document.add(headtable);

 
				document.close();
				
				
				//网页预览
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				//response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("厂检通知单_"+billno+".pdf", "utf-8"));
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();
				
				/**
				//下载pdf
				response.setContentType("application/x-msdownload");
				response.setContentLength(baos.size());
				response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("厂检通知单_"+billno+".pdf", "utf-8"));
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				out.flush();
				out.close();
				*/
				
				/**
				//保存pdf文件到本地
				String stodaytime=DateUtil.getNowTime("yyyyMMddHHmmss");
				String savepath="D:\\Download\\厂检通知单_"+billno+".pdf";
				FileOutputStream fos=new FileOutputStream(savepath);
				baos.writeTo(fos);
				fos.flush();
				fos.close();
				*/
				}		
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

	
}


