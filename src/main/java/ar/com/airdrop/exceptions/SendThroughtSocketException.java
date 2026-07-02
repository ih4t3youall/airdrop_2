package ar.com.airdrop.exceptions;

public class SendThroughtSocketException extends Exception {

	private static final long serialVersionUID = 1L;

	public SendThroughtSocketException(Exception cause, String ip, String message) {
		super(message + ip, cause);
	}

}
