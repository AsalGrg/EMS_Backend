package com.backend.serviceImpls;

import com.backend.exceptions.InternalServerError;
import com.backend.models.User;
import com.backend.models.VendorFollowers;
import com.backend.repositories.VendorFollowersRepository;
import com.backend.services.UserService;
import com.backend.services.VendorFollowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class VendorFollowerServiceImpl implements VendorFollowerService {

    private final VendorFollowersRepository vendorFollowersRepository;

    public VendorFollowerServiceImpl(
            VendorFollowersRepository vendorFollowersRepository
    ){;
        this.vendorFollowersRepository = vendorFollowersRepository;
    }

    @Override
    public boolean checkIfHasFollowedVendor(int vendorId, int followerId) {
        VendorFollowers vendorFollowers= getVendorFollowerFromUserToVendor(vendorId, followerId);
        return vendorFollowers != null;
    }

    public VendorFollowers getVendorFollowerFromUserToVendor(int vendorId, int followerId){
        return vendorFollowersRepository.getVendorFollowerDetailByBothId(vendorId, followerId);
    }

    @Override
    public void addFollower(User followedTo, User followedBy) {

        if(followedBy.getUserId()== followedTo.getUserId()){
            throw new InternalServerError("Invalid follow request!");
        }

        VendorFollowers vendorFollower = vendorFollowersRepository.getVendorByFollowerIdAndFollowingId(followedTo.getUserId(),followedBy.getUserId());

        //checking if the vendor is followed by a user already or not, if yes throw an error.
        if(vendorFollower!=null){
            throw new InternalServerError("Multiple user follow requests");
        }

        vendorFollowersRepository.addFollowers(
                VendorFollowers
                        .builder()
                        .followedBy(followedBy)
                        .followedTo(followedTo)
                        .followedDate(LocalDate.now())
                        .build()
        );

    }

    @Override
    public void removeFollower(int vendorId, int userId) {
        vendorFollowersRepository.removeFollower(userId, vendorId);
    }

    @Override
    public int getNoOfFollowers(int userId) {
        return vendorFollowersRepository.getNoOfFollowers(userId);
    }

    @Override
    public int getNoOfFollowing(int userId) {
        return 0;
    }

    @Override
    public List<VendorFollowers> getAllUserFollowings(int userId) {
        return vendorFollowersRepository.getAllFollowings(userId);
    }
}
