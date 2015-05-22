package com.flabser.restful;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/hello")
public class Hello {

  @GET
  @Path("{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello(@PathParam("id") String id) {
    return "Hello 2Next " + id;
  }

  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    return "<?xml version=\"1.0\"?>" + "<hello> Hello 2Next" + "</hello>";
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String sayHtmlHello() {
    return "<html> " + "<title>" + "Hello 2Next" + "</title>"
        + "<body><h1>" + "Hello 2Next" + "</body></h1>" + "</html> ";
  }

} 