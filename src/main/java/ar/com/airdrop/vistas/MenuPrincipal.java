package ar.com.airdrop.vistas;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import ar.com.airdrop.componentes.BarraDeMenu;
import ar.com.airdrop.context.SpringContext;
import ar.com.airdrop.exceptions.ArchivoNoExisteException;
import ar.com.airdrop.listeners.EscanearListener;
import ar.com.airdrop.listeners.IngresarIpListener;
import ar.com.airdrop.listeners.ListenerBotonSalir;
import ar.com.airdrop.listeners.ListenerEditar;
import ar.com.airdrop.listeners.ListenerEnviarArchivo;
import ar.com.airdrop.listeners.ListenerEnviarMensaje;
import ar.com.airdrop.listeners.ListnerEnviarComando;
import ar.com.airdrop.persistencia.Persistencia;
import ar.com.airdrop.services.PcService;
import ar.com.commons.send.airdrop.Pc;

public class MenuPrincipal extends JFrame {

	private JButton escanear, enviarArchivos, salir, ingresarIp, enviarMensaje,
			editar, enviarComando;
	private LinkedList<Pc> pcs = new LinkedList<Pc>();
	private List lista = new List();

	private static PcService pcService = (PcService) SpringContext.getContext()
			.getBean("pcService");

	public MenuPrincipal() {
		super("Escanner : " + pcService.obtenerIpLocal());

		this.setJMenuBar(new BarraDeMenu(this));

		GridBagConstraints constraints = new GridBagConstraints();
		editar = new JButton("Editar");
		escanear = new JButton("Escanear");
		enviarArchivos = new JButton("Enviar Archivos");
		salir = new JButton("Salir");
		ingresarIp = new JButton("Ingresar Ip");
		enviarMensaje = new JButton("Enviar Mensaje");
		enviarComando = new JButton("Enviar Comando");

		Dimension size = new Dimension(550, 200);

		setSize(size);
		setLocation(500, 500);
		setResizable(false);

		this.getContentPane().setLayout(new GridBagLayout());

		// lista
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;

		// constraints.fill = GridBagConstraints.BOTH;

		this.getContentPane().add(lista, constraints);
		constraints.weighty = 0;
		constraints.weightx = 0;
		// boton1
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		// constraints.fill = GridBagConstraints.BOTH;

		this.getContentPane().add(escanear, constraints);
		constraints.weighty = 0.0;

		// boton 2

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		// constraints.fill = GridBagConstraints.BOTH;

		this.getContentPane().add(enviarArchivos, constraints);
		constraints.weighty = 0.0;

		// ip a mano
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		// constraints.fill = GridBagConstraints.BOTH;

		this.getContentPane().add(ingresarIp, constraints);
		constraints.weighty = 0.0;

		// boton 3

		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		// constraints.fill = GridBagConstraints.BOTH;

		this.getContentPane().add(salir, constraints);
		constraints.weighty = 0.0;

		// boton Editar
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		// constraints.fill = GridBagConstraints.BOTH;

		this.getContentPane().add(editar, constraints);
		constraints.weighty = 0.0;

		// boton enviarComando
		constraints.gridx = 4;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		// constraints.fill = GridBagConstraints.BOTH;

		this.getContentPane().add(enviarComando, constraints);
		constraints.weighty = 0.0;

		this.escanear.addActionListener(new EscanearListener(this));
		this.ingresarIp.addActionListener(new IngresarIpListener());
		this.salir.addActionListener(new ListenerBotonSalir(this));
		this.enviarMensaje.addActionListener(new ListenerEnviarMensaje(lista));
		this.enviarArchivos.addActionListener(new ListenerEnviarArchivo(lista));
		this.editar.addActionListener(new ListenerEditar(lista, this));
		this.enviarComando.addActionListener(new ListnerEnviarComando(lista));

		this.cargarLista();

		// enviar mensaje
		// ip a mano
		constraints.gridx = 4;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		// constraints.fill = GridBagConstraints.BOTH;

		this.getContentPane().add(enviarMensaje, constraints);
		constraints.weighty = 0.0;

		// cerrar y terminar programa
		setDefaultCloseOperation(0); // que por defecto no haga nada
		addWindowListener(new WindowAdapter() {
			@Override
			// (anotacion) se sobreescribe el metodo windowClosing
			public void windowClosing(WindowEvent we) {
				int eleccion = JOptionPane.showConfirmDialog(null,
						"Desea salir?");
				if (eleccion == 0) {
					System.out.println("adios");
					System.exit(0);
				}
			}
		});
	}

	public LinkedList<Pc> damePcs() {

		return this.pcs;

	}

	public void deshabilitarBotones() {

		this.escanear.setEnabled(false);
		this.enviarArchivos.setEnabled(false);
		this.salir.setEnabled(false);
		this.ingresarIp.setEnabled(false);

	}

	public void habilitarBotones() {

		this.escanear.setEnabled(true);
		this.enviarArchivos.setEnabled(true);
		this.salir.setEnabled(true);
		this.ingresarIp.setEnabled(true);

	}

	public void cargarLista() {

		LinkedList<Pc> ListaOtrasPc = pcService.obtenerListaPcExternas();

		lista.removeAll();

		for (Pc pc : ListaOtrasPc) {
			String aux = "";
			if (pc.getNombreEquipo().length() > 14)
				aux = pc.getNombreEquipo().substring(0, 14);
			else
				aux = pc.getNombreEquipo();

			lista.add(aux);

		}

	}

	public static PcService getPcService() {
		return pcService;
	}

	public static void setPcService(PcService pcService) {
		MenuPrincipal.pcService = pcService;
	}

	public void renovarNombre() {
		this.setTitle(pcService.getPcLocal().getIp());

	}

	public void eliminarElSeleccionado() {

		int selectedIndex = lista.getSelectedIndex();
		lista.remove(selectedIndex);

	}

	public List getLista() {

		return lista;
	}

}
