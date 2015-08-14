package com.flabser.restful.data;

import java.util.ArrayList;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SecureAppEntity extends AppEntity {
	ArrayList<String> editors = new ArrayList<String>();
	ArrayList<String> readers = new ArrayList<String>();

}
