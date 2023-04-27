package com.a2m.sso.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {

    public static String encrypt(String content, String publicKeyStr)
            throws Exception {
        PublicKey publicKey = decodePublicKey(publicKeyStr);
        byte[] contentBytes = content.getBytes();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherContent = cipher.doFinal(contentBytes);
        String encoded = Base64.getEncoder().encodeToString(cipherContent);
        return encoded;
    }

    public static String decrypt(String cipherContent, String privateKeyStr)
            throws Exception {
        PrivateKey privateKey = decodePrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cipherContentBytes = Base64.getDecoder().decode(cipherContent.getBytes());
        byte[] decryptedContent = cipher.doFinal(cipherContentBytes);
        String decoded = new String(decryptedContent);
        return decoded;
    }

    public static String encodeKey(Key key) {
        byte[] keyBytes = key.getEncoded();
        String encodedKeyStr = Base64.getUrlEncoder().encodeToString(keyBytes);
        return encodedKeyStr;
    }

    public static PublicKey decodePublicKey(String keyStr) throws Exception {
        byte[] keyBytes = Base64.getUrlDecoder().decode(keyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(spec);
        return key;
    }

    public static PrivateKey decodePrivateKey(String keyStr) throws Exception {
        byte[] keyBytes = Base64.getUrlDecoder().decode(keyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        return key;
    }

    public static Map<String, Object> genPublicKeyAndPrivateKey() throws Exception {
        SecureRandom sr = new SecureRandom();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048, sr);
        KeyPair keyPair = kpg.generateKeyPair();
        Key publicKey = keyPair.getPublic();
        Key privateKey = keyPair.getPrivate();
        Map<String, Object> result = new HashMap<>();
        result.put("publicKey", encodeKey(publicKey));
        result.put("privateKey", encodeKey(privateKey));
        return result;
    }

//    public static String getPublicKey() throws IOException {
//        ClassPathResource classPathResource = new ClassPathResource("public.key");
//        byte[] publicKeyBytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
//        return new String(publicKeyBytes);
//    }
//
//    public static String getPrivateKey() throws IOException {
//        ClassPathResource classPathResource = new ClassPathResource("private.key");
//        byte[] publicKeyBytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
//        return new String(publicKeyBytes);
//    }
}
