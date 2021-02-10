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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String LIST_MEALS = "/meals.jsp";
    private static final String EDIT_MEALS = "/editmeals.jsp";
    private final IMealDao mealDao;

    public MealServlet() {
        mealDao = new MealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward="";
        String action = request.getParameter("action");

        if(action == null) {
            request.setAttribute("meals", getMealsTo());
            forward = LIST_MEALS;
        } else {
            switch (action.toLowerCase()) {
                case "delete":
                    {
                        int id = Integer.parseInt(request.getParameter("id"));
                        mealDao.deleteMeal(id);
                        request.setAttribute("meals", getMealsTo());
                        forward = LIST_MEALS;
                    }
                    break;
                case "edit":
                    {
                        int id = Integer.parseInt(request.getParameter("id"));
                        Meal meal = mealDao.getMealById(id);
                        request.setAttribute("meal", meal);
                        forward = EDIT_MEALS;
                    }
                    break;

                case "add":
                {
                    forward = EDIT_MEALS;
                }
                break;
            }
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to editmeals");
        String date = req.getParameter("date");
        String time = req.getParameter("time");
        String description = req.getParameter("description");
        String caloriesString = req.getParameter("calories");
        String idString = req.getParameter("id");

        log.debug(description);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter));
        int calories = Integer.parseInt(caloriesString);

        if (idString.equals("")) {
            mealDao.addMeal(new Meal(dateTime, description, calories));
        } else {
            int id = Integer.parseInt(idString);
            mealDao.updateMeal(id, new Meal(dateTime, description, calories));
        }

        req.setAttribute("meals", getMealsTo());
        RequestDispatcher view = req.getRequestDispatcher(LIST_MEALS);
        view.forward(req, resp);
    }

    private List<MealTo> getMealsTo(){
        return MealsUtil.filteredByStreams(
                mealDao.getAllMeals(),
                LocalTime.MIN,
                LocalTime.MAX,
                MealsUtil.CALORIES_PER_DAY);
    }

}
