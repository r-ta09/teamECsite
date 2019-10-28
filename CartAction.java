package com.internousdev.arizona.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.arizona.dao.CartInfoDAO;
import com.internousdev.arizona.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction extends ActionSupport implements SessionAware{

	private Map<String,Object> session;
	private ArrayList<CartInfoDTO> cartItemList=new ArrayList<CartInfoDTO>();
	private int totalPrice;

	public String execute(){

		if(!session.containsKey("tempUserId") && !session.containsKey("userId")) {	//セッション保有がなくなったらセッションタイムアウト
			return "sessionTimeout";
		}

		CartInfoDAO dao=new CartInfoDAO();

		String userId=null;

		int logined = Integer.parseInt(String.valueOf(session.get("logined")));
		if(logined == 1) {	//	ログインしている場合
			userId = session.get("userId").toString();
		} else {	//ログインしてない場合
			userId = String.valueOf(session.get("tempUserId"));
		}

		try {
			cartItemList=dao.getCartInfoDTOlist(userId);
		} catch (SQLException e) {
			e.printStackTrace();
			return ERROR;
		}
		try {
			totalPrice=dao.getTotalPrice(userId);
		} catch (SQLException e) {
			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;
	}

	public Map<String,Object> getSession(){
		return session;
	}

	public void setSession(Map<String,Object> session){
		this.session=session;
	}

	public ArrayList<CartInfoDTO> getCartItemList(){
		return cartItemList;
	}

	public void setCartItemList(ArrayList<CartInfoDTO> cartItemList){
		this.cartItemList=cartItemList;
	}

	public int getTotalPrice(){
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice){
		this.totalPrice=totalPrice;
	}

}
