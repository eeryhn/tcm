/**
 * Overall GUI allows players to interact with a Trichromino object.
 * 
 * @author Angela Wu (primary creator)
 * @author Lisa Li
 * @version %I%, %G%
 * */
 
 package trichromino;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TrichrominoGUI {
  
  private Trichromino game;
  private String[] modeOptions;
  
  private static JFrame frame;
  
  private JPanel content;
  private AboutPanel aboutPanel;
  private GameplayPanel gameplayPanel;
  private TrichrominoPanel gamePanel;  
  private CardLayout layout;
  private JMenuBar menuBar;
  private JMenu gameMenu, helpMenu;
  private JMenuItem newGame, save, load, quit, gameplay, about;
  
  private ButtonListener listener;
  
  /**
   * Creates a JMenuBar allowing players access to help panels and
   * game creation
   * 
   * @return GUI menu bar.
   */
  private JMenuBar makeMenu() {
   
    listener = new ButtonListener();
                                    
    menuBar = new JMenuBar();
    
    gameMenu = new JMenu("Game");
    gameMenu.setMnemonic(KeyEvent.VK_G);
    
    menuBar.add(gameMenu);
    
    newGame = new JMenuItem("New Game      ");
    newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
    newGame.addActionListener(listener);
    gameMenu.add(newGame);
    
    load = new JMenuItem("Load");
    load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    load.addActionListener(listener);
    gameMenu.add(load);
    
    save = new JMenuItem("Save");
    if (game.isEmpty()) save.setEnabled(false);
    save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    save.addActionListener(listener);
    gameMenu.add(save);
    
    quit = new JMenuItem("Quit");
    quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
    quit.addActionListener(listener);
    gameMenu.add(quit);
    
    helpMenu = new JMenu("Help");
    helpMenu.setMnemonic(KeyEvent.VK_H);
    
    menuBar.add(helpMenu);
    
    gameplay = new JMenuItem("Gameplay      ");
    gameplay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.CTRL_MASK));
    gameplay.addActionListener(listener);
    helpMenu.add(gameplay);
    
    about = new JMenuItem("About");
    about.addActionListener(listener);
    helpMenu.add(about);
    
    return menuBar;
    
  }
  
  /**
   * Generates the contents of the frame.
   */
  private void setContent() {
    
    listener = new ButtonListener();
    
    if (game.isOver()) {
      
      int playAgain = JOptionPane.showConfirmDialog(null,"Play Again?");
      if (playAgain == JOptionPane.YES_OPTION) {
        
        newGame();
        
      }
      
    }
    
    aboutPanel = new AboutPanel();
    aboutPanel.getOK().addActionListener(listener);
    aboutPanel.getOK().registerKeyboardAction(listener, 
                                              KeyStroke.getKeyStroke("ESCAPE"), 
                                              JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    gameplayPanel = new GameplayPanel();
    gameplayPanel.getOK().addActionListener(listener);
    gameplayPanel.getOK().registerKeyboardAction(listener, 
                                              KeyStroke.getKeyStroke("ESCAPE"), 
                                              JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    gamePanel = new TrichrominoPanel(game);
    
    layout = new CardLayout();
    content = new JPanel(layout);
    content.add(gamePanel, "game");   
    content.add(aboutPanel, "about");
    content.add(gameplayPanel, "gameplay");
    
  }
  
  /**
   * Executes the game.
   */
  public static void main (String[]args) {  
    
    frame = new JFrame("Trichromino");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    frame.setMinimumSize(new Dimension(800,600));
    
    TrichrominoGUI gui = new TrichrominoGUI();
    
    gui.game = new Trichromino();
    
    frame.setJMenuBar(gui.makeMenu());
    gui.setContent();
    frame.setContentPane(gui.content);
    
    frame.pack();
    frame.setVisible(true);
    
  }
  
  /**
   * Generates a series of dialogs for new game creation.
   */
  private void newGame() {
    
    modeOptions = new String[]{"Basic","Challenge"};
    
    int mode = JOptionPane.showOptionDialog(frame,"Please Select a Game Mode","New Game",0,
                                            JOptionPane.PLAIN_MESSAGE,null, modeOptions, null);
    Trichromino triGame = new Trichromino();
    switch (mode) {
      case 0:
        triGame.generateEasy();
        break;
      case 1:
        triGame.generateSEasy();
        break;
    }
    refresh(triGame);
  }
  private void refresh(Trichromino triGame) {
    
    TrichrominoGUI gui = new TrichrominoGUI();
    gui.game = triGame;
    
    frame.setJMenuBar(gui.makeMenu());
    gui.setContent();
    frame.getContentPane().removeAll();
    frame.setContentPane(gui.content);
    frame.getContentPane().repaint();
    frame.getContentPane().revalidate();
    
  }
  
  /**
   * Generates a series of dialogs to load a game from a file.
   */
  private void loadLoop() {
    
    String fileName = JOptionPane.showInputDialog(frame, "Please Input an Existing File Name");
    if (fileName!=null) {
      try {
        
        refresh(Trichromino.fromFile(fileName));
        
      } catch (Exception exception) {
        loadLoop();
      }
    } 
    
  }
  
  
  /**
   * Generates a series of dialogs to save a game to a file.
   */
  private void saveLoop() {
    String fileName = JOptionPane.showInputDialog(frame, "Please Input a Valid File Name"); 
    if (fileName!=null) {
      try {
        game.save(fileName);
      } catch (Exception exception) {
        saveLoop();
      }
    }
  }
  
  /**
   * ButtonListener responds to user interaction with the menu items.
   */
  private class ButtonListener implements ActionListener {
    
    public void actionPerformed(ActionEvent event) {
      
      Object e = event.getSource();
      
      if (e == newGame) {
        
        newGame();
        
      } else if (e == load) {
        
        loadLoop();
        
      } else if (e == save) {
        
        saveLoop();
        
      } else if (e == quit) {
        
        if (!game.isEmpty()) {
          int confirmQuit = JOptionPane.showConfirmDialog(frame,"Save Before Quitting?");
          switch (confirmQuit) {
            case JOptionPane.YES_OPTION: 
              saveLoop();
              System.exit(0);
            case JOptionPane.NO_OPTION: System.exit(0);
          }
          System.out.println("quit");
          
        } else {
          System.exit(0);
        }
            
      } else if (e == gameplay) {
        
        layout.show(content, "gameplay");
        System.out.println("gameplay");        
        
      } else if (e == about) {
        
        layout.show(content, "about");
        System.out.println("about");        
        
      } else if (e == gameplayPanel.getOK()) {
        
        layout.show(content,"game");
        System.out.println("game");
        
      } else if (e == aboutPanel.getOK()) {
        
        layout.show(content,"game");
        System.out.println("game");
        
      }
      
    }
    
  }
  
}