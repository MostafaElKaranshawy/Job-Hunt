package com.software.backend.service;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import org.springframework.http.ResponseEntity;


import com.software.backend.dto.GoogleAuthDto;
import com.software.backend.factory.UserFactory;
import com.software.backend.model.user.User;
import com.software.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    final String CLIENT_ID = "992406545501-gsf4drs652b27m7886oq1ttlfrq3o14t.apps.googleusercontent.com";

    private Boolean verifyGoogleToken(GoogleAuthDto googleAuthDto) {
        if(googleAuthDto.getIdToken()==null || googleAuthDto.getIdToken().isEmpty()) {
            throw new IllegalArgumentException("idToken is required");
        }
        try {
            TokenVerifier tokenVerifier = TokenVerifier.newBuilder()
                    .setAudience(CLIENT_ID)
                    .build();
            JsonWebSignature idToken = tokenVerifier.verify(googleAuthDto.getIdToken());
            googleAuthDto.setEmail((String) idToken.getPayload().get("email"));
            googleAuthDto.setName((String) idToken.getPayload().get("name"));
            googleAuthDto.setProfilePicture((String) idToken.getPayload().get("picture"));
            return true;
        } catch (Exception e) {
            return false;
        }
      }

    public Boolean googleSignUp(GoogleAuthDto googleAuthDto) {
        if(verifyGoogleToken(googleAuthDto)){
            UserFactory factory = new UserFactory();
            User newUser = factory.createUser("Employee", googleAuthDto.getEmail(), googleAuthDto.getName(), googleAuthDto.getProfilePicture(), true);
            UserRepository userRepository = new UserRepository();
//            userRepository.save(newUser);
            System.out.println(googleAuthDto.getEmail());
            System.out.println(googleAuthDto.getName());
        }
        return true;
    }
}
