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

import gwtBlocks.client.ValueChangeListener;
import gwtBlocks.client.models.ValueModel;

import com.google.gwt.user.client.ui.Composite;

/**
 * @author hkrishna
 */
public abstract class BaseView<M extends ValueModel<?>> extends Composite implements ValueChangeListener<M>
{
    private M _model;

    public void setModel(M model)
    {
        if (_model != null)
            _model.removeChangeListener(this);

        _model = model;

        if (_model != null)
        {
            _model.registerChangeListener(this);
            valueChanged(_model);
        }
    }

    public M getModel()
    {
        return _model;
    }

    public void valueChanged(M model)
    {
    }
}