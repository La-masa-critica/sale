package com.masa.sell.controller;

import com.masa.sell.model.Sale;
import com.masa.sell.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sell")
public class SellController {
    private ISaleService saleService;

    @PutMapping("/checkout")
    public ResponseEntity<Sale> checkout(@RequestParam Long cartId) {
        return saleService.createSale(cartId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/cancel")
    public ResponseEntity<Boolean> cancel(@RequestParam Long saleId) {
        return ResponseEntity.ok(saleService.cancelSale(saleId));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Sale> confirm(@RequestParam Long saleId) {
        // TODO: Implement a method to check if the payment was successful
        return saleService.confirmSale(saleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Autowired
    public void setSaleService(ISaleService saleService) {
        this.saleService = saleService;
    }
}
