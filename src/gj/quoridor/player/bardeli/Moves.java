package gj.quoridor.player.bardeli;

import gj.quoridor.player.bardeli.MinimumPath;

import java.util.ArrayList;

import gj.quoridor.player.bardeli.BardeliPlayer;;

public class Moves {

	public static ArrayList<int[]> ArrayList( int[][][] tab) {
		int[][][] tabCopyMove=MinimumPath.createBoard(17, 17)  ;
		ArrayList<int[]> moveWall = new ArrayList<int[]>();
		int [] nWall=BardeliPlayer.getnWall();
		
		tabCopyMove = MinimumPath.copyBoard(BardeliPlayer.getTab(), tabCopyMove);

		for (int wall = 0; wall <= 127; wall++) {
			while(nWall[wall]==-1&&wall < 127){
				wall++;
			}
			
			int[] wallCoordinate = MinimumPath.CoordinateByRange(wall, tabCopyMove);		
			if (canCreatedWall(tabCopyMove ,wallCoordinate)==true){// non ho un muro allora lo creo	
				MinimumPath.createWall(tabCopyMove,wallCoordinate);
								
				int pathToWinBardeli=(BardeliPlayer.isFirst())?(MinimumPath.aStar(tab, BardeliPlayer.getBardeli(),MinimumPath.red(BardeliPlayer.getBardeli(), tabCopyMove))):(MinimumPath.aStar(tab, BardeliPlayer.getBardeli(),MinimumPath.blue(BardeliPlayer.getBardeli(), tabCopyMove)));
				int pathToWinAdversary=(!BardeliPlayer.isFirst())?(MinimumPath.aStar(tab, BardeliPlayer.getAdversary(),MinimumPath.red(BardeliPlayer.getAdversary(), tabCopyMove))):(MinimumPath.aStar(tab, BardeliPlayer.getAdversary(),MinimumPath.blue(BardeliPlayer.getAdversary(), tabCopyMove)));
				if( pathToWinBardeli!=-1&&pathToWinAdversary!=-1){
					int[] path={pathToWinBardeli,pathToWinAdversary,wall};
					moveWall.add(path);
				}
			}
			tabCopyMove = MinimumPath.copyBoard(BardeliPlayer.getTab(), tabCopyMove);
		

		}
		
		
		

		return moveWall;

	}
	
	public static boolean canCreatedWall( int[][][] tab,int [] arrWall) {
		boolean can=true;
		
		if(tab[arrWall[0]][arrWall[1]][0]==0){
			if(tab[arrWall[0]][arrWall[1]+1]==null){
				can=(tab[arrWall[0]][arrWall[1]+2]==null)?(true):(tab[arrWall[0]][arrWall[1]+2][0]==0);				
			}else{
				can=(tab[arrWall[0]+2][arrWall[1]]==null)?(true):(tab[arrWall[0]+2][arrWall[1]][0]==0);		
			}
			
		}else{
			can=false;
		}
			
		
		return can;
	}
	public static void removeNWall( int nWall){
		
		int[] walls=BardeliPlayer.getnWall();
		
		walls[nWall]=-1;
		BardeliPlayer.setnWall(walls);
		
	}
	
	public static void removeCross( int []nWall,int [][][] tab){
		if(tab [nWall[0]][nWall[1]+1]==null){
			removeNWall( tab [nWall[0]-1][nWall[1]+1][1]); //rimuove dalla lista il possibile mure che forma la x di muri
			if( tab [nWall[0]][nWall[1]+2]!=null){removeNWall( tab [nWall[0]][nWall[1]+2][1]);}
		}
		if(tab [nWall[0]+1][nWall[1]]==null&&tab [nWall[0]][nWall[1]][1]<120){			
			removeNWall(tab[nWall[0]+1][nWall[1]-1][1]); //rimuove dalla lista il possibile mure che forma la x di muri
			if( tab [nWall[0]+2][nWall[1]]!=null){removeNWall( tab [nWall[0]+2][nWall[1]][1]);}
		}
		removeNWall(tab [nWall[0]][nWall[1]][1]);
	
	}
	
}
