package ar.com.airdrop.services;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;

import ar.com.airdrop.domine.Pc;
import ar.com.airdrop.exceptions.ServiceException;


public class IpService {

	private String hostAddress ;
	private String hostName ;

	public Pc obtenerIp() throws ServiceException{
		if (hostAddress == null){
			hostAddress = resolveLocalAddress();
			hostName = resolveHostName();
		}
		Pc pc = new Pc(hostAddress);
		pc.setPcName(hostName);
		return pc;

	}

	/**
	 * Resuelve la IPv4 real de la maquina en la LAN de forma multiplataforma
	 * (Windows, Linux y macOS). No usa {@link InetAddress#getLocalHost()}
	 * porque en Linux suele devolver la loopback (127.0.0.1) segun el
	 * mapeo del hostname en /etc/hosts.
	 */
	private String resolveLocalAddress() throws ServiceException {

		// 1) Preguntamos al SO por que interfaz saldria hacia afuera. Es un
		//    socket UDP: connect() sobre UDP no envia ningun paquete, solo
		//    fija la ruta, asi que funciona incluso sin salida a internet.
		try (DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			InetAddress local = socket.getLocalAddress();
			if (local instanceof Inet4Address
					&& !local.isAnyLocalAddress()
					&& !local.isLoopbackAddress()) {
				return local.getHostAddress();
			}
		} catch (Exception ignored) {
			// Sin ruta por defecto: caemos al escaneo de interfaces.
		}

		// 2) Fallback: recorremos las interfaces y elegimos la primera IPv4
		//    privada (site-local) que no sea loopback.
		try {
			InetAddress candidate = null;
			for (NetworkInterface ni : Collections.list(
					NetworkInterface.getNetworkInterfaces())) {

				if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
					continue;
				}

				for (InetAddress addr : Collections.list(ni.getInetAddresses())) {
					if (!(addr instanceof Inet4Address)
							|| addr.isLoopbackAddress()) {
						continue;
					}
					if (addr.isSiteLocalAddress()) {
						// 192.168.x / 10.x / 172.16-31.x: la mejor opcion.
						return addr.getHostAddress();
					}
					if (candidate == null) {
						// Cualquier IPv4 no-loopback como ultimo recurso.
						candidate = addr;
					}
				}
			}
			if (candidate != null) {
				return candidate.getHostAddress();
			}
		} catch (SocketException e) {
			throw new ServiceException("Error al obtener la ip", e);
		}

		throw new ServiceException(
				"No se encontro una interfaz de red con IP valida",
				new UnknownHostException("sin IPv4 no-loopback"));
	}

	/**
	 * Nombre de la maquina. Si falla (por ejemplo por el hostname sin
	 * resolucion inversa) no rompemos: devolvemos la propia IP.
	 */
	private String resolveHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return hostAddress;
		}
	}

}
