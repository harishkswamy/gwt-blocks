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
package gwtBlocks.client.models;

import gwtBlocks.client.ValueChangeListener;

/**
 * This is the fundamental unit that forms the tree of models that support a composite view. Every composable model
 * becomes a part of the tree by holding on to its parent provided to it via {@link #setParent(String, CompositeModel)}.
 * The root model of the tree has no parent and so its parent is left null.
 * <p>
 * Every composable model holds a single value and has listeners, registered to it via
 * {@link #registerChangeListener(ValueChangeListener)}, that listen to changes to the model's value. Every parent
 * listens to its children and every child listens to its parent and there by enabling a decoupled solution.
 * 
 * @see CompositeModel
 * @see BaseModel
 * @see BaseView
 * 
 * @author hkrishna
 */
public interface ComposableModel<V>
{
    void setValue(V value);

    V getValue();

    void registerChangeListener(ValueChangeListener<? extends ComposableModel<V>> listener);

    void removeChangeListener(ValueChangeListener<? extends ComposableModel<V>> listener);

    void setParent(String key, CompositeModel<?> parent);

    <P extends CompositeModel<?>> P getParent();

    void parentValueChanged();
}
