package com.movie.servlet;

import com.movie.dao.MovieDAO;
import com.movie.model.Movie;
import com.movie.util.Html;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/movies")
public class MovieListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        StringBuilder sb = new StringBuilder("<table><tr><th>Title</th><th>Genre</th><th>Show Time</th>"
                + "<th>Price</th><th>Seats Left</th><th></th></tr>");
        try {
            for (Movie m : new MovieDAO().findAll()) {
                sb.append("<tr><td>").append(Html.esc(m.title())).append("</td><td>").append(Html.esc(m.genre()))
                  .append("</td><td>").append(Html.esc(m.showTime())).append("</td><td>&#8377;").append(m.price())
                  .append("</td><td>").append(m.availableSeats()).append("</td><td>");
                if (m.availableSeats() > 0) sb.append("<a class='btn' href='book?movieId=").append(m.id()).append("'>Book</a>");
                else sb.append("Sold out");
                sb.append("</td></tr>");
            }
            sb.append("</table>");
        } catch (Exception e) {
            sb = new StringBuilder("<p class='err'>Database error: " + Html.esc(e.getMessage()) + "</p>");
        }
        resp.getWriter().print(Html.page("Now Showing", sb.toString()));
    }
}
