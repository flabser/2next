package com.flabser.restful;

import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/provider")
public class RestProvider {
	@GET
	@Path("/print/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Student produceJSON( @PathParam("name") String name ) {

		Student st = new Student(name, "Diaz",22,1);

		return st;

	}

}
