package org.firstinspires.ftc.teamcode;//you want it every time
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;//sensor. In case you wanna use other sensors, just look at samples
import com.qualcomm.robotcore.*;//the '*' sign is the magic––every stuff under this library will be imported.
import org.firstinspires.ftc.*;//also an example
import java.lang.*;//another example!
import java.util.*;//you know what I'm gonna say
@Autonomous(name="testAuto", group="Pushbot")//make sure name="" matches the name you want. I'd say file name.
public class testAuto extends LinearOpMode{
    //this is the part you'd initialize every part's name.

    private ElapsedTime runtime = new ElapsedTime();//timer
    DcMotor motorFL,motorFR,motorBL,motorBR,roller;
    Servo clipper;

    static final double     COUNTS_PER_MOTOR_REV    = 490 ;//Depending on different motors, this may vary. This is for gobilda.
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;
    static final double     WHEEL_DIAMETER_INCHES   = 4.72441 ;//wheel diameter. Change it when using different wheels.
    static final double     COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    //just for initializing. Connect all parts and put servos in correct positions.
    public void initialize(){
        //make sure the name in get() matches their names on the configuration in phone.
        motorFL=hardwareMap.dcMotor.get("Motor Front Left");        
        motorBL=hardwareMap.dcMotor.get("Motor Back Left");
        motorFR=hardwareMap.dcMotor.get("Motor Front Right");
        motorBR=hardwareMap.dcMotor.get("Motor Back Right");
        roller=hardwareMap.dcMotor.get("roller");
        motorFL.setDirection(DcMotor.Direction.FORWARD);//set reverse for Andymark motors
        motorFR.setDirection(DcMotor.Direction.FORWARD);
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR.setDirection(DcMotor.Direction.FORWARD);
        
        motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        clipper=hardwareMap.servo.get("clipper");
        clipper.setPosition(0.5);
    }
    public void runOpMode(){//the real running part
        initialize();
        waitForStart();//after this line, all begins.
        encoderDrive(0.5,25,25,25,25,4);//drive 25 inches forwardin 4 secs with speed of 0.5
        encoderDrive(0.5,-15,15,-15,15,2);//turn left in 2 secs
        clipper.setPosition(0);
        sleep(1000);//sleep 1000 miliseconds, aka 1 sec
        clipper.setPosition(1);
        roller.setPower(0.5);
        sleep(1000);// All components would keep their state during this time. 
        //For example, roller would still keep running during this time
        roller.setPower(0);
        sleep(1000);//it can give the parts a reasonable time for running
        //and prevent parts from being overwhelmed.
    }

    //below is the function for driving four wheels.
    //I guess you know what it means by each parameter here
    //make sure directions all match:
    //front: all positive
    //back: all negative
    //turn left: left wheels negative, right wheels positive
    //turn right: right wheels negative, left wheels positive
    //some moves that aren't that consistent:
    //move right horizontally: front left and back right positive, other two negative
    //move left horizontally: vice versa
    /*depending on what wheels you use. 
    if you use mechanum wheels, you can try horizontally or diagnolly
    other wise, just move front back or turn

    speed ranges 0~1.
    */
    public void encoderDrive(double speed,
                             double FLD, double FRD, double BLD, double BRD,
                             double timeoutS) {


        int newFLTarget;
        int newFRTarget;
        int newBLTarget;
        int newBRTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller

            newFLTarget = motorFL.getCurrentPosition() + (int)(FLD * COUNTS_PER_INCH);
            newFRTarget = motorFR.getCurrentPosition() + (int)(FRD * COUNTS_PER_INCH);
            newBLTarget = motorBL.getCurrentPosition() + (int)(BLD * COUNTS_PER_INCH);
            newBRTarget = motorBR.getCurrentPosition() + (int)(BRD * COUNTS_PER_INCH);



            motorFL.setTargetPosition(newFLTarget);
            motorFR.setTargetPosition(newFRTarget);
            motorBL.setTargetPosition(newBLTarget);
            motorBR.setTargetPosition(newBRTarget);

            // Turn On RUN_TO_POSITION
            motorFL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorFR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorBL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorBR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.

            runtime.reset();

            motorFL.setPower(speed);
            motorFR.setPower(speed);
            motorBL.setPower(speed);
            motorBR.setPower(speed);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (motorFL.isBusy() || motorFR.isBusy() || motorBL.isBusy() || motorBR.isBusy() ))
            {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d :%7d :%7d", newFLTarget, newFRTarget, newBLTarget, newBRTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
                        motorFL.getCurrentPosition(),
                        motorFR.getCurrentPosition(),
                        motorBL.getCurrentPosition(),
                        motorBR.getCurrentPosition());
                telemetry.addData("motor pwr:", "%.2f", speed);
                telemetry.update();
            }

            // Stop all motion;
            motorFL.setPower(0);
            motorFR.setPower(0);
            motorBL.setPower(0);
            motorBR.setPower(0);

            // Turn off RUN_TO_POSITION
            motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //sleep(250);   // optional pause after each move
        }
    }
}
