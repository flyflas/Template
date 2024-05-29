package icu.xiaobai.librarydemo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    private final String key = """
            -----BEGIN EC PRIVATE KEY-----
            MHcCAQEEIOpKmCheso2KVCN90ti0eP2hE9jNOjFSGllLcAP0E49soAoGCCqGSM49
            AwEHoUQDQgAE+a0bDoemW6UJq+qQAmZglQlk+nd2ZJrNz0NRQlWsofvAzTwgfbQM
            iuNtTYUK3i8wId7bqj1Mn+zq38Jxo/YXFQ==
            -----END EC PRIVATE KEY-----
            """;

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Algorithm algorithm;
    @Value("${jwt.issuer:manager@xiaobai.icu}")
    private String issuer;
    @Value("${jwt.expire-time:1}")
    private Integer expireTime;

    public JwtUtil() {
        // 解析私钥
        PEMParser pemParser = new PEMParser(new StringReader(key));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

        Object keyPairObj = null;
        KeyPair keyPair = null;

        try {
            keyPairObj = pemParser.readObject();
            PEMKeyPair pemKeyPair = (PEMKeyPair) keyPairObj;
            keyPair = converter.getKeyPair(pemKeyPair);
            // 获取私钥和公钥
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            algorithm = Algorithm.ECDSA256((ECPublicKey) publicKey, (ECPrivateKey) privateKey);
        } catch (IOException e) {
            log.error("JWT密钥转化错误!!!", e);
        }
    }

    /**
     * JWT 生成函数
     *
     * @param email 用户的邮箱（用户的唯一标识）
     * @return 生成的jwt字符串
     */
    public String createJWT(String email) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, expireTime);
        Date expireDate = calendar.getTime();

        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withNotBefore(new Date())
                .withExpiresAt(expireDate)
                .withClaim("email", email)
                .sign(algorithm);
    }

    /**
     * jwt 验证函数
     * @param jwtString 需要验证的jwt函数
     * @return DecodedJWT 解密之后的字符串对象
     */
    public DecodedJWT jwtVerify(String jwtString) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) publicKey, (ECPrivateKey) privateKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .acceptLeeway(10)
                .withIssuer(issuer)
                .withClaimPresence("email")
                .build();

        return verifier.verify(jwtString);
    }


}
