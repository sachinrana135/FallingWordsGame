package com.example.fallingwords.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.fallingwords.R
import com.example.fallingwords.data.model.Word
import com.example.fallingwords.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_game.*


class GameFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

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
        viewModel.startGame()

    }

    private fun initObserver() {

        viewModel.activeWord.observe(viewLifecycleOwner, Observer {
            showNewWord(it)
        })

    }

    private fun showNewWord(newWord: Word?) {
        word.text = newWord?.text_eng
        val animation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
        animation.fillAfter = true
        word.startAnimation(animation)
        //word.setVisibility(View.VISIBLE)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}