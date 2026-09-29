package com.ticketdesk.service;

import com.ticketdesk.Entity.Show;
import com.ticketdesk.Repository.ShowRepository;
import com.ticketdesk.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    // Create show
    public Show createShow(Show show) {
        return showRepository.save(show);
    }

    // Get all shows
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    // Get show by ID
    public Show getShowById(Long id) {

        return showRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show not found with ID: " + id
                        )
                );
    }
}