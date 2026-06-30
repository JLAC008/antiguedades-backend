package com.antiguedades.config;

import com.antiguedades.security.JwtAuthenticationFilter;
import com.antiguedades.security.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SecurityAuthorizationTest {
    private AnnotationConfigWebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.register(TestConfig.class);
        context.refresh();
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void userCannotAccessAdminRoutes() throws Exception {
        mockMvc.perform(get("/api/admin/probe").with(user("user").roles("user")))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessAdminRoutes() throws Exception {
        mockMvc.perform(get("/api/admin/probe").with(user("admin").roles("admin")))
            .andExpect(status().isOk());
    }

    @Test
    void superuserCanAccessAdminRoutes() throws Exception {
        mockMvc.perform(get("/api/admin/probe").with(user("root").roles("superuser")))
            .andExpect(status().isOk());
    }

    @Test
    void regularUserCannotWriteApplicationData() throws Exception {
        mockMvc.perform(post("/api/antiques").with(user("user").roles("user")))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanWriteApplicationData() throws Exception {
        mockMvc.perform(post("/api/antiques").with(user("admin").roles("admin")))
            .andExpect(status().isOk());
    }

    @Configuration
    @EnableWebMvc
    @Import(SecurityConfig.class)
    static class TestConfig {
        @Bean
        JwtTokenProvider jwtTokenProvider() {
            return mock(JwtTokenProvider.class);
        }

        @Bean
        JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
            return new JwtAuthenticationFilter(jwtTokenProvider, "AUTH_TOKEN");
        }

        @Bean
        ProbeController probeController() {
            return new ProbeController();
        }
    }

    @RestController
    static class ProbeController {
        @GetMapping("/api/admin/probe")
        String probe() {
            return "ok";
        }

        @PostMapping("/api/antiques")
        String writeProbe() {
            return "ok";
        }
    }
}
