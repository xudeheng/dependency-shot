/*
 *  Copyright 2010 mathieuancelin.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package cx.ath.mancel01.dependencyshot.event;

import cx.ath.mancel01.dependencyshot.api.event.EventListener;
import cx.ath.mancel01.dependencyshot.api.event.Event;
import java.util.Collection;

/**
 * Thread that notify listener of the particular event.
 *
 * @author mathieuancelin
 */
public class EventBroadcastExecution implements Runnable  {

    private final Event event;

    private final Collection<EventListener> listeners;

    public EventBroadcastExecution(Event event, Collection<EventListener> listeners) {
        this.event = event;
        this.listeners = listeners;
    }

    @Override
    public void run() {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
