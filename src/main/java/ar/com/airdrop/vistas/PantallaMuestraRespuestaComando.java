package ar.com.airdrop.vistas;

import ar.com.commons.send.airdrop.Mensaje;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class PantallaMuestraRespuestaComando extends JFrame{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton aceptar;
	public JTextArea texto;
	
	public PantallaMuestraRespuestaComando(Mensaje mensaje){
		
		aceptar = new JButton("Aceptar");
		texto = new JTextArea(10, 20);
		
		setLayout(new FlowLayout());

		
		JScrollPane sbrText = new JScrollPane(texto);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sbrText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		add(sbrText);
		add(aceptar);
		texto.setText(mensaje.getMensaje());
		aceptar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			
				dispose();
				
				
				
			}
		});
		setResizable(false);
		setLocation(200,200);
		setSize(320,245);
		setVisible(true);
		
		
		
	}
	
}
