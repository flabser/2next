package cashtracker.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cashtracker.model.Invitation;
import cashtracker.validation.InvitationValidator;
import cashtracker.validation.ValidationError;

import com.flabser.restful.RestProvider;


@Path("invitations")
public class InvitationService extends RestProvider {

	private InvitationValidator validator = new InvitationValidator();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Invitation m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.ok(ve).status(Status.BAD_REQUEST).build();
		}

		return Response.ok().build();
	}
}
