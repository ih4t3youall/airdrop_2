package ar.com.airdrop.listeners;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.services.PcService;
import ar.com.airdrop.vistas.Editar;
import ar.com.airdrop.vistas.MenuPrincipal;
import ar.com.commons.send.airdrop.Pc;

public class ListenerEditar implements ActionListener {

	
	
	private List lista;
	private PcService pcService =(PcService) SpringContext
			.getContext().getBean("pcService") ;
	private MenuPrincipal menuPrincipal;
	
	public ListenerEditar(List lista, MenuPrincipal menuPrincipal){
		this.menuPrincipal = menuPrincipal;
		this.lista = lista;
		
	}
	
	
	public void actionPerformed(ActionEvent arg0) {

		
		int selectedIndex = lista.getSelectedIndex();
		
		if (selectedIndex == -1){
		
			JOptionPane.showMessageDialog(null, "debe seleccionar una pc");
			
			
		}else {
		Pc pcExterna = pcService.obtenerListaPcExternas().get(selectedIndex);
		
		new Editar(pcExterna,menuPrincipal);
		
		}
		
		
	}

}
