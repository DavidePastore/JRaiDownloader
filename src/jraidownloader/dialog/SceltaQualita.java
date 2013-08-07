/**
 * 
 */
package jraidownloader.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import jraidownloader.JRaiFrame;
import jraidownloader.Video;
import jraidownloader.logging.JRaiLogger;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Dialog che chiede all'utente la qualità del video.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class SceltaQualita extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox comboBox = new JComboBox();
	private Video video;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SceltaQualita dialog = new SceltaQualita(null, true, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SceltaQualita(JFrame parent, boolean modal, Video video) {
		super(parent, modal);
		this.video = video;
		init();
	}
	
	private void init(){
		setTitle("Qualit\u00E0 video");
		setBounds(100, 100, 248, 118);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblQualitVideo = new JLabel("Qualit\u00E0 video");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblQualitVideo, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblQualitVideo, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblQualitVideo);
		
		sl_contentPanel.putConstraint(SpringLayout.NORTH, comboBox, -3, SpringLayout.NORTH, lblQualitVideo);
		sl_contentPanel.putConstraint(SpringLayout.WEST, comboBox, 6, SpringLayout.EAST, lblQualitVideo);
		
		//Popolo la combobox
		Map<String, String> urls = video.getUrls();
		Set<String> keys = urls.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext()){
			comboBox.addItem(iterator.next());
		}
		
		contentPanel.add(comboBox);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String qualitaVideo = (String) comboBox.getSelectedItem();
						JRaiFrame jRaiFrame = (JRaiFrame) SceltaQualita.this.getOwner();
						video.setQualitaVideo(qualitaVideo);
						jRaiFrame.setVideo(video);
						SceltaQualita.this.hide();
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
						JRaiFrame jRaiFrame = (JRaiFrame) SceltaQualita.this.getOwner();
						jRaiFrame.setStateLabel(JRaiFrame.QUALITA_NON_SCELTA);
						SceltaQualita.this.hide();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
