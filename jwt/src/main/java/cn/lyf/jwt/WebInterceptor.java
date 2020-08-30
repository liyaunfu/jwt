package cn.lyf.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @date 2020/8/29 10:38
 */
public class WebInterceptor implements HandlerInterceptor  {

    @Value("${jwt.secret-url}")
    private String jwtSecretUrl;

    @Value("${jwt.safety.secret}")
    private String jwtSafetySecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return noAccess403(response);
        } else {
            try {
                String token = authorization.substring(7).replaceAll(" ", "");

                if (StringUtils.isNotEmpty(token) && Jwts.safetyVerification(token, jwtSafetySecret)) {
                    JwtClaims jwtClaims = JSON.parseObject(Base64Utils.getFromBase64(token.split("\\.")[1]), JwtClaims.class);
                    request.setAttribute("claims", jwtClaims);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return noAccess(response);
            }
        }
        return false;
    }


    public boolean noAccess(HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setContentType("text/json; charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Json.newInstance(Apistatus.CODE_401)));
        return false;
    }


    public boolean noAccess403(HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setContentType("text/json; charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Json.newInstance(Apistatus.CODE_403)));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object
            o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse, Object o, Exception e) throws Exception {
    }

}
