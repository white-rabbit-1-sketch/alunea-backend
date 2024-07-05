package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1__Init extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        String sql = """
            CREATE TABLE `user` (
                `id` varchar(36) NOT NULL,
                `email` varchar(180) NOT NULL,
                `is_email_verified` tinyint(1) NOT NULL,
                `password` varchar(255) NOT NULL,
                `role` varchar(255) NOT NULL,
                `favorite_domain_id` varchar(36) DEFAULT NULL,
                `favorite_mailbox_id` varchar(36) DEFAULT NULL,
                `language` ENUM('en', 'ru') NOT NULL,
                `email_mode` ENUM('forward', 'hard-replace', 'soft-replace') NOT NULL,
                `create_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                `update_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
        
                PRIMARY KEY (`id`),
                UNIQUE INDEX (email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;     
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `mailbox` (
                `id` varchar(36) NOT NULL,
                `user_id` varchar(36) NOT NULL,
                `email` varchar(180) NOT NULL,
                `is_email_verified` tinyint(1) NOT NULL,
                `is_enabled` tinyint(1) NOT NULL,
                `create_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,

                PRIMARY KEY (`id`),
                KEY (user_id, email),
                FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `information_subscription` (
                `id` varchar(36) NOT NULL,
                `email` varchar(180) NOT NULL,
                `language` ENUM('en', 'ru') NOT NULL,
                `create_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,

                PRIMARY KEY (`id`),
                UNIQUE INDEX (email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `used_domain` (
                `id` varchar(36) NOT NULL,
                `domain` varchar(255) NOT NULL,
                `create_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,

                PRIMARY KEY (`id`),
                UNIQUE INDEX (`domain`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `domain` (
                `id` varchar(36) NOT NULL,
                `type` SET('custom', 'system', 'subdomain') NOT NULL,
                `domain` varchar(255) NOT NULL,
                `is_enabled` tinyint(1) NOT NULL,
                `create_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,

                PRIMARY KEY (`id`),
                UNIQUE INDEX (`type`, `domain`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `custom_domain` (
                `id` varchar(36) NOT NULL,
                `user_id` varchar(36) NOT NULL,
                `catch_all_mailbox_id` varchar(36) DEFAULT NULL,

                PRIMARY KEY (`id`),
                FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `system_domain` (
                `id` varchar(36) NOT NULL,

                PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `subdomain` (
                `id` varchar(36) NOT NULL,
                `subdomain` varchar(255) NOT NULL,
                `user_id` varchar(36) NOT NULL,
                `system_domain_id` varchar(36) NOT NULL,
                `catch_all_mailbox_id` varchar(36) DEFAULT NULL,

                PRIMARY KEY (`id`),
                FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY (`system_domain_id`) REFERENCES `system_domain` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `contact` (
                `id` varchar(36) NOT NULL,
                `user_id` varchar(36) NOT NULL,
                `email` varchar(255) NOT NULL,
                `is_enabled` tinyint(1) NOT NULL,
                `create_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,

                PRIMARY KEY (`id`),
                UNIQUE KEY (user_id, email),
                FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `token` (
                `id` varchar(36) NOT NULL,
                `user_id` varchar(36) NOT NULL,
                `type` varchar(255),
                `expire_time` timestamp NOT NULL,
                `create_time` timestamp NOT NULL,

                PRIMARY KEY (`id`),
                KEY (`type`),
                FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `user_auth_token` (
                `id` varchar(36) NOT NULL,

                PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `user_password_reset_token` (
                `id` varchar(36) NOT NULL,

                PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `user_email_verification_token` (
                `id` varchar(36) NOT NULL,
                `email` varchar(255) NOT NULL,

                PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `mailbox_email_verification_token` (
                `id` varchar(36) NOT NULL,
                `mailbox_id` varchar(36) NOT NULL,

                PRIMARY KEY (`id`),
                FOREIGN KEY (`mailbox_id`) REFERENCES `mailbox` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            insert into domain (id, `type`, domain, is_enabled)
            values ('58c2107c-6a32-4896-988b-3140f00ced0d', 'system', 'alunea.io', 1);
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            insert into system_domain (id)
            values ('58c2107c-6a32-4896-988b-3140f00ced0d');
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `alias` (
                `id` varchar(36) NOT NULL,
                `type` SET('mailbox', 'contact') NOT NULL,
                `user_id` varchar(36) NOT NULL,
                `domain_id` varchar(36) NOT NULL,
                `recipient` varchar(255) NOT NULL,
                `value` varchar(255) NOT NULL,
                `is_enabled` tinyint(1) NOT NULL,
                `create_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,

                PRIMARY KEY (`id`),
                UNIQUE KEY (domain_id, recipient),
                UNIQUE KEY (`value`),
                FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY (`domain_id`) REFERENCES `domain` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            CREATE TABLE `mailbox_alias` (
                `id` varchar(36) NOT NULL,
                `mailbox_id` varchar(36) NOT NULL,

                PRIMARY KEY (`id`),
                FOREIGN KEY (`mailbox_id`) REFERENCES `mailbox` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }


        sql = """
            CREATE TABLE `contact_alias` (
                `id` varchar(36) NOT NULL,
                `contact_id` varchar(36) NOT NULL,
                `mailbox_alias_id` varchar(36) NOT NULL,

                PRIMARY KEY (`id`),
                FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY (`mailbox_alias_id`) REFERENCES `mailbox_alias` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            ALTER TABLE `user` ADD FOREIGN KEY (`favorite_domain_id`) REFERENCES `domain` (`id`) ON DELETE SET NULL ON UPDATE CASCADE; 
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }

        sql = """
            ALTER TABLE `user` ADD FOREIGN KEY (`favorite_mailbox_id`) REFERENCES `mailbox` (`id`) ON DELETE SET NULL ON UPDATE CASCADE; 
        """;
        try(var statement = context.getConnection().createStatement()) {
            statement.execute(sql);
        }
    }
}