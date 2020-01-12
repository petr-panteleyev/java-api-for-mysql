# Java API for MySQL

Annotation based Java API for MySQL.

The library is intended for desktop applications that perform insert/update/delete operations only. 

[![BSD-2 license](https://img.shields.io/badge/License-BSD--2-informational.svg)](LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.panteleyev/java-api-for-mysql/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.panteleyev/java-api-for-mysql/)
[![Javadocs](http://www.javadoc.io/badge/org.panteleyev/java-api-for-mysql.svg)](http://www.javadoc.io/doc/org.panteleyev/java-api-for-mysql)

### Artifact

```
<dependency>
    <groupId>org.panteleyev</groupId>
    <artifactId>java-api-for-mysql</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Limitations

* Each new version may introduce incompatible changes
* No queries, either mapped or raw
* Basic support for constraints
* No support for schema migration in case of changes

## Data Types

Java | MySQL
-----|-------
```String```|VARCHAR
```String``` with ```Column.isJson() = true```|JSON
```BigDecimal```|DECIMAL ( ```Column.precision(), Column.scale()``` )
```Date```|BIGINT
```LocalDate```|BIGINT
```byte[]```|VARBINARY ( ```Column.length()``` )
```UUID```|BINARY(16) or VARCHAR(36)

```java.util.Date``` are stored as long using ```Date.getTime()```
```java.time.LocalDate``` is stored as long using ```LocalDate.toEpochDay()```

The following types can be used as primary keys:
* Integer, int
* Long, long
* String
* UUID

Database independent auto-increment is supported for integer and long keys.

## Usage Examples

### Immutable Object

```java
@Table("book")
public class Book implements Record {
    @PrimaryKey
    @Column(Field.ID)
    private final int id;
    
    @Column("title")
    private final String title;

    @RecordBuilder
    public Book(@Column(Column.ID) int id, 
                @Column("title") String title) 
    {
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
```

### Foreign Keys

```java
@Table("parent_table")
public class ParentTable implements Record {
    @Column(value = "data", unique = true)
    private String data;

    public String getData() {
        return data;
    }
}

@Table("child_table")
public class ChildTable implements Record {
    @Column("parent_data")
    @ForeignKey(table = ParentTable.class, column = "data",
        onDelete = ReferenceOption.RESTRICT, onUpdate = ReferenceOption.CASCADE)
    private final String parentData;

    public String getParentData() {
        return parentData;
    }
}
```
