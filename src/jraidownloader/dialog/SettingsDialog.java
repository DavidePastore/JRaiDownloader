/**
 * 
 */
package jraidownloader.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import jraidownloader.logging.JRaiLogger;
import jraidownloader.properties.PropertiesManager;
import javax.swing.JCheckBox;

/**
 * Qui verrà gestita la possibilità di modificare le impostazioni del programma.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class SettingsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldDirectory;
	private JFileChooser chooser = new JFileChooser();
	private JCheckBox chckbxSovrascritturaFile;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		try {
			SettingsDialog dialog = new SettingsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		init();
	}
	
	/**
	 * Inizializza l'oggetto.
	 */
	private void init(){
		setBounds(100, 100, 450, 133);
		setTitle("Personalizza Impostazioni");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel labelDirectory = new JLabel("Directory");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, labelDirectory, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, labelDirectory, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(labelDirectory);
		
		textFieldDirectory = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textFieldDirectory, -3, SpringLayout.NORTH, labelDirectory);
		sl_contentPanel.putConstraint(SpringLayout.WEST, textFieldDirectory, 6, SpringLayout.EAST, labelDirectory);
		textFieldDirectory.setEditable(false);
		contentPanel.add(textFieldDirectory);
		textFieldDirectory.setColumns(10);
		
		JButton btnScegliCartella = new JButton("Sfoglia...");
		btnScegliCartella.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				Component parent = SettingsDialog.this;
				int returnVal = chooser.showOpenDialog(parent);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					textFieldDirectory.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.EAST, textFieldDirectory, -11, SpringLayout.WEST, btnScegliCartella);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, btnScegliCartella, -4, SpringLayout.NORTH, labelDirectory);
		sl_contentPanel.putConstraint(SpringLayout.EAST, btnScegliCartella, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(btnScegliCartella);
		
		chckbxSovrascritturaFile = new JCheckBox("Sovrascrivere file esistenti");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, chckbxSovrascritturaFile, 6, SpringLayout.SOUTH, labelDirectory);
		sl_contentPanel.putConstraint(SpringLayout.WEST, chckbxSovrascritturaFile, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(chckbxSovrascritturaFile);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//Salva le caratteristiche in un file di configurazione
						PropertiesManager.setProperty(PropertiesManager.SAVE_PATH_KEY, textFieldDirectory.getText());
						PropertiesManager.setProperty(PropertiesManager.OVERWRITE_FILES, Boolean.toString(chckbxSovrascritturaFile.isSelected()));
						PropertiesManager.storeToXML();
						
						SettingsDialog.this.hide();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SettingsDialog.this.hide();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setDefault();
	}
	
	/**
	 * Setta le impostazioni di default lette dal file.
	 */
	private void setDefault(){
		/* Save path */
		if(PropertiesManager.getProperty(PropertiesManager.SAVE_PATH_KEY) != null){
			textFieldDirectory.setText(PropertiesManager.getProperty(PropertiesManager.SAVE_PATH_KEY));
		}
		
		String directory = PropertiesManager.getProperty(PropertiesManager.SAVE_PATH_KEY);
		
		JRaiLogger.getLogger().log(Level.INFO, "Path: " + directory);
		if(directory != null){
			chooser.setCurrentDirectory(new File(directory));
		}
		
		/* Overwrite files */
		if(PropertiesManager.getProperty(PropertiesManager.OVERWRITE_FILES) != null){
			boolean overwriteOption = Boolean.parseBoolean(PropertiesManager.getProperty(PropertiesManager.OVERWRITE_FILES));
			chckbxSovrascritturaFile.setSelected(overwriteOption);
		}
		
	}
	
	/**
	 * Costruttore con il parent
	 * @param parent il parent del dialog.
	 * @param modal il modal.
	 */
	public SettingsDialog(JFrame parent, boolean modal){
		super(parent, modal);
		init();
	}
}
