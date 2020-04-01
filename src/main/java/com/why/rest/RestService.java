package com.why.rest;

//import com.why.pojo.User;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/rest")
public class RestService {
    //	http://127.0.0.1:8080/rest/rest/getText?name=why
    @GET
    @Path("/getText")
    public String getString(@QueryParam("name") String name) {
        System.out.println(name);
        return "Hello, " + name;
    }

    //	http://127.0.0.1:8080/rest/rest/getJson?name=why0000
//    @GET
//    @Path("getJson")
//    @Produces(MediaType.APPLICATION_JSON)
//    public User getJson(@QueryParam("name") String name) {
//        System.out.println(name);
//        User user = new User();
//        user.setUserName(name);
//
//        return user;
//    }
//
//    @POST
//    @Path("jsonTest")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public User addStudent(@NotNull() User user) {
//        return user;
//    }
}
