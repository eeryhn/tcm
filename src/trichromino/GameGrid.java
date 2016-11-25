/**
 * GameGrid is an array representation of a grid occupied by black and white squares
 * in which adjacent squares have the potential to change the visual representation
 * of each other.
 * <p>
 * A array correspondence of a visual representation of a grid can be generated. However, 
 * the grid itself is stored with all squares represented as an integer corresponding to
 * their respective original colours.
 * 
 * @author Lisa Li (primary creator)
 * @author Angela Wu
 * @version %I%, %G%
 * */

package trichromino;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.awt.Point;

public class GameGrid implements Serializable, Cloneable {
  
  // instance variables
  private int[][] grid;
  private LinkedList<PieceNode<Piece>> pieces;
 
  private final int MAX_WIDTH, MAX_HEIGHT;
  private final int EMPTY = 0;
  
  // "color" integer identifiers
  private final int WHITE = 1;
  private final int BLACK = 3;
  private final int GRAY = 5;
  
  private final int TRAP = 9;
  private final int T_BLACK = 10;
  private final int T_WHITE = 12;
  
  // used to identify a floating piece
  private final int OFFSET_FACTOR = 13;
  
  // range from EMPTY (0) to BLACK(3)
  // for getAdjacent.
  private final int NUM_VALUES = 4;
  
  // for serialization
  private static final long serialVersionUID = 1;
  
  
  /**
   * Creates an empty GameGrid with n rows and m columns
   * 
   * @param rows the number of rows in this grid
   * @param cols the number of columns in this grid
   */
  public GameGrid(int rows, int cols) { 
    
    grid = new int[rows][cols];
    pieces = new LinkedList<PieceNode<Piece>>();
    
    MAX_WIDTH = cols;
    MAX_HEIGHT = rows;
    
  }
  
  /**
   * Returns an array representation of this grid.
   * 
   * @return the array representation of this grid
   */  
  public int[][] getGrid() {
    
    return grid;
    
  }
  
  /**
   * Gets the width of this grid.
   * 
   * @return the width of the grid
   */
  public int getWidth() {
    
    return MAX_WIDTH;
    
  }
  
  /**
   * Gets the height of this grid.
   * 
   * @return the height of the grid
   */
  public int getHeight() {
    
    return MAX_HEIGHT;
    
  }
  
  
  /**
   * Creates a white square in the specified location.
   * 
   * @param row the target row
   * @param col the target column
   * @throws IllegalStateException If the space is already occupied by a square.
   */
  public void setWhite(int row, int col) {
    
    if (!isEmptySquare(row,col)) throw new IllegalStateException("setWhite: Space is already occupied");
    
    grid[row][col] += WHITE;
        
  }
  
  /**
   * Determines if the specified space is occupied by a white square.
   * 
   * @param row the target row
   * @param col the target column
   * @return true if the specified space is occupied by a white square
   */
  public boolean isWhite(int row, int col) {
    
    return (grid[row][col] == WHITE || grid[row][col] == T_WHITE) ;
    
  }
  
  /**
   * Removes the white square from the specified location.
   * 
   * @param row the target row
   * @param col the target column
   * @throws IllegalStateException If the space is not occupied by a white square.
   */
  public void removeWhite(int row, int col) {
    
    if (!isWhite(row,col)) throw new IllegalStateException("removeWhite: Space is unoccupied by white square.");
    
    grid[row][col] -= WHITE;
    
  }
  
  /**
   * Creates a black square at the specified location.
   * 
   * @param row the target row
   * @param col the target column
   * @throws IllegalStateException If the space is already occupied by a square.
   */
  public void setBlack(int row, int col) {
    
    if (!isEmptySquare(row,col)) throw new IllegalStateException("setBlack: Space is already occupied");
    
    grid[row][col] += BLACK;
    
  }
  
  /**
   * Determines if the specified space is occupied by a black square.
   * 
   * @param row the target row
   * @param col the target column
   * @return true if the specified space is occupied by a black square
   */
  public boolean isBlack(int row, int col) {
    
    return (grid[row][col] == BLACK || grid[row][col] == T_BLACK) ;
    
  }
  
  /**
   * Removes the black square from the specified location.
   * 
   * @param row the target row
   * @param col the target column
   * @throws IllegalStateException If the space is not occupied by a white square.
   */
  public void removeBlack(int row, int col) {
    
    if (!isBlack(row,col)) throw new IllegalStateException("removeBlack: Space is unoccupied by black square.");
    
    grid[row][col] -= BLACK;
    
  }
  
  /**
   * Creates a trap in this grid at the specified position.
   * 
   * @param row the target row
   * @param col the target column
   * @throws IllegalStateException If the space is already a trap.
   */
  public void setTrap(int row, int col) {
    
    if (hasTrap(row,col)) throw new IllegalStateException("setTrap: There is already a trap");
    
    grid[row][col] += TRAP;
    
  }
  
  /**
   * Determines if there is a trap at the specified position in this grid.
   * 
   * @param row the target row
   * @param col the target column
   * @return true if there is a trap at the specified position.
   */
  public boolean hasTrap(int row, int col) {
    
    return grid[row][col] >= TRAP;
    
  }
  
  /**
   * Removes the trap at the specified position in this grid.
   * 
   * @param row the target row
   * @param col the target column
   * @throws IllegalStateException if the specified position in the grid is not a trap.
   */
  public void removeTrap(int row, int col) {
    
    if (!hasTrap(row,col)) throw new IllegalStateException("removeTrap: Space is not a trap.");
    
    grid[row][col] -= TRAP;
    
  }
  
  /**
   * Determines whether or not the specified space is occupied.
   * 
   * @param row the target row
   * @param col the target column
   * @return true if the specified space is unoccupied by a square.
   */
  public boolean isEmptySquare(int row, int col) {
    
    return (grid[row][col] == EMPTY || grid[row][col] == TRAP);
    
  }
  
  /**
   * Adds the given piece to this grid with the upper left corner at the given point.
   * 
   * @param pce the piece being added
   * @param pt the point at which the piece is being added (x = col, y = row)
   * @throws IllegalArgumentException If the piece is empty.
   * @throws IndexOutOfBoundsException If the piece does not fit in this grid.
   * @throws IllegalStateException If the piece is overlapping an occupied space.
   */
  public void addPiece(Piece pce, Point pt) {
    
    if (pce.isEmpty()) throw new IllegalArgumentException("addPiece: Cannot add an empty piece to grid");
    
    if (pieceFits(pce,pt)) {
      int[][] matrix = pce.getMatrix();
      
      int startRow = pt.x;
      int startCol = pt.y;
      
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[i].length; j++) {
          
          grid[i+startCol][j+startRow] += matrix[i][j];
          
        }
      }
      
      PieceNode<Piece> addition = new PieceNode<Piece>(pce,pt);
      addition.place();
      pieces.add(addition);
    }
    
  }  
  
  /**
   * Returns an array representation of this grid with the target piece hovering at the target point.
   * 
   * @param pce the hovering piece
   * @param pt the point at which the piece is hovering (x = col, y = row)
   * @throws IllegalArgumentException If the piece is empty.
   * @throws IndexOutOfBoundsException If the piece does not fit in this grid.
   * @return The array representation of this grid with the target piece hovering at the target point.
   */
  public int[][] showPiece(Piece pce, Point pt) {
    
    // Essentially a combination of addPiece and getGrid that uses an offset factor to distinguish
    // between placed and unplaced squares.  This factor is used by the GUI to determine
    // the specific appearance of a square.
    if (pce.isEmpty()) throw new IllegalArgumentException("showPiece: Cannot show an empty piece to grid");
    
    int[][] matrix = pce.getMatrix();
    int[][] displayGrid = evaluate();
    
    int startRow = pt.y;
    int startCol = pt.x;
    
    if (startRow+matrix.length > MAX_HEIGHT || startCol+matrix[0].length > MAX_WIDTH) 
      throw new IndexOutOfBoundsException("showPiece: Piece cannot be shown outside the grid");
    
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
          
        if (matrix[i][j] > 0)
          //offset factor allows 
          displayGrid[i+startRow][j+startCol] += matrix[i][j] + OFFSET_FACTOR;
        
      }
    }
    
    return displayGrid;
    
  }
  
  /**
   * Determines whether or not the piece can be placed at the given point in this grid.
   * 
   * @param pce the piece being placed
   * @param pt the point at which the piece is being placed (x = col, y = row)
   * @return true if the piece can be placed at the given point in this grid.
   * @throws IllegalArgumentException If the piece is empty.
   */
  public boolean pieceFits(Piece pce, Point pt) {
    
    if (pce.isEmpty()) throw new IllegalArgumentException("pieceFits: Cannot try an empty piece");
    
    int[][] matrix = pce.getMatrix();
    
    int startRow = pt.y;
    int startCol = pt.x;
    
    if (startRow+matrix.length > MAX_HEIGHT || startCol+matrix[0].length > MAX_WIDTH) 
      return false;   
    
    for (int i = 0;i<matrix.length;i++) {
      for (int j = 0; j<matrix[i].length;j++) {
        
        if (matrix[i][j]!= EMPTY && !isEmptySquare(i+startRow,j+startCol))
          return false;
        
      }
    }
    
    return true;
    
  }
  
  /**
   * Removes the given piece from this grid with the upper left corner at the given point.
   * 
   * @param pce the piece being removed
   * @throws IllegalArgumentException If the piece is not in this grid.
   * @return The node corresponding to the removed piece.
   */
  public PieceNode<Piece> removePiece(PieceNode<Piece> pce) {
    
    for (PieceNode<Piece> piece : pieces) {
      if (piece.equals(pce)) {
      
        int[][] matrix = piece.getObj().getMatrix();
        
        int startRow = piece.getFloatPos().y;
        int startCol = piece.getFloatPos().x;
        
        for (int i = 0; i < matrix.length; i++) {
          for (int j = 0; j < matrix[i].length; j++) {
            
            grid[i+startRow][j+startCol] -= matrix[i][j];
            
          }
        }
        
        pieces.remove(piece);
        piece.displace();
        
        return piece;
        
      }
    }
    
    throw new IllegalArgumentException("removePiece: Piece is not in grid");
    
  }
  
  /**
   * Removes all pieces from the grid and returns the list of pieces.
   * Used in game creation and game clear.
   * 
   * @return A list of all removed pieces.
   */
  public LinkedList<PieceNode<Piece>> removeAll() {
    
    LinkedList<PieceNode<Piece>> list = new LinkedList<PieceNode<Piece>>();
    
    while (pieces.size()>0) {
      PieceNode<Piece> addition = removePiece(pieces.peek());
      addition.reset();
      list.add(addition);
    }
    
    return list;
    
  }
  
  /**
   * Gets the colour of the square at the given position (accounts for occupied traps).
   * 
   * @param row the target row
   * @param col the target column
   * @return the integer representation of the colour of the square at the given position.
   */
  private int getOrigShade(int row, int col) {
    
    int color = grid[row][col];
    
    switch (color) {
      
      case T_BLACK: 
        color = BLACK;
        break;
      case T_WHITE: 
        color = WHITE;
        break;
      
    }
    
    return color;
    
  }
  
  /**
   * Gets the number of empty, white, and black squares adjacent to the given position.
   * 
   * @param row the target row
   * @param col the target column
   * @return an int[] of length three ({empty, white, black}).
   * @throws ArrayIndexOutOfBoundsException if the target position is not on the grid
   */
  public int[] getAdjacent(int row, int col) {
    
    if (row >= MAX_HEIGHT || col >= MAX_WIDTH || row < 0 || col < 0) 
      throw new ArrayIndexOutOfBoundsException("getAdjacent: target out of bounds");
    
    int[] adjacentCount = new int[NUM_VALUES];
    
    if (row > 0) adjacentCount[getOrigShade(row-1,col)%TRAP]++;
    if (col > 0) adjacentCount[getOrigShade(row,col-1)%TRAP]++;
    if (row < MAX_HEIGHT-1) adjacentCount[getOrigShade(row+1,col)%TRAP]++;
    if (col < MAX_WIDTH-1) adjacentCount[getOrigShade(row,col+1)%TRAP]++;
    
    return adjacentCount;
    
  }
  
  /**
   * Returns a list of all empty points strictly adjacent (left, right, up, down) 
   * to the target location.
   * 
   * @param row the target row
   * @param col the target column
   * @return A list of all adjacent empty points.
   */
  public LinkedList<Point> strictAdjEmpty(int row, int col) {
    
    if (row >= MAX_HEIGHT || col >= MAX_WIDTH || row < 0 || col < 0) 
      throw new ArrayIndexOutOfBoundsException("strictAdjEmpty:target out of bounds");
    
    LinkedList<Point> empty = new LinkedList<Point>();
    
    if (row > 0 && isEmptySquare(row-1,col)) empty.add(new Point(col,row-1));
    if (col > 0 && isEmptySquare(row,col-1)) empty.add(new Point(col-1,row));
    if (row < MAX_HEIGHT-1 && isEmptySquare(row+1,col)) empty.add(new Point(col,row+1));
    if (col < MAX_WIDTH-1 && isEmptySquare(row,col+1)) empty.add(new Point(col+1,row));
    
    return empty;
    
  }
  
  /**
   * Returns a list of all empty points loosly adjacent (diagonals included) 
   * to the target location.
   * To be used in farther development of the game.
   * 
   * @param row the target row
   * @param col the target column
   * @return A list of all adjacent empty points.
   */
  public LinkedList<Point> looseAdjEmpty(int row, int col) {
    
    if (row >= MAX_HEIGHT || col >= MAX_WIDTH || row < 0 || col < 0) 
      throw new ArrayIndexOutOfBoundsException("looseAdj: target out of bounds");
    
    LinkedList<Point> empty = new LinkedList<Point>();
    
    if (row > 0) {
      if (isEmptySquare(col,row-1)) empty.add(new Point(col,row-1));
      if (col > 0)
        if (isEmptySquare(col-1,row-1))empty.add(new Point(col-1,row-1));
      if (col < MAX_WIDTH-1)
        if (isEmptySquare(col+1,row-1))empty.add(new Point(col+1,row-1));
    }
    if (row < MAX_HEIGHT-1) {
      if (isEmptySquare(col,row+1)) empty.add(new Point(col,row+1));
      if (col > 0)
        if (isEmptySquare(col-1,row+1)) empty.add(new Point(col-1,row+1));
      if (col < MAX_WIDTH-1)
        if (isEmptySquare(col+1,row+1)) empty.add(new Point(col+1,row+1));
    }
    if (col > 0) {
      if (isEmptySquare(col-1,row)) empty.add(new Point(col-1,row));
    }
    if (col < MAX_WIDTH-1) {
      if (isEmptySquare(col+1,row)) empty.add(new Point(col+1,row));
    }
    
    return empty;
    
  }
  
  /**
   * Gets the final (displayed) colour of the square at the given position (accounts for occupied traps).
   * 
   * @param row the target row
   * @param col the target column
   * @return the integer representation of the final (displayed) colour of the square at the given position.
   */
  public int getVisShade(int row, int col) {
    
    int[] adj = getAdjacent(row,col);
    
    int orig = getOrigShade(row,col);
    
    switch (orig) {
      
      case WHITE : //if the square is white and adjacent to one or more black squares, it changes.
        
        if (adj[BLACK]==1) return GRAY;
        else if (adj[BLACK]>1) return BLACK;
        else return orig;
        
      case BLACK : //if the square is black and adjacent to one or more black squares, it changes.
        
        if (adj[WHITE]==1) return GRAY;
        else if (adj[WHITE]>1) return WHITE;
        else return orig;
      
      default : 
        
        return orig;
      
    }
    
  }
  
  /**
   * Creates a clone of this grid.
   * 
   * @return a clone of this grid.
   */
  public GameGrid clone() {
    
    GameGrid clone = new GameGrid(MAX_HEIGHT, MAX_WIDTH);
    for (PieceNode<Piece> pce : pieces) {
      
      clone.pieces.add(pce);
      
    }
    for (int i = 0; i < MAX_HEIGHT ; i++) {
      for (int j = 0; j < MAX_WIDTH ; j++) {
        
        clone.grid[i][j] = grid[i][j];
        
      }
    }
    
    return clone;
    
  }
  
  /**
   * Returns the number of empty squares in this grid.
   * 
   * @return the number of empty squares in this grid.
   */
  public int numEmpty() {
    
    int empty = 0;
    
    for (int i = 0; i < MAX_HEIGHT ; i++) {
      for (int j = 0; j < MAX_WIDTH ; j++) {
        
        if (isEmptySquare(i,j)) empty++;
        
      }
    }
    
    return empty;
    
  }
  
  /**
   * Determines whether or not this grid is filled (used primarily for game creation)
   * A grid is consistered filled when it is around 83% full or none of the empty squares
   * in the grid are adjacent to two or more empty squares.
   * 
   * @return true if the grid is considered filled.
   */
  public boolean isComplete() {
    
    if (numEmpty() < (MAX_HEIGHT*MAX_WIDTH/6)) return true;
    for (int i = 0; i < MAX_HEIGHT; i++) {
      for (int j = 0; j < MAX_WIDTH; j++) {
        
        if (isEmptySquare(i,j) && getAdjacent(i,j)[EMPTY] > 1) return false;
        
      }
    }
    
    return true;
    
  }
  
  /**
   * Returns a string representation of this grid.
   * 
   * @return a string representation of this grid.
   */
  public String toString() {
    
    String s = "";
    
    for (int i = 0; i < MAX_HEIGHT ; i++) {
      for (int j = 0; j < MAX_WIDTH ; j++) {
        
        s += grid[i][j] + " " ;
        
      }
      
      s += "\n";
        
    }
    
    return s;
    
  }
  
  /**
   * Returns an array representation of the evaluation (final visual representation) of this grid.
   * 
   * @return an array representation of this grid evaluated.
   */
  public int[][] evaluate() {
    
    int[][] solution = new int[MAX_HEIGHT][MAX_WIDTH];
    
    for (int i = 0; i < MAX_HEIGHT ; i++) {
      for (int j = 0; j < MAX_WIDTH ; j++) {
        
        solution[i][j] = getVisShade(i,j);
        
      }
      
    }
    
    return solution;
    
  }
  
  /**
   * Determines whether or not this grid (evaluated) matches a given solution array.
   * 
   * @param solution the array to which this evaluated grid is being compared.
   * @return true if this grid's evaluation matches the solution.
   */
  public boolean matches(int[][] solution) {
    
    if (grid.length != solution.length || grid[0].length != solution[0].length) return false;
    
    int[][] evaluatedGrid = evaluate();
    
    for (int i = 0; i < MAX_HEIGHT ; i++) {
      for (int j = 0; j < MAX_WIDTH; j++) {
        if (evaluatedGrid[i][j] != solution[i][j]) return false;
      }
    }
    
    return true;
    
  }
  
  /** 
   * Moderate testing done with GameGrid class.
   */
  public  static void main (String[]args) {
    
    Piece b = new Piece(5,5,3);
    b.addSquare(3,3);
    b.addSquare(3,2);
    b.addSquare(2,3);
    b.addSquare(1,3);
    b = b.crop();
    System.out.println(b);
    
    Piece w = new Piece(5,5,1);
    w.addSquare(1,2);
    w.addSquare(1,3);
    w.addSquare(2,2);
    w.addSquare(3,2);
    w = w.crop();
    System.out.println(w);
    
    GameGrid grid = new GameGrid(10,10);
    
    grid.addPiece(b, new Point(2,3));
    
    System.out.println(grid.pieceFits(w, new Point(2,2)));
    System.out.println(grid.pieceFits(w, new Point(2,3)));
    
    grid.addPiece(w, new Point(2,2));
    
    System.out.println(grid.pieceFits(w, new Point(2,2)));
    System.out.println(grid.pieceFits(w, new Point(7,7)));
    System.out.println(grid.pieceFits(w, new Point(3,4)));
    
    grid.setTrap(3,3);
    
    System.out.println(grid);
    
    
    System.out.println(Arrays.toString(grid.getAdjacent(3,3)));
    
    System.out.println(grid.getOrigShade(3,3));
    
    System.out.println(grid.getVisShade(3,3));
    
    for (int i = 0; i<grid.evaluate().length; i++) {
        
        System.out.println(Arrays.toString(grid.evaluate()[i]));
        
    }
    
    System.out.println(grid.matches(grid.evaluate()));
    
    GameGrid grid2 = grid.clone();
    
    grid2.removePiece(new PieceNode<Piece>(b, new Point(2,3)));
    
    for (int i = 0; i<grid2.evaluate().length; i++) {
        
        System.out.println(Arrays.toString(grid2.evaluate()[i]));
        
    }
    
    System.out.println(grid2.matches(grid.evaluate()));
    
    System.out.println(grid);
    System.out.println(grid2);
    
    System.out.println(grid.strictAdjEmpty(5,5));
    System.out.println(grid.looseAdjEmpty(8,8));
    System.out.println(grid.looseAdjEmpty(9,12));
    
        
  }
  
}