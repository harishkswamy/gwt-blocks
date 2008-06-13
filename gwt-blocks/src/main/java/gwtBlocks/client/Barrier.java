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
    private int                 _parties;
    private Map<String, Object> _results;
    private List<Command>       _taskList;
    private boolean             _failed;

    public Barrier(int parties)
    {
        _parties = parties;
    }

    public void arrive(String party, Object result)
    {
        if (_failed)
            return;

        Map<String, Object> results = getResultsMap();
        results.put(party, result);

        _parties--;

        if (_parties == 0)
        {
            _results = null;
            proceed(results);

            if (_taskList != null)
            {
                for (Iterator<Command> itr = _taskList.iterator(); itr.hasNext();)
                    itr.next().execute();

                _taskList = null;
            }
        }
    }

    public void execute(Command task)
    {
        if (_parties == 0)
            task.execute();

        else
            getTaskList().add(task);
    }

    public void failed(String name)
    {
        _failed = true;
        _results = null;
        _taskList = null;
    }

    private Map<String, Object> getResultsMap()
    {
        if (_results == null)
            _results = new HashMap<String, Object>();

        return _results;
    }

    private List<Command> getTaskList()
    {
        if (_taskList == null)
            _taskList = new ArrayList<Command>();

        return _taskList;
    }

    protected abstract void proceed(Map<String, Object> results);
}
