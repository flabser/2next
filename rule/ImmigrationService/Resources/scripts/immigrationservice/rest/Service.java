package immigrationservice.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.flabser.restful.RestProvider;
import com.flabser.util.RestUtil;

@Path("service")
public class Service extends RestProvider {
	private static final String RECEPIENT_EMAIL = "office@visa-bg.ru";

	@POST
	@Path("/sendmail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(@FormParam("email") String email, @FormParam("subject") String subj,
			@FormParam("message") String msg, @FormParam("g-recaptcha-response") String grecaptcha) {
		return RestUtil.processSimpleMessage(getSession(), RECEPIENT_EMAIL, email, subj, msg, grecaptcha);
	}

}
