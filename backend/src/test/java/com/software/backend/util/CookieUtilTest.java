package com.software.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookieUtilTest {

    @Test
    void testAddCookie_SuccessfullyAddsCookie() {
        // Arrange
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        String name = "testCookie";
        String value = "testValue";

        // Act
        CookieUtil.addCookie(response, name, value);

        // Assert
        verify(response, times(1)).addCookie(argThat(cookie ->
                cookie.getName().equals(name) &&
                        cookie.getValue().equals(value) &&
                        cookie.isHttpOnly() &&
                        cookie.getSecure() &&
                        cookie.getPath().equals("/") &&
                        cookie.getMaxAge() == 7 * 24 * 60 * 60
        ));
    }

    @Test
    void testGetCookie_WhenCookieExists_ReturnsCookie() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);

        Cookie[] cookies = new Cookie[] {
                new Cookie("testCookie", "testValue"),
                new Cookie("otherCookie", "otherValue")
        };
        when(request.getCookies()).thenReturn(cookies);

        // Act
        Cookie result = CookieUtil.getCookie(request, "testCookie");

        // Assert
        assertNotNull(result);
        assertEquals("testCookie", result.getName());
        assertEquals("testValue", result.getValue());
    }

    @Test
    void testGetCookie_WhenCookieDoesNotExist_ReturnsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);

        Cookie[] cookies = new Cookie[] {
                new Cookie("otherCookie", "otherValue")
        };
        when(request.getCookies()).thenReturn(cookies);

        // Act
        Cookie result = CookieUtil.getCookie(request, "testCookie");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetCookie_WhenNoCookies_ReturnsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        // Act
        Cookie result = CookieUtil.getCookie(request, "testCookie");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetCookie_WhenCookiesArrayIsEmpty_ReturnsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{});

        // Act
        Cookie result = CookieUtil.getCookie(request, "testCookie");

        // Assert
        assertNull(result);
    }
}
