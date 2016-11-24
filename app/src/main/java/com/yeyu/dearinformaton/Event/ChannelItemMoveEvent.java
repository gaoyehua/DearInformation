package com.yeyu.dearinformaton.Event;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public class ChannelItemMoveEvent {
    private int fromPosition;
    private int toPosition;

    public int getFromPosition() {
        return fromPosition;
    }

    public int getToPosition() {
        return toPosition;
    }

    public ChannelItemMoveEvent(int fromPosition, int toPosition) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;

    }
}
