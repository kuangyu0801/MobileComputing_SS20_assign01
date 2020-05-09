package mcteam08.assign.task01;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;


/** Note for this calculator
 * The calculator only support basic arithmetics, which includes:
 * - can only take a single input at a time
 * - operator has equal priority and is executed in FIFO order
 * - reset only with a CLEAR button, otherwise the result from last calculation continues
 * Operation:
 * numButtons: store the number into StringBuffer
 * optButtons:
 *  - parse the current StringBuffer into Integer and store it in a
* */

public class Task01 extends AppCompatActivity {

    final private String TAG = Task01.class.getCanonicalName();
    private Button[] numButtons = new Button[10];
    private Button[] optButtons = new Button[5]; // =, +, -, *, /
    final private String[] OPT = new String[] {"ADD", "SUB", "MUL", "DIV", "EQU"};
    final private HashSet<String> OPTSET = new HashSet<>(Arrays.asList(OPT));
    private Button clearButton;
    private float result = 0;
    private StringBuffer inputStr = new StringBuffer(); // used to store the input string
    private LinkedList<String> optList = new LinkedList();
    private TextView textViewDisplay;
    //private TextView textViewDisplay = (TextView) findViewById(R.id.textViewDisplay);


    private float calculate(float pre, float cur, String opt) {

        if (opt.equals(OPT[0])) {
            return pre + cur;
        } else if (opt.equals(OPT[1])) {
            return pre - cur;
        } else if (opt.equals(OPT[2])) {
            return pre * cur;
        } else if (opt.equals(OPT[3])) {
            return pre / cur;
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task01);
        textViewDisplay = findViewById(R.id.textViewDisplay);

        numButtons[0] = findViewById(R.id.buttonNum0);
        numButtons[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "0 entered");
                inputStr.append("0");
                textViewDisplay.setText(inputStr.toString());
            }
        });



        numButtons[1] = findViewById(R.id.buttonNum1);
        numButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "1 entered");
                inputStr.append("1");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        numButtons[2] = findViewById(R.id.buttonNum2);
        numButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "2 entered");
                inputStr.append("2");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        numButtons[3] = findViewById(R.id.buttonNum3);
        numButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "3 entered");
                inputStr.append("3");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        numButtons[4] = findViewById(R.id.buttonNum4);
        numButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "4 entered");
                inputStr.append("4");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        numButtons[5] = findViewById(R.id.buttonNum5);
        numButtons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "5 entered");
                inputStr.append("5");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        numButtons[6] = findViewById(R.id.buttonNum6);
        numButtons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "6 entered");
                inputStr.append("6");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        numButtons[7] = findViewById(R.id.buttonNum7);
        numButtons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "7 entered");
                inputStr.append("7");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        numButtons[8] = findViewById(R.id.buttonNum8);
        numButtons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "8 entered");
                inputStr.append("8");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        numButtons[9] = findViewById(R.id.buttonNum9);
        numButtons[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "9 entered");
                inputStr.append("9");
                textViewDisplay.setText(inputStr.toString());
            }
        });

        optButtons[0] = findViewById(R.id.buttonEqual);
        optButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, OPT[4] + "entered");
                String inNumStr = inputStr.toString();
                Log.i(TAG, "input number: " + inNumStr);

                // this means button no numbers has been entered before this operation
                if (!inNumStr.isEmpty()) {
                    optList.addLast(inNumStr);
                    optList.addLast(OPT[4]);
                    textViewDisplay.setText(inNumStr + OPT[4]);
                }

                float curNum = 0;
                float preNum = 0;
                String curOpt = "";
                for (int i = 0; i < optList.size(); i += 1) {
                    String s = optList.get(i);
                    Log.i(TAG, "[Iteration] " + i);
                    Log.i(TAG, "[Parsing]: " + s);

                    /** Evaluation Always ends with EQUAL */
                    if (OPTSET.contains(s)) {
                        // equal
                        if (s.equals(OPT[4])) {
                            result = curNum;
                            textViewDisplay.setText(String.valueOf(result));
                            break;
                        } else {
                            curOpt = s;
                            preNum = curNum;
                        }
                    } else {
                        curNum = Integer.parseInt(s);
                        if (!curOpt.isEmpty()) {
                            curNum = calculate(preNum, curNum, curOpt);
                        }
                    }
                }
                Log.i(TAG, "final result: " + result);
                optList.clear();
                inputStr.setLength(0);
            }
        });

        optButtons[1] = findViewById(R.id.buttonPlus);
        optButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, OPT[0] + " entered");
                String inNumStr = inputStr.toString();
                Log.i(TAG, "input number: " + inNumStr);

                // this means button no numbers has been entered before this operation
                if (!inNumStr.isEmpty()) {
                    optList.addLast(inNumStr);
                    optList.addLast(OPT[0]);
                    inputStr.setLength(0);
                    textViewDisplay.setText(inNumStr + OPT[0]);
                }
            }
        });

        optButtons[2] = findViewById(R.id.buttonMinus);
        optButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, OPT[1] + " entered");
                String inNumStr = inputStr.toString();
                Log.i(TAG, "input number: " + inNumStr);

                // this means button no numbers has been entered before this operation
                if (!inNumStr.isEmpty()) {
                    optList.addLast(inNumStr);
                    optList.addLast(OPT[1]);
                    inputStr.setLength(0);
                    textViewDisplay.setText(inNumStr + OPT[1]);
                }
            }
        });

        optButtons[3] = findViewById(R.id.buttonMultiply);
        optButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, OPT[2] + " entered");
                String inNumStr = inputStr.toString();
                Log.i(TAG, "input number: " + inNumStr);

                // this means button no numbers has been entered before this operation
                if (!inNumStr.isEmpty()) {
                    optList.addLast(inNumStr);
                    optList.addLast(OPT[2]);
                    inputStr.setLength(0);
                    textViewDisplay.setText(inNumStr + OPT[2]);
                }
            }
        });

        optButtons[4] = findViewById(R.id.buttonDivision);
        optButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, OPT[3] + " entered");
                String inNumStr = inputStr.toString();
                Log.i(TAG, "input number: " + inNumStr);

                // this means button no numbers has been entered before this operation
                if (!inNumStr.isEmpty()) {
                    optList.addLast(inNumStr);
                    optList.addLast(OPT[3]);
                    inputStr.setLength(0);
                    textViewDisplay.setText(inNumStr + OPT[3]);
                }
            }
        });

        clearButton = findViewById(R.id.buttonClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "CLEAR entered");
                result = 0;
                optList.clear();
                inputStr.setLength(0);
                textViewDisplay.setText(R.string.button_name_0);
            }
        });

    }
}
