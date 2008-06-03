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

import com.google.gwt.user.client.ui.TextBox;

/**
 * @author hkrishna
 */
public class TextBoxView<M extends InputModel<?>> extends TextBoxBaseView<M>
{
    private static TextBox buildTextBox()
    {
        TextBox textBox = new TextBox();
        textBox.addStyleName("cvg-text-box");

        return textBox;
    }

    TextBoxView(M model)
    {
        super(buildTextBox(), model);
    }

    public TextBox getTextBox()
    {
        return (TextBox) getTextWidget();
    }
}
