package com.flabser.tests.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestJPA {

	private static EntityManagerFactory factory;
	private static EntityManager em;

	public static void main(String[] args) {
		factory = Persistence.createEntityManagerFactory("JPA");
		em = factory.createEntityManager();

	}

}
