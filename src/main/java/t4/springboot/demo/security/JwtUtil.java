package t4.springboot.demo.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTUtil {

	private String secret;
	private int expInMillis;
	
	@Value("${secret}")
	void getSecret(String secret) {
		this.secret = secret;
	}
	
	@Value("${expInMillis}")
	void getSecret(int exp) {
		this.expInMillis = exp;
	}
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	private Date getExpFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	private Claims getAllClaims(String token) {
		return Jwts
		.parser()
		.setSigningKey(secret)
		.parseClaimsJws(token)
		.getBody();
	}
	
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private String doGenerateToken(String username, Map<String, Object> claims) {
		return Jwts
				.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+expInMillis))
				.signWith(SignatureAlgorithm.HS512, getSignKey())
				.compact();
				
	}
	
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<String, Object>();
		return doGenerateToken(username, claims);
	}
	
	private Key getSignKey() {
		byte[] keys = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keys);
	}
	
	private boolean isTokenExpired(String token) {
		return getExpFromToken(token).before(new Date(System.currentTimeMillis()));
	}
	
	public boolean isTokenValid(String token, String username) {
		return !isTokenExpired(token) && getUsernameFromToken(token).equals(username);
	}
	
	
}
