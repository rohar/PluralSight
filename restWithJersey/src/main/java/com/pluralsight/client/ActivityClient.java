package com.pluralsight.client;

import com.pluralsight.model.Activity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class ActivityClient {
    private Client client;

    public ActivityClient() {
        client = ClientBuilder.newClient();
    }

    public Activity get(String id) {
        WebTarget target = client.target("http://localhost:8080/exercise-services/webapi/");

        Response response = target.path("activities/" + id).request().get(Response.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus() + ": there was an error on the server");
        }

        return response.readEntity(Activity.class);
    }

    public List<Activity> get() {
        WebTarget target = client.target("http://localhost:8080/exercise-services/webapi/");

        List<Activity> activity = target.path("activities").request().get(new GenericType<List<Activity>>() {});

        return activity;
    }

    public Activity create(Activity activity) {
        WebTarget target = client.target("http://localhost:8080/exercise-services/webapi/");

        Response response = target.path("activities/activity").request().post(Entity.entity(activity, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus() + ": there was an error on the server");
        }

        return response.readEntity(Activity.class);
    }

    public Activity update(Activity activity) {
        WebTarget target = client.target("http://localhost:8080/exercise-services/webapi/");

        Response response = target.path("activities/" + activity.getId()).request().put(Entity.entity(activity, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus() + ": there was an error on the server");
        }

        return response.readEntity(Activity.class);
    }

    public void delete(String activityId) {
        WebTarget target = client.target("http://localhost:8080/exercise-services/webapi/");

        Response response = target.path("activities/" + activityId).request().delete(Response.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus() + ": there was an error on the server");
        }
    }
}
