package it.cgmconsulting.auth.service;

import it.cgmconsulting.auth.entity.Authority;
import it.cgmconsulting.auth.entity.User;
import it.cgmconsulting.auth.exception.ResourceNotFoundException;
import it.cgmconsulting.auth.payload.request.ChangeRoleRequest;
import it.cgmconsulting.auth.payload.request.SignInRequest;
import it.cgmconsulting.auth.payload.request.SignUpRequest;
import it.cgmconsulting.auth.payload.response.UserResponse;
import it.cgmconsulting.auth.payload.response.JwtAuthenticationResponse;
import it.cgmconsulting.auth.repository.AuthorityRepository;
import it.cgmconsulting.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtService;

    public ResponseEntity<?> signup(SignUpRequest request){
        if(existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            return new ResponseEntity<>("Username or email already in use", HttpStatus.BAD_REQUEST);
        User u = fromRequestToEntity(request);

        Optional<Authority> a = authorityRepository.findByAuthorityName("ROLE_READER");
        if(!a.isPresent())
            return new ResponseEntity<>("Something went wrong during registration", HttpStatus.UNPROCESSABLE_ENTITY);
        u.getAuthorities().add(a.get());
        u.setEnabled(true);

        save(u);
        return new ResponseEntity<>("Signup successfully completed", HttpStatus.OK);
    }


    public ResponseEntity<?> signin(SignInRequest request) {
        Optional<User> u = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail());
        if(!u.isPresent())
            return new ResponseEntity("Wrong username or password", HttpStatus.FORBIDDEN);
        if(!passwordEncoder.matches(request.getPassword(), u.get().getPassword()))
            return new ResponseEntity("Wrong username or password", HttpStatus.FORBIDDEN);

        return new ResponseEntity(JwtAuthenticationResponse.createJwtAuthenticationResponse(u.get(), jwtService.generateToken(u.get())), HttpStatus.OK);
    }

    protected boolean existsByUsernameOrEmail(String username, String email){
        return userRepository.existsByUsernameOrEmail(username, email);
    }
    protected User fromRequestToEntity(SignUpRequest request){
        return new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
    }
    protected void save(User user){
        userRepository.save(user);
    }

    @Transactional
    public ResponseEntity<?> changeRoles(ChangeRoleRequest request, long headerId) {

        if(headerId == request.getId())
            return new ResponseEntity<>("You can't change your role", HttpStatus.BAD_REQUEST);

        Set<Authority> authorities = authorityRepository.findByAuthorityNameIn(request.getNewAuthorities());
        if(authorities.isEmpty())
            return new ResponseEntity<>("No valid authority selected", HttpStatus.BAD_REQUEST);

        User u = findUserByIdAndEnabledTrue(request.getId());
        u.setAuthorities(authorities);
        return new ResponseEntity<>("Roles updated successfully", HttpStatus.OK);

    }

    protected User findUserById(long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );
        return user;
    }

    protected User findUserByIdAndEnabledTrue(long userId){
        User user = userRepository.findByIdAndEnabledTrue(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );
        return user;
    }

    public String getUsername(long userId){
        return userRepository.getUsername(userId);
    }

    public List<UserResponse> getUsersByRole(String role) {
        return userRepository.getUsersByRole(role);
    }



    public List<UserResponse> getUsersByIds(Set<Long> userIds) {
        List<UserResponse> users = userRepository.getUsersBydIds(userIds);
        return users;
    }
}
