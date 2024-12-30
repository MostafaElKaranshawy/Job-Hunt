package com.software.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookieUtilTest {

    private CookieUtil cookieUtil;

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil(); // Instantiate the CookieUtil
    }

    @Test
    void testAddCookie_SuccessfullyAddsCookie() {
        // Arrange
        HttpServletResponse response = mock(HttpServletResponse.class);

        String name = "testCookie";
        String value = "testValue";

        // Act
        cookieUtil.addCookie(response, name, value);

        // Assert
        verify(response, times(1)).addCookie(argThat(cookie ->
                cookie.getName().equals(name) &&
                        cookie.getValue().equals(value) &&
                        cookie.isHttpOnly() &&
                        cookie.getSecure() &&
                        cookie.getPath().equals("/") &&
                        cookie.getMaxAge() == 7 * 24 * 60 * 60 // 7 days
        ));
    }

    @Test
    void testDeleteCookie_SuccessfullyDeletesCookie() {
        // Arrange
        HttpServletResponse response = mock(HttpServletResponse.class);
        String name = "testCookie";

        // Act
        cookieUtil.deleteCookie(response, name);

        // Assert
        verify(response, times(1)).addCookie(argThat(cookie ->
                cookie.getName().equals(name) &&
                        cookie.getValue().isEmpty() &&
                        cookie.isHttpOnly() &&
                        cookie.getSecure() &&
                        cookie.getPath().equals("/") &&
                        cookie.getMaxAge() == 0 // Immediate expiration
        ));
    }

    @Test
    void testGetCookie_WhenCookieExists_ReturnsCookie() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);

        Cookie[] cookies = {
                new Cookie("testCookie", "testValue"),
                new Cookie("otherCookie", "otherValue")
        };
        when(request.getCookies()).thenReturn(cookies);

        // Act
        Cookie result = cookieUtil.getCookie(request, "testCookie");

        // Assert
        assertNotNull(result);
        assertEquals("testCookie", result.getName());
        assertEquals("testValue", result.getValue());
    }

    @Test
    void testGetCookie_WhenCookieDoesNotExist_ReturnsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);

        Cookie[] cookies = {
                new Cookie("otherCookie", "otherValue")
        };
        when(request.getCookies()).thenReturn(cookies);

        // Act
        Cookie result = cookieUtil.getCookie(request, "testCookie");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetCookie_WhenNoCookies_ReturnsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        // Act
        Cookie result = cookieUtil.getCookie(request, "testCookie");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetCookie_WhenCookiesArrayIsEmpty_ReturnsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{});

        // Act
        Cookie result = cookieUtil.getCookie(request, "testCookie");

        // Assert
        assertNull(result);
    }
}
