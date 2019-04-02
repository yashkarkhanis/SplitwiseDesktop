package com.splitwise.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.splitwise.splitwisesdk.responses.ExpenseResponse;
import com.splitwise.splitwisesdk.responses.ExpenseResponse.ExpenseUser;

public class Expense {
	public long id;
	public long group_id;
	public String description;
	public Date updated_at;
	public Date created_date;
	public long created_by_id;
	public float cost;
	public List<ExpenseRatio> expenseRatio;
	public Set<String> users;
	private String jsonText;
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Expense(ExpenseResponse expense) {
    	this.id = expense.id;
    	this.group_id = expense.group_id;
    	this.description = expense.description;
    	this.updated_at = expense.updated_at;
    	this.created_by_id = expense.created_by_id;
    	this.cost = expense.cost;
    	this.created_date = expense.created_date;
    	expenseRatio = new ArrayList<ExpenseRatio>();
    	users = new HashSet<String>();
    	for(ExpenseResponse.ExpenseUser eu : expense.expenseUsers) {
    		expenseRatio.add(new ExpenseRatio(eu));
    		users.add(eu.id + "");
    	}
    	this.jsonText = expense.getJson();
    }
    
    public long getGroupId() {
    	return this.group_id;
    }
    
    public String description() {
    	return this.description;
    }
    
    public Date getUpdatedAt() {
    	return this.updated_at;
    }
    
    public long getCreatedById() {
    	return this.created_by_id;
    }
    
    public float getCost() {
    	return this.cost;
    }
    
    public List<ExpenseRatio> getRatio() {
    	return this.expenseRatio;
    }
    public long getId() {
		return this.id;
	}

	public float getOwedShare(long id) {
		for(ExpenseRatio er : expenseRatio) {
			if(id == er.getId()) {
				return er.getOwedShare();
			}
		}
		return 0;
	}

	public String getDescription() {
		return this.description;
	}

	public Date getCreatedDate() {
		return this.created_date;
	}
	
	public String toString() {
		return this.jsonText;
	}

	public float getNetBalance(long id) {
		for(ExpenseRatio er : expenseRatio) {
			if(id == er.getId()) {
				return er.getNetBalance();
			}
		}
		return 0;
	}
}
