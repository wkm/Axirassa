
package test.axirassa.overlord;

import charva.awt.BorderLayout;
import charva.awt.Color;
import charva.awt.Container;
import charva.awt.Toolkit;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charvax.swing.BoxLayout;
import charvax.swing.JFrame;
import charvax.swing.JLabel;
import charvax.swing.JMenu;
import charvax.swing.JMenuBar;
import charvax.swing.JMenuItem;
import charvax.swing.JOptionPane;
import charvax.swing.JPanel;

public class TestCharva extends JFrame implements ActionListener {

	static {
		System.out.println("LOADING PDCURSES");
		System.loadLibrary("pdcurses");
	}


	public TestCharva() {
		setForeground(Color.green);
		setBackground(Color.black);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		JMenuBar menubar = new JMenuBar();
		JMenu jMenuFile = new JMenu("File");
		jMenuFile.setMnemonic('F');

		JMenuItem jMenuItemFileChooser = new JMenuItem("JFileChooser", 'F');
		jMenuItemFileChooser.addActionListener(this);
		jMenuFile.add(jMenuItemFileChooser);

		JMenuItem jMenuItemCustomFileChooser = new JMenuItem("custom FileChooser", 'c');
		jMenuItemCustomFileChooser.addActionListener(this);
		jMenuFile.add(jMenuItemCustomFileChooser);

		jMenuFile.addSeparator();

		JMenuItem jMenuItemFileExit = new JMenuItem("Exit", 'x');
		jMenuItemFileExit.addActionListener(this);
		jMenuFile.add(jMenuItemFileExit);

		JMenu jMenuLayout = new JMenu("Layouts");
		jMenuLayout.setMnemonic('L');
		JMenuItem jMenuItemLayoutNull = new JMenuItem("Null Layout");
		jMenuItemLayoutNull.setMnemonic('N');
		jMenuItemLayoutNull.addActionListener(this);
		jMenuLayout.add(jMenuItemLayoutNull);

		jMenuLayout.addSeparator();

		JMenuItem jMenuItemLayoutMisc = new JMenuItem("Miscellaneous Layouts");
		jMenuItemLayoutMisc.setMnemonic('M');
		jMenuItemLayoutMisc.addActionListener(this);
		jMenuLayout.add(jMenuItemLayoutMisc);

		JMenuItem jMenuItemLayoutColor = new JMenuItem("Layouts in Color");
		jMenuItemLayoutColor.setMnemonic('C');
		jMenuItemLayoutColor.addActionListener(this);
		jMenuLayout.add(jMenuItemLayoutColor);

		JMenuItem jMenuItemLayoutGBL = new JMenuItem("GridBagLayout");
		jMenuItemLayoutGBL.setMnemonic('G');
		jMenuItemLayoutGBL.addActionListener(this);
		jMenuLayout.add(jMenuItemLayoutGBL);

		JMenu jMenuContainers = new JMenu("Containers");
		jMenuContainers.setMnemonic('C');

		JMenuItem jMenuItemContainerJTabbedPane = new JMenuItem("JTabbedPane");
		jMenuItemContainerJTabbedPane.setMnemonic('T');
		jMenuItemContainerJTabbedPane.addActionListener(this);
		jMenuContainers.add(jMenuItemContainerJTabbedPane);

		JMenu jMenuItemContainerJOptionPane = new JMenu("JOptionPane...");
		jMenuItemContainerJOptionPane.setMnemonic('O');
		jMenuContainers.add(jMenuItemContainerJOptionPane);

		JMenuItem jMenuItemShowMessageDialog = new JMenuItem("showMessageDialog");
		jMenuItemShowMessageDialog.addActionListener(this);
		jMenuItemContainerJOptionPane.add(jMenuItemShowMessageDialog);

		JMenuItem jMenuItemShowConfirmDialog = new JMenuItem("showConfirmDialog");
		jMenuItemShowConfirmDialog.addActionListener(this);
		jMenuItemContainerJOptionPane.add(jMenuItemShowConfirmDialog);

		JMenuItem jMenuItemShowInputDialog = new JMenuItem("showInputDialog");
		jMenuItemShowInputDialog.addActionListener(this);
		jMenuItemContainerJOptionPane.add(jMenuItemShowInputDialog);

		JMenuItem jMenuItemShowCustomInputDialog = new JMenuItem("show Custom InputDialog");
		jMenuItemShowCustomInputDialog.addActionListener(this);
		jMenuItemContainerJOptionPane.add(jMenuItemShowCustomInputDialog);

		JMenu jMenuWidgets = new JMenu("Widgets");
		jMenuWidgets.setMnemonic('W');

		JMenuItem jMenuItemWidgetText = new JMenuItem("Text components");
		jMenuItemWidgetText.setMnemonic('T');
		jMenuItemWidgetText.addActionListener(this);
		jMenuWidgets.add(jMenuItemWidgetText);

		JMenuItem jMenuItemWidgetSelection = new JMenuItem("Selection components");
		jMenuItemWidgetSelection.setMnemonic('S');
		jMenuItemWidgetSelection.addActionListener(this);
		jMenuWidgets.add(jMenuItemWidgetSelection);

		JMenuItem jMenuItemWidgetButtons = new JMenuItem("Buttons");
		jMenuItemWidgetButtons.setMnemonic('B');
		jMenuItemWidgetButtons.addActionListener(this);
		jMenuWidgets.add(jMenuItemWidgetButtons);

		JMenuItem jMenuItemWidgetJTable = new JMenuItem("JTable");
		jMenuItemWidgetJTable.setMnemonic('J');
		jMenuItemWidgetJTable.addActionListener(this);
		jMenuWidgets.add(jMenuItemWidgetJTable);

		JMenu jMenuEvents = new JMenu("Events");
		jMenuEvents.setMnemonic('E');

		JMenuItem jMenuItemKeyEvents = new JMenuItem("KeyEvents");
		jMenuItemKeyEvents.setMnemonic('K');
		jMenuItemKeyEvents.addActionListener(this);
		jMenuEvents.add(jMenuItemKeyEvents);

		JMenuItem jMenuItemFocusEvents = new JMenuItem("FocusEvents");
		jMenuItemFocusEvents.setMnemonic('F');
		jMenuItemFocusEvents.addActionListener(this);
		jMenuEvents.add(jMenuItemFocusEvents);

		JMenu jMenuThreads = new JMenu("Threads");
		jMenuThreads.setMnemonic('T');

		JMenuItem jMenuItemProgressBar = new JMenuItem("JProgressBar");
		jMenuItemProgressBar.setMnemonic('P');
		jMenuItemProgressBar.addActionListener(this);
		jMenuThreads.add(jMenuItemProgressBar);

		menubar.add(jMenuFile);
		menubar.add(jMenuLayout);
		menubar.add(jMenuContainers);
		menubar.add(jMenuWidgets);
		menubar.add(jMenuEvents);
		menubar.add(jMenuThreads);

		setJMenuBar(menubar);

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		labelPanel.add(new JLabel("Use LEFT and RIGHT cursor keys to select a menu."));
		labelPanel.add(new JLabel("Use ENTER to invoke a menu or menu-item."));
		labelPanel.add(new JLabel("(You can also use the " + "underlined \"mnemonic key\" to invoke a menu.)"));
		labelPanel.add(new JLabel("Use BACKSPACE or ESC to dismiss a menu."));
		contentPane.add(labelPanel, BorderLayout.SOUTH);

		setLocation(0, 0);
		setSize(80, 24);
		validate();
	}


	@Override
	public void actionPerformed(ActionEvent ae_) {
		String actionCommand = ae_.getActionCommand();
		if (actionCommand.equals("Exit")) {
			System.gc(); // so that HPROF reports only live objects.
			System.exit(0);
		} else if (actionCommand.equals("JFileChooser")) {
		} else if (actionCommand.equals("custom FileChooser")) {
			// JFileChooser.CANCEL_LABEL = "Cancel (F4)";
			// JFileChooser.CANCEL_ACCELERATOR = KeyEvent.VK_F4;
			// (new JFileChooser()).show();
			JOptionPane.showMessageDialog(this, "This test has been (temporarily) disabled", "Information",
			                              JOptionPane.PLAIN_MESSAGE);
		} else if (actionCommand.equals("Null Layout")) {
		} else if (actionCommand.equals("Miscellaneous Layouts")) {
		} else if (actionCommand.equals("Layouts in Color")) {
			if (!Toolkit.getDefaultToolkit().hasColors()) {
				JOptionPane.showMessageDialog(this, "This terminal does not have color capability!", "Error",
				                              JOptionPane.PLAIN_MESSAGE);
				return;
			}
		} else if (actionCommand.equals("GridBagLayout")) {

		} else if (actionCommand.equals("JTabbedPane")) {

		} else if (actionCommand.equals("showMessageDialog")) {
			JOptionPane.showMessageDialog(this, "This is an example of a Message Dialog "
			        + "with a single message string", "This is the title", JOptionPane.PLAIN_MESSAGE);
		} else if (actionCommand.equals("showConfirmDialog")) {

		} else if (actionCommand.equals("showInputDialog")) {

		} else if (actionCommand.equals("show Custom InputDialog")) {

		} else if (actionCommand.equals("Text components")) {

		} else if (actionCommand.equals("Selection components")) {

		} else if (actionCommand.equals("Buttons")) {

		} else if (actionCommand.equals("JTable")) {

		} else if (actionCommand.equals("KeyEvents")) {

		} else if (actionCommand.equals("FocusEvents")) {
		} else if (actionCommand.equals("JProgressBar")) {

		} else {
			JOptionPane.showMessageDialog(this, "Menu item \"" + actionCommand + "\" not implemented yet", "Error",
			                              JOptionPane.PLAIN_MESSAGE);
		}
		// Trigger garbage-collection after every menu action.
		Toolkit.getDefaultToolkit().triggerGarbageCollection(this);
	}


	static public void main(String[] args) throws InterruptedException {
		System.out.println("executing main() process");

		TestCharva test = new TestCharva();
		test.show();
	}

}
