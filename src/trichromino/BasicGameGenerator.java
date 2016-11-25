/**
 * GameGenerator creates the grids used in generating new
 * Trichromino games.
 * 
 * @author Angela Wu (primary creator)
 * @author Lisa Li
 * @version %I%, %G%
 * */

package trichromino;

import java.awt.Point;
import java.util.Random;
import java.util.LinkedList;

public class BasicGameGenerator {
  
  private GameGrid grid;
  private int color;
  private int gridWidth;
  private int gridHeight;
  
  private int minRow;
  private int minCol;
  
  private Random random;
  
  // color identification integers
  private final int EMPTY = 0;
  private final int WHITE = 1;
  private final int BLACK = 3;
  
  // min piece size (subject to change)
  public final int MIN_SIZE = 3;
  
  // minimum/maximum traps permitted on a board.
  private final int MIN_NUM_TRAPS = 6;
  private final int MAX_NUM_TRAPS = 10;
  
  /**
   * Constructor takes in a GameGrid and initialises
   * instance variables.
   * 
   * @param g GameGrid in which Generator operations will be performed.
   */
  public BasicGameGenerator(GameGrid g) {
    
    grid = g;
    gridWidth = g.getWidth();
    gridHeight = g.getHeight();
    color = WHITE;
    
    minRow = minCol = 0;
    
    random = new Random();
    
  }
  
  /**
   * Generates and places a set of purely polyomino pieces onto
   * the grid.  Makes no changes to the grid itself.
   * 
   * @return The generated grid.
   */
  public GameGrid easyGrid() {
    
    while (!grid.isComplete()) {
      
      int numSquares;
      if (grid.numEmpty()/5 > 3) numSquares = grid.numEmpty()/6;
      else numSquares = 3;
      
      createPolyomino(getRoot(), numSquares);
      
    }
    
    return grid;
    
  }
  
  /**
   * Generates and places a set of purely polyomino pieces onto
   * the grid.  Additionally adds a number of traps to the grid.
   * 
   * @return The generated grid.
   */
  public GameGrid sEasyGrid() {
    
    int numTraps = random.nextInt(MAX_NUM_TRAPS-MIN_NUM_TRAPS) + MIN_NUM_TRAPS;
    
    while (numTraps > 0) {
      
      Point p = new Point(random.nextInt(gridWidth),random.nextInt(gridHeight));
      
      while (grid.hasTrap(p.y,p.x)) {
        p = new Point(random.nextInt(gridWidth),random.nextInt(gridHeight));
      }
      
      grid.setTrap(p.y,p.x);
      numTraps--;
      
    }
    
    while(!grid.isComplete()) {
      
      int numSquares;
      if (grid.numEmpty()/5 > 3) numSquares = grid.numEmpty()/6;
      else numSquares = 3;
      
      createPolyomino(getRoot(), numSquares);
      
    }
    
    return grid;
    
  }
  
  /**
   * Generates and places a random polyomino of size numSquares through a BFS starting at
   * root.
   * 
   * @param root the point at which the BFS starts
   * @param numSquares the number of squares making up the produced polyomino.
   */
  private void createPolyomino(Point root, int numSquares) {
    
    Piece p = new Piece(gridHeight,gridWidth,color);
    
    //Creates a list of points in which squares will be added.
    LinkedList<Point> additions = bfs(root,numSquares);
    
    // Does nothing if the returned bfs is smaller than three squares
    // (consider this a quick fix for the time being.  Seems to denote
    // a problem in the bfs algorithm.)
    if (additions.size()>=MIN_SIZE) {
      
      //Creates and crops piece.
      for (Point pt : additions) {
        p.addSquare(pt.y,pt.x);
      }
      
      p = p.crop();
      
      grid.addPiece(p,new Point(minCol,minRow));
      
      // Switches the current colour value in order
      // to create pieces of alternating colour.
      switch (color) {
        case WHITE : 
          color = BLACK;
          break;
        case BLACK : 
          color = WHITE;
          break;
      }
      
      minRow=minCol=0;
      
    }
  }
  
  
  /**
   * Searches for a point in the grid at which there are two or
   * more paths to follow.
   * 
   * @return A point at which there are two or more paths to follow.
   */
  private Point getRoot() {
    
    int n, m, vertCount, horzCount;
    
    vertCount = horzCount = 0;
    
    // Generates a random set of int for a starting point.
    n = random.nextInt(gridHeight);
    m = random.nextInt(gridWidth);
    
    //if the starting point does not have two or more paths to follow,
    //traces through the remainder of the grid searching for a point that does.
    while((!grid.isEmptySquare(n,m) || !hasPiece(n,m)) && vertCount <= gridHeight) {
      while((!grid.isEmptySquare(n,m) || !hasPiece(n,m)) && horzCount <= gridWidth) {
        m = (m+1)%gridWidth;
        horzCount++;
      }
      n = (n+1)%gridHeight;
      vertCount++;
    }
    return new Point(m,n);
    
  }
  
  
  /**
   * Determines whether or not the given point has two or more paths
   * emerging from it.
   * Helper method used in getRoot() to ascertain that a given piece
   * of three or more squares can be formed.
   * 
   * @return true if there are two or more paths emerging from the
   *              given point.
   */
  private boolean hasPiece(int n, int m) {
    
    if (grid.getAdjacent(n,m)[EMPTY] > 1) return true;
    
    return false;
    
  }
  
  /**
   * Modified version of BFS search which has a factor of randomness, involving
   * decisions made on whether or not a particular path will be traversed.
   * 
   * @param root the starting point of the search
   * @param size the preferred size of the retured list.
   * @return A list of points traveled by this BFS with a length of at most size.
   */
  private LinkedList<Point> bfs(Point root, int size) {
    
    // tracks the minimum row/column occupied by a square.
    minRow = root.y;
    minCol = root.x;
    
    LinkedList<Point> visited = new LinkedList<Point>();
    visited.add(root);
    
    LinkedList<Point> queue = new LinkedList<Point>();
    queue.add(root);
    
    // if the size of the queue is less than zero then there are no farther
    // extensions for the piece and the list is returned.  Otherwise continues
    // to add to the visited list until it reaches a length of size.
    while (queue.size() > 0 && visited.size() < size) {
      
      Point currentPoint = queue.remove();
      
      LinkedList<Point> adjacent = emptyAdjacent(currentPoint,visited);
      
      // if at a particular point there are more than two possible
      // directions of travel, picks at least two of them to append
      // to the queue.
      if (adjacent.size() > 2) {
        
        int numPoints = random.nextInt(adjacent.size()-1) + 2;
        
        while (size-(visited.size()) < numPoints) numPoints--;
        
        while (numPoints > 0) {
          
          int nextIndex = random.nextInt(adjacent.size());
          Point nextPoint = adjacent.remove(nextIndex);
          
          if (nextPoint.y < minRow) minRow = nextPoint.y;
          if (nextPoint.x < minCol) minCol = nextPoint.x;
          
          queue.add(nextPoint);
          visited.add(nextPoint);
          numPoints--;
          
        }
        
      } else {
        if (size-(visited.size()) >= adjacent.size()) {
          
          //while loop ascertains that the adjacent squares are added in a random order.
          while (adjacent.size() > 0) {
            
            Point next = adjacent.remove(random.nextInt(adjacent.size()));
            
            if (next.y < minRow) minRow = next.y;
            if (next.x < minCol) minCol = next.x;
            
            queue.add(next);
            visited.add(next);
            
          }
          
        } else {
          while (size-(visited.size()) > 0) {
            
            Point next = adjacent.remove(random.nextInt(adjacent.size()));
            
            if (next.y < minRow) minRow = next.y;
            if (next.x < minCol) minCol = next.x;
            
            queue.add(next);
            visited.add(next);
            
          }
        }
      }
      
    }
    
    return visited;
    
  }
  
  /**
   * Returns a list of unvisited points from point according to visited list.
   * 
   * @param point the target point
   * @param visited a list of visited points
   * @return A list of points adjacent to point that have not been traversed in visited.
   */
  private LinkedList<Point> emptyAdjacent(Point point, LinkedList<Point> visited) {
    
    LinkedList<Point> unvisited = grid.strictAdjEmpty(point.y,point.x);
    
    for (Point p : visited) {
      
      if (unvisited.contains(p)) unvisited.remove(p);
      
    }
    
    return unvisited;
    
  }
  
  
  /**
   * Rudimentary testing done through main.
   */
  public static void main(String[]args) {
    
    GameGrid grid = new GameGrid(10,10);
    BasicGameGenerator test = new BasicGameGenerator(grid);
    test.createPolyomino(new Point(5,5),20);
    
    System.out.println(test.grid);
    
    test.easyGrid();
    System.out.println(grid);
    
    grid = new GameGrid(10,10);
    BasicGameGenerator test2 = new BasicGameGenerator(grid);
    test2.sEasyGrid();
    System.out.println(grid);
    
  }
  
}
