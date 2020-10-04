package com.example.fallingwords.ui.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fallingwords.R
import com.example.fallingwords.ui.MainViewModel
import com.example.fallingwords.util.UserResponse
import com.example.fallingwords.util.WORD_FALLING_DURATION
import kotlinx.android.synthetic.main.fragment_game.*


class GameFragment : Fragment() {

    private var objectFallingAnimator: ObjectAnimator? = null
    lateinit var viewModel: MainViewModel

    private val wordAnimationScaleListener = object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            //do nothing
        }

        override fun onAnimationEnd(p0: Animation?) {
            startFallingAnswer()
        }

        override fun onAnimationRepeat(p0: Animation?) {
            //do nothing
        }
    }

    private val answerImgAnimationScaleListener = object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            img_answer.visibility = View.GONE
            viewModel.showNewWord()
        }

        override fun onAnimationRepeat(p0: Animation?) {
            //do nothing
        }
    }

    private val objectFallingAnimationListener = object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            falling_word.visibility = View.VISIBLE
            viewModel.startTimer()
        }

        override fun onAnimationEnd(animation: Animator) {
            falling_word.visibility = View.GONE
            viewModel.stopTimer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initListener()
        viewModel.startGame()
        viewModel.getSession()
    }

    private fun initListener() {
        btn_correct.setOnClickListener {
            viewModel.onCorrectButtonClicked()
        }

        btn_wrong.setOnClickListener {
            viewModel.onWrongButtonClicked()
        }

        btn_restart.setOnClickListener {
            onGameRestart()
        }
    }

    private fun initObserver() {

        viewModel.activeWord.observe(viewLifecycleOwner, Observer {
            toggleButtonState(true)
            showNewWord()
        })

        viewModel.liveTimer.observe(viewLifecycleOwner, Observer {
            timer.text = it?.toString()
        })

        viewModel.userSelectedAnswer.observe(viewLifecycleOwner, Observer {
            toggleButtonState(false)
            stopFallingAnimation()
            viewModel.getSession()
            when (it) {
                UserResponse.CORRECT -> img_answer.setImageResource(R.drawable.ic_correct)
                UserResponse.WRONG -> img_answer.setImageResource(R.drawable.ic_wrong)
                UserResponse.UNATTEMPTED -> img_answer.setImageResource(R.drawable.ic_wrong)
            }
            val animation: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
            animation.setAnimationListener(answerImgAnimationScaleListener)
            img_answer.visibility = View.VISIBLE
            img_answer.startAnimation(animation)
        })

        viewModel.session.observe(viewLifecycleOwner, Observer { session ->
            words.text = String.format(
                getString(R.string.text_words),
                "${session?.attempted}/${session?.WordsPerSession}"
            )
            score.text = String.format(getString(R.string.txt_score), session?.correct)
        })

        viewModel.isGameOver.observe(viewLifecycleOwner, Observer {
            onGameOver()
        })

    }

    private fun onGameRestart() {
        action_view.visibility = View.VISIBLE
        toggleButtonState(true)
        layout_restart.visibility = View.GONE
        viewModel.restartGame()
        viewModel.showNewWord()
        viewModel.getSession()
    }

    private fun onGameOver() {
        word.visibility = View.GONE
        falling_word.visibility = View.GONE
        val animation: Animation =
            AnimationUtils.loadAnimation(context, R.anim.slide_up)
        layout_restart.visibility = View.VISIBLE
        layout_restart.startAnimation(animation)
        action_view.visibility = View.GONE
    }

    private fun toggleButtonState(isClickable: Boolean) {
        btn_wrong.isClickable = isClickable
        btn_correct.isClickable = isClickable
    }

    private fun showNewWord() {
        word.visibility = View.VISIBLE
        word.text = viewModel.activeWord.value?.text_eng
        val animation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
        animation.setAnimationListener(wordAnimationScaleListener)
        animation.fillAfter = true
        word.startAnimation(animation)
    }

    override fun onResume() {
        super.onResume()
        viewModel.showNewWord()
    }

    override fun onPause() {
        super.onPause()
        stopFallingAnimation()
    }

    private fun startFallingAnimation() {
        objectFallingAnimator?.start()
    }

    private fun stopFallingAnimation() {
        objectFallingAnimator?.cancel()
    }

    private fun startFallingAnswer() {
        falling_word.visibility = View.VISIBLE
        falling_word.text = viewModel.activeWord.value?.randomTranslatedWord
        val yValueStart = score_view.height
        val yValueEnd: Int =
            this.view?.bottom?.minus((falling_word.height + action_view.height + score_view.height))
                ?: 0
        stopFallingAnimation()
        objectFallingAnimator = ObjectAnimator.ofFloat(
            falling_word,
            View.TRANSLATION_Y,
            yValueStart.toFloat(),
            yValueEnd.toFloat()
        ).setDuration(WORD_FALLING_DURATION.toLong())
        objectFallingAnimator?.interpolator = LinearInterpolator()
        objectFallingAnimator?.addListener(objectFallingAnimationListener)
        startFallingAnimation()
    }


}