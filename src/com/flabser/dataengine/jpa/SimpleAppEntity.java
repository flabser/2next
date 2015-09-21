package com.flabser.dataengine.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class SimpleAppEntity implements ISimpleAppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;


	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}



}
