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

import gwtBlocks.client.models.InputModel;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author hkrishna
 */
public class TextBoxBaseView<M extends InputModel<?>> extends BaseView<M> implements CanEnable
{
    private TextBoxBase _textWidget;

    public TextBoxBaseView(TextBoxBase textWidget, M model)
    {
        _textWidget = textWidget;
        _textWidget.addChangeListener(new ChangeListener()
        {
            public void onChange(Widget sender)
            {
                getModel().setText(_textWidget.getText());
            }
        });

        initWidget(_textWidget);
        setModel(model);
    }

    protected TextBoxBase getTextWidget()
    {
        return _textWidget;
    }

    public void valueChanged(M model)
    {
        _textWidget.setText(model.getText());
    }

    public void setEnabled(boolean flag)
    {
        _textWidget.setEnabled(flag);
    }
}
