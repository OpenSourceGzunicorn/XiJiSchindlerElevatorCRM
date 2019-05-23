/*
 * Created on 2005-8-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.dataimport;

/**
 * Created on 2005-7-12
 * <p>Title:	iSCM系统</p>
 * <p>Description:	获取ImportBarcodeDAO中异常并进行处理</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */
public class ImportException extends Exception{

      public ImportException() {
      }

      public ImportException(String msg) {
        super(msg);
      }

      public ImportException(Throwable cause) {
        super(cause);
      }


      public ImportException(String msg, Throwable cause) {
        super(msg, cause);
      }

}
