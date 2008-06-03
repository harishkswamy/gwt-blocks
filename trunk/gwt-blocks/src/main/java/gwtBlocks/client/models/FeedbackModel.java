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

/**
 * This is a {@link ValueModel} that wraps the {@link MessageModel} and has a name. This is a convenience model that can
 * be used to provide user feedback.
 * 
 * @author hkrishna
 */
public class FeedbackModel<V> extends ValueModel<V>
{
    private String       _name;
    private MessageModel _messageModel;

    public void setName(String name)
    {
        _name = name;
    }

    public String getName()
    {
        return _name;
    }

    public MessageModel getMessageModel()
    {
        return getParent() == null ? _messageModel : getParent().getMessageModel();
    }

    public void setMessageModel(MessageModel model)
    {
        _messageModel = model;
    }

    public void detach()
    {
        MessageModel msgModel = getMessageModel();

        if (msgModel == null)
            return;

        msgModel.clear(this);
    }
}
