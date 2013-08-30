/**
 * 
 */
package jraidownloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jraidownloader.dialog.AddUrl;
import jraidownloader.dialog.SettingsDialog;
import jraidownloader.downloader.factory.DownloaderFactory;
import jraidownloader.downloader.utils.DownloaderUtils;
import jraidownloader.logging.JRaiLogger;
import jraidownloader.properties.PropertiesManager;
import jraidownloader.queue.QueueManager;
import jraidownloader.table.JRDTable;
import jraidownloader.table.JRDTableModel;
import jraidownloader.table.ProgressRenderer;
import jraidownloader.video.Videos;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

/**
 * Finestra principale per l'applicazione.
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRaiFrame extends JFrame {
	
	/**
	 * Singleton class.
	 */
	private static JRaiFrame jRaiFrame;
	
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
	private JMenu settingsMenu;
	private JMenuItem menuItemCustomize;
	private JSpinner spinnerMaxDownload;
	
	private Videos videos;
	private JRDTable downloadTable;
	
	private String addedUrl;

	/**
	 * Create the frame.
	 */
	private JRaiFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//Salva le caratteristiche in un file di configurazione
				PropertiesManager.setProperty(PropertiesManager.MAX_NO_OF_SIMULTANEOUS_DOWNLOADS, spinnerMaxDownload.getValue().toString());
				PropertiesManager.storeToXML();
			}
		});
		setTitle("JRaiDownloader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 542, 341);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new TitledBorder(null, "Form", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		downloadTable = new JRDTable();
		/*sl_contentPane.putConstraint(SpringLayout.NORTH, downloadTable, -11, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, downloadTable, -11, SpringLayout.NORTH, textFieldUrl);*/
		
		/*sl_contentPane.putConstraint(SpringLayout.WEST, downloadTable, 0, SpringLayout.WEST, textFieldUrl);
		sl_contentPane.putConstraint(SpringLayout.EAST, downloadTable, -488, SpringLayout.EAST, contentPane);*/
		downloadTable.setModel(JRDTableModel.getInstance());
		ProgressRenderer progressRenderer = new ProgressRenderer(0, 100);
		progressRenderer.setStringPainted(true); //Show Progress text
		downloadTable.setDefaultRenderer(JProgressBar.class, progressRenderer);
		downloadTable.setRowHeight((int)progressRenderer.getPreferredSize().getHeight());
		
		JScrollPane jScrollPane = new JScrollPane(downloadTable);
		sl_contentPane.putConstraint(SpringLayout.NORTH, jScrollPane, 9, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, jScrollPane, 24, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, jScrollPane, -10, SpringLayout.EAST, contentPane);
		contentPane.add(jScrollPane);
		
		spinnerMaxDownload = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.SOUTH, jScrollPane, -6, SpringLayout.NORTH, spinnerMaxDownload);
		spinnerMaxDownload.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int maxNoOfDownloads = Integer.parseInt(spinnerMaxDownload.getValue() + "");

		        // set this value to queuemanager's variable
		        QueueManager.getInstance().setMaxNoOfDownloads(maxNoOfDownloads);

		        //Update the queuing sequence so that more uploads may be started.
		        QueueManager.getInstance().updateQueue();
			}
		});
		
		String maxNoSimDownloads = PropertiesManager.getProperty(PropertiesManager.MAX_NO_OF_SIMULTANEOUS_DOWNLOADS);
		QueueManager.getInstance().setMaxNoOfDownloads(Integer.parseInt(maxNoSimDownloads));
		spinnerMaxDownload.setModel(new SpinnerNumberModel(Integer.parseInt(maxNoSimDownloads), 1, null, 1));
		contentPane.add(spinnerMaxDownload);
		
		JLabel lblSimultaneousDownloads = new JLabel("Max n. di download simultanei:");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblSimultaneousDownloads, 26, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, spinnerMaxDownload, -3, SpringLayout.NORTH, lblSimultaneousDownloads);
		sl_contentPane.putConstraint(SpringLayout.WEST, spinnerMaxDownload, 6, SpringLayout.EAST, lblSimultaneousDownloads);
		sl_contentPane.putConstraint(SpringLayout.EAST, spinnerMaxDownload, 50, SpringLayout.EAST, lblSimultaneousDownloads);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblSimultaneousDownloads, -3, SpringLayout.SOUTH, contentPane);
		contentPane.add(lblSimultaneousDownloads);
		
		JButton btnAddUrl = new JButton("Aggiungi Url");
		sl_contentPane.putConstraint(SpringLayout.EAST, btnAddUrl, -10, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnAddUrl, 0, SpringLayout.SOUTH, spinnerMaxDownload);
		btnAddUrl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddUrl addUrl = new AddUrl(JRaiFrame.this);
				addUrl.setAlwaysOnTop(true);
				addUrl.setVisible(true);
				
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
					
					String downloaderName;

					@Override
					protected Void doInBackground() {
						try {
							if(addedUrl != null){
								downloaderName = DownloaderUtils.getDownloaderName(addedUrl);
								
								if(downloaderName == null){
									throw new Exception("Url non corrispondente a nessun downloader.");
								}
								
								JRDTableModel.getInstance().addDownload(DownloaderFactory.createDownloader(downloaderName, addedUrl));
								QueueManager.getInstance().setQueueLock(false);
								QueueManager.getInstance().setStopFurther(false);
								addedUrl = null;
							}
						} catch (ClientProtocolException e) {
							JRaiLogger.getLogger().log(Level.SEVERE, "ClientProtocolException: " + e);
						} catch (IOException e) {
							JRaiLogger.getLogger().log(Level.SEVERE, "IOException: " + e);
						} catch (JSONException e) {
							JRaiLogger.getLogger().log(Level.SEVERE, "JSONException: " + e);
						} catch (Exception e) {
							JRaiLogger.getLogger().log(Level.SEVERE, "Exception: " + e);
						}
						return null;
					}
					
					/* (non-Javadoc)
					 * @see javax.swing.SwingWorker#done()
					 */
					@Override
					protected void done() {
						//super.done();
						//submitButton.setEnabled(true);
					}
					
				};
            	worker.execute();
			}
		});
		contentPane.add(btnAddUrl);
		
		
		
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
	private JRaiFrame(String title){
		this();
		setTitle(title);
	}
	
	/**
	 * Return this instance.
	 * @return the JRaiFrame instance.
	 */
	public static JRaiFrame getInstance(){
		if(jRaiFrame == null){
			jRaiFrame = new JRaiFrame();
		}
		return jRaiFrame;
	}
	
	/**
	 * @return the video
	 */
	public Videos getVideo() {
		return videos;
	}
	
	/**
	 * @return the table model
	 */
	public JRDTableModel getModel() {
		return (JRDTableModel) this.downloadTable.getModel();
	}
	
	/**
	 * @return the table
	 */
	public JTable getTable() {
		return this.downloadTable;
	}
	
	/**
	 * Set the added url.
	 * @param addedUrl the addedUrl to set
	 */
	public void setAddedUrl(String addedUrl) {
		this.addedUrl = addedUrl;
	}
	
	/**
	 * Set the the table model.
	 * @param model the model to set for the table.
	 */
	public void setModel(JRDTableModel model){
		this.downloadTable.setModel(model);
	}
	
	/**
	 * @param videos the video to set
	 */
	public void setVideo(Videos videos) {
		this.videos = videos;
	}
}
