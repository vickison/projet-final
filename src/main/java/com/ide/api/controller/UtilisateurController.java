package com.ide.api.controller;

import com.ide.api.configurations.JwtTokenProvider;
import com.ide.api.dto.JwtAuthenticationResponse;
import com.ide.api.dto.LoginRequest;
import com.ide.api.entities.*;
import com.ide.api.message.ResponseMessage;
import com.ide.api.service.*;
import com.ide.api.utilities.EmailValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "")
public class UtilisateurController {
    private UtilisateurService utilisateurService;
    private DocumentService documentService;
    private TagService tagService;
    private CategorieService categorieService;
    private AuteurService auteurService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UtilisateurController(UtilisateurService utilisateurService,
                                 DocumentService documentService,
                                 TagService tagService,
                                 CategorieService categorieService,
                                 AuteurService auteurService,
                                 AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider) {
        this.utilisateurService = utilisateurService;
        this.documentService = documentService;
        this.tagService = tagService;
        this.categorieService = categorieService;
        this.auteurService = auteurService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createUser(@RequestBody Utilisateur utilisateur){
        String message = "";
        try{
            if(EmailValidator.isValid(utilisateur.getEmail())) {
                utilisateur.setAdmin(false);
                this.utilisateurService.createUtilisateur(utilisateur);
                message = "Utilisateur créé avec succès...";
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }else{
                message = "Email incorrect...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }

        }catch (Exception e){
            message = "Echec de création d'utilisateur...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/admin/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createAdmin(@RequestBody Utilisateur utilisateur){
        String message = "";
        try{
            if(EmailValidator.isValid(utilisateur.getEmail())) {
                utilisateur.setAdmin(true);
                this.utilisateurService.createUtilisateur(utilisateur);
                message = "Utilisateur créé avec succès...";
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }else{
                message = "Email incorrect...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }

        }catch (Exception e){
            message = "Echec de création d'utilisateur...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Utilisateur> findUsers(){
        return this.utilisateurService.findUtilisateurs();
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Utilisateur findUser(@PathVariable Integer id){
        return this.utilisateurService.findUtilisateur(id);
    }

    @GetMapping("/user/{utilisateurID}/documents")
    public ResponseEntity<List<Document>> findDocumentsByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Document> documents = this.documentService.findDocumentsByUtilisateurId(utilisateur);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/user/{utilisateurID}/tags")
    public ResponseEntity<List<Tag>> findTagsByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Tag> tags = this.tagService.findTagsByUtilisateurId(utilisateur);
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/user/{utilisateurID}/categories")
    public ResponseEntity<List<Categorie>> findCategoriesByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Categorie> categories = this.categorieService.findCategoriesByUtilisateurId(utilisateur);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/user/{utilisateurID}/auteurs")
    public ResponseEntity<List<Auteur>> findAuteurByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Auteur> auteurs = this.auteurService.findAuteursByUtilisateurId(utilisateur);
        return ResponseEntity.ok(auteurs);
    }

    /*@PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody LoginRequest loginRequest){
        if(utilisateurService.utilisateurAuthentifieParUsername(loginRequest.getUsername(), loginRequest.getPassword())){
            return new ResponseEntity<>("Login Succesful", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Invalid Credentials or Permission denied", HttpStatus.UNAUTHORIZED);
        }
    }*/

    @PostMapping("/admin/login")
    public ResponseEntity<ResponseMessage> adminLogin(@RequestBody @NotNull LoginRequest loginRequest){
        if(utilisateurService.adminAuthentifieParUsername(loginRequest.getUsername(), loginRequest.getPassword())){
            return ResponseEntity.ok(new ResponseMessage("Login successful"));
        }else {
            return ResponseEntity.status(401).body(new ResponseMessage("Login failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        // Perform authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Set the authentication object in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Generate JWT token
        String token = jwtTokenProvider.generateToken(authentication);
        String jsonResponse = "{\"token\":\"" + token + "\"}";


        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(86400);
        cookie.setPath("/api");
        response.addCookie(cookie);

        return ResponseEntity.ok(jsonResponse);
    }

}
