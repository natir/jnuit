package com.esotericsoftware.tablelayout;

public class MaxWidthValue<C, T extends C> extends CellValue<C, T> {
    
    MaxWidthValue(Toolkit<C, T> toolkit) {
        super(toolkit);
    }

    @Override
    public float get (Cell<C,T> cell) {
        if (cell == null) throw new RuntimeException("maxWidth can only be set on a cell property.");
        C widget = cell.widget;
        if (widget == null) return 0;
        return toolkit.getMaxWidth(widget);
    }
}
