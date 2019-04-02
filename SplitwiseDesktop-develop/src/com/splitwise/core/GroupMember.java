package com.splitwise.core;

import com.splitwise.splitwisesdk.responses.GroupResponse;

public class GroupMember {
    private long memberId;
    private float balance;
    private String groupId;

    GroupMember(GroupResponse.GroupMemberResponse gmr) {
        this.memberId = gmr.id;
        this.balance = gmr.balance_amount;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getGroupId() {
        return groupId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
