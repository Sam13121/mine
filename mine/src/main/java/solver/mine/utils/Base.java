package solver.mine.utils;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Base {
	public Base(){}
	public static int ScreenWidth = 1920;
	public static int ScreenHeight = 1080;

	public static int BoardWidth = 0;
	public static int BoardHeight = 1;
	public static double BoardPix = 0;
	public static int BoardTopW = 0;
	public static int BoardTopH = 0;
	
	public static int mouseLocX = ScreenWidth / 2;
	public static int mouseLocY = ScreenHeight / 2;
	
	public static Scanner scanner = new Scanner(System.in);
	public static Robot robot;
	public static Random rand = new Random();
	
	public static int[][] onScreen = null;
	public static boolean[][] flags = null;

	public static int numMines = 0;
	public static int TOT_MINES = 99;
	
	public static int[][] bayesian_board = null;
	public static boolean[][] knownMine = null;
	public static boolean[][] knownEmpty = null;
	public static ArrayList<boolean[]> bayesian_solutions;
	  
	  // Should be true -- if false, we're bruteforcing the endgame
	public static boolean borderOptimization;
	public static int BF_LIMIT = 8;

}
