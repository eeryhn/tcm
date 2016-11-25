/**
 * Simple panel combining a GamePanel and a SidePanel to represent a
 * game of Trichromino.
 * 
 * @author Angela Wu 
 * @version %I%, %G%
 * */

package trichromino;

import javax.swing.*;
import java.awt.*;

public class TrichrominoPanel extends JPanel {
  
  private final int SPACING = 5;
  private JPanel gamePanel, sidePanel;
  
  private static final long serialVersionUID = 1;
  
  /**
   * Constructor combines a GamePanel and a SidePanel using
   * a BorderLayout.
   * 
   * @param game the game around which this panel will be built.
   */
  public TrichrominoPanel(Trichromino game) { 
    
    setLayout(new BorderLayout(SPACING,SPACING));
    
    gamePanel = new GamePanel(game);
    sidePanel = new SidePanel(game,gamePanel);
    
    add(gamePanel, BorderLayout.CENTER);
    add(sidePanel, BorderLayout.EAST);
    
  }
  
}