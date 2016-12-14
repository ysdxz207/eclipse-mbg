package com.puyixiaowo.eclipsembg.dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.puyixiaowo.eclipsembg.constants.Constant;
import com.puyixiaowo.eclipsembg.dialog.handler.NewConfigDialogHandler;

public class NewConfigDialog extends MBGDialog{

	public NewConfigDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	public Shell open(String title) {
		Shell shell = super.open(title);
		Display display = shell.getParent().getDisplay();
		
		new NewConfigDialogHandler(shell);//fill dialog
		
		shell.setSize(Constant.WIDTH_CONFIG, Constant.HEIGHT_CONFIG);
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
