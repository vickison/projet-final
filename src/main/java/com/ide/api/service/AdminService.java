package com.ide.api.service;

import com.ide.api.entities.Admin;
import com.ide.api.entities.Auteur;
import com.ide.api.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void createAdmin(Admin admin){
        this.adminRepository.save(admin);
    }

    public List<Admin> findAllAdmins(){
        return this.adminRepository.findAll();
    }

    public Admin findAdmin(Integer id){
        return this.adminRepository.findById(id).get();
    }
}
