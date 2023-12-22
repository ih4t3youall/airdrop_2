package ar.com.airdrop.vistas;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.services.ArchivoService;
import ar.com.airdrop.services.EnvioService;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;

public class VistaEnviarArchivo extends JFrame {

	protected JTextField archivo = new JTextField("", 25);
	private JButton seleccionar = new JButton("Seleccionar");
	private JButton aceptar = new JButton("Aceptar");
	private EnvioService envioService = new EnvioService();
	private ArchivoService archivoService =(ArchivoService) SpringContext.getContext().getBean("archivoService");
	protected Pc pc = null;

	public VistaEnviarArchivo(Pc pc) {
		this.pc = pc;

		
		try {

			JFileChooser jfc = new JFileChooser();

			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

				Mensaje mensaje = new Mensaje(this.pc);
				mensaje.setIpDestino(this.pc.getIp());
				mensaje.setFile(new File(archivo.getText()));
				mensaje.setComando("archivo");
				mensaje.setNombreArchivo(jfc.getSelectedFile().getName());
				mensaje.setFile(jfc.getSelectedFile());
				
				archivoService.archivoAEnviar(mensaje);
				envioService.enviarMensaje(mensaje);
				
				
			}
		} catch (Exception e) {

			e.printStackTrace();
		}


	}

}
