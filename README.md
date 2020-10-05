# The Falling Words.

## Author:- Sachin Rana
email: <sachinrana135@gmail.com>

## Overview
The Falling Words is a language game. When user clicks on the play button one can see a words (in English) on the screen.
Another word (in Spanish) will start falling down from top of the screen. User has to figure out if the translation is correct or not.
If the user doesn't take any action and word get fallen out at bottom of screen, it will be considered wrong answer. User will be shown total 10 words and will be given 5 seconds to guess the answer for one word.


## Tech/framework used

1.	MVVM – Architecture
2.	Kotlin - Language
3.	Coroutine – For asynchronous calls
4.	Android Naviagation comoponent 
5.  jUnit 4
6.  Mockito
7.  Gson
  
## About Development 
 ### Total Time Spent: 6-7 Hours
 - Spent 1 hour to analyze the requirement and think about the app architecture.
 - Spent roughly 30-45 hour setting up the app architecture.
 - Spent roughly 30-45 minutes in UI design. (UI can be improved but I focused more on business login instead of design part.)
 - Spent roughly 30-45 minutes in UI animation
 - Spent roughly 3-4 hour in writing the business logic and events handling.
 - Spent roughly 1 hour in writing Unit test case

 ### Decisions made to solve certain aspects of the game
 - Implemented GameManager that manage the game session and make sure no single word will be shown twice to user in a session.
 - Game manager also manage the number of answer options out of the falling word will be chosen.For example, if you set it to 3, then answer options will contain      the one correct and 2 wrong spanish translation. So the correctness of falling word will become 33.33%. If you set it 4, correctness of falling word will become    25%.
 - Shown animation to user in real time if the answer is correct or wrong.
 
 ### decisions made because of restricted time
 - Avoided network call and read the word file locally.
 - Focus mainly on business logic and given less time on UI/UX

 ### Things To Improve
 - The UI/UX can definitely be improved.
 - If the answer is wrong, correct answer should be shown to user by some visual. It will help user to learn the translations.
 
## Video

<img src="https://github.com/sachinrana135/FallingWordsGame/blob/main/Falling_word.gif" width="240" height="427">
