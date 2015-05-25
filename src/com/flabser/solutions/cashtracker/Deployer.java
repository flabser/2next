package com.flabser.solutions.cashtracker;

import com.flabser.dataengine.IDeployer;
import com.flabser.users.ApplicationProfile;

public class Deployer implements IDeployer {

	private ApplicationProfile appProfile;
	
	public Deployer(ApplicationProfile appProfile) {
		this.appProfile = appProfile;
	}

	@Override
	public boolean isStructureActual() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int deploy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int remove() {
		// TODO Auto-generated method stub
		return 0;
	}

}
