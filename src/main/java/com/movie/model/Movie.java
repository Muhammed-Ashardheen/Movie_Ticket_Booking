package com.movie.model;

import java.math.BigDecimal;

public record Movie(int id, String title, String genre, String showTime, BigDecimal price, int availableSeats) {}
