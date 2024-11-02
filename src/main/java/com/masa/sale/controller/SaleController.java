package com.masa.sale.controller;

import com.masa.sale.model.Sale;
import com.masa.sale.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {
    private ISaleService saleService;

    @GetMapping("/{saleId}")
    public ResponseEntity<Sale> getSale(@PathVariable Long saleId) {
        return saleService.find(saleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/checkout")
    public ResponseEntity<Sale> checkout(@RequestParam Long cartId) {
        return saleService.create(cartId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/cancel")
    public ResponseEntity<Sale> cancel(@RequestParam Long saleId) {
        return saleService.cancel(saleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/confirm")
    public ResponseEntity<Sale> confirm(@RequestParam Long saleId) {
        // TODO: Implement a method to check if the payment was successful
        return saleService.confirm(saleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<Sale>> getSalesByProfileId(@PathVariable Long profileId) {
        return ResponseEntity.ok(saleService.findAllByProfileId(profileId));
    }

    @Autowired
    public void setSaleService(ISaleService saleService) {
        this.saleService = saleService;
    }
}
