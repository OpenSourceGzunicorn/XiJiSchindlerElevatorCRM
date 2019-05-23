package com.gzunicorn.common.logic;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class MakeUpXML {
	private String root = "root";

	private Hashtable ht = new Hashtable();

	private String version = "1.0";

	private String encoding = "GBK";

	private String header = "<?xml version='" + version + "' " + "encoding='"
			+ encoding + "'?>";

	public MakeUpXML(String root) {
		this.root = root;
	}

	// 设置每一列的内容。
	public void setCol(String rowid, String colname, String colvalue) {
		List list;
		// 判断是否存在此行，如果存在的话，把这写内容也追加到此list中去。
		if (ht.containsKey(rowid)) {
			// 对象中，存放的是引用。
			list = (List) ht.get(rowid);
			// 把这的新的对象添加到list对象中去。
			// 更新这个list里面的内容，实际上起操作的是同一个对象，故已经更新了其里面的内容。
			//这样的话，就这一个list里面放了很多个这样的值了。
			list.add(new ColBean(colname, colvalue));
		} else {
			// 产生一个新的list，然后把list的内容放到Hashtable中去的了。
			list = new ArrayList();
			// 返回的是一个对象。
			list.add(new ColBean(colname, colvalue));
			ht.put(rowid, list);
		}
	}

	public String getXml(String sort) {// 排序
		StringBuffer sb = new StringBuffer();
		List list;
		String key;
		Enumeration ele = ht.keys();
		sb.append(this.header);
		sb.append("<").append(root).append(">");
		int row = 0;
		while (ele.hasMoreElements()) {
			key = ele.nextElement().toString();
			list = (List) ht.get(key);
			if (list != null && list.size() > 0) {
				int len = list.size();
				sb.append("<rows id='").append(row++).append("' name='")
						.append(key).append("'>");
				for (int i = 0; i < len; i++) {
					ColBean cb = (ColBean) list.get(i);
					sb.append("<cols id='").append(i).append("' name='")
							.append(cb.colname).append("'>");
					sb.append(cb.colvale);
					sb.append("</cols>");
				}
				sb.append("</rows>");
			}
		}
		sb.append("</").append(root).append(">");
		return sb.toString();
	}

	public String getXml() {// 不排序
		StringBuffer sb = new StringBuffer();
		List list;
		String key;
		Enumeration ele = ht.keys();
		sb.append(this.header);
		sb.append("<").append(root).append(">");
		int row = 0;
		while (ele.hasMoreElements()) {
			key = ele.nextElement().toString();
			list = (List) ht.get(key);
			if (list != null && list.size() > 0) {
				int len = list.size();
				sb.append("<rows id='").append(row++).append("' name='")
						.append(key).append("'>");
				for (int i = 0; i < len; i++) {
					ColBean cb = (ColBean) list.get(i);
					sb.append("<cols id='").append(i).append("' name='")
							.append(cb.colname).append("'>");
					sb.append(cb.colvale);
					sb.append("</cols>");
				}
				sb.append("</rows>");
			}
		}
		sb.append("</").append(root).append(">");
		return sb.toString();
	}

	// 内部类
	public class ColBean {
		// 列名称
		private String colname;

		// 列值
		private String colvale;

		public ColBean(String cn, String cv) {
			this.colname = cn;
			this.colvale = cv;
		}

		public String getColname() {
			return colname;
		}

		public void setColname(String colname) {
			this.colname = colname;
		}

		public String getColvale() {
			return colvale;
		}

		public void setColvale(String colvale) {
			this.colvale = colvale;
		}

	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
		this.header = "<?xml version='" + this.version + "' " + "encoding='"
				+ encoding + "'?>";
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
		this.header = "<?xml version='" + version + "' " + "encoding='"
				+ this.encoding + "'?>";
	}

}
