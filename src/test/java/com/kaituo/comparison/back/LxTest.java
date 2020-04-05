package com.kaituo.comparison.back;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.util.HttpClientUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class LxTest {

    private static final String APP_ID         = "20200330404450598087602176496473";
    private static final String DEVICE_KEY         = "4fcac9f3ec43453da26f52bc7254a219";
    private static final String APP_PRIVATE_KEY    = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCigGky9clyBlVyBF7FW1hXlpS8uxpZUgz+UqeXVyUYSg+O/bE2v793RbB2CDdr4wfL/OnmMEl5Kr8FQFtCPHZmmCD2BsyyY4tadiZMlS74inEatapmnvmesaRgZwiJKF5Mt2iD8nS1SXXVJrC2lASkduCrrqtTZzRz0vLUukdpe/KT7E36o0ghtOh2zBND6VGDEyQcI6CQJGs4tg/D6SeEUEhp/opYx3npEvCV5y9MlZNyCKHdKzOBKTgpuDfwPoWoay9v77mxkSkeyFx67y92aZa7grCJegNUB3A7hgGnCrIpWBD+dttEqmf3EceNzYrIWIbWKouSrmV1jHKBu+y3AgMBAAECggEAf1HFGtxgnSpBQDgUWOUc7mNY3hZqqpkS/SCdloH82m8MiahTdOX/id/cRl5hrfIPZdAfIu+qYXkQeXjXB6p/yhpfmOPy9cHwqUvw7gfk6W7u91Jm1Eiyo5fjhnSzx2MNn2YESX1p27BW1k6eHi8TBBjwJxOPwKUX/7fD8tCQqx/hzDzxadJiZxg0ZmPeW9gyDZfAWbFzC8yYdr6LloC6npaaQsoX+0TlrXJ5SU4af4TyvAIzpUQqqgoW0a4cG2nilN2j0YBVaxnxNJzJVcnG6pKdgBABloLuYrbogLulB73QQatCKxB9et6B/5/1IFjZgrAWqiemJYV1adYdNWZngQKBgQDxyooaMAOtDIO7gOxnzwViSqccBGIIGaO4MckJsnIWjV/ra7f3HhtaZEz+W7e0AqT066i/p7McvNhJqQzWMZ81uvxeks1QIQNhwCxR5lsPd8TzE5QpS2oEbnDJAIz6o9MdaGyq3vGXPV91cTcr0ib3YhxWzOx+054oXH6oWYjsRwKBgQCsDQ3l0rN8uSAMM8DlHINYG7dIzGzMBxEPiv00jHKxe+iPwnJlBBXz1Uy5Eted10ETTXRThiMI/uIRXO2Z+XZXNnDTnsEE8DhxpQQcWytDusqmJ8ZQ93vhHVxdmt/zeNziHND54mdoyRKbGtp8JI68Q3e1dY7GYkYMZvN4dKPkEQKBgQDAeaTaF+K1PMMY3hZSxL/acOb6LJsFK7Piiu8oYv7i6QO0WnmFpFfN6w5HZSelin1Z4sbgWlUAyWu7p3DqiXHdyc3gUyhRYxZYGCyvvZo7g/IqNim5fakVgf0SZPfVBdinDluC4hqbdGWrDccHgTE3uMgqaNLSWw8JvietSCjxPQKBgQCpp3W82jgDFMl1PtNHGQarKylgr7PYvF8mZHeqCZRPT0/T8nm7AnxjJEGzfdD/4/JXhljew7ZoeG2TkX76BfObrCM1iaXrqLT/2yhijcPr1ZVeonn7/Q46vSkHZGDRRK0Kij/HKGmv+hZOu/wVzjaxjhXM6U8QcHeCY0JR1TKHAQKBgEMpXIwCytXuxMtDHe1p06jPMD0FVEZAwGp2PfJiIbflKlpGb6K6G0RNUCsGAzl0dUdvWozPoRHb4xDD9gMfsaFECZfsoK4F4lNGwNU+MsfyEcizjfUTyX0k8ckrrsuUjCviUDVnRJpclC0dwF0JbyD2RPM191NeTY5b0XxAKvD8";
    private static final String APP_PUBLIC_KEY     = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAooBpMvXJcgZVcgRexVtYV5aUvLsaWVIM/lKnl1clGEoPjv2xNr+/d0Wwdgg3a+MHy/zp5jBJeSq/BUBbQjx2Zpgg9gbMsmOLWnYmTJUu+IpxGrWqZp75nrGkYGcIiSheTLdog/J0tUl11SawtpQEpHbgq66rU2c0c9Ly1LpHaXvyk+xN+qNIIbTodswTQ+lRgxMkHCOgkCRrOLYPw+knhFBIaf6KWMd56RLwlecvTJWTcgih3SszgSk4Kbg38D6FqGsvb++5sZEpHshceu8vdmmWu4KwiXoDVAdwO4YBpwqyKVgQ/nbbRKpn9xHHjc2KyFiG1iqLkq5ldYxygbvstwIDAQAB";
    //private static final String TOKEN              = "a624780b5a4f9ab5";
    private static final String TOKEN              = "bcb2b16d279b3205";
    //f666b9c6cac13bd2

    private static final String MAP_KEY_APP_ID = "app_id";
    private static final String MAP_KEY_DEVICE_KEY = "device_key";
    private static final String MAP_KEY_TIMESTAMP  = "timestamp";
    private static final String MAP_KEY_TOKEN      = "token";
    private static final String MAP_KEY_SIGN       = "sign";

    public static void main(String[] args) throws Exception {
        TreeMap<String, String> map = new TreeMap<>();

        map.put(MAP_KEY_APP_ID, APP_ID);
        map.put(MAP_KEY_DEVICE_KEY, DEVICE_KEY);
        map.put(MAP_KEY_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        map.put(MAP_KEY_TOKEN, TOKEN);
        String sign = getSign(map, APP_PRIVATE_KEY);
      //  System.out.println(sign);
        map.put(MAP_KEY_SIGN,sign);
        StringBuffer content = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            content.append(entry.getValue()).append("|");
        }
        String signContent = content.substring(0, content.length()-1);
        System.out.println("sign");
        System.out.println(sign);
    //    boolean result = rsaCheckContent(signContent, sign, APP_PUBLIC_KEY, "UTF-8");
      //  System.out.println("result~~~" + result);
        System.out.println("map");
        System.out.println(map);
        JSONObject jsonObject = HttpClientUtil.doPost("https://dtest-property.51yzone.com/shared/xikanma/callbackgateway"
                , JSON.parseObject(JSON.toJSONString(map)));
        System.out.println("result");
        System.out.println(jsonObject);
    }

    private static String getSign(TreeMap<String, String> map, String privateKey) {

        if (privateKey==null || privateKey.equals("")) {
            return "";
        }

        StringBuffer content = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            content.append(entry.getValue()).append("|");
        }

        String signContent = content.substring(0, content.length()-1);

        String sign = rsaContentSign(signContent, APP_PRIVATE_KEY, "UTF-8");

        return sign;
    }

    private static String rsaContentSign(String signContent, String privateKey, String charset) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));

            java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");

            signature.initSign(priKey);

            if (charset==null || charset.equals("")) {
                signature.update(signContent.getBytes());
            } else {
                signature.update(signContent.getBytes(charset));
            }

            byte[] signed = signature.sign();

            return new String(Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException ie) {
            throw new RuntimeException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
        } catch (Exception e) {
            throw new RuntimeException("RSAcontent = " + signContent + "; charset = " + charset, e);
        }
    }

    private static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || algorithm==null || algorithm.equals("")) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = StreamUtil.readText(ins).getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    private static boolean rsaCheckContent(String content, String sign, String publicKey, String charset) {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));

            java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");

            signature.initVerify(pubKey);

            if (charset==null || charset.equals("")) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
        }
    }

    private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        StringWriter writer = new StringWriter();
        StreamUtil.io(new InputStreamReader(ins), writer);

        byte[] encodedKey = writer.toString().getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }



}
