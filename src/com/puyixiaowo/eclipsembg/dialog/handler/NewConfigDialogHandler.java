package com.puyixiaowo.eclipsembg.dialog.handler;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.puyixiaowo.eclipsembg.constants.Constant;
import com.puyixiaowo.eclipsembg.enums.JavaTypeResolverEnum;

public class NewConfigDialogHandler {

	private Shell shell;
	// filename
	private Label fileNameLabel;
	private Text fileNameText;
	// force big decimals
	private Label forceBigDecimalsLabel;
	private Text forceBigDecimalsText;
	// model
	private Label modelTargetPackageLabel;
	private Label modelTargetProjectLabel;
	private Text modelTargetPackageText;
	private Text modelTargetProjectText;
	// sql map
	private Label sqlMapTargetPackageLabel;
	private Label sqlMapTargetProjectLabel;
	private Text sqlMapTargetPackageText;
	private Text sqlMapTargetProjectText;
	// java client
	private Label javaClientTargetPackageLabel;
	private Label javaClientTargetProjectLabel;
	private Text javaClientTargetPackageText;
	private Text javaClientTargetProjectText;

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
		// -----file name
		fileNameLabel = new Label(shell, SWT.NULL);
		fileNameLabel.setText("file name:");
		fileNameText = new Text(shell, SWT.BORDER);
		fileNameText.setLayoutData(getGridData(SWT.FILL, 2));
		fileNameText.setText(Constant.defaultConfig.getFileName());
		// -----force big decimals
		forceBigDecimalsLabel = new Label(shell, SWT.NULL);
		forceBigDecimalsLabel.setText("force big decimals:");
		forceBigDecimalsText = new Text(shell, SWT.BORDER);
		forceBigDecimalsText.setLayoutData(getGridData(SWT.FILL, 2));
		forceBigDecimalsText.setText(Constant.defaultConfig.getContext().getJavaTypeResolver()
				.getProperty(JavaTypeResolverEnum.FORCE_BIG_DECIMALS.name));

		// -----model
		Group modelGroup = newGroup("model", 6);
		modelTargetPackageLabel = new Label(modelGroup, SWT.NULL);
		modelTargetPackageLabel.setText("target package:");
		modelTargetPackageText = new Text(modelGroup, SWT.BORDER);
		modelTargetPackageText.setLayoutData(getGridData(SWT.FILL, 1));
		modelTargetProjectLabel = new Label(modelGroup, SWT.NULL);
		modelTargetProjectLabel.setText("target project:");
		modelTargetProjectText = new Text(modelGroup, SWT.BORDER);
		modelTargetProjectText.setLayoutData(getGridData(SWT.FILL, 1));

		// -----sql map
		Group sqlMapGroup = newGroup("sql map(*Mapper.xml)", 6);
		sqlMapTargetPackageLabel = new Label(sqlMapGroup, SWT.NULL);
		sqlMapTargetPackageLabel.setText("target package:");
		sqlMapTargetPackageText = new Text(sqlMapGroup, SWT.BORDER);
		sqlMapTargetPackageText.setLayoutData(getGridData(SWT.FILL, 1));
		sqlMapTargetProjectLabel = new Label(sqlMapGroup, SWT.NULL);
		sqlMapTargetProjectLabel.setText("target project:");
		sqlMapTargetProjectText = new Text(sqlMapGroup, SWT.BORDER);
		sqlMapTargetProjectText.setLayoutData(getGridData(SWT.FILL, 1));

		// -----java client
		Group javaClientGroup = newGroup("java client(*Mapper.java)", 6);
		javaClientTargetPackageLabel = new Label(javaClientGroup, SWT.NULL);
		javaClientTargetPackageLabel.setText("target package:");
		javaClientTargetPackageText = new Text(javaClientGroup, SWT.BORDER);
		javaClientTargetPackageText.setLayoutData(getGridData(SWT.FILL, 1));
		javaClientTargetProjectLabel = new Label(javaClientGroup, SWT.NULL);
		javaClientTargetProjectLabel.setText("target project:");
		javaClientTargetProjectText = new Text(javaClientGroup, SWT.BORDER);
		javaClientTargetProjectText.setLayoutData(getGridData(SWT.FILL, 1));

		// -----tables
		Group tableGroup = newGroup("tables", 6);
		final List<com.puyixiaowo.eclipsembg.model.Table> defaultTables = Constant.defaultConfig.getContext()
				.getTables();

		final Table table = new Table(tableGroup,
				SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.BORDER | SWT.CHECK | SWT.MULTI);
		table.setLayout(new GridLayout(6, false));
		
		GridData gdTable = new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1);
		gdTable.heightHint = 200;
		
		table.setLayoutData(gdTable);
		//table data
		for (int i = 0; i < defaultTables.size(); i++) {
			String tableName = defaultTables.get(i).getProperty("tableName");
			TableItem item = new TableItem(table, SWT.NULL);
			
			item.setText(tableName);
		}

		// edit table
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = table.getItem(event.index);
				System.out.println(item.getText());
			}
		});
		// context menu
		table.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				makeContextMenu(table);
			}
		});

		//

		// -----test url btn
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
				// System.out.println("default selected");
			}
		});
		// -----create btn
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
				// System.out.println("default selected");
			}
		});
	}

	/**
	 * nake a table context menu
	 * 
	 * @param table
	 */
	public void makeContextMenu(final Table table) {
		final Action browse = new Action("checked") {

			@Override
			public void run() {
				super.run();
				TableItem[] items = table.getSelection();
				for (TableItem item : items) {
					item.setChecked(true);
				}
			}

		};
		MenuManager menuMgr = new MenuManager(null);
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(browse);
				manager.add(new Separator());
			}
		});
		Menu menu = menuMgr.createContextMenu(table);
		table.setMenu(menu);
	}

	/**
	 * 
	 * @param swt
	 *            SWT.LEFT or SWT.RIGHT
	 * @param i
	 * @return
	 */
	public GridData getGridDataBottom(int swt, int i) {
		GridData gridData = new GridData(swt, SWT.BOTTOM, false, true, i, 1);
		gridData.horizontalSpan = i;
		return gridData;
	}

	public GridData getGridData(int swt, int i) {
		GridData gridData = new GridData(swt, SWT.NONE, true, false);
		gridData.horizontalSpan = i;
		return gridData;
	}

	/**
	 * 创建group
	 * 
	 * @param text
	 * @param columns
	 * @return
	 */
	public Group newGroup(String text, int columns) {
		Display display = shell.getDisplay();
		Group group = new Group(shell, SWT.SHADOW_NONE);

		group.setFont(new Font(display, "Consolas", 10, SWT.BOLD));
		group.setText(text);
		group.setEnabled(true);
		group.setLayout(new GridLayout(columns, false));
		group.setLayoutData(getGridData(SWT.FILL, columns));

		return group;
	}
}
