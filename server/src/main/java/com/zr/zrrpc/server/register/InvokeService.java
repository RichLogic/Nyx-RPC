package com.zr.zrrpc.server.register;

import com.zr.zpc.core.exception.RpcException;
import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zpc.core.model.RpcResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/28
 */
@Service
public class InvokeService {

    @Resource
    private RpcServiceRegistrar registrar;

    public RpcResult invoke(RpcInvoker invoker) {
        try {
            Object result = this.handle(invoker);
            return new RpcResult<>().setResult(result);
        } catch (RpcException e) {
            return new RpcResult<>().setExceptionStackInfo(Arrays.toString(e.getStackTrace()));
        }
    }

    private Object handle(RpcInvoker invoker) throws RpcException {
        Object service = registrar.getService(invoker.getInterfaceName());

        Object result;
        try {
            result = service.getClass().getMethod(invoker.getMethod(), invoker.getParamClasses()).invoke(service, invoker.getParams());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RpcException("反射方法执行异常");
        }
        if (result.getClass() != invoker.getReturnType()) {
            throw new RpcException("实际返回值与目标返回值不一致");
        }

        return result;
    }

}
