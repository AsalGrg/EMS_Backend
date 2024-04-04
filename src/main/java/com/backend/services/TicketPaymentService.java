package com.backend.services;


import com.backend.dtos.payment.PaymentRequestDto;
import com.backend.dtos.payment.PaymentResponseDto;

public interface TicketPaymentService {

    void makePayment (PaymentRequestDto paymentRequestDto);
}
