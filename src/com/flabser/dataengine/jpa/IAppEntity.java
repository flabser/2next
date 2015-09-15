package com.flabser.dataengine.jpa;

import java.util.Date;


public interface IAppEntity extends ISimpleAppEntity {

	Long getAuthor();

	void setAuthor(Long author);

	Date getRegDate();

	void setRegDate(Date regDate);



}
