
public class Visitados {

	int 	x[],
		y[],
		dir [],
		top;
	boolean dir_tomadas [];
	boolean visitas = false;
	public Visitados (int n, int m) {

		x = new int[n*m];
		y = new int[n*m];
		dir = new int [n*m];
		top = 0;
	}
	public boolean visitados (int x, int y) {

		int i = 0;
		boolean diferente = true;
		
		while ((i < top) && (diferente)) {
			if ((this.x[i] == x) && (this.y[i] == y))
				diferente = false;
			else
				i++;
		}
		 if (diferente) 
			return false;
		else
			
				return true;
	}
	public void push (int x, int y, int dir) {
		
		this.x[top] = x;
		this.y[top] = y;
		if (dir != 0)
			this.dir[top] = dir;
		else
			this.dir[top]++;
		top++;
	}
	public void reset () {
		top = 0;
	} 
	public int get_x () {
		return this.x[top-1];
	}
	public int get_y () {
		return this.y[top-1];
	}
	public int get_dir (){
		return this.dir[top-1];
	}
	public int top () {
		return top;
	}
	public void pop () {
		top--;
	}

}
