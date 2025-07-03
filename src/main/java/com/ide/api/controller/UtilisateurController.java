package com.ide.api.controller;

import com.ide.api.configurations.JwtTokenProvider;
import com.ide.api.dto.LoginRequest;
import com.ide.api.dto.PasswordVerificationRequest;
import com.ide.api.dto.UtilisateurDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.TypeGestion;
import com.ide.api.message.ResponseMessage;
import com.ide.api.message.UserResponse;
import com.ide.api.repository.UtilisateurRepository;
import com.ide.api.service.*;
import com.ide.api.utilities.EmailValidator;
import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;
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


//    @ResponseStatus(value = HttpStatus.CREATED)
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping(value="/admin/add", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResponseMessage> createUser(@RequestBody UtilisateurDTO utilisateurDTO,
//                                                      HttpServletRequest request){
//        Utilisateur utilisateur = new Utilisateur();
//        String adresseIP = request.getRemoteAddr();
//        String message = "";
//        try{
//            if(EmailValidator.isValid(utilisateur.getEmail())) {
//                utilisateur.setAdmin(false);
//                utilisateur.setAddresseIP(adresseIP);
//                this.utilisateurService.createUtilisateur(utilisateur);
//                message = "Utilisateur créé avec succès...";
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(new ResponseMessage(message));
//            }else{
//                message = "Email incorrect...";
//                return ResponseEntity
//                        .status(HttpStatus.EXPECTATION_FAILED)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(new ResponseMessage(message));
//            }
//
//        }catch (Exception e){
//            message = "Echec de création d'utilisateur...";
//            return ResponseEntity
//                    .status(HttpStatus.EXPECTATION_FAILED)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(new ResponseMessage(message));
//        }
//    }

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
            Utilisateur superAdm = this.utilisateurService.findUtilisateur(adminID);
            if(superAdm.isSuperAdmin()){
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
            }else{
                message = "Accès non autorisé, super administrateur seulement...";
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
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
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(id);
        utilisateur.setPassword("******");
        return utilisateur;
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
        try {
            // Authentifie l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Vérifie si l'utilisateur est supprimé
            if (userDetails.isDelete()) {
                logger.warn("Utilisateur avec username {} a été supprimé.", userDetails.getUsername());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Forbidden", "Compte utilisateur a été supprimé."));
            }

            // Génère le cookie JWT
            ResponseCookie jwtCookie = jwtTokenProvider.generateJwtCookie(userDetails);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Retourne la réponse avec le cookie
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new UserResponse(userDetails.getId(), userDetails.getUsername(), roles));

        } catch (BadCredentialsException e) {
            // Gestion des identifiants incorrects
            logger.error("Identifiants incorrects: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthorized", "Identifiants incorrects."));
        } catch (UsernameNotFoundException e) {
            // Gestion des utilisateurs non trouvés
            logger.error("Utilisateur non trouvé: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthorized", "Utilisateur non trouvé."));
        } catch (Exception e) {
            // Gestion d'autres exceptions
            logger.error("Erreur inattendue: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", "Une erreur est survenue."));
        }
    }
    
    
    @PostMapping("/signout")
    public ResponseEntity<?> signout(){
        ResponseCookie cookie = jwtTokenProvider.getCleanJwtCookie();;
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ResponseMessage("Déconnexion réussite..."));
    }

    private static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }



//    @PostMapping("/signin")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
//        logger.info("Inside login controller...");
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsername(),
//                        loginRequest.getPassword()
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        //String jwt = jwtTokenProvider.generateJwtToken(authentication);
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        if (userDetails.isDelete()) {
//            logger.warn("Utilisateur avec username {} a été supprimé.", userDetails.getUsername());
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Compte utilisateur a été supprimé.");
//        }
//        ResponseCookie jwtCookie = jwtTokenProvider.generateJwtCookie(userDetails);
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                .body(new UserResponse(userDetails.getId(),
//                        userDetails.getUsername(),
//                        roles
//        ));
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Utilisateur> updateUsers(@PathVariable Integer id,
                                                   @Valid @RequestBody UtilisateurDTO utilisateurDetails){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(id);
        Utilisateur admin = this.utilisateurService.findUtilisateur(adminID);

        if (!admin.isSuperAdmin()) {
            // Règle: Seul un super admin peut modifier un admin ou lui-même
            if (utilisateur.isAdmin() || utilisateur.getUtilisateurID().equals(admin.getUtilisateurID())) {
                throw new AccessDeniedException("Seul un Super Admin peut modifier un administrateur");
            }
        }


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
        utilisateurUpdated.setPassword("******");
        return ResponseEntity.ok(utilisateurUpdated);
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PutMapping("/admin/modif/{id}")
//    public ResponseEntity<Utilisateur> updateUserV2(
//            @PathVariable Integer id,
//            @Valid @RequestBody UtilisateurDTO utilisateurDetails,
//            @RequestParam(required = false) String currentPassword) {
//
//        // 1. Authentification
//        CustomUserDetails adminDetails = (CustomUserDetails) SecurityContextHolder.getContext()
//                .getAuthentication().getPrincipal();
//        Utilisateur admin = this.utilisateurService.findUtilisateur(adminDetails.getId());
//        Utilisateur targetUser = this.utilisateurService.findUtilisateur(id);
//
//        // 2. Vérification des permissions
//        if (!admin.isSuperAdmin()) {
//            // Règle: Seul un super admin peut modifier un admin ou lui-même
//            if (targetUser.isAdmin() || targetUser.getUtilisateurID().equals(admin.getUtilisateurID())) {
//                throw new AccessDeniedException("Seul un Super Admin peut modifier un administrateur");
//            }
//        }
//
//        // 3. Vérification du mot de passe pour les actions sensibles
//        if (isSensitiveAction(utilisateurDetails, targetUser)) {
//            if (currentPassword == null || !passwordEncoder.matches(currentPassword, admin.getPassword())) {
//                throw new AccessDeniedException("Confirmation par mot de passe requise");
//            }
//        }
//
//        // 4. Mise à jour sécurisée
//        updateUserData(targetUser, utilisateurDetails, admin);
//
//        // 5. Sauvegarde et audit
//        Utilisateur updatedUser = this.utilisateurRepository.save(targetUser);
//        logAdminAction(admin, targetUser, TypeGestion.Modifier);
//
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    private boolean isSensitiveAction(UtilisateurDTO newData, Utilisateur existingUser) {
//        return newData.getPassword() != null ||  // Changement de mot de passe
//                newData.isAdmin() != existingUser.isAdmin() ||  // Changement de rôle admin
//                newData.isSuperAdmin() != existingUser.isSuperAdmin();  // Changement de rôle superAdmin
//    }
//
//    private void updateUserData(Utilisateur target, UtilisateurDTO source, Utilisateur admin) {
//        // Mise à jour des informations de base
//        target.setNom(source.getNom());
//        target.setPrenom(source.getPrenom());
//        target.setUsername(source.getUsername());
//        target.setEmail(source.getEmail());
//        target.setAuteurModificationUtil(admin.getUsername());
//
//        // Mise à jour du mot de passe si fourni
//        if (source.getPassword() != null && !source.getPassword().isEmpty()) {
//            target.setPassword(passwordEncoder.encode(source.getPassword()));
//        }
//
//        // Seul un superAdmin peut modifier les rôles
//        if (admin.isSuperAdmin()) {
//            target.setAdmin(source.isAdmin());
//            target.setSuperAdmin(source.isSuperAdmin());
//        }
//    }


    private void logAdminAction(Utilisateur admin, Utilisateur targetUser, TypeGestion actionType) {
        adminUtilisateurService.findByAdminAndUtil(admin, targetUser)
                .ifPresentOrElse(
                        adminUtil -> {
                            adminUtil.setTypeGestion(actionType);
                            adminUtilisateurService.createAdminUtilisateur(adminUtil);
                        },
                        () -> {
                            AdminUtilisateur newAdminUtil = new AdminUtilisateur();
                            newAdminUtil.setUtilisateurID(targetUser);
                            newAdminUtil.setAdminID(admin);
                            newAdminUtil.setTypeGestion(actionType);
                            adminUtilisateurService.createAdminUtilisateur(newAdminUtil);
                        }
                );
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/modif/{id}")
    public ResponseEntity<Utilisateur> updateUserV4(
            @PathVariable Integer id,
            @Valid @RequestBody UtilisateurDTO utilisateurDetails,
            @RequestParam String currentPassword) {  // Mot de passe maintenant obligatoire

        // 1. Authentification
        CustomUserDetails adminDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Utilisateur admin = this.utilisateurService.findUtilisateur(adminDetails.getId());
        Utilisateur targetUser = this.utilisateurService.findUtilisateur(id);

        // 2. Vérification STRICTE : seul un super-admin peut modifier
        if (!admin.isSuperAdmin()) {
            //log.warn("Tentative de modification non autorisée par {}", admin.getUsername());
            throw new AccessDeniedException("Seuls les Super-Admins peuvent modifier les utilisateurs");
        }

        // 3. Validation du mot de passe admin (obligatoire)
        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            throw new BadCredentialsException("Mot de passe administrateur incorrect");
        }

        // 4. Mise à jour sécurisée (uniquement par super-admin)
        updateUserDataSuperAdmin(targetUser, utilisateurDetails, admin);

        // 5. Sauvegarde et audit
        Utilisateur updatedUser = this.utilisateurRepository.save(targetUser);
        logAdminAction(admin, targetUser, TypeGestion.Modifier);

        return ResponseEntity.ok(updatedUser);
    }

    private void updateUserDataSuperAdmin(Utilisateur target,
                                          UtilisateurDTO source,
                                          Utilisateur admin) {
        // Champs modifiables
        target.setNom(source.getNom());
        target.setPrenom(source.getPrenom());
        target.setUsername(source.getUsername());
        target.setEmail(source.getEmail());
        target.setAuteurModificationUtil(admin.getUsername());

        // Mot de passe
        if (source.getPassword() != null && !source.getPassword().isEmpty()) {
            target.setPassword(passwordEncoder.encode(source.getPassword()));
        }

        // Rôles (seul un super-admin peut les modifier)
        target.setAdmin(source.isAdmin());
        target.setSuperAdmin(source.isSuperAdmin());
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/delete/{id}")
    public ResponseEntity<Utilisateur> deleteUser(@PathVariable Integer id){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(id);
        Utilisateur admin = this.utilisateurService.findUtilisateur(adminID);

        // 2. Vérification STRICTE : seul un super-admin peut modifier
        if (!admin.isSuperAdmin()) {
            //log.warn("Tentative de modification non autorisée par {}", admin.getUsername());
            throw new AccessDeniedException("Seuls les Super-Admins peuvent modifier les utilisateurs");
        }

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
        utilisateurDeleted.setPassword("******");
        return ResponseEntity.ok(utilisateurDeleted);
    }


    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordVerificationRequest request) {
        boolean isValid = utilisateurService.verifyPassword(request.getUserId(), request.getPassword());
        return ResponseEntity.ok().body(Map.of("isValid", isValid));
    }


    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String token = jwtTokenProvider.getJwtFromCookie(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("status", "no_token"));
        }

        Map<String, Object> validation = jwtTokenProvider.validateJwtTokenWithDetails(token);

        if (!(boolean) validation.get("valid")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(validation);
        }

        return ResponseEntity.ok(validation);
    }

//    @GetMapping("/validate")
//    public ResponseEntity<Map<String, Object>> validateTokenV2(HttpServletRequest request) {
//        String token = jwtTokenProvider.getJwtFromCookie(request);
//        Map<String, Object> response = new HashMap<>();
//
//        if (token == null) {
//            response.put("valid", false);
//            response.put("reason", "no_token");
//            return ResponseEntity.status(401).body(response);
//        }
//
//        try {
//            Jws<Claims> claims = Jwts.parserBuilder()
//                    .setSigningKey(jwtTokenProvider.key())
//                    .build()
//                    .parseClaimsJws(token);
//
//            response.put("valid", true);
//            response.put("username", claims.getBody().getSubject());
//            return ResponseEntity.ok(response);
//
//        } catch (ExpiredJwtException e) {
//            response.put("valid", false);
//            response.put("reason", "expired");
//            return ResponseEntity.status(401).body(response);
//        } catch (JwtException | IllegalArgumentException e) {
//            response.put("valid", false);
//            response.put("reason", "invalid");
//            return ResponseEntity.status(401).body(response);
//        }
//    }




    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateTokenV2(HttpServletRequest request) {
        String token = jwtTokenProvider.getJwtFromCookie(request);
        Map<String, Object> response = new HashMap<>();

        if (token == null) {
            response.put("valid", false);
            response.put("reason", "no_token");
            response.put("status", "unauthorized");  // Ajout d'un champ status dans la réponse
            return ResponseEntity.ok(response);
        }

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(jwtTokenProvider.key())
                    .build()
                    .parseClaimsJws(token);

            response.put("valid", true);
            response.put("username", claims.getBody().getSubject());
            response.put("status", "authorized");  // Ajout d'un champ status dans la réponse
            return ResponseEntity.ok(response);

        } catch (ExpiredJwtException e) {
            response.put("valid", false);
            response.put("reason", "expired");
            response.put("status", "unauthorized");  // Ajout d'un champ status dans la réponse
            return ResponseEntity.ok(response);
        } catch (JwtException | IllegalArgumentException e) {
            response.put("valid", false);
            response.put("reason", "invalid");
            response.put("status", "unauthorized");  // Ajout d'un champ status dans la réponse
            return ResponseEntity.ok(response);
        }
    }

}
