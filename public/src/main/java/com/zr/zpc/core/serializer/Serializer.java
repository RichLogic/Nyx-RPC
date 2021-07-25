package com.zr.zpc.core.serializer;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zpc.core.serializer</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/07/15
 */
public abstract class Serializer {
    public abstract <T> byte[] serializer(T obj);
    public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);
}