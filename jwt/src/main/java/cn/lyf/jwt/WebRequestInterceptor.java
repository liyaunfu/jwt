package cn.lyf.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @date 2020/8/29 10:41
 */
public class WebRequestInterceptor extends WebMvcConfigurerAdapter{
    private WebInterceptor webInterceptor;

    /**
     * 需要JWT拦截的Url
     */
    @Value("${jwt.secret-url}")
    private String jwtSecretUrl;
    /**
     * 需要JWT拦截的Url
     */
    @Value("${jwt.noSecret-url}")
    private String jwtNoSecretUrl;
    /**
     * JWT密钥
     */
    @Value("${jwt.safety.secret}")
    private String jwtSafetySecret;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        jwtSecretUrl = jwtSecretUrl.replaceAll(" ", "");
        jwtNoSecretUrl = jwtNoSecretUrl.replaceAll(" ", "");
        registry.addInterceptor(webInterceptor).addPathPatterns(jwtSecretUrl.split(",")).excludePathPatterns(jwtNoSecretUrl.split(","));
    }

}
