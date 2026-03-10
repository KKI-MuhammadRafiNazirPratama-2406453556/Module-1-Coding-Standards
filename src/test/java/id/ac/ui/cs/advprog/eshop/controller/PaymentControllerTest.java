package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @InjectMocks
    PaymentController paymentController;

    @Mock
    PaymentService paymentService;

    MockMvc mockMvc;

    Payment payment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        Product product = new Product();
        product.setProductId("prod-id");
        product.setProductName("Test Product");
        product.setProductQuantity(1);

        Order order = new Order("order-id", Arrays.asList(product), 1L, "Test Author");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("payment-id", "VOUCHER_CODE", order, paymentData);
    }

    @Test
    void testPaymentDetailForm_noParam() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"));
    }

    @Test
    void testPaymentDetailForm_emptyParam() throws Exception {
        mockMvc.perform(get("/payment/detail").param("paymentId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"));
    }

    @Test
    void testPaymentDetailForm_withParam() throws Exception {
        mockMvc.perform(get("/payment/detail").param("paymentId", "payment-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/detail/payment-id"));
    }

    @Test
    void testPaymentDetail_found() throws Exception {
        when(paymentService.getPayment("payment-id")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/payment-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetailPage"));
    }

    @Test
    void testPaymentDetail_notFound() throws Exception {
        when(paymentService.getPayment("non-existent")).thenReturn(null);

        mockMvc.perform(get("/payment/detail/non-existent"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/detail"));
    }

    @Test
    void testPaymentAdminList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(Arrays.asList(payment));

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"));
    }

    @Test
    void testPaymentAdminList_empty() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"));
    }

    @Test
    void testPaymentAdminDetail_found() throws Exception {
        when(paymentService.getPayment("payment-id")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/payment-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"));
    }

    @Test
    void testPaymentAdminDetail_notFound() throws Exception {
        when(paymentService.getPayment("non-existent")).thenReturn(null);

        mockMvc.perform(get("/payment/admin/detail/non-existent"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }

    @Test
    void testSetPaymentStatus_found() throws Exception {
        when(paymentService.getPayment("payment-id")).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/payment-id")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/payment-id"));
    }

    @Test
    void testSetPaymentStatus_notFound() throws Exception {
        when(paymentService.getPayment("non-existent")).thenReturn(null);

        mockMvc.perform(post("/payment/admin/set-status/non-existent")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }
}
