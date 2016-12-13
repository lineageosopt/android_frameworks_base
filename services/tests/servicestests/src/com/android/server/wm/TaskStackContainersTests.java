/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.server.wm;

import static android.app.ActivityManager.StackId.PINNED_STACK_ID;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.platform.test.annotations.Presubmit;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link DisplayContent.TaskStackContainers} container in {@link DisplayContent}.
 *
 * Build/Install/Run:
 *  bit FrameworksServicesTests:com.android.server.wm.TaskStackContainersTests
 */
@SmallTest
@Presubmit
@RunWith(AndroidJUnit4.class)
public class TaskStackContainersTests extends WindowTestsBase {

    @Test
    public void testStackPositionChildAt() throws Exception {
        // Test that always-on-top stack can't be moved to position other than top.
        final TaskStack stack1 = createTaskStackOnDisplay(sDisplayContent);
        final TaskStack stack2 = createTaskStackOnDisplay(sDisplayContent);
        sDisplayContent.addStackToDisplay(PINNED_STACK_ID, true);
        final TaskStack pinnedStack = sWm.mStackIdToStack.get(PINNED_STACK_ID);

        final WindowContainer taskStackContainer = stack1.getParent();

        final int stack1Pos = taskStackContainer.mChildren.indexOf(stack1);
        final int stack2Pos = taskStackContainer.mChildren.indexOf(stack2);
        final int pinnedStackPos = taskStackContainer.mChildren.indexOf(pinnedStack);

        taskStackContainer.positionChildAt(WindowContainer.POSITION_BOTTOM, pinnedStack, false);
        assertEquals(taskStackContainer.mChildren.get(stack1Pos), stack1);
        assertEquals(taskStackContainer.mChildren.get(stack2Pos), stack2);
        assertEquals(taskStackContainer.mChildren.get(pinnedStackPos), pinnedStack);

        taskStackContainer.positionChildAt(1, pinnedStack, false);
        assertEquals(taskStackContainer.mChildren.get(stack1Pos), stack1);
        assertEquals(taskStackContainer.mChildren.get(stack2Pos), stack2);
        assertEquals(taskStackContainer.mChildren.get(pinnedStackPos), pinnedStack);
    }
}
