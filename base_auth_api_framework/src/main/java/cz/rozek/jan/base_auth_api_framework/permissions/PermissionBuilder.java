package cz.rozek.jan.base_auth_api_framework.permissions;

public class PermissionBuilder {

    public PermissionBuild getPermission() {
        PermissionBuild pb = new PermissionBuild();
        return pb;
    }

    public final class PermissionBuild {
        private String name = "";
        private String action = "";

        public PermissionBuild setName(String name) {
            this.name = name;
            return this;
        }

        public PermissionBuild create() {
            action = "create";
            return this;
        }
        public PermissionBuild read() {
            action = "read";
            return this;
        }
        public PermissionBuild update() {
            action = "update";
            return this;
        }
        public PermissionBuild delete() {
            action = "delete";
            return this;
        }

        public PermissionBuild setAction(String action) {
            this.action = action;
            return this;
        }

        public Permission build() {
            Permission permission = new Permission();    

            return permission.setAction(action).setName(name);
        }
    }
}
