package cn.lyf.jwt;

import cn.lyf.jwt.*;
import cn.lyf.jwt.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @date 2020/8/29 10:43
 */
public class LoginService {
    @Value("${jwt.safety.secret}")
    private String jwtSafetySecret;

    @Value("${jwt.valid.time}")
    private int jwtValidTime;


    public String login(String customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("jiayao");
        customer.setPhone("1234567890");
        return createTokenString(customer);
    }


    public Customer findCustomerById(String customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("liyuanfu");
        customer.setPhone("1234567890");
        return customer;
    }


    public String createTokenString(Customer customer) {
        String jwtToken = null;
        try {
            jwtToken = Jwts.header(Header.SM4, jwtSafetySecret)
                    .payload(new JwtClaims()
                            .put("id", customer.getId())
                            .put("name", customer.getName())
                            .put("phone", customer.getPhone())
                            .put("failureTime", FailureTimeUtils.creatValidTime(FailureTime.DAY, jwtValidTime))
                            .put("mytest", "我的个性属性"))
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jwtToken.replaceAll("\r\n","");
    }

}
