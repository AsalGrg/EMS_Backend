package com.backend.serviceImpls;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.ApplyPromoCodeResponseDto;
import com.backend.exceptions.InternalServerError;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Event;
import com.backend.models.PromoCode;
import com.backend.repositories.PromocodeRepository;
import com.backend.services.EventService;
import com.backend.services.PromoCodeService;
import com.backend.services.PromoCodeTypeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromoCodeServiceImplementation implements PromoCodeService {

    private final PromocodeRepository promocodeRepository;
    private final PromoCodeTypeService promoCodeTypeService;
//    private final EventService eventService;

    PromoCodeServiceImplementation
            (PromocodeRepository promocodeRepository,
             PromoCodeTypeService promoCodeTypeService
             ){
        this.promocodeRepository= promocodeRepository;
        this.promoCodeTypeService= promoCodeTypeService;
//        this.eventService= eventService;
    }

    @Override
    public PromoCode getPromoCodeByTitle(String title) {
        return promocodeRepository.findByTitle(title)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid promo code"));
    }

    @Override
    public PromoCode getPromoCodeByid(int id) {
        return promocodeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Promo code does not exists"));
    }

    @Override
    public boolean checkPromoCodeExistsByTitle(String title){
        return promocodeRepository.existsByTitle(title);
    }

    @Override
    public PromoCode getPromoCodeByTitleAndEventId(String title, Event event) {
        return promocodeRepository.findByEventAndTitle(event, title);
    }

    @Override
    public PromoCode savePromoCode(PromoCode promoCode){
        return promocodeRepository.save(promoCode);
    }

    @Override
    public PromoCode addPromocode(AddPromoCodeDto promoCodeDto,Event event) {

        PromoCode promoCodeFromDb= promocodeRepository.findByEventAndTitle(event, promoCodeDto.getName());
        if(promoCodeFromDb!=null) throw new ResourceAlreadyExistsException("Given promo code name exists");

        PromoCode promoCode = PromoCode
                .builder()
                .title(promoCodeDto.getName())
                .discount(promoCodeDto.getDiscount())
                .event(event)
                .expiryDate(promoCodeDto.getExpiryDate())
                .limit(promoCodeDto.getLimit())
                .availableQuantity(promoCodeDto.getLimit())
                .applicableOn(promoCodeDto.getApplicableOn())
                .hasNoEnd(promoCodeDto.isHasNoEnd())
                .promoCodeType(promoCodeTypeService.getPromoCodeByTitle(promoCodeDto.getPromoCodeType()))
                .isActive(true)
                .build();

        return savePromoCode(promoCode);
    }

    @Override
    public ApplyPromoCodeResponseDto isPromoCodeValid(String promoCode, Event event, double total) {


        PromoCode promoCodeDetails = getPromoCodeByTitleAndEventId(promoCode, event);

        if(promoCodeDetails==null){
            throw new ResourceNotFoundException("Invalid promo code");
        }

        if(!promoCodeDetails.isActive()){
            throw new InternalServerError("Promo code not active");
        }

        if (!LocalDate.now().isAfter(promoCodeDetails.getExpiryDate())){
            throw new InternalServerError("Promo code expired");
        }

        if(!promoCodeDetails.getAvailableQuantity().equals("No limit")){
            int availableQuantity= Integer.parseInt(promoCodeDetails.getAvailableQuantity());
            if (availableQuantity <= 0) {
                throw new ResourceNotFoundException("Promo code limit exceed");
            }
        }

        double applicableOn = promoCodeDetails.getApplicableOn().equals("Minimum value")? event.getEventThirdPageDetails().getEventTicket().getTicketPrice(): Integer.parseInt(promoCodeDetails.getApplicableOn());
        if(!(total >=applicableOn)){
            throw new InternalServerError("Promo code cannot be applied");
        }

        double grandTotal;
        double discountAmount;
        String discountType;
        if(promoCodeDetails.getPromoCodeType().getTitle().equals("Cash discount")){
            discountAmount= promoCodeDetails.getDiscount();
            discountType= "Rs "+discountAmount+" OFF";
        } else if (promoCodeDetails.getPromoCodeType().getTitle().equals("Percentage discount")){
            discountAmount= total * (promoCodeDetails.getDiscount()/100);
            discountType= promoCodeDetails.getDiscount()+" % OFF";
        }else {
            throw new InternalServerError("Invalid promo code type");
        }

        grandTotal = total-discountAmount;

        ApplyPromoCodeResponseDto applyPromoCodeResponseDto = ApplyPromoCodeResponseDto
                .builder()
                .promoCode(promoCode)
                .netTotal(total)
                .discountAmount(discountAmount)
                .grandTotal(grandTotal)
                .discountType(discountType)
                .validTill(promoCodeDetails.getExpiryDate())
                .build();

        return applyPromoCodeResponseDto;
    }

    @Override
    public boolean checkPromoCodeExistsInEvent(Event event) {

        List<PromoCode> promoCode = promocodeRepository.findByEvent(event);
        return !promoCode.isEmpty();
    }

    @Override
    public List<PromoCode> getPromoCodesOfEvent(Event event) {
        return promocodeRepository.findByEvent(event);
    }

    @Override
    public void activatePromoCode(int promoCodeId) {
        promocodeRepository.updateActiveStatusById(promoCodeId, true);
    }

    @Override
    public void deactivatePromoCode(int promoCodeId) {
        promocodeRepository.updateActiveStatusById(promoCodeId, false);
    }

    @Override
    public void updatePromoCodeUsed(PromoCode promoCode) {
        if(promoCode.getLimit().equals("No limit")){
            return;
        }
        int availableQuantity = Integer.parseInt(promoCode.getAvailableQuantity());
        availableQuantity= availableQuantity-1;

        promoCode.setAvailableQuantity(String.valueOf(availableQuantity));
        savePromoCode(promoCode);
    }

}
