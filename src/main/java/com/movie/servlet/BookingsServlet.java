package com.movie.servlet;

import com.movie.dao.BookingDAO;
import com.movie.model.Booking;
import com.movie.util.Html;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/bookings")
public class BookingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        StringBuilder sb = new StringBuilder("<table><tr><th>ID</th><th>Movie</th><th>Name</th><th>Email</th>"
                + "<th>Seats</th><th>Total</th><th>Booked At</th><th></th></tr>");
        try {
            for (Booking b : new BookingDAO().findAll()) {
                sb.append("<tr><td>").append(b.id()).append("</td><td>").append(Html.esc(b.movieTitle()))
                  .append("</td><td>").append(Html.esc(b.customerName())).append("</td><td>").append(Html.esc(b.email()))
                  .append("</td><td>").append(b.seats()).append("</td><td>&#8377;").append(b.totalAmount())
                  .append("</td><td>").append(b.bookedAt())
                  .append("</td><td><form method='post' action='bookings' onsubmit=\"return confirm('Cancel booking?')\">"
                        + "<input type='hidden' name='id' value='").append(b.id())
                  .append("'><button class='danger'>Cancel</button></form></td></tr>");
            }
            sb.append("</table>");
        } catch (Exception e) {
            sb = new StringBuilder("<p class='err'>Database error: " + Html.esc(e.getMessage()) + "</p>");
        }
        resp.getWriter().print(Html.page("All Bookings", sb.toString()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            new BookingDAO().cancel(Integer.parseInt(req.getParameter("id")));
        } catch (Exception e) {
            throw new IOException(e);
        }
        resp.sendRedirect("bookings");
    }
}
