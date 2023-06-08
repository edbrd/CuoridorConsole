package gj.quoridor.player.bardeli;

import java.util.ArrayList;

import gj.quoridor.player.Player;
import gj.quoridor.player.bardeli.MinimumPath;
import gj.quoridor.player.bardeli.Moves;

public class BardeliPlayer implements Player {

	private static int[][][] tab;
	private int wall = 10;
	private static boolean isFirst;
	private static int[] bardeli;
	private static int[] adversary;
	private static int[] nWall = new int[128];

	public static int[] getnWall() {
		return nWall;
	}

	public static void setnWall(int[] nWall) {
		BardeliPlayer.nWall = nWall;
	}

	public static int[][][] getTab() {
		return tab;
	}

	public void setTab(int[][][] tab) {
		BardeliPlayer.tab = tab;
	}

	public int getWall() {
		return wall;
	}

	public void setWall(int wall) {
		this.wall = wall;
	}

	public static boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		BardeliPlayer.isFirst = isFirst;
	}

	public static int[] getBardeli() {
		return bardeli;
	}

	public void setBardeli(int o, int a) {
		int[] tmp = { o, a };
		BardeliPlayer.bardeli = tmp;
	}

	public static int[] getAdversary() {
		return adversary;
	}

	public void setAdversary(int o, int a) {
		int[] tmp = { o, a };
		BardeliPlayer.adversary = tmp;
	}

	@Override
	public int[] move() {
		boolean findWall =false;
		int[] move = { (int) (Math.random() * 2), 0 };
		
		if ( (int)(Math.random() * 2)==1&&getWall() > 0 ) {			
			ArrayList<int[]> moveWall = Moves.ArrayList(tab);
			int[] path = moveWall.get(0);			
			// stampa i possibili muri con i passi 
			for (int i = 0; i < moveWall.size(); i++) {	int[] k = moveWall.get(i);System.out.print(+k[0] + "-" + k[1] + "-" + k[2]);System.out.print("/");}System.out.println();

		for (int i = 1; i < moveWall.size()&&isFirst==true; i++) {  //favorisce red
				int[] tmp = moveWall.get(i);
				if (path[0]<=tmp[0]&&tmp[1]>path[1]) {
					System.out.print("Possibile muro "+tmp[0]+"-"+tmp[1]+"-"+tmp[2]+"  " );
					move[1] = tmp[2];
					findWall=true;					
				}
			}
		for (int i = moveWall.size()-1; i > 0&&isFirst==false; i--) { //blu
			int[] tmp = moveWall.get(i);
			if (path[0]<=tmp[0]&&tmp[1]>path[1]) {
				System.out.print("Possibile muro "+tmp[0]+"-"+tmp[1]+"-"+tmp[2]+"  " );
				move[1] = tmp[2];
				findWall=true;					
			}
		}
			if(findWall==true){
				setWall(getWall() - 1);
			MinimumPath.createWall(tab, MinimumPath.CoordinateByRange(move[1], tab));
			Moves.removeCross(MinimumPath.CoordinateByRange(move[1], tab), tab);			
			move[0] = 1;			
			}
			
		} 
		if (findWall==false)	{
			move[0]=0;
			int[] adiacent = MinimumPath.adiacentCellMin(getBardeli(), tab, isFirst);
			for (int d = 0; d < 4; d++) {
				int[] nm = MinimumPath.adiacentCell(tab, bardeli, d);
				if (nm != null && nm[0] == adiacent[0] && nm[1] == adiacent[1]) {
					move[1] = MinimumPath.directionBasedOnColor(d, isFirst);
				}
			}
			bardeli = MinimumPath.setPositionBasedOnColor(getBardeli(), move[1], isFirst);
			
			System.out.println();					
			
			// stampa dei muri rimossi
			System.out.print("muri rimossi");
			for (int i = 0; i < nWall.length ; i++) {
				if (nWall[i] == -1) {
					System.out.print("-");
					System.out.print(i);

				}
			}
			System.out.println();
			System.out.println("Mossa Comunicata: " + move[0] + "-" + move[1]);
			return move;
		}
		return move;
	}

	@Override
	public void start(boolean arg0) {
		// TODO Auto-generated method stub

		if (arg0) {
			setBardeli(0, 8);
			setAdversary(16, 8);
		} else {
			setBardeli(16, 8);
			setAdversary(0, 8);
		}
		setFirst(arg0);
		tab = MinimumPath.createBoard(17, 17);

	}

	@Override
	public void tellMove(int[] move) {

		if (move[0] == 0) {System.out.println("A rel omve");
			adversary = MinimumPath.setPositionBasedOnColor(getAdversary(), move[1], !isFirst);

		} 
		if(move[0] == 1){
		
			System.out.println("A tell move");
			MinimumPath.createWall(tab, MinimumPath.CoordinateByRange(move[1], tab));
			Moves.removeCross(MinimumPath.CoordinateByRange(move[1], tab), tab);

		}

	}

}
