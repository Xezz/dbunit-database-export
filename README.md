## Overview
This is a sample application to pull data from a database and export it as an XML file.
This can then be used for integration tests with dbunit to have a consistent database state to test against.

## Current Status
Apache commons CLI

## Resources
This example bases on the information from http://dbunit.sourceforge.net/faq.html#extract
and from http://jdbc.postgresql.org/documentation/head/connect.html

## Adjustments
You have to adjust the pom.xml if you want to use a different database. Currently it has a dependency to postgresql but you can use all supoprted databases

## TODO
    * Use apache commons to make it interactive
    * Support a few more databases
