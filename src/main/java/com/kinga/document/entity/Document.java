package com.kinga.document.entity;

import com.kinga.utils.KingaUtils;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Document {
    @Id
    private String id;
    private String title;
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
