package com.backend.repositories;

import com.backend.models.PromoCodeType;

public interface PromoCodeTypeRepository {
    PromoCodeType getPromoCodeTypeByTitle(String title);
}
