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
 * DB error exception.
 */
public class DbError extends CdbException {

    /**
     * Default constructor.
     */
    public DbError() {
        super();
        setErrorCode(CdbStatus.CDB_DB_ERROR);
    }

    /**
     * Constructor using error message.
     *
     * @param message error message
     */
    public DbError(String message) {
        super(message);
        setErrorCode(CdbStatus.CDB_DB_ERROR);
    }

    /**
     * Constructor using throwable object.
     *
     * @param throwable throwable object
     */
    public DbError(Throwable throwable) {
        super(throwable);
        setErrorCode(CdbStatus.CDB_DB_ERROR);
    }

    /**
     * Constructor using error message and throwable object.
     *
     * @param message error message
     * @param throwable throwable object
     */
    public DbError(String message, Throwable throwable) {
        super(message, throwable);
        setErrorCode(CdbStatus.CDB_DB_ERROR);
    }

}
