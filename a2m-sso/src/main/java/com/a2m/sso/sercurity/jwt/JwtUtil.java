package com.a2m.sso.sercurity.jwt;

import java.security.PublicKey;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.a2m.sso.constant.SecurityConstant;
import com.a2m.sso.sercurity.service.UserDetailsImpl;
import com.a2m.sso.util.RSAUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {

	public static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${spring.security.jwt.secret}")
	private String secretKey;
	@Value("${spring.security.jwt.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.RS256, SecurityConstant.PRIVATE_KEY).compact();
	}

	public boolean validateJwtToken(String accessToken, String publicKey) {
		try {
			PublicKey key = RSAUtil.decodePublicKey(publicKey);
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Jwt Rsa error", e.getMessage());
		}
		return false;
	}
	
	public String parseJwt(HttpServletRequest httpServletRequest) {
		String header = httpServletRequest.getHeader(SecurityConstant.AUTHORIZATION_HEADER);
		
		if (StringUtils.hasText(header) && header.startsWith(SecurityConstant.TOKEN_PREFIX)) {
			return header.substring(7, header.length());
		}
		
		return null;
	}

}
