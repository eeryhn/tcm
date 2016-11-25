/**
 * For our purposes, PieceNode will connect GameGrids to Pieces in Trichromino.
 * In general, PieceNode stores a location and a isPlaced boolean for a given 
 * object moving around in another object/board (0,0 being the top-left corner).
 * <p>
 * Pieces that have been placed cannot be moved.  Pieces that can be moved are
 * considered to be "floating" around in the containing object/board.
 * 
 * @author Lisa Li (primary creator)
 * @author Angela Wu 
 * @version %I%, %G%
 * */

package trichromino;

import java.awt.Point;
import java.io.Serializable;

public class PieceNode<T> implements Serializable, Cloneable{
  
  private T obj; // stored object
  private Point floatLoc; // the location at which the object is "floating"
  
  private boolean isPlaced;
  
  private final int NULL_PIECE = -1;
  private final int START_CORNER = 0; // all pieces start in the top-left corner.
  
  private static final long serialVersionUID = 1;
  
  /**
   * Default constructor sets the stored object and location to null/a predefined
   * null pointer.
   */
  public PieceNode() {
    
    obj = null;
    
    isPlaced = false;
    
    floatLoc = new Point(NULL_PIECE, NULL_PIECE);
    
  }
  
  /**
   * Constructor takes the object being stored in the "board".
   * Objects start off floating in the top-left corner of the "board".
   * 
   * @param object T object being stored.
   */
  public PieceNode(T object) { 
    
    obj = object;
    
    isPlaced = false;
    
    floatLoc = new Point(START_CORNER,START_CORNER);
    
  }
  
  /**
   * Constructor takes a T and a point, placing the T
   * at the point location on the "board".
   * 
   * @param object the T object being stored.
   * @param p the point at which object is being stored.
   */
  public PieceNode(T object, Point p) {
    
    obj = object;
    
    isPlaced = false;
    
    floatLoc = p;
    
  }
  
  /**
   * Returns a clone of this node.
   * 
   * @return A clone of this node.
   */
  public PieceNode<T> clone() {
    
    PieceNode<T> clone = new PieceNode<T>(obj);
    clone.isPlaced = this.isPlaced;
    
    clone.floatLoc = (Point) this.floatLoc.clone();
    
    return clone;
    
  }
  
  /**
   * Returns the object stored in this node.
   * 
   * @return the T stored in this node
   */
  public T getObj() {
    
    return obj;
    
  }
  
  /**
   * Moves the object location left.
   */
  public void left() {
    
    if (isPlaced) throw new IllegalStateException("left: cannot be called on a placed object");
    
    floatLoc.x--;
    
  }
  
  /**
   * Moves the object location right.
   */
  public void right() {
    
    if (isPlaced) throw new IllegalStateException("right: cannot be called on a placed object");
    
    floatLoc.x++;
    
  } 
  
  /**
   * Moves the object location up.
   */
  public void up() {
    
    if (isPlaced) throw new IllegalStateException("up: cannot be called on a placed object");
    
    floatLoc.y--;
    
  }
  
  /**
   * Moves the object location down.
   */
  public void down() {
    
    if (isPlaced) throw new IllegalStateException("down: cannot be called on a placed object");
    
    floatLoc.y++;
    
  }
  
  /**
   * Marks the object as placed.
   */
  public void place() {
    
    isPlaced = true;
    
  }
  
  /**
   * Sets the object to float at the given location.
   * 
   * @param n the target row
   * @param m the target column
   */
  public void hover(int n, int m) {
    
    floatLoc.setLocation(m,n);
    
  }
  
  /**
   * Sets the object to float at the given location.
   * 
   * @param p the target location
   */
  public void hover(Point p) {
    
    floatLoc.setLocation(p);
    
  }
  
  /**
   * Moves the object to the given location and then places it.
   * 
   * @param n the target row
   * @param m the target column
   */
  public void place(int n, int m) {
    
    hover(n,m);
    isPlaced = true;
    
  }
  
  /**
   * Returns the point at which the stored object is floating.
   * 
   * @return The point at which the stored object is floating.
   */
  public Point getFloatPos() {
    
    return floatLoc;
    
  }
  
  /**
   * Determines whether or not the stored object is placed.
   * 
   * @return true of the stored object is placed.
   */
  public boolean isPlaced() {
    
    return isPlaced;
    
  }
  
  /**
   * Sets the isPlaced boolean to false (the stored object
   * returns to floating in the container)
   */
  public void displace() {
    
    isPlaced = false;
    
  }
  
  /**
   * Displaces the stored object and resets the location at which
   * it is hovering to the top-left corner.
   */
  public void reset() {
    
    floatLoc.setLocation(START_CORNER,START_CORNER);
    if (this.isPlaced) this.isPlaced = false;
    
  }
  
  /**
   * Determines whether or not this node and node are equal.
   * PieceNodes are considered equal if they are both floating or both placed in the same
   * position containing equal objects.
   * 
   * @param node the node to which this is being compared
   * @return true if the this and node are equal.
   */
  public boolean equals(PieceNode<T> node) {
    
    return (this.obj.equals(node.obj) && this.floatLoc.equals(node.floatLoc) && this.isPlaced == node.isPlaced);
    
  }
  
  /**
   * Returns a string representation of this node denoting
   * its position and whether or not it has been placed.
   * 
   * @return A string representation of this node.
   */
  public String toString() {
    
    return obj.toString() + isPlaced() + "\n" + floatLoc;
    
  }
  
  /**
   * Rudimentary testing done with a basic piece.
   */
  public static void main (String[] args) {
    
    Piece p = new Piece(5,5,3);
    p.addSquare(3,3);
    p.addSquare(3,2);
    p.addSquare(2,3);
    p.addSquare(1,3);
    
    p = p.crop();
    
    PieceNode<Piece> testNode = new PieceNode<Piece>(p);
    testNode.place(5,5);
    System.out.println(testNode);
    testNode.reset();
    System.out.println(testNode);
    
    
  }
  
}