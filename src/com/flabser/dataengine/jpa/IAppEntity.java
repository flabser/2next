package com.flabser.dataengine.jpa;

import java.util.Date;
import java.util.List;


public interface IAppEntity extends ISimpleAppEntity {

	Long getAuthor();

	void setAuthor(Long author);

	Date getRegDate();

	void setRegDate(Date regDate);

	List<AttachmentEntity> getAttachments();

	void setAttachments(List<AttachmentEntity> attachments);

}
