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

import gwtBlocks.client.models.BaseModel;

import com.google.gwt.user.client.ui.Label;

/**
 * @author hkrishna
 */
public class DynamicLabelView extends BaseView<Label, BaseModel<String>>
{
    public DynamicLabelView(BaseModel<String> model)
    {
        super(model);
    }

    @Override
    protected Label buildView(BaseModel<String> model, Object... args)
    {
        return new Label();
    }
    
    @Override
    public void valueChanged(BaseModel<String> model)
    {
        getWidget().setText(model.getValue());
    }
}
