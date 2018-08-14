package com.hixel.hixel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import org.junit.Before;
import org.junit.Rule;


import org.mockito.MockitoAnnotations;


public class ClientTest {
    // Run on test thread
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        // Inject mocks
        MockitoAnnotations.initMocks(this);
    }

    // NOTE: No testing is required for this class yet, there is no logic to test.
    // If this changes ensure that you use MockWebServer to avoid calls to the server.
}
