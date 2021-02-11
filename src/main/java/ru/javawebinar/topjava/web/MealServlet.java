package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.dao.MealDao;
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
import java.time.Month;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String LIST_MEALS = "/meals.jsp";
    private static final String EDIT_MEALS = "/editmeals.jsp";

    private IMealDao mealDao;

    @Override
    public void init() {
        mealDao = new MealDao();
        mealDao.add(new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealDao.add(new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealDao.add(new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mealDao.add(new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealDao.add(new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealDao.add(new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealDao.add(new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("meals", getMealsTo());
            forward = LIST_MEALS;
        } else {
            switch (action.toLowerCase()) {
                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    log.debug("DELETE: id = " + id);
                    mealDao.delete(id);
                    log.debug("redirect to meals: after deleted meals with id = " + id);
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
                    log.debug("redirect to meals: no action or unknown action");
                    response.sendRedirect("meals");
                    return;
            }
        }

        log.debug("redirect to " + forward);
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.of(
                LocalDate.parse(req.getParameter("date")),
                LocalTime.parse(req.getParameter("time"))
        );

        String idString = req.getParameter("id");
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if (idString.equals("")) {
            Meal added = mealDao.add(new Meal(0, dateTime, description, calories));
            log.debug(String.format(
                    "ADD: id = %s, dataTime = %s, description = %s, calories = %s",
                    added.getId(),
                    added.getDateTime(),
                    added.getDescription(),
                    added.getCalories()));
        } else {
            int id = Integer.parseInt(idString);
            Meal updated = mealDao.update(new Meal(id, dateTime, description, calories));
            if(updated != null) {
                log.debug(String.format(
                        "UPDATE: meal with id = %s to dataTime = %s, description = %s, calories = %s",
                        updated.getId(),
                        updated.getDateTime(),
                        updated.getDescription(),
                        updated.getCalories()));
            } else {
                log.debug("UPDATE: unknown meal with id = " + id);
            }
        }

        log.debug("redirect to meals");
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
