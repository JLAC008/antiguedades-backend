package com.antiguedades.security;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesTokenFromHttpOnlyCookie() throws Exception {
        JwtTokenProvider tokenProvider = mock(JwtTokenProvider.class);
        when(tokenProvider.validateToken("signed-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("signed-token")).thenReturn("admin@example.com");
        when(tokenProvider.getRoleFromToken("signed-token")).thenReturn("admin");
        when(tokenProvider.getUserIdFromToken("signed-token")).thenReturn("user-id");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenProvider, "AUTH_TOKEN");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/admin/users");
        request.setCookies(new Cookie("AUTH_TOKEN", "signed-token"));

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
            .extracting(Object::toString)
            .containsExactly("ROLE_admin");
    }
}
