package t4.springboot.demo.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

	String secret;
	int exp;

	@Value("${secret}")
	public void getSecret(String secret) {
		this.secret = secret;

	}

	@Value("${expInMillis}")
	public void getExp(int exp) {
		this.exp = exp;
	}

	public String extratctUsernameFromToken(String token) {
		return extractClaimFromToken(token, Claims::getSubject);
	}

	public Date extratctExpDateFromToken(String token) {
		return extractClaimFromToken(token, Claims::getExpiration);
	}

	private Claims getAllClaims(String token) {

		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public <T> T extractClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
		
		
	}

	public String generateToken(String username) {
		System.out.println("Generating token");
		Map<String, Object> claims = new HashMap<String, Object>();
		return doGenerateToken(claims, username);
	}

	private String doGenerateToken(Map<String, Object> claims, String username) {
		return Jwts
				.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + exp))
				.signWith(SignatureAlgorithm.HS512, getSignKey())
				.compact();
	}

	private boolean isTokenExpired(String token) {
		return extratctExpDateFromToken(token).before(new Date(System.currentTimeMillis()));
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		return !isTokenExpired(token) && userDetails.getUsername().equals(extratctUsernameFromToken(token));
	}

	private Key getSignKey() {
		byte[] keys = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keys);
//	private byte[] getKey() {
//		
//	}

}
}