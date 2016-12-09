package com.puyixiaowo.eclipsembg.dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.puyixiaowo.eclipsembg.conf.Constant;
import com.puyixiaowo.eclipsembg.dialog.handler.NewDBConnectionDialogHandler;

public class NewDBConnectionDialog extends MBGDialog{

	public NewDBConnectionDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	public Shell open(String title) {
		Shell shell = super.open(title);
		Display display = shell.getParent().getDisplay();
		
		new NewDBConnectionDialogHandler(shell);//fill dialog
		
		shell.setSize(Constant.WIDTH, Constant.HEIGHT);
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
