package ar.com.airdrop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFileChooser;

import ar.com.airdrop.services.EnvioService;
import ar.com.commons.send.airdrop.Constantes;
import ar.com.commons.send.airdrop.Mensaje;

public class ListenerAceptoEnvio implements ActionListener {

	
	private Mensaje mensaje = null;
	
	private EnvioService envioService = new EnvioService();
	public ListenerAceptoEnvio(Mensaje mensaje){
		
		this.mensaje = mensaje;
		
	}
	
	public void actionPerformed(ActionEvent e) {
	
		
		try {
			Socket socket = new Socket(mensaje.getIpDestino(), Constantes.PUERTO_ARCHIVOS);
			
			JFileChooser jfc = new JFileChooser();
			 jfc.showOpenDialog(null);

			envioService.enviaFichero(jfc.getSelectedFile().getAbsolutePath(),new ObjectOutputStream(socket.getOutputStream()));
			
			
			
			
			
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		
		
		
		
		

	}

}
