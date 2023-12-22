package ar.com.airdrop.vistas;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.exceptions.EnviarSocketException;
import ar.com.airdrop.services.EnvioService;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;

public class VistaEnviarMensajePrompt extends JFrame {

	JTextField textfield = new JTextField("", 30);
	JButton aceptar, cancelar;
	private Pc pc;

	private EnvioService envioService = (EnvioService) SpringContext
			.getContext().getBean("envioService");

	public VistaEnviarMensajePrompt(Pc pc1) {
		this.pc = pc1;
		this.aceptar = new JButton("Aceptar");
		this.cancelar = new JButton("Cancelar");

		Dimension d = new Dimension(400, 100);

		setSize(d);

		setLocation(200, 200);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		panel.add(textfield);
		panel.add(aceptar);
		panel.add(cancelar);

		textfield.requestFocus();
		textfield.requestFocusInWindow();
		
		
		add(panel);
		setVisible(true);
		this.setResizable(false);
		
		cancelar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			dispose();
				
			}
		});

		aceptar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Mensaje mensaje = new Mensaje(pc);

				mensaje.setComando("mensajePrompt");

				mensaje.setIpDestino(pc.getIp());
				
				mensaje.setMensaje(textfield.getText());

				try {
					envioService.enviarMensaje(mensaje);
				} catch (EnviarSocketException e1) {
					e1.printStackTrace();
				} finally {

					setVisible(false);

				}

			}
		});

	}

	public EnvioService getEnvioService() {
		return envioService;
	}

	public void setEnvioService(EnvioService envioService) {
		this.envioService = envioService;
	}

}
