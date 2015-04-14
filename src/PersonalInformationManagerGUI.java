import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Main class to draw the windows for the Personal Information Manager Application
 * @author	Jack Evans
 */
public class PersonalInformationManagerGUI {
	final static String APPOINTEMENTSTAB = "Appointments";
	final static String CONTACTSTAB = "Contacts";
	final static String UNIVERSITYRESOURCESTAB = "University Resources";
	final static String NOTESTAB = "Notes";

	static DataBaseConnection dataBaseConncetion = new DataBaseConnection();

	public static void main(String[] args) {
		// Establish a connection to the DataBase
		dataBaseConncetion.makeConnection();
		try {
			// For Linux:GTKLookAndFeel, Windows:WindowsLookAndFeel, OSX:???
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (UnsupportedLookAndFeelException | IllegalAccessException | ClassNotFoundException | InstantiationException ex) {
			ex.printStackTrace();
		}

		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Adds each main app feature as a tab across the top of the application
	 */
	public void addComponentToPane(Container pane) {
		JTabbedPane tabbedPane = new JTabbedPane();
		buildNotesPanel(tabbedPane);
		buildContactsPanel(tabbedPane);
		buildAppointmentsPanel(tabbedPane);
		buildUniversityResourcesPanel(tabbedPane);
		pane.add(tabbedPane, BorderLayout.CENTER);
	}

	/**
	 * Create the GUI and show it. For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("PerosnalInformationManager");

		// Get the quote of the day and make it appear in pop up window dialog
		QuoteOfTheDay quoteOfTheDay = new QuoteOfTheDay();
		JOptionPane.showMessageDialog(frame, quoteOfTheDay.getQuoteOfTheDay() + ' ' + quoteOfTheDay.getAuthor(), "QuoteOfTheDay", JOptionPane.PLAIN_MESSAGE);

		//Set the dimensions of the window
		frame.setPreferredSize(new Dimension(1000, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		PersonalInformationManagerGUI app = new PersonalInformationManagerGUI();
		app.addComponentToPane(frame.getContentPane());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 *  Method to fetch contacts data and build contacts panel
	 */
	public void buildContactsPanel(JTabbedPane tabbedPane) {
		JPanel contactsPanel = new JPanel();
		try {
			ResultSet results = dataBaseConncetion.getTableData("CONTACTS");
			while (results.next()) {
				contactsPanel.add(new JLabel("Forename: "));
				contactsPanel.add(new JTextField(results.getString("CONTACT_FORENAME")));

				contactsPanel.add(new JLabel("Surname: "));
				contactsPanel.add(new JTextField(results.getString("CONTACT_SURNAME")));

				contactsPanel.add(new JLabel("Phone Number: "));
				contactsPanel.add(new JTextField(results.getString("CONTACT_PHONE")));

				contactsPanel.add(new JLabel("Email: "));
				contactsPanel.add(new JTextField(results.getString("CONTACT_EMAIL")));
			}
			tabbedPane.addTab(CONTACTSTAB, contactsPanel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JScrollPane contactPane = new JScrollPane(contactsPanel);
		contactPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		contactPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		contactPane.setBounds(50, 30, 300, 50);
		tabbedPane.addTab(NOTESTAB, contactPane);
	}

	/**
	 *  Method to fetch appointment data and build appointment panel
	 */
	public void buildAppointmentsPanel(JTabbedPane tabbedPane) {
		JPanel appointmentsPanel = new JPanel();
		try {
			ResultSet results = dataBaseConncetion.getTableData("APPOINTMENTS");
			while (results.next()) {
				appointmentsPanel.add(new JLabel("Appointment: "));
				appointmentsPanel.add(new JTextField(results.getString("APPOINTMENT_TITLE")));

				appointmentsPanel.add(new JLabel("Category: "));
				appointmentsPanel.add(new JTextField(results.getString("APPOINTMENT_CATEGORY")));

				appointmentsPanel.add(new JLabel("Description: "));
				appointmentsPanel.add(new JTextField(results.getString("APPOINTMENT_DESCRIPTION")));

				appointmentsPanel.add(new JLabel("Location: "));
				appointmentsPanel.add(new JTextField(results.getString("APPOINTMENT_LOCATION")));
			}
			tabbedPane.addTab(APPOINTEMENTSTAB, appointmentsPanel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JScrollPane appointmentPane = new JScrollPane(appointmentsPanel);
		appointmentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		appointmentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		appointmentPane.setBounds(50, 30, 300, 50);
		tabbedPane.addTab(APPOINTEMENTSTAB, appointmentPane);
	}

	/**
	 *  Method to fetch notes data and build notes panel
	 */
	public void buildNotesPanel(JTabbedPane tabbedPane) {
		JPanel notesPanel = new JPanel();
		GridLayout layout = new GridLayout(0,1);
      	layout.setHgap(10);
      	layout.setVgap(10);
		notesPanel.setLayout(layout);
		try {
			ResultSet results = dataBaseConncetion.getTableData("NOTES");
			while (results.next()) {
				notesPanel.add(new JTextArea(results.getString("NOTE_TITLE")));
				notesPanel.add(new JTextArea(results.getString("NOTE_CONTENT")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JScrollPane notesPane = new JScrollPane(notesPanel);
		notesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		notesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		notesPane.setBounds(50, 30, 300, 50);
		tabbedPane.addTab(NOTESTAB, notesPane);
	}

	/**
	 * Method to build University Resources panel
	 */
	public void buildUniversityResourcesPanel(JTabbedPane tabbedPane) {
		JPanel universityResourcesTab = new JPanel();
		universityResourcesTab.add(new JButton("Click me to make it rain $$$$"));
		tabbedPane.addTab(UNIVERSITYRESOURCESTAB, universityResourcesTab);
	}
}