package com.kakarote.crm9.common.config.druid;
import com.jfinal.plugin.druid.IDruidStatViewAuth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author zhangzhiwei
 * 配置druid的进入规则，暂定为只要登录就可查看
 */
public class DruidConfig implements IDruidStatViewAuth {
    @Override
    public boolean isPermitted(HttpServletRequest request) {
        Cookie[] cookies=request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("tql")){
                return Objects.equals("c94525f62b7341d28c398c9795e8f81b",cookie.getValue());
            }
        }
        return false;
    }
}
