/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2005-2007, Beneficent
Technology, Inc. (The Benetech Initiative).

Martus is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later
version with the additions and exceptions described in the
accompanying Martus license file entitled "license.txt".

It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, including warranties of fitness of purpose or
merchantability.  See the accompanying Martus License and
GPL license for more details on the required license terms
for this software.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.

*/

package org.martus.client.tools;

public class TestWrappingTest extends javax.swing.JFrame {
    /** Creates new form TestWrappingTest */
  public TestWrappingTest() {
      initComponents();
            String message =
          "<h3>The Declaration of Independence of the Thirteen Colonies</h3>" +
          "When in the Course of human events, it becomes <i>necessary</i> " +
          "for one people to dissolve the political bands which have " +
          "connected them with another, and to assume among the powers " +
          "of the earth, the separate and equal station to which the " +
          "<u>Laws of Nature and of Nature's God</u> entitle them, a decent " +
          "respect to the opinions of mankind requires that they should " +
          "declare the causes which impel them to the separation.";
            label.setContentType( "text/html" );
      label.setText( message );
  }
    /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  private void initComponents() {
      scroller = new javax.swing.JScrollPane();
      label = new javax.swing.JTextPane();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      scroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scroller.setViewportView(label);

      getContentPane().add(scroller, java.awt.BorderLayout.CENTER);

      pack();
  }
    /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
      java.awt.EventQueue.invokeLater(new Runnable() {
          public void run() {
              new TestWrappingTest().setVisible(true);
          }
      });
  }
    // Variables declaration - do not modify
  private javax.swing.JTextPane label;
  private javax.swing.JScrollPane scroller;
  // End of variables declaration
 }
