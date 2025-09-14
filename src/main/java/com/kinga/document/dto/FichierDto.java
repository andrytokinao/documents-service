package com.kinga.document.dto;

import com.kinga.document.entity.Fichier;
import lombok.Data;

@Data
public class FichierDto {
    Long id;
    private String fileName;
    private String filePath;
    public FichierDto(Fichier fichier) {
        this.id = fichier.getId();
        this.fileName = fichier.getFileName();
        this.filePath = fichier.getFilePath();
    }
}
