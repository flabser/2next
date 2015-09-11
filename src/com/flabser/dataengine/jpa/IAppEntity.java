package com.flabser.dataengine.jpa;

import java.util.Date;
import java.util.List;


public interface IAppEntity {

	void setId(long id);

	long getId();

	Long getAuthor();

	void setAuthor(Long author);

	Date getRegDate();

	void setRegDate(Date regDate);


	List<Attachment> getAttachments();

	void setAttachments(List<Attachment> attachments);

}
