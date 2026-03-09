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
class PaymentFunctionalTest {

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
    void paymentDetailPage_hasCorrectTitle(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/detail");
        assertEquals("Payment Detail", driver.getTitle());
    }

    @Test
    void paymentAdminListPage_hasCorrectTitle(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/admin/list");
        assertEquals("Payment Admin List", driver.getTitle());
    }

    @Test
    void paymentAdminList_showsPaymentsAfterOrder(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys("AdminListTestAuthor");
        driver.findElement(By.id("productNameInput")).sendKeys("Admin Test Product");
        driver.findElement(By.id("productQuantityInput")).clear();
        driver.findElement(By.id("productQuantityInput")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.id("authorInput")).sendKeys("AdminListTestAuthor");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.cssSelector(".btn-pay")).click();

        driver.findElement(By.id("voucherCodeInput")).sendKeys("ESHOP1234ABC5678");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl + "/payment/admin/list");
        assertEquals("Payment Admin List", driver.getTitle());
        assertTrue(driver.findElements(By.cssSelector("table tbody tr")).size() > 0);
    }

    @Test
    void paymentAdminDetail_hasCorrectTitle(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys("AdminDetailTestAuthor");
        driver.findElement(By.id("productNameInput")).sendKeys("Admin Detail Product");
        driver.findElement(By.id("productQuantityInput")).clear();
        driver.findElement(By.id("productQuantityInput")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.id("authorInput")).sendKeys("AdminDetailTestAuthor");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.cssSelector(".btn-pay")).click();

        driver.findElement(By.id("voucherCodeInput")).sendKeys("ESHOP9876XYZ5432");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl + "/payment/admin/list");
        driver.findElement(By.cssSelector("table tbody tr:last-child .btn-info")).click();

        assertEquals("Payment Admin Detail", driver.getTitle());
    }

    @Test
    void paymentAdminDetail_setStatus_updatesPayment(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys("SetStatusTestAuthor");
        driver.findElement(By.id("productNameInput")).sendKeys("Status Product");
        driver.findElement(By.id("productQuantityInput")).clear();
        driver.findElement(By.id("productQuantityInput")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.id("authorInput")).sendKeys("SetStatusTestAuthor");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.cssSelector(".btn-pay")).click();

        driver.findElement(By.id("voucherCodeInput")).sendKeys("ESHOP1111AAA2222");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl + "/payment/admin/list");
        driver.findElement(By.cssSelector("table tbody tr:last-child .btn-info")).click();

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertEquals("Payment Admin Detail", driver.getTitle());
    }
}
