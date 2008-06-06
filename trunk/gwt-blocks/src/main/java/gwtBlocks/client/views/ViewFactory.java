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

import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author hkrishna
 */
public class ViewFactory
{
    public <M extends InputModel<? extends String>> TextBoxView<TextBox, M> newStringBox(M model, String name)
    {
        model.setName(name);

        TextBox widget = new TextBox();
        widget.addStyleDependentName("String");

        return new TextBoxView<TextBox, M>(widget, model);
    }

    public <M extends InputModel<? extends Number>> TextBoxView<TextBox, M> newNumberBox(M model, String name)
    {
        model.setName(name);

        TextBox widget = new TextBox();
        widget.addStyleDependentName("Number");

        return new TextBoxView<TextBox, M>(widget, model);
    }

    public <M extends InputModel<? extends String>> TextBoxView<PasswordTextBox, M> newPasswordBox(M model, String name)
    {
        model.setName(name);

        return new TextBoxView<PasswordTextBox, M>(new PasswordTextBox(), model);
    }

    public <M extends InputModel<? extends String>> TextBoxView<TextArea, M> newTextArea(M model, String name)
    {
        model.setName(name);

        return new TextBoxView<TextArea, M>(new TextArea(), model);
    }
}
