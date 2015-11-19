package com.flabser.dataengine.deploing;

import java.lang.reflect.Constructor;
import java.util.List;

import com.eztech.util.JavaClassFinder;
import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.jpa.IAppEntity;
import com.flabser.dataengine.jpa.IDAO;
import com.flabser.script._Session;
import com.flabser.users.User;

public class FillInitialData {
	private _Session ses;

	public FillInitialData(AppTemplate template, String contextID) {
		User user = DatabaseFactory.getSysDatabase().getUser(User.SYSTEM_USER);
		ses = new _Session(template, contextID, user);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process() {
		JavaClassFinder classFinder = new JavaClassFinder();
		List<Class<? extends IInitialData>> populatingClassesList = classFinder
				.findAllMatchingTypes(IInitialData.class);

		for (Class<?> populatingClass : populatingClassesList) {
			if (!populatingClass.isInterface()) {
				IInitialData<IAppEntity, IDAO> pcInstance;
				try {
					pcInstance = (IInitialData) Class.forName(populatingClass.getCanonicalName()).newInstance();
					List<IAppEntity> entities = pcInstance.getData();
					Class<?> daoClass = pcInstance.getDAO();
					for (IAppEntity entity : entities) {
						IDAO dao = getDAOInstance(daoClass, entity.getClass());
						if (dao != null) {
							dao.add(entity);
						}
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private IDAO<?, ?> getDAOInstance(Class<?> daoClass, Class<?> cl) {
		@SuppressWarnings("rawtypes")
		Class[] intArgsClass = new Class[] { _Session.class };
		IDAO<?, ?> dao = null;

		try {
			Constructor<?> intArgsConstructor = daoClass.getConstructor(intArgsClass);
			dao = (IDAO<?, ?>) intArgsConstructor.newInstance(new Object[] { ses });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dao;
	}

}
