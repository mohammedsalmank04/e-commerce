package com.practice.ecommerce.service;

import com.practice.ecommerce.Repositories.CategoryRepository;
import com.practice.ecommerce.Repositories.ProductRepository;
import com.practice.ecommerce.exceptions.APIExceptions;
import com.practice.ecommerce.exceptions.ResourceNotFoundException;
import com.practice.ecommerce.model.Category;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.payload.ProductDTO;
import com.practice.ecommerce.payload.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;



    @Override
    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {

        Product productInDatabase = productRepository.findByProductName(productDto.getProductName());
        if(productInDatabase != null){
            throw new APIExceptions("Product with name: " + productInDatabase.getProductName() + " already exits");
        }
        Product product = modelMapper.map(productDto, Product.class);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("category","categoryId",categoryId));

        double specicalPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());

        product.setCategory(category);
        product.setSpecialPrice(specicalPrice);
        product.setImage("default.png");

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        //to check if no products
        List<Product> productList = productRepository.findAll();
        if(productList.isEmpty()){
            throw new APIExceptions("No Products present, add some products");
        }
       List<ProductDTO> productDTOSList = productList.stream().map(product ->
            modelMapper.map(product,ProductDTO.class)).toList();
       ProductResponse productResponse = new ProductResponse();
       productResponse.setProductsList(productDTOSList);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        //produc size is 0
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("category","categoryId",categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        if(products.isEmpty()){
            throw new APIExceptions("There is no products in: " + category.getCategoryName() + ".");
        }
        List<ProductDTO> productDTOList = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductsList(productDTOList);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {

        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');

        if(products.isEmpty()){
            throw new APIExceptions("There is  no product that starts with: " + keyword + ".");
        }
        List<ProductDTO> productDTOList = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductsList(productDTOList);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDto, Long productId) {
        Product product = modelMapper.map(productDto,Product.class);
        Product oldProduct = productRepository.findById(productId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Product", "ProductId", productId)
                );
        oldProduct.setProductName(product.getProductName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setQuantity(product.getQuantity());
        oldProduct.setDiscount(product.getDiscount());
        double specialPrice =  product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());

        productRepository.save(oldProduct);

        return modelMapper.map(oldProduct,ProductDTO.class);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Product","ProductId",productId)
                );
        productRepository.delete(product);
        return "Product Deleted";
    }

    @Override
    public ProductDTO updateImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product","ProductId",productId));


        String fileName = fileService.uploadImage(path, image);

        product.setImage(fileName);

        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDTO.class);

    }




}
