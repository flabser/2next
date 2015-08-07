package cashtracker.init;

import java.util.ArrayList;

import com.flabser.dataengine.IAppDatabaseInit;
import com.flabser.restful.Application;


public class FirstAction implements IAppDatabaseInit {

	@Override
	public void setApplicationProfile(Application app) {
		app.addRole("operations", "can add transactions");
		app.addRole("rn_amount_control", "a receiver notifying about exceeding a sum of the cash transaction");
		app.addRole("dailymailing", "a receiver notifying about a day transactions");
		app.addRole("administrator", "administrator");
	}

	@Override
	public ArrayList <String> getTablesDDE() {
		ArrayList <String> result = new ArrayList <>();

		result.add(getBudgetDDE());
		result.add(getAccountDDE());
		result.add(getCategoryDDE());
		result.add(getCostCenterDDE());
		result.add(getTagDDE());
		result.add(getTransactionDDE());

		return result;
	}

	@Override
	public ArrayList <String> getInitActions() {
		// INFO заполнение базовых значений (справочников, ...)
		return null;
	}

	private static String getBudgetDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE BUDGETS ");
		sql.append("(");
		sql.append("  ID         SERIAL NOT NULL,\n");
		sql.append("  NAME       CHARACTER VARYING(128),\n");
		sql.append("  REGDATE    TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),\n");
		sql.append("  OWNER      CHARACTER VARYING(128),\n");
		sql.append("  STATUS     NUMERIC,\n");
		sql.append(" CONSTRAINT BUDGET_ID_PK PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	private static String getAccountDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE ACCOUNTS ");
		sql.append("(");
		sql.append("  ID                  SERIAL NOT NULL,\n");
		sql.append("  NAME                CHARACTER VARYING(128),\n");
		sql.append("  TYPE                SMALLINT,\n");
		sql.append("  CURRENCY_CODE       CHARACTER VARYING(3),\n");
		sql.append("  OPENING_BALANCE     NUMERIC,\n");
		sql.append("  AMOUNT_CONTROL      NUMERIC,\n");
		sql.append("  OWNER               CHARACTER VARYING(128),\n");
		sql.append("  ENABLED             BOOLEAN,\n");
		sql.append("  OBSERVERS           TEXT[],\n");
		sql.append("  INCLUDE_IN_TOTALS   BOOLEAN,\n");
		sql.append("  NOTE                CHARACTER VARYING(256),\n");
		sql.append("  SORT_ORDER          SMALLINT,\n");
		sql.append(" CONSTRAINT ACCOUNT_ID_PK PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	private static String getCategoryDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE CATEGORIES ");
		sql.append("(");
		sql.append("  ID                 SERIAL NOT NULL,\n");
		sql.append("  TRANSACTION_TYPE   SMALLINT,\n");
		sql.append("  PARENT_ID          BIGINT,\n");
		sql.append("  ENABLED            BOOLEAN,\n");
		sql.append("  NAME               CHARACTER VARYING(128),\n");
		sql.append("  NOTE               CHARACTER VARYING(256),\n");
		sql.append("  COLOR              SMALLINT,\n");
		sql.append("  SORT_ORDER         SMALLINT,\n");
		sql.append(" CONSTRAINT CATEGORY_ID_PK PRIMARY KEY (ID), ");
		sql.append(" CONSTRAINT CATEGORY_PARENT_ID_FK FOREIGN KEY (PARENT_ID)\n");
		sql.append("   REFERENCES CATEGORIES (ID) MATCH SIMPLE\n");
		sql.append("   ON UPDATE NO ACTION ON DELETE NO ACTION");
		sql.append(")");
		return sql.toString();
	}

	private static String getCostCenterDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE COSTCENTERS ");
		sql.append("(");
		sql.append("  ID         SERIAL NOT NULL,\n");
		sql.append("  NAME       CHARACTER VARYING(128),\n");
		sql.append(" CONSTRAINT COSTCENTER_ID_PK PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	private static String getTagDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE TAGS ");
		sql.append("(");
		sql.append("  ID         SERIAL NOT NULL,\n");
		sql.append("  NAME       CHARACTER VARYING(32),\n");
		sql.append(" CONSTRAINT TAG_ID_PK PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	private static String getTransactionDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE TRANSACTIONS ");
		sql.append("(");
		sql.append("  ID                  BIGSERIAL NOT NULL,\n");
		sql.append("  \"USER\"            CHARACTER VARYING(128),\n");
		sql.append("  TRANSACTION_TYPE    SMALLINT,\n");
		sql.append("  TRANSACTION_STATE   SMALLINT,\n");
		sql.append("  REG_DATE            TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),\n");
		sql.append("  ACCOUNT_FROM        BIGINT,\n");
		sql.append("  ACCOUNT_TO          BIGINT,\n");
		sql.append("  AMOUNT              NUMERIC,\n");
		sql.append("  EXCHANGE_RATE       NUMERIC,\n");
		sql.append("  CATEGORY            BIGINT,\n");
		sql.append("  COST_CENTER         BIGINT,\n");
		sql.append("  TAGS                CHARACTER VARYING(32)[],\n");
		sql.append("  REPEAT              BOOLEAN,\n");
		sql.append("  EVERY               NUMERIC,\n");
		sql.append("  REPEAT_STEP         SMALLINT,\n");
		sql.append("  START_DATE          TIME WITHOUT TIME ZONE,\n");
		sql.append("  END_DATE            TIME WITHOUT TIME ZONE,\n");
		sql.append("  NOTE                CHARACTER VARYING(256),\n");
		sql.append("  INCLUDE_IN_REPORTS  BOOLEAN,\n");
		sql.append("  BASIS               BYTEA,\n");
		sql.append(" CONSTRAINT TRANSACTION_ID_PK PRIMARY KEY (ID),\n");
		sql.append(" CONSTRAINT TRANSACTION_ACCOUNT_FROM_FK FOREIGN KEY (ACCOUNT_FROM)\n");
		sql.append("   REFERENCES ACCOUNTS (ID) MATCH SIMPLE\n");
		sql.append("   ON UPDATE NO ACTION ON DELETE NO ACTION,\n");
		sql.append(" CONSTRAINT TRANSACTION_ACCOUNT_TO_FK FOREIGN KEY (ACCOUNT_TO)\n");
		sql.append("   REFERENCES ACCOUNTS (ID) MATCH SIMPLE\n");
		sql.append("   ON UPDATE NO ACTION ON DELETE NO ACTION,\n");
		sql.append(" CONSTRAINT TRANSACTION_CATEGORY_FK FOREIGN KEY (CATEGORY)\n");
		sql.append("   REFERENCES CATEGORIES (ID) MATCH SIMPLE\n");
		sql.append("   ON UPDATE NO ACTION ON DELETE NO ACTION,\n");
		sql.append(" CONSTRAINT TRANSACTION_COST_CENTER_FK FOREIGN KEY (COST_CENTER)\n");
		sql.append("   REFERENCES COSTCENTERS (ID) MATCH SIMPLE\n");
		sql.append("   ON UPDATE NO ACTION ON DELETE NO ACTION");
		sql.append(")");
		return sql.toString();
	}
}
