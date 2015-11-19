package com.flabser.dataengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.eztech.util.JavaClassFinder;
import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.deploing.IInitialData;
import com.flabser.dataengine.jpa.DAO;
import com.flabser.dataengine.jpa.IAppEntity;
import com.flabser.dataengine.jpa.IDAO;
import com.flabser.dataengine.jpa.ISimpleAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;

public class PopulatingProcFinder {
	private List<Class<? extends IDAO>> daoClasses;
	private _Session ses;

	public PopulatingProcFinder(AppTemplate template, String contextID) {
		ses = new _Session(template, contextID, new User());

	}

	public void process() {
		JavaClassFinder classFinder = new JavaClassFinder();
		daoClasses = classFinder.findAllMatchingTypes(IDAO.class);
		List<Class<? extends IInitialData>> initClasses = classFinder.findAllMatchingTypes(IInitialData.class);

		for (Class<?> initClass : initClasses) {
			System.out.println("Found " + initClass.getCanonicalName());
			if (!initClass.isInterface()) {
				IInitialData iidClass;
				try {
					iidClass = (IInitialData) Class.forName(initClass.getCanonicalName()).newInstance();
					List<ISimpleAppEntity> entities = iidClass.getData();
					Class<?> dao = iidClass.getDAO();
					for (ISimpleAppEntity entity : entities) {
						Class<IAppEntity> realClass = (Class<IAppEntity>) entity.getClass();
						Instance inst = getDAOInstance(dao, realClass);
						if (inst != null) {
							@SuppressWarnings("rawtypes")
							Object[] paramInt = { entity };
							inst.method.invoke(inst.dao, paramInt);
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
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private Instance getDAOInstance(Class<?> daoClass, Class<?> cl) {
		@SuppressWarnings("rawtypes")
		Class[] intArgsClass = new Class[] { IAppEntity.class, _Session.class };

		Instance inst = null;

		try {
			System.out.println(daoClass.getCanonicalName());
			Method m = null;
			try {
				m = daoClass.getMethod("add", cl);
			} catch (NoSuchMethodException e) {
				m = null;

			}

			if (m != null) {
				Constructor<?> intArgsConstructor = daoClass.getConstructor(intArgsClass);
				IDAO<?, ?> dao = (DAO<?, ?>) intArgsConstructor.newInstance(cl, ses);
				inst = new Instance();
				inst.dao = dao;
				inst.method = m;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inst;
	}

	class Instance {
		IDAO<?, ?> dao;
		Method method;

	}

	public static void main(String[] args) throws Throwable {
		// new PopulatingProcFinder();
	}
}
