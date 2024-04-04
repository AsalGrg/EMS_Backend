package com.backend.serviceImpls;

import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.PromoCodeType;
import com.backend.repositories.PromoCodeTypeRepository;
import com.backend.services.PromoCodeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoCodeTypeServiceImpl implements PromoCodeTypeService {

    @Autowired
    private PromoCodeTypeRepository promoCodeTypeRepository;

    @Override
    public PromoCodeType getPromoCodeByTitle(String title) {
        PromoCodeType promoCodeType= promoCodeTypeRepository.getPromoCodeTypeByTitle(title);
        if(promoCodeType==null) throw new ResourceNotFoundException("Invalid promo code type");

        return promoCodeType;
    }
}
