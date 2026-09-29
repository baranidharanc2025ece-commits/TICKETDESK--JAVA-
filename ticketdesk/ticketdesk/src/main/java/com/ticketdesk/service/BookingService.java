package com.ticketdesk.service;

import com.ticketdesk.Entity.Booking;
import com.ticketdesk.Entity.BookingStatus;
import com.ticketdesk.Entity.Show;
import com.ticketdesk.Entity.Student;
import com.ticketdesk.Repository.BookingRepository;
import com.ticketdesk.Repository.ShowRepository;
import com.ticketdesk.Repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final StudentRepository studentRepository;
    private final ShowRepository showRepository;

    // Constructor
    public BookingService(
            BookingRepository bookingRepository,
            StudentRepository studentRepository,
            ShowRepository showRepository) {

        this.bookingRepository = bookingRepository;
        this.studentRepository = studentRepository;
        this.showRepository = showRepository;
    }

    // =========================================================
    // CREATE BOOKING
    // =========================================================

    @Transactional
    public Booking createBooking(
            Long studentId,
            Long showId,
            Integer seats) {

        // Check seats value
        if (seats == null || seats <= 0) {
            throw new RuntimeException(
                    "Number of seats must be greater than 0"
            );
        }

        // Find student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found with ID: " + studentId
                        )
                );

        // Find show
        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Show not found with ID: " + showId
                        )
                );

        // Get total confirmed booked seats
        Integer bookedSeats =
                bookingRepository.getBookedSeats(
                        showId,
                        BookingStatus.CONFIRMED
                );

        // If there are no bookings
        if (bookedSeats == null) {
            bookedSeats = 0;
        }

        // Calculate available seats
        int availableSeats =
                show.getTotalSeats() - bookedSeats;

        // Prevent overbooking
        if (seats > availableSeats) {
            throw new RuntimeException(
                    "Not enough seats available. " +
                            "Only " + availableSeats +
                            " seats are remaining."
            );
        }

        // Create new booking
        Booking booking = new Booking();

        booking.setStudent(student);
        booking.setShow(show);
        booking.setSeats(seats);
        booking.setStatus(BookingStatus.CONFIRMED);

        // Save booking
        return bookingRepository.save(booking);
    }

    // =========================================================
    // GET ALL BOOKINGS
    // =========================================================

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // =========================================================
    // GET BOOKING BY ID
    // =========================================================

    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with ID: " + id
                        )
                );
    }

    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    @Transactional
    public Booking cancelBooking(Long id) {

        // Find booking
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with ID: " + id
                        )
                );

        // Check if already cancelled
        if (BookingStatus.CANCELLED.equals(
                booking.getStatus())) {

            throw new RuntimeException(
                    "Booking is already cancelled"
            );
        }

        // Change status to CANCELLED
        booking.setStatus(BookingStatus.CANCELLED);

        // Save updated booking
        return bookingRepository.save(booking);
    }

    // =========================================================
    // GET AVAILABLE SEATS
    // =========================================================

    public int getAvailableSeats(Long showId) {

        // Find show
        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Show not found with ID: " + showId
                        )
                );

        // Get confirmed booked seats
        Integer bookedSeats =
                bookingRepository.getBookedSeats(
                        showId,
                        BookingStatus.CONFIRMED
                );

        if (bookedSeats == null) {
            bookedSeats = 0;
        }

        // Calculate available seats
        return show.getTotalSeats() - bookedSeats;
    }
}