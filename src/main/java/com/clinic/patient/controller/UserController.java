package com.clinic.patient.controller;
import com.clinic.patient.models.*;
import com.clinic.patient.security.JwtService;
import com.clinic.patient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Krishana dubey
 * @apiNote
 *  user registration ,
 *  GET,PUT,POST,DELETE by ID
 *  user login
 */


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
   private UserController(UserService userService,JwtService jwtService){
       this.userService =userService;
       this.jwtService =jwtService;
   }

    /**
     *
     * @param dto  object uses to register the user
     * @return UserResponseDTO object
     */

    @PostMapping("auth/register")
    public ResponseEntity<AuthResponse> createUser(@RequestBody UserRequestDTO dto) {

        UserResponseDTO userResponseDTO = userService.createUser(dto);

        TokenResponse tokenResponse =new TokenResponse(jwtService.generateNewToken(dto.getEmail()), jwtService.generateRefreshToken(dto.getEmail()));
        return ResponseEntity.ok(new AuthResponse(userResponseDTO,tokenResponse));
    }

    /**
     *
     * @param id  uses to fetch the user
     * @return user object
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     *
     * @param id  uses to update the user
     * @return updated user object
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }


    /**
     *
     * @param id  uses to delete the user
     * @return void matters with the status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     *
     * @param loginRequest  is used to validate a user by this response body
     * @return UserResponseDTO object
     *
     */
    @PostMapping("auth/login")
    public ResponseEntity<AuthLoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
    return userService.login(loginRequest);
    }
}
