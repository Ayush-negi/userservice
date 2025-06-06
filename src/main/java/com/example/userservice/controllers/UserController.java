package com.example.userservice.controllers;

import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import com.example.userservice.dtos.UserDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.exceptions.UnAuthorizedException;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.dtos.LoginRequestDto;
import com.example.userservice.dtos.LogoutRequestDto;
import com.example.userservice.dtos.SignUpRequestDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
 

    public UserController(UserService userService) {
        this.userService = userService;
    }
    

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto requestDto)  throws UserNotFoundException, UnAuthorizedException
    {
        return userService.login(requestDto.getEmail(), requestDto.getPassword() );
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto ) {

        User user = userService.signUp(
            requestDto.getName(),
            requestDto.getEmail(),
            requestDto.getPassword()
        );

        //Convert this User object into UserDto object.

        return UserDto.from(user);
    }
    

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto)
    {
        userService.logout(requestDto.getTokenValue());
        return new ResponseEntity<Void>
        (HttpStatus.OK);
    }

    @GetMapping("/validate/{tokenValue}")
    public UserDto validateToken(@PathVariable String tokenValue)
    {
        User user = userService.validateToken(
            tokenValue
        );

        return UserDto.from(user);
    }

    

}
