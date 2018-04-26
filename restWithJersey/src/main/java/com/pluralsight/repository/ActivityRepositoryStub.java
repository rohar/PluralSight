package com.pluralsight.repository;

import com.pluralsight.model.Activity;
import com.pluralsight.model.ActivitySearch;
import com.pluralsight.model.User;

import java.util.ArrayList;
import java.util.List;

public class ActivityRepositoryStub implements ActivityRepository {


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

    @Override
    public Activity findActivity(String activityId) {
        if ("7777".equals(activityId)) {
            return null; // ie not found
        }

        Activity activity1 = new Activity();
        activity1.setId(activityId);
        activity1.setDescription("Swimming");
        activity1.setDuration(55);

        User user = new User();
        user.setId("5678");
        user.setName("Bryan");

        activity1.setUser(user);

        return activity1;
    }

    @Override
    public void create(Activity activity) {
        // should issue an insert statement to db
    }

    @Override
    public Activity update(Activity activity) {
        Activity currentActivity = findActivity(activity.getId());

        if (currentActivity == null) {
            return null;
        }

        // do DB update
        return activity;
    }

    @Override
    public void delete(String activityId) {
        // delete from activity where activity_id = :activityId
    }

    @Override
    public List<Activity> findByDescription(List<String> descriptions) {
        // select * from activities where description in (x,y,z) etc...

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

    @Override
    public List<Activity> findByDescription(List<String> descriptions, int durationFrom, int durationTo) {
        // select * from activities where description in (x,y,z) and duration between durationFrom and durationTo

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

    @Override
    public List<Activity> findByConstraints(ActivitySearch search) {
        // build SQL based on criteria object, obviously with PreparedStatement to stop and SQL Injection attacks ;-)
        // select * from activities where description in (x,y,z) and duration between durationFrom and durationTo

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
