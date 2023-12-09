package com.ide.api.service;

import com.ide.api.entities.Utilisateur;
import com.ide.api.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UtilisateurService {
    private UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurService(PasswordEncoder passwordEncoder, UtilisateurRepository utilisateurRepository) {
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

    public boolean utilisateurAuthentifieParEmail(String email, String password){
        Utilisateur utilisateur = this.utilisateurRepository.findByEmail(email);

        if(utilisateur == null)
            return false;
        if(!utilisateur.isAdmin()){
            return passwordEncoder.matches(password, utilisateur.getPassword());
        }else{
            return false;
        }

    }

    public boolean adminAuthentifieParEmail(String email, String password){
        Utilisateur utilisateur = this.utilisateurRepository.findByEmail(email);

        if(utilisateur == null)
            return false;
        if(utilisateur.isAdmin()){
            return passwordEncoder.matches(password, utilisateur.getPassword());
        }else{
            return false;
        }

    }

    public boolean utilisateurAuthentifieParUsername(String username, String password){
        Utilisateur utilisateur = this.utilisateurRepository.findByUsername(username);

        if(utilisateur == null)
            return false;
        if(!utilisateur.isAdmin()){
            return passwordEncoder.matches(password, utilisateur.getPassword());
        }else{
            return false;
        }

    }

    public boolean adminAuthentifieParUsername(String username, String password){
        Utilisateur utilisateur = this.utilisateurRepository.findByUsername(username);

        if(utilisateur == null)
            return false;
        if(utilisateur.isAdmin()) {
            return passwordEncoder.matches(password, utilisateur.getPassword());
        }else{
            return false;
        }

    }

}
