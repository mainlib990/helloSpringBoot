package kr.ac.hansung.cse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.repo.ProductRepository;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

	@Autowired
	ProductRepository rep;
	
	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		
		try {
		
			Product _product = rep.save(
					new Product(
							product.getName(),
							product.getCategory(),
							product.getPrice(),
							product.getManufacturer(),
							product.getUnitInStock(),
							product.getDescription()
							)
					);
			
			return new ResponseEntity<>(_product, HttpStatus.CREATED);
		
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
			
		}
		
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		
		List<Product> products = new ArrayList<>();
		
		try {
			
			rep.findAll().forEach(products::add);
			
			if (products.isEmpty()) {
				
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			
			return new ResponseEntity<>(products, HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable int id) {
		
		Optional<Product> productData = rep.findById(id);
		
		if (productData.isPresent()) {
			
			return new ResponseEntity<>(productData.get(), HttpStatus.OK);
			
		} else {
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}
		
	}
	
	@GetMapping("/products/category/{category}")
	public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
		
		try {
			
			List<Product> products = rep.findByCategory(category);
			
			if (products.isEmpty()) {
				
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			
			return new ResponseEntity<>(products, HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
			
		}
		
		
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateProduct(
			@PathVariable int id,
			@RequestBody Product product
			) {
		
		Optional<Product> productData = rep.findById(id);
		
		if (productData.isPresent()) {
			
			Product _product = productData.get();
			
			_product.setName(product.getName());
			_product.setCategory(product.getCategory());
			_product.setPrice(product.getPrice());
			_product.setManufacturer(product.getManufacturer());
			_product.setUnitInStock(product.getUnitInStock());
			_product.setDescription(product.getDescription());
			
			return new ResponseEntity<>(rep.save(_product), HttpStatus.OK);
			
		} else {
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}
		
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<HttpStatus> deleteProduct(@PathVariable int id) {
		
		try {
			
			rep.deleteById(id);
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
			
		}
		
	}
	
}
