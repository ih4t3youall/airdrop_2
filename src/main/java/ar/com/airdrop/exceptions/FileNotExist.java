package ar.com.airdrop.exceptions;

public class FileNotExist extends Exception {

	private static final long serialVersionUID = 1L;

	public FileNotExist(String message) {
		super(message);
	}
}
