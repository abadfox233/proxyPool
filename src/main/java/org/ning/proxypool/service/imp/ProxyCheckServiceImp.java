package org.ning.proxypool.service.imp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.ning.proxypool.domain.Proxy;
import org.ning.proxypool.http.OKhttpClientBuilder;
import org.ning.proxypool.service.ProxyCheckService;
import org.ning.proxypool.service.ProxyService;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

/**
 * @author NingWang
 * 2020-02-11 20:41
 * 校验代理
 */
@Slf4j
@Service
public class ProxyCheckServiceImp implements ProxyCheckService {

    @Autowired
    ProxyService proxyService;
    private final Request checkRequest = new Request.Builder().url("http://www.baidu.com")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
            .build();

    /**
     * 代理校验
     *
     * @param proxySet 代理集合
     * @param check    是否已校验
     */
    @Override
    @Async("checkExecutor")
    public void checkProxy(Set<Proxy> proxySet, boolean check) {
        for(Proxy proxy:proxySet){
            boolean successFlag = true;
            OkHttpClient client = OKhttpClientBuilder.getClientFromProxy(proxy);
            if(client == null){
                successFlag = false;
            }
            else {
                try{
                    Response response =  client.newCall(checkRequest).execute();
                    successFlag = checkSuccess(response);
                }catch (IOException e){
                    successFlag = false;
                }
            }
            processProxy(proxy, successFlag, check);
        }
    }

    /**
     * 检测代理是否成功
     * @param response 访问指定连接的结果
     * @return 代理是否可用
     */
    private boolean checkSuccess(Response response){
        boolean flag = true;
        try{
            if(response.isSuccessful()&&response.body()!=null){
                JXDocument doc = JXDocument.create(response.body().string());
                if(!"百度一下，你就知道".equals(doc.selOne("//title/text()"))){
                    flag = false;
                }
            }else{
                flag = false;
            }
            response.close();

        }catch (Exception e){
            flag = false;
            try{
                response.close();
            }catch (Exception ignore){

            }
        }
        return flag;

    }

    /**
     * 处理验证验证结果
     * @param proxy 代理
     * @param success 验证是否成功
     * @param check 代理队列
     */
    private void processProxy(Proxy proxy, boolean success, boolean check){
        if(success){
            if(!check){
                // 处理未校验队列
                proxyService.removeProxy(proxy, false);
            }else {
                // 处理已校验队列
                proxyService.removeProxy(proxy, true);
            }
            proxy.setCheckCount(proxy.getCheckCount()+1);
            proxy.setLastTimestamp((System.currentTimeMillis()));
            proxy.setLastStatus(true);
            proxyService.saveProxy(proxy, true, false);
        }else {
            if(!check){
                // 处理未校验队列
                proxyService.removeProxy(proxy, false);
            }else {
                proxyService.removeProxy(proxy, true);
                proxy.setCheckCount(proxy.getCheckCount()+1);
                proxy.setFailCount(proxy.getFailCount()+1);
                proxy.setLastTimestamp((System.currentTimeMillis()));
                proxy.setLastStatus(true);
                if((double)proxy.getFailCount()/proxy.getCheckCount()<0.5){
                    proxyService.saveProxy(proxy, true, false);
                }
            }
        }

    }
}
