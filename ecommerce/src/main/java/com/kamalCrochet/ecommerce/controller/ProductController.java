package com.kamalCrochet.ecommerce.controller;

import com.kamalCrochet.ecommerce.model.Product;
import com.kamalCrochet.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/")
public class ProductController {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @Autowired
    private ProductService productService;

    // Buyer & Seller Pages
    @GetMapping("buyer/home")
    public String buyerHome() {
        return "buyer-home";
    }

    @GetMapping("seller/dashboard")
    public String sellerDashboard() {
        return "seller-dashboard";
    }

    // Show All Products
    @GetMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    // Show Add Product Form
    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    // Add a New Product
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.write(path, bytes);
                product.setImagePath("/images/" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }
}
