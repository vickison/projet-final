package com.ide.api.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ide.api.entities.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ResponseDocument {
    private Integer id;
    private String titre;
    private String resume;
    private Date datePublication;
    private String formatDocument;
    private long taille;
    private String url;

    private Integer nombreDeTelechargements;
    private Integer nombreDeConsultations;
    private String proprietaire;
    private String langue;
    private Double note;
    private Integer nombreCommentaires;

    public ResponseDocument(Integer id,
                            String titre,
                            String resume,
                            Date datePublication,
                            String formatDocument,
                            long taille,
                            String url,
                            Integer nombreDeTelechargements,
                            Integer nombreDeConsultations,
                            String proprietaire,
                            String langue,
                            Double note,
                            Integer nombreCommentaires) {
        this.id = id;
        this.titre = titre;
        this.resume = resume;
        this.datePublication = datePublication;
        this.formatDocument = formatDocument;
        this.taille = taille;
        this.url = url;
        this.nombreDeTelechargements = nombreDeTelechargements;
        this.nombreDeConsultations = nombreDeConsultations;
        this.proprietaire = proprietaire;
        this.langue = langue;
        this.note = note;
        this.nombreCommentaires = nombreCommentaires;
    }
}
