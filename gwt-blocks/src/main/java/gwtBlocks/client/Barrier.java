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

import com.google.gwt.user.client.Command;

/**
 * @author hkrishna
 */
public class Barrier
{
    private int     _pendingParties;
    private Command _proceedTask;
    private boolean _failed;

    public Barrier(int parties)
    {
        _pendingParties = parties;
    }
    
    public void reset(int parties)
    {
        _pendingParties = parties;
        _proceedTask = null;
        _failed = false;
    }

    public void arrive()
    {
        if (_failed || _pendingParties == 0)
            return;

        _pendingParties--;

        if (_pendingParties == 0)
        {
            try
            {
                proceed();
                
                if (_proceedTask != null)
                    _proceedTask.execute();
            }
            finally
            {
                reset(0);
            }
        }
    }

    public void onProceed(Command task)
    {
        if (_pendingParties == 0)
            task.execute();
        else
            _proceedTask = task;
    }

    public void failed()
    {
        _failed = true;
    }

    public boolean isComplete()
    {
        return _pendingParties == 0;
    }

    protected void proceed()
    {
        // Template method
    }
}
