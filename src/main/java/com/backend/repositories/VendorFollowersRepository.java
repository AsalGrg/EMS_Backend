package com.backend.repositories;

import com.backend.models.VendorFollowers;

import java.util.List;

public interface VendorFollowersRepository {

    VendorFollowers getVendorByFollowerIdAndFollowingId(int followedTo,int followedBy);
    void addFollowers(VendorFollowers vendorFollower);
    void removeFollower(int followedBy, int followedTo);
    int getNoOfFollowers(int userId);
    int getNoOfFollowing(int userId);
    VendorFollowers getVendorFollowerDetailByBothId(int vendorId, int followerId);
    List<VendorFollowers> getAllFollowings(int userId);
}
