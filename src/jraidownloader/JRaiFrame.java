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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import jraidownloader.dialog.SceltaQualita;
import jraidownloader.dialog.SettingsDialog;
import jraidownloader.logging.JRaiLogger;
import jraidownloader.video.Video;
import jraidownloader.video.Videos;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

/**
 * Finestra principale per l'applicazione.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRaiFrame extends JFrame {
	
	/**
	 * L'URL viene analizzato.
	 */
	public static final String ANALISI_URL = "Analisi URL";
	
	/**
	 * La qualità non viene scelta.
	 */
	public static final String QUALITA_NON_SCELTA = "<html><font color='red'>Qualità non scelta</font></html>";
	
	/**
	 * Il download è in corso.
	 */
	public static final String DOWNLOAD_IN_CORSO = "Download in corso";
	
	/**
	 * Il download è stato completato.
	 */
	public static final String DOWNLOAD_COMPLETATO = "<html><font color='green'>Download effettuato con successo</font></html>";
	
	/**
	 * Il download ha provocato un errore.
	 */
	public static final String DOWNLOAD_ERRATO = "<html><font color='red'>Download errato</font></html>";

	private JPanel contentPane;
	private JTextField textFieldUrl;
	private JProgressBar progressBar;
	private JLabel labelStato;
	private JButton submitButton;
	private JMenu settingsMenu;
	private JMenuItem menuItemCustomize;
	private JLabel downloadSpeed;
	
	private Videos videos;

	/**
	 * Create the frame.
	 */
	public JRaiFrame() {
		setTitle("JRaiDownloader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 205);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new TitledBorder(null, "Form", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		textFieldUrl = new JTextField();
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
						try {
							labelStato.setText(JRaiFrame.ANALISI_URL);
							videos = new Videos(textFieldUrl.getText());
							SceltaQualita sceltaQualita = new SceltaQualita(JRaiFrame.this, true, videos);
							sceltaQualita.setAlwaysOnTop(true);
							sceltaQualita.setVisible(true);
							
							if(videos.getVideoScelto() == null){
								throw new Exception("Qualità del video non scelta");
							}
							
							//Video video = videos.getVideoByQuality(videos.getVideoScelto());
							Video video = videos.getVideoScelto();
							
							labelStato.setText(JRaiFrame.DOWNLOAD_IN_CORSO);
							Downloader downloader = new Downloader();
							downloader.downloadFile(video.getUrl(), videos.getNomeProgramma() + ".mp4", videos.getData(), progressBar);
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
							labelStato.setText(JRaiFrame.DOWNLOAD_ERRATO);
						}
						else{
							submitButton.setEnabled(true);
							labelStato.setText(JRaiFrame.DOWNLOAD_COMPLETATO);
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
		sl_contentPane.putConstraint(SpringLayout.SOUTH, progressBar, 33, SpringLayout.SOUTH, submitButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, progressBar, 0, SpringLayout.EAST, textFieldUrl);
		contentPane.add(progressBar);
		
		labelStato = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, labelStato, 4, SpringLayout.NORTH, submitButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, labelStato, -155, SpringLayout.WEST, submitButton);
		contentPane.add(labelStato);
		
		JLabel labelUrl = new JLabel("URL");
		sl_contentPane.putConstraint(SpringLayout.WEST, labelStato, 0, SpringLayout.WEST, labelUrl);
		sl_contentPane.putConstraint(SpringLayout.WEST, progressBar, 0, SpringLayout.WEST, labelUrl);
		sl_contentPane.putConstraint(SpringLayout.NORTH, textFieldUrl, -3, SpringLayout.NORTH, labelUrl);
		sl_contentPane.putConstraint(SpringLayout.WEST, textFieldUrl, 34, SpringLayout.EAST, labelUrl);
		sl_contentPane.putConstraint(SpringLayout.NORTH, labelUrl, 3, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, labelUrl, 26, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, labelUrl, 52, SpringLayout.WEST, contentPane);
		contentPane.add(labelUrl);
		labelUrl.setHorizontalAlignment(SwingConstants.RIGHT);
		labelUrl.setLabelFor(textFieldUrl);
		
		downloadSpeed = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.WEST, downloadSpeed, 0, SpringLayout.WEST, progressBar);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, downloadSpeed, -10, SpringLayout.SOUTH, contentPane);
		contentPane.add(downloadSpeed);
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		settingsMenu = new JMenu("Impostazioni");
		menuBar.add(settingsMenu);
		
		menuItemCustomize = new JMenuItem("Personalizza");
		menuItemCustomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsDialog settingsDialog = new SettingsDialog(JRaiFrame.this, true);
				settingsDialog.setAlwaysOnTop(true);
				settingsDialog.setVisible(true);
			}
		});
		settingsMenu.add(menuItemCustomize);
	}
	
	/**
	 * Create the frame with the title.
	 * @param title il titolo del frame.
	 */
	public JRaiFrame(String title){
		this();
		setTitle(title);
	}
	
	/**
	 * @return the video
	 */
	public Videos getVideo() {
		return videos;
	}

	/**
	 * @param videos the video to set
	 */
	public void setVideo(Videos videos) {
		this.videos = videos;
	}

	/**
	 * Setta il testo della label stato.
	 * @param string il nuovo testo da settare.
	 */
	public void setStateLabel(String stato) {
		this.labelStato.setText(stato);
	}

	/**
	 * Setta la velocità del download.
	 * @param speed la velocità del download.
	 */
	public void setSpeed(String speed) {
		this.downloadSpeed.setText(speed);
	}
}
