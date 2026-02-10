package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}.
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");

        driver.findElement(By.id("nameInput")).clear();
        driver.findElement(By.id("nameInput")).sendKeys("Sampo Cap Bambang");

        driver.findElement(By.id("quantityInput")).clear();
        driver.findElement(By.id("quantityInput")).sendKeys("100");

        driver.findElement(By.tagName("button")).click();
        String pageTitle = driver.getTitle();
        assertEquals("Product List", pageTitle);

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Sampo Cap Bambang"));
        assertTrue(pageSource.contains("100"));
    }

    @Test
    void createProduct_pageTitle_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");
        String pageTitle = driver.getTitle();

        assertEquals("Create New Product", pageTitle);
    }

    @Test
    void createProduct_appearsInProductList(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");
        driver.findElement(By.id("nameInput")).clear();
        driver.findElement(By.id("nameInput")).sendKeys("Sabun Cap Usep");
        driver.findElement(By.id("quantityInput")).clear();
        driver.findElement(By.id("quantityInput")).sendKeys("50");
        driver.findElement(By.tagName("button")).click();

        String productName = driver.findElement(By.xpath("//table/tbody/tr[last()]/td[1]"))
                .getText();
        String productQuantity = driver.findElement(By.xpath("//table/tbody/tr[last()]/td[2]"))
                .getText();

        assertEquals("Sabun Cap Usep", productName);
        assertEquals("50", productQuantity);
    }

    @Test
    void createMultipleProducts_allAppearInProductList(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");
        driver.findElement(By.id("nameInput")).clear();
        driver.findElement(By.id("nameInput")).sendKeys("Sampo Cap Bambang");
        driver.findElement(By.id("quantityInput")).clear();
        driver.findElement(By.id("quantityInput")).sendKeys("100");
        driver.findElement(By.tagName("button")).click();

        driver.get(baseUrl + "/product/create");
        driver.findElement(By.id("nameInput")).clear();
        driver.findElement(By.id("nameInput")).sendKeys("Sabun Cap Usep");
        driver.findElement(By.id("quantityInput")).clear();
        driver.findElement(By.id("quantityInput")).sendKeys("50");
        driver.findElement(By.tagName("button")).click();

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Sampo Cap Bambang"));
        assertTrue(pageSource.contains("100"));
        assertTrue(pageSource.contains("Sabun Cap Usep"));
        assertTrue(pageSource.contains("50"));
    }
}