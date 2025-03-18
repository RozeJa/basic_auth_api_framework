package cz.rozek.jan.base_auth_api_framework;

import cz.rozek.jan.base_auth_api_framework.exceptions.ValidationException;

public interface Entity {  
    String getId();
    void setId(String id);

    boolean isEnabled();
    void setEnabled(boolean enabled);

    void validate() throws ValidationException;
}