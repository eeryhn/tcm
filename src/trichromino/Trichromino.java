/**
 * A Trichromino game consists of a GameGrid and a LinkedList of PieceNodes<Piece>
 * as well as a solutions, which is stored separately as an integer array.
 * Trichromino games are computer generated or loaded from file.  Games created
 * from the default constructor are empty.
 * <p>
 * An integer stack is kept to track a user's interaction with a game in order to
 * allow for undo's and redo's.
 * 
 * @author Lisa Li (primary creator)
 * @author Angela Wu
 * @version %I%, %G%
 * */

package trichromino;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Arrays;

public class Trichromino implements Serializable {
  
  private GameGrid currentGrid;
  private int[][] solution;
  private int currentIndex;
  private PieceNode<Piece> currentPiece;
  private LinkedList<PieceNode<Piece>> pieces;
  
  private Stack<Integer> undoable;
  private Stack<Integer> redoable;
  
  private final int MAX_UNDO = 30;
  
  private final int EASY_SIZE = 10;
  
  private final int NOT_FOUND = -1;
  
  // Modulo in which user input is registered
  private final int MODULO = 9;
  
  // Integers corresponding to user input.
  private final int LEFT = 0;
  private final int RIGHT = 9;
  private final int UP = 1;
  private final int DOWN = 8;
  private final int PREV = 2;
  private final int NEXT = 7;
  private final int PLACE = 3;
  private final int DISPLACE = 6;
  private final int UNDO = 4;
  private final int REDO = 5;
  
  private static final long serialVersionUID = 1;
  
  /**
   * Default constructor creates an empty game (empty tracking stacks,
   * empty grid/array/piece list)
   */
  public Trichromino() { 
    
    undoable = new Stack<Integer>();
    redoable = new Stack<Integer>();
    
    currentGrid = new GameGrid(EASY_SIZE,EASY_SIZE);
    
    solution = new int[EASY_SIZE][EASY_SIZE];
    currentPiece = new PieceNode<Piece>();
    currentIndex = NOT_FOUND;
    pieces = new LinkedList<PieceNode<Piece>>();
    
  }
  
  /**
   * Static method can be called in order to load a Trichromino game
   * from a file (utilises ObjectInputStream).
   * 
   * @param fileName the name of the file from which the game will be created.
   * @return The Trichromino object created from the file.
   * @throws Exception if a problem is encountered in loading the game from file.
   */
  public static Trichromino fromFile(String fileName) throws Exception {
    
    try {
      
      FileInputStream fileIn = new FileInputStream(fileName + ".tcm");
      ObjectInputStream objIn = new ObjectInputStream(fileIn);
      Trichromino game = null;
      game = (Trichromino) objIn.readObject();
      objIn.close();
      fileIn.close();
      
      return game;
      
    } catch (Exception e) {     
      throw new Exception("Trichromino: " + e);      
    }
    
  }
  
  /**
   * Generates a Trichromino game in which there exist only polyomino pieces
   * on an empty board.
   */
  public void generateEasy() {
    
    currentGrid = new GameGrid(EASY_SIZE,EASY_SIZE);
    BasicGameGenerator generator = new BasicGameGenerator(currentGrid);
    currentGrid = generator.easyGrid();
    solution = currentGrid.evaluate();
    pieces = currentGrid.removeAll();
    currentIndex = 0;
    currentPiece = pieces.get(currentIndex);
    
  }
  
  /**
   * Generates a Trichromino game in which there exist only polyomino pieces
   * on a board with a pre-existing polyomino.
   */
  public void generateSEasy() {
    
    currentGrid = new GameGrid(EASY_SIZE,EASY_SIZE);
    BasicGameGenerator generator = new BasicGameGenerator(currentGrid);
    currentGrid = generator.sEasyGrid();
    solution = currentGrid.evaluate();
    pieces = currentGrid.removeAll();
    currentIndex = 0;
    currentPiece = pieces.get(currentIndex);
    
  }
  
  /**
   * Returns the width of the grid.
   * 
   * @return The width of the grid.
   */
  public int getWidth() {
    
    return currentGrid.getWidth();
    
  }
  
  /**
   * Returns the height of the grid.
   * 
   * @return The height of the grid.
   */
  public int getHeight() {
    
    return currentGrid.getHeight();
    
  }
  
  /**
   * Returns the currently selected piece.
   * 
   * @return The currently selected piece.
   */
  public PieceNode<Piece> getCurrentNode() {
    
    return currentPiece;
    
  }
  
  /**
   * Returns the solution to this game.
   * 
   * @return The solution to this grid.
   */
  public int[][] getSolution() {
    
    return solution;
    
  }
  
  /**
   * Returns the game grid.
   * 
   * @return The game grid.
   */
  public GameGrid getCurrentGame() {
    
    return currentGrid;
    
  }
  
  /**
   * Returns the array representation of the game grid.
   * 
   * @return The array representation of the game grid.
   */
  public int[][] getCurrentGrid() {
    
    return currentGrid.getGrid();
    
  }
  
  /**
   * Returns the list of pieces in this game.
   * 
   * @return The list of pieces in this game.
   */
  public LinkedList<PieceNode<Piece>> getPieces() {
    
    return pieces;
    
  }
  
  /**
   * Denotes whether or not this game is empty.
   * An empty game is denoted as a game with an invalid pointer (current index) 
   * to the list of pieces.
   * 
   * @return true if the game is empty.
   */
  public boolean isEmpty() {
    
    return currentIndex == NOT_FOUND;
    
  }
  
  /**
   * Denotes whether or not the game has been cleared (Grid matches solution).
   * 
   * @return true if the grid matches the solution.
   */
  public boolean isOver() {
    
    if (isEmpty()) return false;
    
    return currentGrid.matches(solution);
    
  }
  
  /**
   * Denotes whether or not the selected piece fits in the grid at its floating position.
   * 
   * @return true if the selected piece fits in the grid at its floating position.
   */
  public boolean validPlace() {
    
    if (isEmpty()) return false;
    
    return currentGrid.pieceFits(currentPiece.getObj(), currentPiece.getFloatPos());
    
  }
  
  /**
   * Determines whether or not the current piece can move up/right/left/down (does not
   * touch an edge in the direction it is trying to move).
   * 
   * @param cmd the integer corresponding to the desired move.
   * @return true if the piece can move in the denoted direction.
   * @throws IllegalArgumentException if cmd does not correspond to a move.
   */
  public boolean canMove(int cmd) {
    
    if (isEmpty()) return false;
    
    switch (cmd) {
      
      case LEFT : 
        if (currentPiece.getFloatPos().x-1 < 0) return false;
        return true;
        
      case RIGHT :
        if (currentPiece.getFloatPos().x + currentPiece.getObj().getWidth() + 1 > currentGrid.getWidth()) return false;
        return true;
        
      case UP :
        if (currentPiece.getFloatPos().y - 1 < 0) return false;
        return true;
        
      case DOWN :
        if (currentPiece.getFloatPos().y + currentPiece.getObj().getHeight()+1 > currentGrid.getHeight()) return false;
        return true;
        
      default :
        throw new IllegalArgumentException("canMove: Invalid command");
        
    }
    
  }
  
  /**
   * Moves the current piece in the denoted direction.
   * 
   * @param cmd the integer corresponding to the desired move.
   * @throws IllegalStateException if this game is empty.
   * @throws IllegalArgumentException if cmd does not correspond to a move.
   */
  public void move(int cmd) {
    
    if (isEmpty()) throw new IllegalStateException("move: Game is current empty");
    
    if (!currentPiece.isPlaced() && canMove(cmd)) {  
      appendUndo(cmd);
      
      switch (cmd) {
        
        case LEFT : 
          currentPiece.left();
          break;
          
        case RIGHT :
          currentPiece.right();
          break;
          
        case UP :
          currentPiece.up();
          break;
          
        case DOWN :
          currentPiece.down();
          break;
          
        default :
          throw new IllegalArgumentException("move: Invalid command");
          
      }
      
    }
  }
  
  /**
   * Places the selected piece in its location in the grid if the
   * piece is unplaced.
   * 
   * @throws IllegalStateException if this game is empty.
   */
  public void place() {
    
    if (isEmpty()) throw new IllegalStateException("place: Game is current empty");
    
    if (!currentPiece.isPlaced()) {
      
      appendUndo(PLACE);
      
      currentPiece.place();
      currentGrid.addPiece(currentPiece.getObj(),currentPiece.getFloatPos());
    }
    
  }
  
  
  /**
   * Displaces the selected piece from its location in the grid if
   * the piece is placed.
   * 
   * @throws IllegalStateException if this game is empty.
   */
  public void displace() {
    
    if (isEmpty()) throw new IllegalStateException("displace: Game is current empty");
    
    if (currentPiece.isPlaced()) {
      
      appendUndo(DISPLACE);
      
      currentGrid.removePiece(currentPiece);
      currentPiece.displace();
      
    }
    
  }

  /**
   * Sets the selected piece to the next piece in the piece list.
   * 
   * @throws IllegalStateException If this game is empty.
   */
  public void next() {
    
    if (isEmpty()) throw new IllegalStateException("next: Game is current empty");
    
    appendUndo(NEXT);
    
    currentIndex = (currentIndex+1)%pieces.size();
    currentPiece = pieces.get(currentIndex);
    
  }

  /**
   * Sets the selected piece to the previous piece in the piece list.
   * 
   * @throws IllegalStateException if this game is empty.
   */
  public void prev() {
    
    if (isEmpty()) throw new IllegalStateException("prev: Game is current empty");
    
    appendUndo(PREV);
    
    currentIndex = (currentIndex-1+pieces.size())%pieces.size();
    currentPiece = pieces.get(currentIndex);
    
  }
  
  /**
   * Appends step into the undoable stack and clears the redoable stack.
   * If the size of the stack exceeds MAX_UNDO, the bottom of the stack is
   * removed.
   * 
   * @param step the integer corresponding to the step being made.
   */
  private void appendUndo(int step) {
    
    undoable.push(step);
    redoable.removeAllElements();
    if (undoable.size() >= MAX_UNDO) {
      undoable.removeElementAt(0);
    }
    
  }
  
  /**
   * Denotes whether or not there is a move to be undone.
   * 
   * @return true if undoable contains one or more integers.
   */
  public boolean canUndo() {
    
    return (undoable.size() > 0);
    
  }
  
  /**
   * Denotes whether or not there is a move to be redone.
   * 
   * @return true if redoable contains one or more integers.
   */
  public boolean canRedo() {
    
    return (redoable.size() > 0);
    
  }
  
  /**
   * Undoes or redoes depending on cmd.
   * 
   * @param cmd integer corresponding either to undo or redo.
   */
  public void stepper(int cmd) {
    
    int step;
    
    switch (cmd) {
      
      case UNDO:
        if (canUndo()) {
        step = undoable.pop();
        redoable.push(step);
        stepperCommands(Math.abs(step-MODULO));
      }
        break;
        
      case REDO:
        if (canRedo()) {
        step = redoable.pop();
        undoable.push(step);
        stepperCommands(step);
      }
        break;
        
      default: {
        throw new IllegalArgumentException("stepper: " + cmd + " is not a valid command.");
      }
        
    }
    
  }
  
  /**
   * Makes the action corresponding to step.
   * Helper method used for undo and redo.
   * 
   * @param step the integer corresponding to the desired action.
   */
  private void stepperCommands(int step) {
    
      switch (step) {
        case LEFT: 
          currentPiece.left();
          break;
          
        case RIGHT:
          currentPiece.right();
          break;
          
        case UP:
          currentPiece.up();
          break;
          
        case DOWN:
          currentPiece.down();
          break;
          
        case PREV:
          currentIndex = (currentIndex - 1 + pieces.size())%pieces.size();
          currentPiece = pieces.get(currentIndex);
          break;
          
        case NEXT:
          currentIndex = (currentIndex + 1)%pieces.size();
          currentPiece = pieces.get(currentIndex);
          break;
          
        case PLACE: 
          currentGrid.addPiece(currentPiece.getObj(),currentPiece.getFloatPos());
          currentPiece.place();
          break;
          
        case DISPLACE:
          currentGrid.removePiece(currentPiece);
          currentPiece.displace();
          break;
          
        default: {
          throw new IllegalArgumentException("stepperCommands: " + step + 
                                             " is not a valid command.");
        }
          
      }
    }
  
  /**
   * Saves the game in fileName.tcm (utilising ObjectOutputStream)
   * 
   * @param fileName the file in which the game is to be saved.
   * @throws IOException if there is a problem with fileName.
   */
  public void save(String fileName) throws IOException {
    
    if (isEmpty()) throw new IllegalStateException("save: Game is current empty");
    
    try {
      
      FileOutputStream fileOut = new FileOutputStream(fileName + ".tcm");
      ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
      objOut.writeObject(this);
      objOut.close();
      fileOut.close();
      
    } catch (IOException e) {
      throw new IOException("save: " + e);
    }
    
  }
  
  /**
   * Clears the grid and resets the location of all pieces.
   * 
   * @throws IllegalStateException if the game is empty.
   */
  public void clear() {
    
    if (isEmpty()) throw new IllegalStateException("clear: Game is current empty");
    
    currentGrid.removeAll();
    for (PieceNode<Piece> piece : pieces) {
      
      if (piece.isPlaced()) piece.displace();
      
    }
    
    undoable.removeAllElements();
    redoable.removeAllElements();
    
  }
  
  /**
   * Returns a string representation of the grid, this game's solution, and
   * the pieces stored in pieces.
   * 
   * @return A string representation of this game.
   */
  public String toString() {
    
    String s = currentGrid.toString() + "\n" ;
    
    for (int i = 0; i < solution.length ; i++) {
      for (int j = 0; j < solution[0].length ; j++) {
        
        if (currentPiece.isPlaced()) s += currentGrid.evaluate()[i][j] + " ";
        
        else s += currentGrid.showPiece(currentPiece.getObj(),currentPiece.getFloatPos())[i][j] + " " ;
        
      }
      
      s += "\n";
        
    }
    
    s += "\n";
    
    for (int i = 0; i < solution.length ; i++) {
      for (int j = 0; j < solution[0].length ; j++) {
        
        s += solution[i][j] + " " ;
        
      }
      
      s += "\n";
        
    }
    
    s += "\n";
    
    for (PieceNode<Piece> node : pieces) {
      
      s += (node + "\n\n");
      
    }
    
    return s;
    
  }
  
  /**
   * Rudimentary testing done in Trichromino creates a .tcm file for GUI testing.
   */
  public  static void main(String[]args) {
    
    LinkedList<PieceNode<Piece>> list = new LinkedList<PieceNode<Piece>>();
    
    Piece b = new Piece(10,10,3);
    b.addSquare(3,3);
    b.addSquare(3,2);
    b.addSquare(2,3);
    b.addSquare(1,3);
    b = b.crop();
    System.out.println(b);
    
    PieceNode<Piece> pb1 = new PieceNode<Piece>(b);
    PieceNode<Piece> pb2 = new PieceNode<Piece>(b);
    pb1.place(3,2);
    pb2.place(4,3);
    list.add(pb1);
    list.add(pb2);
    
    Piece w = new Piece(10,10,1);
    w.addSquare(1,2);
    w.addSquare(1,3);
    w.addSquare(2,2);
    w.addSquare(3,2);
    w = w.crop();
    System.out.println(w);
    
    PieceNode<Piece> pw1 = new PieceNode<Piece>(w);
    PieceNode<Piece> pw2 = new PieceNode<Piece>(w);
    pw1.place(2,2);
    pw2.place(0,0);
    list.add(pw1);
    list.add(pw2);
    
    Piece b2 = new Piece(10,10,3);
    b2.addSquare(1,2);
    b2.addSquare(2,2);
    b2.addSquare(2,1);
    b2.addSquare(3,1);
    b2 = b2.crop();
    System.out.println(b2);
    
    PieceNode<Piece> pb3 = new PieceNode<Piece>(b2);
    pb3.place(0,1);
    list.add(pb3);
             
    
    Piece w2 = new Piece(10,10,1);
    w2.addSquare(0,0);
    w2.addSquare(1,0);
    w2.addSquare(2,0);
    w2.addSquare(3,0);
    w2 = w2.crop();
    System.out.println(w2);
    
    PieceNode<Piece> pw3 = new PieceNode<Piece>(w2);
    pw3.place(3,1);
    list.add(pw3);
    
    GameGrid grid = new GameGrid(7,5);
    
    grid.setTrap(3,3);
    grid.setTrap(2,0);
    grid.setTrap(6,4);
    
    for (PieceNode<Piece> node : list) {
      
      grid.addPiece(node.getObj(),node.getFloatPos());
      
    }
    
    System.out.println(grid);
    
    Trichromino gameTest = new Trichromino();
    gameTest.solution = grid.evaluate();
    gameTest.pieces = grid.removeAll();
    gameTest.currentGrid = grid;
    gameTest.currentIndex = 0;
    gameTest.currentPiece = gameTest.pieces.get(gameTest.currentIndex);
    System.out.println(gameTest);
    
    gameTest.place();
    gameTest.next();
    gameTest.next();
    gameTest.move(gameTest.RIGHT);
    System.out.println(gameTest);
    gameTest.move(gameTest.RIGHT);
    System.out.println(gameTest);
    
    try {
      gameTest.save("tri1");
    } catch (Exception e) {
      System.out.println("save fail");
    }
    
    try {
      Trichromino loadTest = fromFile("tri1");
      System.out.println(loadTest);
    } catch (Exception e) {
      System.out.println("load fail " + e );
    }
    
    for (int i = 0 ; i < gameTest.solution.length ; i++) {
      System.out.println(Arrays.toString(gameTest.getSolution()[i]));
    }
    
    Trichromino testGen = new Trichromino();
    testGen.generateEasy();
    System.out.println(testGen);
    
  }
  
  }