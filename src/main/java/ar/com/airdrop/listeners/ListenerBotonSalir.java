package ar.com.airdrop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ar.com.airdrop.vistas.MenuPrincipal;

public class ListenerBotonSalir implements ActionListener {

	
	private MenuPrincipal menuPrincipal;
	
	public ListenerBotonSalir(MenuPrincipal menuPrincipal){
		
		this.menuPrincipal = menuPrincipal;
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
		System.exit(0);
		
		

	}

}
