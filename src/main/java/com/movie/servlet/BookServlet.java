package com.movie.servlet;

import com.movie.dao.BookingDAO;
import com.movie.dao.MovieDAO;
import com.movie.model.Movie;
import com.movie.util.Html;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/book")
public class BookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String body;
        try {
            Movie m = new MovieDAO().findById(Integer.parseInt(req.getParameter("movieId")));
            if (m == null) {
                body = "<p class='err'>Movie not found.</p>";
            } else {
                body = "<p><b>" + Html.esc(m.title()) + "</b> | " + Html.esc(m.showTime())
                     + " | &#8377;" + m.price() + " per seat | " + m.availableSeats() + " seats left</p>"
                     + "<form method='post' action='book'>"
                     + "<input type='hidden' name='movieId' value='" + m.id() + "'>"
                     + "<label>Name</label><input name='name' required>"
                     + "<label>Email</label><input type='email' name='email' required>"
                     + "<label>Seats</label><input type='number' name='seats' min='1' max='" + m.availableSeats()
                     + "' value='1' required>"
                     + "<br><br><button type='submit'>Confirm Booking</button></form>";
            }
        } catch (Exception e) {
            body = "<p class='err'>Error: " + Html.esc(e.getMessage()) + "</p>";
        }
        resp.getWriter().print(Html.page("Book Tickets", body));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String body;
        try {
            int movieId = Integer.parseInt(req.getParameter("movieId"));
            int seats = Integer.parseInt(req.getParameter("seats"));
            int id = new BookingDAO().book(movieId, req.getParameter("name"), req.getParameter("email"), seats);
            body = id > 0
                ? "<p class='ok'>Booking confirmed! Your booking ID is <b>#" + id + "</b>.</p>"
                : "<p class='err'>Sorry, not enough seats available.</p>";
        } catch (Exception e) {
            body = "<p class='err'>Booking failed: " + Html.esc(e.getMessage()) + "</p>";
        }
        resp.getWriter().print(Html.page("Booking Result", body));
    }
}
