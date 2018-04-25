package com.pluralsight;

import com.pluralsight.model.Activity;
import com.pluralsight.model.User;
import com.pluralsight.repository.ActivityRepository;
import com.pluralsight.repository.ActivityRepositoryStub;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("activities") // http://localhost:8080/exercise-services/webapi/activities
public class ActivityResource {
    private ActivityRepository activityRepository = new ActivityRepositoryStub();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Activity> getAllActivities() {
        return activityRepository.findAllActivities();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{activityId}") // http://localhost:8080/exercise-services/webapi/activities/1234
    public Activity getActivity(@PathParam("activityId") String activityId) {
        return activityRepository.findActivity(activityId);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{activityId}/user") // http://localhost:8080/exercise-services/webapi/activities/1234/user
    public User getActivityUser(@PathParam("activityId") String activityId) {
        Activity activity = activityRepository.findActivity(activityId);

        return activity.getUser();
    }
}
