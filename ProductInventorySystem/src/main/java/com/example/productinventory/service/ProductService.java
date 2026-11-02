package com.example.productinventory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productinventory.exception.InvalidProductException;
import com.example.productinventory.exception.ProductNotFoundException;
import com.example.productinventory.model.Product;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private Long nextId = 1L;

    public Product addProduct(Product product) {
        if (product == null) {
            throw new InvalidProductException("Product details are required");
        }

        product.setProductId(nextId);
        nextId++;

        products.add(product);

        return product;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product getProductById(Long id) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            if (product.getProductId().equals(id)) {
                return product;
            }
        }

        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        if (updatedProduct == null) {
            throw new InvalidProductException("Product details are required");
        }

        Product product = getProductById(id);

        product.setProductName(updatedProduct.getProductName());
        product.setCategory(updatedProduct.getCategory());
        product.setStock(updatedProduct.getStock());
        product.setPrice(updatedProduct.getPrice());

        return product;
    }

    public String deleteProduct(Long id) {
        Product product = getProductById(id);
        products.remove(product);

        return "Product deleted successfully";
    }

    public List<Product> searchByCategory(String category) {
        List<Product> result = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            if (product.getCategory().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }

        return result;
    }

    public Product updateStock(Long id, int quantity) {
        Product product = getProductById(id);

        int newStock = product.getStock() + quantity;

        if (newStock < 0) {
            throw new InvalidProductException(
                    "Insufficient stock. Available stock: " + product.getStock());
        }

        product.setStock(newStock);

        return product;
    }

    public List<Product> getLowStockProducts(int limit) {
        List<Product> result = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            if (product.getStock() <= limit) {
                result.add(product);
            }
        }

        return result;
    }

    public double calculateInventoryValue() {
        double total = 0;

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            total = total + (product.getPrice() * product.getStock());
        }

        return total;
    }
}