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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.fallingwords.R
import com.example.fallingwords.ui.MainViewModel
import com.example.fallingwords.util.UserResponse
import com.example.fallingwords.util.WORD_FALLING_DURATION
import kotlinx.android.synthetic.main.fragment_game.*


class GameFragment : Fragment() {

    private var objectFallingAnimator: ObjectAnimator? = null
    private val viewModel: MainViewModel by activityViewModels()

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
    }

    private fun initObserver() {

        viewModel.activeWord.observe(viewLifecycleOwner, Observer {
            showNewWord()
        })

        viewModel.liveTimer.observe(viewLifecycleOwner, Observer {
            timer.text = it?.toString()
        })

        viewModel.userSelectedAnswer.observe(viewLifecycleOwner, Observer {
            stopFallingAnimation()
            viewModel.getSession()
            viewModel.showNewWord()
            when (it) {
                UserResponse.CORRECT -> {

                }

                UserResponse.WRONG -> {

                }

                UserResponse.UNATTEMPTED -> {

                }
            }

            viewModel.showNewWord()

        })

        viewModel.session.observe(viewLifecycleOwner, Observer { session ->
            words.text = String.format(
                getString(R.string.text_words),
                "${session?.attempted}/${session?.WordsPerSession}"
            )
            score.text = String.format(getString(R.string.txt_score), session?.correct)
        })

        viewModel.isGameOver.observe(viewLifecycleOwner, Observer {
            // todo show result 
        })

    }

    private fun showNewWord() {
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