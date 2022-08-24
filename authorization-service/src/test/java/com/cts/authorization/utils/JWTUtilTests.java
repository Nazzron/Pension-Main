package com.cts.authorization.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.authorization.exception.InvalidTokenException;
import com.cts.authorization.util.JwtUtil;


@SpringBootTest
class JWTUtilTests {

	@Autowired
	JwtUtil jwtUtil;

	@Test
	void jwtUtilNotNull() {
		assertNotNull(jwtUtil);
	}

	@Test
	@DisplayName("This method is responsible to test isTokenExpiredOrInvalidFormat() for valid token")
	void testIsTokenExpiredOrInvalidFormat_validToken() {

		final String username = "jugraj";

		final String token = jwtUtil.generateToken(username);

		assertFalse(jwtUtil.isTokenExpiredOrInvalidFormat(token));

		assertNotNull(token);

	}

	@Test
	@DisplayName("This method is responsible to test isTokenExpiredOrInvalidFormat() for Expired token")
	void testIsTokenExpiredOrInvalidFormat_expiredToken() {

		final String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzE4MTgsInN1YiI6ImFkbWluMSIsImV4cCI6MTYyNzAzMTg3OH0.iBDf8UvcnHKa-TVHHxjOQUiC3oEVGgsYrJSvD5LhUQc";

		InvalidTokenException thrownException = assertThrows(InvalidTokenException.class,
				() -> jwtUtil.isTokenExpiredOrInvalidFormat(token));
		assertTrue(thrownException.getMessage().contains("Token has been expired"));

		assertNotNull(token);

	}

	@Test
	void testIsTokenExpiredOrInvalidFormat_invalidFormatToken() {

		final String token = "eyJhbGOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzE4MTgsInN1YiI6ImFkbWluMSIsImV4cCI6MTYyNzAzMTg3OH0.iBDf8UvcnHKa-TVHHxjOQUiC3oEVGgsYrJSvD5LhUQc";

		InvalidTokenException thrownException = assertThrows(InvalidTokenException.class,
				() -> jwtUtil.isTokenExpiredOrInvalidFormat(token));
		assertTrue(thrownException.getMessage().contains("Token is in invalid format"));

		assertNotNull(token);

	}

	@Test
	void testIsTokenExpiredOrInvalidFormat_nullToken() {

		final String token = null;

		InvalidTokenException thrownException = assertThrows(InvalidTokenException.class,
				() -> jwtUtil.isTokenExpiredOrInvalidFormat(token));
		assertTrue(thrownException.getMessage().contains("Token is either null or empty"));

		assertNull(token);

	}

	@Test
	void testIsTokenExpiredOrInvalidFormat_invalidTokenSignature() {

		final String token = "eyJhbGciOiJIUzI1NiJ91.eyJpYXQiOjE2MjczMjA2ODIsInN1YiI6ImFkbWluMSIsImV4cCI6MTYyNzMyMDc0Mn0.tiQjNTsiLwo7Q2EyuJeV9p187jUZVr7PCTZMs9gvBgk";

		InvalidTokenException thrownException = assertThrows(InvalidTokenException.class,
				() -> jwtUtil.isTokenExpiredOrInvalidFormat(token));
		assertTrue(thrownException.getMessage().contains("Token signature is invalid"));

		assertNotNull(token);

	}

	@Test
	void testGetUsernameFromToken() {

		final String username = "jugraj";

		String token = jwtUtil.generateToken(username);

		assertEquals(username, jwtUtil.getUsernameFromToken(token));

	}

}
