package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    @Mock
    PaymentService paymentService;

    MockMvc mockMvc;

    Order order;
    Payment payment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        Product product = new Product();
        product.setProductId("prod-id");
        product.setProductName("Test Product");
        product.setProductQuantity(1);

        order = new Order("order-id", Arrays.asList(product), 1L, "Test Author");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("payment-id", "VOUCHER_CODE", order, paymentData);
    }

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderCreate"));
    }

    @Test
    void testCreateOrder() throws Exception {
        mockMvc.perform(post("/order/create")
                        .param("author", "Test Author")
                        .param("productName", "Test Product")
                        .param("productQuantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
    }

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"));
    }

    @Test
    void testOrderHistory_withResults() throws Exception {
        when(orderService.findAllByAuthor("Test Author")).thenReturn(Arrays.asList(order));

        mockMvc.perform(post("/order/history")
                        .param("author", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistoryList"));
    }

    @Test
    void testOrderHistory_empty() throws Exception {
        when(orderService.findAllByAuthor("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/order/history")
                        .param("author", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistoryList"));
    }

    @Test
    void testOrderPayPage_found() throws Exception {
        when(orderService.findById("order-id")).thenReturn(order);

        mockMvc.perform(get("/order/pay/order-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPay"));
    }

    @Test
    void testOrderPayPage_notFound() throws Exception {
        when(orderService.findById("non-existent")).thenReturn(null);

        mockMvc.perform(get("/order/pay/non-existent"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
    }

    @Test
    void testProcessPayment_voucherCode() throws Exception {
        when(orderService.findById("order-id")).thenReturn(order);
        when(paymentService.addPayment(any(), anyString(), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/order-id")
                        .param("method", "VOUCHER_CODE")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPayResult"));
    }

    @Test
    void testProcessPayment_bankTransfer() throws Exception {
        Map<String, String> bankData = new HashMap<>();
        bankData.put("bankName", "BCA");
        bankData.put("referenceCode", "REF123");
        Payment bankPayment = new Payment("payment-id-2", "BANK_TRANSFER", order, bankData);

        when(orderService.findById("order-id")).thenReturn(order);
        when(paymentService.addPayment(any(), anyString(), any())).thenReturn(bankPayment);

        mockMvc.perform(post("/order/pay/order-id")
                        .param("method", "BANK_TRANSFER")
                        .param("bankName", "BCA")
                        .param("referenceCode", "REF123"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPayResult"));
    }

    @Test
    void testProcessPayment_unknownMethod() throws Exception {
        when(orderService.findById("order-id")).thenReturn(order);
        when(paymentService.addPayment(any(), anyString(), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/order-id")
                        .param("method", "CASH"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPayResult"));
    }

    @Test
    void testProcessPayment_orderNotFound() throws Exception {
        when(orderService.findById("non-existent")).thenReturn(null);

        mockMvc.perform(post("/order/pay/non-existent")
                        .param("method", "VOUCHER_CODE")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
    }
}
