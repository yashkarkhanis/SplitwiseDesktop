package com.splitwise.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.splitwise.splitwisesdk.responses.GroupResponse;

public class Group {

        private long id;
        private String name;
        private Date updated_at;
        private ArrayList<GroupMember> members = new  ArrayList<>();
        private ArrayList<Expense> expenses = new ArrayList<>();

        public Group(GroupResponse group) {
			this.id = group.id;
			this.name = group.name;
			this.updated_at = group.updated_at;
			
			for(GroupResponse.GroupMemberResponse gmr : group.groupMembers) {
				members.add(new GroupMember(gmr));
			}
		}



        public boolean addPeople(People people) {
//            return addGroupMember(new GroupMember(people,id));
        	return false;
        }

        public boolean removeGroupMember(GroupMember groupMember) {

            /*for (int i = 0; i < members.size(); i++) {
                if ((members.get(i).getMember().equals(groupMember.getMember())) && (members.get(i).getGroupId()
                        .equals(groupMember.getGroupId())
                )) {
                    members.remove(i);
                    return true;
                }
            }*/
            return false;


            // Verify that groupMember is in members
            // If yes then remove it and return true
            // else do nothing and return false
        }

    /*public boolean removePeople(People people) {
        return removeGroupMember(new GroupMember(people, groupId));
    }*/

    public void getTotalBalance(){

    }

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}
	
	public Date getUpdatedAt() {
		return this.updated_at;
	}

	public List<GroupMember> getMembers() {
		// TODO Auto-generated method stub
		return this.members;
	}

}
