package com.puyixiaowo.eclipsembg.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Testaa {
	public static void main(String[] args) {
		Display display = new Display();
		Shell parent = new Shell(display);
		parent.setLayout(new GridLayout());
		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		final Composite childComposite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(childComposite);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		childComposite.setLayout(layout);
		for (int i = 0; i < 200; i++) {
			Label label = new Label(childComposite, SWT.BORDER);
			label.setText("Label " + i);
		}
		Point size = childComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrolledComposite.setMinSize(size);
		parent.setSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, 200);
		parent.open();
		while (!parent.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
