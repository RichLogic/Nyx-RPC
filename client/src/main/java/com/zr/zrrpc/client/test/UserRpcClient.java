package com.zr.zrrpc.client.test;

import com.zr.zrrpc.client.annotation.ZrrpcClient;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.test</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/26
 */
@ZrrpcClient("demo-service")
public interface UserRpcClient extends IUserService {

}