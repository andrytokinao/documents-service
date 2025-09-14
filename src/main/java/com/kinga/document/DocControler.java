package com.kinga.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinga.document.entity.Document;
import com.kinga.document.entity.Fichier;
import com.kinga.document.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocControler {

    private final DocumentService  documentService;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam(name = "documentId") String documentId) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Le fichier est vide.");
        }
        try{
            Fichier fichier = documentService.uploadFile(file, documentId);
            ObjectMapper  mapper = new ObjectMapper();
            return ResponseEntity.ok().body(mapper.writeValueAsString(fichier));

        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors du téléchargement du fichier."+ex.getMessage());
        }
    }
    @GetMapping("/create")
    public ResponseEntity<Document> createDocument(@RequestParam("title") String title) throws IOException {
        return ResponseEntity.ok(documentService.createDocument(title));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable String id) {
        return ResponseEntity.ok(documentService.getById(id));
    }

}
