/**
 * 该实例采用axis方式调用，需要加载axis相关jar包
 */
package com.gzunicorn.struts.action.xjsgg;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

/**
 * 短信发送为Webservices服务 由西继迅达提供
 * @author Lijun
 *
 */
public class SmsService {
		
	/**
	 * 急修发送短信
	 * @param istrap 是否困人
	 * @param elevatorno 电梯编号
	 * @param address 项目名称及楼栋号
	 * @param telno 手机号码
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public static boolean sendSMS(String istraptext,String elevatorno,String address, String telno)
			throws MalformedURLException, ServiceException, RemoteException {
		
		Boolean finash=true;
		// Sms Service(目标web service 路径)
		String endpoint = "http://10.10.0.111:8080/API/SmsService.asmx";
		// 声明Service服务
		Service service = new Service();
		Call call;
		try {
			call = (Call) service.createCall();

			// 配置SmsService地址
			call.setTargetEndpointAddress(new URL(endpoint));
			// 是否是soap action,默认为true
			call.setUseSOAPAction(false);
			// 设定调用3分钟不返回则超时
			call.setTimeout(new Integer(180000));
			// 命名空间(WSDL文件中的targetNameSpace属性值) 以及方法名（发布的方法名）
			call.setOperationName(new QName("http://XjsSms.org/", "DispatchWarning"));
			// 方法页面内对应的SOAPAction项
			call.setSOAPActionURI("http://XjsSms.org/DispatchWarning");

			/**
			 * 参数，主要要和webservice接口中的名称一致，类型要使用webservice目标语言的类型 错误例子
			 * ：call.addParameter
			 * ("KunRen",org.apache.axis.encoding.XMLType.XSD_STRING,
			 * ParameterMode.IN); 例子中的方式将webservice将无法获得数据
			 */
			call.addParameter(new QName("http://XjsSms.org/", "KunRen"), org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter(new QName("http://XjsSms.org/", "ElevatorNo"), org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter(new QName("http://XjsSms.org/", "Address"), org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter(new QName("http://XjsSms.org/", "Phone"), org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);

			// 返回值类型
			call.setReturnType(XMLType.XSD_STRING);

			//传递
			String KunRen = istraptext;//是否困人
			String ElevatorNo = elevatorno;// 电梯编号
			String Address = address;// 电梯所在地址
			String Phone = telno;// 维保员的手机号码
			// 获取返回值
			String resXML = (String) call.invoke(new Object[] { KunRen, ElevatorNo, Address, Phone });

			System.out.println("急修=SmsService返回信息 :  " + resXML);
			finash=true;
		} catch (Exception e) {
			finash=false;
			e.printStackTrace();
		}
		return finash;
	}
	/**
	 * 配件申请发送短信
	 * @param phone 手机号码
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public static boolean compSendSMS(String phone)
			throws MalformedURLException, ServiceException, RemoteException {
		
		Boolean finash=true;
		// Sms Service(目标web service 路径)
		String endpoint = "http://10.10.0.111:8080/API/SmsService.asmx";
		// 声明Service服务
		Service service = new Service();
		Call call;
		try {
			call = (Call) service.createCall();

			// 配置SmsService地址
			call.setTargetEndpointAddress(new URL(endpoint));
			// 是否是soap action,默认为true
			call.setUseSOAPAction(false);
			// 设定调用3分钟不返回则超时
			call.setTimeout(new Integer(180000));
			
			// 命名空间(WSDL文件中的targetNameSpace属性值) 以及方法名（发布的方法名）
			call.setOperationName(new QName("http://XjsSms.org/", "ComponentSaleNotify"));
			// 方法页面内对应的SOAPAction项
			call.setSOAPActionURI("http://XjsSms.org/ComponentSaleNotify");

			/**
			 * 参数，主要要和webservice接口中的名称一致，类型要使用webservice目标语言的类型 错误例子
			 * ：call.addParameter
			 * ("KunRen",org.apache.axis.encoding.XMLType.XSD_STRING,
			 * ParameterMode.IN); 例子中的方式将webservice将无法获得数据
			 */
			call.addParameter(new QName("http://XjsSms.org/", "Phone"), org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);

			// 返回值类型
			call.setReturnType(XMLType.XSD_STRING);

			// 获取返回值
			String resXML = (String) call.invoke(new Object[] { phone });

			System.out.println("配件申请=SmsService返回信息 :  " + resXML);
			finash=true;
		} catch (Exception e) {
			finash=false;
			e.printStackTrace();
		}
		return finash;
	}
	/**
	 * 技术支持申请发送短信
	 * @param phone 手机号码
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public static boolean techSendSMS(String phone)
			throws MalformedURLException, ServiceException, RemoteException {
		
		Boolean finash=true;
		// Sms Service(目标web service 路径)
		String endpoint = "http://10.10.0.111:8080/API/SmsService.asmx";
		// 声明Service服务
		Service service = new Service();
		Call call;
		try {
			call = (Call) service.createCall();

			// 配置SmsService地址
			call.setTargetEndpointAddress(new URL(endpoint));
			// 是否是soap action,默认为true
			call.setUseSOAPAction(false);
			// 设定调用3分钟不返回则超时
			call.setTimeout(new Integer(180000));
			
			// 命名空间(WSDL文件中的targetNameSpace属性值) 以及方法名（发布的方法名）
			call.setOperationName(new QName("http://XjsSms.org/", "TechSupportNotify"));
			// 方法页面内对应的SOAPAction项
			call.setSOAPActionURI("http://XjsSms.org/TechSupportNotify");

			/**
			 * 参数，主要要和webservice接口中的名称一致，类型要使用webservice目标语言的类型 错误例子
			 * ：call.addParameter
			 * ("KunRen",org.apache.axis.encoding.XMLType.XSD_STRING,
			 * ParameterMode.IN); 例子中的方式将webservice将无法获得数据
			 */
			call.addParameter(new QName("http://XjsSms.org/", "Phone"), org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);

			// 返回值类型
			call.setReturnType(XMLType.XSD_STRING);

			// 获取返回值
			String resXML = (String) call.invoke(new Object[] { phone });

			System.out.println("技术支持申请=SmsService返回信息 :  " + resXML);
			finash=true;
		} catch (Exception e) {
			finash=false;
			e.printStackTrace();
		}
		return finash;
	}
	
}


