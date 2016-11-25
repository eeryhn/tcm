/**
 * A simple panel explaining the technicalities of the game.
 * 
 * @author Angela Wu (layout creator)
 * @author Lisa Li (content creator)
 * @version %I%, %G%
 * */

package trichromino;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;

public class AboutPanel extends JPanel {
   
  private JButton ok;
  private JLabel about;
  private URL aboutFile = getClass().getResource("/imgs/about.png");
  private static final long serialVersionUID = 1;
  
  /**
   * Constructor creates an information panel containing "version" details and
   * creator details.
   * Uses a BorderLayout for consistency.
   */
  public AboutPanel() { 
    
    ok = new JButton("OK");
    
    setLayout(new BorderLayout(3,3));
    
    try {
      add(content(),BorderLayout.CENTER);
    } catch (IOException e) {
      System.out.println(e);
    }
    add(okPanel(),BorderLayout.SOUTH);
    
  }  
  
  /**
   * Creates a JScrollPane containing the informative content.  While it
   * does not scroll, a JScrollPane is returned for the sake of consistency
   * with GameplayPanel as they are both cards in the GUI.
   * 
   * @return Primary content for this panel.
   */
  private JScrollPane content() throws IOException{
    
    JPanel panel = new JPanel();
    panel.setBackground(new Color(242,242,242));
    
    // create image
    try {
      BufferedImage img = ImageIO.read(aboutFile);
      about = new JLabel(new ImageIcon(img));
      panel.add(about);
    } catch (IOException e) {
      throw new IOException("content: file " + aboutFile + " could not be found.");
    }
    
    // create scrollpane
    JScrollPane scroll = new JScrollPane(panel);
    scroll.setBackground(new Color(242,242,242));
    scroll.setPreferredSize(new Dimension(500,480));
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    return scroll;
    
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
  
}
