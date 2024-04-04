package com.backend.controllers;

import com.backend.dtos.payment.PaymentRequestDto;
import com.backend.dtos.payment.PaymentResponseDto;
import com.backend.serviceImpls.TicketPaymentServiceImplementation;
import com.backend.services.EventService;
import com.backend.services.PromoCodeService;
import com.backend.services.TicketPaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")

@Validated
public class TicketPaymentController {

    private final TicketPaymentService ticketPaymentService;
    private EventService eventService;

    @Autowired
    public TicketPaymentController(
            TicketPaymentService ticketPaymentService,
            EventService eventService
            ){
        this.ticketPaymentService= ticketPaymentService;
        this.eventService= eventService;
    }

    @GetMapping("/applyPromoCode/{promoCodeTitle}/{eventId}/{totalAmount}")
    public ResponseEntity<?> applyPromoCode(@PathVariable("promoCodeTitle") String promoCodeTitle, @PathVariable("eventId") int eventId, @PathVariable("totalAmount") double totalAmount){
        return ResponseEntity.ok(eventService.isPromoCodeValid(promoCodeTitle, eventId, totalAmount));
    }

    @GetMapping("deactivatePromoCode/{promoCodeId}")
    public ResponseEntity<?> deactivatePromoCode(@PathVariable("promoCodeId") int promoCodeId){
        eventService.deactivePromoCode(promoCodeId);
        return ResponseEntity.ok("Promo code deactivate successful");
    }

    @GetMapping("activatePromoCode/{promoCodeId}")
    public ResponseEntity<?> activatePromoCode(@PathVariable("promoCodeId") int promoCodeId){
        eventService.activePromoCode(promoCodeId);
        return ResponseEntity.ok("Promo code active successful");
    }

    @PostMapping("/makePayment")
    public ResponseEntity<?> makePayment(@Valid @RequestBody PaymentRequestDto paymentRequestDto){

        ticketPaymentService.makePayment(paymentRequestDto);

        return ResponseEntity.ok("Ticket details added successfully");
    }
}
