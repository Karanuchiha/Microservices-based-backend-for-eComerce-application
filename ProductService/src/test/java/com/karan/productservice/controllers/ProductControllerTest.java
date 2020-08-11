package com.karan.productservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.karan.productservice.repositories.ProductsRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductControllerTest {

    private static final Integer VERSION = 1;
    private static final String UPDATED = "updated";
    private static final String ID = "1";
    private static final int STOCK_LEVEL = 1;
    private static final int STOCK_LEVEL_SECOND = 2;
    private static final String TEST = "test";
    private static final double PRICE = 1;
    private static final String IMAGEURL = "url";
    private static final String CATEGORY = "cat";

    @InjectMocks
    ProductController productController;
    @Mock
    ProductsRepository mockedProductsRepository;

//    @Test
//    public void positiveSearchByProductId() {
//        Product product = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL_SECOND, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        Mono<Product> monoProduct = Mono.just(product);
//        when(mockedProductsRepository.findById("1")).thenReturn(monoProduct);
//        Mono<ResponseEntity<Product>> monoResponse = productController.searchProductById("1");
//        ResponseEntity<Product> responseEntity = monoResponse.block();
//        assertEquals(ResponseEntity.ok(product), responseEntity);
//    }
//
//    @Test
//    public void negativeSearchByProductId() {
//        when(mockedProductsRepository.findById(anyString())).thenReturn(Mono.empty());
//
//        assertEquals(
//                ResponseEntity.badRequest().build().getStatusCode(),
//                productController.searchProductById("1").block().getStatusCode());
//    }
//
//    @Test
//    public void searchProductsFound() {
//        List<Product> products = new ArrayList<>();
//        Product product = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        products.add(product);
//        when(mockedProductsRepository.findAll(ArgumentMatchers.<Example<Product>>any()))
//                .thenReturn(Flux.just(product));
//        ResponseEntity<List<Product>> results = productController.searchProductsNameOrDescription(TEST).block();
//        assertEquals(ResponseEntity.ok(products), results);
//        assertEquals(1, Objects.requireNonNull(results.getBody()).size());
//    }
//
//    @Test
//    public void getAllProductsFound() {
//        List<Product> products = new ArrayList<>();
//        Product product = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        products.add(product);
//        when(mockedProductsRepository.findAll())
//                .thenReturn(Flux.just(product));
//        ResponseEntity<List<Product>> results = productController.getAllProducts().block();
//        assertEquals(ResponseEntity.ok(products), results);
//        assertEquals(1, Objects.requireNonNull(results.getBody()).size());
//    }
//
//    @Test
//    public void searchProductsNotFound() {
//        List<Product> products = new ArrayList<>();
//        when(mockedProductsRepository.findAll(ArgumentMatchers.<Example<Product>>any()))
//                .thenReturn(Flux.empty());
//        ResponseEntity<List<Product>> results = productController.searchProductsNameOrDescription(TEST).block();
//        assertEquals(ResponseEntity.ok(products), results);
//        assertEquals(0, Objects.requireNonNull(results.getBody()).size());
//    }
//
//    @Test
//    public void getAllProductsNotFound() {
//        List<Product> products = new ArrayList<>();
//        when(mockedProductsRepository.findAll())
//                .thenReturn(Flux.empty());
//        ResponseEntity<List<Product>> results = productController.getAllProducts().block();
//        assertEquals(ResponseEntity.ok(products), results);
//        assertEquals(0, Objects.requireNonNull(results.getBody()).size());
//    }
//
//    @Test
//    public void positiveGetProductPrice() {
//        Product product = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        when(mockedProductsRepository.findById(anyString())).thenReturn(Mono.just(product));
//        assertEquals(ResponseEntity.ok(product.getPrice()), productController.getProductPrice("1").block());
//    }
//
//    @Test
//    public void negativeGetProductPrice() {
//        Product product = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        String wrongId = "wrongId";
//        when(mockedProductsRepository.findById(wrongId)).thenReturn(Mono.empty());
//        String correctId = "correctId";
//        assertEquals(ResponseEntity.badRequest().build(), productController.getProductPrice(wrongId).block());
//    }
//
//    @Test
//    public void newProduct() throws Exception {
//        Product product = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        when(mockedProductsRepository.save(product)).thenReturn(Mono.just(product));
//        ResponseEntity<Product> response = productController.newProduct(product).block();
//        assertEquals(ResponseEntity.ok(product), response);
//    }
//
//    @Test
//    public void updateProductFound() throws Exception {
//
//        Product productToUpdate = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        when(mockedProductsRepository.findById(anyString())).thenReturn(Mono.just(productToUpdate));
//        when(mockedProductsRepository.save(productToUpdate)).thenReturn(Mono.just(productToUpdate));
//        Product updateWith = new Product(ID, UPDATED, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        ResponseEntity response = productController.updateProduct(updateWith).block();
//        assertEquals(updateWith.getName(), productToUpdate.getName());
//        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
//    }
//
//    @Test
//    public void updateProductNotFound() throws Exception {
//        when(mockedProductsRepository.findById(anyString())).thenReturn(Mono.empty());
//        Product updateWith = new Product(ID, UPDATED, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        ResponseEntity response = productController.updateProduct(updateWith).block();
//        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
//    }
//
//    @Test
//    public void updateStockAdd() throws Exception {
//        Product productToUpdate = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        InventoryUpdate inventoryUpdate = new InventoryUpdate(true, STOCK_LEVEL);
//        ResponseEntity<Boolean> response = productController
//                .updateStock(productToUpdate, inventoryUpdate);
//        assertEquals(STOCK_LEVEL_SECOND, productToUpdate.getStockLevel());
//        assertEquals(ResponseEntity.ok(true).getStatusCode(), response.getStatusCode());
//        assertEquals(true, response.getBody());
//    }
//
//    @Test
//    public void updateStockTake() throws Exception {
//        Product productToUpdate = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL_SECOND, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        InventoryUpdate inventoryUpdate = new InventoryUpdate(false, STOCK_LEVEL);
//        ResponseEntity<Boolean> response = productController
//                .updateStock(productToUpdate, inventoryUpdate);
//        assertEquals(STOCK_LEVEL, productToUpdate.getStockLevel());
//        assertEquals(ResponseEntity.ok(true).getStatusCode(), response.getStatusCode());
//        assertEquals(true, response.getBody());
//    }
//
//    @Test
//    public void invalidUpdateStockTake() throws Exception {
//        Product productToUpdate = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        InventoryUpdate inventoryUpdate = new InventoryUpdate(false, STOCK_LEVEL_SECOND);
//        ResponseEntity<Boolean> response = productController
//                .updateStock(productToUpdate, inventoryUpdate);
//        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
//    }
//
//    @Test
//    public void deleteProduct() throws Exception {
//        Product productToUpdate = new Product(ID, TEST, TEST, VERSION, STOCK_LEVEL, PRICE, IMAGEURL, Arrays.asList(CATEGORY));
//        when(mockedProductsRepository.findById(anyString())).thenReturn(Mono.just(productToUpdate));
//        when(mockedProductsRepository.delete(productToUpdate)).thenReturn(Mono.empty());
//        ResponseEntity response = productController.deleteProduct(ID).block();
//        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
//    }
//
//    @Test
//    public void deleteProductNotFound() throws Exception {
//        when(mockedProductsRepository.findById(anyString())).thenReturn(Mono.empty());
//        ResponseEntity response = productController.deleteProduct(ID).block();
//        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
//    }
}
