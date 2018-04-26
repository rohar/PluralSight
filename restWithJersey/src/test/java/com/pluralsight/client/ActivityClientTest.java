package com.pluralsight.client;

import com.pluralsight.model.Activity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivityClientTest {

    @Test
    public void testDelete() {
        ActivityClient client = new ActivityClient();

        client.delete("1234");
    }

    @Test
    public void testPut() {
        ActivityClient client = new ActivityClient();

        Activity activity = new Activity();
        activity.setId("3456");
        activity.setDescription("Bikram Yoga");
        activity.setDuration(90);

        activity = client.update(activity);


        assertNotNull(activity);
    }

    @Test
    public void testCreate() {
        ActivityClient client = new ActivityClient();

        Activity activity = new Activity();
        activity.setDescription("Swimming");
        activity.setDuration(90);

        activity = client.create(activity);

        assertNotNull(activity);
        assertNotNull(activity.getId());
    }

    @Test
    public void testGet() {
        ActivityClient client = new ActivityClient();

        Activity activity = client.get("762123");
        assertNotNull(activity);
        assertEquals("762123", activity.getId());
    }

    @Test
    public void testGetList() {
        ActivityClient client = new ActivityClient();

        List<Activity> activities = client.get();

        assertNotNull(activities);
    }

    @Test
    public void testGetWithBadRequest() {
        ActivityClient client = new ActivityClient();

       assertThrows(RuntimeException.class, ()->client.get("123"));
    }

    @Test
    public void testGetWithNotFound() {
        ActivityClient client = new ActivityClient();

        assertThrows(RuntimeException.class, ()->client.get("7777"));
    }


}