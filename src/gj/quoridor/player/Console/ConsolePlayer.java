package gj.quoridor.player.Console;

import java.util.Scanner;
import gj.quoridor.player.Player;
 
public class ConsolePlayer implements Player{

	 boolean isFirst ;
	 private Scanner sc = new Scanner(System.in);

	@Override
	public int[] move() {
		System.out.print("Insert " + (isFirst ? "red" : "blue") + " Spostamento o Muro : ");
		int row = sc.nextInt();
		System.out.print("Insert " + (isFirst ? "red" : "blue") + (row==0? " Direzione :" : "Numero Muro :"));
		int col = sc.nextInt();
		int [] boo=new int[] { row, col};
		return boo ;
		
	}
	public void start(boolean arg0) {
	isFirst=arg0;	
			}
	public void tellMove(int[] arg0) {}
}
