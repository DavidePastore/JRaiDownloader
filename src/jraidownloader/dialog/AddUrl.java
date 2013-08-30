/**
 * 
 */
package jraidownloader.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import jraidownloader.JRaiFrame;
import jraidownloader.clipboard.ClipboardUtils;
import jraidownloader.popup.MyPopupMenuListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class AddUrl extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldUrl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddUrl dialog = new AddUrl(JRaiFrame.getInstance());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddUrl(JFrame parent) {
		super(parent, true);
		setTitle("Aggiungi Url");
		setBounds(100, 100, 438, 131);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		{
			textFieldUrl = new JTextField();
			sl_contentPanel.putConstraint(SpringLayout.NORTH, textFieldUrl, 10, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, textFieldUrl, 43, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, textFieldUrl, 415, SpringLayout.WEST, contentPanel);
			contentPanel.add(textFieldUrl);
			textFieldUrl.setColumns(10);
		}
		
		//Popup menu
		JPopupMenu popupMenu = new JPopupMenu("PopupMenuUrl");
		JMenuItem menuItem;
		menuItem = new JMenuItem("Copia");
		menuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ClipboardUtils.setContent(textFieldUrl.getSelectedText());
			}
			
		});
	    popupMenu.add(menuItem);
	    
	    menuItem = new JMenuItem("Incolla");
	    menuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				textFieldUrl.setText(ClipboardUtils.getContent());
			}
	    	
	    });
	    popupMenu.add(menuItem);
	    
	    menuItem = new JMenuItem("Taglia");
	    menuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedText = textFieldUrl.getSelectedText();
				String allText = textFieldUrl.getText();
				ClipboardUtils.setContent(selectedText);
				textFieldUrl.setText("");
			}
	    	
	    });
	    popupMenu.add(menuItem);
	    
		popupMenu.addPopupMenuListener(new MyPopupMenuListener());
		textFieldUrl.setComponentPopupMenu(popupMenu);
		{
			JLabel lblUrl = new JLabel("URL:");
			sl_contentPanel.putConstraint(SpringLayout.NORTH, lblUrl, 3, SpringLayout.NORTH, textFieldUrl);
			sl_contentPanel.putConstraint(SpringLayout.EAST, lblUrl, -6, SpringLayout.WEST, textFieldUrl);
			lblUrl.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblUrl);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JRaiFrame.getInstance().setAddedUrl(textFieldUrl.getText());
						AddUrl.this.hide();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Annulla");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AddUrl.this.hide();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
