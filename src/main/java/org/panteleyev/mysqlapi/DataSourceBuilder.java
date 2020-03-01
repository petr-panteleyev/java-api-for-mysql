package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;
import java.util.TimeZone;

/**
 * This class provides MySQL data source builder.
 */
public class DataSourceBuilder {
    private String host;
    private int port = 3306;
    private String dbName;
    private String user;
    private String password;

    public DataSource build() throws Exception {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName(dbName);
        ds.setPort(port);
        ds.setServerName(host);
        ds.setUser(user);
        ds.setPassword(password);
        ds.setCharacterEncoding("utf8");
        ds.setUseSSL(false);
        ds.setServerTimezone(TimeZone.getDefault().getID());
        return ds;
    }

    public DataSourceBuilder host(String host) {
        this.host = host;
        return this;
    }

    public DataSourceBuilder port(int port) {
        this.port = port;
        return this;
    }

    public DataSourceBuilder name(String name) {
        this.dbName = name;
        return this;
    }

    public DataSourceBuilder user(String user) {
        this.user = user;
        return this;
    }

    public DataSourceBuilder password(String password) {
        this.password = password;
        return this;
    }
}
