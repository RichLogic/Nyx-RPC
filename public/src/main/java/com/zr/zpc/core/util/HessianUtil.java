package com.zr.zpc.core.util;

import com.alibaba.com.caucho.hessian.io.*;
import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zpc.core.test.IUserService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zpc.core.util</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/07/16
 */
public class HessianUtil {

    /**
     * 序列化
     *
     * @param obj
     * @return byte[]
     */
    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        AbstractHessianOutput out = new Hessian2Output(os);

        out.setSerializerFactory(new SerializerFactory());
        try {
            out.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                out.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return os.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] bytes) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        AbstractHessianInput in = new Hessian2Input(is);

        in.setSerializerFactory(new SerializerFactory());
        T value;
        try {
            value = (T) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static void main(String[] args) {
        RpcInvoker invoker = new RpcInvoker()
                .setRpcInterface(IUserService.class)
                .setServerName("demo-service");

        byte[] buf = HessianUtil.serialize(invoker);
        RpcInvoker inv = HessianUtil.deserialize(buf);
        System.out.println(inv);
    }
}
