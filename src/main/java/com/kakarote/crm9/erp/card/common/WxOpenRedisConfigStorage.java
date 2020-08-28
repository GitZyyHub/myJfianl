package com.kakarote.crm9.erp.card.common;

import cn.binarywang.wx.miniapp.config.WxMaConfig;
import com.kakarote.crm9.common.config.redis.RedisManager;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.util.http.apache.ApacheHttpClientBuilder;
import me.chanjar.weixin.mp.bean.WxMpHostConfig;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.enums.TicketType;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.bean.WxOpenAuthorizerAccessToken;
import me.chanjar.weixin.open.bean.WxOpenComponentAccessToken;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WxOpenRedisConfigStorage implements WxOpenConfigStorage {
    private String componentAppId;
    private String componentAppSecret;
    private String componentToken;
    private String componentAesKey;

    private String httpProxyHost;
    private int httpProxyPort;
    private String httpProxyUsername;
    private String httpProxyPassword;
    private ApacheHttpClientBuilder apacheHttpClientBuilder;
    private String COMPONENT_VERIFY_TICKET_KEY="COMPONENT_VERIFY_TICKET_KEY:";
    private String COMPONENT_ACCESS_TOKEN="COMPONENT_ACCESS_TOKEN:";
    private String AUTHORIZER_REFRESH_TOKEN="AUTHORIZER_REFRESH_TOKEN:";
    private String AUTHORIZER_ACCESS_TOKEN="AUTHORIZER_ACCESS_TOKEN:";

    public WxOpenRedisConfigStorage() {

    }

    @Override
    public String getComponentAppId() {
        return componentAppId;
    }

    @Override
    public void setComponentAppId(String componentAppId) {
        this.componentAppId = componentAppId;
        COMPONENT_VERIFY_TICKET_KEY=COMPONENT_VERIFY_TICKET_KEY.concat(componentAppId);
        COMPONENT_ACCESS_TOKEN=COMPONENT_ACCESS_TOKEN.concat(componentAppId);
        AUTHORIZER_REFRESH_TOKEN=AUTHORIZER_REFRESH_TOKEN.concat(componentAppId);
        AUTHORIZER_ACCESS_TOKEN=AUTHORIZER_ACCESS_TOKEN.concat(componentAppId);
    }

    @Override
    public String getComponentAppSecret() {
        return componentAppSecret;
    }

    @Override
    public void setComponentAppSecret(String componentAppSecret) {
        this.componentAppSecret = componentAppSecret;
    }

    @Override
    public String getComponentToken() {
        return componentToken;
    }

    @Override
    public void setComponentToken(String componentToken) {
        this.componentToken = componentToken;
    }

    @Override
    public String getComponentAesKey() {
        return componentAesKey;
    }

    @Override
    public void setComponentAesKey(String componentAesKey) {
        this.componentAesKey = componentAesKey;
    }

    @Override
    public String getComponentVerifyTicket() {
        return RedisManager.getRedis().get(COMPONENT_VERIFY_TICKET_KEY);
    }

    @Override
    public void setComponentVerifyTicket(String componentVerifyTicket) {
        RedisManager.getRedis().set(COMPONENT_VERIFY_TICKET_KEY,componentVerifyTicket);
    }

    @Override
    public String getComponentAccessToken() {
        return RedisManager.getRedis().get(COMPONENT_ACCESS_TOKEN);
    }

    @Override
    public boolean isComponentAccessTokenExpired() {
        return RedisManager.getRedis().ttl(COMPONENT_ACCESS_TOKEN)<60L;
    }

    @Override
    public void expireComponentAccessToken() {
        RedisManager.getRedis().expire(COMPONENT_ACCESS_TOKEN,0);
    }

    @Override
    public void updateComponentAccessToken(WxOpenComponentAccessToken componentAccessToken) {
        updateComponentAccessToken(componentAccessToken.getComponentAccessToken(), componentAccessToken.getExpiresIn());
    }

    @Override
    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    @Override
    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    @Override
    public String getHttpProxyUsername() {
        return httpProxyUsername;
    }

    public void setHttpProxyUsername(String httpProxyUsername) {
        this.httpProxyUsername = httpProxyUsername;
    }

    @Override
    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    @Override
    public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
        return apacheHttpClientBuilder;
    }

    public ApacheHttpClientBuilder setApacheHttpClientBuilder(ApacheHttpClientBuilder apacheHttpClientBuilder) {
        return this.apacheHttpClientBuilder = apacheHttpClientBuilder;
    }

    @Override
    public WxMpConfigStorage getWxMpConfigStorage(String appId) {
        return new WxOpenInnerConfigStorage(this, appId);
    }

    @Override
    public WxMaConfig getWxMaConfig(String appId) {
        return new WxOpenInnerConfigStorage(this, appId);
    }

    @Override
    public void updateComponentAccessToken(String componentAccessToken, int expiresInSeconds) {
        RedisManager.getRedis().setex(COMPONENT_ACCESS_TOKEN, expiresInSeconds - 200, componentAccessToken);
    }

    @Override
    public boolean autoRefreshToken() {
        return true;
    }

    @Override
    public String getAuthorizerRefreshToken(String appId) {
        return null;// AdminCompanyMiniapp.queryAppByAppId(appId).getAuthorizerRefreshToken();
    }

    @Override
    public void setAuthorizerRefreshToken(String appId, String authorizerRefreshToken) {
      // AdminCompanyMiniapp.setAppByAppId(appId,authorizerRefreshToken);
    }

    @Override
    public String getAuthorizerAccessToken(String appId) {
        return RedisManager.getRedis().get(AUTHORIZER_ACCESS_TOKEN.concat(appId));
    }


    @Override
    public boolean isAuthorizerAccessTokenExpired(String appId) {
        return RedisManager.getRedis().ttl(AUTHORIZER_ACCESS_TOKEN.concat(appId))<60L;
    }

    @Override
    public void expireAuthorizerAccessToken(String appId) {
        RedisManager.getRedis().expire(AUTHORIZER_ACCESS_TOKEN.concat(appId),0);
    }

    @Override
    public void updateAuthorizerAccessToken(String appId, WxOpenAuthorizerAccessToken authorizerAccessToken) {
        updateAuthorizerAccessToken(appId, authorizerAccessToken.getAuthorizerAccessToken(), authorizerAccessToken.getExpiresIn());
    }

    @Override
    public void updateAuthorizerAccessToken(String appId, String authorizerAccessToken, int expiresInSeconds) {
        RedisManager.getRedis().setex(AUTHORIZER_ACCESS_TOKEN.concat(appId), expiresInSeconds - 200, authorizerAccessToken);
    }

    @Override
    public String getJsapiTicket(String appId) {
        return null;
    }

    @Override
    public boolean isJsapiTicketExpired(String appId) {
        return false;
    }

    @Override
    public void expireJsapiTicket(String appId) {
    }

    @Override
    public void updateJsapiTicket(String appId, String jsapiTicket, int expiresInSeconds) {
    }

    @Override
    public String getCardApiTicket(String appId) {
        return null;
    }

    @Override
    public boolean isCardApiTicketExpired(String appId) {
        return false;
    }

    @Override
    public void expireCardApiTicket(String appId) {
    }

    @Override
    public void updateCardApiTicket(String appId, String cardApiTicket, int expiresInSeconds) {
    }


    private static class WxOpenInnerConfigStorage implements WxMpConfigStorage, WxMaConfig {
        private WxOpenConfigStorage wxOpenConfigStorage;
        private String appId;
        private Lock accessTokenLock = new ReentrantLock();
        private Lock jsapiTicketLock = new ReentrantLock();
        private Lock cardApiTicketLock = new ReentrantLock();

        private WxOpenInnerConfigStorage(WxOpenConfigStorage wxOpenConfigStorage, String appId) {
            this.wxOpenConfigStorage = wxOpenConfigStorage;
            this.appId = appId;
        }

        @Override
        public String getAccessToken() {
            return wxOpenConfigStorage.getAuthorizerAccessToken(appId);
        }

        @Override
        public Lock getAccessTokenLock() {
            return this.accessTokenLock;
        }

        @Override
        public boolean isAccessTokenExpired() {
            return wxOpenConfigStorage.isAuthorizerAccessTokenExpired(appId);
        }

        @Override
        public synchronized void updateAccessToken(WxAccessToken accessToken) {
            updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
        }

        @Override
        public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
            wxOpenConfigStorage.updateAuthorizerAccessToken(appId, accessToken, expiresInSeconds);
        }

        @Override
        public String getTicket(TicketType type) {
            switch (type) {
                case JSAPI: {
                    return wxOpenConfigStorage.getJsapiTicket(appId);
                }
                case WX_CARD: {
                    return wxOpenConfigStorage.getCardApiTicket(appId);
                }
                default: {
                    // do nothing
                }
            }
            return null;
        }

        @Override
        public Lock getTicketLock(TicketType type) {
            switch (type) {
                case JSAPI: {
                    return this.jsapiTicketLock;
                }
                case WX_CARD: {
                    return this.cardApiTicketLock;
                }
                default: {
                    // do nothing
                }
            }
            return null;
        }

        @Override
        public boolean isTicketExpired(TicketType type) {
            switch (type) {
                case JSAPI: {
                    return wxOpenConfigStorage.isJsapiTicketExpired(appId);
                }
                case WX_CARD: {
                    return wxOpenConfigStorage.isCardApiTicketExpired(appId);
                }
                default: {
                    // do nothing
                }
            }

            return false;
        }

        @Override
        public void expireTicket(TicketType type) {
            switch (type) {
                case JSAPI: {
                    wxOpenConfigStorage.expireJsapiTicket(appId);
                    break;
                }
                case WX_CARD: {
                    wxOpenConfigStorage.expireCardApiTicket(appId);
                    break;
                }
                default: {
                    // do nothing
                }
            }
        }

        @Override
        public void updateTicket(TicketType type, String ticket, int expiresInSeconds) {
            switch (type) {
                case JSAPI: {
                    wxOpenConfigStorage.updateJsapiTicket(appId, ticket, expiresInSeconds);
                    break;
                }
                case WX_CARD: {
                    wxOpenConfigStorage.updateCardApiTicket(appId, ticket, expiresInSeconds);
                    break;
                }
                default: {
                    // do nothing
                }
            }

        }

        @Override
        public String getAppid() {
            return this.appId;
        }

        @Override
        public void expireAccessToken() {
            wxOpenConfigStorage.expireAuthorizerAccessToken(appId);
        }

        @Override
        public String getJsapiTicket() {
            return wxOpenConfigStorage.getJsapiTicket(appId);
        }

        @Override
        public Lock getJsapiTicketLock() {
            return this.jsapiTicketLock;
        }

        @Override
        public boolean isJsapiTicketExpired() {
            return wxOpenConfigStorage.isJsapiTicketExpired(appId);
        }

        @Override
        public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
            wxOpenConfigStorage.updateJsapiTicket(appId, jsapiTicket, expiresInSeconds);
        }

        @Override
        public void expireJsapiTicket() {
            wxOpenConfigStorage.expireJsapiTicket(appId);
        }

        @Override
        public String getCardApiTicket() {
            return wxOpenConfigStorage.getCardApiTicket(appId);
        }

        @Override
        public Lock getCardApiTicketLock() {
            return this.cardApiTicketLock;
        }

        @Override
        public boolean isCardApiTicketExpired() {
            return wxOpenConfigStorage.isCardApiTicketExpired(appId);
        }

        @Override
        public synchronized void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
            wxOpenConfigStorage.updateCardApiTicket(appId, cardApiTicket, expiresInSeconds);
        }

        @Override
        public void expireCardApiTicket() {
            wxOpenConfigStorage.expireCardApiTicket(appId);
        }

        @Override
        public String getAppId() {
            return this.appId;
        }

        @Override
        public String getSecret() {
            return null;
        }

        @Override
        public String getToken() {
            return wxOpenConfigStorage.getComponentToken();
        }

        @Override
        public String getTemplateId() {
            return null;
        }

        @Override
        public long getExpiresTime() {
            return 0;
        }


        @Override
        public String getAesKey() {
            return wxOpenConfigStorage.getComponentAesKey();
        }

        @Override
        public String getMsgDataFormat() {
            return null;
        }

        @Override
        public String getOauth2redirectUri() {
            return null;
        }

        @Override
        public String getHttpProxyHost() {
            return this.wxOpenConfigStorage.getHttpProxyHost();
        }

        @Override
        public int getHttpProxyPort() {
            return this.wxOpenConfigStorage.getHttpProxyPort();
        }

        @Override
        public String getHttpProxyUsername() {
            return this.wxOpenConfigStorage.getHttpProxyUsername();
        }

        @Override
        public String getHttpProxyPassword() {
            return this.wxOpenConfigStorage.getHttpProxyPassword();
        }

        @Override
        public String toString() {
            return WxOpenGsonBuilder.create().toJson(this);
        }

        @Override
        public File getTmpDirFile() {
            return null;
        }

        @Override
        public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
            return wxOpenConfigStorage.getApacheHttpClientBuilder();
        }

        @Override
        public boolean autoRefreshToken() {
            return true;
        }

        @Override
        public WxMpHostConfig getHostConfig() {
            return null;
        }
    }
}
