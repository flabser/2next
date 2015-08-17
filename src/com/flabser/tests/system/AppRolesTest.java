package com.flabser.tests.system;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.tests.InitEnv;

public class AppRolesTest extends InitEnv {

	@Test
	public void test() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		ApplicationProfile appProfile = aa.getParent();
		int initSize = appProfile.getRoles().size();
		appProfile.addRole("role1", "it is role1 desciption");
		appProfile.addRole("role2", "it is role2 desciption");
		appProfile.addRole("role3333333333333333333333", "it is role300 desciption");
		assertTrue(appProfile.save());
		assertTrue(appProfile.getRoles().size() == 3 + initSize);
		init();
		appProfile = aa.getParent();
		assertTrue(appProfile.getRoles().size() == 3 + initSize);
		appProfile.setRoles(new ArrayList());
		assertTrue(appProfile.getRoles().size() == 0);
		assertTrue(appProfile.save());
		init();
		appProfile = aa.getParent();
		assertTrue(appProfile.getRoles().size() == 0);

	}
}
