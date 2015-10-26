package flabserpromo.rest;

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

	@POST
	@Path("/sendmail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(@FormParam("email") String email, @FormParam("subject") String subj,
			@FormParam("message") String msg, @FormParam("g-recaptcha-response") String grecaptcha) {
		return RestUtil.processSimpleMessage(getSession(), "k-zone@ya.ru", email, subj, msg, grecaptcha);
	}

}
