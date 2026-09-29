package com.ticketdesk.Controller;

import com.ticketdesk.Entity.Show;
import com.ticketdesk.service.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    // Constructor
    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    // =====================================================
    // CREATE SHOW
    // =====================================================

    @PostMapping
    public ResponseEntity<Show> createShow(
            @RequestBody Show show) {

        Show savedShow = showService.createShow(show);

        return new ResponseEntity<>(
                savedShow,
                HttpStatus.CREATED
        );
    }

    // =====================================================
    // GET ALL SHOWS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<Show>> getAllShows() {

        List<Show> shows = showService.getAllShows();

        return ResponseEntity.ok(shows);
    }

    // =====================================================
    // GET SHOW BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowById(
            @PathVariable Long id) {

        Show show = showService.getShowById(id);

        return ResponseEntity.ok(show);
    }
}