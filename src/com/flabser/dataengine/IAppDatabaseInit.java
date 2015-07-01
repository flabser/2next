package com.flabser.dataengine;

import java.util.ArrayList;

public interface IAppDatabaseInit {
    ArrayList<String> getTablesDDE();
    ArrayList<String> getInitActions();
}
