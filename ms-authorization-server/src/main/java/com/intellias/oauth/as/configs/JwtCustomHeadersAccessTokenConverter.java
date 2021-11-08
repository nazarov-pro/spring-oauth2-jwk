package com.intellias.oauth.as.configs;

import static com.intellias.oauth.as.configs.JwtAuthorizationServerConfiguration.INTELLIAS_JWK_KID;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
public class JwtCustomHeadersAccessTokenConverter extends JwtAccessTokenConverter {
    private final JsonParser objectMapper = JsonParserFactory.create();
    private final Map<String, KeyPair> jwkKeys;

    public JwtCustomHeadersAccessTokenConverter(final Map<String, KeyPair> jwkKeys) {
        super();
        this.jwkKeys = jwkKeys;
    }

    @Override
    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String content;
        try {
            content = this.objectMapper.formatMap(
                    getAccessTokenConverter().convertAccessToken(accessToken, authentication));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot convert access token to JSON", ex);
        }
        final var jwkKeyId = authentication.getOAuth2Request().getRequestParameters()
                .getOrDefault("client", INTELLIAS_JWK_KID);

        final var keyPair = jwkKeys.get(jwkKeyId);
        Objects.requireNonNull(keyPair);
        final var customHeaders = Map.of("kid", jwkKeyId);
        return JwtHelper.encode(content, new RsaSigner((RSAPrivateKey) keyPair.getPrivate()), customHeaders)
                .getEncoded();
    }
}
