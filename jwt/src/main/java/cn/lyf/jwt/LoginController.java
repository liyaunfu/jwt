package cn.lyf.jwt;

import javax.servlet.http.HttpServletRequest;

/**
 * @date 2020/8/29 10:36
 */
public class LoginController {
    @Autowired
    private LoginService loginService;


    @GetMapping(value = "/login")
    public Json login(String customerId) {
        try {
            return Json.newInstance(loginService.login(customerId));
        } catch (Exception e) {
            log.error("登录失败，错误信息{}", e.getMessage());
            return Json.CODE_500;
        }
    }


    @GetMapping(value = "/findCustomerById")
    public Json findCustomerById(HttpServletRequest request) {
        try {
            String customerId = ((JwtClaims) request.getAttribute("claims")).get("id").toString();
            return Json.newInstance(loginService.findCustomerById(customerId));
        } catch (Exception e) {
            log.error("登录失败，错误信息{}", e.getMessage());
            return Json.CODE_500;
        }
    }

}
