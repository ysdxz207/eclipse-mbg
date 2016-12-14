package com.puyixiaowo.eclipsembg.dialog.handler;

import javax.swing.GroupLayout.Group;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.puyixiaowo.eclipsembg.constants.Constant;
import com.puyixiaowo.eclipsembg.enums.JavaTypeResolverEnum;

public class NewConfigDialogHandler {

	private Shell shell;
	
	private Label fileNameLabel;
	private Label forceBigDecimalsLabel;
	private Label modelLabel;
	private Label modelTargetPackageLabel;
	private Label modelTargetProjectLabel;
	private Group modelGroup;
	
	private Text fileNameText;
	private Text forceBigDecimalsText;
	
	private Text modelTargetPackageText;
	private Text modelTargetProjectText;

	private Button testUrlBtn;
	private Button createBtn;
	
	public NewConfigDialogHandler(Shell shell) {
		this.shell = shell;
		fill();
	}

	private void fill() {
		init();
	}

	private void init() {
		
		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 14;
		gridLayout.marginTop = 12;
		gridLayout.marginBottom = 12;
		gridLayout.marginLeft = 12;
		gridLayout.marginRight = 12;
		shell.setLayout(gridLayout);
		shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		//-----file name
		fileNameLabel = new Label(shell, SWT.NULL);
		fileNameLabel.setText("file name:");
		fileNameText = new Text(shell, SWT.BORDER);
		fileNameText.setLayoutData(getGridData(SWT.FILL, 2));
		fileNameText.setText(Constant.defaultConfig.getFileName());
		//-----force big decimals
		forceBigDecimalsLabel = new Label(shell, SWT.NULL);
		forceBigDecimalsLabel.setText("force big decimals:");
		forceBigDecimalsText = new Text(shell, SWT.BORDER);
		forceBigDecimalsText.setLayoutData(getGridData(SWT.FILL, 2));
		forceBigDecimalsText.setText(Constant.defaultConfig.getContext().getJavaTypeResolver().getProperty(JavaTypeResolverEnum.FORCE_BIG_DECIMALS.name));
		
		// -----model
		modelLabel = new Label(shell, SWT.NULL);
		modelLabel.setText("modal:");
		modelTargetPackageLabel = new Label(shell, SWT.NULL);
		modelTargetPackageLabel.setText("target package:");
		modelTargetPackageText = new Text(shell, SWT.BORDER);
		modelTargetPackageText.setLayoutData(getGridData(SWT.FILL, 1));
		modelTargetProjectLabel = new Label(shell, SWT.NULL);
		modelTargetProjectLabel.setText("target project:");
		modelTargetProjectText = new Text(shell, SWT.BORDER);
		modelTargetProjectText.setLayoutData(getGridData(SWT.FILL, 1));
		
		//-----test url btn
		testUrlBtn = new Button(shell, SWT.PUSH);
		testUrlBtn.setText("test connection");
		GridData testUrlBtnGrid = getGridDataBottom(SWT.RIGHT, 2);
		testUrlBtnGrid.widthHint = 100;
		testUrlBtn.setLayoutData(testUrlBtnGrid);
		testUrlBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
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
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//System.out.println("default selected");
			}
		});
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
