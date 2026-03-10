package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(UUID.randomUUID().toString(), method, order, paymentData);

        if (method.equals(PaymentMethod.VOUCHER_CODE.getValue())) {
            String status = validateVoucherCode(paymentData.get("voucherCode"));
            payment.setStatus(status);
        } else if (method.equals(PaymentMethod.BANK_TRANSFER.getValue())) {
            String status = validateBankTransfer(paymentData);
            payment.setStatus(status);
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        if (status.equals(PaymentStatus.SUCCESS.getValue())) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else {
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }

    private String validateVoucherCode(String voucherCode) {
        if (voucherCode == null) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (voucherCode.length() != 16) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (!voucherCode.startsWith("ESHOP")) {
            return PaymentStatus.REJECTED.getValue();
        }
        long numericalCount = voucherCode.chars()
                .filter(Character::isDigit)
                .count();
        if (numericalCount != 8) {
            return PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.SUCCESS.getValue();
    }

    private String validateBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        if (bankName == null || bankName.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (referenceCode == null || referenceCode.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.SUCCESS.getValue();
    }
}