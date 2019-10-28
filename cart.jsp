<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="./css/cart.css">
<link rel="stylesheet" type="text/css" href="./css/home.css">
<title>カート画面</title>
<script src="./js/arizona.js"></script>
</head>
<body>
	<jsp:include page ="header.jsp"/>
	<div id="contents">

			<h1>カート画面</h1>

<!-- 		リストにカート情報が入っている場合 -->
		<s:if test="cartItemList !=null && cartItemList.size()>0">
		<s:form id="cartForm">
		<table>
			<tr>
				<th>#</th>
				<th>商品名</th>
				<th>商品名ふりがな</th>
				<th>商品画像</th>
				<th>値段</th>
				<th>発売会社名</th>
				<th>発売年月日</th>
				<th>購入個数</th>
				<th>合計金額</th>
			<tr>
			<s:iterator value="cartItemList">
			<tr>
<!-- 			リストの中身を出す -->

				<td><input type="checkbox" value='<s:property value="productId"/>' name="checkList" class="checkList" onchange="checkValue()"/></td>
				<td><s:property value="productName"/></td>
				<td><s:property value="productNameKana"/></td>
				<td><img src='<s:property value="imageFilePath"/>/<s:property value="imageFileName"/>' width="50px" height="50px"/></td>
				<td><s:property value="price"/>円</td>
				<td><s:property value="releaseCompany"/></td>
				<td><s:property value="releaseDate"/></td>
				<td><s:property value="productCount"/></td>
				<td><s:property value="subtotal"/>円</td>

			</tr>
			</s:iterator>
			</table>
<!-- 			Actionで設定したカーとの合計金額を反映 -->
				<h2>カート合計金額:<s:property value="totalPrice"/>円</h2>

		<div class="btn">
		<s:if test="#session.logined==1">
			<s:submit value="決済" onclick="goSettlementConfirmAction()" class="submit_btn"/>
		</s:if>
		<s:else>
			<s:submit value="決済" onclick="goGoLoginActionForCartForm()" class="submit_btn"/>
			<s:hidden name="cartFlag" value="1"/>
		</s:else>
		</div>

<!-- 		チェックがついた場合のみ表示 -->
		<div class="btn">
		<s:submit value="削除" id="deleteButton" disabled="true" onclick="goDeleteCartAction()" class="d-button"/>
		</div>
		</s:form>
		</s:if>

<!-- 		リストに何も入っていない場合はメッセージを表示する -->
		<s:else>
			<div id="message">カート情報がありません。</div>
		</s:else>

	</div>

</body>
</html>