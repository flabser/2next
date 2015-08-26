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
		result.add(getProfileDDE());
		return result;
	}

	@Override
	public ArrayList <String> getInitActions() {

		return null;
	}

	private static String getProfileDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE PROFILES ");
		sql.append("(");
		sql.append("  ID                  BIGSERIAL NOT NULL,\n");
		sql.append("  PARENTID                  BIGSERIAL NOT NULL,\n");
		sql.append("  USER_ID             BIGINT,\n");
		sql.append("  REG_DATE            TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),\n");
		sql.append("  BIRTHDAY            TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),\n");
		sql.append("  SEX                CHARACTER VARYING(3),\n");
		sql.append("  COUNTRY                CHARACTER VARYING(32),\n");
		sql.append("  CITY               CHARACTER VARYING(64)\n");

		sql.append(")");
		return sql.toString();
	}
}
