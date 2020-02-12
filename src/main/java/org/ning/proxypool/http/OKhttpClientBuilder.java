package org.ning.proxypool.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.ning.proxypool.domain.Proxy;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * @author NingWang
 * 2020-02-11 20:10
 */
@Slf4j
@Component
public class OKhttpClientBuilder {

    private static OkHttpClient.Builder hcBuilder = null;
    static {
        hcBuilder = new OkHttpClient.Builder();
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    x509TrustManager
            };
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            hcBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager)
                    .hostnameVerifier(hostnameVerifier);
        }catch (Exception e){
            log.error("ssl init fail.err={}",e.getMessage(),e);
        }
    }

    static public OkHttpClient getClientFromProxy(Proxy proxy){
         java.net.Proxy stdProxy = proxy.getStdProxy();
         hcBuilder.callTimeout(20*1000, TimeUnit.MILLISECONDS);
         if(stdProxy!=null){
             return hcBuilder.proxy(stdProxy).build();
         }
        return null;
    }

}
