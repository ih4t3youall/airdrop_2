package ar.com.airdrop.exceptions;

public class ReceiveThroughtServletException extends Exception {

	private static final long serialVersionUID = 1L;

	public ReceiveThroughtServletException(Exception cause, String ip, String message) {
		super(message + ip, cause);
	}

}
