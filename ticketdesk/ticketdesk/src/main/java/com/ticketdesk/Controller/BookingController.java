package com.ticketdesk.Controller;

import com.ticketdesk.Entity.Booking;
import com.ticketdesk.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // CREATE BOOKING
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestParam Long studentId,
            @RequestParam Long showId,
            @RequestParam Integer seats) {

        Booking booking =
                bookingService.createBooking(
                        studentId,
                        showId,
                        seats
                );

        return new ResponseEntity<>(
                booking,
                HttpStatus.CREATED
        );
    }

    // GET ALL BOOKINGS
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }

    // GET BOOKING BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBookingById(id)
        );
    }

    // CANCEL BOOKING
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(id)
        );
    }

    // CHECK AVAILABLE SEATS
    @GetMapping("/show/{showId}/available-seats")
    public ResponseEntity<Integer> getAvailableSeats(
            @PathVariable Long showId) {

        return ResponseEntity.ok(
                bookingService.getAvailableSeats(showId)
        );
    }
}