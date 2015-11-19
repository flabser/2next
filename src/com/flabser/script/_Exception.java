package com.flabser.script;

public class _Exception extends Exception {
	private static final long serialVersionUID = -6047450542781990205L;
	private String errorText;
	private String serviceAddInfo = "";

	public _Exception(_ExceptionType type, String addInfo) {
		super(type.toString() + " " + addInfo);
		errorText = type.toString() + ". " + addInfo;
	}

	@Override
	public String getMessage() {
		return errorText;
	}

	@Override
	public String getLocalizedMessage() {
		String error_message = this.errorText;
		return error_message + serviceAddInfo;
	}

	@Override
	public String toString() {
		return errorText;
	}

}
