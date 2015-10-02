package cashtracker.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import nubis.page.app.InvitationEMail;
import cashtracker.pojo.Errors;
import cashtracker.validation.InvitationValidator;
import cashtracker.validation.ValidationError;

import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.Invitation;
import com.flabser.dataengine.system.entities.constants.InvitationType;
import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;
import com.flabser.users._UsersHelper;

@Path("invitations")
public class InvitationService extends RestProvider {

	private InvitationValidator validator = new InvitationValidator();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Invitation m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		_Session session = getSession();
		User user = getUserSession().currentUser;
		ApplicationProfile ap = user.getApplicationProfile(getAppID());
		User tempUser = _UsersHelper.regTempApplicationUser(ap, m.getEmail());
		if (tempUser.save()) {
			InvitationEMail sve = new InvitationEMail(session, user, m.getMessage(), tempUser);
			if (sve.send()) {
				tempUser.setStatus(UserStatusType.WAITING_FIRST_ENTERING_AFTER_INVITATION);
				if (!tempUser.save()) {
					Errors e = new Errors();
					e.setMessage("User saving error");
					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}
			} else {
				tempUser.setStatus(UserStatusType.INVITATTION_NOT_SENT);
				tempUser.save();
				Errors e = new Errors();
				e.setMessage(UserStatusType.INVITATTION_NOT_SENT.name());
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}
			m.setAppType(ap.appType);
			m.setAppID(ap.appID);
			m.setTempLogin(tempUser.id);
			m.setAuthor(user.id);
			if (m.save()){
				return Response.status(Status.OK).entity(tempUser).build();
			}else{
				Errors e = new Errors();
				e.setMessage(InvitationType.SAVING_FAILED.name());
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}

		} else {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}
	}

}
