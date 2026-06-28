package com.polydes.points;

import java.awt.Rectangle;

import javax.swing.SwingUtilities;

import com.polydes.points.app.PointEditorPage;
import com.polydes.points.app.PointEditorWindow;
import stencyl.core.ext.GameExtension;
import stencyl.core.ext.engine.ExtensionInstanceManager.FormatUpdateSubmitter;
import stencyl.sw.app.main.Menu;

public class PointsExtension extends GameExtension
{
	private PointEditorWindow window;
	
	public static Rectangle pointWindowPos;
	public static int pointWindowSideWidth;

	@Override
	protected void onLoad() {
		super.onLoad();

		pointWindowPos = new Rectangle(
			readIntProp("pointwin.x", -1),
			readIntProp("pointwin.y", -1),
			readIntProp("pointwin.width", 640),
			readIntProp("pointwin.height", 480)
		);
		pointWindowSideWidth = readIntProp("pointwindow.sidewidth", 265);

		owner().getAddons().setAddon(Menu.EXTENSIONS_MENU_ADDON, new Menu.ExtensionMenuAddon(this){
			@Override
			protected void actionPerformed() {
				if(window == null)
					window = new PointEditorWindow(PointsExtension.this);
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						window.setVisible(true);
					}
				});
			}
		});
	}

	@Override
	protected void onUnload() {
		if(window != null)
		{
			window.setVisible(false);
			window.dispose();
		}
		putProp("pointwin.x", pointWindowPos.x);
		putProp("pointwin.y", pointWindowPos.y);
		putProp("pointwin.width", pointWindowPos.width);
		putProp("pointwin.height", pointWindowPos.height);
		putProp("pointwin.sidewidth", pointWindowSideWidth);
		super.onUnload();
	}

	@Override
	protected void onSave() {
		window.onSave();
		super.onSave();
	}

	@Override
	public void updateFromVersion(int fromVersion, FormatUpdateSubmitter formatUpdateQueue) {

	}
}