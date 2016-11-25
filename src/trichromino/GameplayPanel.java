/**
 * A relatively simple panel (featuring a CardLayout) that describes the
 * game mechanics as well as the keybindings.
 * 
 * @author Angela Wu (primary creator)
 * @author Lisa Li 
 * @version %I%, %G%
 * */

package trichromino;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;

public class GameplayPanel extends JPanel {
  
  private JButton ok, game, keys;
  private JLabel gamePlayImg;
  private ButtonListener listener;
  private CardLayout layout;
  private JPanel main;
  private URL gameplayFile, keysFile;
  
  private static final long serialVersionUID = 1;
  
  /**
   * Constructor creates a panel describing game play mechanisms.
   */
  public GameplayPanel() {  
    
    setLayout(new BorderLayout(3,3));
    
    listener = new ButtonListener();
    
    ok = new JButton("OK");
    game = new JButton("Game");
    game.setPreferredSize(new Dimension(100,100));
    game.addActionListener(listener);
    keys = new JButton("Keys");
    keys.setPreferredSize(new Dimension(100,100));
    keys.addActionListener(listener);
    
    layout = new CardLayout();
    main = mainPanel();
    
    add(main,BorderLayout.CENTER);
    add(navButtons(),BorderLayout.WEST);
    add(okPanel(), BorderLayout.SOUTH);
    
  }
  
  /**
   * Creates the primary content pane in this panel.  Content is stacked
   * using a CardLayout.
   * 
   * @return JPanel containing main content of this pain.
   */
  private JPanel mainPanel() {
    
    gameplayFile = GameplayPanel.class.getResource("/imgs/expl.png");
    keysFile = GameplayPanel.class.getResource("/imgs/keybindings.png");
    
    JPanel panel = new JPanel(layout);
    
    try {
      panel.add(mainCard(gameplayFile),"gameplay");
      panel.add(mainCard(keysFile),"keybindings");
    } catch (Exception e) {
      System.out.println(e);
    }
    
    return panel;
    
  }
  
  /**
   * Creates a JScrollPane around the file defined by fileName.
   * 
   * @param fileName the name of the file around which the scroll 
   *                 pane will be created.
   * @return a JScrollPane around the file defined by fileName.
   */
  private JScrollPane mainCard(URL fileName) throws IOException{
    
    JPanel panel = new JPanel();
    panel.setBackground(new Color(242,242,242));
    
    try {
      BufferedImage img = ImageIO.read(fileName);
      gamePlayImg = new JLabel(new ImageIcon(img));
      panel.add(gamePlayImg);
    } catch (IOException e) {
      throw new IOException("gamePlay(): file " + fileName + " could not be found.");
    }
    
    JScrollPane scroll = new JScrollPane(panel);
    scroll.setBackground(new Color(242,242,242));
    scroll.setPreferredSize(new Dimension(500,500));
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    return scroll;
    
  }
  
  /**
   * Stacks a set of navigation buttons in a single width GridLayout.
   * 
   * @return a JPanel containing buttons to the two cards.
   */
  private JPanel navButtons() {
    
    JPanel panel = new JPanel(new GridLayout(0,1));
    panel.setPreferredSize(new Dimension(120,450));
    
    panel.add(game);
    panel.add(keys);
    
    return panel;
    
  }
  
  /**
   * Centers a the OK button in a JPanel.
   * 
   * @return a JPanel containing the OK button.
   */
  private JPanel okPanel() {
    
    JPanel panel = new JPanel();
    
    panel.add(ok);
    
    return panel;
    
  }
  
  /**
   * Returns the OK button for use in the frame.
   * 
   * @return OK button.
   */
  public JButton getOK() {
    
    return ok;
    
  }
  
  /**
   * ButtonListener responds to user interaction with the buttons.
   */
  private class ButtonListener implements ActionListener {
    
    public void actionPerformed(ActionEvent event) {
      Object e = event.getSource();
      
      if (e == game) {
        
        layout.show(main,"gameplay");
        
      } else if ( e == keys ) {
        
        layout.show(main,"keybindings");
        
      }
      
    }
    
  }
  
}
