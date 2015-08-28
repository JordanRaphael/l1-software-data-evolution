package gui.tableElements;


import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class JvTable extends JTable {

	private Font originalFont;
    private int originalRowHeight;
    private float zoomFactor = 1.0f;

    public JvTable(TableModel dataModel)
    {
        super(dataModel);
    }

    @Override
    public void setFont(Font font)
    {
        originalFont = font;
        // When setFont() is first called, zoomFactor is 0.
        if (zoomFactor != 0.0 && zoomFactor != 1.0)
        {
            float scaledSize = originalFont.getSize2D() * zoomFactor;
            font = originalFont.deriveFont(scaledSize);
        }

        super.setFont(font);
    }

    @Override
    public void setRowHeight(int rowHeight)
    {
        originalRowHeight = rowHeight;
        // When setRowHeight() is first called, zoomFactor is 0.
        if (zoomFactor != 0.0 && zoomFactor != 1.0)
            rowHeight = (int) Math.ceil(originalRowHeight * zoomFactor);

        super.setRowHeight(rowHeight);
    }

    public float getZoom()
    {
        return zoomFactor;
    }

    public void setZoom(float zoomFactor)
    {
        if (this.zoomFactor == zoomFactor)
            return;

        if (originalFont == null)
            originalFont = getFont();
        if (originalRowHeight == 0)
            originalRowHeight = getRowHeight();

        float oldZoomFactor = this.zoomFactor;
        this.zoomFactor = zoomFactor;
        Font font = originalFont;
        if (zoomFactor != 1.0)
        {
            float scaledSize = originalFont.getSize2D() * zoomFactor;
            font = originalFont.deriveFont(scaledSize);
        }

        super.setFont(font);
        super.setRowHeight((int) Math.ceil(originalRowHeight * zoomFactor));
        getTableHeader().setFont(font);

        firePropertyChange("zoom", oldZoomFactor, zoomFactor);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
    {
        Component comp = super.prepareRenderer(renderer, row, column);
        comp.setFont(this.getFont());
        return comp;
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column)
    {
        Component comp = super.prepareEditor(editor, row, column);
        comp.setFont(this.getFont());
        return comp;
    }
}
