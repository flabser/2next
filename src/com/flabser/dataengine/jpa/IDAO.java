package com.flabser.dataengine.jpa;

public interface IDAO<T, K> {

	T findById(K id);
}
