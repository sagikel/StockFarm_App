package com.example.stockfarm_app.ui.tutorial;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;
import com.example.stockfarm_app.StockFarmApplication;
import com.example.stockfarm_app.data.TutorialData;

import java.util.ArrayList;
import java.util.Collections;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class TutorialFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final long TIME_COUNTDOWN = 30050;

    Button buttonBack;
    ListView listView;
    String[] options;
    TutorialData tutorialData;
    ScrollView scrollView;
    TextView def;
    TextView defText;
    Button test;
    String defName;
    boolean windowOpen;
    TextView countDown;
    CountDownTimer countDownTimer;
    boolean timerRunning;
    long timeLeft = TIME_COUNTDOWN;
    TextView headline;
    TextView question;
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;
    KonfettiView konfettiView;
    EditText filter;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter1;
    ArrayAdapter<String> arrayAdapter2;
    ArrayAdapter<String> arrayAdapter3;
    ArrayAdapter<String> arrayAdapter4;
    ArrayAdapter<String> arrayAdapter5;
    Button terms;
    Button syllabus;
    boolean termsBol;
    boolean syllabusBol;
    boolean syllabusBol2;
    boolean testWindow;
    Button syllabusA;
    Button syllabusB;
    Button syllabusC;
    Button syllabusD;
    Button syllabusE;
    ListView listView2;
    TextView completeText;
    String[] headlines;
    int sylCounter;
    int sylCounter2;
    int index;
    StockFarmApplication app;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        buttonBack = view.findViewById(R.id.button_back);
        listView = view.findViewById(R.id.list_view);
        scrollView = view.findViewById(R.id.scroll_view_window);
        def = view.findViewById(R.id.definition);
        defText = view.findViewById(R.id.definition_text);
        test = view.findViewById(R.id.test_button);
        countDown = view.findViewById(R.id.countdown);
        headline = view.findViewById(R.id.title);
        question = view.findViewById(R.id.question);
        buttonA = view.findViewById(R.id.buttonA);
        buttonB = view.findViewById(R.id.buttonB);
        buttonC = view.findViewById(R.id.buttonC);
        buttonD = view.findViewById(R.id.buttonD);
        konfettiView = view.findViewById(R.id.viewKonfetti);
        filter = view.findViewById(R.id.editTextFilter);
        terms = view.findViewById(R.id.terms_button);
        syllabus = view.findViewById(R.id.syl_button);
        syllabusA = view.findViewById(R.id.syllabusA);
        syllabusB = view.findViewById(R.id.syllabusB);
        syllabusC = view.findViewById(R.id.syllabusC);
        syllabusD = view.findViewById(R.id.syllabusD);
        syllabusE = view.findViewById(R.id.syllabusE);
        listView2 = view.findViewById(R.id.list_view2);
        completeText = view.findViewById(R.id.complete);

        app = (StockFarmApplication) getActivity().getApplication();

        tutorialData = new TutorialData();
        options = new String[tutorialData.getMap().size()];
        defName = "";
        windowOpen = false;
        timerRunning = false;
        termsBol = false;
        syllabusBol = false;
        syllabusBol2 = false;
        testWindow = false;
        headlines = new String[]{"What is this App?", "First Steps", "Learn The Market", "Risk & Reward", "Smart Investor"};
        sylCounter = 0;
        sylCounter2 = 0;
        index = 0;

        int counter = 0;
        ArrayList<String> d = new ArrayList<>(tutorialData.getMap().keySet());
        Collections.sort(d); // אפשר למנוע את זה אם אסדר את המידע באופן אלפבתי בmap
        for (String string : d) {

            options[counter++] = string;
        }

        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, options);
        arrayAdapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new String[]{"1. a", "2. b", "3. c", "4. d", "5. e"});
        arrayAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new String[]{"1. f", "2. g", "3. h", "4. i", "5. j"});
        arrayAdapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new String[]{"1. k", "2. l", "3. m", "4. n", "5. o"});
        arrayAdapter4 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new String[]{"1. p", "2. q", "3. r", "4. s", "5. t"});
        arrayAdapter5 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new String[]{"1. u", "2. v", "3. w", "4. x", "5. y"});

        listView.setAdapter(arrayAdapter);
        listView2.setAdapter(arrayAdapter1);

        listView.setOnItemClickListener(this);
        listView2.setOnItemClickListener(this);

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {(TutorialFragment.this).arrayAdapter.getFilter().filter(s);}
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                terms.setVisibility(View.INVISIBLE);
                syllabus.setVisibility(View.INVISIBLE);
                filter.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                buttonBack.setVisibility(View.VISIBLE);
                termsBol = true;
            }
        });

        syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terms.setVisibility(View.INVISIBLE);
                syllabus.setVisibility(View.INVISIBLE);
                syllabusA.setVisibility(View.VISIBLE);
                syllabusB.setVisibility(View.VISIBLE);
                syllabusC.setVisibility(View.VISIBLE);
                syllabusD.setVisibility(View.VISIBLE);
                syllabusE.setVisibility(View.VISIBLE);
                buttonBack.setVisibility(View.VISIBLE);
                headline.setText("Syllabus");
                headline.setVisibility(View.VISIBLE);
                syllabusBol = true;
            }
        });


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termsBol) {
                    if (windowOpen) {
                        closeWindow();
                    }
                    else if (testWindow){
                        countDown.setVisibility(View.INVISIBLE);
                        timeOut();
                        listView.setVisibility(View.VISIBLE);
                        filter.setVisibility(View.VISIBLE);
                        countDown.setTextColor(Color.BLACK);
                        stopTimer();
                        testWindow = false;
                    }
                    else {
                        filter.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                        buttonBack.setVisibility(View.INVISIBLE);
                        terms.setVisibility(View.VISIBLE);
                        syllabus.setVisibility(View.VISIBLE);
                        termsBol = false;
                    }
                }
                else {
                    if (windowOpen) {
                        closeWindow();
                    } else if (testWindow) {
                        countDown.setVisibility(View.INVISIBLE);
                        timeOut();
                        listView2.setVisibility(View.VISIBLE);
                        complete();
                        countDown.setTextColor(Color.BLACK);
                        stopTimer();
                        testWindow = false;
                    } else if (syllabusBol2) {
                        headline.setText("SYLLABUS");
                        listView2.setVisibility(View.INVISIBLE);
                        completeText.setVisibility(View.INVISIBLE);
                        syllabusA.setVisibility(View.VISIBLE);
                        syllabusB.setVisibility(View.VISIBLE);
                        syllabusC.setVisibility(View.VISIBLE);
                        syllabusD.setVisibility(View.VISIBLE);
                        syllabusE.setVisibility(View.VISIBLE);
                        syllabusBol2 = false;
                    } else {
                        syllabusA.setVisibility(View.INVISIBLE);
                        syllabusB.setVisibility(View.INVISIBLE);
                        syllabusC.setVisibility(View.INVISIBLE);
                        syllabusD.setVisibility(View.INVISIBLE);
                        syllabusE.setVisibility(View.INVISIBLE);
                        buttonBack.setVisibility(View.INVISIBLE);
                        headline.setVisibility(View.INVISIBLE);
                        terms.setVisibility(View.VISIBLE);
                        syllabus.setVisibility(View.VISIBLE);
                        syllabusBol = false;
                    }
                }
            }
        });

        syllabusA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sylCounter = 0;
                listView2.setAdapter(arrayAdapter1);
                completeText.setVisibility(View.VISIBLE);
                syllabusEnter();
                syllabusBol2 = true;
                index = 0;
                complete();
            }
        });

        syllabusB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sylCounter = 1;
                listView2.setAdapter(arrayAdapter2);
                syllabusEnter();
                syllabusBol2 = true;
                index = 5;
                complete();
            }
        });

        syllabusC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sylCounter = 2;
                listView2.setAdapter(arrayAdapter3);
                syllabusEnter();
                syllabusBol2 = true;
                index = 10;
                complete();
            }
        });

        syllabusD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sylCounter = 3;
                listView2.setAdapter(arrayAdapter4);
                syllabusEnter();
                syllabusBol2 = true;
                index = 15;
                complete();
            }
        });

        syllabusE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sylCounter = 4;
                listView2.setAdapter(arrayAdapter5);
                syllabusEnter();
                syllabusBol2 = true;
                index = 20;
                complete();
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
                if (termsBol) {
                    listView.setVisibility(View.INVISIBLE);
                    filter.setVisibility(View.INVISIBLE);
                }
                else {
                    listView2.setVisibility(View.INVISIBLE);
                    completeText.setVisibility(View.INVISIBLE);
                }
                countDown.setVisibility(View.VISIBLE);
                testWindow = true;
                startTimer();
            }
        });
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                    checkChoice("A");
                }
            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                    checkChoice("B");
                }
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                    checkChoice("C");
                }
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                    checkChoice("D");
                }
            }
        });
        return view;
    }

    private void complete() {
        if (app.userData.getCompleteBol()[25+(index/5)]) {
            completeText.setText("Completed");
            konfettiView.build()
                    .addColors(0xFF0C6325, Color.BLACK, Color.WHITE)
                    .setDirection(0.0, 359.0)
                    .setSpeed(0f, 6f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(5000L)
                    .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                    .addSizes(new Size(8,20f))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .burst(5000);
        }
        else {
            StringBuilder show = new StringBuilder();
            show.append("Not Completed: ");
            for (int i = 0; i < 5 ; i++) {
                if (!app.userData.getCompleteBol()[index+i]) {
                    show.append(i+1).append(", ");
                }
            }
            show.setLength(show.length() - 2);
            completeText.setText(show);
        }
        completeText.setVisibility(View.VISIBLE);
    }

    private void syllabusEnter() {
        syllabusA.setVisibility(View.INVISIBLE);
        syllabusB.setVisibility(View.INVISIBLE);
        syllabusC.setVisibility(View.INVISIBLE);
        syllabusD.setVisibility(View.INVISIBLE);
        syllabusE.setVisibility(View.INVISIBLE);
        listView2.setVisibility(View.VISIBLE);
        headline.setText(headlines[sylCounter]);
    }

    private void checkChoice(String answer) {
        switch (answer) {
            case "A":
                feedback("A");
                buttonB.setVisibility(View.INVISIBLE);
                buttonC.setVisibility(View.INVISIBLE);
                buttonD.setVisibility(View.INVISIBLE);
                break;
            case "B":
                feedback("B");
                buttonA.setVisibility(View.INVISIBLE);
                buttonC.setVisibility(View.INVISIBLE);
                buttonD.setVisibility(View.INVISIBLE);
                break;
            case "C":
                feedback("C");
                buttonB.setVisibility(View.INVISIBLE);
                buttonA.setVisibility(View.INVISIBLE);
                buttonD.setVisibility(View.INVISIBLE);
                break;
            case "D":
                feedback("D");
                buttonB.setVisibility(View.INVISIBLE);
                buttonC.setVisibility(View.INVISIBLE);
                buttonA.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void feedback(String string) {
        String rightAnswer = "";
        if (termsBol) {
            rightAnswer = tutorialData.getMap().get(defName).get(6);
        } else {
            rightAnswer = tutorialData.getMap2().get(defName).get(6);
        }
        if (rightAnswer.equals(string)) {
            if (syllabusBol) {
                if (!app.userData.getCompleteBol()[index + sylCounter2]) {
                    app.userData.getCompleteBol()[index + sylCounter2] = true;
                    int counter = 5;
                    for (int i = 0; i < 5; i++) {
                        if (app.userData.getCompleteBol()[index + i]) {
                            counter--;
                            continue;
                        }
                        break;
                    }
                    if (counter == 0) {
                        app.userData.getCompleteBol()[25+(index/5)] = true;
                    }
                    // fireStore
                    app.updateUserDataToServer();
                }
            }
            question.setText("Correct!");
            konfettiView.build()
                    .addColors(0xFF0C6325, Color.BLACK, Color.GRAY)
                    .setDirection(0.0, 359.0)
                    .setSpeed(0f, 6f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(5000L)
                    .addShapes(Shape.RECT,Shape.CIRCLE)
                    .addSizes(new Size(8, 20))
                    .setPosition(konfettiView.getX()+konfettiView.getWidth()/2f,
                            konfettiView.getY()+konfettiView.getHeight()/2f)
                    .burst(5000);
        } else {
            question.setText("Wrong..\nTry Again");
        }
    }

    private void timeOut() {
        if (termsBol) {
            headline.setVisibility(View.INVISIBLE);
        }
        else {
            headline.setText(headlines[sylCounter]);
        }
        question.setVisibility(View.INVISIBLE);
        buttonA.setVisibility(View.INVISIBLE);
        buttonB.setVisibility(View.INVISIBLE);
        buttonC.setVisibility(View.INVISIBLE);
        buttonD.setVisibility(View.INVISIBLE);
    }

    private void startTimer() {
        headline.setText(defName);
        if (termsBol) {
            question.setText(tutorialData.getMap().get(defName).get(1));
            buttonA.setText(tutorialData.getMap().get(defName).get(2));
            buttonB.setText(tutorialData.getMap().get(defName).get(3));
            buttonC.setText(tutorialData.getMap().get(defName).get(4));
            buttonD.setText(tutorialData.getMap().get(defName).get(5));
        } else {
            question.setText(tutorialData.getMap2().get(defName).get(1));
            buttonA.setText(tutorialData.getMap2().get(defName).get(2));
            buttonB.setText(tutorialData.getMap2().get(defName).get(3));
            buttonC.setText(tutorialData.getMap2().get(defName).get(4));
            buttonD.setText(tutorialData.getMap2().get(defName).get(5));
        }


        headline.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);

        timerRunning = true;
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                int sec = (int) (timeLeft/1000) % 60;
                countDown.setText(String.valueOf(sec));
                if (sec == 5) {
                    countDown.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                countDown.setText("Time out!");
                timerRunning = false;
                timeOut();
            }
        }.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timeLeft = TIME_COUNTDOWN;
        timerRunning = false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("list - item clicked", String.valueOf(parent.getItemAtPosition(position)));
        if (termsBol) {
            if (windowOpen) {
                closeWindow();
            } else {
                closeKeyboard();
                openWindow(String.valueOf(parent.getItemAtPosition(position)));
            }
        }
        else {
            if (windowOpen) {
                closeWindow();
            } else {
                sylCounter2 = position;
                openWindow(String.valueOf(parent.getItemAtPosition(position)));
            }
        }
    }

    private void openWindow(String definition) {
        windowOpen = true;
        if (termsBol) {
            defName = definition;
            defText.setText(tutorialData.getMap().get(defName).get(0));
        }
        else {
            defName = definition.substring(3);
            defText.setText(tutorialData.getMap2().get(defName).get(0));
        }
        def.setText(defName);
        scrollView.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.VISIBLE);
    }

    private void closeWindow() {
        scrollView.setVisibility(View.INVISIBLE);
        windowOpen = false;
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}