import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.Table;
import org.panteleyev.mysqlapi.annotations.RecordBuilder;
import org.panteleyev.mysqlapi.Record;
import org.panteleyev.mysqlapi.MySqlClient;

/**
 Persistence library provides annotation classes for database access.

 <p style="font-size: large;"><strong>Database</strong></p>

 <p>
 Database manipulation is beyond the scope of this API. Calling code must supply correct {@link javax.sql.DataSource}
 and ensure database does exist and proper access control is established.
 </p>

 <p style="font-size: large;"><strong>Table</strong></p>

 <p>
 Class implementing database table is defined by the annotation {@link Table}.
 Such class must also implement interface {@link Record}.
 </p>

 <p>API currently supports the following primary key types:</p>
 <ul>
 <li>{@link java.lang.Integer}, int
 <li>{@link java.lang.Long}, long
 <li>{@link java.lang.String}
 <li>{@link java.util.UUID}
 </ul>

 <p>In case of {@link java.lang.Integer} one may use {@link MySqlClient#generatePrimaryKey} to generate unique values for the
 appropriate table classes. Also make sure that application calls {@link MySqlClient#preload} first.
 </p>

 <p style="font-size: large;"><strong>Deserialization</strong></p>

 <p>
 API supports two ways of object deserialization: by constructor and direct field assignment. Constructor must be
 used for objects with final fields or in case of additional initialization.
 </p>

 <p><strong>Field Assignment</strong></p>

 <p>
 There must be no-argument constructor, either default or explicit. Setters are not used, i.e. there is no way
 to define additional deserialization logic in case of field assignment.
 </p>

  <pre><code>
{@literal @}Table("book")
class Book implements Record {
    {@literal @}PrimaryKey
    {@literal @}Column(Column.ID)
    private int id;
    {@literal @}Column("title")
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
 </code></pre>

 <p><strong>Constructor</strong></p>

 <p>
 Constructor deserialization is triggered by {@link RecordBuilder}
 as shown below. Such constructor must have parameters corresponding to table columns.
 </p>

 <pre><code>
{@literal @}Table("book")
class Book implements Record {
    {@literal @}PrimaryKey
    {@literal @}Column(Field.ID)
    private final int id;
    {@literal @}Column("title")
    private final String title;

    {@literal @}RecordBuilder
    public Book ({@literal @}Column(Column.ID) int id, {@literal @}Column("title") String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
 </code></pre>

 <p style="font-size: large;"><strong>Data Types</strong></p>

 <p>The following data types are supported:</p>

 <table border="1">
 <caption>Data Types</caption>
 <tr><th>Java</th><th>MySQL</th><th>Comment</th></tr>
 <tr><td>int<br>{@link java.lang.Integer}</td><td>INTEGER</td><td></td></tr>
 <tr><td>long<br>{@link java.lang.Long}</td><td>BIGINT</td><td></td></tr>
 <tr><td>boolean<br>{@link java.lang.Boolean}</td><td>BOOLEAN</td><td></td></tr>
 <tr>
 <td>{@link java.lang.String}</td>
 <td>VARCHAR ( {@link Column#length} )</td>
 <td></td>
 </tr>
 <tr>
 <td>{@link java.lang.String} with {@link Column#isJson()} = true</td>
 <td>JSON</td>
 <td></td>
 </tr>
 <tr>
 <td>{@link java.math.BigDecimal}</td>
 <td>DECIMAL ( {@link Column#precision}, {@link Column#scale} )</td>
 <td>
 MySQL representation does not guarantee that retrieved value will be equal to original one by means of
 {@link java.lang.Object#equals}. Use {@link java.math.BigDecimal#compareTo} instead.
 </td>
 </tr>
 <tr>
 <td>{@link java.util.Date}</td>
 <td>BIGINT</td>
 <td>Dates are stored as long using {@link java.util.Date#getTime}</td>
 </tr>
 <tr>
 <td>{@link java.time.LocalDate}</td>
 <td>BIGINT</td>
 <td>Local dates are stored as long using {@link java.time.LocalDate#toEpochDay}</td>
 </tr>
 <tr>
 <td>byte[]</td>
 <td>VARBINARY ( {@link Column#length} ) </td>
 <td></td>
 </tr>
 <tr>
 <td>{@link java.util.UUID}</td>
 <td>BINARY(16) or VARCHAR(36) depending on {@link Column#storeUuidAsBinary()}</td>
 <td>The following conversion functions are used between string and binary representation:
 <code>BIN_TO_UUID()</code> and <code>UUID_TO_BIN()</code>
 </td>
 </tr>
 </table>

 <p style="font-size: large;"><strong>Indexes and Foreign Keys</strong></p>

 <pre><code>
{@literal @}Table("parent_table")
public class ParentTable implements Record {
    {@literal @}Column("data")
    {@literal @}Index(value = "data", unique = true)
    private String data;

    public String getData() {
        return data;
    }
}
 </code></pre>

 <p>This will produce the following SQL for indexed column:<br><code>CREATE UNIQUE INDEX data ON parent_table(data)</code></p>

 <pre><code>
{@literal @}Table("child_table")
public class ChildTable implements Record {
    {@literal @}Column("parent_data")
    {@literal @}ForeignKey(table = ParentTable.class, column = "data",
        onDelete = ReferenceOption.RESTRICT, onUpdate = ReferenceOption.CASCADE)
    private final String parentData;

    public String getParentData() {
        return parentData;
    }
}
 </code></pre>

 <p>This will produce the following SQL for the foreign key:<br>
 <code>CREATE FOREIGN KEY(parent_data) REFERENCES parent_table(data) ON DELETE RESTRICT ON UPDATE CASCADE</code>
 </p>


 @see <a href="https://dev.mysql.com/doc/refman/5.7/en/data-types.html">MySQL Data Types</a>

 */
open module org.panteleyev.mysqlapi {
    requires java.base;
    requires java.sql;
    requires java.naming;
    requires mysql.connector.java;

    exports org.panteleyev.mysqlapi;
    exports org.panteleyev.mysqlapi.annotations;
}