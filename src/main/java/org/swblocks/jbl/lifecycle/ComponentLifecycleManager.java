/*
 * This file is part of the swblocks-jbl library.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.swblocks.jbl.lifecycle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class to manage a set of {@link ComponentLifecycle} components.
 */
public class ComponentLifecycleManager implements ComponentLifecycle {
    private static final Comparator<ComponentLifecycle> PHASE_COMPARATOR =
            Comparator.comparingInt(ComponentLifecycle::getPhase);
    private final List<ComponentLifecycle> components;
    private boolean running = false;

    /**
     * Constructs the {@code ComponentLifecycleManager} with the {@link List} of {@link ComponentLifecycle} objects to
     * manage.
     *
     * @param components The {@link List} of {@link ComponentLifecycle} components managed by this listener
     */
    public ComponentLifecycleManager(final List<ComponentLifecycle> components) {
        this.components = Collections.unmodifiableList(components);
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            this.components.stream()
                    .sorted(PHASE_COMPARATOR)
                    .filter(listener -> !listener.isRunning()).forEach(ComponentLifecycle::start);
        }
        this.running = true;
    }

    @Override
    public void stop() {
        if (isRunning()) {
            this.components.stream()
                    .sorted(PHASE_COMPARATOR.reversed())
                    .filter(ComponentLifecycle::isRunning).forEach(ComponentLifecycle::stop);
        }
        this.running = false;
    }
}
