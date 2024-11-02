package com.masa.sale.client;

import com.masa.sale.dto.ItemDTO;
import com.masa.sale.fallback.ItemDTOFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(
        name = "ITEM-SERVICE",
        path = "/api/v1/item",
        fallback = ItemDTOFallback.class
)
public interface ItemClient {
    @GetMapping("/all/enabled")
    Set<ItemDTO> getEnabledItems();

    @GetMapping("/{itemId}")
    ItemDTO getItem(@PathVariable Long itemId);

    @PutMapping("/increment")
    ItemDTO incrementStock(@RequestParam Long itemId, @RequestParam Integer quantity);

    @PutMapping("/decrement")
    ItemDTO decrementStock(@RequestParam Long itemId, @RequestParam Integer quantity);

    @GetMapping("/exists/{itemId}")
    Boolean existsById(@PathVariable Long itemId);
}
