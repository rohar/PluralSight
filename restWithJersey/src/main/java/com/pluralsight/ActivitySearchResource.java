package com.pluralsight;

import com.pluralsight.model.Activity;
import com.pluralsight.model.ActivitySearch;
import com.pluralsight.repository.ActivityRepository;
import com.pluralsight.repository.ActivityRepositoryStub;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;

@Path("search/activities") // http://localhost:8080/exercise-services/webapi/search/activities
public class ActivitySearchResource {
    private ActivityRepository activityRepository = new ActivityRepositoryStub();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchForActivities(ActivitySearch search) {
        System.out.println(search);

        List<Activity> activities = activityRepository.findByConstraints(search);

        if (activities.size() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(new GenericEntity<List<Activity>>(activities) {}).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchForActivities(@QueryParam(value = "description") List<String> descriptions) {
        System.out.println(descriptions);

        List<Activity> activities = activityRepository.findByDescription(descriptions);

        if (activities.size() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(new GenericEntity<List<Activity>>(activities) {}).build();
    }

    @GET
    @Path("/duration")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchForActivities(@QueryParam(value = "description") List<String> descriptions,
                                        @QueryParam(value = "durationFrom") int durationFrom,
                                        @QueryParam(value = "durationTo") int durationTo) {
        System.out.println(descriptions + ", " + durationFrom + ", " + durationTo);

        List<Activity> activities = activityRepository.findByDescription(descriptions, durationFrom, durationTo);

        if (activities.size() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(new GenericEntity<List<Activity>>(activities) {
        }).build();
    }

}
