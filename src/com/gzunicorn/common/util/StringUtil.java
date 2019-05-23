package com.gzunicorn.common.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串处理类
 * @author ZZG
 *
 */
public class StringUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s1="1、上述项目备料及施工周期：12~15个工作日。施工时间：周一至周五（不含节假日） 8.30~17.30。（施工时间如有特殊要求，需要在合同签定前商议）。  2、上述项目不含土建费用。维修过程如发现其他项目或更换其他零件，费用另计。  3、进场前7天付齐100%工程款。  4、所有工程项目按国家标准验收，质量保修期为一年，且在与我司签定的《电梯保养合同》期内有效。  5、合同签订后或施工过程中因不可抗力原因造成的损失，双方免除赔偿责任。  6、上述本报价一式叁份，甲方(委托单位)壹份，乙方(填报单位)贰份。有效期三十天。  7、上述项目报价已将各分项目价格合成优惠，单个项目报价不能以此为标准。  8、此表不得涂改或擅自增加内容，如需涂改或增加内容，双方应当在相应处盖章方可生效。9、10、11、  ";
//		//System.out.println(s1);
		s1 = replaceStr(s1,"#",2);
		List list = processStringToList(s1,"#");
//		//System.out.println(list.toString());
		if(null != list && !list.isEmpty()){
			for(int i = 0 ; i <list.size(); i++){
				//System.out.println(list.get(i));
			}
		}
	}
	
	/**
	 * 拆分字符串成list
	 * @param strOld
	 * @param replacement	按提供的字符拆分
	 * @return
	 */
	public static List processStringToList(String strOld,String replacement){
		List list = new ArrayList();
		String[] sArr = null;
		if(null != strOld && !"".equals(strOld)){
			sArr = strOld.split(replacement);
		}
		if(null != sArr && sArr.length >0){
			for(int i = 0 ; i < sArr.length; i++){
				String temp = (String)sArr[i];
				if(!"".equals(temp.trim())){
					Map map = new HashMap();
					map.put("val", temp);
					list.add(map);
				}
			}
		}
		return list;
	}


	/**
	 * 将字符串中的(1、,2、,3、,4、,5、,....)替换成指定字符replacement，
	 * @param strOld		字符
	 * @param replacement	替换成指定字符
	 * @param starFrom		开始序号（如2,则从2、开始替换）
	 * @return
	 */
	public static String replaceStr(String strOld,String replacement,int starFrom){
		if(null != strOld && !"".equals(strOld)){
			int i = starFrom;
			int indexof = 0;
			String temp ="";
			boolean flag = true;
			while(flag){
				temp =	i+"、";
				indexof = strOld.indexOf(temp);
//				//System.out.println(temp+" indexof "+indexof);
				if(indexof == -1){
					flag = false;
				}else{
//					//System.out.println(temp);
					strOld=strOld.replaceFirst(temp, replacement+temp);
//					//System.out.println(s);
					temp ="";
				}
				i++;
			}
		}
		return strOld;
	}

}
