package com.polydes.points.app;

import com.polydes.points.PointsExtension;
import stencyl.app.comp.MiniSplitPane;

import javax.swing.*;
import java.awt.*;

public class PointEditorPanel extends JPanel
{
    private final PointsExtension ext;
    private MiniSplitPane splitPane;
    private PointEditorPage editorPage;

    public PointEditorPanel(PointsExtension ext) {
        super(new BorderLayout());
        this.ext = ext;

        add(splitPane = new MiniSplitPane(), BorderLayout.CENTER);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        editorPage = new PointEditorPage(ext);

        splitPane.setLeftComponent(editorPage.getSidebar());
        splitPane.setRightComponent(editorPage);
        splitPane.setDividerLocation(PointsExtension.pointWindowSideWidth);
    }

    public void onSave()
    {
        if(editorPage != null)
            editorPage.save();
    }

    public void close()
    {
        //PointsExtension.pointWindowPos = new Rectangle(getBounds());
        PointsExtension.pointWindowSideWidth = splitPane.getDividerLocation();

        splitPane.removeAll();
        removeAll();

        if(editorPage != null)
        {
            editorPage.dispose();
            editorPage = null;
        }
    }
}
