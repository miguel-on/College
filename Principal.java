import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Principal extends JFrame implements ActionListener {
//	Campos de texto para numero1, numero2 y resultado
	  private JTextField N, M, W, P;

	  //Botones para sumar, restar, multiplicar  y dividir
	  private JButton Generar;
	  private  JButton Wumpus;
	  private JButton Precipicios;
	  private JButton Tesoro;
	  private JButton Pared;
	  private JButton Ciego;
	  private Tablero tablero;
	  private JTextField puntuacionciego;
	  private JTextField movciego;
	  private JButton Heuristica;
	  private JTextField puntuacionh;
	  private JTextField movh;
	  
	  //private Tablero tablero;

	  public static void main(String[] args)
	  {
	    Principal frame = new Principal();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //frame.pack();
	    
	    frame.setSize(1024,768) ;
	    frame.setVisible(true);
	  }

	  public Principal()
	  {
	    setTitle("Buscador del Tesoro");
	    

	    //establecer el FlowLayout para p1
	    JPanel p1 = new JPanel();
	    //p1.setLayout(new FlowLayout());
	    p1.setLayout(new GridBagLayout()); // como hoja de Excel 
	    GridBagConstraints pos = new GridBagConstraints();
	    pos.fill = GridBagConstraints.HORIZONTAL;
	    JLabel Columns = new JLabel("Columnas");
	    pos.gridy = 0;//Fila
	    pos.gridx = 0;//Columna
	    p1.add(Columns,pos);
	     N = new JTextField(3);
	    pos.gridy = 1;
	    pos.gridx = 0;
	    p1.add(N,pos);
	     M = new JTextField(3);
	    pos.gridy = 1;
	    pos.gridx = 1;
	    p1.add(M,pos);
	     W = new JTextField(3);
	    pos.gridy = 3;
	    pos.gridx = 0;
	    p1.add(W,pos);
	     P = new JTextField(3);
	    pos.gridy = 3;
	    pos.gridx = 1;
	    p1.add(P,pos);
	    JLabel Fil = new JLabel("Filas");
	    pos.gridy = 0;
	    pos.gridx = 1;
	    p1.add(Fil,pos);
	    JLabel Wums = new JLabel("Wumpus");
	    pos.gridy = 2;
	    pos.gridx = 0;
	    p1.add(Wums,pos);
	    JLabel Pre = new JLabel("Precipicios");
	    pos.gridy = 2;
	    pos.gridx = 1;
	    p1.add(Pre,pos);
	    Generar = new JButton ("Generar");
	    pos.gridy = 6;
	    pos.gridx = 0;
	    p1.add(Generar,pos);
	    Wumpus = new JButton ("Wumpus");
	    pos.gridy = 4;
	    pos.gridx = 0;
	    p1.add(Wumpus,pos);
	    Precipicios = new JButton ("Precipicios");
	    pos.gridy = 4;
	    pos.gridx = 1;
	    p1.add(Precipicios,pos);
	    Tesoro = new JButton ("Tesoro");
	    pos.gridy = 5;
	    pos.gridx = 0;
	    p1.add(Tesoro,pos);
	    Pared = new JButton ("Pared");
	    pos.gridy = 5;
	    pos.gridx = 1;
	    p1.add(Pared,pos);
	    Ciego = new JButton ("Ciego");
	    pos.gridy = 6;
	    pos.gridx = 0;
	    p1.add(Ciego,pos);
	    Heuristica = new JButton ("Heuristica");
	    pos.gridy = 6;
	    pos.gridx = 1;
	    p1.add(Heuristica,pos);
	    //establecer el FlowLayout para p2
	    JPanel p2 = new JPanel();
	    p2.setLayout (new BorderLayout ());
	    p2.add (tablero = new Tablero ());
	    JPanel p3 = new JPanel ();
	    p3.setLayout(new GridBagLayout()); // como hoja de Excel 
	    GridBagConstraints pos1 = new GridBagConstraints();
	    pos1.fill = GridBagConstraints.HORIZONTAL;
	    JLabel PuntuaCiego = new JLabel("Puntuación del Ciego");
	    pos1.gridy = 0;
	    pos1.gridx = 0;
	    p3.add(PuntuaCiego,pos1);
	    puntuacionciego = new JTextField(8);
	    pos1.gridy = 0;
	    pos1.gridx = 1;
	    p3.add(puntuacionciego,pos1);
	    JLabel MovCie = new JLabel("Movimientos en el Ciego");
	    pos1.gridy = 1;
	    pos1.gridx = 0;
	    p3.add(MovCie,pos1);
	    movciego = new JTextField (8);
	    pos1.gridy = 1;
	    pos1.gridx = 1;
	    p3.add(movciego,pos1);
	    JLabel Puntuah = new JLabel("Puntuación de la Heuristica");
	    pos1.gridy = 2;
	    pos1.gridx = 0;
	    p3.add(Puntuah,pos1);
	    puntuacionh = new JTextField(8);
	    pos1.gridy = 2;
	    pos1.gridx = 1;
	    p3.add(puntuacionh,pos1);
	    JLabel MovHH = new JLabel("Movimientos en la heuristica");
	    pos1.gridy = 3;
	    pos1.gridx = 0;
	    p3.add(MovHH,pos1);
	    movh = new JTextField (8);
	    pos1.gridy = 3;
	    pos1.gridx = 1;
	    p3.add(movh,pos1);
	    //establecer el BorderLayout para el frame 
	    Container c = getContentPane();
	    c.setBackground(Color.white);
	    c.setForeground(Color.blue);
	    c.setLayout(new BorderLayout());
	    c.add("West", p1);
	    c.add ("Center",p2);
	    c.add("East", p3);
	    	
	    //Registrar al marco como oyente de cada boton
	    Generar.addActionListener(this);
	    Wumpus.addActionListener(this);
	    Wumpus.setVisible (false);
	    Precipicios.addActionListener(this);
	    Precipicios.setVisible (false);
	    Tesoro.addActionListener(this);
	    Tesoro.setVisible (false);
	    Pared.addActionListener(this);
	    Pared.setVisible (false);
	    Ciego.addActionListener(this);
	    Ciego.setVisible (false);
	    Heuristica.addActionListener(this);
	    this.Heuristica.setVisible(false);
	  }

	  //manipular los ActionEvent asociados a los 
	  //botones
	  public void actionPerformed(ActionEvent e) {
	    String actionCommand = e.getActionCommand();

	    if (e.getSource() instanceof JButton) {
	      if ("Generar".equals(actionCommand)) {
			tablero.generar (Integer.parseInt (N.getText().trim()),Integer.parseInt (M.getText().trim()),
						Integer.parseInt (W.getText().trim()),Integer.parseInt (P.getText().trim()));
			tablero.repaint (/*frame.getGraphics ()*/);
			Wumpus.setVisible (true);
			Precipicios.setVisible (true);
			Tesoro.setVisible (true);
			Generar.setVisible (false);
			Pared.setVisible (true);
			  Ciego.setVisible (true);
			  this.Heuristica.setVisible(true);
		} else if ("Wumpus".equals(actionCommand)) {
			if  (tablero.get_n_wumps () != tablero.get_colocados()) {
				tablero.set_colocando (1);
			} else {
				Wumpus.setVisible (false);
			}
		} else if ("Precipicios".equals(actionCommand)) {
			if (tablero.get_n_precipicios () > 0) {
				tablero.set_colocando (3);
				
			} else {
				Precipicios.setVisible (false);
				tablero.repaint ();
			}
		} else if ("Tesoro".equals(actionCommand)) {
			tablero.set_colocando (8);
			Tesoro.setVisible (false);
		}  else if ("Pared".equals(actionCommand)) {
			tablero.set_colocando (9);
			
		}  else if ("Ciego".equals(actionCommand)) {
			//tablero.mover_der_manual ();
			
			boolean salida = tablero.ciego();
			/*if (salida) {
				Ciego.setVisible (false);
				
			} else {
				Tesoro.setVisible (true);
				
			} */
			puntuacionciego.setText(" "+ tablero.puntuacion);
			this.movciego.setText(" "+ tablero.mov_ciego);
			//tablero.repaint ();
			
		} else if ("Heuristica".equals(actionCommand)){
			
				boolean salida1 = tablero.heuristica();
				this.puntuacionh.setText(" "+this.tablero.puntuacion_h);
				this.movh.setText(" "+this.tablero.mov_h);
	        
	    }
	    
	      
	  }
	  }

}
