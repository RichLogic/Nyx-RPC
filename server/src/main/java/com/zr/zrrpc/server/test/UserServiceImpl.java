package com.zr.zrrpc.server.test;

import com.zr.zpc.core.test.IUserService;
import com.zr.zrrpc.server.annotation.RpcService;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server.test</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/28
 */
@RpcService
public class UserServiceImpl implements IUserService {

    @Override
    public String getName() {
        return "Hello RCP!";
    }

    @Override
    public String getHa(String name, Integer skip, Integer limit) {
        return null;
    }
}
