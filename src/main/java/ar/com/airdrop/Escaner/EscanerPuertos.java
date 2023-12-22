package ar.com.airdrop.Escaner;

import java.util.Iterator;
import java.util.LinkedList;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.services.PcService;
import ar.com.commons.send.airdrop.Pc;


public class EscanerPuertos {

	
	private PcService pcService = (PcService)SpringContext.getContext().getBean("pcService");
	

	
	public void inicioEscanner(){
		
		
		
		
		Iterator<Pc> iterator = pcService.obtenerListaPcExternas().iterator();
		
		LinkedList<Pc> filtradas = new LinkedList<Pc>();
		
		while(iterator.hasNext()){
			Pc pc = iterator.next();
			
			
		ThreadEscanerPuertos thread =new  ThreadEscanerPuertos(pc,filtradas);
		thread.start();
		}
		
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		pcService.setPcExternas(filtradas);
		
		
	}



	public PcService getPcService() {
		return pcService;
	}



	public void setPcService(PcService pcService) {
		this.pcService = pcService;
	}
	
	
	
}
