
package axirassa.overlord.tui;

import charva.awt.BorderLayout;
import charva.awt.Color;
import charva.awt.Container;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charvax.swing.BoxLayout;
import charvax.swing.JFrame;
import charvax.swing.JLabel;
import charvax.swing.JMenu;
import charvax.swing.JMenuBar;
import charvax.swing.JMenuItem;
import charvax.swing.JPanel;

public class OverlordWindow extends JFrame implements ActionListener {
	public OverlordWindow () {
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

		menubar.add(jMenuFile);

		setJMenuBar(menubar);

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		labelPanel.add(new JLabel("Ax|Overlord"));
		contentPane.add(labelPanel, BorderLayout.NORTH);

		setLocation(0, 0);
		setSize(300, 50);
		validate();
	}


	@Override
	public void actionPerformed (ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals("Exit")) {
			System.exit(0);
		}
	}
}
