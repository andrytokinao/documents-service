package com.kinga.document.services;

import com.kinga.document.dto.DocumentDto;
import com.kinga.document.entity.Document;
import com.kinga.document.entity.Fichier;
import com.kinga.utils.KingaUtils;
import org.apache.tomcat.util.http.fileupload.util.mime.MimeUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepo;
    private final FichierRepository fichierRepo;
    @Value("${kinga.documents.directory}")
    private String baseDir;

    public DocumentService(DocumentRepository documentRepo, FichierRepository fichierRepo) {
        this.documentRepo = documentRepo;
        this.fichierRepo = fichierRepo;
    }

    public Document createDocument(String  title) throws IOException {
        Document doc = new Document();
        doc.setTitre(title);
        String uuid = UUID.randomUUID().toString();
        Path path = Paths.get(baseDir, uuid);
        Files.createDirectories(path);
        doc.setId(uuid);
        doc.setStoragePath(path.toString());
        return documentRepo.save(doc);
    }

    public Fichier uploadFile(MultipartFile file,String documentId) throws IOException {
        Document doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        String dir = doc.getStoragePath();
        Path filePath = Paths.get(dir, file.getOriginalFilename());
        Files.write(filePath, file.getBytes());

        Fichier f = new Fichier();
        f.setFileName(file.getOriginalFilename());
        f.setFilePath(filePath.toString());
        f.setDocument(doc);


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

    public DocumentDto getById(String id) {
        Document d = documentRepo.findById(id).orElse(null);
        if (d == null) {
            return null;
        }
        return new DocumentDto(d);
    }


    public ResponseEntity<Resource> downloadFile(String filePath) throws MalformedURLException {

        Path zipFilePath = Paths.get(KingaUtils.decodeText(filePath));
        Resource singleResource = new UrlResource(zipFilePath.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + singleResource.getFilename() + "\"")
                .body(singleResource);
    }


}
