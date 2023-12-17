package sktech.com.iimtiaz.ui.assignment.codeprovided.gui;

import javax.swing.*;

/**
 * Creates the GUI
 */
public class PlayerEntryDashboard extends JFrame {
	public PlayerEntryDashboard(AbstractPlayerDashboardPanel panel) {
		setTitle("Footie Dashboard");
		add(panel);
		// maximises the JFrame
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
