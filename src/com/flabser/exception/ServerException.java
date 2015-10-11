package com.flabser.exception;

@Deprecated
public class ServerException extends Exception {
	public int id;
	public String user;

	private static final long serialVersionUID = 4762010135613823296L;
	private String errorText;

	public ServerException(ServerExceptionType error, String addText) {
		super();
		switch (error) {
		case APPTEMPLATE_HAS_NOT_INITIALIZED:
			errorText = "Application environment has not initialized. See server log " + addText;
			break;

		}
	}

	@Override
	public String getMessage() {
		return errorText;
	}

	@Override
	public String toString() {
		return errorText;
	}

}
