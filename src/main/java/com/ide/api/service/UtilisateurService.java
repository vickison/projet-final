package com.ide.api.service;

import com.ide.api.entities.Utilisateur;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.UtilisateurRepository;
import com.ide.api.utilities.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UtilisateurService {
    private UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUtilisateur(Utilisateur utilisateur){
        String hashedPassword = passwordEncoder.encode(utilisateur.getPassword());
        utilisateur.setPassword(hashedPassword);
        this.utilisateurRepository.save(utilisateur);

    }

    public List<Utilisateur> findUtilisateurs(){
        return this.utilisateurRepository.findAll();
    }

    public Utilisateur findUtilisateur(Integer id){
        return this.utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur not found with id: " + id));
    }

    public boolean utilisateurAuthentifie(String email, String password){
        Utilisateur utilisateur = this.utilisateurRepository.findByEmail(email);

        if(utilisateur == null)
            return false;

        return passwordEncoder.matches(password, utilisateur.getPassword());
    }
}
