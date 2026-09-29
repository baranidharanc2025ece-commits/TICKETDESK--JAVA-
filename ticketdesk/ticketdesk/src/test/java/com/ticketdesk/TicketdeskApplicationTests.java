package com.ticketdesk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TicketdeskApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testCorsHeaders() throws Exception {
        mockMvc.perform(options("/api/shows")
                        .header("Origin", "http://localhost:5500")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    @Test
    void testCreateAndGetShow() throws Exception {
        String showJson = "{\"title\":\"Movie Night\",\"showTime\":\"2026-10-01T18:00\",\"totalSeats\":100}";

        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(showJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Movie Night"))
                .andExpect(jsonPath("$.totalSeats").value(100));

        mockMvc.perform(get("/api/shows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testCreateStudentViaJsonAndParam() throws Exception {
        String studentJson = "{\"name\":\"Alice\",\"email\":\"alice@example.com\"}";

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));

        mockMvc.perform(post("/api/students")
                        .param("name", "Bob")
                        .param("email", "bob@example.com"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    void testBookingAndAvailableSeats() throws Exception {
        String showJson = "{\"title\":\"Drama Show\",\"showTime\":\"2026-10-02T20:00\",\"totalSeats\":50}";

        String showResponse = mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(showJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number showIdNum = com.jayway.jsonpath.JsonPath.read(showResponse, "$.id");
        long showId = showIdNum.longValue();

        String studentJson = "{\"name\":\"Charlie\",\"email\":\"charlie@example.com\"}";
        String studentResponse = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number studentIdNum = com.jayway.jsonpath.JsonPath.read(studentResponse, "$.id");
        long studentId = studentIdNum.longValue();

        mockMvc.perform(post("/api/bookings")
                        .param("studentId", String.valueOf(studentId))
                        .param("showId", String.valueOf(showId))
                        .param("seats", "2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        mockMvc.perform(get("/api/bookings/show/" + showId + "/available-seats"))
                .andExpect(status().isOk())
                .andExpect(content().string("48"));
    }
}
