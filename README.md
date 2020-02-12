Java 版 Proxy Pool
--------
[![](https://img.shields.io/badge/language-Python-green.svg)](https://github.com/abadfox233/proxyPool)

### 项目介绍

* 测试版本  ![](https://img.shields.io/badge/java-8-green)  ![](https://img.shields.io/badge/spring--boot-2.x-green)
* 项目依赖
   * redis
   * java 8 及以上
   * java 爬虫 SemiCrawler
   * sping boot 

### 下载安装

1. 源代码下载
```shell
git clone https://github.com/abadfox233/proxyPool.git
```
2. 配置环境
```
# 配置统一在sping boot的application.properties中配置

# redission 配置(更多redis配置请查看sping redis stater相关配置)
spring.redis.database=1
spring.redis.host=127.0.0.1
spring.redis.port=6379

# 爬虫配置
seimi.crawler.enabled=true
seimi.crawler.names=proxy_crawler
seimi.crawler.enable-redisson-queue=false
spring.application.name=proxyPool

# 代理检测间隔 需符合cron 格式
proxy.check.cron = 0 0/2 * * * ?

# 获取代理间隔 需符合cron 格式
proxy.getter.cron = 0 0/5 * * * ?

```
3. 启动
```
同spring boot 相同的启动方式
```
### 使用
	启动后通过http://127.0.0.1:8080查看
*  默认接口

| api | method | Description | arg|
| ----| ---- | ---- | ----|
| / | GET | api介绍 | None |
| /get | GET | 随机获取一个代理 | None|
| /get_all | GET | 获取所有代理 |None|
| /status | GET | 查看当前代理数量 |None|

* 扩展代理

* 方法1：（使用默认通用爬虫下载网页解析代理）

  1.  首先在 [ProxyScheduleServiceImp]( https://github.com/abadfox233/proxyPool/blob/master/src/main/java/org/ning/proxypool/service/imp/ProxyScheduleServiceImp.java ) 添加 需要爬取的网址

  2.  然后在 [plugin]( https://github.com/abadfox233/proxyPool/tree/master/src/main/java/org/ning/proxypool/plugins ) 包中添加解析插件

     [示例代码]( https://github.com/abadfox233/proxyPool/blob/master/src/main/java/org/ning/proxypool/plugins/KuaiPlugin.java )

     ```java
     // 示例代码
     @Slf4j
     // 通用爬虫会把下载好的网页交由 网址符合 domain 正则表达式的插件进行处理
     @WebSitePlugin(domain = "(https|http)://www\\.kuaidaili\\.com.*")
     public class KuaiPlugin extends BasePlugin {
     
         @Autowired
         // 保存代理的服务
         ProxyService proxyService;
     
         @Override
         // 在通用爬虫处理逻辑前执行
         public void before(Response response) {
             log.debug("处理快代理");
             JXDocument doc = response.document();
             List<Object> nodes = doc.sel(".//table//tr[position()>1]");
             for(Object item: nodes){
                 if(item instanceof Element){
                     Element element = (Element)item;
                     String ip = element.select("td:eq(0)").text();
                     String port = element.select("td:eq(1)").text();
                     proxyService.saveProxy(new Proxy(ip+":"+port, "快代理",  Proxy.ProxyType.getType("http")), false, true);
                     log.info("get proxy {}:{} from 快代理", ip, port);
                 }
             }
         }
     
         @Override
         // 在通用爬虫处理逻辑后执行
         public void after(Response response) {
     
         }
     }
     ```

     3. 重启代码即可

* 方法2: 自定义爬虫
	
	1. 在 [cralwer]( https://github.com/abadfox233/proxyPool/tree/master/src/main/java/org/ning/proxypool/crawlers ) 包中添加自定义爬虫, 具体方法请查看[SemiCralwer]( http://wiki.seimicrawler.org/ )文档
	2. 重启代码即可

### 内置代理解析
- 框架内置代理解析插件
  - [FreeIP](http://www.freeip.top)
  
  - [无忧代理](http://www.goubanjia.com)
  
  - [IP海](http://www.iphai.com)
  
  - [快代理](http://www.kuaidaili.com)
  
  - [西刺代理](http://www.xicidaili.com)
  
  - [云代理](http://www.ip3366.net)
  
  - [全网代理IP](http://www.goubanjia.com/) 
  
### 致谢

* 本框架参考python 版本的 [proxy_pool](https://github.com/jhao104/proxy_pool)

* 本框架基于 [SemiCralwer](https://github.com/zhegexiaohuozi/SeimiCrawler)

### 问题反馈
	任何问题欢迎在 [Issues](https://github.com/abadfox233/proxyPool/issues) 中反馈