package com.polydes.points.app;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import com.polydes.points.PointsExtension;

import stencyl.app.comp.MiniSplitPane;
import stencyl.app.main.MainWindow;

public class PointEditorWindow extends JDialog
{
	private final PointsExtension ext;
	private MiniSplitPane splitPane;
	private JPanel contents;
	private boolean initialized;
	private PointEditorPage editorPage;
	
	public PointEditorWindow(PointsExtension ext)
	{
		super(MainWindow.get(), "Point Editor", true);
		this.ext = ext;
		
		contents = new JPanel(new BorderLayout());
		
		contents.add(splitPane = new MiniSplitPane(), BorderLayout.CENTER);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		setContentPane(contents);
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				closeWindow();
			}
		});
		
		setVisible(false);
		initialized = false;
	}
	
	private void closeWindow()
	{
		PointsExtension.pointWindowPos = new Rectangle(getBounds());
		PointsExtension.pointWindowSideWidth = splitPane.getDividerLocation();
		
		setVisible(false);
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		if(!initialized && visible)
			init();
		
		super.setVisible(visible);
	}
	
	private void init()
	{
		initialized = true;
		
		Rectangle r = PointsExtension.pointWindowPos;

		if(editorPage == null)
		{
			editorPage = new PointEditorPage(ext);
		}

		splitPane.setLeftComponent(editorPage.getSidebar());
		splitPane.setRightComponent(editorPage);
		splitPane.setDividerLocation(PointsExtension.pointWindowSideWidth);
		
		setSize(r.width, r.height);
		
		if(r.x == -1 || r.y == -1)
			setLocationRelativeTo(MainWindow.get());
		else
			setLocation(r.x, r.y);
	}

	public void onSave()
	{
		if(editorPage != null)
			editorPage.save();
	}
	
	@Override
	public void dispose()
	{
		splitPane.removeAll();
		contents.removeAll();

		if(editorPage != null)
		{
			editorPage.dispose();
			editorPage = null;
		}

		super.dispose();
	}
}
