package com.kinga.document.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue
    private String id;
    private String titre;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String description;
    private  String filePath;
    private String fileName;
    private String storagePath;
    @Column(name = "coure_model")
    private String courseModel;
    @Column(name = "user_id")
    private String creatorId;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fichier> fichiers = new ArrayList<>();


}
