package com.karan.productservice.utility;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

import com.karan.productservice.model.InventoryUpdate;
import com.karan.productservice.model.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class ProductUtils {

    private ProductUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Example<Product> searchRepositoryByExample(String searchString) {
        ExampleMatcher matcher
                = ExampleMatcher.matchingAny()
                        .withIgnorePaths("id", "stockLevel", "price")
                        .withMatcher("name", contains().ignoreCase())
                        .withMatcher("description", contains().ignoreCase());
        return Example.of(new Product(searchString, searchString), matcher);
    }

    public static boolean canProductBeUpdated(Product updatedProduct, Product productToUpdate) {
        return updatedProduct.getVersion().equals(productToUpdate.getVersion());
    }

    public static boolean updateOrRemoveStock(
            InventoryUpdate inventoryUpdate, Product productToUpdate) {
        boolean updated;
        if (inventoryUpdate.isAddStock()) {
            updated = productToUpdate.addStock(inventoryUpdate.getStock());
        } else {
            updated = productToUpdate.removeStock(inventoryUpdate.getStock());
        }
        return updated;
    }
}
