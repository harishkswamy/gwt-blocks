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

import com.google.gwt.user.client.ui.FlexTable;

/**
 * @author hkrishna
 */
public class FlexTableBuilder extends HTMLTableBuilder<FlexTable>
{
    public FlexTableBuilder()
    {
        newTable();
    }
    
    public FlexTableBuilder(FlexTable table)
    {
        build(table);
    }

    public FlexTableBuilder newTable()
    {
        return (FlexTableBuilder) build(WidgetFactory.newFlexTable());
    }

    @Override
    public FlexTableBuilder reset(int rowNum)
    {
        while (_table.getRowCount() > rowNum)
            _table.removeRow(rowNum);

        movePointer(rowNum);

        return this;
    }

    /**
     * Removes the requested row from the table.
     */
    @Override
    public FlexTableBuilder removeRow(int index)
    {
        _table.removeRow(index);
        _row--;

        return this;
    }

    /**
     * Sets the cell's row span.
     */
    @Override
    public FlexTableBuilder rowSpan(int rowSpan)
    {
        return span(rowSpan, 1);
    }

    /**
     * Sets the cell's col span.
     */
    @Override
    public FlexTableBuilder colSpan(int colSpan)
    {
        return span(1, colSpan);
    }

    /**
     * Sets the cell's row and col span.
     */
    @Override
    public FlexTableBuilder span(int rowSpan, int colSpan)
    {
        if (rowSpan > 1)
            _table.getFlexCellFormatter().setRowSpan(_row, _col, rowSpan);

        if (colSpan > 1)
            _table.getFlexCellFormatter().setColSpan(_row, _col, colSpan);

        _colSpan = colSpan;

        return this;
    }
}
