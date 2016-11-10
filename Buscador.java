

public class Buscador {

	int 	x, //posición en X
		y; //posición en Y
	int granadas =   1; //Granadas que dispone
	boolean rico;
	public Buscador () {
		x = y = 0;
		rico = false;
	}
	public Buscador (int x, int y, int granadas, boolean rico) {
		this.x = x ;
		this.y = y;
		this.granadas = granadas;
		this.rico = rico;
	}
	public boolean muere () {
		x = y = 0;
		return true;
	}
	public void disparo () {
		granadas--;
	}
	public int pos_x () {
		return x;
	}
	public int pos_y () {
		return y;
	}
	public int get_granadas () {
		return granadas;
	}
	public boolean get_rico () {
		return rico;
	}
	public void recoger () {
		rico = true;
	}
	public void mover (int dir) {
		int 	sum_x = 0,
			sum_y = 0;
		
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
		
		}
		this.x += sum_x;
		this.y += sum_y;

		//this.x = this.y = 4;
		
	}
	
}

