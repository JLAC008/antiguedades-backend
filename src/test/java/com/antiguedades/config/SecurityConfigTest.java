package com.antiguedades.config;

import com.antiguedades.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    @Test
    void allowsOnlyConfiguredOrigins() {
        CorsConfiguration config = corsConfiguration("http://localhost:4200, https://frontend.example");

        assertThat(config.checkOrigin("http://localhost:4200")).isEqualTo("http://localhost:4200");
        assertThat(config.checkOrigin("https://frontend.example")).isEqualTo("https://frontend.example");
        assertThat(config.checkOrigin("https://evil.example")).isNull();
    }

    @Test
    void emptySettingRejectsCrossOriginRequests() {
        CorsConfiguration config = corsConfiguration("  ");

        assertThat(config.getAllowedOrigins()).isEmpty();
        assertThat(config.checkOrigin("https://evil.example")).isNull();
    }

    private CorsConfiguration corsConfiguration(String origins) {
        SecurityConfig securityConfig = new SecurityConfig(mock(JwtAuthenticationFilter.class), origins);
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(new MockHttpServletRequest("GET", "/api/antiques"));
        assertThat(config).isNotNull();
        return config;
    }
}
