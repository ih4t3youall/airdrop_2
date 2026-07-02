package ar.com.airdrop.scanner;


import java.net.InetAddress;
import java.util.LinkedList;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.domine.Pc;
import ar.com.airdrop.services.PcService;

public class ThreadScanner extends Thread {



	private String serverHostName = null;
	private PcService pcService = (PcService) SpringContext.getContext().getBean("pcService");
	private LinkedList<Pc> pcs = new LinkedList<>();


	public ThreadScanner(String serverHosName, LinkedList<Pc> pcs){
		this.serverHostName = serverHosName;
		this.pcs = pcs;
	}

	public void run() {

		InetAddress ip;
		try {
			ip = InetAddress.getByName(this.serverHostName);

			if (ip.isReachable(4000)){
				this.pcs.add(new Pc(serverHostName));
				System.out.println("Host found: "+serverHostName);
			}else {
				System.out.println("Host not reachable: "+serverHostName);
			}

		} catch (Exception e) {
			// En macOS isReachable() usa ICMP y puede dar "Permission denied"
			// sin privilegios. Se loguea una linea, sin volcar el stack trace.
			System.out.println("No se pudo verificar el host " + serverHostName
					+ " (" + e.getMessage() + ")");
		}


	}

	public PcService getPcService() {
		return pcService;
	}

	public void setPcService(PcService pcService) {
		this.pcService = pcService;
	}






}