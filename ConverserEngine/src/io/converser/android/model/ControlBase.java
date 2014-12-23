package io.converser.android.model;

public abstract class ControlBase extends ConversationAtom {

    protected ControlActions action;

    public ControlActions getActions() {
        return action;
    }

    public boolean hasActions() {
        return action != null;
    }


}
