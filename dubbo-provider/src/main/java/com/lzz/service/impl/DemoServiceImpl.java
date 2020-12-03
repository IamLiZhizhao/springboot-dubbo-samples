package com.lzz.service.impl;

import com.lzz.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@DubboService
public class DemoServiceImpl implements DemoService {

    public String sayHello(String name) {
        System.out.println("[成功调用] Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return String.format("Hello, %s", name);
    }

}