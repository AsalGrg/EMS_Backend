package com.backend.services;

import com.backend.models.PromoCode;

public interface PromoCodeService {

    PromoCode getPromoCodeByTitle(String title);

    boolean checkPromoCodeExistsByTitle(String title);

    PromoCode savePromoCode(PromoCode promoCode);
}
