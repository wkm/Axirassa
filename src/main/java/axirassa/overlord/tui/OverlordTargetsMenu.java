
package axirassa.overlord.tui;

import lombok.Getter;
import axirassa.overlord.ExecutionTarget;
import axirassa.overlord.OverlordConfiguration;
import axirassa.overlord.OverlordMain;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charvax.swing.JMenu;
import charvax.swing.JMenuItem;
import charvax.swing.JOptionPane;

public class OverlordTargetsMenu extends JMenu implements ActionListener {
	@Getter
	private OverlordWindow window;

	@Getter
	private OverlordMain overlord;

	@Getter
	private OverlordConfiguration configuration;


	public OverlordTargetsMenu (OverlordWindow window) {
		super();
		addActionListener(this);

		this.window = window;
		this.overlord = window.getOverlord();
		this.configuration = overlord.getConfiguration();

		initTargets();
	}


	private void initTargets () {
		int index = 1;
		for (ExecutionTarget target : configuration.getExecutionTargets()) {
			int mnemonic = -1;
			String label = target.getName();

			// set a mnemonic for the first 10 groups
			if (index < 10) {
				mnemonic = TuiTools.intToSingleLetter(index);
				label = index + ":" + label;
			} else if (index == 10) {
				mnemonic = TuiTools.intToSingleLetter(0);
				label = "0:" + label;
			} else
				label = "  " + label;

			JMenuItem groupItem = new JMenuItem(label);
			if (mnemonic > -1)
				groupItem.setMnemonic(mnemonic);

			groupItem.addActionListener(this);
			groupItem.setActionCommand(target.getCanonicalName());

			add(groupItem);
			index++;
		}
	}


	@Override
	public void actionPerformed (ActionEvent event) {
		if (event.getActionCommand() != null) {
			window.executeTarget(event.getActionCommand());
			JOptionPane.showConfirmDialog(getAncestorWindow(), event.getActionCommand(), "Action Command",
			                              JOptionPane.YES_NO_CANCEL_OPTION);
		}
	}
}
