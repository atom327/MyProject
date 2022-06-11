package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<UserMeal> userMeals = meals.stream().sorted((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime())).collect(Collectors.toList());
        List<Boolean> excesses = new ArrayList<>();
        int caloriesOfEating = 0;
        int date = userMeals.get(0).getDateTime().getDayOfMonth();
        for (int i = 0; i < userMeals.size(); i++) {
            if (userMeals.get(i).getDateTime().getDayOfMonth() == date) {
                caloriesOfEating += userMeals.get(i).getCalories();
            } else {
                if (caloriesOfEating > caloriesPerDay) {
                    excesses.add(false);
                } else {
                    excesses.add(true);
                }
                date = userMeals.get(i).getDateTime().getDayOfMonth();
                caloriesOfEating = userMeals.get(i).getCalories();
            }
        }
        if (caloriesOfEating > caloriesPerDay) {
            excesses.add(false);
        } else {
            excesses.add(true);
        }
        date = userMeals.get(0).getDateTime().getDayOfMonth();
        for (int i = 0; i < userMeals.size(); i++) {
            if (TimeUtil.isBetweenHalfOpen(userMeals.get(i).getDateTime().toLocalTime(), startTime, endTime)) {
                if (userMeals.get(i).getDateTime().getDayOfMonth() == date) {
                    userMealWithExcesses.add(new UserMealWithExcess(userMeals.get(i).getDateTime(), userMeals.get(i).getDescription(), userMeals.get(i).getCalories(), excesses.get(0)));
                } else {
                    excesses.remove(0);
                    userMealWithExcesses.add(new UserMealWithExcess(userMeals.get(i).getDateTime(), userMeals.get(i).getDescription(), userMeals.get(i).getCalories(), excesses.get(0)));
                    date = userMeals.get(i).getDateTime().getDayOfMonth();
                }
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return null;
    }
}