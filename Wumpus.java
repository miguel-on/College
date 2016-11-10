import java.util.*;
import java.text.*;

import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Wumpus {
	
	int 	x,
		y,
		estado; //Para ver si está vivo 0 -> Muerto   1 -> Vivo 25 -> No posicionado
	

	public Wumpus () {
		x = 0;
		y = 0;
		estado = 25;
	}
	public Wumpus (int x, int y, int nestado) {
		this.x = x;
		this.y = y;
		this.estado = nestado;
	}
	public void posicionar () { //Para que se vea en el tablero
		estado = 1;
	}
	public int get_estado (){
		return estado;
	}
	public void matar () {
		estado = 0;
	}
	public int get_x () {
		return x;
	}
	public int get_y () {
		return y;
	}
	public void set_x (int x) {
		this.x = x;
 	}
	public void set_y (int y) {
		this.y = y;
	}
	public boolean choque (int x, int y) {
		
		if ((x == this.x) && (y == this.y))
			return true;
		else
			return false;
		
		
	}
	public boolean mover (int n, int m, int [][]tablero) {
		Random rnd = new Random();
		int dir = rnd.nextInt(4);
		int 	sum_x = 0,
				sum_y = 0,
				tempx = x,
				tempy = y;
	if (estado == 0) return false; //Wumpus muerto
	switch (dir % 4) {
		case 0: // Se mueve al norte
			sum_x = 0;
			sum_y = -1;
		break;
		case 2: //Se mueve al sur
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
	tempx += sum_x;
	tempy += sum_y;
	
	if ((tempx  < n) && (tempx >= 0)) {
		if ((tempy < m) && (tempy >= 0)) {
			if ((tablero[tempx][tempy] != 9)) {
					this.x = tempx;
					this.y = tempy;
					if (tablero[tempx][tempy] == 3) //El wumpus cae en presipicio
						this.matar();
					return true;
			} else {
					return false;
			}
		}
	}
		return false;//No se ha movido el wumpus		
	}
}
