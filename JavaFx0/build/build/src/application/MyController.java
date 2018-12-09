package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;

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
	}

	// When user click on myButton
	// this method will be called.
	public void showDateTime(ActionEvent event) {
		System.out.println("Button Clicked!");

		Date now = new Date();

		DateFormat df = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
		String dateTimeString = df.format(now);
		System.out.println(dateTimeString);
		// Show in VIEW
		myTextField.setText(dateTimeString);

	}

	public void allTransformService(ActionEvent event) {
		System.out.println("allTransformService run....");

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
						System.out.println(subfix);
						fileList.add(file);
					}
				}
			}
			
			if (fileList != null && !fileList.isEmpty()) {
				int size = fileList.size();
				if (size != 1) {
					
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
									
//									HSSFWorkbook wb = new HSSFWorkbook();
//									HSSFSheet sheet = wb.createSheet("data_sheet");
//									HSSFRow row = sheet.createRow(0);
//									HSSFCellStyle  style = wb.createCellStyle();
//									style.setAlignment(HorizontalAlignment.CENTER);
//									
//									HSSFCell cell = row.createCell(0);
//									cell.setCellValue("序号");
//									cell.setCellStyle(style);
//									
//									HSSFCell cell1 = row.createCell(1);
//									cell1.setCellValue("账号");
//									cell1.setCellStyle(style);
//									
//									HSSFCell cell2 = row.createCell(2);
//									cell2.setCellValue("户名");
//									cell2.setCellStyle(style);
//									
//									HSSFCell cell3 = row.createCell(3);
//									cell3.setCellValue("金额");
//									cell3.setCellStyle(style);
//									
//									FileOutputStream fileOutputStream = new FileOutputStream(tmpDirPath + File.separator + fileList.get(0).getName());
//									wb.write(fileOutputStream);
//									fileOutputStream.close();
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									pi0.setVisible(false);
								}
							}
							
						}.start();
				}
			}
			
		} 
	}

	public void singleTransformService(ActionEvent event) {
		System.out.println("singleTransformService run....");
		List<File> list = fileChooser.showOpenMultipleDialog(this.getPrimaryStage());
		if (list != null) {
			for (File file : list) {
//               openFile(file);
				System.out.println("file path" + file.getAbsolutePath());
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
		System.out.println("targetDir run.....");
		Logger.getLogger(MyController.class.getName()).log(Level.INFO, null, "targetDir run.....");
		File dir = directoryChooser.showDialog(primaryStage);
		if (dir != null) {
			targetDirValue.setText(dir.getAbsolutePath());
			this.setTargetDir(dir.getAbsolutePath());
		} else {
			targetDirValue.setText(null);
			targetDirValue.setText(System.getProperty("user.home"));
		}
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
			Logger.getLogger(MyController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

}