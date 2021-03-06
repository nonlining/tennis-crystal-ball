package org.strangeforest.tcb.dataload

import groovy.sql.*
import org.springframework.jdbc.datasource.*

import javax.sql.*
import java.sql.*
import java.util.concurrent.*

class SqlPool extends LinkedBlockingDeque<Sql> {

	static final String DB_URL_PROPERTY = 'tcb.db.url'
	static final String USERNAME_PROPERTY = 'tcb.db.username'
	static final String PASSWORD_PROPERTY = 'tcb.db.password'
	static final String DB_CONNECTIONS_PROPERTY = 'tcb.db.connections'

	static final String DB_URL_DEFAULT = 'jdbc:postgresql://localhost:5432/postgres?prepareThreshold=0'
	static final String USERNAME_DEFAULT = 'tcb'
	static final String PASSWORD_DEFAULT = 'tcb'
	static final int DB_CONNECTIONS_DEFAULT = 2

	SqlPool(size = null) {
		print 'Allocating DB connections'
		def dbURL = System.getProperty(DB_URL_PROPERTY, DB_URL_DEFAULT)
		def username = System.getProperty(USERNAME_PROPERTY, USERNAME_DEFAULT)
		def password = System.getProperty(PASSWORD_PROPERTY, PASSWORD_DEFAULT)
		def connections = size ?: Integer.parseInt(System.getProperty(DB_CONNECTIONS_PROPERTY, String.valueOf(DB_CONNECTIONS_DEFAULT)))

		for (int i = 0; i < connections; i++) {
			Sql sql = Sql.newInstance(dbURL, username, password, 'org.postgresql.Driver')
			sql.connection.autoCommit = false
			sql.cacheStatements = true
			addFirst(sql)
			print '.'
		}
		println()
	}

	def withSql(Closure c) {
		Sql sql = take()
		try {
			withTx(sql, c)
		}
		finally {
			put(sql)
		}
	}

	static withTx(Sql sql, Closure c) {
		try {
			def r = c(sql)
			sql.commit()
			r
		}
		catch (BatchUpdateException buEx) {
			sql.rollback()
			for (def nextEx = buEx.getNextException(); nextEx ; nextEx = nextEx.getNextException())
				System.err.println(nextEx)
			throw buEx
		}
		catch (Throwable th) {
			sql.rollback()
			throw th
		}
	}

	static DataSource dataSource() {
		def dbURL = System.getProperty(DB_URL_PROPERTY, DB_URL_DEFAULT)
		def username = System.getProperty(USERNAME_PROPERTY, USERNAME_DEFAULT)
		def password = System.getProperty(PASSWORD_PROPERTY, PASSWORD_DEFAULT)
		new DriverManagerDataSource(dbURL, username, password)
	}
}
