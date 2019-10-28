package com.internousdev.arizona.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.internousdev.arizona.dto.CartInfoDTO;
import com.internousdev.arizona.util.DBConnector;

public class CartInfoDAO {

	public boolean isExistsCartInfo(String userId,int productId) throws SQLException{	//カートテーブルに情報があるかどうかの判別

		boolean ret=false;

		DBConnector db=new DBConnector();
		Connection con=db.getConnection();

		String sql="SELECT COUNT(id) AS COUNT FROM cart_info WHERE user_id=? AND product_id=?";

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);

			ResultSet rs=ps.executeQuery();

			if(rs.next()){
				if(rs.getInt("COUNT")>0){
					ret=true;
				}
			}

		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			con.close();
		}
		return ret;
	}

	public ArrayList<CartInfoDTO> getCartInfoDTOlist(String userId) throws SQLException{	//カートに入っている商品をリストにする
		DBConnector db=new DBConnector();
		Connection con=db.getConnection();
		ArrayList<CartInfoDTO> cartList=new ArrayList<CartInfoDTO>();

		String sql="SELECT p.product_id,p.product_name,p.product_name_kana,p.image_file_path,p.image_file_name,p.price,p.release_company,DATE_FORMAT(p.release_date,'%y/%m/%d') AS release_date,p.product_description,c.product_count,c.product_id,(p.price*c.product_count) AS subtotal"
				+ " FROM product_info AS p INNER JOIN cart_info AS c ON p.product_id=c.product_id WHERE c.user_id=? ORDER BY c.update_date DESC,c.regist_date DESC";

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs=ps.executeQuery();

			while(rs.next()){

				CartInfoDTO dto=new CartInfoDTO();	//DTOにセットした値をリストに格納

				dto.setProductId(rs.getInt("product_id"));
				dto.setProductName(rs.getString("product_name"));
				dto.setProductNameKana(rs.getString("product_name_kana"));
				dto.setImageFilePath(rs.getString("image_file_path"));
				dto.setImageFileName(rs.getString("image_file_name"));
				dto.setReleaseCompany(rs.getString("release_company"));
				dto.setReleaseDate(rs.getString("release_date"));
				dto.setPrice(rs.getInt("price"));
				dto.setProductCount(rs.getInt("product_count"));
				dto.setStatus(rs.getString("product_description"));
				dto.setSubtotal(rs.getInt("subtotal"));

				cartList.add(dto);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			con.close();
		}

		return cartList;
	}

	public int getTotalPrice(String userId) throws SQLException{	//商品ごとの小計を算出する
		DBConnector db=new DBConnector();
		Connection con=db.getConnection();

		String sql="SELECT SUM(product_count * price) AS total_price FROM cart_info ci"
				+ " JOIN product_info pi ON ci.product_id=pi.product_id WHERE user_id=? GROUP BY user_id";

		int totalPrice=0;

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs=ps.executeQuery();

			if(rs.next()){
				totalPrice=rs.getInt("total_price");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			con.close();
		}

		return totalPrice;
	}

	public int addCartItem(String userId,int productId,int productCount) throws SQLException{	//カートに追加する
		DBConnector db=new DBConnector();
		Connection con=db.getConnection();

		String sql="INSERT INTO cart_info(user_id,product_id,product_count,regist_date,update_date) VALUES(?,?,?,now(),now())";

		int ret=0;

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ps.setInt(3, productCount);

			ret=ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			con.close();
		}

		return ret;
	}

	public int updateProductCount(String userId,int productId,int productCount) throws SQLException{	//仮ユーザーIDで入れた商品とユーザーIDで入れた商品の個数を合わせる

		DBConnector db=new DBConnector();
		Connection con=db.getConnection();

		String sql="UPDATE cart_info SET product_count=(product_count + ?),update_date=now() WHERE user_id=? AND product_id=?";

		int ret=0;

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);

			ret=ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			con.close();
		}
		return ret;
	}

	public int delete(String productId,String userId) throws SQLException{	//カートデータを削除
		DBConnector db=new DBConnector();
		Connection con=db.getConnection();

		String sql="DELETE FROM cart_info WHERE product_id=? AND user_id=?";

		int ret=0;

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, productId);
			ps.setString(2, userId);

			ret=ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			con.close();
		}

		return ret;
	}

	public int deleteAll(String userId) throws SQLException{
		DBConnector db=new DBConnector();
		Connection con=db.getConnection();

		String sql="DELETE FROM cart_info WHERE user_id=?";

		int ret=0;

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);

			ret=ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			con.close();
		}

		return ret;

	}

	public int linkToUserId(String tempUserId,String userId,int productId) throws SQLException{	//仮ユーザーIDをユーザーIDで上書き
		DBConnector db=new DBConnector();
		Connection con=db.getConnection();

		String sql="UPDATE cart_info SET user_id=?,update_date=now() WHERE user_id=? AND product_id=?";

		int ret=0;

		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, tempUserId);
			ps.setInt(3, productId);

			ret=ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.close();
		}

		return ret;
	}

}
