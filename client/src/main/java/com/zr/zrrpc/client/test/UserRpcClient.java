package com.zr.zrrpc.client.test;

import com.zr.zpc.core.test.IUserService;
import com.zr.zrrpc.client.annotation.RpcClient;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.test</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/26
 */
@RpcClient("demo-service")
public interface UserRpcClient extends IUserService {

}