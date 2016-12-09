package com.puyixiaowo.eclipsembg.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MBGDialog extends Dialog implements IMBGDialog{
	
	private Shell parent = getParent();

	public MBGDialog(Shell parentShell) {
		super(parentShell);
	}

	public MBGDialog(Shell parentShell, int style) {
		super(parentShell, style);
	}
	
	public void centerDialog(Shell shell) {
		int width = shell.getMonitor().getClientArea().width;
		int height = shell.getMonitor().getClientArea().height;
		int x = shell.getSize().x;
		int y = shell.getSize().y;
		if (x > width) {
			shell.getSize().x = width;
		}
		if (y > height) {
			shell.getSize().y = height;
		}
		shell.setLocation((width - x) / 2, (height - y) / 2);

	}
	
	@Override
	public Shell open(String title) {
		final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER
				| SWT.CLOSE | SWT.RESIZE | SWT.APPLICATION_MODAL);
		shell.setText(title);
		shell.setLayout(new GridLayout());
		
		shell.pack();
		
		return shell;
	}

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		MBGDialog dialog = new MBGDialog(shell);
		dialog.open("test");
	}
	
}
