package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EditProductTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testEditProduct_positiveScenario() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sampo Cap Bambang Edisi Baru");
        updatedProduct.setProductQuantity(200);

        Product result = productRepository.edit(updatedProduct);

        assertNotNull(result);
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", result.getProductId());
        assertEquals("Sampo Cap Bambang Edisi Baru", result.getProductName());
        assertEquals(200, result.getProductQuantity());
    }

    @Test
    void testEditProduct_verifyDataIsUpdatedInRepository() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sabun Cap Usep");
        updatedProduct.setProductQuantity(50);
        productRepository.edit(updatedProduct);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals("Sabun Cap Usep", savedProduct.getProductName());
        assertEquals(50, savedProduct.getProductQuantity());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProduct_nonExistentProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Produk Hantu");
        nonExistentProduct.setProductQuantity(999);

        Product result = productRepository.edit(nonExistentProduct);

        assertNull(result);
    }

    @Test
    void testEditProduct_doesNotAffectOtherProducts() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Product updatedProduct1 = new Product();
        updatedProduct1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct1.setProductName("Sampo Cap Bambang V2");
        updatedProduct1.setProductQuantity(150);
        productRepository.edit(updatedProduct1);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct1 = productIterator.next();
        assertEquals("Sampo Cap Bambang V2", savedProduct1.getProductName());
        assertEquals(150, savedProduct1.getProductQuantity());

        Product savedProduct2 = productIterator.next();
        assertEquals("Sampo Cap Usep", savedProduct2.getProductName());
        assertEquals(50, savedProduct2.getProductQuantity());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProduct_onEmptyRepository() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Produk Hantu");
        updatedProduct.setProductQuantity(999);

        Product result = productRepository.edit(updatedProduct);

        assertNull(result);
    }

    @Test
    void testEditProduct_onlyNameChanged() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sampo Cap Bambang Edisi Spesial");
        updatedProduct.setProductQuantity(100);
        productRepository.edit(updatedProduct);

        Product found = productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertNotNull(found);
        assertEquals("Sampo Cap Bambang Edisi Spesial", found.getProductName());
        assertEquals(100, found.getProductQuantity());
    }

    @Test
    void testEditProduct_onlyQuantityChanged() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sampo Cap Bambang");
        updatedProduct.setProductQuantity(300);
        productRepository.edit(updatedProduct);

        Product found = productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertNotNull(found);
        assertEquals("Sampo Cap Bambang", found.getProductName());
        assertEquals(300, found.getProductQuantity());
    }
}