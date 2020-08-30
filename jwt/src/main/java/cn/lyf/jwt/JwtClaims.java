package cn.lyf.jwt;

import java.util.HashMap;
import java.util.Objects;

/**
 * @date 2020/8/29 10:22
 */
public class JwtClaims extends HashMap {
    public JwtClaims() {
        this.put(ID, null);
        this.put(NAME, null);
        this.put(PHONE, null);
        this.put(FAILURETIME, null);
    }

    String ID = "id";
    String NAME = "name";
    String PHONE = "phone";

    String FAILURETIME = "failureTime";

    public JwtClaims put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this);
    }

}
