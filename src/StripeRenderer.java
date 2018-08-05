import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;

/**
 * Changes every even index value into a JLabel with a RBG colour value
 *
 */
public class StripeRenderer extends DefaultListCellRenderer {

  private static final long serialVersionUID = 1L;

  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index,
      boolean isSelected, boolean cellHasFocus) {

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index,
        isSelected, cellHasFocus);

    if (index % 2 == 0) {
      label.setBackground(new Color(237, 237, 237));
    }
    return label;
  }
}
