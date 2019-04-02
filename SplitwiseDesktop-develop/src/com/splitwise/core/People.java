package com.splitwise.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import com.splitwise.splitwisesdk.responses.Friend;
import com.splitwise.splitwisesdk.responses.User;

public class People {
    protected String first_name;
    protected String last_name;
    protected String name;
    protected long id;
    private float net_balance;
    private float total_negative_balance;// Only for current User
    private float total_positive_balance;// Only for current User
    private ArrayList<People> oweList = new ArrayList<>();
    private ArrayList<People> owedList = new ArrayList<>();
    private ArrayList<People> friends = new ArrayList<>();
    protected ArrayList<Group> groups = new ArrayList<>();
    private Date updated_at;// Only for friends
    final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public People(User user) {
		this.name = user.first_name + ((user.last_name!= null && user.last_name.length() > 0)?(" " + user.last_name):"");
		this.first_name = user.first_name;
		this.last_name = user.last_name;
		this.id = user.id;
	}
    
    public People(Friend friend) {
    	this.name = friend.first_name + ((friend.last_name!= null && friend.last_name.length() > 0)?(" " + friend.last_name):"");
    	this.first_name = friend.first_name;
		this.last_name = friend.last_name;
		this.id = friend.id;
		this.updated_at = friend.updated_at;
		this.net_balance = friend.balance_amount;
    }

	public ArrayList<Group> getGroups() {
        return groups;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    //friends balance getter/ setter
    public Float getNetAmount() {
        return (Float)(Math.round(this.net_balance * 100.0f)/100.0f);
    }

    //user balance getter/setter
    public Float getTotalNegativeBalance() {
        return (Float)(Math.round(Math.abs(this.total_negative_balance)*100.0f)/100.0f);
    }
    
    public Float getTotalPositiveBalance() {
        return (Float)(Math.round(Math.abs(this.total_positive_balance)*100.0f)/100.0f);
    }
    
    public void addFriend(People people) {
    	if(people.net_balance < 0) {
    		this.total_negative_balance += people.net_balance;
    		oweList.add(people);
    	} else if(people.net_balance > 0) {
    		this.total_positive_balance += people.net_balance;
    		owedList.add(people);
    	}
    	//LOGGER.info(people + "");
    	this.net_balance += people.net_balance;
    	friends.add(people);
    }
    
    public void clearFriends() {
    	oweList.clear();
    	owedList.clear();
    	this.total_negative_balance = 0;
    	this.total_positive_balance = 0;
    	this.net_balance = 0;
    	friends.clear();
    }
    
    public ArrayList<People> getFriends() {
        return friends;
    }
    
    public ArrayList<People> getOweList() {
        return oweList;
    }
    public ArrayList<People> getOwedList() {
        return owedList;
    }

    public People getFriend(long id) {
    	if(id == this.id) {
    		return this;
    	}
        for(People people : friends) {
        	if(people.id == id) {
        		return people;
        	}
        }
    	return null;
    }
    
    public Date getUpdatedAt() {
    	return this.updated_at;
    }

	public String getFirstName() {
		return this.first_name;
	}

	public Group getGroup(long groupId) {
        for(Group group : groups) {
        	if(group.getId() == groupId) {
        		return group;
        	}
        }
    	return null;
	}
}

