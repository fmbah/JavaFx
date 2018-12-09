package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MyController implements Initializable {

	private Stage primaryStage;
	private String targetDir;

	@FXML
	private Button myButton;

	@FXML
	private TextField myTextField;

	@FXML
	private Button allTransform;
	@FXML
	private Button singleTransform;
	@FXML
	private TextField targetDirValue;
	@FXML
	private ProgressIndicator pi0;
	@FXML
	private Text finishText;
	@FXML
	private Button openFile;
	@FXML
	private Button againTime;

	private static final FileChooser fileChooser;
	private static final DirectoryChooser directoryChooser;
	private static final Desktop desktop;
	

	static {

		fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ELS EXCEL", "*.xls"));

		desktop = Desktop.getDesktop();

		directoryChooser = new DirectoryChooser();
		// Set title for DirectoryChooser
		directoryChooser.setTitle("Select Some Directories");
		// Set Initial Directory
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO (don't really need to do anything here).
		pi0.setVisible(false);
		targetDirValue.setText(System.getProperty("user.home"));
		this.setTargetDir(targetDirValue.getText());
		
		finishText.setVisible(false);
		openFile.setVisible(false);
		againTime.setVisible(false);
		againTime.setDisable(true);
	}

	// When user click on myButton
	// this method will be called.
	public void showDateTime(ActionEvent event) {
		Main.logger.info("showDateTime clicked....");

		Date now = new Date();

		DateFormat df = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
		String dateTimeString = df.format(now);
		Main.logger.info(dateTimeString);
		// Show in VIEW
		myTextField.setText(dateTimeString);

	}

	public void allTransformService(ActionEvent event) {
		Main.logger.info("allTransformService run....");
		finishText.setVisible(false);
		openFile.setVisible(false);
		againTime.setVisible(false);

		File dir = directoryChooser.showDialog(primaryStage);
		if (dir != null) {
			String folderPath = dir.getAbsolutePath();
			File root = new File(folderPath);
			List<File> fileList = new ArrayList<>();
			for (File file : root.listFiles()) {
				if (file.isFile() && file.getName().length() > 3) {
					String fileName = file.getName();
					String subfix = fileName.substring(fileName.length() - 3);
					if (subfix != null && "xls".equals(subfix)) {
						fileList.add(file);
					}
				}
			}
			
			processData(fileList);
			
		} 
	}

	public void singleTransformService(ActionEvent event) {
		Main.logger.info("singleTransformService run....");
		finishText.setVisible(false);
		openFile.setVisible(false);
		againTime.setVisible(false);
		List<File> list = fileChooser.showOpenMultipleDialog(this.getPrimaryStage());
		if (list != null && !list.isEmpty()) {
			processData(list);
		}
	}
	
	public void processData(List<File> fileList) {
		if (fileList != null && !fileList.isEmpty()) {
			int size = fileList.size();
			if (size != 1) {
				final String tmpDirPath = this.getTargetDir();
				new Thread() {
					public void run() {
						try {
							pi0.setVisible(true);
							double[] ds = new double[size];
							for (int i = 1; i <= size; i++) {
								ds[i - 1] = (double)i / size;
							}
							for (int i = 0; i < size; i++) {
								List<HashMap> contents = ExcelToolsUtil.readExcel(fileList.get(i).getAbsolutePath());
								
								ExcelToolsUtil.writeExcel(tmpDirPath + File.separator + new Random().nextInt(999) + fileList.get(i).getName(), contents);	
								
								pi0.setProgress(ds[i]);
								
								Thread.sleep(450);
							}
							
						} catch (Exception e) {
							Main.logger.log(Level.SEVERE, e.getMessage(), e);
						} finally {
							pi0.setVisible(false);
							
							finishText.setVisible(true);
							openFile.setVisible(true);
							againTime.setVisible(true);
						}
					};
				}.start();
				
				
			} else {
				final String tmpDirPath = this.getTargetDir();
					new Thread() {
						@SuppressWarnings({ "static-access", "resource" })
						@Override
						public void run() {
							try {
								pi0.setVisible(true);
								double[] ds = {0.1, 0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9};
								for (int i = 0; i < 9; i++) {
									pi0.setProgress(ds[i]);
									Thread.currentThread().sleep(750);
								}
								
								List<HashMap> contents = ExcelToolsUtil.readExcel(fileList.get(0).getAbsolutePath());
								
								ExcelToolsUtil.writeExcel(tmpDirPath + File.separator + new Random().nextInt(999) + fileList.get(0).getName(), contents);
								
							} catch (Exception e) {
								Main.logger.log(Level.SEVERE, e.getMessage(), e);
							} finally {
								pi0.setProgress(1.0);
								pi0.setVisible(false);
								
								finishText.setVisible(true);
								openFile.setVisible(true);
								againTime.setVisible(true);
							}
						}
						
					}.start();
			}
		}
	}

	public void disbaleSingle(ActionEvent event) {
		singleTransform.setDisable(true);
		allTransform.setDisable(false);
	}

	public void disableAll(ActionEvent event) {
		allTransform.setDisable(true);
		singleTransform.setDisable(false);
	}

	public void targetDir(ActionEvent event) {
		Main.logger.info("targetDir run.....");
		File dir = directoryChooser.showDialog(primaryStage);
		if (dir != null) {
			targetDirValue.setText(dir.getAbsolutePath());
			this.setTargetDir(dir.getAbsolutePath());
		} else {
			targetDirValue.setText(null);
			targetDirValue.setText(System.getProperty("user.home"));
		}
	}
	
	public void openFile(ActionEvent event) {
		this.openFile(new File(this.getTargetDir()));
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			Main.logger.log(Level.SEVERE, "打开文件异常", ex);
		}
		
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

}