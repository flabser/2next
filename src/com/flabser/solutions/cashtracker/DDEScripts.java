package com.flabser.solutions.cashtracker;

public class DDEScripts {

	public static String getBudgetDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE BUDGETS ");
		sql.append("(");
		sql.append("  ID SERIAL NOT NULL,\n");
		sql.append("  NAME CHARACTER VARYING(64),\n");
		sql.append("  REGDATE TIMESTAMP WITHOUT TIME ZONE,\n");
		sql.append("  OWNER CHARACTER VARYING(128),\n");
		sql.append("  STATUS NUMERIC,\n");
		sql.append("  CONSTRAINT BUDGET_ID_PRIMARY_KEY PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	public static String getAccountDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE ACCOUNTS ");
		sql.append("(");
		sql.append("  ID SERIAL NOT NULL,\n");
		sql.append("  NAME TEXT,\n");
		sql.append("  TYPE SMALLINT,\n");
		sql.append("  CURRENCY_CODE CHARACTER VARYING(3),\n");
		sql.append("  OPENING_BALANCE NUMERIC,\n");
		sql.append("  AMOUNT_CONTROL NUMERIC,\n");
		sql.append("  OWNER CHARACTER VARYING(128),\n");
		sql.append("  OBSERVERS TEXT[],\n");
		sql.append("  INCLUDE_IN_TOTALS BOOLEAN,\n");
		sql.append("  NOTE TEXT,\n");
		sql.append("  SORT_ORDER SMALLINT,\n");
		sql.append("  CONSTRAINT ACCOUNT_ID_PRIMARY_KEY PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	public static String getCategoryDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE CATEGORIES ");
		sql.append("(");
		sql.append("  ID SERIAL NOT NULL,\n");
		sql.append("  TRANSACTION_TYPE SMALLINT,\n");
		sql.append("  PARENT_ID BIGINT,\n");
		sql.append("  NAME CHARACTER VARYING(256),\n");
		sql.append("  NOTE TEXT,\n");
		sql.append("  COLOR SMALLINT,\n");
		sql.append("  SORT_ORDER SMALLINT,\n");
		sql.append("  CONSTRAINT CATEGORY_ID_PRIMARY_KEY PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	public static String getCostCenterDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE COSTCENTERS ");
		sql.append("(");
		sql.append("  ID SERIAL NOT NULL,\n");
		sql.append("  NAME CHARACTER VARYING(256),\n");
		sql.append("  CONSTRAINT COSTCENTER_ID_PRIMARY_KEY PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	public static String getTagDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE TAGS ");
		sql.append("(");
		sql.append("  ID SERIAL NOT NULL,\n");
		sql.append("  NAME CHARACTER VARYING(64),\n");
		sql.append("  CONSTRAINT TAG_ID_PRIMARY_KEY PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}

	public static String getTransactionDDE() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE TRANSACTIONS ");
		sql.append("(");
		sql.append("  ID BIGSERIAL NOT NULL,\n");
		sql.append("  \"USER\" CHARACTER VARYING(128),\n");
		sql.append("  TRANSACTION_TYPE SMALLINT,\n");
		sql.append("  TRANSACTION_STATE SMALLINT,\n");
		sql.append("  REG_DATE TIMESTAMP WITHOUT TIME ZONE,\n");
		sql.append("  ACCOUNT_FROM BIGINT,\n");
		sql.append("  ACCOUNT_TO BIGINT,\n");
		sql.append("  AMOUNT NUMERIC,\n");
		sql.append("  EXCHANGE_RATE NUMERIC,\n");
		sql.append("  CATEGORY BIGINT,\n");
		sql.append("  COST_CENTER BIGINT,\n");
		sql.append("  TAGS CHARACTER VARYING(64)[],\n");
		sql.append("  REPEAT BOOLEAN,\n");
		sql.append("  EVERY NUMERIC,\n");
		sql.append("  REPEAT_STEP SMALLINT,\n");
		sql.append("  START_DATE TIME WITHOUT TIME ZONE,\n");
		sql.append("  END_DATE TIME WITHOUT TIME ZONE,\n");
		sql.append("  BASIS BYTEA,\n");
		sql.append("  NOTE TEXT,\n");
		sql.append("  INCLUDE_IN_REPORTS BOOLEAN,\n");
		sql.append("  CONSTRAINT TRANSACTION_ID_PRIMARY_KEY PRIMARY KEY (ID)");
		sql.append(")");
		return sql.toString();
	}
}
