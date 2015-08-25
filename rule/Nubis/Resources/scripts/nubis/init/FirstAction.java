package nubis.init;

import java.util.ArrayList;

import com.flabser.dataengine.IAppDatabaseInit;
import com.flabser.restful.Application;


public class FirstAction implements IAppDatabaseInit {

	@Override
	public void initApplication(Application app) {

	}

	@Override
	public ArrayList <String> getTablesDDE() {
		ArrayList <String> result = new ArrayList <>();



		return result;
	}

	@Override
	public ArrayList <String> getInitActions() {

		return null;
	}


}
