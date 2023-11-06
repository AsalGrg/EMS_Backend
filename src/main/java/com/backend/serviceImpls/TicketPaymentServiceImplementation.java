package com.backend.serviceImpls;

import com.backend.dtos.payment.PaymentRequestDto;
import com.backend.dtos.payment.PaymentResponseDto;
import com.backend.exceptions.InternalServerError;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.exceptions.PaymentException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Event;
import com.backend.models.PromoCode;
import com.backend.models.TicketPayment;
import com.backend.models.User;
import com.backend.repositories.TicketPaymentRepository;
import com.backend.services.TicketPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class TicketPaymentServiceImplementation implements TicketPaymentService {

    private final TicketPaymentRepository ticketPaymentRepository;
    private final EventServiceImplementation eventService;
    private final UserServiceImplementation userService;
    private final PromoCodeServiceImplementation promocodeService;

    @Autowired
    public TicketPaymentServiceImplementation
            (TicketPaymentRepository ticketPaymentRepository, EventServiceImplementation eventService,
             PromoCodeServiceImplementation promocodeService, UserServiceImplementation userService){

        this.ticketPaymentRepository= ticketPaymentRepository;
        this.eventService= eventService;
        this.promocodeService= promocodeService;
        this.userService= userService;
    }

    @Override
    public PaymentResponseDto makePayment(PaymentRequestDto paymentRequestDto){

        //object of the ticketPayment that to be saved in database
        TicketPayment ticketPayment= new TicketPayment();

        PaymentResponseDto paymentResponseDto= new PaymentResponseDto();

        if(paymentRequestDto.getUsername()==null){
            throw new NotAuthorizedException();
        }

        User userDetails = userService.getUserByUsername(paymentRequestDto.getUsername());

        Event eventDetails= eventService.getEventByName(paymentRequestDto.getEvent_name());

        //checking if the event seats are available
        if(eventDetails.getSeats()==0){
            throw new ResourceNotFoundException("Sorry, the tickets are already sold out!");
        }

        double net_total= eventDetails.getEntryFee()* paymentRequestDto.getQuantity();
        double grand_total;
        double savedAmount;

        //if promo code used
        if(paymentRequestDto.getPromoCode()!=null){
            PromoCode promoCode= promocodeService.getPromoCodeByTitle(paymentRequestDto.getPromoCode());

            //checking if the promocode is applicable for the given event or not;
            if(!promoCode.getEvent().getName().equals(paymentRequestDto.getPromoCode())){
                throw new ResourceNotFoundException("No promo code with title "+ paymentRequestDto.getPromoCode()+ " for event title "+ paymentRequestDto.getEvent_name());
            }

            grand_total= net_total - promoCode.getDiscount_amount()*paymentRequestDto.getQuantity();

            //setting promo code related attributes of ticket payment
            ticketPayment.setPromocodeUsed(true);
            ticketPayment.setPromoCode(promoCode);

            paymentResponseDto.setPromoCode(promoCode.getName());
        }

        //if promo code not used
        else{
            grand_total= net_total;
        }

        savedAmount= net_total-grand_total;

        if(grand_total != paymentRequestDto.getTotal_amount()) throw new PaymentException("Insufficient payment amount");

        ticketPayment.setQuantity(paymentRequestDto.getQuantity());
        ticketPayment.setNet_total(net_total);
        ticketPayment.setGrand_total(grand_total);
        ticketPayment.setEvent(eventDetails);
        ticketPayment.setUser(userDetails);

        //setting attribute value for payment response
        paymentResponseDto.setUsername(paymentRequestDto.getUsername());
        paymentResponseDto.setEvent_name(paymentRequestDto.getEvent_name());
        paymentResponseDto.setUnit_price(eventDetails.getEntryFee());
        paymentResponseDto.setQuantity(paymentRequestDto.getQuantity());
        paymentResponseDto.setNet_total(net_total);
        paymentResponseDto.setGrand_total(grand_total);
        paymentResponseDto.setAmount_saved(savedAmount);


        TicketPayment ticketPaymentSaved= this.ticketPaymentRepository.save(ticketPayment);

        if(ticketPaymentSaved.getId()==null){
            throw new InternalServerError();
        }

        //after the payment is done, the event detials such as available seats and ticketsSold are updated here
        eventDetails.setSeats(eventDetails.getSeats()-paymentRequestDto.getQuantity());
        eventDetails.setTicketSold(eventDetails.getTicketSold()+paymentRequestDto.getQuantity());
        eventService.saveEvent(eventDetails);


        return paymentResponseDto;
    }
}
