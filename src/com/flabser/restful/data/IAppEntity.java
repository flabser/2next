package com.flabser.restful.data;

import java.util.Date;


public interface IAppEntity {

	void setId(long id);

	long getId();

	public long getAuthor();

	public void setAuthor(Long author);

	public Date getRegDate();

	public void setRegDate(Date regDate);

}
