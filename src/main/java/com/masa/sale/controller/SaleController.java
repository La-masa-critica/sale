package com.masa.sale.controller;

import com.masa.sale.model.Cart;
import com.masa.sale.model.Sale;
import com.masa.sale.service.ISaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sale")
@AllArgsConstructor
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

    @PutMapping("/fail")
    public ResponseEntity<Cart> fail(@RequestParam Long saleId) {
        return saleService.failSale(saleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping()
    public ResponseEntity<List<Sale>> getSalesByProfileId(@RequestParam Long profileId) {
        return ResponseEntity.ok(saleService.findAllByProfileId(profileId));
    }
}
