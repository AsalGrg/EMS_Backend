package com.backend.serviceImpls;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Event;
import com.backend.models.PromoCode;
import com.backend.repositories.PromocodeRepository;
import com.backend.services.PromoCodeService;
import org.springframework.stereotype.Service;

@Service
public class PromoCodeServiceImplementation implements PromoCodeService {

    private final PromocodeRepository promocodeRepository;

    PromoCodeServiceImplementation
            (PromocodeRepository promocodeRepository){
        this.promocodeRepository= promocodeRepository;
    }

    @Override
    public PromoCode getPromoCodeByTitle(String title) {
        return promocodeRepository.findByName(title)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid promo code"));
    }

    @Override
    public boolean checkPromoCodeExistsByTitle(String title){
        return promocodeRepository.existsByName(title);
    }

    @Override
    public PromoCode savePromoCode(PromoCode promoCode){
        return promocodeRepository.save(promoCode);
    }

    public PromoCode addPromocode(AddPromoCodeDto promoCodeDto,Event event) {
        if(!event.getEventOrganizer().getUsername().equals(promoCodeDto.getUsername())){
            throw new NotAuthorizedException("You do not have privileges to add promo codes to the event");
        }


        PromoCode promoCode = PromoCode
                .builder()
                .name(promoCodeDto.getName())
                .discount_amount(promoCodeDto.getDiscount_amount())
                .event(event)
                .build();

        return savePromoCode(promoCode);
    }

}
