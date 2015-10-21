package com.flabser.dataengine.jpa;

import java.util.List;


public interface IDAO<T, K> {

	T findById(K id);

	List <T> findByIds(List <K> ids);

	List <T> findAll();

	T add(T entity);

	T update(T entity);

	void delete(T entity);
}
