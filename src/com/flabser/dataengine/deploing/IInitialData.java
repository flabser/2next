package com.flabser.dataengine.deploing;

import java.util.List;

public interface IInitialData<T, T1> {
	List<T> getData();

	Class<T1> getDAO();
}
