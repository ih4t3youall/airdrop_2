package ar.com.airdrop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.exceptions.EnviarSocketException;
import ar.com.airdrop.services.EnvioService;
import ar.com.airdrop.vistas.PantallaIngresarComando;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;

public class ListnerMenuBarEnviarComando implements ActionListener {


	private PantallaIngresarComando contexto;
	private Pc pcExterna;
	private EnvioService envioService = (EnvioService) SpringContext
			.getContext().getBean("envioService");
	
	public ListnerMenuBarEnviarComando(PantallaIngresarComando contexto,
			Pc pcExterna) {
		this.pcExterna = pcExterna;
		this.contexto = contexto;
	}

	public void actionPerformed(ActionEvent arg0) {
	
		JMenuItem boton = (JMenuItem) arg0.getSource();
		
		
		if (boton.getText().equals("Abrir Torrent")){
			
			Mensaje mensaje = new Mensaje(pcExterna);
			mensaje.setIpDestino(pcExterna.getIp());
			mensaje.setComando("bash");
			mensaje.setMensaje("tixati");
			mensaje.setRespuesta(false);
			try {
				envioService.enviarMensaje(mensaje);
			} catch (EnviarSocketException e1) {
				e1.printStackTrace();
			} finally {

				contexto.dispose();

			}
			
			
		}
		
		if (boton.getText().equals("Cerrar Torrent")){
			Mensaje mensaje = new Mensaje(pcExterna);
			mensaje.setIpDestino(pcExterna.getIp());
			mensaje.setComando("bash");
			mensaje.setMensaje("killall tixati");
			mensaje.setRespuesta(false);
			try {
				envioService.enviarMensaje(mensaje);
			} catch (EnviarSocketException e1) {
				e1.printStackTrace();
			} finally {

				contexto.dispose();

			}
			
		}
		
	}

}
