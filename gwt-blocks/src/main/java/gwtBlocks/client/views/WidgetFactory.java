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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author hkrishna
 */
public class WidgetFactory
{
    public static final FlexTable newFlexTable()
    {
        FlexTable table = new FlexTable();
        table.setCellSpacing(0);
        table.setCellPadding(0);

        return table;
    }

    public static final FlexTable addCaption(FlexTable table, String caption)
    {
        if (caption == null)
            return table;

        Element captionEle = DOM.createCaption();
        DOM.setInnerText(captionEle, caption);
        DOM.appendChild(table.getElement(), captionEle);

        return table;
    }

    public static DecoratorPanel newDecoratorPanel(Widget widget)
    {
        DecoratorPanel dPanel = new DecoratorPanel();
        dPanel.setWidget(widget);

        return dPanel;
    }
}
