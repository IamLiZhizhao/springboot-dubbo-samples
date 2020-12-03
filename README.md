# springboot-dubbo-samples
springboot整合dubbo示例

使用SpringBoot+Dubbo 搭建一个简单的分布式服务

**一、环境安装搭建**

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\6adb80b71dca409b888ef3dc2e70648f\clipboard.png)

首先提供程序的运行环境。自行下载并安装JDK、IDE、Maven、Zookeeper。

**二、实现服务接口 dubbo-interface**

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\58005423d0694d11a2cb8fadc959dbb2\clipboard.png)

主要分为下面几步：

1. 创建 Maven 项目;
2. 创建接口类
3. 将项目打成 jar 包供其他项目使用

dubbo-interface 后面被打成 jar 包，它的作用只是提供接口。

1、创建 Maven 项目

File->New->Module... ,然后选择 Maven类型的项目，其他的按照提示一步一步走就好。

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\dd67c1f11e4845d0a4954771ac19f89f\clipboard.png)

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\e3e1ba2274ff492abf3ced10e2fbe8c8\clipboard.png)

2.创建接口类

package com.lzz.service; public interface DemoService {    String sayHello(String name); }

\3. 将项目打成 jar 包供其他项目使用

点击右边的 Maven Projects 然后选择 install ，这样 jar 包就打好了。

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\f6144112ebe34b1ca6ccb7970dfe5ab3\clipboard.png)

三、实现服务提供者 dubbo-provider

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\f24c239b1d804784aef73570b12aea47\clipboard.png)

主要分为下面几步：

1. 创建 springboot 项目;
2. 在pom文件引入 dubbo 、zookeeper以及接口的相关依赖 jar 包；
3. 在 application.properties 配置文件中配置 dubbo 相关信息；
4. 实现接口类;
5. 服务提供者启动类编写

1.创建项目流程同dubbo-interface类似，这里就不赘述了

2.在pom文件引入 dubbo 、zookeeper以及接口的相关依赖 jar 包

```xml
<!-- 引入dubbo-interface的依赖 -->
<dependency>
    <groupId>com.lzz</groupId>
    <artifactId>dubbo-interface</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

<!-- 引入dubbo的依赖 -->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.8</version>
</dependency>

<!-- 引入zookeeper的依赖 -->
<dependency>
    <groupId>com.101tec</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.5</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.8.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.8.0</version>
</dependency>
```

注意将本项目和 dubbo-interface 项目的 dependency 依赖的 groupId 和 artifactId 改成自己的。以上的jar包都可以在Maven仓库https://mvnrepository.com/中找到。

例如：

dubbo 整合spring boot 的 jar 包搜索dubbo-spring-boot-starter。

zookeeper 的 jar包搜索 zkclient 。

3. 在 application.properties 配置文件中配置 dubbo 相关信息

```properties
# 应用服务 WEB 访问端口
server.port=6060

## 这个相当于在启动类加上注解@DubboComponentScan("com.lzz.service.impl")
dubbo.scan.base-packages=com.lzz.service.impl

## 配置Dubbo协议
dubbo.protocol.name=dubbo
dubbo.protocol.port=6666

## 配置Dubbo注册中心
dubbo.registry.address=zookeeper://127.0.0.1:2181
```

4. 实现接口

```java
package com.lzz.service.impl;

import com.lzz.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@DubboService
public class DemoServiceImpl implements DemoService {

    public String sayHello(String name) {
        System.out.println("[成功调用] Hello " + name + 
        ", request from consumer: " + 
        RpcContext.getContext().getRemoteAddress());
        return String.format("Hello, %s", name);
    }

}
```

5. 服务提供者启动类编写

如果有在 application.properties文件中配置了dubbo.scan.base-packages的值，那么这里的@DubboComponentScan就可以不用了

```java
@SpringBootApplication
@DubboComponentScan("com.lzz.service.impl")
public class DubboProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboProviderApplication.class, args);
    }
}
```

四、实现服务消费者 dubbo-consumer

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\a429a90f365248e384703a2de4439b3d\clipboard.png)

主要分为下面几步：

1. 创建 springboot 项目;
2. 加入 dubbo 、zookeeper以及接口的相关依赖 jar 包；
3. 在 application.properties 配置文件中配置 dubbo 相关信息；
4. 编写测试类;
5. 服务消费者启动类编写
6. 测试效果

第1，2步和服务提供者的一样，这里直接从第 3 步开始。

3. 在 application.properties 配置文件中配置 dubbo 相关信息

这里跟服务提供者不同的是，服务消费者只需要配置同样的注册中心即可

```properties
# 应用服务 WEB 访问端口
server.port=7070
# 应用名称
spring.application.name=dubbo-consumer

## Dubbo Registry
dubbo.registry.address=zookeeper://127.0.0.1:2181
```

4.编写一个简单 Controller 调用远程服务

```java
package com.lzz.dubboconsumer;

import com.lzz.service.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
```

5. 服务消费者启动类编写

```java
package com.lzz.dubboconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DubboConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class, args);
    }
}
```

6. 测试效果

运行程序之前需要先启动 zk的服务器，直接点击zookeeper的bin文件夹下的zkServer.cmd即可。

浏览器访问 http://localhost:7070/hello，即可观察页面和服务端、客户端的控制台输出。

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\5c710770410a48c8b8688f90d0222f7b\clipboard.png)

客户端：

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\ec8f0d431f7c4fd787e48933fa0fba3b\clipboard.png)

服务端：

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\abcda6a2048a4c1983e9a396617789e4\clipboard.png)

PS：以上项目的创建也可以通过阿里提供的Java 工程脚手架生成：

https://start.aliyun.com/bootstrap.html

![img](E:\学习\weixinobU7VjmjxthekHoIUfmi1zruRodc\33f0799f79a44035aeb3ae7fd53bb724\clipboard.png)
