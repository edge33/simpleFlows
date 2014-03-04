package it.Maida.Flows.Core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class FlowStruct {

	public static final int NONE = 0;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int YELLOW = 4;
	public static final int ORANGE = 5;
	public static final int CYAN = 6;
	public static final int MAGENTA = 7;
	public static final int PINK = 8;
	public static final int WHITE = 9;
	public static final int LIGHT_GRAY = 10;
	public static final int ARDESIA = 11;
	public static final int AMARANTO = 12;
	public static final int BRIDGE = 15;

	private int[][] scheme;
	private int size;
	private HashMap<Integer, ArrayList<Point>> arcs; // mi salvo gli archi
														// prelevandoli

	// dalla soluzione

	public FlowStruct(int size) {
		this.size = size;
		this.scheme = new int[size][size];
		arcs = new HashMap<Integer, ArrayList<Point>>();
		init();

	}

	public void resetAll() {
		arcs.clear();
		for (int i = 0; i < scheme.length; i++) {
			for (int j = 0; j < scheme.length; j++) {
				scheme[i][j] = NONE;
			}
		}
	}

	private void init() {
		for (int i = 0; i < scheme.length; i++) {
			for (int j = 0; j < scheme[i].length; j++) {
				this.scheme[i][j] = NONE;
			}
		}
	}

	public void setColorAt(int i, int j, int color) {
		this.scheme[i][j] = color;
	}

	public int getSize() {
		return size;
	}

	public int getColorAt(int i, int j) {
		return scheme[i][j];
	}

	public boolean validateStruct() { // controlla se ci sono piu di due colori
										// stessi

		HashMap<Integer, Integer> colors = new HashMap<Integer, Integer>();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				if (scheme[i][j] != NONE) {
					if (colors.containsKey(scheme[i][j])) {
						colors.put(scheme[i][j], colors.get(scheme[i][j]) + 1);
					} else {
						colors.put(scheme[i][j], 1);
					}
				}

			}
		}

		for (Integer color : colors.keySet()) {
			if (colors.get(color) > 2) {
				colors.clear();
				return false;
			}
		}

		colors.clear();
		return true;
	}

	public void addArc(Point a, Integer color) {

		if (!arcs.containsKey(color)) {
			ArrayList<Point> point = new ArrayList<Point>();
			point.add(a);
			arcs.put(color, point);
		} else {
			arcs.get(color).add(a);

		}

	}

	public HashMap<Integer, ArrayList<Point>> getArc() {
		return this.arcs;
	}

}
