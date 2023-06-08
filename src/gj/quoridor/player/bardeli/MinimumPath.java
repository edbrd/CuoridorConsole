package gj.quoridor.player.bardeli;

public class MinimumPath {
	/*
	 * UTILITY METHODS
	 */
	/*
	 * Absolute value of a number;
	 */
	public static int abs(int n) {
		if (n < 0) {
			n = -n;
		}
		return n;
	}

	/*
	 * ASTAR METHODS
	 */
	/*
	 * Set the p-value and the h-value of an element of the board.
	 */
	public static void set(int[][][] b, int[] c, int p, int h) {
		b[c[0]][c[1]][4] = p;
		b[c[0]][c[1]][5] = h;
	}

	/*
	 * Logically move one element of the board from open to closed.
	 */
	public static void open2closed(boolean[][] open, boolean[][] closed, int[] c) {
		open[c[0]][c[1]] = false;
		closed[c[0]][c[1]] = true;
	}

	public static int[][][] copyBoard(int[][][] b, int[][][] NewBoard) {

		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				if (b[i][j] == null) {
					b[i][j] = null;
				} else {
					for (int k = 0; k < b[i][j].length; k++) {
						NewBoard[i][j][k] = b[i][j][k];
					}

				}
			}
		}

		return NewBoard;
	}

	/*
	 * Path finding through A* method.
	 */
	public static int aStar(int[][][] b, int[] s, int[] t) {
		if((s[0]==-1&&s[1]==-1)||(t[0]==-1&&t[1]==-1)){
			return -1;
		}
		int[][][] NewBoard = createBoard(b.length, b[0].length);
		NewBoard = copyBoard(b, NewBoard);
		boolean[][] open = new boolean[NewBoard.length][NewBoard[0].length];
		boolean[][] closed = new boolean[NewBoard.length][NewBoard[0].length];
		int[][] list = createList(NewBoard.length * NewBoard[0].length);

		int[] start = { s[0], s[1], 0 };
	//	System.out.print("Start" + s[0] + "-" + s[1] + " ");
	//	System.out.print("Target" + t[0] + "-" + t[1] + " ");
		insert(list, start);
		open[s[0]][s[1]] = true;
		while (!isEmpty(list) && !closed[t[0]][t[1]]) {
			int[] m = minimum(list);
			open2closed(open, closed, m);
			for (int d = 0; d < 4; d++) {

				int[] nm = adiacentCell(NewBoard, m, d);
				if (nm != null && !closed[nm[0]][nm[1]]) {
					int p = NewBoard[m[0]][m[1]][4] + 1;
					int h = abs(nm[0] - t[0]) + abs(nm[1] - t[1]);
					if (!open[nm[0]][nm[1]]) {
						set(NewBoard, nm, p, h);
						int[] bo1 = { nm[0], nm[1], p + h };
						insert(list, bo1);
						open[nm[0]][nm[1]] = true;

					} else {
						if (update(list, nm, p + h)) {
							set(NewBoard, nm, p, h);
						}
					}

				}
			}
		}

		if (closed[t[0]][t[1]]) {
			//System.out.println("numero cammino" + NewBoard[t[0]][t[1]][4] + "-" + NewBoard[t[0]][t[1]][5]);
			return NewBoard[t[0]][t[1]][4] + NewBoard[t[0]][t[1]][5];
		}
	//	System.out.println("cammino non trovato");
		return -1;
	}

	/*
	 * BOARD MANAGEMENT METHODS
	 */
	/**
	 * Return the coordinates of adjacent cell in specific direction. Meaning of
	 * directions: 0=up, 1=left, 2=down, 3=right. If cell does not exist or is
	 * not reachable, return null.
	 */
	public static int[] adiacentCell(int[][][] b, int[] c, int d) {
		int[][] delta = { { -2, 0 }, { 0, -2 }, { 2, 0 }, { 0, 2 } };
		int[] ac = { c[0] + delta[d][0], c[1] + delta[d][1] };
		if (b[c[0]][c[1]][d] == 0) {
			ac = null;
		}
		return ac;
	}

	/*
	 * Create the boar1d nxm. Each element of the board contains an array of 6
	 * values. The first four values specify whether a given direction can be
	 * followed starting from the element of the board. The next two values are
	 * used by the A* method. The first one denotes the number of steps executed
	 * to reach the element, the second one a lower bound on the number of steps
	 * to be executed in order to reach the target element from the given
	 * element.
	 * 
	 * Il primo indica il numero di passi per raggiungere L'elemento il secondo
	 * un legame inferiore sul Numero di passi da eseguire per raggiungere
	 * l'Elemento di destinazione dall'elemento specificato.
	 */
	public static int[][][] createBoard(int n, int m) {
		int[][][] b = new int[n][m][6];
		for (int r = 2; r < n - 1; r = r + 2) {
			for (int c = 2; c < m - 1; c = c + 2) {
				int[] bo2 = { 1, 1, 1, 1, 0, 0 };
				b[r][c] = bo2;
			}
		}
		for (int c = 2; c < m - 1; c = c + 2) { // prima riga su
			int[] bo3 = { 0, 1, 1, 1, 0, 0 };
			b[0][c] = bo3;
		}
		for (int c = 2; c < m - 1; c = c + 2) { // ultima riga giu
			int[] bo4 = { 1, 1, 0, 1, 0, 0 };
			b[n - 1][c] = bo4;
		}
		for (int r = 2; r < n - 1; r = r + 2) { // prima riga sx
			int[] bo5 = { 1, 0, 1, 1, 0, 0 };
			b[r][0] = bo5;
		}
		for (int r = 2; r < n - 1; r = r + 2) { // prima riga dx
			int[] bo6 = { 1, 1, 1, 0, 0, 0 };
			b[r][m - 1] = bo6;
		}
		int[] bo7 = { 0, 0, 1, 1, 0, 0 }; // su sx
		b[0][0] = bo7;
		int[] bo8 = { 0, 1, 1, 0, 0, 0 }; // su dx
		b[0][m - 1] = bo8;
		int[] bo9 = { 1, 0, 0, 1, 0, 0 }; // giu sx
		b[n - 1][0] = bo9;
		int[] bo10 = { 1, 1, 0, 0, 0, 0 }; // giu dx
		b[n - 1][m - 1] = bo10;

		int tmp = 0;
		for (int i = 0; i < 16; i = i + 2) {
			for (int j = 0; j < 17; j++) {
				if ((b[i][j][0] == 0) && (b[i][j][1] == 0) && (b[i][j][2] == 0) && (b[i][j][3] == 0)
						&& (b[i][j][4] == 0)) {
					int[] zero = { 0, tmp };
					tmp++;
					b[i][j] = zero;
				}
			}
			for (int j = 0; j < 15; j = j + 2) {
				if ((b[i + 1][j][0] == 0) && (b[i + 1][j][1] == 0) && (b[i + 1][j][2] == 0) && (b[i + 1][j][3] == 0)
						&& (b[i + 1][j][4] == 0)) {
					int[] zero = { 0, tmp };
					tmp++;
					b[i + 1][j + 1] = null;
					b[i + 1][b.length - 1] = null;
					b[i + 1][j] = zero;
					b[16][j + 1] = null;
				}
			}
		}
		return b;
	}

	/*
	 * Put an obstacle in a cell of the board. Update adjacent cell to avoid the
	 * direction to the cell in which the obstacle is put.
	 */
	public static void createWall(int[][][] b, int[] w) {
		if (b[w[0]][w[1]][0] == 1) {
			b[-10][0][0] = 0;
		} // controlla se è gia stato creato un mure oppure no

		if (b[w[0]][w[1] + 1] == null) {			// se abbaimo a destra null	
			
			b[w[0]][w[1]][0] = 1; // setta a 1 la cella di due posizioni che
									// indica muro!

			if (w[0] < b.length - 1 && b[w[0] + 1][w[1]] != null) { // setta la
																	// cella in
																	// basso
				b[w[0] + 1][w[1]][0] = 0;
			}
			if (w[0] > 0 && b[w[0] - 1][w[1]] != null) {// setta la cella in
														// alto
				b[w[0] - 1][w[1]][2] = 0;
			}

			if (w[0] < b.length - 1 && b[w[0] + 1][w[1] + 2] != null) { // setta
																		// la
																		// cella
																		// in
																		// basso
				b[w[0] + 1][w[1] + 2][0] = 0;
			}
			if (w[0] > 0 && b[w[0] - 1][w[1] + 2] != null) {// setta la cella in
															// alto
				b[w[0] - 1][w[1] + 2][2] = 0;
			}
			if (b[w[0]][w[1] + 2] != null) { // controllo se abbiamo 2 null di
												// fila
				b[w[0]][w[1] + 2][0] = 1; // setta a 1 la cella di due posizioni
											// che indica muro!
			}

		}

		if (b[w[0] + 1][w[1]] == null) {

			b[w[0]][w[1]][0] = 1;
			if (w[1] < b[0].length - 1 && b[w[0]][w[1] + 1] != null) { // setta
																		// la
																		// cella
																		// a
																		// destra
				b[w[0]][w[1] + 1][1] = 0;
			}
			if (w[1] > 0 && b[w[0]][w[1] - 1] != null) { // selle la cella a
															// sinistra
				b[w[0]][w[1] - 1][3] = 0;
			}

			if (w[1] < b[0].length - 1 && b[w[0] + 2][w[1] + 1] != null) { // setta
																			// la
																			// cella
																			// a
																			// destra
				b[w[0] + 2][w[1] + 1][1] = 0;
			}

			if (w[1] > 0 && b[w[0] + 2][w[1] - 1] != null) { // selle la cella a
																// sinistra
				b[w[0] + 2][w[1] - 1][3] = 0;
			}
			if (b[w[0] + 2][w[1]] != null) { // controllo se abbiamo 2 null di
												// fila
				b[w[0] + 2][w[1]][0] = 1; // setta a 1 la cella di due posizioni
											// che indica muro!
			}
		}

	}

	
	/*
	 * LIST MANAGEMENT METHODS
	 */
	/*
	 * Print textual representation of list.
	 */
	public static void printList(int[][] l) {
		System.out.print("<");
		for (int i = 0; i < l.length; i++) {
			if (l[i] != null) {
				System.out.print("[" + l[i][0] + "," + l[i][1] + "," + l[i][2] + "]");
			}
		}
		System.out.println(">");
	}

	/*
	 * Update the value of an element in the list. The method assumes that the
	 * element is in the list.
	 */
	public static boolean update(int[][] l, int[] c, int v) {
		int i = 0;
		while (l[i] == null || l[i][0] != c[0] || l[i][1] != c[1]) {
			i = i + 1;
		}
		if (l[i][2] > v) {
			l[i][2] = v;
			return true;
		}
		return false;
	}

	/*
	 * Extract minimum element from the list. The minimum is decided on the
	 * ground of the third value of each element in the list.
	 */
	public static int[] minimum(int[][] l) {
		int min = 2 * l.length;
		int iMin = -1;
		for (int i = 0; i < l.length; i++) {
			if (l[i] != null && l[i][2] < min) {
				min = l[i][2];
				iMin = i;
			}
		}
		int[] t = { l[iMin][0], l[iMin][1], l[iMin][2] };
		remove(l, t);
		return t;
	}

	/*
	 * Create a list, implemented by means of an array. Each element of the list
	 * is a pair of coordinates (which uniquely identifies the element) and a
	 * value associated to the element.
	 */
	public static int[][] createList(int s) {
		return new int[s][];
	}

	/*
	 * Check whether the list is empty, that is, if all elements are null.
	 */
	public static boolean isEmpty(int[][] l) {
		int i = 0;
		while (i < l.length && l[i] == null) {
			i = i + 1;
		}
		return i == l.length;
	}

	/*
	 * Insert element in the list. The method assumes that the list is not full.
	 */
	public static void insert(int[][] l, int[] e) {
		int i = 0;
		while (i < l.length && l[i] != null) {
			i = i + 1;
		}
		int[] bo11 = { e[0], e[1], e[2] };
		l[i] = bo11;
	}

	/*
	 * Remove element from the list. The method assumes that the element is in
	 * the list.
	 */
	public static void remove(int[][] l, int[] e) {
		int i = 0;
		while (l[i] == null || l[i][0] != e[0] || l[i][1] != e[1]) {
			i = i + 1;
		}
		l[i] = null;
	}

	public static int[] CoordinateByRange(int n, int[][][] tab) {
		int[] coordinate = { -1, -1 };
		for (int i = 0; i < tab.length && coordinate[0] == -1; i = i + 2) {
			for (int j = 1; j < tab.length && coordinate[0] == -1; j = j + 2) {
				if (tab[i][j][1] == n) {
					coordinate[0] = i;
					coordinate[1] = j;
				}
			}
			for (int j = 0; j < tab.length - 2 && coordinate[0] == -1; j = j + 2) {
				if (tab[i + 1][j][1] == n) {
					coordinate[0] = i + 1;
					coordinate[1] = j;
				}
			}
		}
		return coordinate;
	}

	// sono rosso viglio vedere se ho un cammino
	public static int[] red(int[] start, int[][][] b) {
		int[] flag = { -1, -1 };
		int path = 9999;
		int[] target = { b.length - 1, b.length - 1 };
		while (target[1] >= 0) {
			if (aStar(b, start, target) != -1 && aStar(b, start, target) < path) {
				flag[0] = target[0];
				flag[1] = target[1];
				path = aStar(b, start, target);
			}
			target[1] = target[1] - 2;
		}
		return flag;
	}

	// sono blue viglio vedere se ho un cammino
	public static int[] blue(int[] start, int[][][] b) {
		int[] flag = { -1, -1 };
		int path = 9999;
		int[] target = { 0, b.length - 1 };
		while (target[1] >= 0) {
			if (aStar(b, start, target) != -1 && aStar(b, start, target) < path) {
				flag[0] = target[0];
				flag[1] = target[1];
				path = aStar(b, start, target);
			}
			target[1] = target[1] - 2;
		}
		return flag;
	}

	public static int[] adiacentCellMin(int[] start, int[][][] b, boolean redOrBlue) {
		int[] adiacent = new int[2];
		int minAdiacentPath = 9999;

		for (int d = 0; d < 4; d++) {
			int[] cellAdiacent = adiacentCell(b, start, d);
			if (cellAdiacent != null && redOrBlue == true
					&& aStar(b, cellAdiacent, red(cellAdiacent, b)) < minAdiacentPath) {
				minAdiacentPath = aStar(b, cellAdiacent, red(cellAdiacent, b));
				adiacent = cellAdiacent;
			}

			if (cellAdiacent != null && redOrBlue == false
					&& aStar(b, cellAdiacent, blue(cellAdiacent, b)) < minAdiacentPath) {
				minAdiacentPath = aStar(b, cellAdiacent, blue(cellAdiacent, b));
				adiacent = cellAdiacent;
			}
		}

		return adiacent;
	}


	
	/*
	 * This is the main method which tests the correctness of the aStar method.
	 * The board configuration is the following (B:empty, W:wall, S: start, T:
	 * target).
	 * 
	 * BBBBBBB BBBWBBB BBBWBBB BSBWBBB BWBWBTB BBBBBBB
	 * 
	 * The length of the shortest path is 7. By un-commenting the first
	 * instruction, the length of the shortest path is 11. By un-commenting also
	 * the next two instructions, the length of the shortest path is 13. By
	 * un-commenting also the last instructions, there s no and the answer is
	 * -1.
	 */

	public static int directionBasedOnColor(int d, boolean isFirst) {
		int direction = -1;
		switch (d) {

		case 0:
			direction = (isFirst) ? (1) : (0);
			break;
		case 1:
			direction = (isFirst) ? (3) : (2);
			break;
		case 2:
			direction = (isFirst) ? (0) : (1);
			break;
		case 3:
			direction = (isFirst) ? (2) : (3);
			break;
		}
		return direction;
	}

	public static int[] setPositionBasedOnColor(int[] position, int direction, boolean isFirst) {

		switch (direction) {

		case 0:
			position[0] = (isFirst) ? (position[0] + 2) : (position[0] - 2);
			break;

		case 1:
			position[0] = (isFirst) ? (position[0] - 2) : (position[0] + 2);
			break;

		case 2:
			position[1] = (isFirst) ? (position[1] + 2) : (position[1] - 2);
			break;

		case 3:
			position[1] = (isFirst) ? (position[1] - 2) : (position[1] + 2);
			break;

		}
		return position;

	}

}
