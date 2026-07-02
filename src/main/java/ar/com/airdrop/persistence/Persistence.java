package ar.com.airdrop.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ar.com.airdrop.exceptions.FileNotExist;
import ar.com.airdrop.services.PcService;

public class Persistence {

	private static final String DATA_FILE = "airdropdata";

	public void saveRecord(PcService pcService) {
		try (ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(DATA_FILE))) {
			out.writeObject(pcService);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadRecord(PcService pcService) throws FileNotExist {

		File f = new File(DATA_FILE);
		if (!f.exists()) {
			throw new FileNotExist("el archivo no existe");
		}

		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(DATA_FILE))) {

			PcService service = (PcService) ois.readObject();
			pcService.setExternalPc(service.getListExternalPc());
			pcService.setIpLocalhost(service.obtenerIpLocal());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
