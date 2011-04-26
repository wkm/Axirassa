
package axirassa.overlord.tui;


import lombok.Getter;
import axirassa.overlord.OverlordMain;
import charva.awt.BorderLayout;
import charva.awt.Color;
import charva.awt.Container;
import charva.awt.Insets;
import charva.awt.Toolkit;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charvax.swing.JFrame;
import charvax.swing.JMenu;
import charvax.swing.JMenuBar;
import charvax.swing.JMenuItem;
import charvax.swing.JPanel;
import charvax.swing.JScrollPane;
import charvax.swing.JTable;
import charvax.swing.JTextArea;
import charvax.swing.border.LineBorder;
import charvax.swing.border.TitledBorder;
import charvax.swing.table.DefaultTableModel;
import charvax.swing.table.TableModel;

public class OverlordWindow extends JFrame implements ActionListener {

@Getter
	private OverlordMain overlord;


	public OverlordWindow (OverlordMain overlord) {
		super();
		super._insets = new Insets(0, 0, 0, 0);

		this.overlord = overlord;

		Container contentPane = getContentPane();
		BorderLayout gridLayout = new BorderLayout();
		
		contentPane.setLayout(gridLayout);

		JTable table = new JTable();
		TableModel tableModel = new DefaultTableModel(1, 3);
		table.setModel(tableModel);
		contentPane.add(table, BorderLayout.NORTH);


		JTextArea textArea = new JTextArea();
		textArea.setText("wut wut wut");
		textArea.setEditable(false);
		
		textArea.setColumns(150);
		textArea.setRows(30);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		TitledBorder textBorder = new TitledBorder(new LineBorder(Color.cyan));
		textBorder.setTitle("Output");
		scrollPane.setViewportBorder(textBorder);

		scrollPane.setSize(100,30);
		
		JPanel textPanel = new JPanel();
		textPanel.add(scrollPane);
		
		textPanel.setSize(100, 30);
		
		contentPane.add(textPanel, BorderLayout.SOUTH);

		setJMenuBar(buildMenu());		
		
		

		setLocation(0, 0);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		validate();
	}


	private JMenuBar buildMenu () {
		JMenuBar menubar = new JMenuBar();

		JMenu jMenuFile = new JMenu("Ax|Overlord");
		jMenuFile.setMnemonic('O');
		jMenuFile.setSelected(false);

		JMenuItem jMenuItemFileExit = new JMenuItem("Exit", 'x');
		jMenuItemFileExit.addActionListener(this);
		jMenuFile.add(jMenuItemFileExit);

		JMenu jMenuGroups = new OverlordGroupsMenu(this);
		jMenuGroups.setText("+ Group");
		jMenuGroups.setMnemonic('G');

		JMenu jMenuTargets = new OverlordTargetsMenu(this);
		jMenuTargets.setText("+ Target");
		jMenuTargets.setMnemonic('T');

		menubar.add(jMenuFile);
		menubar.add(jMenuGroups);
		menubar.add(jMenuTargets);
		
		return menubar;
    }


	@Override
	public void actionPerformed (ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals("Exit")) {
			System.exit(0);
		} else {
		}
	}
	
	public synchronized void executeTarget (String targetName) {
		
	}
}
