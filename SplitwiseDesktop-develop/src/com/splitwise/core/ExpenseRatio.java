package com.splitwise.core;

import com.splitwise.splitwisesdk.responses.ExpenseResponse;

public class ExpenseRatio {

	private long id; //People
	private float paid_share;
	private float owed_share;
	private float net_balance;
	
	public ExpenseRatio(ExpenseResponse.ExpenseUser eu) {
		this.id = eu.id;
		this.paid_share = eu.paid_share;
		this.owed_share = eu.owed_share;
		this.net_balance = eu.net_balance;
	}
	
	public long getId() {
		return this.id;
	}
	
	public float getPaidShare() {
		return this.paid_share;
	}
	
	public float getOwedShare() {
		return this.owed_share;
	}
	
	public float getNetBalance() {
		return this.net_balance;
	}
}
