/*
 * Copyright (c) 2014-2015, Argonne National Laboratory.
 *
 * SVN Information:
 *   $HeadURL: $
 *   $Date: $
 *   $Revision: $
 *   $Author: $
 */
package gov.anl.aps.cdb.common.exceptions;

import gov.anl.aps.cdb.common.constants.CdbStatus;

/**
 * Configuration error exception.
 */
public class ConfigurationError extends CdbException {

    /**
     * Default constructor.
     */
    public ConfigurationError() {
        super();
        setErrorCode(CdbStatus.CDB_CONFIGURATION_ERROR);
    }

    /**
     * Constructor using error message.
     *
     * @param message error message
     */
    public ConfigurationError(String message) {
        super(message);
        setErrorCode(CdbStatus.CDB_CONFIGURATION_ERROR);
    }

    /**
     * Constructor sing throwable object.
     *
     * @param throwable throwable object
     */
    public ConfigurationError(Throwable throwable) {
        super(throwable);
        setErrorCode(CdbStatus.CDB_CONFIGURATION_ERROR);
    }

    /**
     * Constructor using error message and throwable object.
     *
     * @param message error message
     * @param throwable throwable object
     */
    public ConfigurationError(String message, Throwable throwable) {
        super(message, throwable);
        setErrorCode(CdbStatus.CDB_CONFIGURATION_ERROR);
    }

}
