package com.puyixiaowo.eclipsembg.dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.puyixiaowo.eclipsembg.constants.Constants;
import com.puyixiaowo.eclipsembg.dialog.handler.NewConfigDialogHandler;
import com.puyixiaowo.eclipsembg.model.GeneratorConfig;

public class NewConfigDialog extends MBGDialog{

	public NewConfigDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	public Shell open(String title) {
		Shell shell = super.open(title);
		Display display = shell.getParent().getDisplay();
		
		new NewConfigDialogHandler(shell, null);//fill dialog
		
		shell.setSize(Constants.WIDTH_CONFIG, Constants.HEIGHT_CONFIG);
		shell.setMinimumSize(500, 300);
		super.centerDialog(shell);
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return shell;
	}
	
	public Shell open(String title, GeneratorConfig config) {
		Shell shell = super.open(title);
		Display display = shell.getParent().getDisplay();
		
		new NewConfigDialogHandler(shell, config);//fill dialog
		
		shell.setSize(Constants.WIDTH_CONFIG, Constants.HEIGHT_CONFIG);
		shell.setMinimumSize(500, 300);
		super.centerDialog(shell);
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return shell;
	}
	
}
