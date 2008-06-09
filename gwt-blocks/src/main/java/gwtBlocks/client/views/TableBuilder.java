// Copyright 2008 Harish Krishnaswamy
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package gwtBlocks.client.views;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.*;
import static com.google.gwt.user.client.ui.HasVerticalAlignment.*;
import gwtBlocks.shared.StringUtils;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Helps build GWT FlexTables. The builder is reusable, ie. it can be used to build multiple tables, although it can
 * only build one table at a time. {@link #getTable()} returns the actual table currently being built. The
 * <code>newXXXTable</code> methods reset the builder and start building a new table.
 * <p>
 * The builder builds the table sequentially starting from top left working its way to bottom right. The builder
 * maintains an internal pointer that points to the cell that's currently being built. Requests sent to the builder are
 * fulfilled against the cell currently being pointed to by the internal pointer. The pointer can be moved to the next
 * cell by calling one of the <code>add</code> or <code>space</code> methods. The pointer can be moved to the next
 * row by calling {@link #nextRow()}.
 * <p>
 * Methods that are common to more than one HTML element like <code>width</code> and <code>style</code> have a
 * suffix in their names to identify the element they apply to. Methods that have a "<code>T</code>" suffix apply to
 * table, those that have an "<code>R</code>" suffix apply to table rows and "<code>C</code>" applies to table
 * cells.
 * <p>
 * The builder has some switches that when turned on will apply the effect of the switch to all the cells of the table
 * from the current cell forward until the switch is turned off or the end of the table is reached.
 * <ul>
 * <li>The stretch switch stretches the cell widgets to fit the cell size and, can be turned on via {@link #hStretch()},
 * {@link #vStretch()} or {@link #stretch()} and turned off via {@link #dontStretch()}.
 * <li>The row style switch applies "<code>evenRow</code>" and "<code>oddRow</code>" styles to even and odd
 * rows respectively and, can be turned on via {@link #applyRowStyle()} and turned off via {@link #dontApplyRowStyle()}.
 * In order to apply "<code>evenRow</code>" style to odd rows and "<code>oddRow</code>" style to even rows,
 * simply use {@link #swapRowStyle()} instead of {@link #applyRowStyle()}. Swapping the row style twice will, as
 * expected, return the switch to the {@link #applyRowStyle()} behavior.
 * </ul>
 * 
 * @author hkrishna
 */
public class TableBuilder
{
    private FlexTable _table;
    private int       _row, _col, _colSpan;
    private boolean   _applyRowStyle, _swapRowStyle, _hStretch, _vStretch;

    public TableBuilder newTable()
    {
        _table = WidgetFactory.newFlexTable();

        _applyRowStyle = _swapRowStyle = _hStretch = _vStretch = false;

        resetPos(0);

        return this;
    }

    public TableBuilder newFormTable()
    {
        return newTable().padding(3);
    }

    public TableBuilder reset(int rowNum)
    {
        while (_table.getRowCount() > rowNum)
            _table.removeRow(rowNum);

        resetPos(rowNum);

        return this;
    }

    private void resetPos(int row)
    {
        _row = row;
        _col = 0;
        _colSpan = 1;
    }

    // Table methods ============================================================

    /**
     * Sets the table's &lt;caption&gt;.
     */
    public TableBuilder caption(String caption)
    {
        WidgetFactory.addCaption(_table, caption);

        return this;
    }

    /**
     * Sets the table's <code>cellspacing</code> attribute.
     */
    public TableBuilder spacing(int spacing)
    {
        _table.setCellSpacing(spacing);

        return this;
    }

    /**
     * Sets the table's <code>cellpadding</code> attribute.
     */
    public TableBuilder padding(int padding)
    {
        _table.setCellPadding(padding);

        return this;
    }

    /**
     * Sets the table's width.
     */
    public TableBuilder widthT(String width)
    {
        _table.setWidth(width);

        return this;
    }

    /**
     * Sets the table's height.
     */
    public TableBuilder heightT(String height)
    {
        _table.setHeight(height);

        return this;
    }

    /**
     * Sets the table's width and height.
     */
    public TableBuilder sizeT(String width, String height)
    {
        _table.setWidth(width);
        _table.setHeight(height);

        return this;
    }

    /**
     * Sets the table's primary style name.
     */
    public TableBuilder styleT(String styleName)
    {
        if (!StringUtils.isEmpty(styleName))
            _table.setStylePrimaryName(styleName);

        return this;
    }

    /**
     * Sets the table's border size.
     */
    public TableBuilder border(int width)
    {
        _table.setBorderWidth(width);

        return this;
    }

    /**
     * @return The table currently being built.
     */
    public FlexTable getTable()
    {
        return _table;
    }

    // Switch methods ============================================================

    /**
     * Turns the horizontal stretch switch on and implicitly turns the vertical stretch switch off.
     */
    public TableBuilder hStretch()
    {
        _hStretch = true;
        _vStretch = false;

        return this;
    }

    /**
     * Turns the vertical stretch switch on and implicitly turns the horizontal stretch switch off.
     */
    public TableBuilder vStretch()
    {
        _hStretch = false;
        _vStretch = true;

        return this;
    }

    /**
     * Turns both the horizontal and vertical stretch switches on.
     */
    public TableBuilder stretch()
    {
        _hStretch = _vStretch = true;

        return this;
    }

    /**
     * Turns both the horizontal and vertical stretch switches off.
     */
    public TableBuilder dontStretch()
    {
        _hStretch = _vStretch = false;

        return this;
    }

    /**
     * Turns the row style switch on.
     */
    public TableBuilder applyRowStyle()
    {
        _applyRowStyle = true;

        setRowStyle();

        return this;
    }

    /**
     * Turns row style switch on and swaps the even and odd row style names.
     */
    public TableBuilder swapRowStyle()
    {
        _swapRowStyle = !_swapRowStyle;

        return applyRowStyle();
    }

    /**
     * Turns the row style switch off.
     */
    public TableBuilder dontApplyRowStyle()
    {
        _applyRowStyle = false;
        _swapRowStyle = false;

        return this;
    }

    // Cell methods ==============================================================

    /**
     * Sets the cell's width.
     */
    public TableBuilder widthC(String width)
    {
        _table.getFlexCellFormatter().setWidth(_row, _col, width);

        return this;
    }

    /**
     * Sets the cell's height.
     */
    public TableBuilder heightC(String height)
    {
        _table.getFlexCellFormatter().setHeight(_row, _col, height);

        return this;
    }

    /**
     * Sets the cell's width and height.
     */
    public TableBuilder sizeC(String width, String height)
    {
        _table.getFlexCellFormatter().setWidth(_row, _col, width);
        _table.getFlexCellFormatter().setHeight(_row, _col, height);

        return this;
    }

    /**
     * Allows the cell's contents to wrap.
     */
    public TableBuilder wrap()
    {
        return wrap(true);
    }

    /**
     * Disallows the cell's contents to wrap.
     */
    public TableBuilder dontWrap()
    {
        return wrap(false);
    }

    private TableBuilder wrap(boolean wrap)
    {
        _table.getFlexCellFormatter().setWordWrap(_row, _col, wrap);

        return this;
    }

    /**
     * Horizontally aligns the cell's contents left justified.
     */
    public TableBuilder alignLeftC()
    {
        _table.getFlexCellFormatter().setHorizontalAlignment(_row, _col, ALIGN_LEFT);

        return this;
    }

    /**
     * Horizontally aligns the cell's contents centered.
     */
    public TableBuilder alignCenterC()
    {
        _table.getFlexCellFormatter().setHorizontalAlignment(_row, _col, ALIGN_CENTER);

        return this;
    }

    /**
     * Horizontally aligns the cell's contents right justified.
     */
    public TableBuilder alignRightC()
    {
        _table.getFlexCellFormatter().setHorizontalAlignment(_row, _col, ALIGN_RIGHT);

        return this;
    }

    /**
     * Vertically aligns the cell's contents top justified.
     */
    public TableBuilder alignTopC()
    {
        _table.getFlexCellFormatter().setVerticalAlignment(_row, _col, ALIGN_TOP);

        return this;
    }

    /**
     * Vertically aligns the cell's contents centered.
     */
    public TableBuilder alignMiddleC()
    {
        _table.getFlexCellFormatter().setVerticalAlignment(_row, _col, ALIGN_MIDDLE);

        return this;
    }

    /**
     * Vertically aligns the cell's contents bottom justified.
     */
    public TableBuilder alignBottomC()
    {
        _table.getFlexCellFormatter().setVerticalAlignment(_row, _col, ALIGN_BOTTOM);

        return this;
    }

    /**
     * Sets the cell's style name.
     */
    public TableBuilder styleC(String styleName)
    {
        if (!StringUtils.isEmpty(styleName))
            _table.getFlexCellFormatter().setStyleName(_row, _col, styleName);

        return this;
    }

    /**
     * Sets the cell's row span.
     */
    public TableBuilder rowSpan(int rowSpan)
    {
        return span(rowSpan, 1);
    }

    /**
     * Sets the cell's col span.
     */
    public TableBuilder colSpan(int colSpan)
    {
        return span(1, colSpan);
    }

    /**
     * Sets the cell's row and col span.
     */
    public TableBuilder span(int rowSpan, int colSpan)
    {
        if (rowSpan > 1)
            _table.getFlexCellFormatter().setRowSpan(_row, _col, rowSpan);

        if (colSpan > 1)
            _table.getFlexCellFormatter().setColSpan(_row, _col, colSpan);

        _colSpan = colSpan;

        return this;
    }

    /**
     * Sets the cell's content to the provided text.
     */
    public TableBuilder add(String text)
    {
        _table.setText(_row, nextCol(), text);

        return this;
    }

    /**
     * Sets the cell's content to the provided HTML.
     */
    public TableBuilder addHTML(String html)
    {
        _table.setHTML(_row, nextCol(), html);

        return this;
    }

    /**
     * Sets the cell's content to the provided widget.
     */
    public TableBuilder add(Widget widget)
    {
        if (_hStretch)
            widget.setWidth("100%");

        if (_vStretch)
            widget.setHeight("100%");

        _table.setWidget(_row, nextCol(), widget);

        return this;
    }

    /**
     * Sets the cell's content to the requested number of non-breaking spaces (&amp;nbsp;).
     */
    public TableBuilder space(int spaces)
    {
        StringBuilder b = new StringBuilder("&nbsp;");

        for (int i = 1; i < spaces; i++)
            b.append("&nbsp;");

        _table.setHTML(_row, nextCol(), b.toString());

        return this;
    }

    private int nextCol()
    {
        int col = _col;

        _col += _colSpan;
        _colSpan = 1;

        return col;
    }

    // Row methods ================================================================

    /**
     * Moves the pointer to the next row.
     */
    public TableBuilder nextRow()
    {
        _row++;
        _col = 0;

        return setRowStyle();
    }

    /**
     * Removes the requested row from the table.
     */
    public TableBuilder removeRow(int index)
    {
        _table.removeRow(index);
        _row--;

        return this;
    }

    private TableBuilder setRowStyle()
    {
        if (!_applyRowStyle)
            return this;

        int rowNum = _swapRowStyle ? (_row + 1) % 2 : _row % 2;

        _table.getRowFormatter().setStyleName(_row, rowNum == 0 ? "evenRow" : "oddRow");

        return this;
    }

    /**
     * Sets the row's primary style name.
     */
    public TableBuilder styleR(String styleName)
    {
        if (!StringUtils.isEmpty(styleName))
            _table.getRowFormatter().setStylePrimaryName(_row, styleName);

        return this;
    }

    /**
     * Removes the rows's style name.
     */
    public TableBuilder removeRowStyle(String styleName)
    {
        if (!StringUtils.isEmpty(styleName))
            _table.getRowFormatter().removeStyleName(_row,
                _table.getRowFormatter().getStylePrimaryName(_row) + '-' + styleName);

        return this;
    }
}
