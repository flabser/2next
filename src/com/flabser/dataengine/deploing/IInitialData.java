package com.flabser.dataengine.deploing;

import java.util.List;

import com.flabser.localization.LanguageType;
import com.flabser.localization.Vocabulary;

public interface IInitialData<T, T1> {
	List<T> getData(LanguageType lang, Vocabulary vocabulary);

	Class<T1> getDAO();
}
