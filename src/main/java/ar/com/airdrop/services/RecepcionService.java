package ar.com.airdrop.services;

import ar.com.airdrop.exceptions.RecibirServerSocketException;
import ar.com.airdrop.persistencia.Persistencia;
import ar.com.airdrop.threads.RecibirMensaje;
import ar.com.airdrop.vistas.MenuPrincipal;
import ar.com.commons.send.socket.Server;


public class RecepcionService {


	public void iniciarServerSocketObjetos(MenuPrincipal menuPrincipal){

		RecibirMensaje recibirMensaje;
		try {
			recibirMensaje = new RecibirMensaje(menuPrincipal);
		recibirMensaje.start();
		//new RecibirArchivo().start();
			System.out.println("starting file receiver service");

			Thread t = new Thread(new Server(Persistencia.getDownloadDirectory()));
			t.start();
		} catch (RecibirServerSocketException e) {
			e.printStackTrace();
		}
	}


}
