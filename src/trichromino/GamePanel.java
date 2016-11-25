/**
 * Panel featuring a visual representation of the current game objects (grid and pieces).
 * 
 * @author Angela Wu (primary creator)
 * @author Lisa Li 
 * @version %I%, %G%
 * */

package trichromino;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
  
  //instance and class variables
  private Trichromino game;
  private PieceNode<Piece> currentPiece;
  
  private final int OFFSET_FACTOR = 13;
  private final int SCROLL_SPEED = 15;
  
  private final int WHITE = 1;
  private final int BLACK = 3;
  private final int GRAY = 5;
  
  private final int TRAP = 9;
  private final int T_BLACK = 10;
  private final int T_WHITE = 12;
  
  private final Color BACKGROUND = new Color(190,190,190);
  private final Color OFF_WHITE = new Color (250,250,250);
  private final Color SELECTED_WHITE = new Color(200,200,200);
  private final Color MID_GRAY = new Color(120,120,120);
  private final Color TRAP_BLUE = new Color(155,166,217);
  private final Color OFF_BLACK = new Color(50,50,50);
  private final Color SELECTED_BLACK = new Color(70,70,70);
  
  private static final long serialVersionUID = 1;
  
  /**
   * Constructor creates a panel around the given Trichromino object.
   * 
   * @param trigame the current game this panel will represent
   */
  public GamePanel(Trichromino trigame) { 
    
    game = trigame;
    currentPiece = trigame.getCurrentNode();
    setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(415,500));
    
    add(gameDisplay());
    add(pieceDisplay());
    
  }
  
  /**
   * Generates a JPanel to display the current state of the grid in this game.
   * 
   * @return a JPanel containing a representation of the current state of the grid in this game.
   */
  private JPanel gameDisplay() {
    
    JPanel display = new JPanel(new GridLayout(game.getHeight(),game.getWidth()));
    display.setPreferredSize(new Dimension(415,415));
    display.setMaximumSize(new Dimension(415,415));
    
    // Every space in the grid is represented as a JLabel.  JLabels are placed together
    // in the GridLayout to ultimately generate a grid.
    for (int i = 0; i < game.getHeight(); i++) {
      for (int j = 0; j < game.getWidth(); j++) {
        
        JLabel square = new JLabel();
        square.setOpaque(true);
        square.setBorder(BorderFactory.createLineBorder(Color.black));
        
        int shade; 
        
        if (!game.isEmpty()) {
          // if the piece is not placed, then the piece is shown in the given position.
          if (currentPiece.isPlaced()) shade = game.getCurrentGame().evaluate()[i][j];
          else shade = game.getCurrentGame().showPiece(currentPiece.getObj(),currentPiece.getFloatPos())[i][j];
        }
        else shade = 0;
        
        switch (shade%OFFSET_FACTOR) {
          
          case 0 : 
            square.setBackground(BACKGROUND);
            break;
            
          case WHITE :
            if (shade > OFFSET_FACTOR) square.setBorder(BorderFactory.createRaisedBevelBorder());
            square.setBackground(OFF_WHITE);
            break;
            
          case BLACK :
            if (shade > OFFSET_FACTOR) square.setBorder(BorderFactory.createRaisedBevelBorder());
            square.setBackground(OFF_BLACK);
            break;
            
          case GRAY :
            if (shade>OFFSET_FACTOR) square.setBorder(BorderFactory.createRaisedBevelBorder());
            square.setBackground(MID_GRAY);
            break;
            
          case TRAP :
            square.setBackground(TRAP_BLUE);
            break;
            
          case T_BLACK :
            if (shade > OFFSET_FACTOR) square.setBorder(BorderFactory.createRaisedBevelBorder());
            square.setBackground(OFF_BLACK);
            break;
            
          case T_WHITE : 
            if (shade>OFFSET_FACTOR) square.setBorder(BorderFactory.createRaisedBevelBorder());
            square.setBackground(OFF_WHITE);
            break;
            
          default :
            if (shade>OFFSET_FACTOR) square.setBorder(BorderFactory.createRaisedBevelBorder());
            square.setBackground(new Color(146,88,173,125));
            break;
            
        }
        
        display.add(square);
        
      }
    }
    
    return display;
    
  }
  
  /**
   * Creates a JScrollPane holding JPanel representations of each of the pieces
   * in the game.
   * 
   * @return a JScrollPane representation of the pieces in the game.
   */
  private JScrollPane pieceDisplay() {
    
    JPanel piecePanel = new JPanel();
    if (!game.isEmpty()) {
      for (PieceNode<Piece> node : game.getPieces()) {
        piecePanel.add(singlePiece(node));
      }
    }
    
    JScrollPane pieces = new JScrollPane(piecePanel);
    
    JScrollBar vertical = pieces.getVerticalScrollBar();
    vertical.setUnitIncrement(SCROLL_SPEED);
    JScrollBar horizontal = pieces.getHorizontalScrollBar();
    horizontal.setUnitIncrement(SCROLL_SPEED);
    
    InputMap vertMap = vertical.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    vertMap.put(KeyStroke.getKeyStroke("DOWN"),"positiveUnitIncrement");
    vertMap.put(KeyStroke.getKeyStroke("UP"),"negativeUnitIncrement");
    
    InputMap horMap = horizontal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    horMap.put(KeyStroke.getKeyStroke("RIGHT"),"positiveUnitIncrement");
    horMap.put(KeyStroke.getKeyStroke("LEFT"),"negativeUnitIncrement");
    
    return pieces;
    
  }
  
  /**
   * Creates a JPanel representation of a piece.
   * 
   * @returns a JPanel representation of a piece.
   */
  private JPanel singlePiece(PieceNode<Piece> piece) {
    
    Piece p = piece.getObj();
    
    JPanel singPiece = new JPanel(new GridLayout(p.getHeight(),p.getWidth()));
    singPiece.setPreferredSize(new Dimension((p.getWidth()*20),(p.getHeight()*20)));
    
    for (int i = 0; i < p.getHeight(); i++) {
      for (int j = 0; j < p.getWidth(); j++) {
        
        JLabel square = new JLabel();
        square.setOpaque(true);
        int shade = p.getMatrix()[i][j];
        
        switch (shade) {
            
          case WHITE :
            square.setBackground(OFF_WHITE);
            if (piece.equals(currentPiece)) {
              square.setBorder(BorderFactory.createRaisedBevelBorder());
              square.setBackground(SELECTED_WHITE);
            }
            else if (piece.isPlaced()) square.setBorder(BorderFactory.createLineBorder(OFF_BLACK));
            else square.setBorder(BorderFactory.createLineBorder(Color.gray));
            break;
            
          case BLACK :
            square.setBackground(OFF_BLACK);
            if (piece.equals(currentPiece)) {
              square.setBorder(BorderFactory.createRaisedBevelBorder());
              square.setBackground(SELECTED_BLACK);
            }
            else if (piece.isPlaced()) square.setBorder(BorderFactory.createLineBorder(OFF_WHITE));
            else square.setBorder(BorderFactory.createLineBorder(Color.black));
            break;
            
        }
        
        singPiece.add(square);
        
      }
    }
    
    return singPiece;
    
  }
}