package com.movie.dao;

import com.movie.model.Booking;
import com.movie.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    /** Books seats inside one transaction. Returns booking id, or -1 if not enough seats. */
    public int book(int movieId, String name, String email, int seats) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                BigDecimal price;
                String upd = "UPDATE movies SET available_seats = available_seats - ? "
                           + "WHERE id = ? AND available_seats >= ? RETURNING price";
                try (PreparedStatement ps = c.prepareStatement(upd)) {
                    ps.setInt(1, seats); ps.setInt(2, movieId); ps.setInt(3, seats);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { c.rollback(); return -1; }
                        price = rs.getBigDecimal("price");
                    }
                }
                String ins = "INSERT INTO bookings (movie_id, customer_name, email, seats, total_amount) "
                           + "VALUES (?,?,?,?,?) RETURNING id";
                int id;
                try (PreparedStatement ps = c.prepareStatement(ins)) {
                    ps.setInt(1, movieId); ps.setString(2, name); ps.setString(3, email);
                    ps.setInt(4, seats); ps.setBigDecimal(5, price.multiply(BigDecimal.valueOf(seats)));
                    try (ResultSet rs = ps.executeQuery()) { rs.next(); id = rs.getInt(1); }
                }
                c.commit();
                return id;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public List<Booking> findAll() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, m.title FROM bookings b JOIN movies m ON m.id = b.movie_id ORDER BY b.id DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Booking(rs.getInt("id"), rs.getString("title"), rs.getString("customer_name"),
                        rs.getString("email"), rs.getInt("seats"), rs.getBigDecimal("total_amount"),
                        rs.getTimestamp("booked_at")));
            }
        }
        return list;
    }

    /** Cancels a booking and gives the seats back. */
    public void cancel(int bookingId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int movieId = -1, seats = 0;
                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM bookings WHERE id = ? RETURNING movie_id, seats")) {
                    ps.setInt(1, bookingId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) { movieId = rs.getInt(1); seats = rs.getInt(2); }
                    }
                }
                if (movieId != -1) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "UPDATE movies SET available_seats = available_seats + ? WHERE id = ?")) {
                        ps.setInt(1, seats); ps.setInt(2, movieId);
                        ps.executeUpdate();
                    }
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
}
