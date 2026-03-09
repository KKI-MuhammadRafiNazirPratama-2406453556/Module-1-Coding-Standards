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
class OrderFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createOrderPage_hasCorrectTitle(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        assertEquals("Create Order", driver.getTitle());
    }

    @Test
    void orderHistoryPage_hasCorrectTitle(ChromeDriver driver) {
        driver.get(baseUrl + "/order/history");
        assertEquals("Order History", driver.getTitle());
    }

    @Test
    void createOrder_redirectsToHistory(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys("FunctionalTestAuthor");
        driver.findElement(By.id("productNameInput")).sendKeys("Test Product");
        driver.findElement(By.id("productQuantityInput")).clear();
        driver.findElement(By.id("productQuantityInput")).sendKeys("3");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertEquals("Order History", driver.getTitle());
    }

    @Test
    void orderHistory_showsOrdersForAuthor(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys("HistorySearchAuthor");
        driver.findElement(By.id("productNameInput")).sendKeys("History Product");
        driver.findElement(By.id("productQuantityInput")).clear();
        driver.findElement(By.id("productQuantityInput")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.id("authorInput")).sendKeys("HistorySearchAuthor");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertEquals("Order History List", driver.getTitle());
        assertTrue(driver.getPageSource().contains("HistorySearchAuthor"));
    }

    @Test
    void orderPayPage_hasCorrectTitle(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys("PayPageTestAuthor");
        driver.findElement(By.id("productNameInput")).sendKeys("Pay Test Product");
        driver.findElement(By.id("productQuantityInput")).clear();
        driver.findElement(By.id("productQuantityInput")).sendKeys("2");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.id("authorInput")).sendKeys("PayPageTestAuthor");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.cssSelector(".btn-pay")).click();

        assertEquals("Order Pay", driver.getTitle());
    }

    @Test
    void orderPay_voucherCode_showsPaymentResult(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys("VoucherPayAuthor");
        driver.findElement(By.id("productNameInput")).sendKeys("Voucher Product");
        driver.findElement(By.id("productQuantityInput")).clear();
        driver.findElement(By.id("productQuantityInput")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.id("authorInput")).sendKeys("VoucherPayAuthor");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.cssSelector(".btn-pay")).click();

        driver.findElement(By.id("voucherCodeInput")).sendKeys("ESHOP1234ABC5678");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertEquals("Payment Result", driver.getTitle());
        assertTrue(driver.findElement(By.id("paymentId")).getText().length() > 0);
    }
}
