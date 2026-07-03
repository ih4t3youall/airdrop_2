package ar.com.airdrop.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import ar.com.airdrop.constants.Constants;
import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.domine.Message;
import ar.com.airdrop.domine.Pc;
import ar.com.airdrop.exceptions.ReceiveThroughtServletException;
import ar.com.airdrop.services.SendService;
import ar.com.airdrop.services.PcService;
import ar.com.airdrop.views.MenuPrincipal;

public class ReceiveMessage extends Thread {

	private PcService pcService = (PcService) SpringContext.getContext()
			.getBean("pcService");
	private SendService sendService = (SendService) SpringContext
			.getContext().getBean("sendService");

	private static int PORT = Constants.PORT;

	public ReceiveMessage()
			throws ReceiveThroughtServletException {
	}

	public void run() {

		try (ServerSocket socket = new ServerSocket(PORT)) {

			while (true) {
				System.out.println("Waiting for send....");

				try (Socket cliente = socket.accept()) {

				String ipOtroCliente = cliente.getInetAddress()
						.getHostAddress();
				System.out.println("Connected with " + ipOtroCliente);

				cliente.setSoLinger(true, 10);

				ObjectInputStream buffer = new ObjectInputStream(
						cliente.getInputStream());

				Message message = (Message) buffer.readObject();

				if (message.getCommand().equals("who")) {

					// Guardamos a la PC que nos contacto usando la IP real de
					// la conexion, asi la contactada ya tiene sus datos.
					Pc contacto = message.getPc();
					contacto.setIp(ipOtroCliente);
					pcService.addExternalPc(contacto);

					Message responseMessage = new Message(
							pcService.getPcLocal());
					responseMessage.setCommand("autenticar");
					responseMessage.setDestinationIp(ipOtroCliente);
					sendService.sendMessage(responseMessage);

				}

				if (message.getCommand().equals("bash")){
					
					Process p = Runtime.getRuntime().exec(message.getMessage());
					
					if (message.isResponse()){
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					String s = null;
					String respuestaComando = "";
					// Leemos la salida del comando y la desplegamos en el jTextArea
					while ((s = stdInput.readLine()) != null ) {
						System.out.println(s);
					respuestaComando +=s+"\n";
					}
					stdInput.close();
					stdError.close();
					
					Message responseMessage = new Message(
							pcService.getPcLocal());
					responseMessage.setDestinationIp(ipOtroCliente);
					responseMessage.setCommand("respuestaComando");
					responseMessage.setMessage(respuestaComando);
					sendService.sendMessage(responseMessage);
					}
					
				}
				
				if (message.getCommand().equals("respuestaComando")){

					System.out.println("++++++++++++++++++++++++++++++++++");
					System.out.println("+++++++response from command++++++");
					System.out.println(message.getMessage());
					System.out.println("+++++++end response from command++++++");
					System.out.println("++++++++++++++++++++++++++++++++++");

					// Terminado el intercambio, volvemos al menu principal.
					MenuPrincipal.printMenu();
				}

				if (message.getCommand().equals("autenticar")) {

					System.out.println(" Pc received from IP: "
							+ ipOtroCliente);

					Pc pc = message.getPc();
					pc.setIp(ipOtroCliente);
					pcService.addExternalPc(pc);

				}

				if (message.getCommand().equals("mensajePrompt")) {

					System.out.println("************************************************");
					System.out.println("*********Message received******************");
					System.out.println("************************************************");
					System.out.println(message.getMessage());
					System.out.println("************************************************");
					System.out.println("*********End message received ****************************");
					System.out.println("************************************************");

					// Terminado el intercambio, volvemos al menu principal.
					MenuPrincipal.printMenu();
				}


			} catch (Exception e) {

				String error = "Error whit serversocket: "
						+ PORT;
				System.out.println(error);

			}
			}

		} catch (IOException e) {
			System.out.println("Error creating socket");
		}

	}

	public PcService getPcService() {
		return pcService;
	}

	public void setPcService(PcService pcService) {
		this.pcService = pcService;
	}

	public SendService getSendService() {
		return sendService;
	}

	public void setSendService(SendService sendService) {
		this.sendService = sendService;
	}

}
