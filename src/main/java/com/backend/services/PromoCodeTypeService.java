package com.backend.services;

import com.backend.models.PromoCodeType;

public interface PromoCodeTypeService {

    PromoCodeType getPromoCodeByTitle(String title);
}
