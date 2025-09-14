package com.kinga.document.dto;

import com.kinga.document.entity.Document;
import com.kinga.document.entity.Fichier;
import com.kinga.utils.KingaUtils;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DocumentDto {
    private String id;
    private String titre;

    private String description;
    private List<Fichier> fichiers = new ArrayList<>();
    public DocumentDto(Document document) {
        this.id = document.getId();
        this.titre = document.getTitre();
        this.description = document.getDescription();
        this.setFichiers(document.getFichiers());
    }
    public void setFichiers(List<Fichier> fichiers) {
        if (!CollectionUtils.isEmpty(fichiers)) {
            this.fichiers = fichiers.stream().peek(f->{
                f.setDocument(null);
                f.setFilePath(KingaUtils.encodeText(f.getFilePath()));
            } ).collect(Collectors.toList());
        }
    }
}
