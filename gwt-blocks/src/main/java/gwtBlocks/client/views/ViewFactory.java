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

/**
 * @author hkrishna
 */
public class ViewFactory
{
    public <M extends InputModel<String>> TextBoxView<M> newStringBox(M model, String name, String styleName)
    {
        model.setName(name);

        return new TextBoxView<M>(model);
    }

    public <M extends InputModel<? extends Number>> TextBoxView<M> newNumberBox(M model, String name, String styleName)
    {
        model.setName(name);

        return new TextBoxView<M>(model);
    }
}
