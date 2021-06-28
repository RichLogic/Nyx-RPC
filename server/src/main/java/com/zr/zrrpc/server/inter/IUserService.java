package com.zr.zrrpc.server.inter;

import com.zr.zrrpc.server.RpcInterface;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server.proxy</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/15
 */
@RpcInterface
public interface IUserService{

    String getName(String uid);

}
