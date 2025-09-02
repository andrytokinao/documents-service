package com.kinga.document.services;

import com.kinga.document.entity.Document;
import com.kinga.document.entity.Fichier;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepo;
    private final FichierRepository fichierRepo;
    private String baseDir = "uploads/documents";

    public DocumentService(DocumentRepository documentRepo, FichierRepository fichierRepo) {
        this.documentRepo = documentRepo;
        this.fichierRepo = fichierRepo;
    }

    public Document createDocument(String  title) {
        Document doc = new Document();
        doc.setTitre(title);
        String uuid = UUID.randomUUID().toString();
        doc.setId(uuid);
        String path = baseDir + File.pathSeparator + uuid;
        doc.setStoragePath(path);
        new File(path).mkdirs();
        return documentRepo.save(doc);
    }

    public Fichier uploadFile(MultipartFile file,String documentId) throws IOException {
        Document doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        String dir = doc.getStoragePath();
        String filePath = dir + File.pathSeparator + file.getOriginalFilename();

        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        Fichier f = new Fichier();
        f.setFileName(file.getOriginalFilename());
        f.setFilePath(filePath);
        f.setDocument(doc);

        doc.getFichiers().add(f);
        documentRepo.save(doc);

        return fichierRepo.save(f);
    }

    public List<Fichier> listFiles(String documentId) {
        Document doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        return doc.getFichiers();
    }

    public UrlResource downloadFile(String documentId, Long fileId) throws IOException {
        Document doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        Fichier fichier = fichierRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Fichier non trouvé"));

        Path path = Paths.get(fichier.getFilePath());
        return new UrlResource(path.toUri());
    }

    public Document getById(String id) {
        return documentRepo.findById(id).orElse(null);
    }
}
