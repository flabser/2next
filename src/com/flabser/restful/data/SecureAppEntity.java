package com.flabser.restful.data;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SecureAppEntity extends AppEntity {
	@ElementCollection
	@CollectionTable(name = "editors1",joinColumns =  @JoinColumn(name = "fk_parent"))
	@Column(name = "editors")
	List<String> editors;

	@ElementCollection
	@CollectionTable(name = "readers1",joinColumns =  @JoinColumn(name = "fk_parent"))
	@Column(name = "readers")
	List<String> readers;

	public List<String> getEditors() {
		return editors;
	}
	public void setEditors(List<String> editors) {
		this.editors = editors;
	}
	public List<String> getReaders() {
		return readers;
	}
	public void setReaders(List<String> readers) {
		this.readers = readers;
	}


}
