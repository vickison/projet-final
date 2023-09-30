package com.ide.api.controller;

import com.ide.api.entities.Admin;
import com.ide.api.entities.Auteur;
import com.ide.api.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "admins")
public class AdminController {
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createAadmin(@RequestBody Admin ad){
        this.adminService.createAdmin(ad);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Admin> findAllAdmin(){
        return this.adminService.findAllAdmins();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Admin findAdmin(@PathVariable Integer id){
        return this.adminService.findAdmin(id);
    }
}
