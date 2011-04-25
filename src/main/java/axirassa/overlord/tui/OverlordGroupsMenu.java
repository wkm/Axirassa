
package axirassa.overlord.tui;

import lombok.Getter;
import axirassa.overlord.ExecutionGroup;
import axirassa.overlord.OverlordConfiguration;
import axirassa.overlord.OverlordMain;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charvax.swing.JMenu;
import charvax.swing.JMenuItem;

/**
 * Creates a menu of groups from an overlord configuration. Note that the menu
 * has no text or mnemonic, this is to be set by the container.
 * 
 * @author wiktor
 * 
 */
public class OverlordGroupsMenu extends JMenu implements ActionListener {

	@Getter
	private OverlordMain overlord;

	@Getter
	private OverlordConfiguration configuration;


	public OverlordGroupsMenu (OverlordMain overlord) {
		super();
		addActionListener(this);

		this.overlord = overlord;
		this.configuration = overlord.getConfiguration();

		initGroups();
	}


	private void initGroups () {
		int index = 1;
		for (ExecutionGroup group : configuration.getExecutionGroups()) {
			int mnemonic = -1;
			String label = group.getName();

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
			groupItem.setActionCommand("Group:" + group.getCanonicalName());

			add(groupItem);
			index++;
		}
	}


	@Override
	public void actionPerformed (ActionEvent event) {

	}
}
