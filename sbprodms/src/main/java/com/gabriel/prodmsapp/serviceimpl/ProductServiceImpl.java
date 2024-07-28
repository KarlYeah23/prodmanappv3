package com.gabriel.prodmsapp.serviceimpl;

import com.gabriel.prodmsapp.entity.ProductData;
import com.gabriel.prodmsapp.model.Product;
import com.gabriel.prodmsapp.repository.ProductDataRepository;
import com.gabriel.prodmsapp.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    ProductDataRepository productDataRepository;

    @Override
    public Product[] getProducts() {
        List<ProductData> productsData = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        productDataRepository.findAll().forEach(productsData::add);

        for (ProductData productData : productsData) {
            Product product = new Product();
            product.setId(productData.getId());
            product.setName(productData.getName());
            product.setDescription(productData.getDescription());
            products.add(product);
        }

        return products.toArray(new Product[0]);
    }

    @Override
    public Product create(Product product) {
        logger.info("add: Input" + product.toString());
        ProductData productData = new ProductData();
        productData.setName(product.getName());
        productData.setDescription(product.getDescription());

        productData = productDataRepository.save(productData);
        logger.info("add: Input" + productData.toString());

        Product newProduct = new Product();
        newProduct.setId(productData.getId());
        newProduct.setName(productData.getName());
        newProduct.setDescription(productData.getDescription());
        return newProduct;
    }

    @Override
    public Product update(Product product) {
        ProductData productData = new ProductData();
        productData.setId(product.getId());
        productData.setName(product.getName());
        productData.setDescription(product.getDescription());

        productData = productDataRepository.save(productData);

        Product newProduct = new Product();
        newProduct.setId(productData.getId());
        newProduct.setName(productData.getName());
        newProduct.setDescription(productData.getDescription());
        return newProduct;
    }

    @Override
    public Product getProduct(Integer id) {
        logger.info("Input id >> " + Integer.toString(id));
        Optional<ProductData> optional = productDataRepository.findById(id);
        if (optional.isPresent()) {
            logger.info("Is present >> ");
            Product product = new Product();
            ProductData productDatum = optional.get();
            product.setId(productDatum.getId());
            product.setName(productDatum.getName());
            product.setDescription(productDatum.getDescription());
            return product;
        }
        logger.info("Failed  >> unable to locate product");
        return null;
    }

    @Override
    public void delete(Integer id) {
        logger.info("Input >> " + Integer.toString(id));
        Optional<ProductData> optional = productDataRepository.findById(id);
        if (optional.isPresent()) {
            ProductData productDatum = optional.get();
            productDataRepository.delete(productDatum);
            logger.info("Successfully deleted >> " + productDatum.toString());
        } else {
            logger.info("Failed  >> unable to locate product id: " + Integer.toString(id));
        }
    }
}
