
public class Granada {
	int 	x,
	y;
boolean disparada;
int dir; //Dirección en la que se dispara la granada
public Granada () {
	disparada = false;
	x = y = 0;
	dir = 100; //No se ha disparado
}
public Granada (int x, int y, int dir, boolean disparo) {
	this.disparada = disparo;
	this.x = x; 
	this.y = y;
	this.dir = dir; 
}
public void disparo (int dir) {
	
	disparada = true;
	this.dir = dir;
}
public int pos_x () {
	return x;
}
public int pos_y () {
	return y;
}
public int get_dir () {
	return dir;
}
public boolean get_disparo () {
	return disparada;
}
public int impacto (int [][]tablero, int n, int m,Wumpus []wumps, int n_wumps) {

	if ((x >= n) || (x <= 0) || (y >= m) || (y <= 0)) {
		return 7; //Choque pared
	} else {
		if (tablero[x][y] == 3){ //Impacto con Precipicio
			return 3;
		} else {
			int i = 0;
			boolean choque = false;
			
			while ((i < n_wumps) && (!choque)) {
				if (wumps[i].choque(this.x, this.y))
					choque = true;
				else 
					i++;
			}
			if (choque)
				return i+10;
			else 
				return 0;
		}
	} 
}
public int mover (int [][]tablero, Wumpus []wumps, int n_wumps, int n, int m) {
	int 	sum_x = 0,
		sum_y = 0;
	
	switch (dir) {
		case 0: // Se mueve alllnorte
			sum_x = 0;
			sum_y = 1;
		break;
		case 1: //Se mueve al sur
			sum_x = 0;
			sum_y = -1;
		break;
		case 2: //Se mueve al oeste
			sum_x = 1;
			sum_y = 0;
		break;
		case 3: ///Mueve al este
			sum_x = -1;
			sum_y = 0;
		break;
	}
	x += sum_x;
	y += sum_y;
	int obstaculo = this.impacto(tablero, n, m, wumps, n_wumps);
	
	if (obstaculo != 0) //Se ha chocado con algo
		this.disparada = false ;
		
	
	return obstaculo;
}
}
