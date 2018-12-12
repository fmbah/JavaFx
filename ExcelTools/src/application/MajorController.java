package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MajorController implements Initializable {

	private Stage primaryStage;
	
	@FXML
	private Button chooseFolder;
	@FXML
	private Text chooseFolderText;
	@FXML
	private ProgressIndicator uploadProgress;
	@FXML
	private Button chooseFile;
	@FXML
	private Text chooseFileText;
	@FXML
	private TextField targetDirValue;
	@FXML
	private Button chooseTargetDir;
	@FXML
	private Text finishText;
	@FXML
	private Button openFile;
	@FXML
	private Button againTime;
	@FXML
	private RadioButton batchRadio;
	@FXML
	private RadioButton singleRadio;
	@FXML
	private Button startTransform;
	
	private static final FileChooser fileChooser;
	private static final DirectoryChooser directoryChooser;
	private static final Desktop desktop;
	
	private String targetDir;
	private List<File> list;
	

	static {

		fileChooser = new FileChooser();
		fileChooser.setTitle("选择文件");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ELS EXCEL", "*.xls"),
				new FileChooser.ExtensionFilter("XLSX EXCEL", "*.xlsx"));

		desktop = Desktop.getDesktop();

		directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("选择文件夹");
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Tools.logger.info("MajorController initialize run....");
		
		targetDirValue.setDisable(true);
		targetDirValue.setText(System.getProperty("user.home"));
		this.setTargetDir(System.getProperty("user.home"));
		chooseFolderText.setVisible(false);
		chooseFileText.setVisible(false);
		uploadProgress.setVisible(false);
		finishText.setVisible(false);
		openFile.setVisible(false);
		againTime.setVisible(false);
		batchRadio.setSelected(true);
		chooseFile.setDisable(true);
		startTransform.setVisible(false);
		
	}
	
	public void chooseFolder(ActionEvent event) {
		Tools.logger.info("chooseFolder run....");
		
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
					if (fileName != null && (fileName.endsWith(".xls") || fileName.endsWith(".xlsx") )) {
						fileList.add(file);
					}
				}
			}
			
			this.setList(fileList);
			startTransform.setVisible(true);
			chooseFolderText.setVisible(true);
			chooseFolderText.setText(dir.getAbsolutePath());
		}
	}
	
	public void chooseFile(ActionEvent event) {
		Tools.logger.info("chooseFile run....");
		
		finishText.setVisible(false);
		openFile.setVisible(false);
		againTime.setVisible(false);
		
		List<File> list = fileChooser.showOpenMultipleDialog(this.getPrimaryStage());
		if (list != null && !list.isEmpty()) {
			this.setList(list);
			startTransform.setVisible(true);
			chooseFileText.setText(list.get(0).getAbsolutePath());
			chooseFileText.setVisible(true);
		}
		
	}
	
	public void chooseTargetDir(ActionEvent event) {
		Tools.logger.info("chooseTargetDir run....");
		
		File dir = directoryChooser.showDialog(primaryStage);
		if (dir != null) {
			targetDirValue.setText(dir.getAbsolutePath());
			this.setTargetDir(dir.getAbsolutePath());
		} else {
			targetDirValue.setText(System.getProperty("user.home"));
			this.setTargetDir(System.getProperty("user.home"));
		}
	}
	
	public void openFile(ActionEvent event) {
		Tools.logger.info("openFile run....");
		this.openFile(new File(this.getTargetDir()));
	}
	
	public void processData(List<File> fileList) {
		if (fileList != null && !fileList.isEmpty()) {
			int size = fileList.size();
			if (size != 1) {
				String tmpDirPath = this.getTargetDir();
				
				new Thread() {
					public void run() {
						try {
							uploadProgress.setVisible(true);
							double[] ds = new double[size];
							for (int i = 1; i <= size; i++) {
								ds[i - 1] = (double)i / size;
							}
							for (int i = 0; i < size; i++) {
								List<HashMap> contents = ExcelToolsUtil.readExcel(fileList.get(i).getAbsolutePath());
								
								ExcelToolsUtil.writeExcel(tmpDirPath + File.separator + new Random().nextInt(999) + fileList.get(i).getName(), contents);	
								
								uploadProgress.setProgress(ds[i]);
								
								Thread.sleep(450);
							}
							
						} catch (Exception e) {
							Tools.logger.log(Level.SEVERE, e.getMessage(), e);
						} finally {
							uploadProgress.setVisible(false);
							
							finishText.setVisible(true);
							openFile.setVisible(true);
							againTime.setVisible(true);
							
							if (singleRadio.isSelected()) {
								chooseFolder.setDisable(true);
								chooseFile.setDisable(false);
							} else {
								chooseFolder.setDisable(false);
								chooseFile.setDisable(true);
							}
							
							startTransform.setVisible(false);
						}
					};
				}.start();
				
			} else {
				final String tmpDirPath = this.getTargetDir();
				new Thread() {
					public void run() {
						try {
				    		Tools.logger.info("async run....");
					    	
					    	uploadProgress.setVisible(true);
					    	double[] ds = {0.1, 0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9};
							
					    	for (int i = 0; i < 9; i++) {
								uploadProgress.setProgress(ds[i]);
								Thread.sleep(450);
				    		}
				    		List<HashMap> contents = ExcelToolsUtil.readExcel(fileList.get(0).getAbsolutePath());
							
							ExcelToolsUtil.writeExcel(tmpDirPath + File.separator + new Random().nextInt(999) + fileList.get(0).getName(), contents);
						} catch (Exception e) {
							Tools.logger.log(Level.SEVERE, e.getMessage(), e);
						} finally {
							uploadProgress.setProgress(1.0);
							uploadProgress.setVisible(false);
							
							finishText.setVisible(true);
							openFile.setVisible(true);
							againTime.setVisible(true);
							
							if (singleRadio.isSelected()) {
								chooseFolder.setDisable(true);
								chooseFile.setDisable(false);
							} else {
								chooseFolder.setDisable(false);
								chooseFile.setDisable(true);
							}
							
							startTransform.setVisible(false);
						}
					};
				}.start();
//				Platform.runLater(new Runnable() {
//				    @Override
//				    public void run() {
//				    }
//				});
			}
		}
	}
	
	public void againTime(ActionEvent event) {
		finishText.setVisible(false);
		openFile.setVisible(false);
		againTime.setVisible(false);
		chooseFileText.setVisible(false);
		chooseFolderText.setVisible(false);
	}
	
	public void batchRadio(ActionEvent event) {
		Tools.logger.info("batchRadio run...");
		chooseFile.setDisable(true);
		chooseFileText.setText(null);
		chooseFileText.setVisible(false);
		chooseFolder.setDisable(false);
		chooseFolderText.setText(null);
		chooseFolderText.setVisible(false);
		startTransform.setVisible(false);
		
		finishText.setVisible(false);
		openFile.setVisible(false);
		againTime.setVisible(false);
	}
	
	public void singleRadio(ActionEvent event) {
		Tools.logger.info("singleRadio run...");
		chooseFile.setDisable(false);
		chooseFileText.setText(null);
		chooseFileText.setVisible(false);
		chooseFolder.setDisable(true);
		chooseFolderText.setText(null);
		chooseFolderText.setVisible(false);
		startTransform.setVisible(false);
		
		finishText.setVisible(false);
		openFile.setVisible(false);
		againTime.setVisible(false);
	}
	
	public void startTransform(ActionEvent event) {
		startTransform.setVisible(false);

		Tools.logger.info("startTransform run...");
		processData(this.getList());
	}
	
	
	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			Tools.logger.log(Level.SEVERE, "打开文件异常", ex);
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public List<File> getList() {
		return list;
	}

	public void setList(List<File> list) {
		this.list = list;
	}

}
