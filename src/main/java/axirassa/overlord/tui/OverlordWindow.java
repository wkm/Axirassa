
package axirassa.overlord.tui;

import axirassa.overlord.OverlordMain;
import charva.awt.Container;
import charva.awt.Insets;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charvax.swing.BorderFactory;
import charvax.swing.BoxLayout;
import charvax.swing.JFrame;
import charvax.swing.JMenu;
import charvax.swing.JMenuBar;
import charvax.swing.JMenuItem;
import charvax.swing.JTable;
import charvax.swing.JTextArea;
import charvax.swing.table.DefaultTableModel;
import charvax.swing.table.TableModel;

public class OverlordWindow extends JFrame implements ActionListener {
	private OverlordMain overlord;


	public OverlordWindow (OverlordMain overlord) {
		super();
		super._insets = new Insets(0, 0, 0, 0);

		this.overlord = overlord;

		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(null, BoxLayout.Y_AXIS));

		JTable table = new JTable();
		TableModel tableModel = new DefaultTableModel(5, 3);
		table.setModel(tableModel);
		contentPane.add(table);

		JTextArea textArea = new JTextArea();
		textArea.setText("wut wut wut");
		textArea.setBorder(BorderFactory.createTitledBorder("Output"));
		textArea.setEditable(false);

		contentPane.add(textArea);

		JMenuBar menubar = new JMenuBar();

		JMenu jMenuFile = new JMenu("Ax|Overlord");
		jMenuFile.setMnemonic('O');
		jMenuFile.setSelected(false);

		JMenuItem jMenuItemFileExit = new JMenuItem("Exit", 'x');
		jMenuItemFileExit.addActionListener(this);
		jMenuFile.add(jMenuItemFileExit);

		JMenu jMenuGroups = new OverlordGroupsMenu(overlord);
		jMenuGroups.setText("+ Group");
		jMenuGroups.setMnemonic('G');

		JMenu jMenuTargets = new OverlordTargetsMenu(overlord);
		jMenuTargets.setText("+ Target");
		jMenuTargets.setMnemonic('T');

		menubar.add(jMenuFile);
		menubar.add(jMenuGroups);
		menubar.add(jMenuTargets);

		setJMenuBar(menubar);

		setLocation(0, 0);
		setSize(150, 50);
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
