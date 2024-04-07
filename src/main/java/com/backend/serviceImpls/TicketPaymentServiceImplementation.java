package com.backend.serviceImpls;

import com.backend.dtos.aboutEvent.TicketDetail;
import com.backend.dtos.payment.PaymentRequestDto;
import com.backend.dtos.payment.PaymentResponseDto;
import com.backend.exceptions.InternalServerError;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.*;
import com.backend.repositories.TicketPaymentRepository;
import com.backend.services.EventService;
import com.backend.services.PromoCodeService;
import com.backend.services.TicketPaymentService;
import com.backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service

public class TicketPaymentServiceImplementation implements TicketPaymentService {

    private final TicketPaymentRepository ticketPaymentRepository;
    private final EventService eventService;
    private final UserService userService;
    private final PromoCodeService promocodeService;

    @Autowired
    public TicketPaymentServiceImplementation
            (TicketPaymentRepository ticketPaymentRepository,
             PromoCodeService promocodeService, UserService userService,
             EventService eventService
             ){

        this.ticketPaymentRepository= ticketPaymentRepository;
        this.promocodeService= promocodeService;
        this.userService= userService;
        this.eventService= eventService;
    }


    @Override
    public void makePayment (PaymentRequestDto paymentRequestDto){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User userDetails= userService.getUserByUsername(username);
        Event eventDetails= eventService.getEventById(paymentRequestDto.getEvent_id());

        EventTicket eventTicket= eventDetails.getEventThirdPageDetails().getEventTicket();
        double total=0.0;
        boolean promoCodeUsed= false;
        int ticketQuantity= paymentRequestDto.getQuantity();

        if(eventTicket.getTicketSold()== eventTicket.getTicketQuantity()){
            throw new ResourceNotFoundException("Tickets are already sold out!");
        }

        if(!eventTicket.getTicketType().getTitle().equals("Donation")){

            double calculatedAmount = getPaymentTotal(eventTicket,eventDetails,paymentRequestDto.getQuantity(), paymentRequestDto.getPromoCode());
            if(paymentRequestDto.getTotal_amount()!= calculatedAmount){
                throw new InternalServerError("Insufficient payment quantity");
            }
            total= calculatedAmount;
            PromoCode promoCode= promocodeService.getPromoCodeByTitleAndEventId(paymentRequestDto.getPromoCode(), eventDetails);
            if(promoCode!=null){
                promoCodeUsed=true;
            }
        }else {
            total = paymentRequestDto.getTotal_amount();
            ticketQuantity= 1;
        }
        log.info("PROMO USED: "+ promoCodeUsed);
        TicketPayment ticketPayment= TicketPayment
                .builder()
                .grand_total(total)
                .promoCodeUsed(promoCodeUsed)
                .purchasedAt(LocalDateTime.now())
                .user(userDetails)
                .event(eventDetails)
                .quantity(ticketQuantity)
                .promoCode(paymentRequestDto.getPromoCode()!=null? promocodeService.getPromoCodeByTitleAndEventId(paymentRequestDto.getPromoCode(), eventDetails): null)
                .build();

        ticketPaymentRepository.save(
                ticketPayment
        );

    }

    public double getPaymentTotal(EventTicket eventTicket, Event event, int ticketQuantity, String promoCode){
        double grandTotal =0;

        if(eventTicket.getTicketType().getTitle().equals("Free")){
            return 0;
        }
        grandTotal = eventTicket.getTicketPrice()*ticketQuantity;

        if(promoCode!=null || !promoCode.isEmpty()){
            PromoCode promoCodeDetails = promocodeService.getPromoCodeByTitleAndEventId(promoCode, event);
            if(promoCodeDetails!=null){
                throw new ResourceNotFoundException("Invalid promo code");
            }else{
                grandTotal= promocodeService.isPromoCodeValid(promoCode, event, grandTotal).getGrandTotal();
            }
        }
        return grandTotal;

    }

}
