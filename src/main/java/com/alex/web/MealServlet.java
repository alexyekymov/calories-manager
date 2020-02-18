package com.alex.web;

import com.alex.util.MealsUtil;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("get all");
        req.setAttribute("meals", MealsUtil.getTos(MealsUtil.MEALS, MealsUtil.CALORIES_PER_DAY));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
