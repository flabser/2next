package com.flabser.dataengine.deploying;

import java.lang.reflect.Constructor;
import java.util.List;

import com.eztech.util.JavaClassFinder;
import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.jpa.IAppEntity;
import com.flabser.dataengine.jpa.IDAO;
import com.flabser.localization.LanguageType;
import com.flabser.script._Session;
import com.flabser.users.User;

public class PostInitialData {
	private _Session ses;
	private AppTemplate template;

	public PostInitialData(AppTemplate template, String contextID) {
		User user = DatabaseFactory.getSysDatabase().getUser(User.SYSTEM_USER);
		ses = new _Session(template, contextID, user);
		this.template = template;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(String lang) {
		JavaClassFinder classFinder = new JavaClassFinder();
		List<Class<? extends IInitialData>> populatingClassesList = classFinder.findAllMatchingTypes(IInitialData.class);

		for (Class<?> populatingClass : populatingClassesList) {
			if (!populatingClass.isInterface()) {
				IInitialData<IAppEntity, IDAO> pcInstance;
				try {
					pcInstance = (IInitialData) Class.forName(populatingClass.getCanonicalName()).newInstance();
					List<IAppEntity> entities = pcInstance.getData(LanguageType.valueOf(lang), template.vocabulary);
					Class<?> daoClass = pcInstance.getDAO();
					IDAO dao = getDAOInstance(daoClass);
					if (dao != null) {
						for (IAppEntity entity : entities) {
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

	private IDAO<?, ?> getDAOInstance(Class<?> daoClass) {
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
