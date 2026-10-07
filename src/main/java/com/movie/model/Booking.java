package com.movie.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record Booking(int id, String movieTitle, String customerName, String email,
                      int seats, BigDecimal totalAmount, Timestamp bookedAt) {}
