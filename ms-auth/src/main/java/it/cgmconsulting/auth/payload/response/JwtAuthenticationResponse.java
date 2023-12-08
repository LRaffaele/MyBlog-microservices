package it.cgmconsulting.auth.payload.response;

import it.cgmconsulting.auth.entity.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import it.cgmconsulting.auth.entity.User;
import java.util.Set;

@Setter @Getter @NoArgsConstructor
public class JwtAuthenticationResponse {

	private long id;
	private String username;
	private String email;
	private Set<Authority> authorities;
	private String token;
	
	public JwtAuthenticationResponse(long id, String username, String email, Set<Authority> authorities, String token) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.authorities = authorities;
		this.token = "Bearer " + token;
	}

	public static JwtAuthenticationResponse createJwtAuthenticationResponse(User u, String token){
		return new JwtAuthenticationResponse(
				u.getId(),
				u.getUsername(),
				u.getEmail(),
				u.getAuthorities(),
				token
		);
	}
}
