package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.entity.Payment;
import ru.yandex.practicum.enums.PaymentState;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final double TAX_RATE = 10.0;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final DeliveryClient deliveryClient;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    public PaymentDto create(OrderDto orderDto) {
        float productCost = calculateProductCost(orderDto);
        float deliveryCost = deliveryClient.calculateDeliveryCost(orderDto);
        float feeTotal = calculateFee(productCost);
        float totalPayment = productCost + feeTotal + deliveryCost;

        Payment payment = Payment.builder()
                .totalPayment(totalPayment)
                .deliveryTotal(deliveryCost)
                .feeTotal(feeTotal)
                .paymentState(PaymentState.PENDING)
                .build();

        return saveAndMapToDto(payment);
    }

    @Override
    public float calculateTotalCost(OrderDto orderDto) {
        float productCost = calculateProductCost(orderDto);
        float deliveryCost = deliveryClient.calculateDeliveryCost(orderDto);
        return productCost + calculateFee(productCost) + deliveryCost;
    }

    @Override
    public float calculateProductCost(OrderDto orderDto) {
        return orderDto.products()
                .entrySet()
                .stream()
                .map(entry -> calculateSingleProductCost(entry.getKey(), entry.getValue()))
                .reduce(0f, Float::sum);
    }

    @Override
    public ResponseEntity<Void> refund(UUID orderId) {
        Payment payment = findPaymentByOrderId(orderId);
        updatePaymentState(payment, PaymentState.SUCCESS);
        orderClient.pay(orderId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public void paymentFailed(UUID orderId) {
        Payment payment = findPaymentByOrderId(orderId);
        updatePaymentState(payment, PaymentState.FAILED);
        orderClient.abortOrderByPaymentFailed(orderId);
        throw new NoPaymentFoundException("Payment failed for orderId: " + orderId);
    }


    private float calculateFee(float productCost) {
        return productCost * (float) TAX_RATE / 100;
    }

    private float calculateSingleProductCost(UUID productId, long quantity) {
        ProductDto product = shoppingStoreClient.getProduct(productId);
        return product.price() * quantity;
    }

    private Payment findPaymentByOrderId(UUID orderId) {
        return paymentRepository.findPaymentByOrderId(orderId)
                .orElseThrow(() -> new NoPaymentFoundException("No payment found for orderId: " + orderId));
    }

    private void updatePaymentState(Payment payment, PaymentState state) {
        payment.setPaymentState(state);
        paymentRepository.save(payment);
    }

    private PaymentDto saveAndMapToDto(Payment payment) {
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toPaymentDto(savedPayment);
    }
}