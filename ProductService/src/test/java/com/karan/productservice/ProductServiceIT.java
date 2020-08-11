//package com.bearingpoint.productservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@AutoConfigureMockMvc(addFilters = false)
//@SpringBootTest(classes = {ProductService.class})
//@ContextConfiguration(initializers = ProductServiceIT.MongoDbInitializer.class)
//public class ProductServiceIT {

//  private static MongoDbContainer MONGO_DB;
//  private static ObjectMapper objectMapper;
//  private  Product productToGet;
//  @Autowired
//  protected WebApplicationContext context;
//  @Autowired
//  MockMvc mockMvc;
//  @Autowired
//  private ProductsRepository repository;
//
//
//  @BeforeAll
//  private static void setUp() {
//    MONGO_DB = new MongoDbContainer();
//    MONGO_DB.setPortBindings(Collections.singletonList("27017:27017"));
//    MONGO_DB.start();
//    Assert.assertTrue(MONGO_DB.isCreated());
//
//    MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();
//    objectMapper = jacksonMessageConverter.getObjectMapper();
//  }
//
//  @BeforeEach
//  private void productAndDatabaseSetup() {
//    productToGet = new Product("2", "Test", "Test", null, 0, 0,"url",Arrays.asList("cat"));
//    resetDatabase(productToGet);
//  }
//
//  private MvcResult updateProduct(Product productToUpdate) throws Exception {
//    return mockMvc.perform(MockMvcRequestBuilders.put("/products")
//        .contentType(MediaType.APPLICATION_JSON)
//        .characterEncoding("utf8")
//        .content(objectMapper.writeValueAsString(productToUpdate))
//        .accept(MediaType.APPLICATION_JSON))
//        .andReturn();
//  }
//
//  @Test
//  @Disabled
//  @WithMockUser(username = "admin", authorities = {"Admin"})
//  public void UpdateProductMultipleInsertions() throws Throwable {
//
//    //Given
//    MvcResult result = getProductFromDatabase(productToGet.getId());
//    Product productToUpdate = productFromJson(result);
//    productToUpdate.setStockLevel(5);
//    Product originalProduct = productFromJson(result);
//
//    //When
//    MvcResult successfulUpdate = updateProduct(productToUpdate);
//    MvcResult errorUpdateProduct = updateProduct(originalProduct);
//    MvcResult updatedProductFromDatabase = getProductFromDatabase(productToUpdate.getId());
//    Product updatedProduct = productFromJson(updatedProductFromDatabase);
//
//    //Then
//    assertEquals(200, successfulUpdate.getResponse().getStatus());
//    assertEquals(400, errorUpdateProduct.getResponse().getStatus());
//    assertEquals(5, updatedProduct.getStockLevel());
//
//  }
//
//  private Product productFromJson(MvcResult result)
//      throws com.fasterxml.jackson.core.JsonProcessingException, UnsupportedEncodingException {
//    return objectMapper
//        .readValue(result.getResponse().getContentAsString(), Product.class);
//  }
//
//
//  private MvcResult getProductFromDatabase(String id) throws Exception {
//    return mockMvc
//        .perform(MockMvcRequestBuilders.get("/products/" + id)).andReturn();
//  }
//
//
//  private void resetDatabase(Product productToGet) {
//    Optional<Product> deleteProduct = repository.findById(productToGet.getId()).blockOptional();
//    deleteProduct.ifPresent(product -> repository.delete(product));
//    repository.save(productToGet);
//  }
//
//  public static class MongoDbInitializer implements
//      ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//    @Override
//    public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
//
//      TestPropertyValues values = TestPropertyValues.of(
//          "spring.data.mongodb.uri=mongodb://localhost" + ":" + MONGO_DB.getPort(),
//          "spring.data.mongodb.database=products"
//      );
//      values.applyTo(configurableApplicationContext);
//    }
//  }


