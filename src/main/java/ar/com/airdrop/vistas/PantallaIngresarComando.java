package ar.com.airdrop.vistas;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import ar.com.airdrop.componentes.BarraDeMenuEnviarComando;
import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.exceptions.EnviarSocketException;
import ar.com.airdrop.services.EnvioService;
import ar.com.airdrop.services.PcService;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;

public class PantallaIngresarComando extends JFrame {

	public JButton aceptar,cancelar;
	public JTextField textoComando;
	private PcService pcService =(PcService) SpringContext.getContext().getBean("pcService");
	private EnvioService envioService = (EnvioService) SpringContext.getContext().getBean("envioService");
	private JCheckBox check;
	

	
	public PantallaIngresarComando(final Pc pcExterna){
		
		
		
		setLayout(new FlowLayout());
		aceptar = new JButton("Aceptar");
		cancelar = new JButton("Cancelar");
		textoComando = new JTextField("",20);
		check = new JCheckBox("con respuesta",false);
	
		check.isEnabled();
	
		this.setJMenuBar(new BarraDeMenuEnviarComando(this,pcExterna));
		
		setSize(570,120);
		setLocation(400,400);
		add(textoComando);
		add(aceptar);
		add(cancelar);
		add(check);
		
		setVisible(true);
		this.setResizable(false);
		aceptar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			
				
				 
				Mensaje mensaje = new Mensaje(pcExterna);
				mensaje.setIpDestino(pcExterna.getIp());
				mensaje.setComando("bash");
				mensaje.setMensaje(textoComando.getText());
				mensaje.setRespuesta(check.isSelected());
				try {
					envioService.enviarMensaje(mensaje);
				} catch (EnviarSocketException e1) {
					e1.printStackTrace();
				} finally {

					setVisible(false);

				}
				
				
			}
		});
		
		cancelar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				
			}
		});
		
		
		
		
	}
	
}
