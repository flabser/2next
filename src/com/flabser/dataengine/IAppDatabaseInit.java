package com.flabser.dataengine;

import java.util.ArrayList;
import java.util.Map;

public interface IAppDatabaseInit {
    Map<String, String> getTablesDDE();
    ArrayList<String> getInitActions();
}
