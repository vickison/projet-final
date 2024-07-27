package com.ide.api.controller;

import com.ide.api.configurations.JwtTokenProvider;
import com.ide.api.dto.*;
import com.ide.api.entities.*;
import com.ide.api.enums.TypeGestion;
import com.ide.api.message.JwtResponse;
import com.ide.api.message.ResponseMessage;
import com.ide.api.message.UserResponse;
import com.ide.api.repository.UtilisateurRepository;
import com.ide.api.service.*;
import com.ide.api.utilities.EmailValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "users")
public class UtilisateurController {
    private static final Logger logger = LoggerFactory.getLogger(UtilisateurController.class);
    private UtilisateurService utilisateurService;
    private UtilisateurRepository utilisateurRepository;
    private DocumentService documentService;
    private TagService tagService;
    private CategorieService categorieService;
    private AuteurService auteurService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;
    private CustomUserDetailsService userDetailsService;
    private AdminUtilisateurService adminUtilisateurService;
    public UtilisateurController(UtilisateurService utilisateurService,
                                 DocumentService documentService,
                                 TagService tagService,
                                 CategorieService categorieService,
                                 AuteurService auteurService,
                                 AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 UtilisateurRepository utilisateurRepository,
                                 PasswordEncoder passwordEncoder,
                                 JdbcTemplate jdbcTemplate,
                                 CustomUserDetailsService userDetailsService,
                                 AdminUtilisateurService adminUtilisateurService
                                 ) {
        this.utilisateurService = utilisateurService;
        this.documentService = documentService;
        this.tagService = tagService;
        this.categorieService = categorieService;
        this.auteurService = auteurService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
        this.userDetailsService = userDetailsService;
        this.adminUtilisateurService = adminUtilisateurService;
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/admin/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createUser(@RequestBody Utilisateur utilisateur,
                                                      HttpServletRequest request){
        String adresseIP = request.getRemoteAddr();
        String message = "";
        try{
            if(EmailValidator.isValid(utilisateur.getEmail())) {
                utilisateur.setAdmin(false);
                utilisateur.setAddresseIP(adresseIP);
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
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/root/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> rootUser(@RequestBody UtilisateurDTO utilisateurDTO,
                                                      HttpServletRequest request){
        String adresseIP = request.getRemoteAddr();
        Utilisateur utilisateur = new Utilisateur();
        String message = "";
        try{
            if(EmailValidator.isValid(utilisateurDTO.getEmail())) {
                utilisateur.setAdmin(true);
                utilisateur.setSuperAdmin(true);
                utilisateur.setNom(utilisateurDTO.getNom());
                utilisateur.setNom(utilisateurDTO.getNom());
                utilisateur.setPrenom(utilisateurDTO.getPrenom());
                utilisateur.setPassword(utilisateurDTO.getPassword());
                utilisateur.setEmail(utilisateurDTO.getEmail());
                utilisateur.setUsername(utilisateurDTO.getUsername());
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/admin/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createAdmin(@RequestBody UtilisateurDTO utilisateurDTO,
                                                       HttpServletRequest request) throws SQLException {
        String adresseIP = request.getRemoteAddr();
        Utilisateur utilisateur = new Utilisateur();
        String message = "";
        //String userDatabase = jdbcTemplate.getDataSource().getConnection().getMetaData().getUserName();
        //String[] parts = userDatabase.split("@");
        //String userCr = parts[0];
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        try{
            if(EmailValidator.isValid(utilisateurDTO.getEmail())) {
                utilisateur.setNom(utilisateurDTO.getNom());
                utilisateur.setPrenom(utilisateurDTO.getPrenom());
                utilisateur.setPassword(utilisateurDTO.getPassword());
                utilisateur.setEmail(utilisateurDTO.getEmail());
                utilisateur.setUsername(utilisateurDTO.getUsername());
                utilisateur.setAdmin(true);
                utilisateur.setAddresseIP(adresseIP);
                utilisateur.setAuteurCreationUtil(userDetails.getUsername());
                this.utilisateurService.creerUtilisateur(utilisateur, adminID);
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Utilisateur> findUsers(){
        return this.utilisateurService.findUtilisateurs();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Utilisateur findUser(@PathVariable Integer id){
        return this.utilisateurService.findUtilisateur(id);
    }

    @GetMapping("/{utilisateurID}/documents")
    public ResponseEntity<List<Document>> findDocumentsByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Document> documents = this.documentService.findDocumentsByUtilisateurId(utilisateur);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{utilisateurID}/tags")
    public ResponseEntity<List<Tag>> findTagsByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Tag> tags = this.tagService.findTagsByUtilisateurId(utilisateur);
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{utilisateurID}/categories")
    public ResponseEntity<List<Categorie>> findCategoriesByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Categorie> categories = this.categorieService.findCategoriesByUtilisateurId(utilisateur);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{utilisateurID}/auteurs")
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/login")
    public ResponseEntity<ResponseMessage> adminLogin(@RequestBody @NotNull LoginRequest loginRequest){
        if(utilisateurService.adminAuthentifieParUsername(loginRequest.getUsername(), loginRequest.getPassword())){
            return ResponseEntity.ok(new ResponseMessage("Login successful"));
        }else {
            return ResponseEntity.status(401).body(new ResponseMessage("Login failed"));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Inside login controller...");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //String jwt = jwtTokenProvider.generateJwtToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails.isDelete()) {
            logger.warn("User with username {} is deleted and cannot log in.", userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User account is deleted.");
        }
        ResponseCookie jwtCookie = jwtTokenProvider.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        roles
        ));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Utilisateur> updateUsers(@PathVariable Integer id,
                                                   @Valid @RequestBody Utilisateur utilisateurDetails){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(id);
        Utilisateur admin = this.utilisateurService.findUtilisateur(adminID);
        utilisateur.setNom(utilisateurDetails.getNom());
        utilisateur.setPrenom(utilisateurDetails.getPrenom());
        utilisateur.setUsername(utilisateurDetails.getUsername());
        utilisateur.setEmail(utilisateurDetails.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(utilisateurDetails.getPassword()));
        utilisateur.setAdmin(utilisateurDetails.isAdmin());
        utilisateur.setAuteurModificationUtil(admin.getUsername());
        final Utilisateur utilisateurUpdated = this.utilisateurRepository.save(utilisateur);
        Optional<AdminUtilisateur> adminUtil = this.adminUtilisateurService.findByAdminAndUtil(admin, utilisateur);
        if(adminUtil.isPresent()){
            AdminUtilisateur adminUtilisateur = adminUtil.get();
            adminUtilisateur.setTypeGestion(TypeGestion.Modifier);
            this.adminUtilisateurService.createAdminUtilisateur(adminUtilisateur);
        }else {
            AdminUtilisateur newAdmUtil = new AdminUtilisateur();
            newAdmUtil.setUtilisateurID(utilisateurUpdated);
            newAdmUtil.setAdminID(admin);
            newAdmUtil.setTypeGestion(TypeGestion.Modifier);
            this.adminUtilisateurService.createAdminUtilisateur(newAdmUtil);
        }
        return ResponseEntity.ok(utilisateurUpdated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/delete/{id}")
    public ResponseEntity<Utilisateur> deleteUser(@PathVariable Integer id){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(id);
        Utilisateur admin = this.utilisateurService.findUtilisateur(adminID);
        utilisateur.setSupprimerUtil(true);
        utilisateur.setAuteurModificationUtil(admin.getUsername());
        final Utilisateur utilisateurDeleted = this.utilisateurRepository.save(utilisateur);
        Optional<AdminUtilisateur> adminUtil = this.adminUtilisateurService.findByAdminAndUtil(admin, utilisateur);
        if(adminUtil.isPresent()){
            AdminUtilisateur adminUtilisateur = adminUtil.get();
            adminUtilisateur.setTypeGestion(TypeGestion.Supprimer);
            this.adminUtilisateurService.createAdminUtilisateur(adminUtilisateur);
        }else {
            AdminUtilisateur newAdmUtil = new AdminUtilisateur();
            newAdmUtil.setUtilisateurID(utilisateurDeleted);
            newAdmUtil.setAdminID(admin);
            newAdmUtil.setTypeGestion(TypeGestion.Supprimer);
            this.adminUtilisateurService.createAdminUtilisateur(newAdmUtil);
        }
        return ResponseEntity.ok(utilisateurDeleted);
    }

}
