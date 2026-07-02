package ar.com.airdrop.services;

import java.io.ObjectOutputStream;
import java.net.Socket;

import ar.com.airdrop.constants.Constants;
import ar.com.airdrop.domine.Message;

public class SendMessage extends Thread{

	public Message message;

	public SendMessage(Message message){
		this.message = message;
	}

	public void run(){

		try (Socket socket = new Socket(message.getDestinationIp(), Constants.PORT);
				ObjectOutputStream buffer = new ObjectOutputStream(
						socket.getOutputStream())) {

			buffer.writeObject(message);

		} catch (Exception e) {
			// Normal durante el scan: el host no corre airdrop (Connection
			// refused). Se loguea una linea, sin volcar el stack trace.
			System.out.println("No se pudo enviar a " + message.getDestinationIp()
					+ " (" + e.getMessage() + ")");
		}
	}

}
