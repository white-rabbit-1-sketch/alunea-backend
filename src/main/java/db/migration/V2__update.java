package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V2__update extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        String sql = """
            TRUNCATE `token`;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            TRUNCATE `user_auth_token`;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            TRUNCATE `user_password_reset_token`;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            TRUNCATE `user_email_verification_token`;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            TRUNCATE `mailbox_email_verification_token`;            
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            ALTER TABLE `user_auth_token` ADD FOREIGN KEY (`id`) REFERENCES `token` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            ALTER TABLE `user_password_reset_token` ADD FOREIGN KEY (`id`) REFERENCES `token` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            ALTER TABLE `user_email_verification_token` ADD FOREIGN KEY (`id`) REFERENCES `token` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            ALTER TABLE `mailbox_email_verification_token` ADD FOREIGN KEY (`id`) REFERENCES `token` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;            
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            DELETE FROM `domain` WHERE `id` = '6cbcd455-d47a-4e31-9602-cb8d1f85c24b';            
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }
    }
}