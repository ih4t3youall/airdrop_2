package ar.com.airdrop.scanner;

import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.domine.Message;
import ar.com.airdrop.services.PcService;

public class Escanear {

	private PcService pcService = (PcService) SpringContext.getContext()
			.getBean("pcService");

	public void startScanning() throws InterruptedException {

		String localIp = pcService.obtenerIpLocal();
		String prefix = cleanIp(localIp);

		// Un unico mensaje "who" con nuestra Pc local (ip + nombre). El
		// receptor lo registra y responde "autenticar", quedando ambos lados
		// enterados del otro.
		Message who = new Message(pcService.getPcLocal());
		who.setCommand("who");

		// Pool acotado para no abrir 254 sockets a la vez (limite de
		// descriptores de archivo, sobre todo en macOS/Linux).
		ExecutorService pool = Executors.newFixedThreadPool(64);
		for (int i = 1; i < 255; i++) {
			String host = prefix + i;
			if (host.equals(localIp)) {
				continue; // no nos escaneamos a nosotros mismos
			}
			pool.submit(new ThreadScanner(host, who));
		}

		pool.shutdown();
		pool.awaitTermination(30, TimeUnit.SECONDS);

		// Damos un momento a que lleguen las respuestas "autenticar".
		Thread.sleep(1000);

		System.out.println("Scan finalizado. "
				+ "Usa la opcion 4 para ver las PCs con airdrop encontradas.");
	}

	public String cleanIp(String ip) {
		String cleanIp = "";
		StringTokenizer token = new StringTokenizer(ip, ".");

		for (int i = 0; i < 3; i++) {
			cleanIp += token.nextToken() + ".";
		}
		return cleanIp;
	}

	public PcService getPcService() {
		return pcService;
	}

	public void setPcService(PcService pcService) {
		this.pcService = pcService;
	}

}
