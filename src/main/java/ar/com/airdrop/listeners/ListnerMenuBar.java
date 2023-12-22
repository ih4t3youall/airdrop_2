package ar.com.airdrop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.exceptions.ArchivoNoExisteException;
import ar.com.airdrop.persistencia.Persistencia;
import ar.com.airdrop.services.PcService;
import ar.com.airdrop.vistas.Editar;
import ar.com.airdrop.vistas.MenuPrincipal;

public class ListnerMenuBar implements ActionListener {

	private PcService pcService = (PcService) SpringContext
			.getContext().getBean("pcService");
	private MenuPrincipal contextoPadre;
		
	

	public ListnerMenuBar(MenuPrincipal contextoPadre2) {
		this.contextoPadre = contextoPadre2;
	}

	public void actionPerformed(ActionEvent event) {

		JMenuItem menuItem = (JMenuItem) event.getSource();

		if (menuItem.getText().equals("Editar Local")) {

			new Editar(pcService.getPcLocal(),contextoPadre);

		}
		if (menuItem.getText().equals("Guardar")) {
			
			 
				 Persistencia persistencia = new Persistencia();
				 persistencia.Guardar(pcService);
			
		}
		if (menuItem.getText().equals("Cargar")) {
			
				Persistencia persistencia = new Persistencia();
				try {
					persistencia.recuperarGuardado(pcService);
					this.contextoPadre .cargarLista();
					this.contextoPadre .renovarNombre();
				} catch (ArchivoNoExisteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}

	}

}
