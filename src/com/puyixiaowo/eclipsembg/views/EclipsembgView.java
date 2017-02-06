package com.puyixiaowo.eclipsembg.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.puyixiaowo.eclipsembg.dialog.NewDBConnectionDialog;
import com.puyixiaowo.eclipsembg.model.GeneratorConfig;
import com.puyixiaowo.eclipsembg.util.DBUtil;
import com.puyixiaowo.eclipsembg.util.GeneratorConfigUtil;
import com.puyixiaowo.eclipsembg.util.JDBCUtil;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class EclipsembgView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.puyixiaowo.eclipsembg.views.EclipsembgView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action actionNewDb;
	private Action actionGenerate;
	private Action actionOpenConfView;
	private Action doubleClickAction;
	private NewDBConnectionDialog newDBConnectionDialog;
	private ViewContentProvider viewContentProvider;

	class TreeObject implements IAdaptable {
		private String name;
		private List<String> tableNames;
		private TreeParent parent;

		public TreeObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		public List<String> getTableNames() {
			return tableNames;
		}

		public void setTableNames(List<String> tableNames) {
			this.tableNames = tableNames;
		}

		public String toString() {
			return getName();
		}

		public <T> T getAdapter(Class<T> key) {
			return null;
		}
	}

	class TreeParent extends TreeObject {
		private ArrayList<TreeObject> children;

		public TreeParent(String name) {
			super(name);
			children = new ArrayList<TreeObject>();
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements ITreeContentProvider {
		private TreeParent invisibleRoot;

		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null)
					initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		/*
		 * We will set up a dummy model to initialize tree heararchy. In a real
		 * code, you will connect to a real model and expose its hierarchy.
		 */
		private void initialize() {
			List<GeneratorConfig> configList = GeneratorConfigUtil.getGeneratorConfigs();
			invisibleRoot = new TreeParent("");
			TreeParent root = new TreeParent("dbs");
			for (GeneratorConfig config : configList) {
				TreeParent db = new TreeParent(DBUtil.getDbName(config));
				root.addChild(db);
			}

			invisibleRoot.addChild(root);
		}
	}

	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}

	/**
	 * The constructor.
	 */
	public EclipsembgView() {
		GeneratorConfigUtil.generateDefaultConfFile();// generate default config to dropin/eclipse-mbg dir
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewContentProvider = new ViewContentProvider();
		viewer.setContentProvider(viewContentProvider);
		viewer.setInput(getViewSite());
		viewer.setLabelProvider(new ViewLabelProvider());

		// create new db dialog
		newDBConnectionDialog = new NewDBConnectionDialog(parent.getShell());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "eclipse-mbg.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				EclipsembgView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(actionNewDb);
		manager.add(new Separator());
		manager.add(actionGenerate);
	}

	private void fillContextMenu(IMenuManager manager) {
		TreeSelection obj = (TreeSelection) viewer.getSelection();
		
		if (obj.getFirstElement() instanceof TreeParent && ((TreeParent) obj.getFirstElement()).getName().equals("dbs")) {
			manager.add(actionNewDb);
		} else {
			manager.add(actionGenerate);
		}
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(actionNewDb);
		manager.add(actionGenerate);
		manager.add(actionOpenConfView);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		actionNewDb = new Action() {
			public void run() {
				// open add db connection shell
				newDBConnectionDialog.open("New DB connection");
			}
		};
		actionNewDb.setText("New DB connection");
		actionNewDb.setToolTipText("Add a new DB connection");
		actionNewDb.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		actionGenerate = new Action() {
			public void run() {
					//run generator
				   List<String> warnings = new ArrayList<String>();
				   boolean overwrite = true;
				   Configuration config = GeneratorConfigUtil.getDefaultConfig().toMybatisConfiguration();
				   
				   //generate by config
				   
				   
				   DefaultShellCallback callback = new DefaultShellCallback(overwrite);
				   try {
					MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
					   myBatisGenerator.generate(null);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		};
		actionGenerate.setText("generate with dao and mapper");
		actionGenerate.setToolTipText("generate with mapper.xml dao.java daoImpl.java");
		actionGenerate.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		actionOpenConfView = new Action() {
			public void run() {
				// open generatorConfView
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
							.showView("com.puyixiaowo.eclipsembg.views.GeneratorConfView");
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		};
		actionOpenConfView.setText("generator config");
		actionOpenConfView.setToolTipText("edit generator config list");
		actionOpenConfView.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				try {
					loadDbTables(obj.toString());
				} catch (Exception e) {
					alert("load tables error:" + e.getMessage());
				}
				
				//expand tree
				viewer.expandAll();
			}

			/*
			 * 
			 */
			private void loadDbTables(String dbName) throws Exception {
				if (!dbName.equals("dbs") && dbName != null) {
					List<String> tabelNames = JDBCUtil.getTableNames(dbName);
					if (tabelNames == null) {
						return;
					}
					TreeParent treeParents = (TreeParent) viewContentProvider
							.getChildren(viewContentProvider.invisibleRoot)[0];
					TreeObject[] obj = treeParents.getChildren();
					
					for (Object treeParent : obj) {
						if (treeParent instanceof TreeParent) {
							if (((TreeParent)treeParent).getName().equals(dbName)) {
								for (String tableName : tabelNames) {
									TreeObject child = new TreeObject(tableName);
									((TreeParent)treeParent).addChild(child);
								}
							}
						}
						
					}
				}

			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void alert(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Eclipse MBG View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
