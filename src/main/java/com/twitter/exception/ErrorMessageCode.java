package com.twitter.exception;

public enum ErrorMessageCode {

    ERROR_DB_001("The entity has not been found in DB"),
    ERROR_DB_002("After insert in DB, ID doesn't exist "),
    ERROR_DB_003("The ID is mandatory to can upsert/delete counter companies"),
    ERROR_DB_004("Error when delete entities in database"),
    ERROR_DB_005("No content - Element not found in DB"),
    ERROR_SERVICE_001("Error with unexpected in our service"),
    ERROR_DB_DUPLICATED("A %s with the same key does already exist"),
    REMOTE_SERVICE_ERROR("Remote service error"),
    REMOTE_SERVICE_TIMEOUT_ERROR("Remote service timeout error"),
    REMOTE_SERVICE_BAD_REQUEST_ERROR("Remote service bad request error");

    public final String name;

    ErrorMessageCode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
