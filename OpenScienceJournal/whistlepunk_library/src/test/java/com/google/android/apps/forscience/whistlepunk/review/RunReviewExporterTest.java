/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.android.apps.forscience.whistlepunk.review;

import com.google.android.apps.forscience.whistlepunk.data.GoosciSensorLayout;
import com.google.android.apps.forscience.whistlepunk.metadata.ExperimentRun;
import com.google.android.apps.forscience.whistlepunk.metadata.Label;
import com.google.android.apps.forscience.whistlepunk.metadata.Run;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link RunReviewExporter}.
 */
public class RunReviewExporterTest {
    @Test
    public void testSanitizeFileName_normal() {
        String experimentName = "Untitled Experiment";
        String runName = "Run 1";

        assertEquals("Untitled Experiment Run 1", RunReviewExporter.sanitizeFilename(
                experimentName + " " + runName));
    }

    @Test
    public void testSanitizeFileName_evil() {
        String experimentName = "//Untitled Experiment";
        String runName = "Run 1";

        assertEquals("__Untitled Experiment Run 1", RunReviewExporter.sanitizeFilename(
                experimentName + " " + runName));

        experimentName = "U.nti\tled Ex. periment";
        runName = "Run 10";

        assertEquals("U.nti_led Ex. periment Run 10", RunReviewExporter.sanitizeFilename(
                experimentName + " " + runName));
    }

    @Test public void makeFilenameWhenShort() {
        ExperimentRun erun = makeExperimentRun("runTitle");
        String filename = RunReviewExporter.makeExportFilename("experiment", erun, null);
        assertEquals("experiment runTitle.csv", filename);
    }

    @Test
    public void makeFilenameWhenTooLong() {
        String veryLongString = "012345678901234567890123456789012345678901234567890123456789";
        ExperimentRun erun = makeExperimentRun("runTitle" + veryLongString);

        String filename =
                RunReviewExporter.makeExportFilename("experiment" + veryLongString, erun, null);
        assertEquals(80, filename.length());
        assertEquals(
                "experiment01234567890123456789018953cd53 runTitle0123456789012345678a7c9ddcd.csv",
                filename);
    }

    private ExperimentRun makeExperimentRun(String runTitle) {
        Run run = new Run("runId", 0, new ArrayList<GoosciSensorLayout.SensorLayout>(), false);
        run.setTitle(runTitle);
        return ExperimentRun.fromLabels(run, new ArrayList<Label>());
    }


}
