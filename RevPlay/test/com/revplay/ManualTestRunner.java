package com.revplay;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import java.io.PrintWriter;

public class ManualTestRunner {
    public static void main(String[] args) {
        System.out.println("Starting Test Runner...");
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage("com.revplay.service"))
                .build();

        launcher.execute(request);

        listener.getSummary().printTo(new PrintWriter(System.out));
        System.out.println("Test Run Complete. Failures: " + listener.getSummary().getFailures().size());

        if (listener.getSummary().getFailures().size() > 0) {
            listener.getSummary().getFailures().forEach(failure -> {
                System.out.println("FAILURE: " + failure.getTestIdentifier().getDisplayName());
                failure.getException().printStackTrace();
            });
        }
    }
}
