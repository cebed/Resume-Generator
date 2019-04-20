package com.example.resumegeneratorbackend.controller;

import com.example.resumegeneratorbackend.model.Users;


import com.example.resumegeneratorbackend.payloads.JWTLoginSuccessRes;
import com.example.resumegeneratorbackend.payloads.LoginRequests;
import com.example.resumegeneratorbackend.repository.UsersRepository;
import com.example.resumegeneratorbackend.security.JwtTokenProvider;
import com.example.resumegeneratorbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

import static com.example.resumegeneratorbackend.security.SecurityCockpit.TOKEN_PREFIX;

@RestController
@CrossOrigin
@RequestMapping("api/users")
public class UsersController {

    @Autowired
    private UserService userServices;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UsersRepository usersRepository;


    @GetMapping(value = "/all")
    public List<Users> getAll() {

        return userServices.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequests loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = userServices.StoreValidationErrorService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )

        );
        //At this point we assume that the person is authenticated
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessRes(true, jwt));
    }

    @GetMapping("{us_id}")
    public ResponseEntity<?> getUsByID(@PathVariable Long us_id){

        Users users = userServices.findById(us_id);
        return new ResponseEntity<Users>(users, HttpStatus.OK);
    }


    @PutMapping("/allusers/{id}")
    public Users updateUser(@RequestBody Users u, @PathVariable Long id) {


        return usersRepository.findById(id)
                .map(users -> {
                    users.setCurrentTitle(u.getCurrentTitle());
                    users.setFullName(u.getFullName());
                    users.setUsername(u.getUsername());
                    users.setAddress(u.getAddress());
                    users.setPhone(u.getPhone());
                    users.setUserProfile(u.getUserProfile());
                    return usersRepository.save(users);
                })
                .orElseGet(() -> {
                    u.setId(id);
                    return usersRepository.save(u);
                });
    }




    @PostMapping("/register")
    public Users registerUser(@Valid @RequestBody Users user){

        return  userServices.saveUser(user);

    }







    /*
    // tillfällig login metod finns på services
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_XML_VALUE)
    public String Login(@RequestBody String username) {
        return userServices.Login(username);
    }
    */
}