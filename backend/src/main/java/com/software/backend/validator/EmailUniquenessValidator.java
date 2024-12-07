//package com.software.backend.validator;
//
//import com.software.backend.dto.SignUpRequest;
//import com.software.backend.entity.User;
//import com.software.backend.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//public class EmailUniquenessValidator extends Validator {
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    @Override
//    public void validate(SignUpRequest signUpRequest){ // throws ValidationException {
//        String email = signUpRequest.getEmail();
//
//        User user = userRepository.findByEmail(email);
//
//        // Check if the email already exists in the database
//        boolean emailExists = user != null;
//        if (emailExists) {
////            throw new ValidationException("Email already exists.");
//        }
//
//        if (nextValidator != null) {
//            nextValidator.validate(signUpRequest);
//        }
//    }
//}
