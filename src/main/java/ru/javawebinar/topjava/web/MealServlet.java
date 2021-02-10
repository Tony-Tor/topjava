package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.mockdb.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String LIST_MEALS = "/meals.jsp";
    private static final String EDIT_MEALS = "/editmeals.jsp";
    private IMealDao mealDao;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void init() throws ServletException {
        mealDao = new MealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward;
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("meals", getMealsTo());
            forward = LIST_MEALS;
        } else {
            switch (action.toLowerCase()) {
                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    mealDao.delete(id);
                    response.sendRedirect("meals");
                    return;
                }
                case "edit": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Meal meal = mealDao.getById(id);
                    request.setAttribute("meal", meal);
                    forward = EDIT_MEALS;
                }
                break;

                case "add": {
                    forward = EDIT_MEALS;
                }
                break;
                default:
                    response.sendRedirect("meals");
                    return;
            }
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to editmeals");
        req.setCharacterEncoding("UTF-8");

        String date = req.getParameter("date");
        String time = req.getParameter("time");
        String description = req.getParameter("description");
        String caloriesString = req.getParameter("calories");
        String idString = req.getParameter("id");

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(date, DATE_FORMATTER), LocalTime.parse(time, TIME_FORMATTER));
        int calories = Integer.parseInt(caloriesString);


        if (idString.equals("")) {
            mealDao.add(new Meal(0, dateTime, description, calories));
        } else {
            int id = Integer.parseInt(idString);
            mealDao.update(new Meal(id, dateTime, description, calories));
        }

        resp.sendRedirect("meals");
    }

    private List<MealTo> getMealsTo() {
        return MealsUtil.filteredByStreams(
                mealDao.getAll(),
                LocalTime.MIN,
                LocalTime.MAX,
                MealsUtil.CALORIES_PER_DAY);
    }

}
