package t4.springboot.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import t4.springboot.demo.service.UserAuthService;

@Service
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	UserAuthService userAuthService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String username = null;
		String token = null;
		String reqtoken = request.getHeader("Authorization");
		if (reqtoken != null && reqtoken.startsWith("Bearer")) {
			token = reqtoken.substring(7);
			username = jwtUtil.getUsernameFromToken(token);
			System.out.println("Username: " + username);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtil.isTokenValid(token, username)) {
				UserDetails userDetails = userAuthService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken utoken = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				utoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(utoken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
