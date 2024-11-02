package com.masa.sale.fallback;

import com.masa.sale.dto.ItemDTO;
import com.masa.sale.client.ItemClient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class ItemDTOFallback implements ItemClient {
    @Override
    public Set<ItemDTO> getEnabledItems() {
        return Collections.emptySet();
    }

    @Override
    public ItemDTO getItem(Long itemId) {
        return null;
    }

    @Override
    public ItemDTO decrementStock(Long itemId, Integer quantity) {
        return null;
    }

    @Override
    public Boolean existsById(Long itemId) {
        return false;
    }

    @Override
    public ItemDTO incrementStock(Long itemId, Integer quantity) {
        return null;
    }
}
