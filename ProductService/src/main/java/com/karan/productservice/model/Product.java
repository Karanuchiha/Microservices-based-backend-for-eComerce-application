package com.karan.productservice.model;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @NotEmpty
    @Size(max = 50)
    private String name;
    @NotEmpty
    @Size(max = 50)
    private String description;
    @Version
    private Integer version;
    @Min(value = 0)
    private int stockLevel;

    @Min(value = 0)
    private double price;
//
//    @NotEmpty
//    @Size(max = 300)
//    private String imageURL;

//    @NotEmpty
//    private List<String> categories;

    public Product(String description, String name, int stockLevel, double price, String imageURL, List<String> categories) {
        this.description = description;
        this.name = name;
        this.stockLevel = stockLevel;
        this.price = price;
//        this.imageURL = imageURL;
//        this.categories = categories;
    }

    public Product(String description, String name) {
        this.id = null;
        this.description = description;
        this.name = name;
        this.stockLevel = 0;
        this.price = 0;
    }

    public boolean removeStock(int stockLevel) {
        if (this.stockLevel - stockLevel >= 0) {
            this.stockLevel = this.getStockLevel() - stockLevel;
            return true;
        }
        return false;
    }

    public boolean addStock(int stockLevel) {
        this.stockLevel = this.getStockLevel() + stockLevel;
        return true;
    }

    public void updateProduct(Product updatedProduct) {
        this.setName(updatedProduct.getName());
        this.setStockLevel(updatedProduct.getStockLevel());
        this.setDescription(updatedProduct.getDescription());
        this.setPrice(updatedProduct.getPrice());
//        this.setImageURL(updatedProduct.getImageURL());
//        this.setCategories(updatedProduct.getCategories());
    }
}
