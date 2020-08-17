# SQL Formatter

## What is it?

**_This is a formatter for SQL queries such as DDL and DML._**

`SQL Formatter` is specialized in formatting DDL and DML queries that do not use regular expressions and has no functions other than formatting. **_Therefore it is extremely lightweight besides can be expanded to meet user needs._**

The following syntaxes of ORACLE SQL are supported. I have not checked for compatibility with other RDBMS, sorry!

- DDL

  - `CREATE`
  - `ALTER`
  - `DROP`
  - `COMMENT`

- DML
  - `SELECT`
  - `INSERT`
  - `UPDATE`
  - `DELETE`

## How To Use

### 1. Add the dependencies

> Note:<br>
> Replace version you want to use. Check the latest [Packages](https://github.com/myConsciousness/sql-formatter/packages).<br>
> Please contact me for a token to download the package.

**_Maven_**

```xml
<dependency>
  <groupId>org.thinkit.formatter</groupId>
  <artifactId>sql-formatter</artifactId>
  <version>v1.0.0-1-g22b76d8</version>
</dependency>

<servers>
  <server>
    <id>github</id>
    <username>myConsciousness</username>
    <password>xxxxxxxxxxxxxxxxxx</password>
  </server>
</servers>
```

**_Gradle_**

```gradle
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/myConsciousness/sql-formatter")
        credentials {
          username = "myConsciousness"
          password = "xxxxxxxxxxxxxxxxxx"
        }
    }
}

dependencies {
    implementation 'org.thinkit.formatter:sql-formatter:v1.0.0-1-g22b76d8'
}
```

### 2. Add an import for [**_SQL Fomratter_**](https://github.com/myConsciousness/sql-formatter/blob/master/src/main/java/org/thinkit/formatter/SqlFormatter.java)

```java
import org.thinkit.formatter.SqlFormatter;
```

### 3. Create a instance

```java
Formatter formatter = SqlFormatter.of();
```

You can also create an instance with a specific number of indents as following.

> _Note:_<br>
> If a negative indentation is passed at instantiation time, the initial value defined in the [content file](https://github.com/myConsciousness/json-formatter/blob/master/src/main/resources/content/formatter/json/SqlDefaultIndentItem.json) takes precedence.

```java
Formatter formatter = SqlFormatter.withIndent(indent);
```

### 4. Format!

```java
Formatter formatter = SqlFormatter.withIndent(indent);
String formattedSql = formatter.format(sql);
```

## Demonstrate I/O

I have prepared the following unformatted select query for input/output reference. It's very simple query but it's hard to see without any spaces or line breaks.

```sql
      select t.foo, h.foo, t.foo from foo t, foo as hoge order by something desc left outer join foo h on t.foo = h.foo and t.foo = h.foo where name like '%foofoofoo' and test in ('foo', 'foo', 'foo', 'foo');
```

Let's run `SQL Formatter` with the SQL query of above template as an argument.

```sql
select
    t.foo,
    h.foo,
    t.foo
from
    foo t,
    foo as hoge
order by
    something desc
left outer join
    foo h
        on t.foo = h.foo
        and t.foo = h.foo
    where
        name like '%foofoofoo'
        and test in (
            'foo', 'foo', 'foo', 'foo'
        )
;
```

The output is as follows.

> _Note:_<br>
> Whitespace in the JSON string before formatting is trimmed during the formatting process, so no pre-processing is required.

## License

```
Copyright 2020 Kato Shinya.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied. See the License for the specific language governing permissions and limitations under
the License.
```

## More Information

`SQL Formatter` was designed and implemented by Kato Shinya, who works as a freelance developer from Japan.

Regardless of the means or content of communication, I would love to hear from you if you have any questions or concerns. I do not check my email box very often so a response may be delayed, anyway thank you for your interest!

- [Creator Profile](https://github.com/myConsciousness)
- [License](https://github.com/myConsciousness/sql-formatter/blob/master/LICENSE)
- [Release Note](https://github.com/myConsciousness/sql-formatter/releases)
- [Package](https://github.com/myConsciousness/sql-formatter/packages)
- [File a Bug](https://github.com/myConsciousness/sql-formatter/issues)
