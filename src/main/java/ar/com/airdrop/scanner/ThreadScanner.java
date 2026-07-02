package ar.com.airdrop.scanner;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import ar.com.airdrop.constants.Constants;
import ar.com.airdrop.domine.Message;

/**
 * Sondea un host del segmento: intenta conectarse al puerto de airdrop y,
 * si esta abierto, le manda el "who" para iniciar el handshake (el receptor
 * responde "autenticar").
 *
 * Reemplaza al ping ICMP (InetAddress.isReachable), que en macOS requiere
 * privilegios y ademas no distingue si el host realmente corre airdrop.
 */
public class ThreadScanner implements Runnable {

	private static final int CONNECT_TIMEOUT_MS = 500;

	private final String host;
	private final Message whoMessage;

	public ThreadScanner(String host, Message whoMessage) {
		this.host = host;
		this.whoMessage = whoMessage;
	}

	public void run() {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, Constants.PORT),
					CONNECT_TIMEOUT_MS);

			// El puerto esta abierto: el host corre airdrop. Le mandamos el
			// "who" y respondera con "autenticar" a nuestro puerto de escucha.
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			out.writeObject(whoMessage);
			out.flush();

			System.out.println("Airdrop encontrado en: " + host);

		} catch (Exception e) {
			// Puerto cerrado, host inexistente o sin airdrop: se ignora.
		}
	}

}
