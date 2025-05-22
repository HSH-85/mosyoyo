// src/main/java/com/project/msy/product/controller/OriginController.java
package com.project.msy.product.controller;

import com.project.msy.product.entity.ProductOrigin;
import com.project.msy.product.service.OriginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/origins", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OriginController {

    private final OriginService originService;

    @GetMapping
    public List<ProductOrigin> list() {
        return originService.getAllOrigins();
    }
}
