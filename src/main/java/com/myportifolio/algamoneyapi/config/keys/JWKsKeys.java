package com.myportifolio.algamoneyapi.config.keys;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Component;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class JWKsKeys {
    public static RSAKey generate(){
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            var keyPair = keyPairGenerator.generateKeyPair();

            var publicKey = (RSAPublicKey) keyPair.getPublic();
            var privateKey = (RSAPrivateKey) keyPair.getPrivate();

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem generating RSA Keys",e);
        }

    }
}
