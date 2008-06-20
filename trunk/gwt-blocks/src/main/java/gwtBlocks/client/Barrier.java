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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Command;

/**
 * @author hkrishna
 */
public abstract class Barrier
{
    private int                 _pendingParties;
    private Map<String, Object> _resultMap;
    private List<Command>       _taskList;
    private boolean             _failed;

    public Barrier(int parties)
    {
        _pendingParties = parties;
    }

    public void arrive(String party, Object... results)
    {
        if (_failed || _pendingParties == 0)
            return;

        Map<String, Object> resultMap = getResultsMap();
        
        if (results != null && results.length > 0)
            resultMap.put(party, results.length == 1 ? results[0] : results);

        _pendingParties--;

        if (_pendingParties == 0)
        {
            _resultMap = null;
            proceed(resultMap);

            if (_taskList != null)
            {
                for (Iterator<Command> itr = _taskList.iterator(); itr.hasNext();)
                    itr.next().execute();

                _taskList = null;
            }
        }
    }

    public void onProceedExecute(Command task)
    {
        if (_pendingParties == 0)
            task.execute();
        else
            getTaskList().add(task);
    }

    public void failed(String name)
    {
        _failed = true;
        _resultMap = null;
        _taskList = null;
    }

    public boolean isComplete()
    {
        return _pendingParties == 0;
    }
    
    private Map<String, Object> getResultsMap()
    {
        if (_resultMap == null)
            _resultMap = new HashMap<String, Object>();

        return _resultMap;
    }

    private List<Command> getTaskList()
    {
        if (_taskList == null)
            _taskList = new ArrayList<Command>();

        return _taskList;
    }

    protected abstract void proceed(Map<String, Object> results);
}
