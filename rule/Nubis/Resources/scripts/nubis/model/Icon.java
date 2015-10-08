package nubis.model;

import javax.persistence.Entity;

import com.flabser.dataengine.jpa.AppEntity;

@Entity
public class Icon extends AppEntity {

	private String appID;
	private int position;


	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}

}
