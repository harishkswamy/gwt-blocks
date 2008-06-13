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
package gwtBlocks.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author hkrishna
 */
public abstract class FeedbackAsyncCallback<T> implements AsyncCallback<T>
{
    private static ProcessingIndicator _indicator;
    private static int                 _callCount;

    public static void setProcessingIndicator(ProcessingIndicator indicator)
    {
        _indicator = indicator;
    }

    private String  _name;
    private Barrier _barrier;

    public FeedbackAsyncCallback()
    {
        beginCall();
    }

    public FeedbackAsyncCallback(String name, Barrier barrier)
    {
        this();

        _name = name;
        _barrier = barrier;
    }

    private void beginCall()
    {
        synchronized (this)
        {
            _callCount++;
        }
        _indicator.setVisible(true);
    }

    private void endCall()
    {
        synchronized (this)
        {
            _callCount--;
        }
        if (_callCount == 0)
            _indicator.setVisible(false);
    }

    public void onFailure(Throwable caught)
    {
        endCall();
        callFailed(caught);

        if (_barrier != null)
            _barrier.failed(_name);
    }

    public void onSuccess(T result)
    {
        try
        {
            callPassed(result);

            if (_barrier != null)
                _barrier.arrive(_name, result);
        }
        finally
        {
            endCall();
        }
    }

    protected abstract void callPassed(T result);

    protected abstract void callFailed(Throwable caught);
}
