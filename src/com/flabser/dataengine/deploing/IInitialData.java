package com.flabser.dataengine.deploing;

import java.util.List;

import com.flabser.dataengine.jpa.ISimpleAppEntity;

public interface IInitialData {
	List<ISimpleAppEntity> getData();

	Class<?> getDAO();
}
