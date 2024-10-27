package com.masa.sell.client;

import com.masa.sell.DTO.ItemDTO;
import com.masa.sell.fallback.ItemDTOFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(
        name = "item-service",
        path = "/ap1/v1/item",
        fallback = ItemDTOFallback.class
)
public interface ItemClient {
    @GetMapping("/all/enabled")
    Set<ItemDTO> getEnabledItems();

    @GetMapping("/{itemId}")
    ItemDTO getItem(@PathVariable Long itemId);

    @PutMapping("/update")
    Boolean updateItem(@RequestParam Long itemId, @RequestParam Integer quantity);
}
