package com.pluralsight.repository;

import com.pluralsight.model.Activity;

import java.util.List;

public interface ActivityResource {
    List<Activity> findAllActivities();
}
