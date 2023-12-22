package ar.com.airdrop.vistas;

import ar.com.commons.send.airdrop.Pc;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Editar extends JFrame {

	
	protected Pc pc;
	protected Editar contexto;
	
	private JPanel panel;
	
	private JTextField ip = new JTextField();
	private JTextField nombreEquipo = new JTextField();
	private JButton aceptar,cancelar,eliminar;
	private MenuPrincipal contextoPadre;
	
	public Editar(Pc pcExterna, MenuPrincipal menuPrincipal){
		//TODO arreglar esto que es un asco bah no se
		this.contextoPadre = menuPrincipal;
		aceptar = new JButton("Aceptar");
		cancelar = new JButton("Cancelar");
		eliminar = new JButton("Eliminar");
		
		this.pc =pcExterna;
		contexto = this;
		panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		
		panel.add(new JLabel("Ip"));
		panel.add(ip);
		panel.add(new JLabel("Nombre Equipo"));
		panel.add(nombreEquipo);
		panel.add(aceptar);
		panel.add(cancelar);
		panel.add(eliminar);
		
		ip.setText(pc.getIp());
		nombreEquipo.setText(pc.getNombreEquipo());
		
		aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pc.setIp(ip.getText());
				pc.setNombreEquipo(nombreEquipo.getText());
				contextoPadre.cargarLista();
				contextoPadre.renovarNombre();
				contexto.dispose();
			}
		});

		cancelar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				contexto.dispose();
				
			}
		});
		
		eliminar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {

				contextoPadre.eliminarElSeleccionado();
				contexto.dispose();
				
			}
		});
		
		this.setSize(250, 100);
		this.add(panel);
		this.setLocation(contextoPadre.getLocation());
		this.setVisible(true);
		this.setResizable(false);
		
		
		
	}

	





	
}
