package com.kakarote.crm9.erp.card.common;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import com.kakarote.crm9.common.config.redis.RedisManager;
import com.kakarote.crm9.utils.HttpUtils;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.util.http.apache.ApacheHttpClientBuilder;

import java.util.concurrent.locks.Lock;

public class WxMiNiAppOpenRedisConfigStorage  implements WxMaConfig {

    private volatile String appid;
    private volatile String secret;

    private String MINI_APP_ACCESSTOKEN_KEY="MINI_APP_ACCESSTOKEN_KEY:";

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Override
    public String getAccessToken() {
        String accessToken =  RedisManager.getRedis().get(MINI_APP_ACCESSTOKEN_KEY);
        if (accessToken == null){
            String url = String.format(WxMaService.GET_ACCESS_TOKEN_URL, appid, secret);
                String sbf = HttpUtils.httpURLConnectionGET(url,null);
                WxAccessToken wxAccessToken = WxAccessToken.fromJson(sbf);
                System.out.println("返回来的报文："+sbf);
                updateAccessToken(wxAccessToken.getAccessToken(), 60*60);
                return wxAccessToken.getAccessToken();
        }
        return accessToken;
    }

    @Override
    public Lock getAccessTokenLock() {
        return null;
    }

    @Override
    public boolean isAccessTokenExpired() {
        return false;
    }

    @Override
    public void expireAccessToken() {

    }

    @Override
    public void updateAccessToken(WxAccessToken accessToken) {

    }

    @Override
    public void updateAccessToken(String accessToken, int expiresInSeconds) {
        RedisManager.getRedis().setex(MINI_APP_ACCESSTOKEN_KEY,expiresInSeconds,accessToken);
    }

    @Override
    public String getJsapiTicket() {
        return null;
    }

    @Override
    public Lock getJsapiTicketLock() {
        return null;
    }

    @Override
    public boolean isJsapiTicketExpired() {
        return false;
    }

    @Override
    public void expireJsapiTicket() {

    }

    @Override
    public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {

    }

    @Override
    public String getCardApiTicket() {
        return null;
    }

    @Override
    public Lock getCardApiTicketLock() {
        return null;
    }

    @Override
    public boolean isCardApiTicketExpired() {
        return false;
    }

    @Override
    public void expireCardApiTicket() {

    }

    @Override
    public void updateCardApiTicket(String apiTicket, int expiresInSeconds) {

    }

    @Override
    public String getAppid() {
        return appid;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getAesKey() {
        return null;
    }

    @Override
    public String getMsgDataFormat() {
        return null;
    }

    @Override
    public long getExpiresTime() {
        return 0;
    }

    @Override
    public String getHttpProxyHost() {
        return null;
    }

    @Override
    public int getHttpProxyPort() {
        return 0;
    }

    @Override
    public String getHttpProxyUsername() {
        return null;
    }

    @Override
    public String getHttpProxyPassword() {
        return null;
    }

    @Override
    public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
        return null;
    }

    @Override
    public boolean autoRefreshToken() {
        return false;
    }
}
