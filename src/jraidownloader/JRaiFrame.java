/**
 * 
 */
package jraidownloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import jraidownloader.logging.JRaiLogger;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

/**
 * Finestra principale per l'applicazione.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRaiFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldUrl;
	private JProgressBar progressBar;
	private JLabel labelRisultato;
	private JButton submitButton;

	/**
	 * Create the frame.
	 */
	public JRaiFrame() {
		setTitle("JRaiDownloader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 407, 144);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new TitledBorder(null, "Form", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel labelUrl = new JLabel("URL");
		sl_contentPane.putConstraint(SpringLayout.NORTH, labelUrl, 3, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, labelUrl, 26, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, labelUrl, 52, SpringLayout.WEST, contentPane);
		contentPane.add(labelUrl);
		labelUrl.setHorizontalAlignment(SwingConstants.RIGHT);
		labelUrl.setLabelFor(textFieldUrl);
		
		textFieldUrl = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, textFieldUrl, -3, SpringLayout.NORTH, labelUrl);
		sl_contentPane.putConstraint(SpringLayout.WEST, textFieldUrl, 34, SpringLayout.EAST, labelUrl);
		sl_contentPane.putConstraint(SpringLayout.EAST, textFieldUrl, -10, SpringLayout.EAST, contentPane);
		contentPane.add(textFieldUrl);
		textFieldUrl.setColumns(2);
		
		submitButton = new JButton("Scarica");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
					
					boolean error = false;

					@Override
					protected Void doInBackground() {
						submitButton.setEnabled(false);
						progressBar.setEnabled(true);
						Video video;
						try {
							video = new Video(textFieldUrl.getText());
							String url = video.findBestQualityUrl();
							Downloader downloader = new Downloader();
							downloader.downloadFile(url, video.getNomeProgramma() + ".mp4", progressBar);
						} catch (ClientProtocolException e) {
							JRaiLogger.getLogger().log(Level.SEVERE, "ClientProtocolException: " + e);
							error = true;
						} catch (IOException e) {
							JRaiLogger.getLogger().log(Level.SEVERE, "IOException: " + e);
							error = true;
						} catch (JSONException e) {
							JRaiLogger.getLogger().log(Level.SEVERE, "JSONException: " + e);
							error = true;
						} catch (Exception e) {
							JRaiLogger.getLogger().log(Level.SEVERE, "Exception: " + e);
							error = true;
						}
						return null;
					}
					
					/* (non-Javadoc)
					 * @see javax.swing.SwingWorker#done()
					 */
					@Override
					protected void done() {
						//super.done();
						if(error){
							submitButton.setEnabled(true);
							labelRisultato.setText("<html><font color='red'>Download errato</font></html>");
						}
						else{
							submitButton.setEnabled(true);
							labelRisultato.setText("<html><font color='green'>Download effettuato con successo</font></html>");
						}
					}
					
				};
            	worker.execute();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, submitButton, 10, SpringLayout.SOUTH, textFieldUrl);
		sl_contentPane.putConstraint(SpringLayout.EAST, submitButton, 0, SpringLayout.EAST, textFieldUrl);
		contentPane.add(submitButton);
		
		progressBar = new JProgressBar();
		sl_contentPane.putConstraint(SpringLayout.NORTH, progressBar, 14, SpringLayout.SOUTH, submitButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, progressBar, 0, SpringLayout.WEST, labelUrl);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, progressBar, 33, SpringLayout.SOUTH, submitButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, progressBar, 0, SpringLayout.EAST, textFieldUrl);
		contentPane.add(progressBar);
		
		labelRisultato = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, labelRisultato, 4, SpringLayout.NORTH, submitButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, labelRisultato, 0, SpringLayout.WEST, labelUrl);
		sl_contentPane.putConstraint(SpringLayout.EAST, labelRisultato, -155, SpringLayout.WEST, submitButton);
		contentPane.add(labelRisultato);
	}
	
	/**
	 * Create the frame with the title.
	 * @param title il titolo del frame.
	 */
	public JRaiFrame(String title){
		this();
		setTitle(title);
	}
}
