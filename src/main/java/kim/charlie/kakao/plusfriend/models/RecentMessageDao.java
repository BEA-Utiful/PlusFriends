package kim.charlie.kakao.plusfriend.models;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecentMessageDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public int getCount(String user_key) throws SQLException {
		return jdbcTemplate.query(con -> {
			return con.prepareStatement("select count(*) from search where user_key =\"" + user_key + "\"");
		}, rs -> {
			rs.next();
			return rs.getInt(1);
		});
	}
	
	public void add(String user_key) throws SQLException {
		jdbcTemplate.update("insert into user(user_key, status) values(?, ?) on duplicate key update user_key = ?, status = ?", user_key, 1, user_key, 1);
	}
	
	public void delete(String user_key) throws SQLException {
		jdbcTemplate.update("update user set status = 2 where user_key = \"" + user_key + "\"");
	}
	
	public void leave(String user_key) throws SQLException {
		jdbcTemplate.update("update user set status = 3 where user_key = \"" + user_key + "\"");
	}
	
	public void deleteAll(String user_key) throws SQLException {
		jdbcTemplate.update("delete from search where user_key = \"" + user_key + "\"");
	}
	
	public void addSearch(String user_key, String text, String link) throws SQLException {
		jdbcTemplate.update("insert into search(user_key, search_text, search_link) values(?, ?, ?)", user_key, text, link);
	}
	
	public String getSearchLink(String user_key, String text) throws SQLException {
		return jdbcTemplate.query(connection -> {
			return connection.prepareStatement("select * from search where user_key =\"" + user_key + "\" and search_text = \"" + text + "\"");
		}, resultSet -> {
			resultSet.next();
			return resultSet.getString("search_link");
		});
	}
}