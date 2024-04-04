package com.backend.services;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.ApplyPromoCodeResponseDto;
import com.backend.models.Event;
import com.backend.models.PromoCode;

import java.util.List;

public interface PromoCodeService {

    PromoCode getPromoCodeByTitle(String title);
    PromoCode getPromoCodeByid(int id);

    boolean checkPromoCodeExistsByTitle(String title);

    PromoCode getPromoCodeByTitleAndEventId(String title, Event event);

    PromoCode savePromoCode(PromoCode promoCode);

    PromoCode addPromocode(AddPromoCodeDto promoCodeDto, Event event);

    //is promocode valid
    public ApplyPromoCodeResponseDto isPromoCodeValid(String promoCode, Event event, double total);

    boolean checkPromoCodeExistsInEvent (Event event);

    List<PromoCode> getPromoCodesOfEvent(Event event);

    void activatePromoCode(int promoCodeId);
    void deactivatePromoCode(int promoCodeId);
}
