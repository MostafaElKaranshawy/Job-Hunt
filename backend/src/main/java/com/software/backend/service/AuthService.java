package com.software.backend.service;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
import com.software.backend.dto.ApplicantDto;
import com.software.backend.entity.Applicant;

import com.software.backend.dto.GoogleAuthDto;
import com.software.backend.entity.User;
import com.software.backend.enums.UserType;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    final String CLIENT_ID = "992406545501-gsf4drs652b27m7886oq1ttlfrq3o14t.apps.googleusercontent.com";

    @Autowired
    private UserRepository userRepository;

    private JsonWebSignature verifyGoogleToken(String idToken) {
        idToken = idToken.replace("\"", "");
        if (idToken == null || idToken.isEmpty()) {
            throw new IllegalArgumentException("idToken is required");
        }
        try {
            TokenVerifier tokenVerifier = TokenVerifier.newBuilder()
                    .setAudience(CLIENT_ID)
                    .build();
            JsonWebSignature verifiedToken = tokenVerifier.verify(idToken);
            return verifiedToken;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }


    public Boolean googleSignUp(String idToken) {
        JsonWebSignature verifiedToken = verifyGoogleToken(idToken);
        if(verifiedToken != null) {
            JsonWebToken.Payload payload = verifiedToken.getPayload();
            String email = (String) payload.get("email");
            String name = (String) payload.get("name");
            System.out.println("name  :" + name);
            System.out.println("email :" + email);

            User user = createUser(email);

//            Applicant applicant = createApplicantFromDto();
            //employee
            //employee.create(dto
            // )
            return true;
        }
        return false;
    }

    public Applicant createApplicantFromDto(ApplicantDto applicantDto){
        Applicant applicant = new Applicant();
        return applicant;
    }

    private User createUser(String email){
        User user = new User();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword("123456789");
        user.setUserType(UserType.APPLICANT);
        userRepository.save(user);
        return user;
    }
}
