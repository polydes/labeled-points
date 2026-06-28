package com.polydes.points;

import java.awt.Rectangle;

import javax.swing.*;

import com.polydes.points.app.PointEditorPanel;
import stencyl.app.ext.PageAddon;
import stencyl.core.ext.GameExtension;
import stencyl.core.ext.engine.ExtensionInstanceManager.FormatUpdateSubmitter;
import stencyl.sw.app.center.GameLibrary;

public class PointsExtension extends GameExtension
{
	private PointEditorPanel pointEditorPanel;
	
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

		owner().getAddons().setAddon(GameLibrary.DASHBOARD_SIDEBAR_PAGE_ADDONS, new PageAddon.ExtensionPageAddon(this){
			@Override
			public JPanel getPage() {
				if(pointEditorPanel == null)
					pointEditorPanel = new PointEditorPanel(PointsExtension.this);
				return pointEditorPanel;
			}
		});
	}

	@Override
	protected void onUnload() {
		if(pointEditorPanel != null)
		{
			pointEditorPanel.close();
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
		pointEditorPanel.onSave();
		super.onSave();
	}

	@Override
	public void updateFromVersion(int fromVersion, FormatUpdateSubmitter formatUpdateQueue) {

	}
}