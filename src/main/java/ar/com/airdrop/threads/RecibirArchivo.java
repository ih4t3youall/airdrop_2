package ar.com.airdrop.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import ar.com.airdrop.constants.Constants;
import ar.com.airdrop.domine.GetFileMessage;
import ar.com.airdrop.views.MenuPrincipal;

/**
 * Escucha en el puerto de archivos y recibe archivos de cualquier tipo,
 * guardandolos en ~/airdrop-recibidos. El emisor streamea el contenido como
 * una secuencia de {@link GetFileMessage} y cierra con un chunk final.
 */
public class RecibirArchivo extends Thread {

	private static final File CARPETA_DESTINO = new File(
			System.getProperty("user.home"), "airdrop-recibidos");

	public void run() {

		// Si no se puede bindear el puerto (ej: ya en uso), se avisa y se
		// termina el thread (sin quedar en un while(true) sobre socket null).
		try (ServerSocket socket = new ServerSocket(Constants.FILE_PORT)) {

			while (true) {
				try (Socket accept = socket.accept();
						ObjectInputStream ois = new ObjectInputStream(
								accept.getInputStream())) {

					recibirArchivo(accept, ois);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Error con el socket");
			e1.printStackTrace();
		}
	}

	private void recibirArchivo(Socket accept, ObjectInputStream ois)
			throws Exception {

		// El primer chunk trae el nombre del archivo.
		GetFileMessage chunk = (GetFileMessage) ois.readObject();

		String nombre = new File(chunk.fileName).getName(); // solo el nombre
		if (nombre == null || nombre.isEmpty()) {
			nombre = "archivo_recibido";
		}

		CARPETA_DESTINO.mkdirs();
		File destino = new File(CARPETA_DESTINO, nombre);

		System.out.println("Recibiendo archivo de "
				+ accept.getInetAddress().getHostAddress() + "...");

		try (FileOutputStream fos = new FileOutputStream(destino)) {
			while (true) {
				fos.write(chunk.fileContent, 0, chunk.validBytes);
				if (chunk.lastMessage) {
					break;
				}
				chunk = (GetFileMessage) ois.readObject();
			}
		}

		System.out.println("Archivo recibido: " + destino.getAbsolutePath());

		// Terminado el intercambio, volvemos al menu principal.
		MenuPrincipal.printMenu();
	}
}
