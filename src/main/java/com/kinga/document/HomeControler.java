package com.kinga.document;

import com.kinga.document.entity.Fichier;
import com.kinga.document.services.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeControler {
    private final DocumentService documentService;

    public HomeControler(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping({"/", "//**", "/list/**","/new","/**"})
    public String index(){
        return "/index.html";
    }

}
