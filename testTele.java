package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
@TeleOp(name = "testTele", group = "Concept")
/*
TeleOp is stupidly simple. Don't worry, you can get it easily.
*/
public class TeleopMecanum_5404brute extends OpMode {
    double          clawOffset     = 0.5;
    double          rotateOffset   = 0.5;
    final double    CLAW_SPEED      = 0.002 ;                   // sets rate to move servo
    double left;
    double right;
    double drive;
    double turn;
    double max;

    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor armExtendA;
    DcMotor armExtendB;
    DcMotor gripper;
    Servo   claw;
    Servo   rotate;

    public MecanumDrivetrain drivetrain;

    public testTele() {

    }

    @Override
    public void init() {

        motorFrontRight = hardwareMap.dcMotor.get("motor front right");
        motorFrontLeft = hardwareMap.dcMotor.get("motor front left");
        motorBackLeft = hardwareMap.dcMotor.get("motor back left");
        motorBackRight = hardwareMap.dcMotor.get("motor back right");
        armExtendA = hardwareMap.dcMotor.get("arm extend A");
        armExtendB = hardwareMap.dcMotor.get("arm extend B");
        gripper = hardwareMap.dcMotor.get("gripper");
        claw   = hardwareMap.servo.get("claw");
        rotate   = hardwareMap.servo.get("rotate");

        drivetrain = new MecanumDrivetrain(new DcMotor[]{motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight});


        //These work without reversing (Tetrix motors).
        //AndyMark motors may be opposite, in which case uncomment these lines:
        //motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        //motorBackRight.setDirection(DcMotor.Direction.REVERSE);
    }


    @Override
    public void loop() {
        //sets up the driving wheel part
        double course = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI/2;
        double velocity = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double rotation = -0.5 * gamepad1.right_stick_x;

        drivetrain.setCourse(course);
        drivetrain.setVelocity(0.5 * velocity);
        drivetrain.setRotation(rotation);




        // I believe you can simply understand these codes...right?
        if (gamepad1.dpad_right){
            armExtendA.setPower(0.2);
        }
        else if (gamepad1.dpad_left) {
            armExtendA.setPower(-0.4);
        }
        else {
            armExtendA.setPower(0);
        }

        if (gamepad1.dpad_up){
            armExtendB.setPower(-0.4);
        }
        else if (gamepad1.dpad_down) {
            armExtendB.setPower(0.3);
        }
        else  {
            armExtendB.setPower(0);
        }

        if (gamepad1.x) {
            gripper.setPower(0.2);

        }
        else if (gamepad1.b) {
            gripper.setPower(-0.2);
        }
        else {
            gripper.setPower(0);
        }

        //use gamepad left & right trigger to rotate claw
        if (gamepad1.right_trigger > 0.4) {
            rotateOffset += CLAW_SPEED;
        }
        else if (gamepad1.left_trigger > 0.4) {
            rotateOffset -= CLAW_SPEED;
        }

        rotateOffset = Range.clip(rotateOffset, -0.5, 0.5);//keeps the result into -.5~.5
        rotate.setPosition(0.5 + rotateOffset);

        if (gamepad1.left_bumper) {
            clawOffset += CLAW_SPEED;
        }
        else if (gamepad1.right_bumper) {
            clawOffset -= CLAW_SPEED;
        }

        clawOffset = Range.clip(clawOffset, -1, 1);
        claw.setPosition(0.5 + clawOffset);
    }

    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
