package com.ticketdesk.Repository;

import com.ticketdesk.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT COALESCE(SUM(b.seats), 0)
        FROM Booking b
        WHERE b.show.id = :showId
        AND b.status = :status
    """)
    Integer getBookedSeats(
            @Param("showId") Long showId,
            @Param("status") String status
    );
}