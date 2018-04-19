Alex Ruth SID: 216106075

Compile instructions:
- Software: Android Studio
- Emulation: Run app (Shift +F10)

Directory structure:
- Images:           app\src\main\res\drawable
- JSON file/s:      app\src\main\assets
- Java files:       app\src\main\java\com\example\alexuni\sit305ass2
- XML layout files: app\src\main\res\layout

———————————————————————————————————————————————————————————————————————

# Henry comments 13/April
- Your JSON test file isn't in the correct syntax :) . Use a JSON validator online to check it.
- Not enough commits + changelog items to pass at this frequency.
- You're still missing a licenses.txt file.


# Henry comments 17/April
- Great fixes. May I make one suggestion. When doing conversations, if you want them to be in order, use an array, not a map with keys.
Not this:
  "introText1": "Hello there, are you okay? You seemed to have crashed your plane.",
  "introText2": "... Ugh, where am I?",
  
But this:
  "Hello there, are you okay? You seemed to have crashed your plane.",
  "... Ugh, where am I?",
  
Or even this:

  {"id": "introText1", "character": "xx", "text": "Hello there, are you okay? You seemed to have crashed your plane."},
  {"id": "introText2", "character": "xx", "text": "... Ugh, where am I?"},
  
... that might make it easier for you to then go through each conversation and maybe have options in each speech line to go to other lines. E.g.
  {"id": "introText2", "character": "xx", "text": "... Ugh, where am I?", "answers": [{"I want out of here": "introText2", "I want to buy ice cream": "shopLine1"]},
  
Maybe something like that. There are many ways to do it, it just depends on what information would be most helpful to you for each line of speech, and the speech overall.


## Thanks Henry! I appreciate the suggestions! Implementing it has significantly reduced  the amount of code I use.

