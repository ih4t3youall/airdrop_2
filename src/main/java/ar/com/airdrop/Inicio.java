package ar.com.airdrop;

import javax.swing.JOptionPane;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.exceptions.ServiceException;
import ar.com.airdrop.services.PcService;
import ar.com.airdrop.services.RecepcionService;
import ar.com.airdrop.vistas.MenuPrincipal;
import ar.com.commons.send.airdrop.Pc;
import ar.com.commons.send.services.IpService;

public class Inicio {

	private static IpService ipService = (IpService) SpringContext.getContext()
			.getBean("ipService");
	private RecepcionService recepcionService = (RecepcionService) SpringContext
			.getContext().getBean("recepcionService");
	private PcService pcService = (PcService) SpringContext
			.getContext().getBean("pcService");
	
	

	public Inicio() {

		Pc pc = null;
		try {
			pc = ipService.obtenerIp();
			if (pcService.obtenerIpLocal().equals("0")){
			pcService.setIpLocalhost(pc.getIp());
			}
			pcService.setNombreLocal(pc.getNombreEquipo());

			
		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(null,
							"Error al obtener la ip local, verifique su conexion a internet");
			System.exit(0);
		}
//
//		try {
//			new Escanear().inicioEscanner();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		
//			
//			new EscanerPuertos().inicioEscanner();
			
		
		
		MenuPrincipal menuPrincipal = new MenuPrincipal();
		recepcionService.iniciarServerSocketObjetos(menuPrincipal);
		menuPrincipal.setVisible(true);


	}

	public IpService getIpService() {
		return ipService;
	}

	public void setIpService(IpService ipService) {
		this.ipService = ipService;
	}

	public RecepcionService getRecepcionService() {
		return recepcionService;
	}

	public void setRecepcionService(RecepcionService recepcionService) {
		this.recepcionService = recepcionService;
	}

}
