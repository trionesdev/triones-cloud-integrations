package com.moensun.cloud.integration.huaweicloud.sms;

import com.moensun.cloud.integration.api.sms.SmsException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class HuaweiCloudSmsClient {

    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";


    private final String appKey;
    private final String appSecret;
    private SSLSocketFactory sslSocketFactory;
    private final OkHttpClient okHttpClient;

    public HuaweiCloudSmsClient(String appKey,String appSecret){
        this.appKey = appKey;
        this.appSecret = appSecret;
        X509TrustManager trustManager = trustManager();
        try{
            this.sslSocketFactory = sslSocketFactory(trustManager);
        }catch (Exception ex){
            log.error(ex.getMessage(),ex);
        }
        this.okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(this.sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
    }


    public HuaweiCloudSmsResponse request(HuaweiCloudSmsRequest smsRequest){
        String regionId = StringUtils.isNotBlank(smsRequest.getRegionId())?smsRequest.getRegionId():"cn-south-1";
        String url = "https://smsapi."+regionId+".myhuaweicloud.com:443/sms/batchSendSms/v1"; //APP接入地址(在控制台"应用管理"页面获取)+接口访问URI

        String body = buildRequestBody(smsRequest.getSender(), smsRequest.getReceiver(), smsRequest.getTemplateId(), smsRequest.getTemplateParams(),
                smsRequest.getStatusCallBack(), smsRequest.getSignature());
        if (null == body || body.isEmpty()) {
            throw new SmsException("body is null.");
        }

        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(appKey, appSecret);
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            throw new SmsException("wsse header is null.");
        }

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(body, MediaType.parse("application/x-www-form-urlencoded")))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", AUTH_HEADER_VALUE)
                .addHeader("X-WSSE", wsseHeader)
                .build();

        try(Response response = this.okHttpClient.newCall(request).execute()){
            return HuaweiCloudSmsResponse.builder().build();
        }catch (Exception ex){
            throw new SmsException(ex);
        }

    }



    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallBack, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            log.error("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        Map<String, String> map = new HashMap<>();

        map.put("from", sender);
        map.put("to", receiver);
        map.put("templateId", templateId);
        if (null != templateParas && !templateParas.isEmpty()) {
            map.put("templateParas", templateParas);
        }
        if (null != statusCallBack && !statusCallBack.isEmpty()) {
            map.put("statusCallback", statusCallBack);
        }
        if (null != signature && !signature.isEmpty()) {
            map.put("signature", signature);
        }

        StringBuilder sb = new StringBuilder();
        String temp = "";

        for (String s : map.keySet()) {
            try {
                temp = URLEncoder.encode(map.get(s), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(),e);
            }
            sb.append(s).append("=").append(temp).append("&");
        }

        return sb.deleteCharAt(sb.length()-1).toString();
    }

    static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            log.error("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce

        MessageDigest md;
        byte[] passwordDigest = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update((nonce + time + appSecret).getBytes());
            passwordDigest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(),e);
        }

        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(passwordDigest); //PasswordDigest
        //如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
        //String passwordDigestBase64Str = Base64.encodeBase64String(passwordDigest); //PasswordDigest
        //若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
        //passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

    static X509TrustManager trustManager(){
        return new  X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return;
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return;
            }
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
    }

    static SSLSocketFactory sslSocketFactory(X509TrustManager trustManager) throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] { trustManager};
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        return sc.getSocketFactory();
    }
}
