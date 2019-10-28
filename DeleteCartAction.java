package com.internousdev.arizona.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.arizona.dao.CartInfoDAO;
import com.internousdev.arizona.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction extends ActionSupport implements SessionAware{

	private Map<String,Object> session;
	private int totalPrice;
	private String[] checkList;
	private ArrayList<CartInfoDTO> cartItemList=new ArrayList<CartInfoDTO>();

	public String execute(){

		if(!session.containsKey("tempUserId") && !session.containsKey("userId")) {	//セッションの保有がなくなったらセッションタイムアウト
			return "sessionTimeout";
		}

		String ret=ERROR;

		CartInfoDAO dao=new CartInfoDAO();

		int count=0;
		String userId=null;
		int logined=Integer.parseInt(String.valueOf(session.get("logined")));

		if(logined==1){	//ログインしてる場合
			userId=session.get("userId").toString();
		}else{	//ログインしてない場合
			userId=String.valueOf(session.get("tempUserId"));
		}

		for(String productId:checkList){	//拡張for文でチェックボックスをリスト化してまわす、productIdを対応させて削除
			try {
				count+=dao.delete(productId,userId);
			} catch (SQLException e) {
				e.printStackTrace();
				return ERROR;
			}
		}

		if(count==checkList.length){	//削除されたら
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

			ret=SUCCESS;
		}

		return ret;
	}

	public Map<String,Object> getSession(){
		return session;
	}

	public void setSession(Map<String,Object> session){
		this.session=session;
	}

	public int getTotalPrice(){
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice){
		this.totalPrice=totalPrice;
	}

	public ArrayList<CartInfoDTO> getCartItemList(){
		return cartItemList;
	}

	public void setCartItemList(ArrayList<CartInfoDTO> cartItemList){
		this.cartItemList=cartItemList;
	}

	public String[] getCheckList(){
		return checkList;
	}

	public void setCheckList(String[] checkList){
		this.checkList=checkList;
	}

}
