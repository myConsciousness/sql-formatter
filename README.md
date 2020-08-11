# SQL Formatter

This is a formatter for formatting SQL. A syntax correctness check is not yet implemented.

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

# How To Use

You can use it by importing the sql-formatter jar and writing the following code.

```java
String formattedSql = SqlFormatter.of().format(sql);
```

Strings are trimmed at formatting time so no preprocessing is required before `SqlFormatter.java` is started. Okay, let's execute `SqlFormatter.java` with a following SQL query as an argument for example.

```sql
      select t.foo, h.foo, t.foo from foo t, foo as hoge order by something desc left outer join foo h on t.foo = h.foo and t.foo = h.foo where name like '%foofoofoo' and test in ('foo', 'foo', 'foo', 'foo');
```

The result of the above output is as follows.

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
        );
```

If you want to specify an arbitrary number of indents, create an instance with `withIndent(int)` constructor as shown below.

```java
String formattedSql = SqlFormatter.withIndent(indent).format(sql);
```

# License

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