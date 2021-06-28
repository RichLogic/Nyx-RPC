package com.zr.zpc.core.model;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/28
 */
public class RpcResult<T> {

    private Object result;
    private String exceptionStackInfo;

    public Object getResult() {
        return result;
    }

    public RpcResult<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public String getExceptionStackInfo() {
        return exceptionStackInfo;
    }

    public RpcResult<T> setExceptionStackInfo(String exceptionStackInfo) {
        this.exceptionStackInfo = exceptionStackInfo;
        return this;
    }
}
