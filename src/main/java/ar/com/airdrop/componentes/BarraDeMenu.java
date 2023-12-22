package ar.com.airdrop.componentes;


	

import java.util.LinkedList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ar.com.airdrop.listeners.ListnerMenuBar;
import ar.com.airdrop.vistas.MenuPrincipal;


public class BarraDeMenu extends JMenuBar{

	private static String[] labels = {"Archivo"};
	private static String[] labelsItems = {"Editar Local","Guardar","Cargar"};
	private LinkedList<JMenu> itemsMenu = new LinkedList<JMenu>();
	 private MenuPrincipal contextoPadre;
	
	public BarraDeMenu(MenuPrincipal menuPrincipal){
		contextoPadre = menuPrincipal;
		for (int i = 0 ; i<labels.length;i++){
			
			JMenu menu = new JMenu(labels[i]);
			itemsMenu.add(menu);
			this.add(menu);
		}
		
		for (JMenu menu : itemsMenu) {
			
			for (int i = 0 ; i<labelsItems.length;i++){
				
				JMenuItem menuItem = new JMenuItem(labelsItems[i]);
				menuItem.addActionListener(new ListnerMenuBar(contextoPadre));
				menu.add(menuItem);
			}
		}
		
		
	}
	
	
}

	
