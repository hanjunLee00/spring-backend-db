package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */

@Slf4j
public class MemberRepositoryV0 {
    public Member save (Member member) throws SQLException {
        String sql = "insert into member (member_id, money) values (?, ?)"; //SQL Injection 공격 방어 가능

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql); //sql 쿼리 날려주는 친구
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); //준비된 데이터들이 넣어져 실제 실행
            return member;
        } catch (SQLException e){
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }
    
    public void update (String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";
        
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql); //sql 쿼리 날려주는 친구
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate(); //영향받은 row 수 반환
            log.info("resultSize={}", resultSize);
        } catch (SQLException e){
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection conn, Statement stmt, ResultSet rs){

        if (rs != null) {
            try{
                rs.close(); //SQLException
            } catch (SQLException e){
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try{
                stmt.close(); //SQLException
            } catch (SQLException e){
                log.info("error", e);
            }
        }

        if (conn != null) {
            try{
                conn.close(); //Exception
            } catch (SQLException e){
                log.info("error", e);
            }
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql); //sql 쿼리 날려주는 친구
            pstmt.setString(1, memberId);
            int resultSize = pstmt.executeUpdate(); //영향받은 row 수 반환
            log.info("resultSize={}", resultSize);
        } catch (SQLException e){
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
