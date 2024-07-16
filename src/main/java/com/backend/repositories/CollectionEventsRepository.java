package com.backend.repositories;

import com.backend.models.CollectionEvents;
import com.backend.models.EventCollection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionEventsRepository extends JpaRepository<CollectionEvents, Integer> {

    CollectionEvents findCollectionEventsByEvent_IdAndCollection_Id(int eventId, int collectionId);

    List<CollectionEvents> findAllByCollection_Id(int collectionId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CollectionEvents ce WHERE ce.collection.id= :collectionId AND ce.event.id= :eventId")
    void deleteEventFromEventCollection(@Param("collectionId") int collectionId, @Param("eventId") int eventId);
}
