package ar.com.airdrop.views;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import ar.com.airdrop.scanner.Escanear;
import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.domine.Message;
import ar.com.airdrop.domine.Pc;
import ar.com.airdrop.exceptions.SendThroughtSocketException;
import ar.com.airdrop.persistence.Persistence;
import ar.com.airdrop.services.SendService;
import ar.com.airdrop.services.PcService;

public class MenuPrincipal {

	private static PcService pcService = (PcService) SpringContext.getContext()
			.getBean("pcService");

	private static SendService sendService = (SendService) SpringContext
			.getContext().getBean("sendService");

	/** PC destino actual: a donde se mandan mensajes/comandos. */
	private static Pc selectedPc = null;

	public MenuPrincipal() throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {

			printMenu();

			String linea = br.readLine();
			if (linea == null) { // stdin cerrado (EOF): salimos limpio
				System.exit(0);
			}

			int opcion;
			try {
				opcion = Integer.parseInt(linea.trim());
			} catch (NumberFormatException e) {
				System.out.println("Escribi un numero de opcion.");
				continue;
			}

			switch (opcion) {

			case 1: // Scan network
				try {
					new Escanear().startScanning();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;

			case 2: // List / select PC
				selectPc(br);
				break;

			case 3: { // Add PC by IP (handshake manual)
				System.out.println("IP de la PC a agregar:");
				String newIp = br.readLine().trim();
				Message message = new Message(new Pc(newIp));
				message.setDestinationIp(newIp);
				message.setCommand("who");
				try {
					sendService.sendMessage(message);
					System.out.println("Handshake enviado a " + newIp
							+ ". Revisa la lista con la opcion 2.");
				} catch (SendThroughtSocketException e) {
					System.out.println("Error en el handshake");
				}
				break;
			}

			case 4: { // Send message
				if (selectedPc == null) {
					System.out.println("Primero selecciona un destino (opcion 2).");
					break;
				}
				System.out.println("Mensaje para " + describe(selectedPc) + ":");
				String texto = br.readLine();
				Message message = new Message(selectedPc);
				message.setCommand("mensajePrompt");
				message.setMessage(texto);
				message.setDestinationIp(selectedPc.getIp());
				try {
					sendService.sendMessage(message);
					System.out.println("Mensaje enviado.");
				} catch (SendThroughtSocketException e) {
					System.out.println("Error enviando el mensaje");
				}
				break;
			}

			case 5: { // Send command
				if (selectedPc == null) {
					System.out.println("Primero selecciona un destino (opcion 2).");
					break;
				}
				System.out.println("Comando para " + describe(selectedPc) + ":");
				String comando = br.readLine();
				Message message = new Message(selectedPc);
				message.setDestinationIp(selectedPc.getIp());
				message.setCommand("bash");
				message.setMessage(comando);

				System.out.println("Con respuesta? (s/n):");
				String resp = br.readLine();
				message.setResponse(esSi(resp));

				try {
					sendService.sendMessage(message);
					System.out.println("Comando enviado.");
				} catch (SendThroughtSocketException e) {
					System.out.println("Error enviando el comando");
				}
				break;
			}

			case 6: { // Send file
				if (selectedPc == null) {
					System.out.println("Primero selecciona un destino (opcion 2).");
					break;
				}
				File archivo = elegirArchivo();
				if (archivo == null) {
					System.out.println("Envio cancelado.");
					break;
				}
				System.out.println("Enviando " + archivo.getName() + " a "
						+ describe(selectedPc) + "...");
				try {
					sendService.sendFileTo(selectedPc.getIp(), archivo);
					System.out.println("Archivo enviado.");
				} catch (IOException e) {
					System.out.println("Error enviando el archivo: " + e.getMessage());
				}
				break;
			}

			case 7: { // My IP / name
				System.out.println("Tu IP:     " + pcService.getPcLocal().getIp());
				System.out.println("Tu nombre: " + pcService.getPcLocal().getPcName());
				System.out.println("Nueva IP (Enter para dejar igual):");
				String nuevaIp = br.readLine();
				if (nuevaIp != null && !nuevaIp.trim().isEmpty()) {
					pcService.getPcLocal().setIp(nuevaIp.trim());
				}
				System.out.println("Nuevo nombre (Enter para dejar igual):");
				String nuevoNombre = br.readLine();
				if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
					pcService.getPcLocal().setPcName(nuevoNombre.trim());
				}
				System.out.println("Ahora sos: " + describe(pcService.getPcLocal()));
				break;
			}

			case 8: // Save configuration
				new Persistence().saveRecord(pcService);
				System.out.println("Configuracion guardada.");
				break;

			case 9: // Exit
				System.out.println("Saliendo...");
				System.exit(0);

			default:
				System.out.println("Esa opcion no existe.");
				break;
			}
		}
	}

	public static void printMenu() {
		System.out.println();
		System.out.println("========== AirDrop ==========");
		System.out.println("Yo:      " + describe(pcService.getPcLocal()));
		System.out.println("Destino: "
				+ (selectedPc != null ? describe(selectedPc) : "(ninguno - opcion 2)"));
		System.out.println("-----------------------------");
		System.out.println("1) Scan network");
		System.out.println("2) List / select PC");
		System.out.println("3) Add PC by IP");
		System.out.println("4) Send message");
		System.out.println("5) Send command");
		System.out.println("6) Send file");
		System.out.println("7) My IP / name");
		System.out.println("8) Save configuration");
		System.out.println("9) Exit");
	}

	/** Lista las PCs detectadas numeradas y deja elegir el destino por numero. */
	private void selectPc(BufferedReader br) throws IOException {
		LinkedList<Pc> lista = pcService.getListExternalPc();

		if (lista.isEmpty()) {
			System.out.println("No hay PCs detectadas. Corre el Scan (opcion 1).");
			return;
		}

		System.out.println("PCs detectadas:");
		for (int i = 0; i < lista.size(); i++) {
			System.out.println("  " + i + ") " + describe(lista.get(i)));
		}
		System.out.println("Numero para elegir destino (Enter para volver):");

		String sel = br.readLine();
		if (sel == null || sel.trim().isEmpty()) {
			return;
		}
		try {
			int i = Integer.parseInt(sel.trim());
			if (i >= 0 && i < lista.size()) {
				selectedPc = lista.get(i);
				System.out.println("Destino seleccionado: " + describe(selectedPc));
			} else {
				System.out.println("Numero fuera de rango.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Numero invalido.");
		}
	}

	/** Abre un selector grafico y devuelve el archivo elegido (o null si se cancela). */
	private static File elegirArchivo() {
		// Frame invisible siempre-al-frente para que el dialogo no quede
		// escondido detras de la terminal.
		JFrame parent = new JFrame();
		parent.setAlwaysOnTop(true);
		parent.setLocationRelativeTo(null);
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Elegi el archivo a enviar");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();
				if (f != null && f.isFile()) {
					return f;
				}
			}
			return null;
		} catch (HeadlessException e) {
			System.out.println("No hay entorno grafico disponible para el selector.");
			return null;
		} finally {
			parent.dispose();
		}
	}

	/** "ip - nombre" (o solo ip si no hay nombre). */
	private static String describe(Pc pc) {
		if (pc == null) {
			return "(ninguno)";
		}
		String n = pc.getPcName();
		return pc.getIp() + (n != null && !n.isEmpty() ? " - " + n : "");
	}

	private static boolean esSi(String s) {
		if (s == null) {
			return false;
		}
		s = s.trim().toLowerCase();
		return s.equals("s") || s.equals("si") || s.equals("y")
				|| s.equals("yes") || s.equals("true");
	}

}
