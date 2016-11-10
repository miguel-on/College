import java.util.*;
import java.text.*;

import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Tablero extends JPanel  implements MouseListener {
	int 	n = 2, 
	m = 2,
	n_wumps,
	n_precipicios ,
	colocados = 0,tempx,tempy,//Wumpus colocados
	puntuacion = 0,
	puntuacion_h,
	n_visitados = 0;
	boolean agente_muerto = false;
	int mov_ciego,
	    mov_h;
boolean ciego;
boolean pintar = false;
boolean visitando = false;
Wumpus wumps[];// = new Wumpus[20];
int colocando ; //1 se está colocando wumpus,  3 precipicios, 8 Tesoro
int [][]tablet; 
int [][]heur;
int 	anchos = 0, 
	altos = 0;
int   ptx, //Posición del tesoro		
	pty,  //Posición del tesoro
	xo = 0,
	yo = 0;
final int Paso_atras = 3500;
final int MuroPresipicio = 3400;
final int error = 5000;

//OBjeto Agente
Buscador agente = new Buscador();
//Objeto Granada
Granada granada = new Granada ();
//Clase en la que se almacena los estados anteriores
//Estados estados[] = new Estados[3000];
int direcciones[] = new int[3000];
int pos_estados = 0;
//Indica que direcciones ha tomado anteriormente
int direccion = 0;
//Nodos visitados
Visitados visitados;
Visitados anteriores;

public Tablero (int n, int m, int n_wumps, int n_pre) {
	/*addMouseListener (new MouseEventHandler ());*/
	addMouseListener (this);
	
}
public Tablero () {
	/*addMouseListener (new MouseEventHandler ());*/
	addMouseListener (this);
	colocados = 0;
	
} 
public void mover_der_manual () {
	int tempx = this.agente.pos_x (),
		tempy = this.agente.pos_y ();
	tablet[tempx][tempy] = 0;
	this.agente.mover(3);
	tablet[this.agente.pos_x()][ this.agente.pos_y()] = 2;	
	repaint ();
}
/***************************************************************************
	Mueve todos los elementos del tablero
****************************************************************************/
public void mover (int dir) {
	
	//tablet[tempx][tempy] = 0;
	this.agente.mover(dir);
	//tablet[this.agente.pos_x()][ this.agente.pos_y()] = 2;	
	int tempx ,
	tempy ;
	//Movimiento de los Wumpus

	for (int i = 0; i < n_wumps; i++) {

			tempx = this.wumps[i].get_x ();
			tempy = this.wumps[i].get_y ();
		if (this.wumps[i].mover(n,m,tablet)){
			refrescar  (tempx,tempy);
			tempx = this.wumps[i].get_x ();
			tempy = this.wumps[i].get_y ();
			alrededor (tempx,tempy, 5);
			if (wumps[i].choque(agente.pos_x(),agente.pos_y())) {//Si hay un agente comido
				if (agente.get_granadas() > 0){//El agente dispara una granada
					wumps[i].matar ();
					agente.disparo();
					this.refrescar(tempx, tempy);
					if (ciego)
						this.puntuacion +=5000;
					else
						this.puntuacion_h += 5000;
				} else {
					this.agente.muere ();
					this.agente_muerto = true;
					if (this.ciego)
						this.puntuacion -= 10000;
					else
						this.puntuacion_h -= 10000;
				}
			}	
		}
		
	}
	if (granada.get_disparo ()) {
		
		int obstaculo = this.granada.mover(tablet, wumps, n_wumps, n, m);
		
		if (obstaculo != 0){ //Se ha chocado con algo
			if (obstaculo >= 10){ //Se ha impactado con un Wumpus
				wumps[obstaculo - 10].matar ();
				this.puntuacion += 5000;
			}
		}
		
	}
	//repaint ();
}
public void generar (int n, int m, int n_wumps, int n_pre) {
	/*addMouseListener (new MouseEventHandler ());*/
	
	this.n = n;
	this.m = m;
	tablet = new int[n][m];
	heur = new int [n][m];
	this.n_wumps = n_wumps;
	colocados = 0;
	this.n_precipicios = n_pre;
	wumps = new Wumpus [n_wumps];
	for (int i = 0; i < n_wumps; i++) {
		wumps[i] = new Wumpus ();
	}
	pintar = true;
	//tablet[0][0] = 2;
	visitados = new Visitados (n,m);
	anteriores = new Visitados ((n*100),(m*100));
	//this.repaint ();
}


/******************************************************************** 
	Esta función devuelve true si el movimiento es posible 
**********************************************************************/
public boolean posible (int dir) {
	int tempx = this.agente.pos_x (),
	tempy = this.agente.pos_y ();
	int 	sum_x = 0,
		sum_y = 0;
	boolean salida = false;

	
	switch (dir) {
		case 2: // Se mueve al norte
			sum_x = 0;
			sum_y = -1;
		break;
		case 0: //Se mueve al sur
			sum_x = 0;
			sum_y = 1;
		break;
		case 1: //Se mueve al oeste
			sum_x = -1;
			sum_y = 0;
		break;
		case 3: ///Mueve al este
			sum_x = 1;
			sum_y = 0;
		break;
	
		default:
			return false; //La dirección no es la correca
		
	}

	tempx += sum_x;
	tempy += sum_y;

	//System.out.println ("X ="+tempx+" Y="+tempy);
	if ((tempx  < n) && (tempx >= 0)) {
		if ((tempy < m) && (tempy >= 0)) {
			if ((tablet[tempx][tempy] == 9) || (visitados.visitados (tempx, tempy))) {
				if ((visitados.visitados (tempx, tempy)) && (this.n_visitados < 2)){
					this.n_visitados++;
					salida = true;
					visitando = true;
				} else
					salida = false;
			} else {
				if (!visitados.visitados (tempx, tempy)){
					this.n_visitados = 0;
					visitando = false;
				}
					
				
					salida = true;
			}
		}
	}
	/*if (!salida) {
		if (visitados.visitados(tempx, tempy)){
			if ((visitados.visitados(tempx+1, tempy)) && (visitados.visitados(tempx-1, tempy))
				&& (visitados.visitados(tempx, tempy+1)) && (visitados.visitados(tempx, tempy-1)))
				salida = true;
		}
	}*/
	
	return salida;
}
/***********************************************************************
	Esta función nos indica si el agente está en peligro
***********************************************************************/
public boolean  riesgo () {
	boolean salida = false;
	int tempx = this.agente.pos_x (),
	tempy = this.agente.pos_y ();
	//Detecto frescor u olor
	if ((tablet[tempx][tempy] == 5) || (tablet[tempx][tempy] == 6)){ 
					salida = true;
	} else {
					salida = false;
	}
	return salida;
	
}
/******************************************************************** 
	Esta función devuelve 4 si el disparo no es posible
	en otro caso devuelve la dirección del wumpus
**********************************************************************/
public int disparo () {
	int tempx ,
	tempy ;
	int 	sum_x = 0,
		sum_y = 0;
	int salida = 4;
	if (this.agente.get_granadas () == 0) return 4;

	for (int dir = 0; dir < 4; dir++) {
		tempx = this.agente.pos_x ();
		tempy = this.agente.pos_y ();
		switch (dir) {
			case 0: // Se mueve al norte
				sum_x = 0;
				sum_y = -1;
			break;
			case 1: //Se mueve al sur
				sum_x = 0;
				sum_y = 1;
			break;
			case 2: //Se mueve al oeste
				sum_x = -1;
				sum_y = 0;
			break;
			case 3: ///Mueve al este
				sum_x = 1;
				sum_y = 0;
			break;
		}

		tempx += sum_x;
		tempy += sum_y;


		if ((tempx  < n) && (tempx > 0)) {
			if ((tempy < m) && (tempy > 0)) {
				if (tablet[tempx][tempy] != 5) {
				//Dirección del olor de Wumpus
					salida = dir;
				}
			}
		}
	}
	return salida;
	
}
/***********************************************************************
	sabemos si ya estamos en 0,0 con el tesoo
************************************************************************/
public boolean objetivo () {
	int tempx = this.agente.pos_x (),
	tempy = this.agente.pos_y ();
	boolean rico = this.agente.get_rico ();
	System.out.println ("Objetivo riqueza Fuera"+rico+"Pos "+tempx+" "+tempy);
	if ((rico) && (tempx == 0) && (tempy == 0)) {
		System.out.println ("Objetivo riqueza Objetivo"+rico+"Pos "+tempx+" "+tempy);
		return true;
	} else { 
		System.out.println ("Objetivo riqueza !Objetivo"+rico+"Pos "+tempx+" "+tempy);
		return false;
		}

}
/***********************************************************************
	Implementa una busqueda en profundidad
************************************************************************/
public boolean ciego () {
	int tempx,
	tempy ;
	int resta = 0;
	//boolean pos;
	int fallos = 0;
	boolean rico = false;
	boolean meta = false;
	anteriores.reset();
	visitados.reset ();
	anteriores.push (0,0,direccion);
	visitados.push (0,0,0);
	visitados.visitas = true;
	meta = false;
	this.agente_muerto = false;
	this.agente.rico = false;
	this.agente.x = this.agente.y = 0;
	tablet[ptx][pty] = 8;
	agente.granadas = 1;
	this.puntuacion = 0;
	this.mov_ciego = 0;
	this.ciego =  true;
	for (int i = 0; i < n_wumps; i++)
		wumps[i].posicionar();
	while ((anteriores.top() > 0) && (meta == false) && (!this.agente_muerto)) {

		System.out.println ("Tras el While");
		if (agente.rico){
			resta = 2;
		}
		else 
			resta = 0;
		
		if (posible((direccion + resta) % 4)) {//Movimiento
			fallos = 0;  //Se cuenta el nº de fallos
			mover ((direccion + resta) % 4);
			tempx = this.agente.pos_x ();
			tempy = this.agente.pos_y ();
			puntuacion--;
			this.mov_ciego++;
			System.out.println ("X ="+tempx+" Y="+tempy+" Riqueza = "+rico);
			if ((ptx == tempx) && (pty == tempy) && (!agente.rico)) {
				System.out.println ("Tesoro");
				this.agente.recoger ();
				tablet[ptx][pty] = 0;
				rico = true;
				visitados.reset();
				visitados.push (ptx,pty,0);
				this.direccion = 0; //Reseteo la direcciones
			}
			if (rico) {
				System.out.println ("X ="+tempx+" Y="+tempy+" Rico Meta "+meta);
				if ((tempx == 0) && (tempy == 0)) {
					meta = true;
				}
			}
			if (riesgo ()) { //Esta  en una casilla de olor o frescor paso atras
				anteriores.pop ();
				agente.x = anteriores.x[anteriores.top()];
				agente.y = anteriores.y[anteriores.top()];
				direccion = anteriores.dir[anteriores.top()] + 1;
					System.out.println ("Riesgo");
				
			} else {
				anteriores.push (tempx,tempy,direccion);
				visitados.push (tempx,tempy,0);
				System.out.println ("Salida 0"+anteriores.top());
			}
			try {
  					Thread.sleep(500);
			} catch( InterruptedException e ) { }
		} else { //En de no poder aplicar la direccion aplicada hasta ahora
			if (fallos < 3) { //En este nodo no se ha probado con las 4 opciones
				fallos++;
				direccion++;
			} else {
				if (this.n_visitados == 2){
					anteriores.pop ();
					agente.x = anteriores.x[anteriores.top()];
					agente.y = anteriores.y[anteriores.top()];
					direccion = anteriores.dir[anteriores.top()] + 1;
					this.n_visitados = 0;
						System.out.println ("Visitado");
				}
				else {	
					System.out.println ("No Tiene Solución");
					break;
				}
			}
		}
		//repaint ();
		this.pintando(this.getGraphics() );
	}
	if (meta){
		this.puntuacion += 1000;
		return true;
	} else 
		return false;
}
/***********************************************************************
Implementa una busqueda en mediante una heuristica max ((xo-x),(yo-y)) 
************************************************************************/
public boolean heuristica () {
int tempx,
tempy ;

//boolean pos;

boolean rico = false;
boolean meta = false;
int dir;
anteriores.reset();
anteriores.push (0,0,direccion);
visitados.visitas = true;
meta = false;
this.agente_muerto = false;
this.agente.rico = false;
this.agente.x = this.agente.y = 0;
tablet[ptx][pty] = 8;
this.xo = this.ptx;
this.yo = this.pty;
agente.granadas = 1;
this.puntuacion_h = 0;
this.reset ();
this.mov_h = 0;
this.ciego = false;
for (int i = 0; i < n_wumps; i++)
	wumps[i].posicionar();
while ((anteriores.top() > 0) && (meta == false) && (!this.agente_muerto)) {

	
	dir = this.bus_dir();
	
	if (dir != this.error) {//Movimiento
		tempx = this.agente.pos_x ();
		tempy = this.agente.pos_y ();
		this.heur[tempx][tempy] +=10;
		mover (dir);
		tempx = this.agente.pos_x ();
		tempy = this.agente.pos_y ();
		puntuacion_h--;
		this.mov_h++;
		System.out.println ("X ="+tempx+" Y="+tempy+" Riqueza = "+rico);
		if ((ptx == tempx) && (pty == tempy) && (!agente.rico)) {
			System.out.println ("Tesoro");
			this.agente.recoger ();
			tablet[ptx][pty] = 0;
			rico = true;
			this.reset(); //Resetea la matriz heuristica
			this.xo = 0;
			this.yo = 0;
		}
		if (rico) {
			System.out.println ("X ="+tempx+" Y="+tempy+" Rico Meta "+meta);
			if ((tempx == 0) && (tempy == 0)) {
				meta = true;
			}
		}
		if (riesgo ()) { //Esta  en una casilla de olor o frescor paso atras
			anteriores.pop ();
			tempx = this.agente.pos_x ();
			tempy = this.agente.pos_y ();
			agente.x = anteriores.x[anteriores.top()];
			agente.y = anteriores.y[anteriores.top()];
			heur[tempx][tempy] = this.Paso_atras;//Para que no vuelva a esa posición
				System.out.println ("Riesgo");
			
		} else {
			anteriores.push (tempx,tempy,dir);
			System.out.println ("Salida 0"+anteriores.top());
		}
		try {
					Thread.sleep(500);
		} catch( InterruptedException e ) { }
	} else { //En de no poder aplicar la direccion aplicada hasta ahora
		
			System.out.println ("No Tiene Solución");
			break;
			
		
	}
	//repaint ();
	this.pintando(this.getGraphics() );
}
if (meta){
	this.puntuacion_h += 1000;
	return true;
} else 
	return false;
}
/*********************************************************************************************
 * *******************************************************************************************
 * resetea la matriz heuristica
 */
public void reset () {

	for (int i = 0; i < n; i++)
		for (int j = 0; j < m; j++){
			
			if (this.heur[i][j] != this.MuroPresipicio) 
				this.heur[i][j] = 0;
		}
}
/*********************************************************************************************
 * *******************************************************************************************
 * da el valor de heuristica por max ((xo-x),(yo-y))
 */
public int H (int x, int y){
	int tx = Math.abs(xo - x),
		ty = Math.abs(yo - y);
	
	if (tx > ty)
		return tx;
	else
		return ty;
	

}
/**********************************************************************************************
 * Esta función devolverá una dirección 
 */
public int bus_dir () {
	int sum_x = 0,
		sum_y = 0;
	int tempx,
		tempy;
	int minimo = this.error;
	int dir = this.error;
	int temp = this.error;
	
	for (int i = 0;i < 4; i++){
		switch (i) {
		case 2: // Se mueve al norte
			sum_x = 0;
			sum_y = -1;
		break;
		case 0: //Se mueve al sur
			sum_x = 0;
			sum_y = 1;
		break;
		case 1: //Se mueve al oeste
			sum_x = -1;
			sum_y = 0;
		break;
		case 3: ///Mueve al este
			sum_x = 1;
			sum_y = 0;
		break;
	
		}
		tempx = agente.pos_x();
		tempy = agente.pos_y();
		tempx += sum_x;
		tempy += sum_y;
		if ((tempx  < n) && (tempx >= 0)) {
			if ((tempy < m) && (tempy >= 0)) {
				if (tablet[tempx][tempy] != 9){
					if (heur[tempx][tempy] == 0) {//Es una casilla no computada
							temp = heur [tempx][tempy]= this.H(tempx, tempy);
					}
					else {
						temp = heur [tempx][tempy];
					}
				}
			}
		} else {
			temp = this.error;
		}
		
		if (minimo > temp) {
			minimo = temp;
			dir = i;
		}
	}
	System.out.println ("Dirección ="+dir+"Minimo="+minimo);
	return dir;
}
/********************************************************************************************************
*********************************************************************************************************
******************************************************************************************************+**/
//class MouseEventHandler extends MouseAdapter {
		public void mousePressed(MouseEvent e){
		
		}
	//}
	/*public void update (Graphics g) {
		paint (g);
	}*/
  //el resto hace nada 
		public void mouseMoved(MouseEvent evt) {} 
		public void mouseClicked(MouseEvent e) {
		int tx, ty;
		tempx = tx = e.getX ();
		tempy = ty = e.getY ();
		
		switch (colocando) {
		case 1: //Colocando un wumpus
			if (colocados != n_wumps) {
				//tablet[(tx / anchos)][(ty / altos)] = colocando;
				alrededor ((int)(tx / anchos),(int)(ty / altos), 5);
				//wumps[colocados] = new Wumpus ();
				wumps[colocados].posicionar ();
				wumps[colocados].set_x ((int)(tx / anchos));
				wumps[colocados].set_y ((int)(ty / altos));
				colocados = colocados + 1;
				colocando = 0;
			}
		break;
		case 3: //Colocando un presipicio 
			if (n_precipicios > 0) {
				tablet[(tx / anchos)][(ty / altos)] = colocando;
				alrededor ((int)(tx / anchos),(int)(ty / altos), 6);
				n_precipicios--;
				colocando =  0;
			}
		break;
		case 8://Tesoro
			tablet[(tx / anchos)][(ty / altos)] = colocando;
			ptx = (tx / anchos); //Posición en X del tesoro
			pty = (ty / altos); //Posición en y del tesoro
			colocando = 0;
		break;
		case 9://Pared
			tablet[(tx / anchos)][(ty / altos)] = colocando;
			this.heur[(tx / anchos)][(ty / altos)]=this.MuroPresipicio;
			colocando = 0;
		break;
		} 
		//Sistema de apestar
		
		repaint ();
	 }
		public void mouseEntered(MouseEvent evt) { }
		public void mouseExited(MouseEvent evt) { }
	public void mouseReleased(MouseEvent evt) {}
	public void mouseDragged(MouseEvent evt) {}
public int get_colocados () {
	return colocados;
}
public int get_n_wumps () {
	return n_wumps;
}
public int get_n_precipicios () {
	return n_precipicios ;
}
public void set_colocando (int new_estado) {
	colocando = new_estado ;
}
public int get_colocando   () {
	return colocando;
}
public char caracter (int estado) {
	char temp;
	switch (estado) {
		case 0:
			
			temp = ' ';
		break;
		case 1:
			temp = 'W';
		break;
		case 2:
			temp = 'A';
		break;
		case 3:
			temp = 'P';
		break;
		case 4:
			temp = 'G';
		break;
		case 5:
			temp = 'O';
		break;
		case 6:
			temp = 'F';
		break;
		case 7:
			temp = 'R';
		break;
		case 8:
			temp = 'T';
		break;
		case 9: //Pared
			temp ='M';
		break;
		default:	
			temp = ' ';
		break;
	}
	return temp;
}
public int get_estado (int x, int y) {

	return tablet[x][y];
}
public int get_n () {
	return n;
}
public int get_m () {
	return m;
}
public void set_estado (int x, int y, int estado) {
	tablet[x][y] = estado;
}
public void paintComponent (Graphics g) {
	//super.paintComponent (g);
	pintando (g);
}

public void pintando (Graphics g) {
	super.paintComponent(g);
	if (pintar) {
		Dimension d = this.getSize ();

		int alto = d.height;
		int ancho = d.width;
		//Dibujo lineas horizontales
		int alto_filas = alto / m;
		int ancho_columnas = ancho / n;
		int y;
		int xcenter = ancho_columnas /2;
		int ycenter = alto_filas / 2;
		anchos = ancho_columnas;
		altos = alto_filas;

		for (y = 0; y < alto; y += alto_filas ) 
			g.drawLine (0, y, ancho, y);
		//Dbujo lineas Verticales
		int x;
		for (x = 0; x < ancho; x += ancho_columnas) 
			g.drawLine (x,0,x,alto);
		//Dibuja el contenido del tablero
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				g.drawString ( "" + caracter(tablet[i][j]),((i*ancho_columnas)+xcenter), ((j*alto_filas)+ycenter));
			}
		}
		//Dibujo los Wumpus
		for (int i = 0; i < n_wumps; i++) {
			if (wumps[i].get_estado () == 1) {
				g.drawString ( "W",((ancho_columnas*wumps[i].get_x())+xcenter), ((wumps[i].get_y()*alto_filas)+ycenter));
			}
		}
		//Dibujo el Agente
		g.drawString ( "A",((ancho_columnas*agente.pos_x())+xcenter), ((agente.pos_y()*alto_filas)+ycenter));
		//g.drawString ("(" + tempx + "," + tempy + ")", tempx,tempy);
		
		//g.drawString (hora, xdigital, ydigital);
	  }
}

/*******************************************************************************+
* Permite definir el estado de las casillas adyacentes a un precipicio o wumpus
*********************************************************************************/
public void alrededor (int x, int y, int estado) {
	
	if ((x-1) >= 0)  
		if (tablet[x-1][y] == 0)
			tablet[x-1][y] = estado;
	if ((x+1) < (this.n ))  
		if (tablet[x+1][y] == 0)
			tablet[x+1][y] = estado;
	if ((y-1) >= 0) 
		if (tablet[x][y-1] == 0)
			tablet[x][y-1] = estado;
	if ((y+1) < (this.m )) 
		if (tablet[x][y+1] == 0)
			tablet[x][y+1] = estado;
}
/*******************************************************************************+
* Permite refrescar un area
*********************************************************************************/
public void refrescar (int x, int y) {
	
	if ((x-1) >= 0)  
		if (tablet[x-1][y] == 5)
			tablet[x-1][y] = 0;
	if ((x+1) < (this.n ))  
		if (tablet[x+1][y] == 5)
			tablet[x+1][y] = 0;
	if ((y-1) >= 0) 
		if (tablet[x][y-1] == 5)
			tablet[x][y-1] = 0;
	if ((y+1) < (this.m )) 
		if (tablet[x][y+1] == 5)
			tablet[x][y+1] = 0;
}

}
