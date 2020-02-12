package org.ning.proxypool.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NingWang
 * 2020-02-11 12:58
 */
@Data
@Slf4j
@ToString
public class Proxy {

    public static enum ProxyType {
        HTTP("http"),
        HTTPS("https"),
        SOCKET("socket");

        private String type;

        ProxyType(String type) {
            this.type = type;
        }

        public static ProxyType getType(String value){
            for(ProxyType item:values()){
                if(value.toLowerCase().equals(item.type)){
                    return item;
                }
            }
            throw new  RuntimeException("no such ProxyType");
        }
        public java.net.Proxy.Type getStdType() {
            if(type.equals("http")||type.equals("https"))
                return java.net.Proxy.Type.HTTP;
            else
                return java.net.Proxy.Type.SOCKS;
        }



    }

    static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 代理 ip:port
     */
    private String proxyStr="";

    /**
     * 检测失败次数
     */
    private int failCount = 0;

    /**
     * 来源
     */
    private String from="NULL";

    /**
     * 代理类型 http/https/socket
     */
    private ProxyType type=ProxyType.HTTP;

    /**
     * 检测次数
     */
    private int checkCount = 0;

    /**
     * 最后一次检测结果
     */
    private boolean lastStatus = true;

    /**
     * 最后一次检测时间戳
     */
    private long lastTimestamp = 0L;

    public String getLastTimeStr(){
       return df.format(new Date(lastTimestamp));
    }

    /**
     * 获取java 标准Proxy 用于检测
     * @return 标准代理类
     */
    public java.net.Proxy getStdProxy(){
        if(type!=null&& !StringUtils.isEmpty(proxyStr)){
            String[] pies = proxyStr.split(":");
            int port = Integer.parseInt(pies[1]);
            String host = pies[0];
            return new java.net.Proxy(type.getStdType(), new InetSocketAddress(host, port));
        }
        return null;
    }
    public Proxy(){}

    public Proxy(String proxyStr, String from, ProxyType type) {
        this.proxyStr = proxyStr;
        this.from = from;
        this.type = type;
    }

    public Map<String, Object> convert2Map(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("proxy", proxyStr);
        result.put("check count", checkCount);
        result.put("fail count", failCount);
        result.put("from", from);
        result.put("last check time", getLastTimeStr());
        result.put("last Status", lastStatus);
        result.put("proxy type", type.type);
        return result;
    }
}
