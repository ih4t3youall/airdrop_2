package ar.com.airdrop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ar.com.airdrop.Escaner.Escanear;
import ar.com.airdrop.vistas.MenuPrincipal;

public class EscanearListener implements ActionListener {

	
	private MenuPrincipal menuPrincipal;
	public EscanearListener(MenuPrincipal menuPrincipal){
		
		
		this.menuPrincipal = menuPrincipal;
	}
	
	
	public void actionPerformed(ActionEvent arg0) {
	
		try {
			
			 new Escanear().inicioEscanner();
			
		} catch (InterruptedException e) {
			System.out.println("Fallo el scanner.");
		}

		
		 
		
		
		
		
		
		
	}

}
