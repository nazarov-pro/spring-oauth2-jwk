package com.intellias.oauth.as.configs;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class JwtAuthorizationServerConfiguration {
    private static final String INTELLIAS_KEY_STORE_FILE = "intellias-jwt.jks";
    private static final String INTELLIAS_KEY_STORE_PASSWORD = "intellias-pass";
    private static final String INTELLIAS_KEY_ALIAS = "intellias-oauth-jwt";
    public static final String INTELLIAS_JWK_KID = "intellias-key-id";

    private static final String ANDROID_KEY_STORE_FILE = "android-jwt.jks";
    private static final String ANDROID_KEY_STORE_PASSWORD = "android-store-pass";
    private static final String ANDROID_KEY_ALIAS = "android-client-jwt";
    public static final String ANDROID_JWK_KID = "android-key-id";

    public KeyPair intelliasKeyPair() {
        ClassPathResource ksFile = new ClassPathResource(INTELLIAS_KEY_STORE_FILE);
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, INTELLIAS_KEY_STORE_PASSWORD.toCharArray());
        return ksFactory.getKeyPair(INTELLIAS_KEY_ALIAS);
    }

    public KeyPair androidKeyPair() {
        ClassPathResource ksFile = new ClassPathResource(ANDROID_KEY_STORE_FILE);
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, ANDROID_KEY_STORE_PASSWORD.toCharArray());
        return ksFactory.getKeyPair(ANDROID_KEY_ALIAS);
    }

    @Bean
    public JWKSet jwkSet(final Map<String, KeyPair> jwkKeys) {
        return new JWKSet(
                jwkKeys.entrySet().stream().map(
                        key -> new RSAKey.Builder((RSAPublicKey) key.getValue().getPublic()).keyUse(KeyUse.SIGNATURE)
                                .algorithm(JWSAlgorithm.RS256)
                                .keyID(key.getKey()).build()
                ).collect(Collectors.toList())
        );
    }

    @Bean
    public Map<String, KeyPair> jwkKeys() {
        final var keyPairs = new HashMap<String, KeyPair>();
        keyPairs.put(INTELLIAS_JWK_KID, intelliasKeyPair());
        keyPairs.put(ANDROID_JWK_KID, androidKeyPair());
        return keyPairs;
    }
}
