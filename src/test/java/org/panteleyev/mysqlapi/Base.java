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
import org.panteleyev.mysqlapi.model.ImmutableBinaryRecord;
import org.panteleyev.mysqlapi.model.ImmutableRecord;
import org.panteleyev.mysqlapi.model.ImmutableRecordWithPrimitives;
import org.panteleyev.mysqlapi.model.RecordWithAllTypes;
import org.panteleyev.mysqlapi.model.RecordWithOptionals;
import org.panteleyev.mysqlapi.model.RecordWithPrimitives;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Random;

public class Base {
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    private MySqlClient dao;

    private DataSource dataSource;

    private static String TEST_DB_NAME = "TestDB";

    void setDao(MySqlClient dao) {
        this.dao = dao;
    }

    public MySqlClient getDao() {
        return dao;
    }

    @BeforeClass
    public void setupMySQL() throws Exception {
        var dbName = System.getProperty("mysql.database", TEST_DB_NAME);
        var host = System.getProperty("mysql.host", "localhost");
        var port = Integer.parseInt(System.getProperty("mysql.port", "3306"));
        var user = System.getProperty("mysql.user");
        var password = System.getProperty("mysql.password");

        if (user == null || password == null) {
            throw new SkipException("Test config is not set");
        }

        dataSource = new DataSourceBuilder()
            .host(host)
            .port(port)
            .user(user)
            .password(password)
            .build();

        try (var conn = dataSource.getConnection()) {
            var st = conn.createStatement();
            st.execute("CREATE DATABASE " + dbName);
            ((MysqlDataSource) dataSource).setDatabaseName(dbName);
            var dao = new MySqlClient(dataSource);
            setDao(dao);
        } catch (SQLException ex) {
            throw new SkipException("Unable to create database", ex);
        }
    }

    @AfterClass
    public void cleanupMySQL() throws Exception {
        try (var conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.execute("DROP DATABASE " + TEST_DB_NAME);
        }
    }

    @DataProvider(name = "recordClasses")
    public Object[][] recordClassesProvider() {
        return new Object[][]{
            {RecordWithAllTypes.class},
            {RecordWithOptionals.class},
            {ImmutableRecord.class},
            {RecordWithPrimitives.class},
            {ImmutableRecordWithPrimitives.class},
            {ImmutableBinaryRecord.class}
        };
    }

    protected <T extends Record<Integer>> T givenRandomRecord(Class<T> clazz) throws Exception {
        Integer id = dao.generatePrimaryKey(clazz);
        return givenRandomRecordWithId(clazz, id);
    }

    protected <T extends Record> T givenRandomRecordWithId(Class<T> clazz, Integer id) throws Exception {
        var method = clazz.getDeclaredMethod("newRecord", Integer.class, Random.class);
        return (T) method.invoke(null, id, RANDOM);
    }

    protected <T extends Record<Integer>> T givenNullRecord(Class<T> clazz) throws Exception {
        var id = dao.generatePrimaryKey(clazz);
        var method = clazz.getDeclaredMethod("newNullRecord", Integer.class);
        return (T) method.invoke(null, id);
    }

    public static boolean compareBigDecimals(BigDecimal x, BigDecimal y) {
        return Objects.equals(x, y)
            || (x != null && x.compareTo(y) == 0);
    }
}

