package com.puyixiaowo.eclipsembg.dialog.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.puyixiaowo.eclipsembg.enums.ClassPathEntryEnum;
import com.puyixiaowo.eclipsembg.enums.ContextEnum;
import com.puyixiaowo.eclipsembg.enums.JavaClientGeneratorEnum;
import com.puyixiaowo.eclipsembg.enums.JavaModelGeneratorEnum;
import com.puyixiaowo.eclipsembg.enums.SqlMapGeneratorEnum;
import com.puyixiaowo.eclipsembg.enums.TableEnum;
import com.puyixiaowo.eclipsembg.model.ClassPathEntry;
import com.puyixiaowo.eclipsembg.model.Context;
import com.puyixiaowo.eclipsembg.model.GeneratorConfig;
import com.puyixiaowo.eclipsembg.model.JavaClientGenerator;
import com.puyixiaowo.eclipsembg.model.JavaModelGenerator;
import com.puyixiaowo.eclipsembg.model.SqlMapGenerator;
import com.puyixiaowo.eclipsembg.util.GeneratorConfigUtil;
import com.puyixiaowo.eclipsembg.views.GeneratorConfView;

public class NewConfigDialogHandler {

	private Shell shell;
	private boolean isNew = false;
	private GeneratorConfig config;
	// filename
	private Label fileNameLabel;
	private Text fileNameText;
	// force big decimals
	// private Label forceBigDecimalsLabel;
	// private Text forceBigDecimalsText;
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
	//table
	private Table table;
	// table edit
	private Label tableNameLabel;
	private Text tableNameText;
	private Label domainObjectNameLable;
	private Text domainObjectNameText;
	private Button enableCountByExampleRadio1;
	private Button enableCountByExampleRadio0;
	private Button enableUpdateByExampleRadio1;
	private Button enableUpdateByExampleRadio0;
	private Button enableDeleteByExampleRadio1;
	private Button enableDeleteByExampleRadio0;
	private Button enableSelectByExampleRadio1;
	private Button enableSelectByExampleRadio0;
	private Button selectByExampleQueryIdRadio1;
	private Button selectByExampleQueryIdRadio0;

	private Button testUrlBtn;
	private Button createBtn;

	public NewConfigDialogHandler(Shell shell, GeneratorConfig config) {
		this.shell = shell;
		this.config = config;

		fill();
	}

	private void fill() {
		init();
	}

	private void init() {

		if (config == null) {
			isNew = true;
			config = GeneratorConfigUtil.getDefaultConfig();
		}

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
		fileNameText.setText(config.getFileName());
		if (!isNew) {
			fileNameText.setEnabled(false);
		}
		// -----force big decimals
		// forceBigDecimalsLabel = new Label(shell, SWT.NULL);
		// forceBigDecimalsLabel.setText("force big decimals:");
		// forceBigDecimalsText = new Text(shell, SWT.BORDER);
		// forceBigDecimalsText.setLayoutData(getGridData(SWT.FILL, 2));
		// forceBigDecimalsText.setText(
		// config.getContext().getJavaTypeResolver().getProperty(JavaTypeResolverEnum.FORCE_BIG_DECIMALS.name));

		// -----model
		Group modelGroup = newGroup("model", 6);
		modelTargetPackageLabel = new Label(modelGroup, SWT.NULL);
		modelTargetPackageLabel.setText("target package:");
		modelTargetPackageText = new Text(modelGroup, SWT.BORDER);
		modelTargetPackageText.setLayoutData(getGridData(SWT.FILL, 1));
		modelTargetPackageText.setText(
				config.getContext().getJavaModelGenerator().getProperty(JavaModelGeneratorEnum.TARGET_PACKAGE.name));
		modelTargetProjectLabel = new Label(modelGroup, SWT.NULL);
		modelTargetProjectLabel.setText("target project:");
		modelTargetProjectText = new Text(modelGroup, SWT.BORDER);
		modelTargetProjectText.setLayoutData(getGridData(SWT.FILL, 1));
		modelTargetProjectText.setText(
				config.getContext().getJavaModelGenerator().getProperty(JavaModelGeneratorEnum.TARGET_PROJECT.name));

		// -----sql map
		Group sqlMapGroup = newGroup("sql map(*Mapper.xml)", 6);
		sqlMapTargetPackageLabel = new Label(sqlMapGroup, SWT.NULL);
		sqlMapTargetPackageLabel.setText("target package:");
		sqlMapTargetPackageText = new Text(sqlMapGroup, SWT.BORDER);
		sqlMapTargetPackageText.setLayoutData(getGridData(SWT.FILL, 1));
		sqlMapTargetPackageText
				.setText(config.getContext().getSqlMapGenerator().getProperty(SqlMapGeneratorEnum.TARGET_PACKAGE.name));
		sqlMapTargetProjectLabel = new Label(sqlMapGroup, SWT.NULL);
		sqlMapTargetProjectLabel.setText("target project:");
		sqlMapTargetProjectText = new Text(sqlMapGroup, SWT.BORDER);
		sqlMapTargetProjectText.setLayoutData(getGridData(SWT.FILL, 1));
		sqlMapTargetProjectText
				.setText(config.getContext().getSqlMapGenerator().getProperty(SqlMapGeneratorEnum.TARGET_PROJECT.name));

		// -----java client
		Group javaClientGroup = newGroup("java client(*Mapper.java)", 6);
		javaClientTargetPackageLabel = new Label(javaClientGroup, SWT.NULL);
		javaClientTargetPackageLabel.setText("target package:");
		javaClientTargetPackageText = new Text(javaClientGroup, SWT.BORDER);
		javaClientTargetPackageText.setLayoutData(getGridData(SWT.FILL, 1));
		javaClientTargetPackageText.setText(
				config.getContext().getJavaClientGenerator().getProperty(JavaClientGeneratorEnum.TARGET_PACKAGE.name));
		javaClientTargetProjectLabel = new Label(javaClientGroup, SWT.NULL);
		javaClientTargetProjectLabel.setText("target project:");
		javaClientTargetProjectText = new Text(javaClientGroup, SWT.BORDER);
		javaClientTargetProjectText.setLayoutData(getGridData(SWT.FILL, 1));
		javaClientTargetProjectText.setText(
				config.getContext().getJavaClientGenerator().getProperty(JavaClientGeneratorEnum.TARGET_PROJECT.name));

		// -----tables
		Group tableGroup = newGroup("tables[double click to edit]", 4);
		final List<com.puyixiaowo.eclipsembg.model.Table> defaultTables = config.getContext().getTables();

		table = new Table(tableGroup,
				SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.BORDER | SWT.CHECK | SWT.MULTI);
		table.setLayout(new GridLayout(3, false));

		GridData gdTable = new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1);
		gdTable.heightHint = 200;

		table.setLayoutData(gdTable);
		// table data
		for (int i = 0; i < defaultTables.size(); i++) {
			String tableName = defaultTables.get(i).getProperty("tableName");
			TableItem item = new TableItem(table, SWT.NULL);

			item.setText(tableName);
		}

		// edit table
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = table.getItem(event.index);
				fillTableData(item.getText());
			}
		});
		// context menu
		table.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				makeContextMenu(table);
			}
		});
		
		//shell.setLayout(new FillLayout());
		// edit table
		final ScrolledComposite sc = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);
		
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd.heightHint = 200;
		sc.setLayoutData(gd);
		final Composite editTableContainer = new Composite(sc, SWT.NONE);
		sc.setContent(editTableContainer);
		
		GridLayout gridLayoutTableContainer = new GridLayout();
		gridLayoutTableContainer.numColumns = 2;
		editTableContainer.setLayout(gridLayoutTableContainer);
		tableNameLabel = new Label(editTableContainer, SWT.NULL);
		tableNameLabel.setText("table name:");
		tableNameText = new Text(editTableContainer, SWT.BORDER);
		domainObjectNameLable = new Label(editTableContainer, SWT.NULL);
		domainObjectNameLable.setText("domainObjectName:");
		domainObjectNameText = new Text(editTableContainer, SWT.BORDER);
		
		
		Group groupByExample = newGroup("enable by example", editTableContainer);
		GridLayout gridLayoutExample = new GridLayout(5, false);
		gridLayoutExample.horizontalSpacing = 10;
		gridLayoutExample.verticalSpacing = 14;
		groupByExample.setLayout(gridLayoutExample);
		GridData gridDataGroupByExample = new GridData();
		gridDataGroupByExample.horizontalSpan = 5;
		groupByExample.setLayoutData(gridDataGroupByExample);

		Group groupCount = newGroup("count", groupByExample);
		GridLayout gridLayoutCount = new GridLayout(1, false);
		gridLayoutCount.horizontalSpacing = 10;
		gridLayoutCount.verticalSpacing = 14;
		groupCount.setLayout(gridLayoutCount);
		enableCountByExampleRadio1 = new Button(groupCount, SWT.RADIO);
		enableCountByExampleRadio1.setText("true");
		enableCountByExampleRadio0 = new Button(groupCount, SWT.RADIO);
		enableCountByExampleRadio0.setText("false");
		enableCountByExampleRadio0.setSelection(true);

		Group groupUpdate = newGroup("update", groupByExample);
		GridLayout gridLayoutUpdate = new GridLayout(1, false);
		gridLayoutUpdate.horizontalSpacing = 10;
		gridLayoutUpdate.verticalSpacing = 14;
		groupUpdate.setLayout(gridLayoutUpdate);
		enableUpdateByExampleRadio1 = new Button(groupUpdate, SWT.RADIO);
		enableUpdateByExampleRadio1.setText("true");
		enableUpdateByExampleRadio0 = new Button(groupUpdate, SWT.RADIO);
		enableUpdateByExampleRadio0.setText("false");
		enableUpdateByExampleRadio0.setSelection(true);

		Group groupDelete = newGroup("delete", groupByExample);
		GridLayout gridLayoutDelete = new GridLayout(1, false);
		gridLayoutDelete.horizontalSpacing = 10;
		gridLayoutDelete.verticalSpacing = 14;
		groupDelete.setLayout(gridLayoutDelete);
		enableDeleteByExampleRadio1 = new Button(groupDelete, SWT.RADIO);
		enableDeleteByExampleRadio1.setText("true");
		enableDeleteByExampleRadio0 = new Button(groupDelete, SWT.RADIO);
		enableDeleteByExampleRadio0.setText("false");
		enableDeleteByExampleRadio0.setSelection(true);

		Group groupSelect = newGroup("select", groupByExample);
		GridLayout gridLayoutSelect = new GridLayout(1, false);
		gridLayoutSelect.horizontalSpacing = 10;
		gridLayoutSelect.verticalSpacing = 14;
		groupSelect.setLayout(gridLayoutSelect);
		enableSelectByExampleRadio1 = new Button(groupSelect, SWT.RADIO);
		enableSelectByExampleRadio1.setText("true");
		enableSelectByExampleRadio0 = new Button(groupSelect, SWT.RADIO);
		enableSelectByExampleRadio0.setText("false");
		enableSelectByExampleRadio0.setSelection(true);

		Group groupSelectQueryId = newGroup("select queryId ", groupByExample);
		GridLayout gridLayoutSelectQueryId = new GridLayout(1, false);
		gridLayoutSelectQueryId.horizontalSpacing = 10;
		gridLayoutSelectQueryId.verticalSpacing = 14;
		groupSelectQueryId.setLayout(gridLayoutSelectQueryId);
		selectByExampleQueryIdRadio1 = new Button(groupSelectQueryId, SWT.RADIO);
		selectByExampleQueryIdRadio1.setText("true");
		selectByExampleQueryIdRadio0 = new Button(groupSelectQueryId, SWT.RADIO);
		selectByExampleQueryIdRadio0.setText("false");
		selectByExampleQueryIdRadio0.setSelection(true);
		
		sc.setMinSize(editTableContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

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
		createBtn.setText("save");
		GridData createBtnGrid = getGridDataBottom(SWT.RIGHT, 1);
		createBtnGrid.widthHint = 90;
		createBtn.setLayoutData(createBtnGrid);
		createBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createConfig();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// System.out.println("default selected");
			}
		});
	}
	
	/**
	 * fill table data
	 * @param text
	 */
	protected void fillTableData(String text) {
		tableNameText.setText(text);
		//select table by tablename
		com.puyixiaowo.eclipsembg.model.Table table = GeneratorConfigUtil.getTableByTableName(config, text);
		
		domainObjectNameText.setText(table.getProperty(TableEnum.domainObjectName.name));
		
		String enableCountByExample = table.getProperty(TableEnum.enableCountByExample.name);
		if (enableCountByExample.equals("true")) {
			enableCountByExampleRadio1.setSelection(true);
			enableCountByExampleRadio0.setSelection(false);
		} else {
			enableCountByExampleRadio0.setSelection(true);
			enableCountByExampleRadio1.setSelection(false);
		}
		
		String enableUpdateByExample = table.getProperty(TableEnum.enableUpdateByExample.name);
		if (enableUpdateByExample.equals("true")) {
			enableUpdateByExampleRadio1.setSelection(true);
			enableUpdateByExampleRadio0.setSelection(false);
		} else {
			enableUpdateByExampleRadio0.setSelection(true);
			enableUpdateByExampleRadio1.setSelection(false);
		}
		
		String enableDeleteByExample = table.getProperty(TableEnum.enableDeleteByExample.name);
		if (enableDeleteByExample.equals("true")) {
			enableDeleteByExampleRadio1.setSelection(true);
			enableDeleteByExampleRadio0.setSelection(false);
		} else {
			enableDeleteByExampleRadio0.setSelection(true);
			enableDeleteByExampleRadio1.setSelection(false);
		}
		
		String enableSelectByExample = table.getProperty(TableEnum.enableSelectByExample.name);
		if (enableSelectByExample.equals("true")) {
			enableSelectByExampleRadio1.setSelection(true);
			enableSelectByExampleRadio0.setSelection(false);
		} else {
			enableSelectByExampleRadio0.setSelection(true);
			enableSelectByExampleRadio1.setSelection(false);
		}
		
		String selectByExampleQueryId = table.getProperty(TableEnum.selectByExampleQueryId.name);
		if (selectByExampleQueryId.equals("true")) {
			selectByExampleQueryIdRadio1.setSelection(true);
			selectByExampleQueryIdRadio0.setSelection(false);
		} else {
			selectByExampleQueryIdRadio0.setSelection(true);
			selectByExampleQueryIdRadio1.setSelection(false);
		}
		
		
	}

	private void createConfig() {
		String filename = fileNameText.getText();
		if (isNew && GeneratorConfigUtil.isGeneratorConfigExists(filename)) {
			MessageDialog.openError(shell, "create config", "config has exists");
			return;
		}
		String modelTargetPackage = modelTargetPackageText.getText();
		String modelTargetProject = modelTargetProjectText.getText();
		String sqlMapTargetPackage = sqlMapTargetPackageText.getText();
		String sqlMapTargetProject = sqlMapTargetProjectText.getText();
		String javaClientTargetPackage = javaClientTargetPackageText.getText();
		String javaClientTargetProject = javaClientTargetProjectText.getText();
		TableItem [] tableItems = table.getItems();
		

		final GeneratorConfig config = new GeneratorConfig();

		config.setFileName(filename);

		ClassPathEntry classPathEntry = new ClassPathEntry();
		classPathEntry.addProperty(ClassPathEntryEnum.LOCATION.name, "location");
		config.setClassPathEntry(classPathEntry);

		Context context = new Context();
		context.addProperty(ContextEnum.ID.name, "DB2Tables");
		context.addProperty(ContextEnum.TARGET_RUNTIME.name, "MyBatis3");

		JavaModelGenerator javaModelGenerator = new JavaModelGenerator();
		javaModelGenerator.addProperty(JavaModelGeneratorEnum.TARGET_PACKAGE.name, modelTargetPackage);
		javaModelGenerator.addProperty(JavaModelGeneratorEnum.TARGET_PROJECT.name, modelTargetProject);
		context.setJavaModelGenerator(javaModelGenerator);

		SqlMapGenerator sqlMapGenerator = new SqlMapGenerator();
		sqlMapGenerator.addProperty(SqlMapGeneratorEnum.TARGET_PACKAGE.name, sqlMapTargetPackage);
		sqlMapGenerator.addProperty(SqlMapGeneratorEnum.TARGET_PROJECT.name, sqlMapTargetProject);
		context.setSqlMapGenerator(sqlMapGenerator);

		JavaClientGenerator javaClientGenerator = new JavaClientGenerator();
		javaClientGenerator.addProperty(JavaClientGeneratorEnum.TARGET_PACKAGE.name, javaClientTargetPackage);
		javaClientGenerator.addProperty(JavaClientGeneratorEnum.TARGET_PROJECT.name, javaClientTargetProject);
		context.setJavaClientGenerator(javaClientGenerator);
		
		List<com.puyixiaowo.eclipsembg.model.Table> tables = new ArrayList<com.puyixiaowo.eclipsembg.model.Table>();
		for (TableItem tableItem : tableItems) {
			com.puyixiaowo.eclipsembg.model.Table t = new com.puyixiaowo.eclipsembg.model.Table();
			//t.addProperty(name, value);
			tables.add(t);
		}
		context.setTables(tables);

		config.setContext(context);

		ProgressMonitorDialog progressDlg = new ProgressMonitorDialog(shell);

		shell.dispose();// close shell

		try {
			progressDlg.run(false, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("creating...", 1);
					try {

						GeneratorConfigUtil.addGeneratorConfig(config);
						GeneratorConfigUtil.refreshGeneratorConfig();

						refreshConfigView();

						monitor.worked(1);
					} finally {
						monitor.done();
					}

				}

			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void refreshConfigView() {
		IWorkbenchWindow[] ws = PlatformUI.getWorkbench().getWorkbenchWindows();

		for (IWorkbenchWindow iWorkbenchWindow : ws) {
			IViewPart part = iWorkbenchWindow.getActivePage().findView(GeneratorConfView.ID);
			if (part instanceof GeneratorConfView) {
				GeneratorConfView viewPart = (GeneratorConfView) part;
				if (viewPart != null) {
					viewPart.refreshView();
				}
			}
		}
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

	/**
	 * 创建group
	 * 
	 * @param text
	 * @param columns
	 * @return
	 */
	public Group newGroup(String text, Composite composite) {
		Display display = shell.getDisplay();
		Group group = new Group(composite, SWT.SHADOW_NONE);

		group.setFont(new Font(display, "Consolas", 10, SWT.BOLD));
		group.setText(text);
		group.setEnabled(true);

		return group;
	}
}
