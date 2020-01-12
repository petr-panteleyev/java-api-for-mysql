/*
 * Copyright (c) 2020, Petr Panteleyev <petr@panteleyev.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.panteleyev.mysqlapi;

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
