package uk.ac.lancaster.scc210.game.controller;

import org.jsfml.window.Joystick;

public enum ControllerAxis {
    D_PAD_LEFT_RIGHT(Joystick.Axis.POV_X),
    D_PAD_UP_DOWN(Joystick.Axis.POV_Y),
    LEFT_TRIGGER(Joystick.Axis.R),
    RIGHT_TRIGGER(Joystick.Axis.Z),
    RIGHT_JOYSTICK_LEFT_RIGHT(Joystick.Axis.U),
    RIGHT_JOYSTICK_UP_DOWN(Joystick.Axis.V),
    LEFT_JOYSTICK_LEFT_RIGHT(Joystick.Axis.X),
    LEFT_JOYSTICK_UP_DOWN(Joystick.Axis.Y);

    // Most axises will register themselves somewhere around -5 to +5 even when not pressed, so let's have a threshold
    public static final float AXIS_POSITIVE_THRESHOLD = 20;

    public static final float AXIS_NEGATIVE_THRESHOLD = -20;

    private final Joystick.Axis axis;

    ControllerAxis(Joystick.Axis axis) {
        this.axis = axis;
    }

    public float getAxisPosition() {
        return Joystick.getAxisPosition(Controller.CONTROLLER_ID, this.axis);
    }

    public boolean hasAxis() {
        return Joystick.hasAxis(Controller.CONTROLLER_ID, this.axis);
    }

    public boolean axisMoved() {
        float axisPosition = getAxisPosition();

        return axisPosition <= AXIS_NEGATIVE_THRESHOLD || axisPosition >= AXIS_POSITIVE_THRESHOLD;
    }

    public boolean axisesMoved(ControllerAxis other) {
        return this.axisAcrossThresholds() && other.axisAcrossThresholds();
    }

    private boolean axisAcrossThresholds() {
        float position = this.getAxisPosition();

        return position < ControllerAxis.AXIS_NEGATIVE_THRESHOLD || position > ControllerAxis.AXIS_POSITIVE_THRESHOLD;
    }
}
