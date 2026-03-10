package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailForm(@RequestParam(required = false) String paymentId, Model model) {
        if (paymentId != null && !paymentId.isEmpty()) {
            return "redirect:/payment/detail/" + paymentId;
        }
        return "paymentDetail";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            return "redirect:/payment/detail";
        }
        model.addAttribute("payment", payment);
        return "paymentDetailPage";
    }

    @GetMapping("/admin/list")
    public String paymentAdminList(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "paymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String paymentAdminDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            return "redirect:/payment/admin/list";
        }
        model.addAttribute("payment", payment);
        return "paymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable String paymentId,
                                   @RequestParam String status) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            return "redirect:/payment/admin/list";
        }
        paymentService.setStatus(payment, status);
        return "redirect:/payment/admin/detail/" + paymentId;
    }
}
