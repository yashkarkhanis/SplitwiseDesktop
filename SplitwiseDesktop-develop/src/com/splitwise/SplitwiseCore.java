package com.splitwise;

import com.splitwise.core.Activity;
import com.splitwise.core.Expense;
import com.splitwise.core.ExpenseRatio;
import com.splitwise.core.Group;
import com.splitwise.core.LedgerManager;
import com.splitwise.core.People;
import com.splitwise.gui.MainFrame;
import com.splitwise.splitwisesdk.APIException;
import com.splitwise.splitwisesdk.SplitwiseSDK;
import com.splitwise.splitwisesdk.responses.ActivityResponse;
import com.splitwise.splitwisesdk.responses.ExpenseResponse;
import com.splitwise.splitwisesdk.responses.Friend;
import com.splitwise.splitwisesdk.responses.GroupResponse;
import com.splitwise.splitwisesdk.responses.Response;
import com.splitwise.splitwisesdk.responses.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SplitwiseCore {
	private static SplitwiseCore instance;
	
	private People currentUser;
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Expense> expenses = new ArrayList<>();
    private String lastFetchedActivity;
    private DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    private Callback callback;
    final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private long groupId;
    private long userId;
    
    private int checkAfterEvery = 3000;
    
    private ReentrantLock lock = new ReentrantLock();
    
    
    public void login(){}

    public void initialize(){
    	fetchUser();
    	fetchFriends();
    	fetchGroups();
    	fetchExpenses();
    	fetchActivities();
    	
    	startContinuousTimer();
    	
    	if(callback != null) {
    		callback.callback();
    		callback = null;
    	}
    }
    
    public void startContinuousTimer() {
    	new Thread() {
    		public void run() {
    			while(true) { //Runs forever
	    			try {
						Thread.sleep(checkAfterEvery);
						int previousCount = activities.size();
						LOGGER.setLevel(Level.OFF);
	    				fetchActivities();
	    				LOGGER.setLevel(Level.INFO);
	    				int newCount = activities.size();
	    				if(newCount - previousCount > 0) {
	    					LOGGER.info("GOT NEW NOTIFICATION " + (newCount - previousCount));
	    					MainFrame.getInstance().gotNewNotification(newCount - previousCount);
	    				}
	    				LOGGER.setLevel(Level.OFF);
	    				previousCount = currentUser.getFriends().size();
	    				fetchFriends();
	    				newCount = currentUser.getFriends().size();
	    				
	    				int previousGCount = currentUser.getGroups().size();
	    				fetchGroups();
	    				int newGCount = currentUser.getGroups().size();
	    				
	    				int previousECount = expenses.size();
	    				fetchExpenses();
	    				int newECount = expenses.size();
	    				
	    				if(newCount != previousCount || newGCount != previousGCount) {
	    					MainFrame.getInstance().reInitLeftPanel();
	    					MainFrame.getInstance().repaint();
	    				}
	    				
	    				LOGGER.setLevel(Level.INFO);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}
    	}.start();
    }
    
    public List<Activity> getActivities(){
    	List<Activity> temp = new ArrayList<Activity>();
    	acquireLock();
    	temp.addAll(this.activities);
    	releaseLock();
    	return temp;
    }
   
    public People getCurrentUser() {
    	return this.currentUser;
    }
    
    public List<Expense> getExpenses() {
    	List<Expense> retList = new ArrayList<Expense>();
    	acquireLock();
    	if(this.userId == 0 && this.groupId == 0) {
    		retList.addAll(expenses);
    	} else if(this.userId != 0){
    		LOGGER.info("Filter by User Id " + this.userId);
    		for(Expense expense : expenses) {
    			if(expense.users.contains("" + this.userId)) {
    				retList.add(expense);
    			}
    		}
    	} else if(this.groupId != 0){
    		LOGGER.info("Filter by Group Id " + this.groupId);
    		for(Expense expense : expenses) {
    			if(expense.getGroupId() == this.groupId) {
    				retList.add(expense);
    			}
    		}
    	}
    	releaseLock();
    	return retList;
    }
    
    public List<Group> getGroups() {
    	List<Group> temp = new ArrayList<Group>();
    	acquireLock();
    	temp.addAll(currentUser.getGroups());
    	releaseLock();
    	return temp;
    }
    
    public List<People> getFriends() {
    	List<People> temp = new ArrayList<People>();
    	acquireLock();
    	temp.addAll(currentUser.getFriends());
    	releaseLock();
    	return temp;
    }
    
    public People getFriend(long id) {
    	People temp;
    	acquireLock();
    	temp = currentUser.getFriend(id);
    	releaseLock();
    	return temp;
    }
    
    public Group getCurrentGroup() {
    	// Currently selected group
    	return getGroup(this.groupId);
    }
    public Group getGroup(long groupId) {
    	Group temp;
    	acquireLock();
    	temp = currentUser.getGroup(groupId);
    	releaseLock();
    	return temp;
    }
    
    public void fetchUser() {
    	LOGGER.info("Fetching User");
    	try {
			User user = SplitwiseSDK.getInstance().getCurrentUser();
			
			currentUser = new People(user);
			LOGGER.info("Fetched User " + currentUser.getId());
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
    }
    
    public void fetchFriends() {
    	LOGGER.info("Fetching Friends");
    	try {
    		List<Friend> friendList = SplitwiseSDK.getInstance().getFriends();
    		acquireLock();
    		currentUser.clearFriends();
			for(Friend friend : friendList) {
				currentUser.addFriend(new People(friend));
				//LOGGER.info("Fetched Friend " + friend.id);
			}
			Collections.sort(currentUser.getFriends(), (f1,f2)->f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase()));
			releaseLock();
			LOGGER.info("Fetched Friend " + currentUser.getFriends().size());
		} catch (APIException e) {
			e.printStackTrace();
			System.exit(0);
		}
    }
    
    public void fetchGroups() {
    	LOGGER.info("Fetching Groups");
    	try {
    		List<GroupResponse> groupResponseList = SplitwiseSDK.getInstance().getGroups();
    		acquireLock();
    		currentUser.getGroups().clear();
			for(GroupResponse group : groupResponseList) {
				currentUser.getGroups().add(new Group(group));
				//LOGGER.info("Fetched Friend " + friend.id);
			}
			Collections.sort(currentUser.getGroups(), (f1,f2)->f1.getName().compareTo(f2.getName()));
			releaseLock();
			LOGGER.info("Fetched Groups " + currentUser.getGroups().size());
		} catch (APIException e) {
			e.printStackTrace();
			System.exit(0);
		}
    }
    
    public void fetchExpenses() {
    	LOGGER.info("Fetching Expenses");
    	try {
    		List<ExpenseResponse> expenseResponseList = SplitwiseSDK.getInstance().getExpenses();
    		acquireLock();
    		expenses.clear();
			for(ExpenseResponse expense : expenseResponseList) {
				if(!expense.isDeleted)
					expenses.add(new Expense(expense));
			}
			Collections.sort(expenses, (f1,f2)->(f1.getCreatedDate().compareTo(f2.getCreatedDate()) > 0)?-1:1);
			releaseLock();
			LOGGER.info("Fetched Expenses " + expenses.size());
		} catch (APIException e) {
			e.printStackTrace();
			System.exit(0);
		}
    }
    
    public void fetchActivities() {
    	LOGGER.info("Fetching Activities");
    	try {
    		HashMap<String,String> params = new HashMap<String,String>();
    		params.put("limit","50");
    		if(lastFetchedActivity != null) {
    			params.put("updated_after",lastFetchedActivity);
    			LOGGER.info("Last Updated At " + lastFetchedActivity);
    		}
    		//activities.clear();
    		int activityCount = 0;
    		boolean isFirst = true;
    		List<ActivityResponse> arList = SplitwiseSDK.getInstance().getActivities(params);
    		acquireLock();
			for(ActivityResponse activity : arList) {
				if(isFirst) {
					lastFetchedActivity = activity.created_date_str;
					isFirst = false;
				}
				activities.add(new Activity(activity));
				activityCount++;
			}
			Collections.sort(activities, (f1,f2)->(f1.getCreatedDate().compareTo(f2.getCreatedDate()) > 0)?-1:1);
			releaseLock();
			LOGGER.info("Fetched Activities " + activityCount);
		} catch (APIException e) {
			e.printStackTrace();
			System.exit(0);
		}
    }
    
    public void setCallback(Callback callback) {
    	this.callback = callback;
    }

    public static SplitwiseCore getInstance() {
    	if(instance == null) {
    		instance = new SplitwiseCore();
    	}
    	return instance;
    }
    
    public static interface Callback {
    	public void callback();
    }

	public void setFilterByFriendId(long friendId) {
		this.userId = friendId;
	}

	public void setFilterByGroupId(long groupId) {
		this.groupId = groupId;
		
	}

	public void settleUpWith(long friendId) {
		People friend = getFriend(friendId);
		if(friend.getNetAmount() < 0) {
			String cost = String.valueOf(Math.round(Math.abs(friend.getNetAmount()) * 100.0f)/100.0f);
			String description = "Payment";
			HashMap<String,String> expenseParams = new HashMap<String, String>();
			expenseParams.put("cost",cost);
			expenseParams.put("description",description);
			expenseParams.put("payment","true");
			expenseParams.put("creation_method","payment");
			expenseParams.put("users__0__user_id",String.valueOf(currentUser.getId()));
			expenseParams.put("users__0__paid_share",cost);
			expenseParams.put("users__0__owed_share","0.0");
			expenseParams.put("users__1__user_id",String.valueOf(friendId));
			expenseParams.put("users__1__paid_share","0.0");
			expenseParams.put("users__1__owed_share",cost);
			
			for(String key : expenseParams.keySet()) {
				LOGGER.info(key + " : " + expenseParams.get(key));
			}
			
			new Thread(){
				public void run() {
					try {
						ExpenseResponse er = SplitwiseSDK.getInstance().createExpense(expenseParams);
						LOGGER.info("Created expense " + er.id);
						expenses.add(new Expense(er));
						fetchFriends();
						//fetchActivities();
						fetchExpenses();
						fetchGroups();
						if(callback != null) {
							callback.callback();
							callback=null;
						}
					} catch (APIException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
	
	public void createSplitExpense(String cost, String description, long userIds[]) {
		createSplitExpense( cost, description, userIds, -1); //-1 to indicate that group id parameter has not to be included
	}
	public void createSplitExpense(String cost, String description, long userIds[], long groupId) {
		HashMap<String,String> expenseParams = new HashMap<String, String>();
		expenseParams.put("cost",cost);
		expenseParams.put("description",description);
		if(groupId != -1) {
			expenseParams.put("group_id",String.valueOf(groupId));
		}
		float value = Float.parseFloat(cost);
		float splitValue = value / userIds.length;
		splitValue = Math.round(splitValue * 100.0f)/100.0f;
		float remainingValue = value;
		// Users
		for(int i=0;i<userIds.length-1;i++) {
			long id = userIds[i];
			float paid_share = 0;
			float owed_share = splitValue;
			if(id == this.currentUser.getId()) {
				paid_share = value;
			}
			expenseParams.put("users__"+i+"__user_id",String.valueOf(id));
			expenseParams.put("users__"+i+"__paid_share",String.valueOf(paid_share));
			expenseParams.put("users__"+i+"__owed_share",String.valueOf(owed_share));
			remainingValue -= splitValue;
		}
		// For last user
		int i = userIds.length - 1;
		float paid_share = 0;
		if(userIds[i] == this.currentUser.getId()) {
			paid_share = value;
		}
		expenseParams.put("users__"+i+"__user_id",String.valueOf(userIds[i]));
		expenseParams.put("users__"+i+"__paid_share",String.valueOf(paid_share));
		expenseParams.put("users__"+i+"__owed_share",String.valueOf(remainingValue));
		
		for(String key : expenseParams.keySet()) {
			LOGGER.info(key + " : " + expenseParams.get(key));
		}
		
		new Thread(){
			public void run() {
				try {
					ExpenseResponse er = SplitwiseSDK.getInstance().createExpense(expenseParams);
					LOGGER.info("Created expense " + er.id);
					expenses.add(new Expense(er));
					fetchFriends();
					//fetchActivities();
					fetchExpenses();
					fetchGroups();
					if(callback != null) {
						callback.callback();
						callback=null;
					}
				} catch (APIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void createFriend(Map<String, String> args) {
		for(String key : args.keySet()) {
			LOGGER.info(key + " : " + args.get(key));
		}
		
		
			new Thread(){
				public void run() {
					try {
						Friend friend = SplitwiseSDK.getInstance().createFriend(args);
						LOGGER.info("Created friend " + friend.id + " " + friend.first_name);
						SplitwiseCore.getInstance().getCurrentUser().addFriend(new People(friend));
						//Update Entire list of friends
						fetchFriends();
						//fetchActivities();
						MainFrame.getInstance().reInitLeftPanel();
						MainFrame.getInstance().repaint();
					} catch (APIException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		
	}
	
	public void createGroup(Map<String, String> args) {
		args.put("simplify_by_default","true");
		for(String key : args.keySet()) {
			LOGGER.info(key + " : " + args.get(key));
		}
		
		
			new Thread(){
				public void run() {
					try {
						GroupResponse groupResponse = SplitwiseSDK.getInstance().createGroup(args);
						LOGGER.info("Created Group " + groupResponse.id + " " + groupResponse.name);
						
						//Update Entire list of Groups
						fetchGroups();
						//fetchActivities();
						MainFrame.getInstance().reInitLeftPanel();
						MainFrame.getInstance().repaint();
					} catch (APIException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		
	}
	
	public void createGroupMember(Map<String, String> args) {
		if(this.groupId == 0) {
			LOGGER.warning("Group Id is missing");
			return;
		}
		args.put("group_id",String.valueOf(this.groupId));
		for(String key : args.keySet()) {
			LOGGER.info(key + " : " + args.get(key));
		}
		
		long thatGroupId = this.groupId;
			new Thread(){
				public void run() {
					try {
						Response response = SplitwiseSDK.getInstance().createGroupMember(args);
						LOGGER.info("Created Group Member");
						
						//Update Entire list of Groups
						fetchGroups();
						//fetchActivities();
						MainFrame.getInstance().showGroupExpenses(thatGroupId);;
						MainFrame.getInstance().repaint();
					} catch (APIException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
	}

	public void acquireLock() {
		if(lock.isLocked()) {
			LOGGER.info(Thread.currentThread().getName() + " Unable to acquire the lock");
			lock.lock();
			LOGGER.info(Thread.currentThread().getName() + " Acquired the lock");
			return;
		} else {
			lock.lock();
		}
	}
	public void releaseLock() {
		lock.unlock();
	}

	

	
}
