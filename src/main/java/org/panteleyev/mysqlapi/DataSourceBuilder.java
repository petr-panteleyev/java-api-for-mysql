/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.mysqlapi;

import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.TimeZone;

/**
 * This class provides MySQL data source builder.
 */
public class DataSourceBuilder {
    private String host = "localhost";
    private int port = 3306;
    private String dbName;
    private String user;
    private String password;
    private String characterEncoding = "utf8";
    private String serverTimeZone = TimeZone.getDefault().getID();
    private boolean useSsl = false;
    private boolean allowPublicKeyRetrieval = true;

    /**
     * Builds datasource.
     *
     * @return datasource instance
     */
    public DataSource build() {
        try {
            var ds = new MysqlDataSource();
            ds.setDatabaseName(dbName);
            ds.setPort(port);
            ds.setServerName(host);
            ds.setUser(user);
            ds.setPassword(password);
            ds.setCharacterEncoding(characterEncoding);
            ds.setUseSSL(useSsl);
            ds.setAllowPublicKeyRetrieval(allowPublicKeyRetrieval);
            ds.setServerTimezone(serverTimeZone);
            return ds;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Sets database host. Default value is &quot;localhost&quot;.
     *
     * @param host database host
     * @return {@code this}
     */
    public DataSourceBuilder host(String host) {
        this.host = host;
        return this;
    }

    /**
     * Sets database port. Default value is 3306.
     *
     * @param port database port
     * @return {@code this}
     */
    public DataSourceBuilder port(int port) {
        this.port = port;
        return this;
    }

    /**
     * Sets database name
     *
     * @param name database name
     * @return {@code this}
     */
    public DataSourceBuilder name(String name) {
        this.dbName = name;
        return this;
    }

    /**
     * Sets database user name.
     *
     * @param user database user
     * @return {@code this}
     */
    public DataSourceBuilder user(String user) {
        this.user = user;
        return this;
    }

    /**
     * Sets database password.
     *
     * @param password database password
     * @return {@code this}
     */
    public DataSourceBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Sets character encoding. Default value is &quot;utf8&quot;.
     *
     * @param characterEncoding character encoding
     * @return {@code this}
     */
    public DataSourceBuilder characterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
        return this;
    }

    /**
     * Sets server timezone. Default value is {@code TimeZone.getDefault().getID()}.
     *
     * @param serverTimeZone server timezone
     * @return {@code this}
     */
    public DataSourceBuilder serverTimeZone(String serverTimeZone) {
        this.serverTimeZone = serverTimeZone;
        return this;
    }

    /**
     * Sets allowPublicKeyRetrieval option. Default value is {@code true}.
     *
     * @param allowPublicKeyRetrieval allowPublicKeyRetrieval option value
     * @return {@code this}
     */
    public DataSourceBuilder allowPublicKeyRetrieval(boolean allowPublicKeyRetrieval) {
        this.allowPublicKeyRetrieval = allowPublicKeyRetrieval;
        return this;
    }

    /**
     * Sets useSSL option. Default value is {@code false}.
     *
     * @param useSsl useSSL option value
     * @return {@code this}
     */
    public DataSourceBuilder useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }
}
