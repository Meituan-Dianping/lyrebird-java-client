# lyrebird-java-client

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.meituan-dianping.lyrebird.sdk/lyrebird-java-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.appium/java-client)
[![javadoc](https://javadoc.io/badge2/com.github.meituan-dianping.lyrebird.sdk/lyrebird-java-client/javadoc.svg)](https://javadoc.io/doc/com.github.meituan-dianping.lyrebird.sdk/lyrebird-java-client)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/Meituan-Dianping/lyrebird-java-client.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Meituan-Dianping/lyrebird-java-client/alerts/)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Meituan-Dianping/lyrebird-java-client.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Meituan-Dianping/lyrebird-java-client/context:java)

---

- [简介](#简介)
- [快速开始](#快速开始)
  - [环境要求](#环境要求)
  - [安装](#安装)
- [使用](#使用)
  - [设置 Lyrebird Client](#设置-Lyrebird-Client)
  - [获取 Lyrebird Status](#获取-Lyrebird-Status)
  - [Mock 数据激活](#Mock-数据激活)
    - [使用 groupID 手动激活](#使用-groupID-手动激活)
    - [使用注解方式自动激活](#使用注解方式自动激活)
      - [TestNG](#TestNG)
      - [Junit4](#Junit4)
    - [取消激活](#取消激活)
    - [读取Mock数据](#读取Mock数据)
  - [获取网络数据信息](#获取网络数据信息)
    - [获取 flow List](#获取-flow-List)
    - [获取 flow ID](#获取-flow-ID)
    - [获取请求持续时长](#获取请求持续时长)
    - [获取请求开始时间](#获取请求开始时间)
    - [获取 flow 数据的详细信息](#获取-flow-数据的详细信息)
    - [获取请求对象](#获取请求对象)
    - [获取返回对象](#获取返回对象)
    - [清空 Flow 数据](#清空-Flow-数据)
  - [获取指定频道的消息总线数据](#获取指定频道的消息总线数据)
  - [获取Socket对象进行事件监听](#获取Socket对象进行事件监听)
- [应用场景](#应用场景)
  - [在UI自动化中校验请求参数是否符合预期](#在UI自动化中校验请求参数是否符合预期)
  - [在UI自动化中校验返回与客户端展示是否一致](#在UI自动化中校验返回与客户端展示是否一致)

## 简介

lyrebird-java-client 是[Lyrebird](https://github.com/Meituan-Dianping/lyrebird)的一个 Java SDK，通过调用Lyrebird本身提供的[API](https://meituan-dianping.github.io/lyrebird/guide/api.html)实现在Java项目中控制 Lyrebird Services。比如：激活Mock数据；实时查看、验证网络数据等。

## 快速开始

### 环境要求

- Java 1.8
- Junit 4 or TestNG 6.14.x

### 安装

- Maven项目添加如下依赖到 pom.xml 文件中

```xml
<dependency>
  <groupId>com.meituan.lyrebird.sdk</groupId>
  <artifactId>lyrebird-java-client</artifactId>
  <version>RELEASE</version>
</dependency>
```

## 使用

### 设置 Lyrebird Client

- 默认 Lyrebird 端口地址 (9090)

```java
Lyrebird lyrebird = new Lyrebird();
```

- 指定 Lyrebird 端口地址

```java
Lyrebird lyrebird = new Lyrebird("http://<lyrebird-ip>:<lyrebird-port>");
```

### 获取 Lyrebird Status

```java
Lyrebird lyrebird = new Lyrebird();
Status status = lyrebird.status();

// 获取当前服务MOCK端口
int mockPort = status.getMockPort();

// 获取当前服务代理端口
int proxyPort = status.getPorxyPort();

// 获取当前服务IP
String lyrebirdIP = status.getIp();
```

### Mock 数据激活

#### 使用 groupID 手动激活

> groupID: 89e0426c-9cf9-454a-bbe0-94246fc23b04

```java
Lyrebird lyrebird = new Lyrebird();

lyrebird.activate("89e0426c-9cf9-454a-bbe0-94246fc23b04");
```

#### 使用注解方式自动激活

在测试类或测试方法上声明 MockData 注解并设置 groupID 和 groupName

```java
@MockData(groupID = "89e0426c-9cf9-454a-bbe0-94246fc23b04", groupName = "首页")
public class TestClass {
    ...
}


@MockData(groupID = "89e0426c-9cf9-454a-bbe0-94246fc23b04", groupName = "首页")
@Test
public void testMethod() {
    ...
}
```

如果 Lyrebird 启动在特定的域名端口下，需要在测试方法执行前设置 Lyrebird 服务地址

```java
// import here

public class DemoCase {
    @BeforeMethod
    public void setup() {
        // Lyrebird server that start on local port 8082
        Lyrebird.setRemoteAddress("http://localhost:8082");
        ...
    }
}
```

#### TestNG

设置监听器

- 方法一：修改 testng.xml

```xml
<suite name="TestNGSample">
<listeners>
    <listener class-name="com.meituan.lyrebird.client.listeners.TestNGListener" />
</listeners>
<test name="Test Demo">
    <classes>
        <class name="tests.SampleTest" />
    </classes>
</test>
</suite>
```

- 方法二：源码中直接添加

```java
import com.meituan.lyrebird.client.listeners.TestNGListener;
import com.meituan.lyrebird.client.MockData;
...


@MockData(groupID = "89e0426c-9cf9-454a-bbe0-94246fc23b04", groupName = "首页")
@Listeners(TestNGListener.class)
public class TestClass {
    ...
}
```

#### Junit4

设置监听器

- 方法一：修改 pom.xml

```xml
<build>
    <plugins>
        [...]
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <properties>
                    <property>
                        <name>listener</name>
                        <value>com.meituan.lyrebird.client.events.Junit4Listener</value>
                    </property>
                </properties>
            </configuration>
        </plugin>
        [...]
</build>
```

- 方法二：源码中直接添加

```java
import org.junit.runner.RunWith;
import com.meituan.lyrebird.client.events.Junit4Runner;
import com.meituan.lyrebird.client.MockData;

@MockData(groupID = "89e0426c-9cf9-454a-bbe0-94246fc23b04", groupName = "首页")
@RunWith(Junit4Runner.class)
public class TestClass {
    ...
}
```

#### 取消激活

```java
Lyrebird lyrebird = new Lyrebird();

lyrebird.deactivate();
```

#### 读取Mock数据

调用 getMockData(String dataId) 方法可以获取该数据组的 Mock 数据详情
```java
Lyrebird lyrebird = new Lyrebird();

LBMockData lbMockData = lyrebird.getMockData("cfa0c589-8ef0-4885-b4f4-b9688c5af0d5");
// 读取Mock数据名
String mockDataName = lbMockData.getName();
// 读取Mock Response (注意：Lyrebird服务端返回的是一个 Json 字符串，并不是一个 Json 对象)
String mockDataResponse = lbMockData.getResponseData();
```

### 获取网络数据信息

flow 示例

```javascript
[
    {
        "id": "b193416d-f89c-435f-b158-4e47911cf98b",
        "size": 350,
        "duration": 0.21155691146850586,
        "start_time": 1566805862.385838,
        "request": {
            "url": "https://lyrebird.java.client.com/api/example",
            "path": "/api/example",
            "host": "lyrebird.java.client.com",
            "method": "POST"
        },
        "response": {
            "code": 200,
            "mock": "proxy"
        }
    }
]
```

flow detail 示例

```javascript
{
    "id": "b193416d-f89c-435f-b158-4e47911cf98b",
    "duration": 0.21155691146850586,
    "start_time": 1566805862.385838,
    "request": {
        "headers": {
            "Accept-Encoding": "gzip",
            "Userid": "-1",
            "Content-Type": "application/x-www-form-urlencoded",
            "Content-Length": "2942",
            "Host": "127.0.0.1:9090",
            "Connection": "Keep-Alive"
        },
        "method": "POST",
        "query": {
            "name": "tester",
            "city": "beijing"
        },
        "url": "https://lyrebird.java.client.com/api/example",
        "host": "lyrebird.java.client.com",
        "path": "/api/example",
        "data": {
            "countryCode": "86"
        }
    },
    "response": {
        "code": 200,
        "headers": {
            "Date": "Mon, 26 Aug 2019 07:51:02 GMT",
            "Content-Type": "application/json;charset=utf-8",
            "Content-Length": "761",
            "Connection": "keep-alive"
        },
        "timestamp": 1566805862.6,
        "data": {
            "user": {
                "age": 18,
                "username": "lyrebird-java-client"
            }
        }
    }
}
```

Flow 类属性

| 属性名       | 说明                         |
| :---------- | :-------------------------- |
| `id`        | 描述 flow data 的唯一 ID 标识 |
| `duration`  | 客户端发起请求的持续时长        |
| `startTime` | 客户端发起请求的时间戳         |
| `request`   | 客户端请求服务端的请求Java对象  |
| `response`  | 远端服务返回的响应报文Java对象  |

#### 获取 flow List

> Flow 数据保存为一个 List，单个 flow data 数据详见上面的示例

```java
Lyrebird lyrebird = new Lyrebird();

Flow[] flowList = lyrebird.getFlowList();
```

#### 获取 flow ID

```java
String flowId = flowList[0].getId();
```

#### 获取请求持续时长

```java
double duration = flowList[0].getDuration();
```

#### 获取请求开始时间

```java
double startTime = flowList[0].getStartTime();
```

#### 获取 flow 数据的详细信息

> 默认 flow 中包含的网络数据是概要信息，可以通过 flow id 获取网络数据详细信息

```java
FlowDetail flowDetail = lyrebird.getFlowDetail(flowId);
```

#### 获取请求对象

```java
Request request = flowDetail.getRequest();
```

#### 获取返回对象

```java
Response response = flowDetail.getResponse();
```

#### 清空 Flow 数据

```java
Lyrebird lyrebird = new Lyrebird();

// 清空 flow 列表
lyrebird.clearFlowList();
```

### 获取指定频道的消息总线数据

接口文档[点击这里](https://meituan-dianping.github.io/lyrebird/guide/api.html#获取已激活mock数据)

```java
// 获取 page 频道对应的消息总线数据
EventDetail[] eventList = lyrebird.getEventList("page");

// 获取该频道第0个事件的事件ID
String eventID = eventList[0].getEventID();

// 获取该频道第0个事件的内容
String content = eventList[0].getContent();
```

### 获取Socket对象进行事件监听

使用[socket.io client](https://github.com/clwillingham/java-socket.io.client)监听Lyrebird socket事件消息

```java
// 获取 socket.io client
Socket socket = lyrebird.getSocketInstance();

// 事件监听
socket.on("action", new Emitter.Listener() {

    @Override
    public void call(Object... args) {
        // do something here...
    }
});

// 建立连接
socket.connect();

// 关闭连接
socket.disconnect();
```

## 应用场景

在UI自动化中，可将移动设备通过代理的方式将请求数据接入Lyrebird，[操作指南](https://github.com/Meituan-Dianping/lyrebird#连接移动设备)，在测试用例中通过调用Lyrebird API来校验网络请求参数是否符合预期。

Lyrebird Java SDK 分别提供 Request, Response 类描述客户端发起的请求和响应数据

Request 类

| 属性名     | 说明             |
| :-------- | :-------------- |
| `headers` | 客户端请求报文头部 |
| `method`  | 客户端HTTP请求方法 |
| `query`   | query参数        |
| `url`     | 客户端请求url     |
| `host`    | 客户端请求host    |
| `path`    | 客户端请求path    |
| `data`    | from-data参数    |

Response 类

| 属性名     | 说明                 |
| :-------- | :------------------ |
| `code`    | HTTP 状态码          |
| `headers` | 服务端返回响应报文头部  |
| `data`    | 服务端返回响应报文主体  |

### 在UI自动化中校验请求参数是否符合预期

```java
// 实例化 Lyrebird 对象
Lyrebird lyrebird = new Lyrebird();

// 获取客户端发送的所有 Flow 数据保存为一个 List
Flow[] flowList = lyrebird.getFlowList();

// 遍历 flow list 中的每一条 flow 数据
for (Flow flow : flowList) {
    // 取出 flow data 的 url 信息
    String url = flow.getRequest().getUrl();
    // 断言 http://lyrebird.java.client.com/api/example 接口的客户端请求内容
    if (url.equals("http://lyrebird.java.client.com/api/example")) {
        // 通过 flow id 查询该条 flow 数据的详细信息
        FlowDetail flowDetail = lyrebird.getFlowDetail(flow.getId());
        // 断言请求 headers 中 Content-Type 等于 application/x-www-form-urlencoded
        assertEquals("application/x-www-form-urlencoded", flowDetail.getRequest().getHeaders().get("Content-Type"));
        // 断言请求方法是 POST
        assertEquals("POST", flowDetail.getRequest().getMethod());
        // 断言请求 query 中 name 等于 tester
        assertEquals("tester", flowDetail.getRequest().getQuery().get("name"));
        // 断言请求 form-data 中 countryCode 等于 86
        assertEquals("86", flowDetail.getRequest().getData().get("countryCode"));
    }
}
```

### 在UI自动化中校验返回与客户端展示是否一致

```java
// 实例化 Lyrebird 对象
Lyrebird lyrebird = new Lyrebird();

// 获取客户端发送的所有 Flow 数据保存为一个 List
Flow[] flowList = lyrebird.getFlowList();

// 遍历 flow list 中的每一条 flow 数据
for (Flow flow : flowList) {
    // 取出 flow data 的 url 信息
    String url = flow.getRequest().getUrl();
    // 断言 http://lyrebird.java.client.com/api/example 接口的客户端请求内容
    if (url.equals("http://lyrebird.java.client.com/api/example")) {
        // 通过 flow id 查询该条 flow 数据的详细信息
        FlowDetail flowDetail = lyrebird.getFlowDetail(flow.getId());
        // 断言 HTTP 状态码为 200
        assertEquals("200", flowDetail.getResponse().getCode());
        // 断言返回 headers 中 Content-Type 等于 application/json;charset=utf-8
        assertEquals("application/json;charset=utf-8", flowDetail.getResponse().getHeaders().get("Content-Type"));
        /*
         * 断言 data 中 countryCode 等于 86
         * getData() 参数 JsonPath 相关使用方法及说明，详见官方 README: https://github.com/json-path/JsonPath/edit/master/README.md  
         */
        assertEquals(18, flowDetail.getResponse().getData("$.user.age"));
    }
}
```
