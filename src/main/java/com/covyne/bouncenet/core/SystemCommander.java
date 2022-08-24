package com.covyne.bouncenet.core;

import com.covyne.bouncenet.datastore.IDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.IOException;
import java.util.Optional;

@Component
@ApplicationScope
public class SystemCommander {

    private static final String MONGODB_URI = "MONGODB_URI";
    private static final String GOOGLE_OAUTH2_CLIENT_ID = "GOOGLE_OAUTH2_CLIENT_ID";
    private static final String GOOGLE_OAUTH2_CLIENT_SECRET = "GOOGLE_OAUTH2_CLIENT_SECRET";

    @Value("${com.covyne.bouncenet.core.environmentsettings.onerrorshutdowndelay}")
    private int errorShutdownDelay;

    @Value("${com.covyne.bouncenet.mongo.connectionuri}")
    private String mongoDBUri;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleOAuthClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleOAuthClientId;

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private ApplicationContext appContext;

    private final Logger logger = LoggerFactory.getLogger(SystemCommander.class);

    @Autowired
    private IDataStore dataStore;

    private static boolean shudownInProgress;

    @EventListener(ApplicationReadyEvent.class)
    protected void runChecks() {
        printBanner();
        logger.info("Starting System Checks...");
        logger.info("Validating Environment Settings...");
        validateSettings();
        logger.info("Environment Settings Check Complete.");
        logger.info("Checking datastore...");
        checkDataStore();
        logger.info("Datastore Checks Complete.");
        logger.info("System Checks Complete.");
    }

    private void printBanner() {

        final String buildInfoMessage = String.format(" ~<| %s (v%s) |>~", buildProperties.getName(), buildProperties.getVersion());

        logger.info("");
        logger.info("");
        logger.info("  ____                                          _   _          _   ");
        logger.info(" |  _  \\                                       | \\ | |        | |  ");
        logger.info(" | |_) |   ___    _   _   _ __     ___    ___  |  \\| |   ___  | |_ ");
        logger.info(" |  _ <   / _ \\  | | | | | '_ \\   / __|  / _ \\ | . ` |  / _ \\ | __|");
        logger.info(" | |_) | | (_) | | |_| | | | | | | (__  |  __/ | |\\  | |  __/ | |_ ");
        logger.info(" |____/   \\___/   \\__,_| |_| |_|  \\___|  \\___| |_| \\_|  \\___|  \\__|");
        logger.info(buildInfoMessage);
        logger.info("");
    }

    private void validateSettings() {

        final String missingVariable;

        if (!StringUtils.hasText(googleOAuthClientSecret)) {
            missingVariable = GOOGLE_OAUTH2_CLIENT_SECRET;
        } else if (!StringUtils.hasText(googleOAuthClientId)) {
            missingVariable = GOOGLE_OAUTH2_CLIENT_ID;
        } else if (!StringUtils.hasText(mongoDBUri) || !mongoDBUri.startsWith("mongodb")) {
            missingVariable = MONGODB_URI;
        } else {
            missingVariable = "";
        }

        if (missingVariable.length() > 0) {
            String message = String.format("FATAL: \"%s\" Environment Variable or System Property not found. " +
                    "See docs at https://github.com/naishtech/bouncenet for more details. Exiting!", missingVariable);
            logger.error(message);
            forceShutdown();
        }

        printEnvironmentSettings();


    }

    private void printEnvironmentSettings() {

        printEnvironmentSetting(MONGODB_URI, mongoDBUri);
        printEnvironmentSetting(GOOGLE_OAUTH2_CLIENT_ID, googleOAuthClientId);
        printEnvironmentSetting(GOOGLE_OAUTH2_CLIENT_SECRET, googleOAuthClientSecret);

    }

    private void printEnvironmentSetting(final String key, final String val) {

        Optional.ofNullable(val).ifPresent(value -> logger.info(String.format("%s set.", key)));
    }

    public void checkDataStore() {

        try {
            dataStore.healthCheck();
        } catch (IOException ex) {
            String message = String.format("FATAL: \"%s\" Data Store Checks Failed. " +
                    "See docs at https://github.com/naishtech/bouncenet for more details. Exiting!", ex.getMessage());
            logger.error(message);
            forceShutdown();
        }
    }

    public void forceShutdown() {

        if (shudownInProgress) {
            return;
        }
        shudownInProgress = true;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        ((ConfigurableApplicationContext) appContext).close();
                    }
                },
                errorShutdownDelay
        );

    }

}
