package com.ide.api.dto;

import com.ide.api.entities.AuteurDocument;
import com.ide.api.entities.CategorieDocument;
import com.ide.api.entities.DocumentTag;
import com.ide.api.entities.UtilisateurDocument;
import com.ide.api.enums.Langue;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DocumentDTO {
    private String resume;
    private Langue langue;
    private MultipartFile file;

    public DocumentDTO() {
    }

    public DocumentDTO(String resume,
                       Langue langue,
                       MultipartFile file) {
        this.resume = resume;
        this.langue = langue;
        this.file = file;
    }

    // Getters and setters


    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Langue getLangue() {
        return langue;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }


    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
