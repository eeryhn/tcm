/**
 * A piece is defined on a matrix with 0 representing unoccupied squares, 1
 * representing white squares, and 3 representing black squares.
 * Pieces start as an empty matrix of a preset size where squares cannot be
 * added outside the bounds of this matrix.
 * 
 * Pieces can be cropped.  However, a cropped piece cannot be changed.
 * 
 * @author Angela Wu (primary creator)
 * @author Lisa Li
 * @version %I%, %G%
 * */

package trichromino;

import java.io.Serializable;

public class Piece implements Serializable {
    
  private int[][] matrix;
  private int id, numSquares;
  
  private boolean isCropped;
  
  private final int MAX_WIDTH, MAX_HEIGHT; 
  private final int BLACK = 3;
  private final int WHITE = 1;
  private final int EMPTY = 0;

  private static final long serialVersionUID = 1;
  
  
  /**
   * Constructor takes in the maximum dimensions of this piece as well
   * as an integer defining the colour of squares in this piece.
   * 
   * @param maxWidth the maximum number of columns in this piece
   * @param maxHeight the maximum number of rows in this piece
   * @param shade the integer corresponding to the shade of this piece.
   * @throws IllegalArgumentException If the value of shade is not
   * either black or white.
   */
  public Piece (int maxWidth, int maxHeight, int shade) {
    
    if (maxWidth<=0 || maxHeight<=0 || (shade!=BLACK && shade!=WHITE)) 
      throw new IllegalArgumentException("Piece: invalid argument in constructor");
    
    id = shade;
    numSquares = 0;
    MAX_WIDTH = maxWidth;
    MAX_HEIGHT = maxHeight;
    matrix = new int[MAX_HEIGHT][MAX_WIDTH];
    
    isCropped = false;
    
  }
  
  /**
   * Determines whether or not this piece matrix is empty.
   * 
   * @return true if the piece matrix is emtpy.
   */
  public boolean isEmpty() {
    
    return numSquares==0;
    
  }
  
  /**
   * Returns the array definition of this piece.
   * 
   * @return an int[][] corresponding to this piece.
   */
  public int[][] getMatrix() {
    
    return matrix;
    
  }
  
  /**
   * Returns the maximum height of this piece.
   * 
   * @return the maximum height of this piece.
   */
  public int getWidth() {
    
    return MAX_WIDTH;
    
  }
  
  /**
   * Returns the maximum width of this piece.
   * 
   * @return the maximum width of this piece.
   */
  public int getHeight() {
    
    return MAX_HEIGHT;
    
  }
  
  /**
   * Returns the integer corresponding to the colour of this piece.
   * 
   * @return the integer corresponding to the colour of this piece.
   */
  public int getShade() {
    
    return id; 
    
  }
  
  /**
   * Adds a square at the specified location in the matrix.
   * 
   * @param n the target row
   * @param m the target column
   * @throws IndexOutOfBoundsException If the specified location is outside the matrix.
   * @throws IllegalStateException If the piece has already been cropped.
   */
  public void addSquare(int n, int m) {
    
    if (isCropped) throw new IllegalStateException("addSquare: Squares cannot be added to a cropped piece");
    
    try {
      matrix[n][m] = id;
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("addSquare: n = " + n + "; m = " +m);
    }
    
    numSquares++;
    
  }
  
  /**
   * Adds the square from the specified location in the matrix.
   * 
   * @param n the target row
   * @param m the target column
   * @throws IllegalStateException If the piece has already been cropped.
   */
  
  public void removeSquare(int n, int m) {
    
    if (isCropped) throw new IllegalStateException("removeSquare: Squares cannot be removed from a cropped piece");
      
    matrix[n][m] = EMPTY;
    
  }
  
  /**
   * Determines whether or not the specified location in the matrix is occupied.
   * 
   * @param n the target row
   * @param m the target column
   * @return true if the specified location is occupied.
   * @throws IndexOutOfBoundsException If the specified location is outside the matrix.
   */
  private boolean isSquare(int n, int m) {
    
    try {
      return matrix[n][m]==id;
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("isSquare: n = " + n + "; m = " +m);
    }
    
  }
  
  /**
   * Determines whether or not the n-th row is empty.
   * 
   * @param n the target row
   * @return true if the specified row is unoccupied.
   */
  private boolean isEmptyRow(int n) {
    
    for (int i = 0; i<MAX_HEIGHT ; i++) {
      if (matrix[n][i]>0) return false;
    } 
    return true;
    
  }
  
  /**
   * Determines whether or not the m-th row is empty.
   * 
   * @param m the target column
   * @return true if the specified row is unoccupied.
   */
  private boolean isEmptyCol(int m) {
    
    for (int i = 0; i<MAX_HEIGHT ; i++) {
      if (matrix[i][m]>0) return false;
    } 
    return true;
    
  }
  
  /**
   * Returns the row in which the top-most square in this piece occurs.
   * Helper method for crop.
   * 
   * @return The integer corresponding to the top-most square in this piece.
   * @throws IllegalStateException if the Piece is empty.
   */
  private int minRow() {
    
    if (isEmpty()) throw new IllegalStateException("minRow: Piece matrix is unoccupied");
    
    int min = 0;
    while (isEmptyRow(min)) {
      
      min++;
      
    }
    
    return min;
    
  }
  
  /**
   * Returns the column in which the left-most square in this piece occurs.
   * Helper method for crop.
   * 
   * @return The integer corresponding to the left-most square in this piece.
   * @throws IllegalStateException if the Piece is empty.
   */
  private int minCol() {
    
    if (isEmpty()) throw new IllegalStateException("minCol: Piece matrix is unoccupied");
    
    int min = 0;
    while (isEmptyCol(min)) {
      
      min++;
      
    }
    
    return min;
    
  }
  
  /**
   * Returns the row in which the bottom-most square in this piece occurs. 
   * Helper method for crop.
   * 
   * @return The integer corresponding to the bottom-most square in this piece.
   * @throws IllegalStateException if the Piece is empty.
   */
  private int maxRow() {
    
    if (isEmpty()) throw new IllegalStateException("maxRow: Piece matrix is unoccupied");
    
    int max = MAX_HEIGHT-1;
    while (isEmptyRow(max)) {
      
      max--;
      
    }
    
    return max;
    
  }
  
  /**
   * Returns the column in which the right-most square in this piece occurs. 
   * Helper method for crop.
   * 
   * @return The integer corresponding to the right-most square in this piece.
   * @throws IllegalStateException if the Piece is empty.
   */
  private int maxCol() {
    
    if (isEmpty()) throw new IllegalStateException("minCol: Piece matrix is unoccupied");
    
    int max = MAX_WIDTH-1;
    while (isEmptyCol(max)) {
      
      max--;
      
    }
    
    return max;
    
  }
  
  /**
   * Returns a cropped version of this piece in which there are no empty rows
   * beyond the top/bottom/left/right-most square.
   * 
   * @return Copy of this piece without bordering whitespace.
   * @throws IllegalStateException if the piece is empty.
   */
  public Piece crop() {
    
    if (isEmpty()) throw new IllegalStateException("crop: Piece is empty");
    
    Piece cropped = new Piece(maxCol()-minCol()+1,maxRow()-minRow()+1,id);
    for (int i = minRow(); i<=maxRow(); i++) {
      for (int j = minCol(); j<=maxCol();j++) {
        
        if (isSquare(i,j)) cropped.addSquare(i-minRow(),j-minCol());
        
      }     
    }
    
    cropped.isCropped = true;
    
    return cropped;
    
  }
  
  /**
   * Returns true if the two pieces consist of identical matrices.
   * 
   * @return true if the two pieces are identical.
   */
  public boolean equals(Piece p) {
    
    if (this.MAX_WIDTH != p.MAX_WIDTH || this.MAX_HEIGHT!=p.MAX_HEIGHT) return false;
    
    for (int i = 0 ; i<MAX_HEIGHT ; i++) {
      for (int j = 0; j<MAX_WIDTH ; j++) {
        if (this.matrix[i][j] != p.matrix[i][j]) return false;
      }
    }
    
    return true;
    
  }
  
  
  /**
   * Returns a string representation of this piece, using a
   * space as a delimiter.
   * 
   * @return A string representation of this piece.
   */
  public String toString() {
    
    String s = "";
    for (int i = 0; i < MAX_HEIGHT ; i++) {
      for (int j = 0; j < MAX_WIDTH ; j++) {
        
        s += matrix[i][j] + " " ;
        
      }
      
      s += "\n";
        
    }
    return s;
    
  }
  
  /**
   * Preliminary testing done on Piece
   */
  public static void main(String[]args) {
    
    Piece p = new Piece(5,5,3);
    p.addSquare(3,3);
    p.addSquare(3,2);
    p.addSquare(2,3);
    p.addSquare(1,3);
    System.out.println(p);
    System.out.println(p.minRow());
    System.out.println(p.maxRow());
    System.out.println(p.minCol());
    System.out.println(p.maxCol());
    Piece pcrop = p.crop();
    System.out.println(pcrop);
    System.out.println(pcrop.numSquares);
    
  }
  
}