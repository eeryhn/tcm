/**
 * SidePanel contains all the buttons used to interact with the game.
 * 
 * @author Angela Wu (primary creator)
 * @author Lisa Li
 * @version %I%, %G%
 * */

package trichromino;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SidePanel extends JPanel {
  
  private JPanel sidePanel;
  private JPanel mainPanel;
  private Trichromino game;
  private int[][] solution;
  private JButton place, remove, up, down, left, right, prev, next, undo, redo, reset;
  
  private final Color BACKGROUND = new Color(190,190,190);
  private final Color OFF_WHITE = new Color (250,250,250);
  private final Color MID_GRAY = new Color(120,120,120);
  private final Color OFF_BLACK = new Color(50,50,50);
  
  private final int WHITE = 1;
  private final int BLACK = 3;
  private final int GRAY = 5;
  
  private final int LEFT = 0;
  private final int RIGHT = 9;
  private final int UP = 1;
  private final int DOWN = 8;
  private final int UNDO = 4;
  private final int REDO = 5;
  
  private static final long serialVersionUID = 1;
  
  /**
   * Constructor creates the SidePanel stacking multiple
   * JPanels containing buttons in a BoxLayout.
   * 
   * @param trigame the game with which this panel interacts
   * @param main the JPanel corresponding to the grid and pieces of the game.
   */
  public SidePanel(Trichromino trigame, JPanel main) {
    
    sidePanel = this;
    mainPanel = main;
    game = trigame;
    solution = game.getSolution();
    
    setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    setPreferredSize(new Dimension(200,450));
    
    ButtonListener listener = new ButtonListener();
    
    // All JButtons have keybindings assigned to them for
    // a more intuitive game experience.  Different buttons have
    // slightly different conditions under which they are disabled.
    place = new JButton("Place");
    if (!game.validPlace() || game.getCurrentNode().isPlaced() || game.isOver()) place.setEnabled(false);
    place.addActionListener(listener);
    place.registerKeyboardAction(listener, 
                                 KeyStroke.getKeyStroke("Z"), 
                                 JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    remove = new JButton("Remove");
    if (game.isEmpty() || !game.getCurrentNode().isPlaced() || game.isOver()) remove.setEnabled(false);
    remove.addActionListener(listener);
    remove.registerKeyboardAction(listener, 
                                  KeyStroke.getKeyStroke("Z"), 
                                  JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    up = new JButton("^");
    if (!game.canMove(UP) || game.getCurrentNode().isPlaced() || game.isOver()) up.setEnabled(false);
    up.addActionListener(listener);
    up.registerKeyboardAction(listener,
                              KeyStroke.getKeyStroke("E"), 
                              JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    down = new JButton("v");
    if (!game.canMove(DOWN) || game.getCurrentNode().isPlaced() || game.isOver()) down.setEnabled(false);
    down.addActionListener(listener);
    down.registerKeyboardAction(listener, 
                                KeyStroke.getKeyStroke("D"), 
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    left = new JButton("<");
    if (!game.canMove(LEFT) || game.getCurrentNode().isPlaced() || game.isOver()) left.setEnabled(false);
    left.addActionListener(listener);
    left.registerKeyboardAction(listener, 
                                KeyStroke.getKeyStroke("S"), 
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    right = new JButton(">");
    if (!game.canMove(RIGHT) || game.getCurrentNode().isPlaced() || game.isOver()) right.setEnabled(false);
    right.addActionListener(listener);
    right.registerKeyboardAction(listener, 
                                 KeyStroke.getKeyStroke("F"), 
                                 JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    prev = new JButton ("<<");
    if (game.isEmpty() || game.isOver()) prev.setEnabled(false);
    prev.addActionListener(listener);
    prev.registerKeyboardAction(listener, 
                                KeyStroke.getKeyStroke("X"), 
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    next = new JButton (">>");
    if (game.isEmpty() || game.isOver()) next.setEnabled(false);
    next.addActionListener(listener);
    next.registerKeyboardAction(listener, 
                                KeyStroke.getKeyStroke("C"), 
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    undo = new JButton("Undo");
    if (!game.canUndo() || game.isOver()) undo.setEnabled(false);
    undo.addActionListener(listener);
    undo.registerKeyboardAction(listener, 
                                KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK), 
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    redo = new JButton("Redo");
    if (!game.canRedo() || game.isOver()) redo.setEnabled(false);
    redo.addActionListener(listener);
    redo.registerKeyboardAction(listener, 
                                KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK), 
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    reset = new JButton("Reset");
    if (game.isEmpty()) reset.setEnabled(false);
    reset.addActionListener(listener);
    reset.registerKeyboardAction(listener, 
                                 KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, true), 
                                 JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    add(flexibleComp());
    add(solution());
    add(Box.createRigidArea(new Dimension(0,5)));
    add(placeKeys());
    add(Box.createRigidArea(new Dimension(0,3)));
    add(moveKeys());
    add(Box.createRigidArea(new Dimension(0,3)));
    add(undoKeys());
    add(Box.createRigidArea(new Dimension(0,5)));
    add(reset());
    add(flexibleComp());
    
  }
  
  /**
   * Generates a JPanel consisting of a grid of labels.  Overall JPanel is a visual
   * representation of the solution of the current game.
   * 
   * @return JComponent representation of the game solution.
   */
  private JPanel solution() {
    
    JPanel solutionPane = new JPanel(new GridLayout(game.getHeight(),game.getWidth()));
    solutionPane.setPreferredSize(new Dimension(200,200));
    solutionPane.setMaximumSize(new Dimension(200,200));
    for (int i = 0; i < solution.length; i++) {
      for (int j = 0; j < solution[0].length; j++) {
        
        JLabel square = new JLabel();
        square.setOpaque(true);
        square.setBorder(BorderFactory.createRaisedBevelBorder());
        int shade = solution[i][j];
        
        switch (shade) {
            
          case WHITE :
            square.setBackground(OFF_WHITE);
            break;
            
          case BLACK :
            square.setBackground(OFF_BLACK);
            break;
            
          case GRAY :
            square.setBackground(MID_GRAY);
            break;
            
          default :
            square.setBackground(BACKGROUND);
            square.setBorder(BorderFactory.createLineBorder(Color.black));
            break;
            
        }
        
        solutionPane.add(square);
        
      }
    }
    
    return solutionPane;
    
  }
  
  /**
   * Generates a JPanel containing the place and remove buttons.
   * 
   * @return JPanel containing buttons for placing/removing pieces.
   */
  private JPanel placeKeys() {
    
    JPanel placePanel = new JPanel(new GridLayout(0,2));
    placePanel.setPreferredSize(new Dimension(200,50));
    placePanel.setMaximumSize(new Dimension(200,50));
    
    placePanel.add(place);
    placePanel.add(remove);
    
    return placePanel;
    
  }
  
  /**
   * Generates a JPanel containing buttons for moving between pieces as
   * well as moving individual pieces.
   * 
   * @return JPanel containing buttons for piece manipulation
   */
  private JPanel moveKeys() {
    
    JPanel keysPanel = new JPanel(new GridLayout(0,3));
    keysPanel.setPreferredSize(new Dimension(200,120));
    keysPanel.setMaximumSize(new Dimension(200,120));
    
    keysPanel.add(prev);
    keysPanel.add(up);
    keysPanel.add(next);
    keysPanel.add(left);
    keysPanel.add(down);
    keysPanel.add(right);
    
    return keysPanel;
    
  }
  
  /**
   * Generates a JPanel containing the undo and redo buttons.
   * 
   * @return JPanel containing buttons for undoing and redoing steps.
   */
  private JPanel undoKeys() {
    
    JPanel undoPanel = new JPanel(new GridLayout(0,2));
    undoPanel.setPreferredSize(new Dimension(200,55));
    undoPanel.setMaximumSize(new Dimension(200,55));
    
    undoPanel.add(undo);
    undoPanel.add(redo);
    
    return undoPanel;
    
  }
  
  /**
   * Generates a JPanel containing the reset button.
   * 
   * @return JPanel containing the reset button.
   */
  private JPanel reset() {
    
    JPanel resetPanel = new JPanel(new GridLayout(0,1));
    resetPanel.setPreferredSize(new Dimension(200,60));
    resetPanel.setMaximumSize(new Dimension(200,60));
    
    resetPanel.add(reset);
    
    return resetPanel;
    
  }
  
  /**
   * Generates a JPanel with undefined size preferences
   * allowing for the remainder of the SidePanel to hold
   * constant dimensions.
   * 
   * @return empty JPanel
   */
  private JPanel flexibleComp() {
    
    return new JPanel();
    
  }
  
  /**
   * Refreshes the appearence of this SidePanel and the corresponding GamePanel.
   */
  private void refresh() {
        
        mainPanel.removeAll();
        mainPanel.add(new GamePanel(game));
        mainPanel.repaint();
        mainPanel.revalidate();
        
        sidePanel.removeAll();
        sidePanel.add(new SidePanel(game,mainPanel));
        sidePanel.repaint();
        sidePanel.revalidate();
    
  }
  
  /**
   * Listeners respond to user interaction with this SidePanel.
   */
  private class ButtonListener implements ActionListener {
    
    public void actionPerformed(ActionEvent event) {
      
      Object e = event.getSource();
      
      if (e == place) {
        
        game.place();
        refresh();
        
        if (game.isOver()) {
          JOptionPane.showMessageDialog(SwingUtilities.getRoot(sidePanel),
                                        "Congratulations. You have completed the puzzle.",
                                        "Game Cleared",JOptionPane.PLAIN_MESSAGE);
        }
        
        System.out.println("placed");
          
      } else if (e == remove) {
        
        game.displace();
        refresh();
        
        System.out.println("displaced");
        
      } else if (e == up) {
        
        game.move(UP);
        refresh();
        
        System.out.println("moved up");
        
      } else if (e == down) {
        
        game.move(DOWN);
        refresh();
        
        System.out.println("moved down");
        
      } else if (e == left) {
        
        game.move(LEFT);
        refresh();
        
        System.out.println("moved left");
        
      } else if (e == right) {
        
        game.move(RIGHT);
        refresh();
        
        System.out.println("moved right");
        
      } else if (e == prev) {
        
        game.prev();
        refresh();
        
        System.out.println("prev got");
        
      } else if (e == next) {
        
        game.next();
        refresh();
        
        System.out.println("next got");
        
      } else if (e == undo) {
        
        game.stepper(UNDO);
        refresh();
        
        System.out.println("undo");
        
      } else if (e == redo) {
        
        game.stepper(REDO);
        refresh();
        
        System.out.println("redo");
        
      } else if (e == reset) {
        
        game.clear();
        System.out.println(game);
        refresh();
        
        System.out.println("reset");
        
      }
      
    }
    
  }
  
}