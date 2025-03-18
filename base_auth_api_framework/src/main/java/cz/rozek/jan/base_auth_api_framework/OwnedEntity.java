package cz.rozek.jan.base_auth_api_framework;

public interface OwnedEntity extends Entity {
    String getOwnerId();
    void setOwnerId(String ownerId);
}
