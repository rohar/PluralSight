package com.pluralsight.client;

import com.pluralsight.model.Activity;
import com.pluralsight.model.ActivitySearch;
import com.pluralsight.model.ActivitySearchType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActivitySearchClientTest {

    @Test
    public void testSearchObject() {
        ActivitySearchClient client = new ActivitySearchClient();

        List<String> searchValues = Arrays.asList("Biking", "Running");
        ActivitySearch search = new ActivitySearch();
        search.setSearchType(ActivitySearchType.SEARCH_BY_DURATION_RANGE);
        search.setDescriptions(searchValues);
        search.setDurationFrom(30);
        search.setDurationTo(55);

        List<Activity> activities = client.search(search);
        assertNotNull(activities);
        assertEquals(2, activities.size());
    }

    @Test
    public void testSearch() {
        ActivitySearchClient client = new ActivitySearchClient();

        String param = "description";
        List<String> searchValues = Arrays.asList("swimming", "running");

        List<Activity> activities = client.search(param, searchValues);
        assertNotNull(activities);
        assertEquals(2, activities.size());
    }

    @Test
    public void testSearchWithDuration() {
        ActivitySearchClient client = new ActivitySearchClient();

        String firstParam = "description";
        List<String> searchValues = Arrays.asList("swimming", "running");

        String secondParam = "durationFrom";
        int durationFrom = 30;

        String thirdParam = "durationTo";
        int durationTo = 55;

        List<Activity> activities = client.search(firstParam, searchValues, secondParam, durationFrom, thirdParam, durationTo);
        assertNotNull(activities);
        assertEquals(2, activities.size());
    }
}
