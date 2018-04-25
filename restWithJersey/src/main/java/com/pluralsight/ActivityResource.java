package com.pluralsight;

import com.pluralsight.model.Activity;
import com.pluralsight.model.User;
import com.pluralsight.repository.ActivityRepository;
import com.pluralsight.repository.ActivityRepositoryStub;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("activities") // http://localhost:8080/exercise-services/webapi/activities
public class ActivityResource {
    private ActivityRepository activityRepository = new ActivityRepositoryStub();

    @POST
    @Path("activity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Activity createActivity(Activity activity) {

        activity.setId("5555");

        activityRepository.create(activity);
        return activity;
    }

    @POST
    @Path("activity") // http://localhost:8080/exercise-services/webapi/activities/activity
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON})
    public Activity createActivityParams(MultivaluedMap<String, String> formParams) {
        String description = formParams.getFirst("description");
        String duration = formParams.getFirst("duration");

        System.out.println(description);
        System.out.println(duration);

        Activity activity = new Activity();
        activity.setId("999");
        activity.setDuration(Integer.parseInt(duration));
        activity.setDescription(description);

        activityRepository.create(activity);

        return activity;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Activity> getAllActivities() {
        return activityRepository.findAllActivities();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{activityId}") // http://localhost:8080/exercise-services/webapi/activities/1234
    public Response getActivity(@PathParam("activityId") String activityId) {
        if (activityId == null || activityId.length() < 4) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        Activity activity = activityRepository.findActivity(activityId);
        if (activity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(activity).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{activityId}/user") // http://localhost:8080/exercise-services/webapi/activities/1234/user
    public User getActivityUser(@PathParam("activityId") String activityId) {
        Activity activity = activityRepository.findActivity(activityId);

        return activity.getUser();
    }
}
