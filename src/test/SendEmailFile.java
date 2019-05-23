package test;

import java.io.File;  
import java.util.Date;  
import java.util.Properties;
import javax.activation.DataHandler;  
import javax.activation.DataSource;  
import javax.activation.FileDataSource;  
import javax.mail.BodyPart;  
import javax.mail.Message;  
import javax.mail.Multipart;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeBodyPart;  
import javax.mail.internet.MimeMessage;  
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;  
  
/*** 
 *  发送邮件带附件
 * */  
public class SendEmailFile {  
    
	/**
	 * 发送邮件
	 * @param subject 标题
	 * @param toMail 接收邮箱
	 * @param content 邮件内容
	 * @param files 附件完整路径【如果为空就不发附件，可以是文件夹如 D:/Download，也可以是文件如 D:/Download/更新文件路径.txt】
	 * @return
	 */
    public static boolean sendMail(String subject,String toMail,String content,String files) {  
        boolean isFlag = false;  
        try {  
        	System.out.println("正在发送邮件.........");
        	
            String smtpFromMail = "lj@gzunicorn.com";  //账号  
            String pwd = "abc123456*"; //密码  
            int port = 25; //端口  
            String host = "smtp.gzunicorn.com"; //端口  
            
//            String smtpFromMail = "crm@guangri.net";  //账号  
//            String pwd = "12345678"; //密码  
//            int port = 25; //端口  
//            String host = "www.guangri.net"; //端口  
  
            Properties props = new Properties();  
            props.put("mail.smtp.host", host);  
            props.put("mail.smtp.auth", "true");  
            props.put("mail.smtp.ssl.enable", "true");  
            Session session = Session.getDefaultInstance(props);  
            session.setDebug(false);  
  
            MimeMessage message = new MimeMessage(session);  
            try {
                message.setFrom(new InternetAddress(smtpFromMail, "QQ邮件测试"));  
                message.addRecipient(Message.RecipientType.TO,new InternetAddress(toMail));  
                message.setSubject(subject);  
                message.addHeader("charset", "UTF-8");  

                /*添加正文内容*/  
                Multipart multipart = new MimeMultipart();
                //添加邮件正文  
                BodyPart contentBodyPart = new MimeBodyPart();  
                //邮件内容  
                contentBodyPart.setContent(content, "text/html;charset=UTF-8");  
                multipart.addBodyPart(contentBodyPart); 
                  
                /*添加附件*/ 
                if(files!=null && !files.trim().equals("")){
	                //for (String file : files) {  
	                    File usFile = new File(files);  
	                    MimeBodyPart fileBody = new MimeBodyPart();  
	                    DataSource source = new FileDataSource(files);  
	                    fileBody.setDataHandler(new DataHandler(source));  
	                    
	                    System.out.println("......"+usFile.getName());
	
	                    fileBody.setFileName(MimeUtility.encodeWord(usFile.getName()));
	                    multipart.addBodyPart(fileBody);  
	               // }  
                }
                message.setContent(multipart);  
                message.setSentDate(new Date());  
                message.saveChanges();  
                Transport transport = session.getTransport("smtp");  
  
                transport.connect(host, port, smtpFromMail, pwd);  
                transport.sendMessage(message, message.getAllRecipients());  
                transport.close();  
                isFlag = true;  
                
                System.out.println("发送邮件成功.........");
            } catch (Exception e) {  
                isFlag = false;  
                e.printStackTrace();
                System.out.println("发送邮件失败1.........");
            }  
        } catch (Exception e2) {  
            e2.printStackTrace();  
            System.out.println("发送邮件失败2.........");
        }  
        return isFlag;  
    }  
  
    public static void main(String[] args) {
    	//String content="朋友好久不见";
    	String content = "<a href='http://crm.guangri.net:9000/GRCRM/contractProcessAction.do?method=toDisplayRecord target='_blank'>abc阿富汗的举案说法</a>";  
    	String filepath="D:/Download/更新文件路径.txt";
    	
    	boolean isFlag=SendEmailFile.sendMail("你好", "wwlijun@foxmail.com", content,filepath); 
    	
    }  
      
}  






