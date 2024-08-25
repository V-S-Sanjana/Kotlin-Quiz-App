package com.example.quizapp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var questionNumberTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var questionImageView: ImageView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var option1RadioButton: RadioButton
    private lateinit var option2RadioButton: RadioButton
    private lateinit var option3RadioButton: RadioButton
    private lateinit var option4RadioButton: RadioButton
    private lateinit var nextButton: Button
    private lateinit var submitButton: Button
    private lateinit var scoreSection: LinearLayout

    private val questions = listOf(
        Question("Which is the deepest point from the sea level on the Earth?", listOf("North Channel", "Pacific Ocean", "Mariana Trench", "Red Sea"), 2, R.drawable.marina),
        Question("Which is the largest planet in our solar system?", listOf("Mars", "Jupiter", "Venus", "Earth"), 1, R.drawable.jupiter),
        Question("Who painted the Mona Lisa?", listOf("Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh", "Michelangelo"), 0, R.drawable.monaa),
        Question("What is the capital of France?", listOf("London", "Berlin", "Paris", "Rome"), 2, R.drawable.france),
        Question("Who wrote 'Romeo and Juliet'?", listOf("Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"), 1, R.drawable.rmjl),
        Question("What is the powerhouse of the cell?", listOf("Nucleus", "Mitochondria", "Ribosome", "Chloroplast"), 1, R.drawable.mito),
        Question("Which country won the FIFA World Cup in 2018?", listOf("Brazil", "Germany", "France", "Argentina"), 2, R.drawable.football),
        Question("Which planet is covered by clouds of sulphuric acid?", listOf("Uranus", "Mercury", "Saturn", "Venus"), 3, R.drawable.venus),
        Question("Who is the CEO of Tesla?", listOf("Jeff Bezos", "Elon Musk", "Bill Gates", "Mark Zuckerberg"), 1, R.drawable.tesla),
        Question("Which programming language is developed by JetBrains?", listOf("Java", "Kotlin", "Python", "C#"), 1, R.drawable.jetb)
    )

    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timerTextView)
        questionNumberTextView = findViewById(R.id.questionNumberTextView)
        questionTextView = findViewById(R.id.questionTextView)
        questionImageView = findViewById(R.id.questionImageView)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        option1RadioButton = findViewById(R.id.option1RadioButton)
        option2RadioButton = findViewById(R.id.option2RadioButton)
        option3RadioButton = findViewById(R.id.option3RadioButton)
        option4RadioButton = findViewById(R.id.option4RadioButton)
        nextButton = findViewById(R.id.nextButton)
        submitButton = findViewById(R.id.submitButton)
        scoreSection = findViewById(R.id.scoreSection)

        startTimer()
        showQuestion()

        nextButton.setOnClickListener {
            checkAnswer()
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                showQuestion()
                if (currentQuestionIndex == questions.size - 1) {
                    nextButton.visibility = android.view.View.GONE
                    submitButton.visibility = android.view.View.VISIBLE
                }
            }
        }

        submitButton.setOnClickListener {
            countDownTimer.cancel() // Stop the timer on submission
            calculateScore()
            showFinalScore()
            nextButton.visibility = android.view.View.GONE
            submitButton.visibility = android.view.View.GONE
        }

        val restartButton: Button = findViewById(R.id.restartButton)
        restartButton.setOnClickListener {
            currentQuestionIndex = 0
            score = 0
            startTimer()
            showQuestion()
            resetQuiz()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerTextView.text = "Time remaining: $secondsRemaining s"
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!!!"
                calculateScore()
                showFinalScore()
                nextButton.visibility = android.view.View.GONE
                submitButton.visibility = android.view.View.GONE
            }
        }.start()
    }

    private fun showQuestion() {
        val question = questions[currentQuestionIndex]
        questionNumberTextView.text = "Question ${currentQuestionIndex + 1} of ${questions.size}"
        questionTextView.text = question.question
        questionImageView.setImageResource(question.imageResId)
        option1RadioButton.text = question.options[0]
        option2RadioButton.text = question.options[1]
        option3RadioButton.text = question.options[2]
        option4RadioButton.text = question.options[3]
        optionsRadioGroup.clearCheck()

        nextButton.visibility = android.view.View.VISIBLE
        submitButton.visibility = android.view.View.GONE
        scoreSection.visibility = android.view.View.GONE
        optionsRadioGroup.visibility = android.view.View.VISIBLE
    }

    private fun checkAnswer() {
        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
        val correctAnswerIndex = questions[currentQuestionIndex].correctAnswerIndex

        if (selectedOptionId != -1) {
            val selectedOption = optionsRadioGroup.findViewById<RadioButton>(selectedOptionId)
            val selectedAnswerIndex = optionsRadioGroup.indexOfChild(selectedOption)

            if (selectedAnswerIndex == correctAnswerIndex) {
                score++
            }
        }
    }

    private fun calculateScore() {
        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
        val correctAnswerIndex = questions[currentQuestionIndex].correctAnswerIndex

        if (selectedOptionId != -1) {
            val selectedOption = optionsRadioGroup.findViewById<RadioButton>(selectedOptionId)
            val selectedAnswerIndex = optionsRadioGroup.indexOfChild(selectedOption)

            if (selectedAnswerIndex == correctAnswerIndex) {
                score++
            }
        }
    }

    private fun showFinalScore() {
        scoreSection.visibility = View.VISIBLE
        optionsRadioGroup.visibility = View.GONE
        questionNumberTextView.visibility = View.GONE
        questionTextView.visibility = View.GONE
        questionImageView.visibility = View.GONE
        option1RadioButton.visibility = View.GONE
        option2RadioButton.visibility = View.GONE
        option3RadioButton.visibility = View.GONE
        option4RadioButton.visibility = View.GONE

        val finalScoreTextView: TextView = findViewById(R.id.finalScoreTextView)
        finalScoreTextView.text = "Your Score: $score / ${questions.size}"

        // Display correct answers
        val correctAnswersTextView: TextView = findViewById(R.id.correctAnswersTextView)
        correctAnswersTextView.visibility = View.VISIBLE
        val answersStringBuilder = StringBuilder()
        for (i in questions.indices) {
            answersStringBuilder.append("Question ${i + 1}: ${questions[i].options[questions[i].correctAnswerIndex]}\n")
        }
        correctAnswersTextView.text = answersStringBuilder.toString()
    }


    private fun resetQuiz() {
        questionNumberTextView.visibility = android.view.View.VISIBLE
        questionTextView.visibility = android.view.View.VISIBLE
        questionImageView.visibility = android.view.View.VISIBLE
        option1RadioButton.visibility = android.view.View.VISIBLE
        option2RadioButton.visibility = android.view.View.VISIBLE
        option3RadioButton.visibility = android.view.View.VISIBLE
        option4RadioButton.visibility = android.view.View.VISIBLE
        optionsRadioGroup.visibility = android.view.View.VISIBLE

        scoreSection.visibility = android.view.View.GONE
        nextButton.visibility = android.view.View.VISIBLE
        submitButton.visibility = android.view.View.GONE
    }
}

data class Question(val question: String, val options: List<String>, val correctAnswerIndex: Int, val imageResId: Int)
