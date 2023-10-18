package com.backend.controllers;

import com.backend.dtos.payment.PaymentRequestDto;
import com.backend.dtos.payment.PaymentResponseDto;
import com.backend.serviceImpls.TicketPaymentServiceImplementation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketPaymentController {

    private TicketPaymentServiceImplementation ticketPaymentServiceImpl;

    @Autowired
    public TicketPaymentController(TicketPaymentServiceImplementation ticketPaymentServiceImpl){
        this.ticketPaymentServiceImpl= ticketPaymentServiceImpl;
    }


    @PostMapping("/makePayment")
    public ResponseEntity<?> makePayment(@Valid @RequestBody PaymentRequestDto paymentRequestDto, HttpSession httpSession){
        paymentRequestDto.setUsername((String)httpSession.getAttribute("CurrentUser"));
        PaymentResponseDto paymentResponseDto= this.ticketPaymentServiceImpl.makePayment(paymentRequestDto);

        return (new ResponseEntity<>(paymentResponseDto, HttpStatus.OK));
    }
}
