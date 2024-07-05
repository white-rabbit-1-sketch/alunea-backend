package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V999999__Fixtures extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        String sql = """
            INSERT INTO `user` VALUES ('7088428b-76f4-4dd8-9eaf-c0d8015ba8ca','user1@external.com',1,'{bcrypt}$2a$10$ciC..Kq2GNF2FFKyzvI.Gu09uOeFhXREHWBkLp1cYX8rBnLz.PjeK','ROLE_USER',NULL,NULL,'en','hard-replace','2024-05-26 11:06:36','2024-05-26 11:06:36',0),('97b27e20-c649-47fb-87e7-dc6266122bdc','user2@external.com',1,'{bcrypt}$2a$10$DWH0AzcUOVrg0A8IZFa4IOnISFHwGd3J7cHwBPRKGrujAsB4PwN9e','ROLE_USER',NULL,NULL,'en','hard-replace','2024-05-26 11:08:26','2024-05-26 11:08:26',0);
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO `mailbox` VALUES ('51ac796b-60e3-44e1-b8f1-c4c3e1bd44df','7088428b-76f4-4dd8-9eaf-c0d8015ba8ca','user1-mailbox1@external.com',1,1,0,'2024-05-26 11:06:36'),('d472ffb6-67e6-47f0-8ce3-65350169ef29','97b27e20-c649-47fb-87e7-dc6266122bdc','user2-mailbox1@external.com',1,1,0,'2024-05-26 11:08:26');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO `domain` VALUES ('56ae60e4-2664-4122-b5cd-b64c6b962f33','subdomain','user2-subdomain1.silvermail.me',1,0,'2024-05-26 11:08:58'),('abfc8745-7240-4d19-beb9-ce244b14a630','subdomain','user1-subdomain1.silvermail.me',1,0,'2024-05-26 11:07:39');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO `subdomain` VALUES ('56ae60e4-2664-4122-b5cd-b64c6b962f33','user2-subdomain1','97b27e20-c649-47fb-87e7-dc6266122bdc','58c2107c-6a32-4896-988b-3140f00ced0d'),('abfc8745-7240-4d19-beb9-ce244b14a630','user1-subdomain1','7088428b-76f4-4dd8-9eaf-c0d8015ba8ca','58c2107c-6a32-4896-988b-3140f00ced0d');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO `alias` VALUES ('5b062392-7992-4703-8940-83629e893c63','mailbox','97b27e20-c649-47fb-87e7-dc6266122bdc','56ae60e4-2664-4122-b5cd-b64c6b962f33','user2-mailboxalias1','user2-mailboxalias1@user2-subdomain1.silvermail.me',1,0,'2024-05-26 11:09:08'),('87aa8a90-a5b1-4cda-b2f0-799128ae90f4','mailbox','7088428b-76f4-4dd8-9eaf-c0d8015ba8ca','abfc8745-7240-4d19-beb9-ce244b14a630','user1-mailboxalias1','user1-mailboxalias1@user1-subdomain1.silvermail.me',1,0,'2024-05-26 11:08:01');   
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO `mailbox_alias` VALUES ('87aa8a90-a5b1-4cda-b2f0-799128ae90f4','51ac796b-60e3-44e1-b8f1-c4c3e1bd44df'),('5b062392-7992-4703-8940-83629e893c63','d472ffb6-67e6-47f0-8ce3-65350169ef29');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO contact (id, user_id, email, is_enabled, create_time) VALUES ('a2ea650f-8fd1-4750-ae28-fafc7fc1cb2c', '97b27e20-c649-47fb-87e7-dc6266122bdc', 'user2-contact1@external.com', 1, '2024-05-26 17:33:12');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO contact (id, user_id, email, is_enabled, create_time) VALUES ('fb68a0d3-1cc0-480f-baa7-2bacd91f12c1', '7088428b-76f4-4dd8-9eaf-c0d8015ba8ca', 'user1-contact1@external.com', 1, '2024-05-26 17:32:01');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO contact (id, user_id, email, is_enabled, create_time) VALUES ('fa8982e2-f127-4378-995f-7a062bd4f1d3', '7088428b-76f4-4dd8-9eaf-c0d8015ba8ca', 'user1-mailbox1@external.com', 1, '2024-05-26 19:30:28'); 
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO alias (id, type, user_id, domain_id, recipient, value, is_enabled, create_time) VALUES ('a2256172-c9a4-43f7-9c95-bb12b5abb20b', 'contact', '97b27e20-c649-47fb-87e7-dc6266122bdc', '56ae60e4-2664-4122-b5cd-b64c6b962f33', 'user2-contactalias1', 'user2-contactalias1@user2-subdomain1.silvermail.me', 1, '2024-05-26 17:33:23');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO alias (id, type, user_id, domain_id, recipient, value, is_enabled, create_time) VALUES ('f718552a-16bc-46b7-af76-d6aa351aaa17', 'contact', '7088428b-76f4-4dd8-9eaf-c0d8015ba8ca', 'abfc8745-7240-4d19-beb9-ce244b14a630', 'user1-contactalias1', 'user1-contactalias1@user1-subdomain1.silvermail.me', 1, '2024-05-26 17:32:41');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO alias (id, type, user_id, domain_id, recipient, value, is_enabled, create_time) VALUES ('2b55b04f-9e00-477b-8db9-8df0dc239d16', 'contact', '7088428b-76f4-4dd8-9eaf-c0d8015ba8ca', 'abfc8745-7240-4d19-beb9-ce244b14a630', 'user1-mailboxalias2', 'user1-contactalias2@user1-subdomain1.silvermail.me', 1, '2024-05-26 19:30:54');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO contact_alias (id, contact_id, mailbox_alias_id) VALUES ('a2256172-c9a4-43f7-9c95-bb12b5abb20b', 'a2ea650f-8fd1-4750-ae28-fafc7fc1cb2c', '5b062392-7992-4703-8940-83629e893c63');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO contact_alias (id, contact_id, mailbox_alias_id) VALUES ('2b55b04f-9e00-477b-8db9-8df0dc239d16', 'fa8982e2-f127-4378-995f-7a062bd4f1d3', '87aa8a90-a5b1-4cda-b2f0-799128ae90f4');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            INSERT INTO contact_alias (id, contact_id, mailbox_alias_id) VALUES ('f718552a-16bc-46b7-af76-d6aa351aaa17', 'fb68a0d3-1cc0-480f-baa7-2bacd91f12c1', '87aa8a90-a5b1-4cda-b2f0-799128ae90f4');    
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }
    }
}