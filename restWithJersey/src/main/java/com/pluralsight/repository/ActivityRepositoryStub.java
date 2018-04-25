package com.pluralsight.repository;

import com.pluralsight.model.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityResourceStub implements ActivityResource {

    @Override
    public List<Activity> findAllActivities() {
        List<Activity> activities = new ArrayList<>();

        Activity activity1 = new Activity();
        activity1.setDescription("Swimming");
        activity1.setDuration(55);
        activities.add(activity1);

        Activity activity2 = new Activity();
        activity2.setDescription("Cycling");
        activity2.setDuration(120);
        activities.add(activity2);

        return activities;
    }
}
