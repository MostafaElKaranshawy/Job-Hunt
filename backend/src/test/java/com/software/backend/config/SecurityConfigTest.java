//package com.software.backend.config;
//
//import com.software.backend.filter.JwtAuthFilter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import static org.mockito.Mockito.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class SecurityConfigTest {
//
//    @Mock
//    private JwtAuthFilter jwtAuthFilter;
//
//    @Mock
//    private AuthenticationProvider authenticationProvider;
//
//    @Mock
//    private ApplicationContext applicationContext;
//
//    @InjectMocks
//    private SecurityConfig securityConfig;
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize mocks and inject them into SecurityConfig
//    }
//
//    @Test
//    public void testSecurityFilterChain() throws Exception {
//        // Arrange: Mock the HttpSecurity object
//        HttpSecurity httpSecurity = mock(HttpSecurity.class);
//        when(httpSecurity.csrf()).thenReturn(httpSecurity);
//        when(httpSecurity.cors()).thenReturn(httpSecurity);
//        when(httpSecurity.authorizeRequests()).thenReturn(httpSecurity);
//        when(httpSecurity.sessionManagement()).thenReturn(httpSecurity);
//        when(httpSecurity.authenticationProvider(authenticationProvider)).thenReturn(httpSecurity);
//        when(httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)).thenReturn(httpSecurity);
//
//        // Act: Get the SecurityFilterChain bean
//        SecurityFilterChain filterChain = securityConfig.securityFilterChain(httpSecurity);
//
//        // Assert: Verify that the filter chain is built correctly
//        assertNotNull(filterChain);
//        verify(httpSecurity).csrf(AbstractHttpConfigurer::disable);
//        verify(httpSecurity).cors(any());
//        verify(httpSecurity).authorizeRequests();
//        verify(httpSecurity).sessionManagement();
//        verify(httpSecurity).authenticationProvider(authenticationProvider);
//        verify(httpSecurity).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//
//    @Test
//    public void testCorsConfigurationSource() {
//        // Act: Get the CORS configuration source
//        UrlBasedCorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();
//
//        // Assert: Verify that the CORS configuration is set up correctly
//        assertNotNull(corsConfigurationSource);
//        CorsConfiguration corsConfig = corsConfigurationSource.getCorsConfiguration("/**");
//        assertNotNull(corsConfig);
//        assertTrue(corsConfig.getAllowedOrigins().contains("*"));
//        assertTrue(corsConfig.getAllowedHeaders().contains("*"));
//        assertTrue(corsConfig.getAllowedMethods().contains("*"));
//    }
//}
