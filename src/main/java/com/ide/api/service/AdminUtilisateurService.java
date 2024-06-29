package com.ide.api.service;

import com.ide.api.entities.AdminUtilisateur;
import com.ide.api.entities.Utilisateur;
import com.ide.api.repository.AdminUtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminUtilisateurService {
    private AdminUtilisateurRepository adminUtilisateurRepository;

    public AdminUtilisateurService(AdminUtilisateurRepository adminUtilisateurRepository) {
        this.adminUtilisateurRepository = adminUtilisateurRepository;
    }

    public void createAdminUtilisateur(AdminUtilisateur adminUtilisateur){
        this.adminUtilisateurRepository.save(adminUtilisateur);
    }

    public Optional<AdminUtilisateur> findByAdminAndUtil(Utilisateur admin, Utilisateur utilisateur){
        return this.adminUtilisateurRepository.findByadminIDAndUtilisateurID(admin, utilisateur);
    }
}
