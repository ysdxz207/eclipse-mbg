package com.puyixiaowo.eclipsembg.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

/**
 * This class demonstrates ScrolledComposite
 */
public class ScrolledCompositeTest {

	private Shell shell;

	public void run() {
		Display display = new Display();
		shell = new Shell(display);
		createContents(shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private void createContents(Composite parent) {
		parent.setSize(600, 400);
		parent.setLayout(new GridLayout());
		Group group = newGroup("edit table", parent);

		group.setLayout(new GridLayout());
		group.setSize(800, 600);

		// Create the ScrolledComposite to scroll horizontally and vertically
		final ScrolledComposite sc = new ScrolledComposite(group, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Create a child composite to hold the controls
		Composite child = new Composite(sc, SWT.NONE);
		sc.setContent(child);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		child.setLayout(layout);
		// Create the buttons
		new Button(child, SWT.PUSH).setText("One");
		new Button(child, SWT.PUSH).setText("Two");
		/*
		 * // Set the absolute size of the child child.setSize(400, 400);
		 */
		// Set the child as the scrolled content of the ScrolledComposite

		// Set the minimum size
		sc.setMinSize(400, 400);

		// Expand both horizontally and vertically

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

	public static void main(String[] args) {
		new ScrolledCompositeTest().run();
	}
}