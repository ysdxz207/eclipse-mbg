package com.puyixiaowo.eclipsembg.dialog.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.internal.JDBCConnectionFactory;

import com.puyixiaowo.eclipsembg.constants.Constant;
import com.puyixiaowo.eclipsembg.enums.ClassPathEntryEnum;
import com.puyixiaowo.eclipsembg.enums.JdbcConnectionEnum;
import com.puyixiaowo.eclipsembg.model.ClassPathEntry;
import com.puyixiaowo.eclipsembg.model.Context;
import com.puyixiaowo.eclipsembg.model.GeneratorConfig;
import com.puyixiaowo.eclipsembg.model.JdbcConnection;
import com.puyixiaowo.eclipsembg.util.GeneratorConfUtil;

public class NewDBConnectionDialogHandler {

	private String[] extensions = { "*.jar" };
	private Shell shell;
	
	private Label driverClassLabel;
	private Label urlLabel;
	private Label driverPathLabel;
	private Label usernameLabel;
	private Label passwordLabel;
	
	private Text driverClassText;
	private Text urlText;
	private Text driverPathText;
	private Text usernameText;
	private Text passwordText;

	private Button chooseDriverBtn;
	private Button testUrlBtn;
	private Button createBtn;
	private FileDialog fileDialog;
	
	private JDBCConnectionConfiguration config;

	public NewDBConnectionDialogHandler(Shell shell) {
		this.shell = shell;
		fill();
	}

	private void fill() {
		init();
	}

	private void init() {
		
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 14;
		gridLayout.marginTop = 12;
		gridLayout.marginBottom = 12;
		gridLayout.marginLeft = 12;
		gridLayout.marginRight = 12;
		shell.setLayout(gridLayout);
		shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		//-----driverClass
		driverClassLabel = new Label(shell, SWT.NULL);
		driverClassLabel.setText("driverClass:");
		driverClassText = new Text(shell, SWT.BORDER);
		driverClassText.setLayoutData(getGridData(SWT.FILL, 2));
		driverClassText.setText(Constant.defaultConfig.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.DRIVER_CLASS.name));
		//-----url
		urlLabel = new Label(shell, SWT.NULL);
		urlLabel.setText("url:");
		urlText = new Text(shell, SWT.BORDER);
		urlText.setLayoutData(getGridData(SWT.FILL, 2));
		urlText.setText(Constant.defaultConfig.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.CONNECTION_URL.name));
		
		//-----driver
		driverPathLabel = new Label(shell, SWT.NULL);
		driverPathLabel.setText("DB driver:");
		driverPathText = new Text(shell, SWT.BORDER);
		driverPathText.setLayoutData(getGridData(SWT.FILL, 1));
		driverPathText.setText(Constant.defaultConfig.getClassPathEntry().getProperty(ClassPathEntryEnum.LOCATION.name));
		chooseDriverBtn = new Button(shell, SWT.NULL);
		chooseDriverBtn.setText("choose driver...");
		chooseDriverBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// open choose file dialog
				driverPathText.setText(fileDialog.open());
			}
		});

		fileDialog = new FileDialog(shell);
		fileDialog.setText("choose jdbc dirver");
		fileDialog.setFilterExtensions(extensions);
		// -----username
		usernameLabel = new Label(shell, SWT.NULL);
		usernameLabel.setText("username:");
		usernameText = new Text(shell, SWT.BORDER);
		usernameText.setLayoutData(getGridData(SWT.FILL, 2));
		
		// -----password
		passwordLabel = new Label(shell, SWT.NULL);
		passwordLabel.setText("password:");
		passwordText = new Text(shell, SWT.BORDER);
		passwordText.setLayoutData(getGridData(SWT.FILL, 2));
		
		//-----test url btn
		testUrlBtn = new Button(shell, SWT.PUSH);
		testUrlBtn.setText("test connection");
		GridData testUrlBtnGrid = getGridDataBottom(SWT.RIGHT, 2);
		testUrlBtnGrid.widthHint = 100;
		testUrlBtn.setLayoutData(testUrlBtnGrid);
		testUrlBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				testJDBCConnection(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//System.out.println("default selected");
			}
		});
		//-----create btn
		createBtn = new Button(shell, SWT.PUSH);
		createBtn.setText("create");
		GridData createBtnGrid = getGridDataBottom(SWT.RIGHT, 1);
		createBtnGrid.widthHint = 90;
		createBtn.setLayoutData(createBtnGrid);
		createBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				createJDBCConnection();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//System.out.println("default selected");
			}
		});
		
		usernameText.setText(
				Constant.defaultConfig.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.USER_ID.name));

		passwordText.setText(
				Constant.defaultConfig.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.PASSWORD.name));
	}
	/**
	 * test jdbc connection
	 */
	protected boolean testJDBCConnection(boolean isAlert) {
		boolean flag = false;
		String connectionURL = urlText.getText();
		String driverClass = driverClassText.getText();
		String userId = usernameText.getText();
		String password = passwordText.getText();
		String location = driverPathText.getText();
		
		config = new JDBCConnectionConfiguration();
		config.setConnectionURL(connectionURL);
		config.setDriverClass(driverClass);
		config.setUserId(userId);
		config.setPassword(password);
		config.addProperty(ClassPathEntryEnum.LOCATION.name, location);
		
		JDBCConnectionFactory f = new JDBCConnectionFactory(config);
		Connection conn = null;
		try {
			conn = f.getConnection();
		} catch (SQLException e) {
			MessageDialog.openError(shell, "test jdbc connection", "connection error:" + e.getMessage());
		}
		
		if (conn != null) {
			if (isAlert) {
				MessageDialog.openInformation(shell, "test jdbc connection", "connection success !");
			}
			flag = true;
		}
		return flag;
		
	}

	/**
	 * create jdbc connection
	 */
	protected void createJDBCConnection() {
		if (!testJDBCConnection(false)) {
			return;
		}
		//save config info to generatorConfig.xml
		GeneratorConfig generatorConfig = new GeneratorConfig();
		Properties props = new Properties();
		props.setProperty(JdbcConnectionEnum.CONNECTION_URL.name, config.getConnectionURL());
		props.setProperty(JdbcConnectionEnum.DRIVER_CLASS.name, config.getDriverClass());
		props.setProperty(JdbcConnectionEnum.USER_ID.name, config.getUserId());
		props.setProperty(JdbcConnectionEnum.PASSWORD.name, config.getPassword());
		
		JdbcConnection jdbcConnection = new JdbcConnection(props);
		Context context = new Context();
		context.setJdbcConnection(jdbcConnection);
		generatorConfig.setContext(context);
		
		//driver class path
		ClassPathEntry classPathEntry = new ClassPathEntry();
		classPathEntry.addProperty(ClassPathEntryEnum.LOCATION.name, config.getProperty(ClassPathEntryEnum.LOCATION.name));
		generatorConfig.setClassPathEntry(classPathEntry);
		GeneratorConfUtil.updateDefaultConfigFile(generatorConfig);
		shell.dispose();//close shell
	}
	/**
	 * 
	 * @param swt
	 * 			SWT.LEFT or SWT.RIGHT
	 * @param i
	 * @return
	 */
	public GridData getGridDataBottom(int swt, int i){
		GridData gridData = new GridData(swt, SWT.BOTTOM, false, true, i, 1);
		gridData.horizontalSpan = i;
		return gridData;
	}
	
	public GridData getGridData(int swt, int i){
		GridData gridData = new GridData(swt, SWT.NONE, true, false);
		gridData.horizontalSpan = i;
		return gridData;
	}
}
