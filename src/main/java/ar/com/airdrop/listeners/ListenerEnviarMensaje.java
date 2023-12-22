package ar.com.airdrop.listeners;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.services.PcService;
import ar.com.airdrop.vistas.VistaEnviarMensajePrompt;
import ar.com.commons.send.airdrop.Pc;

public class ListenerEnviarMensaje implements ActionListener{

	private List lista;
	
	private PcService pcService =(PcService) SpringContext.getContext().getBean("pcService");
	
	public ListenerEnviarMensaje(List lista) {
		
		this.lista = lista;
		
	}

	public void actionPerformed(ActionEvent e) {

		
		int selectedIndex = lista.getSelectedIndex();
		
		if (selectedIndex == -1){
		
			JOptionPane.showMessageDialog(null, "debe seleccionar una pc");
			
			
		}else {
		Pc pcExterna = pcService.obtenerListaPcExternas().get(selectedIndex);
		
		new VistaEnviarMensajePrompt(pcExterna);
		}
		
		
		
		
	}

	public PcService getPcService() {
		return pcService;
	}

	public void setPcService(PcService pcService) {
		this.pcService = pcService;
	}

	
	
	
	
	
}
