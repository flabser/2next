package com.flabser.solutions.cashtracker;

public class DDEScripts {
	
	public static String getBudgetDDE() {
        String createString = "CREATE TABLE BUDGET\n" +
                "(\n" +
                "  ID SERIAL NOT NULL,\n" +
                "  NAME CHARACTER VARYING(64),\n" +
                "  REGDATE TIMESTAMP WITHOUT TIME ZONE,\n" +
                "  OWNER CHARACTER VARYING(128),\n" +
                "  STATUS NUMERIC,\n" +
                "  CONSTRAINT BUDGET_ID_PRIMARY_KEY PRIMARY KEY (ID)\n" +
                ")";
        return createString;
    }
	
	public static String getAccountDDE() {
        String createString = "CREATE TABLE ACCOUNT\n" +
                "(\n" +
                "  ID SERIAL NOT NULL,\n" +
                "  NAME TEXT,\n" +
                "  TYPE SMALLINT,\n" +
                "  AMOUNTCONTROL NUMERIC,\n" +
                "  OWNER CHARACTER VARYING(128),\n" +
                "  OBSERVERS TEXT[],\n" +
                "  CONSTRAINT ACCOUNT_ID_PRIMARY_KEY PRIMARY KEY (ID)\n" +
                ")";
        return createString;
    }

    public static String getTransactionDDE() {
        String createString = "CREATE TABLE TRANSACTION\n" +
                "(\n" +
                "  ID BIGSERIAL NOT NULL,\n" +
                "  TYPE SMALLINT,\n" +
                "  \"USER\" CHARACTER VARYING(128),\n" +
                "  DATE TIMESTAMP WITHOUT TIME ZONE,\n" +
                "  REGDATE TIMESTAMP WITHOUT TIME ZONE,\n" +
                "  CATEGORY BIGINT,\n" +
                "  SUBCATEGORY BIGINT,\n" +
                "  AMOUNT NUMERIC,\n" +
                "  ACCOUNT BIGINT,\n" +
                "  COSTCENTER BIGINT,\n" +
                "  REPEAT BOOLEAN,\n" +
                "  EVERY NUMERIC,\n" +
                "  REPEATSTEP BIGINT,\n" +
                "  ENDDATE TIME WITHOUT TIME ZONE,\n" +
                "  BASIS BYTEA,\n" +
                "  NOTE TEXT,\n" +
                "  CONSTRAINT TRANSACTION_ID_PRIMARY_KEY PRIMARY KEY (ID)\n" +
                ")";
        return createString;
    }

    public static String getCostCenterDDE() {
        String createString = "CREATE TABLE COSTCENTER\n" +
                "(\n" +
                "  ID SERIAL NOT NULL,\n" +
                "  TYPE SMALLINT,\n" +
                "  NAME TEXT,\n" +
                "  CONSTRAINT COSTCENTER_ID_PRIMARY_KEY PRIMARY KEY (ID)\n" +
                ")";
        return createString;
    }
}
