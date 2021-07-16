package com.zr.zpc.core.test;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server.proxy</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/15
 */
public interface IUserService {

    String getName();

    String getHa(String name, Integer skip, Integer limit);

}
