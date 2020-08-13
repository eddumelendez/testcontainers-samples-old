package samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class DbMigrationTest {

	@Container
	private MySQLContainer mysql = new MySQLContainer();

	private void dbMigration(Consumer<Liquibase> liquibasefn, int expectedRows) throws SQLException, LiquibaseException {
		Connection connection = DriverManager.getConnection(this.mysql.getJdbcUrl(), this.mysql.getUsername(), this.mysql.getPassword());
		DatabaseConnection dbConnection = new JdbcConnection(connection);
		Liquibase liquibase = new Liquibase("db.changelog.yml", new ClassLoaderResourceAccessor(), dbConnection);
		liquibasefn.accept(liquibase);

		Statement statement = connection.createStatement();
		statement.execute("select count(*) from DATABASECHANGELOG");
		ResultSet result = statement.getResultSet();
		result.next();
		assertThat(result.getInt(1)).isEqualTo(expectedRows);
	}

	private Consumer<Liquibase> liquibaseUpdate() {
		return liquibase -> {
			try {
				liquibase.update(new Contexts());
			} catch (LiquibaseException e) {
				e.printStackTrace();
			}
		};
	}

	@Test
	void migration() throws SQLException, LiquibaseException {
		dbMigration(liquibaseUpdate(), 4);
	}

	private Consumer<Liquibase> liquibaseRollback() {
		return liquibase -> {
			try {
				liquibase.update(new Contexts());
				liquibase.rollback("1.0.0", new Contexts());
			} catch (LiquibaseException e) {
				e.printStackTrace();
			}
		};
	}

	@Test
	void rollback() throws SQLException, LiquibaseException {
		dbMigration(liquibaseRollback(), 1);
	}
}
