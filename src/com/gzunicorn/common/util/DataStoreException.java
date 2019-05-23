/*
 * Created on 2005-7-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.util;

/**
 * Created on 2005-7-12
 * <p>Title:	数据存储异常</p>
 * <p>Description:	</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */
public class DataStoreException extends Exception {
    public DataStoreException() {
    }

    public DataStoreException(String msg) {
      super(msg);
    }

    public DataStoreException(Throwable cause) {
      super(cause);
    }

    public DataStoreException(String msg, Throwable cause) {
      super(msg, cause);
    }

}
