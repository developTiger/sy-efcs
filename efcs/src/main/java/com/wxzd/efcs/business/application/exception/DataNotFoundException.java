package com.wxzd.efcs.business.application.exception;

import com.wxzd.protocol.ProtocolError;
import com.wxzd.protocol.ProtocolException;

/**
 * @author Leon Regulus on 2017/4/21.
 * @version 1.0
 * @since 1.0
 */
public class DataNotFoundException extends ProtocolException {

    public DataNotFoundException() {
        super(ProtocolError.Unknown, "数据不存在");
    }

    public DataNotFoundException(String message) {
        super(ProtocolError.Unknown, message);
    }
}
