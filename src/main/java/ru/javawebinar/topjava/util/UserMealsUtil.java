package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        List<Boolean> excesses = new ArrayList<>();
        int caloriesOfEating = 0;

        //отсортировать по дате

        int date = meals.get(0).getDateTime().getDayOfMonth();
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getDateTime().getDayOfMonth() == date) {
                caloriesOfEating += meals.get(i).getCalories();
            } else {
                if (caloriesOfEating > caloriesPerDay) {
                    excesses.add(false);
                } else {
                    excesses.add(true);
                }
                date = meals.get(i).getDateTime().getDayOfMonth();
                caloriesOfEating = meals.get(i).getCalories();
            }
        }
        if (caloriesOfEating > caloriesPerDay) {
            excesses.add(false);
        } else {
            excesses.add(true);
        }
        date = meals.get(0).getDateTime().getDayOfMonth();
        for (int i = 0; i < meals.size(); i++) {
            if (TimeUtil.isBetweenHalfOpen(meals.get(i).getDateTime().toLocalTime(), startTime, endTime)) {
                if (meals.get(i).getDateTime().getDayOfMonth() == date) {
                    userMealWithExcesses.add(new UserMealWithExcess(meals.get(i).getDateTime(), meals.get(i).getDescription(), meals.get(i).getCalories(), excesses.get(0)));
                } else {
                    excesses.remove(0);
                    userMealWithExcesses.add(new UserMealWithExcess(meals.get(i).getDateTime(), meals.get(i).getDescription(), meals.get(i).getCalories(), excesses.get(0)));
                    date = meals.get(i).getDateTime().getDayOfMonth();
                }
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return null;
    }
}