package com.flabser.dataengine.ft;

import java.util.Set;

public interface IFTIndexEngine {
	@Deprecated
	int ftSearchCount(Set<String> complexUserID, String absoluteUserID, String keyWord) ;
	@Deprecated
	StringBuffer ftSearch(Set<String> complexUserID, String absoluteUserID,  String keyWord, int offset, int pageSize) throws FTIndexEngineException;

	int updateFTIndex() throws FTIndexEngineException;	
	
}
