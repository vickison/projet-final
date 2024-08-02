package com.ide.api.service;

import com.ide.api.entities.AdminUtilisateur;
import com.ide.api.entities.Utilisateur;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.AdminUtilisateurRepository;
import com.ide.api.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UtilisateurService {

    private static final Logger logger = LoggerFactory.getLogger(TagService.class);
    private UtilisateurRepository utilisateurRepository;
    private AdminUtilisateurRepository adminUtilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurService(PasswordEncoder passwordEncoder,
                              UtilisateurRepository utilisateurRepository,
                              AdminUtilisateurRepository adminUtilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUtilisateurRepository = adminUtilisateurRepository;
    }

//    public void createUtilisateur(Utilisateur utilisateur){
//        String hashedPassword = passwordEncoder.encode(utilisateur.getPassword());
//        utilisateur.setPassword(hashedPassword);
//        this.utilisateurRepository.save(utilisateur);
//
//    }

    public void createUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            logger.error("Utilisateur fourni est null");
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }

        if (utilisateur.getPassword() == null || utilisateur.getPassword().isEmpty()) {
            logger.error("Mot de passe de l'utilisateur est null ou vide");
            throw new IllegalArgumentException("Le mot de passe ne peut pas être null ou vide");
        }

        try {

            String hashedPassword = passwordEncoder.encode(utilisateur.getPassword());
            utilisateur.setPassword(hashedPassword);
            utilisateurRepository.save(utilisateur);
            logger.info("Utilisateur créé avec succès : {}", utilisateur.getUsername());
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur : {}", utilisateur.getUsername(), e);
            throw new RuntimeException("Erreur lors de la création de l'utilisateur", e);
        }
    }

//    public void creerUtilisateur(Utilisateur utilisateur,
//                                 Integer adminID){
//        String hashedPassword = passwordEncoder.encode(utilisateur.getPassword());
//        utilisateur.setPassword(hashedPassword);
//        Utilisateur utilisateurSaved = this.utilisateurRepository.save(utilisateur);
//        AdminUtilisateur adminUtilisateur = new AdminUtilisateur();
//        Utilisateur admin = this.utilisateurRepository.findById(adminID)
//                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant: " + adminID + " introuvable"));
//        adminUtilisateur.setAdminID(admin);
//        adminUtilisateur.setUtilisateurID(utilisateurSaved);
//        adminUtilisateur.setTypeGestion(TypeGestion.Ajouter);
//        this.adminUtilisateurRepository.save(adminUtilisateur);
//
//    }


    @Transactional
    public void creerUtilisateur(Utilisateur utilisateur, Integer adminID) {
        if (utilisateur == null) {
            logger.error("Utilisateur fourni est null");
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }

        if (utilisateur.getPassword() == null || utilisateur.getPassword().isEmpty()) {
            logger.error("Mot de passe de l'utilisateur est null ou vide");
            throw new IllegalArgumentException("Le mot de passe ne peut pas être null ou vide");
        }

        try {
            // Encodage du mot de passe
            String hashedPassword = passwordEncoder.encode(utilisateur.getPassword());
            utilisateur.setPassword(hashedPassword);

            // Enregistrement de l'utilisateur
            Utilisateur utilisateurSaved = utilisateurRepository.save(utilisateur);
            logger.info("Utilisateur créé avec succès : {}", utilisateurSaved.getUsername());

            // Recherche de l'administrateur
            Utilisateur admin = utilisateurRepository.findById(adminID)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant: " + adminID + " introuvable"));

            AdminUtilisateur adminUtilisateur = new AdminUtilisateur();
            adminUtilisateur.setAdminID(admin);
            adminUtilisateur.setUtilisateurID(utilisateurSaved);
            adminUtilisateur.setTypeGestion(TypeGestion.Ajouter);

            adminUtilisateurRepository.save(adminUtilisateur);
            logger.info("Association de l'utilisateur avec l'administrateur enregistrée : {}", utilisateurSaved.getUsername());
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur ou de l'association avec l'administrateur", e);
            throw new RuntimeException("Erreur lors de la création de l'utilisateur ou de l'association avec l'administrateur", e);
        }
    }

//    public List<Utilisateur> findUtilisateurs(){
//        return this.utilisateurRepository.findAll();
//    }

    public List<Utilisateur> findUtilisateurs() {
        try {
            List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
            logger.info("Nombre d'utilisateurs trouvés : {}", utilisateurs.size());
            return utilisateurs;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des utilisateurs", e);
            throw new RuntimeException("Erreur lors de la récupération des utilisateurs", e);
        }
    }

//    public Utilisateur findUtilisateur(Integer id){
//        return this.utilisateurRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Utilisateur not found with id: " + id));
//    }


    public Utilisateur findUtilisateur(Integer id) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur not found with id: " + id));
            logger.info("Utilisateur trouvé : {}", utilisateur);
            return utilisateur;
        } catch (EntityNotFoundException e) {
            logger.error("Erreur lors de la récupération de l'utilisateur avec id: {}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération de l'utilisateur avec id: {}", id, e);
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur", e);
        }
    }

//    public boolean utilisateurAuthentifieParEmail(String email, String password){
//        Utilisateur utilisateur = this.utilisateurRepository.findByEmail(email);
//
//        if(utilisateur == null)
//            return false;
//        if(!utilisateur.isAdmin()){
//            return passwordEncoder.matches(password, utilisateur.getPassword());
//        }else{
//            return false;
//        }
//
//    }


    public boolean utilisateurAuthentifieParEmail(String email, String password) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email);

            if (utilisateur == null) {
                logger.warn("Authentification échouée : aucun utilisateur trouvé avec l'email {}", email);
                return false;
            }

            if (utilisateur.isAdmin()) {
                logger.warn("Authentification échouée : l'utilisateur avec l'email {} est un administrateur", email);
                return false;
            }

            boolean isPasswordMatch = passwordEncoder.matches(password, utilisateur.getPassword());

            if (isPasswordMatch) {
                logger.info("Authentification réussie pour l'utilisateur avec l'email {}", email);
            } else {
                logger.warn("Authentification échouée : mot de passe incorrect pour l'utilisateur avec l'email {}", email);
            }

            return isPasswordMatch;
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification pour l'utilisateur avec l'email {}", email, e);
            throw new RuntimeException("Erreur lors de l'authentification", e);
        }
    }

//    public boolean adminAuthentifieParEmail(String email, String password){
//        Utilisateur utilisateur = this.utilisateurRepository.findByEmail(email);
//
//        if(utilisateur == null)
//            return false;
//        if(utilisateur.isAdmin()){
//            return passwordEncoder.matches(password, utilisateur.getPassword());
//        }else{
//            return false;
//        }
//
//    }


    public boolean adminAuthentifieParEmail(String email, String password) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email);

            if (utilisateur == null) {
                logger.warn("Authentification échouée : aucun utilisateur trouvé avec l'email {}", email);
                return false;
            }

            if (!utilisateur.isAdmin()) {
                logger.warn("Authentification échouée : l'utilisateur avec l'email {} n'est pas un administrateur", email);
                return false;
            }

            boolean isPasswordMatch = passwordEncoder.matches(password, utilisateur.getPassword());

            if (isPasswordMatch) {
                logger.info("Authentification réussie pour l'administrateur avec l'email {}", email);
            } else {
                logger.warn("Authentification échouée : mot de passe incorrect pour l'administrateur avec l'email {}", email);
            }

            return isPasswordMatch;
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification de l'administrateur avec l'email {}", email, e);
            throw new RuntimeException("Erreur lors de l'authentification", e);
        }
    }

//    public boolean utilisateurAuthentifieParUsername(String username, String password){
//        Utilisateur utilisateur = this.utilisateurRepository.findByUsername(username);
//
//        if(utilisateur == null)
//            return false;
//        if(!utilisateur.isAdmin()){
//            return passwordEncoder.matches(password, utilisateur.getPassword());
//        }else{
//            return false;
//        }
//
//    }


    public boolean utilisateurAuthentifieParUsername(String username, String password) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByUsername(username);

            if (utilisateur == null) {
                logger.warn("Authentification échouée : aucun utilisateur trouvé avec le nom d'utilisateur {}", username);
                return false;
            }

            // Vérifie si l'utilisateur est un administrateur ou non
            boolean isAdmin = utilisateur.isAdmin();
            if (isAdmin) {
                logger.warn("Authentification échouée : l'utilisateur avec le nom d'utilisateur {} est un administrateur", username);
                return false;
            }

            boolean isPasswordMatch = passwordEncoder.matches(password, utilisateur.getPassword());

            if (isPasswordMatch) {
                logger.info("Authentification réussie pour l'utilisateur avec le nom d'utilisateur {}", username);
            } else {
                logger.warn("Authentification échouée : mot de passe incorrect pour l'utilisateur avec le nom d'utilisateur {}", username);
            }

            return isPasswordMatch;
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification de l'utilisateur avec le nom d'utilisateur {}", username, e);
            throw new RuntimeException("Erreur lors de l'authentification", e);
        }
    }

//    public boolean adminAuthentifieParUsername(String username, String password){
//        Utilisateur utilisateur = this.utilisateurRepository.findByUsername(username);
//
//        if(utilisateur == null)
//            return false;
//        if(utilisateur.isAdmin()) {
//            return passwordEncoder.matches(password, utilisateur.getPassword());
//        }else{
//            return false;
//        }
//
//    }


    public boolean adminAuthentifieParUsername(String username, String password) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByUsername(username);

            if (utilisateur == null) {
                logger.warn("Authentification échouée : aucun utilisateur trouvé avec le nom d'utilisateur {}", username);
                return false;
            }

            // Vérifie si l'utilisateur est un administrateur
            if (utilisateur.isAdmin()) {
                boolean isPasswordMatch = passwordEncoder.matches(password, utilisateur.getPassword());

                if (isPasswordMatch) {
                    logger.info("Authentification réussie pour l'administrateur avec le nom d'utilisateur {}", username);
                } else {
                    logger.warn("Authentification échouée : mot de passe incorrect pour l'administrateur avec le nom d'utilisateur {}", username);
                }

                return isPasswordMatch;
            } else {
                logger.warn("Authentification échouée : l'utilisateur avec le nom d'utilisateur {} n'est pas un administrateur", username);
                return false;
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification de l'administrateur avec le nom d'utilisateur {}", username, e);
            throw new RuntimeException("Erreur lors de l'authentification", e);
        }
    }

}
