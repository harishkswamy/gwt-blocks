package gwtBlocks.client.views;

import gwtBlocks.client.models.TableModel;

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * @author hkrishna
 */
public abstract class TableView<V> extends BaseView<TableView<V>.TableWidget, TableModel<V>>
{
    public static final int SCROLLABLE = 0x1;
    public static final int PAGEABLE   = 0x2;

    private class TableWidget extends ComplexPanel
    {
        private Element _headerDiv;
        private Element _bodyDiv;

        private TableWidget(FlexTable header, FlexTable body)
        {
            Element mainDiv = DOM.createDiv();
            DOM.setElementAttribute(mainDiv, "class", "gbk-TableView");
            setElement(mainDiv);

            // Header
            _headerDiv = DOM.createDiv();
            DOM.setElementAttribute(_headerDiv, "class", "header");

            // Body
            _bodyDiv = DOM.createDiv();
            DOM.setElementAttribute(_bodyDiv, "class", "body");

            if (isScrollable())
                DOM.setStyleAttribute(_bodyDiv, "overflow", "auto");
            else
                DOM.setStyleAttribute(_bodyDiv, "overflow", "visible");

            // Adopt
            ownTable(header, _headerDiv);
            ownTable(body, _bodyDiv);
        }

        private void ownTable(FlexTable table, Element parent)
        {
            // Adopt
            adoptTable(table, parent);

            // Personalize
            DOM.setStyleAttribute(table.getElement(), "table-layout", "fixed");
            DOM.setStyleAttribute(table.getElement(), "width", "100%");
        }

        private void adoptTable(FlexTable table, Element parent)
        {
            getChildren().add(table);

            parent.appendChild(table.getElement());

            getElement().appendChild(parent);

            adopt(table);
        }

        private void resetBody(FlexTable newBody)
        {
            remove(1); // Old body table
            ownTable(newBody, _bodyDiv);
        }

        @Override
        protected void onLoad()
        {
            super.onLoad();
            adjustWidth();
            adjustHeight();
        }

        @Override
        public void setWidth(String widthStr)
        {
            super.setWidth(widthStr);
            adjustWidth();
        }

        private void adjustWidth()
        {
            if (!isAttached() || !isScrollable())
                return;

            int width = getOffsetWidth();

            FlexTable header = (FlexTable) getWidget(0);
            FlexTable body = (FlexTable) getWidget(1);

            header.setWidth(width - 17 + "px");
            body.setWidth(width - 17 + "px");
        }

        @Override
        public void setHeight(String height)
        {
            super.setHeight(height);
            adjustHeight();
        }

        private void adjustHeight()
        {
            if (!isAttached() || !isScrollable())
                return;

            int height = getOffsetHeight();

            if (height > 0)
                DOM.setStyleAttribute(_bodyDiv, "height", (height - _headerDiv.getOffsetHeight()) + "px");
        }
    }

    private int              _features;
    private FlexTableBuilder _bodyBuilder;

    public TableView(TableModel<V> model)
    {
        super(model);
    }

    public TableView(TableModel<V> model, int features)
    {
        super(model, features);
    }

    @Override
    protected final TableWidget buildView(TableModel<V> model, Object... args)
    {
        if (args.length > 0)
            _features = (Integer) args[0];

        _bodyBuilder = new FlexTableBuilder();
        FlexTableBuilder b = _bodyBuilder;

        FlexTable header = b.getTable();

        initHeaderTable(b);
        buildHeader(b);

        FlexTable body = b.newTable().getTable();

        initBodyTable(b);

        return new TableWidget(header, body);
    }

    @Override
    public void valueChanged(TableModel<V> model)
    {
        super.valueChanged(model);

        refresh();
    }

    private void refresh()
    {
        clearBody();

        List<V> rows = getModel().getValue();

        if (rows == null)
            return;

        for (V row : rows)
        {
            buildBodyRow(_bodyBuilder, row);

            _bodyBuilder.applyRowStyle().nextRow();
        }
    }

    private void clearBody()
    {
        if (_bodyBuilder.getTable().getRowCount() == 0)
            return;

        FlexTableBuilder b = _bodyBuilder.newTable();

        initBodyTable(b);

        getWidget().resetBody(b.getTable());
    }

    @Override
    public void setSize(String width, String height)
    {
        getWidget().setSize(width, height);
    }

    @Override
    public void setWidth(String width)
    {
        getWidget().setWidth(width);
    }

    @Override
    public void setHeight(String height)
    {
        getWidget().setHeight(height);
    }

    public boolean isScrollable()
    {
        return (_features & SCROLLABLE) == SCROLLABLE;
    }

    public boolean isPageable()
    {
        return (_features & PAGEABLE) == PAGEABLE;
    }

    protected abstract void initHeaderTable(FlexTableBuilder builder);

    protected abstract void buildHeader(FlexTableBuilder builder);

    protected abstract void initBodyTable(FlexTableBuilder builder);

    protected abstract void buildBodyRow(FlexTableBuilder builder, V row);
}
