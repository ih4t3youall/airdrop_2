package ar.com.airdrop.vistas;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.exceptions.EnviarSocketException;
import ar.com.airdrop.services.EnvioService;
import ar.com.airdrop.services.PcService;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;

public class VistaIngresarIpManual extends JFrame {

	
	/**
	 * 
	 */
	
	JTextField textoIp = new JTextField("",20);
	JButton aceptar = new JButton("Aceptar");
	JButton cancelar = new JButton("Cancelar");
	
	private PcService pcService = (PcService)SpringContext.getContext().getBean("pcService");
	private EnvioService envioService =(EnvioService) SpringContext.getContext().getBean("envioService");

	
	public VistaIngresarIpManual() {

		Dimension dimension = new Dimension(300, 100);
		setSize(dimension);
		setLocation(400, 400);
		
		Panel panel = new Panel(new FlowLayout());
		
		
		panel.add(textoIp);
		panel.add(aceptar);
		panel.add(cancelar);
		add(panel);
		setVisible(true);	
		this.setResizable(false);
		
	
		aceptar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				Pc pc = pcService.getPcLocal();
				
				Mensaje mensaje = new Mensaje(pc);
				mensaje.setIpDestino(textoIp.getText());
				mensaje.setComando("who");
				
				
				
				
				try {
					
					envioService.enviarMensaje(mensaje);
				} catch (EnviarSocketException e) {
					System.out.println("fallo la autenticacion forzosa se agrega igualmente la ip");
					
					pc.setIp(pc.getIp());
					
					pcService.addPcExterna(pc);
					e.printStackTrace();
				}finally{
					
					setVisible(false);
					
				}
				
			}
		});
		
		
		
		cancelar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				
			}
		});
		
		
		
	}


	public PcService getPcService() {
		return pcService;
	}


	public void setPcService(PcService pcService) {
		this.pcService = pcService;
	}


	public EnvioService getEnvioService() {
		return envioService;
	}


	public void setEnvioService(EnvioService envioService) {
		this.envioService = envioService;
	}
	
	

	
	

}
