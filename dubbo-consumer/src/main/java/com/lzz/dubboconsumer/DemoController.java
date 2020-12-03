package com.lzz.dubboconsumer;

import com.lzz.service.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务消费
 *
 * @author lizhizhao
 * @since 2020-12-02 17:06
 */
@RestController
public class DemoController {

    @DubboReference
    private DemoService demoService;

    @RequestMapping("/hello")
    public String hello() {
        String result = demoService.sayHello("lizhizhao");
        System.out.println(result);
        return result;
    }

}
