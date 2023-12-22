package ar.com.airdrop.vistas;
import javax.swing.ImageIcon;

public class PantallaCargandoMain extends Thread{

  PantallaCargando screen;

  public  void run()
  {
	  PantallaCargandoEmpieza();
  }
  
  public void PantallaCargandoEmpieza() {
    inicioPantalla();
//	screen.velocidadDeCarga();
  }

  private void inicioPantalla() {
    ImageIcon myImage = new ImageIcon("imagen/java.gif");
    screen = new PantallaCargando(myImage);
    screen.setLocationRelativeTo(null);
    screen.setProgresoMax(100);
    screen.setVisible(true);
  }

  public void setProgress(int progress){
	  if (screen != null){
	  screen.setProgreso(progress + 10);
	  }
  }
  
  
}