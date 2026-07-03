package ar.com.airdrop.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ar.com.airdrop.constants.Constants;
import ar.com.airdrop.domine.Message;
import ar.com.airdrop.domine.GetFileMessage;
import ar.com.airdrop.exceptions.SendThroughtSocketException;

public class SendService {

	public void sendMessage(Message message) throws SendThroughtSocketException {
		SendMessage sendMessage = new SendMessage(message);
		sendMessage.start();
	}

	/**
	 * Envia un archivo (de cualquier tipo) al puerto de archivos del destino.
	 * Se conecta, streamea el contenido en chunks y cierra.
	 */
	public void sendFileTo(String ip, File file) throws IOException {
		try (Socket socket = new Socket(ip, Constants.FILE_PORT);
				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream())) {
			sendFile(file, oos);
		}
	}

	/**
	 * Streamea el archivo como una secuencia de {@link GetFileMessage},
	 * terminando con un chunk marcado como ultimo. No cierra el stream: eso
	 * es responsabilidad de quien lo abrio.
	 */
	public void sendFile(File file, ObjectOutputStream oos) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] buffer = new byte[GetFileMessage.MAX_LENGHT];
			int read;
			while ((read = fis.read(buffer)) > -1) {
				GetFileMessage chunk = new GetFileMessage();
				chunk.fileName = file.getName();
				chunk.validBytes = read;
				chunk.lastMessage = false;
				System.arraycopy(buffer, 0, chunk.fileContent, 0, read);
				oos.writeObject(chunk);
				// reset() evita que ObjectOutputStream retenga en memoria todos
				// los chunks ya enviados (importante en archivos grandes).
				oos.reset();
			}

			// Chunk final que le indica al receptor que termino el archivo.
			GetFileMessage end = new GetFileMessage();
			end.fileName = file.getName();
			end.validBytes = 0;
			end.lastMessage = true;
			oos.writeObject(end);
			oos.flush();
		}
	}

}
