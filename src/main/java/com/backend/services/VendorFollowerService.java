package com.backend.services;

import com.backend.models.User;
import com.backend.models.VendorFollowers;

import java.util.List;

public interface VendorFollowerService {

    boolean checkIfHasFollowedVendor(int vendorId, int followerId);
    void addFollower(User followedTo, User followedBy) ;
    void removeFollower(int vendorId, int userId) ;
    int getNoOfFollowers(int userId);
    int getNoOfFollowing(int userId);
    List<VendorFollowers> getAllUserFollowings(int userId);
}
