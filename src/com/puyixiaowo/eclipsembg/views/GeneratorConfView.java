package com.puyixiaowo.eclipsembg.views;

import java.lang.reflect.InvocationTargetException;
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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.puyixiaowo.eclipsembg.dialog.NewConfigDialog;
import com.puyixiaowo.eclipsembg.model.GeneratorConfig;
import com.puyixiaowo.eclipsembg.util.GeneratorConfigUtil;

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

public class GeneratorConfView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.puyixiaowo.eclipsembg.views.GeneratorConfView";

	private TableViewer viewer;
	private Action doubleClickAction;
	private Action actionRun;
	private Action actionEditConfig;
	private Action actionNewConfig;
	private NewConfigDialog newConfigDialog;

	class ConfigObject extends GeneratorConfig implements IAdaptable {

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return null;
		}

		@Override
		public String toString() {
			return this.getFileName();
		}

	}

	class ViewContentProvider implements IStructuredContentProvider {
		List<? extends GeneratorConfig> configObjectList;

		public Object[] getElements(Object parent) {
			if (configObjectList == null) {
				try {
					init();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return configObjectList.toArray();
		}

		public List<? extends GeneratorConfig> getConfigObjectList() {

			return configObjectList;
		}

		public void setConfigObjectList(List<ConfigObject> configObjectList) {
			this.configObjectList = configObjectList;
		}

		private void init() throws IllegalAccessException, InvocationTargetException {
			configObjectList = GeneratorConfigUtil.getGeneratorConfigs();
		}

	}

	/**
	 * The constructor.
	 */
	public GeneratorConfView() {
		GeneratorConfigUtil.generateDefaultConfFile();// generate default config to dropin/eclipse-mbg dir
		GeneratorConfigUtil.refreshGeneratorConfig();
	}
	
	public void refreshView(){
		viewer.setContentProvider(new ViewContentProvider());
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent);

		viewer.setContentProvider(new ViewContentProvider());

		viewer.setInput(getViewSite());
		

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "eclipse-mbg.viewer");
		getSite().setSelectionProvider(viewer);

		// create new db dialog
		newConfigDialog = new NewConfigDialog(parent.getShell());

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
				GeneratorConfView.this.fillContextMenu(manager);
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
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(new Separator());
		// Other plug-ins can contribute there actions here
		manager.add(actionRun);
		manager.add(actionEditConfig);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
//		manager.add(actionRun);
//		manager.add(actionEditConfig);
		manager.add(actionNewConfig);
		manager.add(new Separator());
	}

	private void makeActions() {
		// run generator
		actionRun = new Action() {
			public void run() {
				
			}
		};
		actionRun.setText("run");
		actionRun.setToolTipText("run generator with selected config");
		actionRun.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		// edit config
		actionEditConfig = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				GeneratorConfig config = GeneratorConfigUtil.getConfigByFilename(obj.toString());
				
				newConfigDialog.open("New config", config);
			}
		};
		actionEditConfig.setText("edit config");
		actionEditConfig.setToolTipText("edit selected config");
		actionEditConfig.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		// open new config dialog
		actionNewConfig = new Action() {
			public void run() {
				newConfigDialog.open("New config", null);
			}
		};
		actionNewConfig.setText("New config");
		actionNewConfig.setToolTipText("New config");
		actionNewConfig.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				alert("Double-click detected on " + obj.toString());
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
		MessageDialog.openInformation(viewer.getControl().getShell(), "alert", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
