package cn.lyf.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @date 2020/8/29 10:24
 */
public class Jwts extends HashMap {
    private static Jwts jwts;

    static {
        jwts = new Jwts();
    }


    private final String jwtSafetySecret = "0dcac1b6ec8843488fbe90e166617e34";



    public static Jwts header(Header header, String jwtSafetySecret) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", header);
        map.put("jwtSafetySecret", jwtSafetySecret);
        jwts.put("header", map);
        return jwts;
    }


    public Jwts payload(JwtClaims jwtClaims) {
        jwts.put("payload", jwtClaims);
        return jwts;
    }


    public String compact() throws Exception {

        HashMap<String, Object> headerObj = (HashMap<String, Object>) jwts.get("header");

        JwtClaims jwtClaims = (JwtClaims) jwts.get("payload");
        jwtClaims.put("uuid", UUID.randomUUID());

        Object jwtSafetySecretObj = headerObj.get("jwtSafetySecret");

        headerObj.remove("jwtSafetySecret");
        String jwtSafetySecret = jwtSafetySecretObj == null ? this.jwtSafetySecret : jwtSafetySecretObj.toString();
        Object code = headerObj.get("code");
        String encryptionType = code == null ? "AES" : code.toString();

        String signature = dataSignature(headerObj, jwtClaims, encryptionType, jwtSafetySecret);

        String token = Base64Utils.getBase64(JSONObject.toJSONString(headerObj)) + "."
                + Base64Utils.getBase64(JSONObject.toJSONString(jwtClaims)) + "."
                + signature;
        System.out.println("生成的token为:" + token);
        return token;
    }

    private static String dataSignature(HashMap<String, Object> headerObj, JwtClaims jwtClaims, String encryptionType, String jwtSafetySecret) throws Exception {
        String dataSignature = null;
        if (encryptionType.equals(Header.AES.name())) {
            dataSignature = AESUtils.encrypt(JSONObject.toJSONString(headerObj) + JSONObject.toJSONString(jwtClaims), jwtSafetySecret);
        } else if (encryptionType.equals(Header.SM3.name())) {
            dataSignature = SM3Cipher.sm3Digest(JSONObject.toJSONString(headerObj) + JSONObject.toJSONString(jwtClaims), jwtSafetySecret);
        } else if (encryptionType.equals(Header.SM4.name())) {
            dataSignature = new SM4Util().encode(JSONObject.toJSONString(headerObj) + JSONObject.toJSONString(jwtClaims), jwtSafetySecret);
        }
        return dataSignature;
    }

    public static Boolean safetyVerification(String tokenString, String jwtSafetySecret) throws Exception {

        String[] split = tokenString.split("\\.");
        if (split.length != 3) {
            throw new RuntimeException("无效的token");
        }

        HashMap<String, Object> obj = JSON.parseObject(Base64Utils.getFromBase64(split[0]), HashMap.class);

        JwtClaims jwtClaims = JSON.parseObject(Base64Utils.getFromBase64(split[1]), JwtClaims.class);

        String signature = split[2];


        if (jwtClaims.get("failureTime") != null) {
            Date failureTime = (Date) jwtClaims.get("failureTime");
            int i = failureTime.compareTo(new Date());
            if (i > 0) {
                throw new RuntimeException("此token已过有效期");
            }
        }


        Object code = obj.get("code");
        String encryptionType = code == null ? "AES" : code.toString();

        String signatureNew = dataSignature(obj, jwtClaims, encryptionType, jwtSafetySecret);
        return signature.equals(signatureNew.replaceAll("\r\n","")) ? true : false;
    }

}
